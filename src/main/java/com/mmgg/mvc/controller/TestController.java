package com.mmgg.mvc.controller;

import com.mmgg.annotation.Autowried;
import com.mmgg.annotation.Controller;
import com.mmgg.annotation.RequestMapping;
import com.mmgg.annotation.RequestParam;
import com.mmgg.mvc.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping("Test")
@Controller
public class TestController {

    @Autowried
    private TestService testService;

    @RequestMapping("get")
    public void query(HttpServletRequest request, HttpServletResponse response,
         @RequestParam("name")String name){
       String str = testService.getTestService();
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
