package com.beanfarm.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "server")
public record ServerConfiguration (
     String host,
     int port,                      
     String context,
     String redirectUri ,
     int timeout,
     String id,
     String secret){

}
