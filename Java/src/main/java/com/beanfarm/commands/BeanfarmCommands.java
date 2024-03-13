package com.beanfarm.commands;

import java.net.URISyntaxException;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import com.beanfarm.auth.AuthHttpServer;
import com.beanfarm.auth.ClientConfiguration;
import com.beanfarm.auth.ServerConfiguration;
import com.beanfarm.client.BeanfarmClient;
import com.beanfarm.model.BeanfarmResponse;

@ShellComponent
public class BeanfarmCommands {

    private final BeanfarmClient beanfarmClient;
    private AuthHttpServer authHttpServer;

    public BeanfarmCommands(BeanfarmClient beanfarmClient) {
        this.beanfarmClient = beanfarmClient;
    }

    @ShellMethod(key = "employees",value = "I will hit the api/employees?access_token")
    public String getRandomDadJoke() {
        BeanfarmResponse random = beanfarmClient.random(authHttpServer.getAccessToken());
        return random.message();
    }
    @ShellMethod(key = "authenticate", value ="Sign into the beanfarm using Google")
    public String authenticate() throws URISyntaxException{
        System.out.println("Click on the link to start authentication:\n");
        authHttpServer = new AuthHttpServer(new ServerConfiguration());
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        System.out.println(clientConfiguration.generateAuthUri(authHttpServer.config.getRedirectUri()));
        authHttpServer.start();
        return "";
    }

    @ShellMethod(key = "accessToken",value = "I will return the access token of the authenticated user")
    public String getAcceToken() {
        return authHttpServer.getAccessToken();
    }

}
