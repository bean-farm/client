package com.beanfarm.auth;

import picocli.CommandLine.Command;

import com.beanfarm.beanfarmRunner.BeanfarmController;

import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;

@Command(name = "auth")
final public class AuthCommand implements Runnable {

    final ClientConfiguration clientConfiguration;
    final AuthHttpServer authHttpServer;

    @Inject
    public AuthCommand(ClientConfiguration clientConfiguration, AuthHttpServer authHttpServer) {
        this.clientConfiguration = clientConfiguration;
        this.authHttpServer = authHttpServer;
    }

    @Override
    public void run() {
        displayAuthUrl();

        handleRedirectUri();

        //PicocliRunner.run(BeanfarmController.class, authHttpServer.getAccessToken(), "" );
    }

    private void handleRedirectUri() {
        authHttpServer.start();
    }

    private void displayAuthUrl() {
        System.out.println("Click on the link to start authentication:\n");
        System.out.println(clientConfiguration.generateAuthUri(authHttpServer.config.getRedirectUri()));
    }
}
