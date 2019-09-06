package com.mmgg.mvc.serviceImpl;

import com.mmgg.annotation.Service;
import com.mmgg.mvc.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String getTestService() {
        return "success";
    }
}
