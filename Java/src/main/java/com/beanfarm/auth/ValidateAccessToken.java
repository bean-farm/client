package com.beanfarm.auth;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.URI;
import java.net.URISyntaxException;


public class ValidateAccessToken {
    
    public static boolean validate (String token){
        try {
        URI accessTokenURL = new URI("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token="+token);
        HttpURLConnection conn = (HttpURLConnection) accessTokenURL.toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int status = conn.getResponseCode();
        System.out.println(status);
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        String userID = "";
        
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
            if (responseLine.contains("\"user_id\":")){
                userID = responseLine.substring(response.lastIndexOf("\"user_id\":\"")+14, responseLine.length()-2).replaceAll("\"","");
                System.out.println(userID);
                return true;
            }
        }
        return false;


    } catch ( URISyntaxException| IOException e) {
       return false;
    }

    }


}
