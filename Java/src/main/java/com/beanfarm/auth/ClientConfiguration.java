package com.beanfarm.auth;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.uri.UriBuilder;

import java.net.URI;



@Introspected
@ConfigurationProperties("beanfarm.auth.client")
final class ClientConfiguration {
    private String url = "https://accounts.google.com/" ;
    private String clientId = System.getenv("CLIENT_ID");
    private String scope = "profile";
    private String responseType = "code";

    public URI generateAuthUri(String redirectUri) {
        return UriBuilder.of(url)
                .path("/o/oauth2/auth")
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", responseType)
                .build();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
