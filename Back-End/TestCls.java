/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uga.ei_final;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author agashe
 */
public class TestCls {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
                    final String BASE_URI = "http://localhost:8080/EI_Final/api";
        
            String name1 = "akshay";
//            out.print(name1);
                        try{
                    //Creating the RESTeasy Client
//                    out.println(BASE_URI+"/jobs/"+name1+"/abc");
		ClientRequest request1 = new ClientRequest(BASE_URI+"/jobs/"+name1+"/abc");
//                out.println("hey");
                
		request1.accept("text/plain");
		ClientResponse<String> response1 = request1.get(String.class);
		if (response1.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ response1.getStatus());
		}
                
                BufferedReader br = new BufferedReader(new InputStreamReader(
			new ByteArrayInputStream(response1.getEntity().getBytes())));

                String jsonText = SupportClass.readAll(br);
                    
                System.out.print(jsonText);
                

                } catch(Exception e) {
                            System.out.println(e);
                }
        
        
        
    }
    
}
