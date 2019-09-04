package com.mmgg;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public void init() throws ServletException {
        //开始初始化进程

        //定位

        //加载

        //注册

        //自动依赖注入
    }
}
