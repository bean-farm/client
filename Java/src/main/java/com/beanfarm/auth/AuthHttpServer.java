package com.beanfarm.auth;

import com.sun.net.httpserver.HttpServer;


import java.net.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;

import java.net.URLDecoder;



final public class AuthHttpServer {

    final public ServerConfiguration config;
    private String accessToken;
    


    public AuthHttpServer(ServerConfiguration config) {
        this.config = config;
    }
    public String getAccessToken(){
        if (accessToken != null){
        return accessToken;
        }
        else return "";
    }
    public void setAccesstoken(String accessToken){
        this.accessToken=accessToken;
    }
   
    public String start(){


        try {
            var latch = new CountDownLatch(1);
            var server = HttpServer.create(new InetSocketAddress(config.host(), config.port()), 0);
            server.createContext(config.context(), exchange -> {
                var code = exchange.getRequestURI().toString().split("=")[1].split("&")[0];
                System.out.println("Code retrieved. Exchanging for an access token...");

                
                try {
                    URI accessTokenURL = new URI("https://oauth2.googleapis.com/token"+"?client_id="+config.id()+"&client_secret="+config.secret()+"&redirect_uri=http://localhost:8081/auth&grant_type=authorization_code&code="+URLDecoder.decode(code, StandardCharsets.UTF_8));
                    System.out.println(accessTokenURL);
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
                    accessToken = "";
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                        if (responseLine.contains("\"access_token\":")){
                            accessToken = responseLine.substring(response.lastIndexOf("\"access_token\": \"")+17, responseLine.length()-1).replaceAll("\"","");
                            System.out.println("Access token retrieved!");
                        }
                    }

                    //Credentials.store(accessToken);

                    var message = "Success! Authentication completed. You can close web browser and return to the terminal window.";

                    exchange.sendResponseHeaders(200, message.length());
                    exchange.getResponseBody().write(message.getBytes(StandardCharsets.UTF_8));

                    latch.countDown();
                } catch (URISyntaxException e) {
                        
                    e.printStackTrace();
                }
            
            });
            server.start();
            System.out.println("Waiting for redirect URI...");
            latch.await(config.timeout(), TimeUnit.SECONDS);
            server.stop(0);
            if (accessToken == null || accessToken == ""){
                System.out.println("Authentication failed");
            } else{
                System.out.println("Success! You are now authenticated!");
            }
            return accessToken;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
