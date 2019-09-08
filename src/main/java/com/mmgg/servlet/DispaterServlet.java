package com.mmgg.servlet;

import com.mmgg.annotation.Autowried;
import com.mmgg.annotation.Controller;
import com.mmgg.annotation.Service;
import com.mmgg.mvc.controller.TestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 161196
 */
public class DispaterServlet extends HttpServlet {

    private Properties contextConfig = new Properties();
    private Map<String,Object> beanMap = new ConcurrentHashMap<String,Object>();
    private List<String> classNames = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=============调用doPost==============");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //开始初始化进程

        //定位
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        //加载
        doScanner(contextConfig.getProperty("scanPackage"));

        //注册
        doRegistry();

        //自动依赖注入
        //在spring中是通过调用getBean方法才触发依赖注入的
        doAutowried();
        TestController testController = (TestController) beanMap.get("testController");
        testController.query(null,null,"wangxinjie");

        //如果是springMvc会多设计一个HanndlerMapping

        //将@RequestMapping中配置的url和一个Method关联上
        //以便从浏览器获得用户输入的url能找到具体的Method通过反射的调用
        initHandlerMapping();
    }

    private void initHandlerMapping() {
    }

    private void doRegistry() {
        if(classNames.isEmpty()){
            return;
        }
        try {
            for(String className: classNames){
                Class<?> clazz = Class.forName(className);
                //在spring中用的多个子方法来处理
                if (clazz.isAnnotationPresent(Controller.class)){
                    String beanName = lowerFisrtCase(clazz.getSimpleName());
                    //在spring中在这个阶段不会直接put instance,这里put的是BeanDefinition
                    beanMap.put(beanName,clazz.newInstance());

                }else if(clazz.isAnnotationPresent(Service.class)){

                   Service service = clazz.getAnnotation(Service.class);

                   //默认使用首字母注入
                   //如果自己定义的了beanName,那么优先使用自己定义的beanName
                   //如果是一个接口，使用接口的类型去自动注入

                   //在Spring中同样分别调用不同的方法 autowriedByBame autowiredByType
                   String beanName = service.value();
                   if("".equals(beanName.trim())){
                       beanName = lowerFisrtCase(clazz.getSimpleName());

                       Object instance = clazz.newInstance();
                       beanMap.put(beanName,instance);
                       Class<?>[] interfaces = clazz.getInterfaces();
                       for(Class<?> i:interfaces){
                           beanMap.put(i.getName(),instance);
                       }
                   }

                }else{
                    continue;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doAutowried() {
        if(beanMap.isEmpty()){return;}

        for (Map.Entry<String,Object> entry : beanMap.entrySet()){
           Field[] files = entry.getValue().getClass().getDeclaredFields();
           for(Field field : files){
               if(!field.isAnnotationPresent(Autowried.class)){
                    continue;
               }
               Autowried autowried = field.getAnnotation(Autowried.class);
               String beanName = autowried.value().trim();
               if("".equals(beanName)){
                   beanName = field.getType().getName();
               }
               field.setAccessible(true);
               try {
                   field.set(entry.getValue(),beanMap.get(beanName));
               } catch (IllegalAccessException e) {
                   e.printStackTrace();
               }
           }
        }
    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.","/"));
        File classDir= new File(url.getFile());

        for(File file : classDir.listFiles()){
            if(file.isDirectory()){
                doScanner(packageName+"."+file.getName());
            }else{
                classNames.add(packageName+"."+file.getName().replaceAll(".class",""));
            }
        }
    }

    private void doLoadConfig(String location) {
        //在spring中是通过Reader去查找和定位
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:",""));
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String lowerFisrtCase(String str){
        char[] chars = str.toCharArray();
        chars[0] +=32;
        return String.valueOf(chars);
    }

}
