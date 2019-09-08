package com.mmgg.mvc.service.serviceImpl;

import com.mmgg.annotation.Service;
import com.mmgg.mvc.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String getTestService(String name) {
        return "My name is:"+name;
    }
}
