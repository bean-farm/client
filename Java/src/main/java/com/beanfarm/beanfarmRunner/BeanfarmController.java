
package com.beanfarm.beanfarmRunner;


import java.net.URL;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import picocli.CommandLine.Command;

import java.net.HttpURLConnection;
import java.util.Scanner;

@Command(name = "beanfarm", description = "The main beanfarm functionality", mixinStandardHelpOptions = true)
final public class BeanfarmController implements Runnable {

    @Override
    public void run(){
     
        System.out.println("What do you want to do?");

        String command = "";
        do {
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine();

        } while (command != "exit");

        try {
        String accessToken = "";
        URL url = new URL("http://localhost:8080/api/employees?access_token="+accessToken);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responsecode = conn.getResponseCode();
        System.out.println(responsecode);
        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {

            String inline = "";
            Scanner scanner = new Scanner(url.openStream());
        
        //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
            inline += scanner.nextLine();
            }
        
        //Close the scanner
        scanner.close();

        //Using the JSON simple library parse the string into a json object
        JSONParser parse = new JSONParser();
        JSONObject data_obj = (JSONObject) parse.parse(inline);   
        System.out.println(data_obj);

        JSONArray list =  (JSONArray) data_obj.get("items");
        //list.stream().map(System.out::println);
        }
    } catch (Exception e) {
        // TODO: handle exception
        System.err.println("shit");
    }
    

    }
}
