package com.beanfarm.commands;

import java.net.URISyntaxException;
import java.io.FileNotFoundException;

import org.apache.el.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import com.beanfarm.auth.AuthHttpServer;
import com.beanfarm.auth.ClientConfiguration;
import com.beanfarm.auth.ServerConfiguration;
import com.beanfarm.auth.ValidateAccessToken;
import com.beanfarm.client.BeanfarmClient;
import com.beanfarm.model.ActionResponse;
import com.beanfarm.model.BeanfarmResponse;

import com.beanfarm.model.ProfilePost;
import com.beanfarm.model.ProfileResponse;
import com.beanfarm.auth.Credentials;
import java.util.List;
import java.util.Scanner;
@ShellComponent
public class BeanfarmCommands {

    private final BeanfarmClient beanfarmClient;
    private final ServerConfiguration serverConfig;
    private final ClientConfiguration clientConfig;
    private AuthHttpServer authHttpServer;
    

    public BeanfarmCommands( BeanfarmClient beanfarmClient, ServerConfiguration serverConfig, ClientConfiguration clientConfig) {
        this.beanfarmClient = beanfarmClient;
        this.serverConfig = serverConfig;
        this.clientConfig = clientConfig;
        System.out.println("************************************");
        System.out.println("*        Virtual Bean Farm         *");
        System.out.println("************************************");
    }


    public String authenticate() throws URISyntaxException{
        authHttpServer = new AuthHttpServer(serverConfig);
        try {
            String accessToken = Credentials.fetch();
            
            if (ValidateAccessToken.validate(accessToken)) {
                authHttpServer.setAccesstoken(accessToken);
                System.out.println("Success! You are now authenticated from the stored credentials");
                return "";
            }

        } catch (Exception e) {
        
        }
        System.out.println("Click on the link to start authentication:\n");
        System.out.println(clientConfig.generateAuthUri(authHttpServer.config.redirectUri()));
        authHttpServer.start();

        return "";
    }


    @ShellMethod(key = "getPlots",value = "Return a list of all your plots and their status")
    public String getPlots() {
        List<String> actions = beanfarmClient.getUsersPlots(authHttpServer.getAccessToken());
        actions.stream().forEach(System.out::println);
        return "";
    }

    @ShellMethod(key = "listBeans",value = "Return a list of all your beans and the quantity you have to plant")
    public String listBeans() {
        List<String> response = beanfarmClient.listBeans(authHttpServer.getAccessToken());
        response.stream().forEach(System.out::println);
        return "";
    }

    @ShellMethod(key = "signIn",value = "Creates a user or logs you in if you have one")
    public String signIn()  throws URISyntaxException {
        String response = authenticate();
        System.out.println("Please enter your name");
        Scanner s = new Scanner(System.in);
        String name = s.nextLine();
        System.out.println("Access token:" + authHttpServer.getAccessToken());
        ProfileResponse profileResponse = beanfarmClient.createProfile(authHttpServer.getAccessToken(), new ProfilePost(name));
        return "Logged in with user id: "+ profileResponse.profileId() +"\nWelcome "+ profileResponse.profileName();
    }

    @ShellMethod(key = "plantBean",value = "Plant a bean")
    public String plantBean() {
        Scanner s = new Scanner(System.in);
        System.out.println("Please enter plot id");
        long plotId = Long.parseLong(s.nextLine());
        System.out.println("Please enter bean type id");
        long beanTypeId =  Long.parseLong(s.nextLine());
        List<String> response = beanfarmClient.plantBean(authHttpServer.getAccessToken(), plotId, beanTypeId);
        response.stream().forEach(System.out::println);
        return "";
    }
    @ShellMethod(key = "waterPlot",value = "Water a plot")
    public String waterPlot() {
        Scanner s = new Scanner(System.in);
        System.out.println("Please enter plot id");
        long beanTypeId =  Long.parseLong(s.nextLine());
        List<String> response = beanfarmClient.waterPlot(authHttpServer.getAccessToken(), beanTypeId);
        response.stream().forEach(System.out::println);
        return "";
    }

    @ShellMethod(key = "harvestPlot",value = "Harvest beans from a plot")
    public String harvestPlot() {
        Scanner s = new Scanner(System.in);
        System.out.println("Please enter plot id");
        long beanTypeId =  Long.parseLong(s.nextLine());
        List<String> response = beanfarmClient.harvestPlot(authHttpServer.getAccessToken(), beanTypeId);
        response.stream().forEach(System.out::println);
        return "";
    }
}

