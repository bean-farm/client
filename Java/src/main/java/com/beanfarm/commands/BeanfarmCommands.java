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

    private final BeanfarmClient dadJokeClient;

    public BeanfarmCommands(BeanfarmClient dadJokeClient) {
        this.dadJokeClient = dadJokeClient;
    }

    @ShellMethod(key = "random",value = "I will return a random dad joke!")
    public String getRandomDadJoke() {
        BeanfarmResponse random = dadJokeClient.random();
        return random.joke();
    }
    @ShellMethod(key = "authenticate", value ="Sign into the beanfarm using Google")
    public String authenticate() throws URISyntaxException{
        System.out.println("Click on the link to start authentication:\n");
        AuthHttpServer authHttpServer = new AuthHttpServer(new ServerConfiguration());
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        System.out.println(clientConfiguration.generateAuthUri(authHttpServer.config.getRedirectUri()));
        authHttpServer.start();

        return "Authed";
    }

}
