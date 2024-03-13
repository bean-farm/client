package com.beanfarm.auth;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "client")
public record ClientConfiguration(
     String url,
     String id,
     String scope,
     String responseType){

    public URI generateAuthUri(String redirectUri) throws URISyntaxException {
        return new URI(url+"o/oauth2/auth"+"?client_id="+id+"&scope="+scope+"&redirect_uri="+redirectUri+"&response_type="+responseType);
    }
}
