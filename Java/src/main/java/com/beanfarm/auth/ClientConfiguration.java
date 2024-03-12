package com.beanfarm.auth;

import java.net.URI;
import java.net.URISyntaxException;


final public class ClientConfiguration {
    private String url = "https://accounts.google.com/" ;
    private String clientId = System.getenv("CLIENT_ID");
    private String scope = "profile";
    private String responseType = "code";

    public URI generateAuthUri(String redirectUri) throws URISyntaxException {
        return new URI(url+"o/oauth2/auth"+"?client_id="+clientId+"&scope="+scope+"&redirect_uri="+redirectUri+"&response_type="+responseType);
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
