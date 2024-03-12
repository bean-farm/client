package com.beanfarm.model;

public record BeanfarmResponse(String message, String issued_to, String audience, String user_id, String scope, int expires_in, String access_type) {
}
