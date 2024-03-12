package com.beanfarm.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.beanfarm.model.BeanfarmResponse;

public interface BeanfarmClient {

    @GetExchange("api/employees")
    BeanfarmResponse random(@RequestParam(name = "access_token") String token);
}
