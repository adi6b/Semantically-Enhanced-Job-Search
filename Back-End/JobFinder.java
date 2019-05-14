/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uga.ei_final;


import static edu.uga.ei_final.Main.getNearLocations;
import static edu.uga.ei_final.Main.getSimilarPositions;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.json.simple.JSONArray;
//import org.json.JSONArray;
//import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author agashe
 */

@Path("/")
public class JobFinder {
    
    final static String JOB_LISTINGS = "src/main/java/JobListings.json";
    
    @GET
    @Path("/jobs/{pos}/{loc}")
    @Produces("text/plain")
    
//    String pos, String loc, String exp, String sal, String edu
    
    // URI for this call http://localhost:8080/EI_Final/api/jobs/hello/hi
    public String getJobs(@PathParam("pos") String pos,
                         @PathParam("loc") String loc)  {
        Main.codeSupporter();
        System.out.println(getNearLocations(loc));
        System.out.println(getSimilarPositions(pos));        
//        return movieTitle+" "+movieDesc;

        List<String> jobLocationsList = getNearLocations(loc);
        List<String> jobPositionsList = getSimilarPositions(pos);

        JSONParser parser = new JSONParser();
        try
        {
            Object unitsObj = parser.parse(new FileReader(JOB_LISTINGS));
            JSONArray unitsJson = (JSONArray) unitsObj;

            for (int i = 0; i < unitsJson.size(); i++) {
                JSONObject jsonobject = (JSONObject)unitsJson.get(i);
                
                if(jobLocationsList.contains(jsonobject.get("location").toString()) && jobPositionsList.contains(jsonobject.get("position").toString())){
                
                    String name = jsonobject.get("companyName").toString();
                    System.out.println(name + " "+jsonobject.get("location").toString()+" "+jsonobject.get("position").toString());
                }
                
            }
            
        }
        catch(Exception e1){
            System.out.println(e1);
        }
        
        

        return pos + " "+loc;
        
    }
    
    
    public static void main(String args[]) throws IOException, FileNotFoundException{
        
        JobFinder jf = new JobFinder();
        System.out.println(jf.getJobs("Sr_Software_Engineer", "Alphretta"));
//        JsonWorker();
        
    }
    
    
    public static void JsonWorker() throws FileNotFoundException, IOException{

    }
    
    
}
