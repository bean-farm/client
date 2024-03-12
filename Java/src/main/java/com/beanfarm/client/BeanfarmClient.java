package com.beanfarm.client;

import org.springframework.web.service.annotation.GetExchange;

import com.beanfarm.model.BeanfarmResponse;

public interface BeanfarmClient {

    @GetExchange("api/employees")
    BeanfarmResponse random();
}
