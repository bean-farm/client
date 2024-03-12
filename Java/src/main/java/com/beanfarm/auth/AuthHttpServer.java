package com.beanfarm.auth;

import com.sun.net.httpserver.HttpServer;

import io.micronaut.http.uri.UriBuilder;

import java.net.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import picocli.CommandLine.Help.Ansi;

import jakarta.inject.Inject;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.net.URLDecoder;




final class AuthHttpServer {

    final ServerConfiguration config;
    private String accessToken;
    @Inject
    public AuthHttpServer(ServerConfiguration config) {
        this.config = config;
    }
    public String getAccessToken(){
        if (accessToken != null){
        return accessToken;
        }
        else return "";
    }

    void start() {
        try {
            var latch = new CountDownLatch(1);
            var server = HttpServer.create(new InetSocketAddress(config.getHost(), config.getPort()), 0);
            server.createContext(config.getContext(), exchange -> {
                var code = exchange.getRequestURI().toString().split("=")[1].split("&")[0];
                System.out.println("Code retrieved. Exchanging for an access token..." + code); //TODO: remove code

                URI accessTokenURL = UriBuilder.of("https://oauth2.googleapis.com")
                .path("/token")
                .queryParam("client_id", System.getenv("CLIENT_ID"))
                .queryParam("client_secret", System.getenv("CLIENT_SECRET"))
                .queryParam("redirect_uri", "http://localhost:8081/auth")
                .queryParam("grant_type", "authorization_code")
                .queryParam("code", URLDecoder.decode(code, StandardCharsets.UTF_8))
                .build();

                HttpURLConnection conn = (HttpURLConnection) accessTokenURL.toURL().openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/x-www-form-urlencoded");
                conn.addRequestProperty("Content-Type", "*/*");
                conn.setRequestProperty("Content-length", "12");
                conn.setDoOutput(true);
                String reqBody = "{\"query\": \"\", \"variables\": \"\"}";
                OutputStream os = conn.getOutputStream();
                byte[] body = reqBody.getBytes("UTF-8");
                os.write( body, 0, body.length);

                conn.connect();
                int status = conn.getResponseCode();
                System.out.println(status);
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                String accessToken = "";
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                    if (responseLine.contains("\"access_token\":")){
                        accessToken = responseLine.substring(response.lastIndexOf("\"access_token\": \"")+17, responseLine.length()-1).replaceAll("\"","");
                        System.out.println("Access token retrieved!");
                    }
                }

                Credentials.store(accessToken);

                var message = "Success! Authentication completed. You can close web browser and return to the terminal window.";

                exchange.sendResponseHeaders(200, message.length());
                exchange.getResponseBody().write(message.getBytes(StandardCharsets.UTF_8));

                latch.countDown();
            });
            server.start();
            System.out.println("Waiting for redirect URI...");
            latch.await(config.getTimeout(), TimeUnit.SECONDS);
            server.stop(0);

            System.out.println(Ansi.ON.string("@|bold,fg(green) Success! You are now authenticated!|@"));

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
