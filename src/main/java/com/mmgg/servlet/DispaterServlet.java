package com.mmgg.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    private List<String> beanNames = new ArrayList<>();

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
        doScanner();

        //注册
        doRegistry();

        //自动依赖注入
        doAutowried();

        //如果是springMvc会多设计一个HanndlerMapping

        //将@RequestMapping中配置的url和一个Method关联上
        //以便从浏览器获得用户输入的url能找到具体的Method通过反射的调用
        initHandlerMapping();
    }

    private void initHandlerMapping() {
    }

    private void doRegistry() {
    }

    private void doAutowried() {
    }

    private void doScanner() {
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
}
