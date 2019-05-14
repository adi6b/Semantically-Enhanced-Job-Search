package edu.uga.ei_final;

/**
 * A program to extract semantic relations for job postings using Sparql and ontologies.
 * @author Gaurav Agarwal
 */
//package uga.ei.team4;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;

import java.util.*;

public class Main {

    final static String PREFIX_JOB_OWL = "http://www.semanticweb.org/gaurav/ontologies/2019/4/job-ontology#";
    final static String RDF_FILE = "src/main/java/Job_Ontology.owl";
    static Model model;

    public static void main(String[] args) {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        model = FileManager.get().loadModel(RDF_FILE);
        getSemantics("Sr_Software_Engineer", "Cupertino", "Entry_Level", "100000", "Masters");
    }
    
    public static void codeSupporter(){
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        model = FileManager.get().loadModel(RDF_FILE);
    }

    /**
     * This method runs the Sparql queries on the ontology and fetches the results
     * @param queryString sparql query
     * @param findParam the list of parameters to get from the result
     * @return aggregated results.
     */
    public static List<String> execSparql(String queryString, List<String> findParam){
        List<String> relationsFound = new ArrayList<>();

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try{
            ResultSet results = qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();
                for(String param: findParam) {
                    RDFNode className = soln.get(param);
                    relationsFound.add(className.toString().replace(PREFIX_JOB_OWL, ""));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            qexec.close();
            return relationsFound;
        }
    }

//    public static String execSparql(String queryString){
//        String relationFound = "";
//
//        Query query = QueryFactory.create(queryString);
//        QueryExecution qexec = QueryExecutionFactory.create(query, model);
//        try{
//            ResultSet results = qexec.execSelect();
//            while (results.hasNext()){
//                QuerySolution soln = results.nextSolution();
//                RDFNode className = soln.get("s");
//                relationFound = className.toString().replace(PREFIX_JOB_OWL, "");
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        } finally {
//            qexec.close();
//            return relationFound;
//        }
//    }

    /**
     * This method removes duplicate elements in the list maintaining the order of elements
     * @param originalList The List having duplicate elements
     * @return List of unique Strigns
     */
    public static List<String> removeDuplicates(List<String> originalList){
        Set<String> hset = new LinkedHashSet<>();
        hset.addAll(originalList);
        originalList.clear();
        originalList.addAll(hset);
        return originalList;
    }

    /**
     * This method finds in the ontology Locations near to the given location.
     * @param loc This is the location whose nearby locations you wish to find
     * @return a List of nearby locations
     */
    public static List<String> getNearLocations( String loc){
        List<String> locations = new ArrayList<>();
        locations.add(loc);
        String queryString =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX job-ontology: <"+ PREFIX_JOB_OWL +">" +
                        "SELECT * WHERE {" +
                        "job-ontology:"+loc + " job-ontology:isNearTo ?s ." +
                        "?locate job-ontology:isNearTo ?s" +
                        "}";
//        loc = execSparql(queryString);
//        if(!locations.contains(loc)){
//            locations.add(loc);
//        }
//        queryString =
//                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
//                        "PREFIX job-ontology: <"+ PREFIX_JOB_OWL +">" +
//                        "SELECT DISTINCT * WHERE {" +
//                        "?s job-ontology:isNearTo job-ontology:"+loc +
//                        "}";
        List<String> param = new ArrayList<>();
        param.add("locate");
        param.add("s");
        locations.addAll(execSparql(queryString, param));
        locations = removeDuplicates(locations);

        return  locations;
    }

    /**
     * This method gets the job similar to the given job name from the ontology
     * @param pos job position name you want to get matching results for.
     * @return a List of job positions similar to the given job position.
     */
    public static List<String> getSimilarPositions(String pos){
        List<String> jobs = new ArrayList<>();
        List<String> param = new ArrayList<>();

        jobs.add(pos);
        param.add("job");

        String queryString =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "PREFIX job-ontology: <"+ PREFIX_JOB_OWL +">" +
                "SELECT * WHERE {"+
                "?s job-ontology:similarTo job-ontology:" + pos + " ." +
                "?s job-ontology:similarTo ?job"+
                "}";
        param.add("s");

        jobs.addAll(execSparql(queryString, param));
        jobs = removeDuplicates(jobs);

        return  jobs;
    }


    public static List<String> getSimilarExpJob(String exp){
        List<String> jobs = new ArrayList<>();
        List<String> param = new ArrayList<>();

        param.add("job");

        String queryString =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX job-ontology: <"+ PREFIX_JOB_OWL +">" +
                        "SELECT * WHERE {"+
                        "?job job-ontology:requiresExperience job-ontology:" + exp +
                        "}";
        jobs.addAll(execSparql(queryString, param));
        jobs = removeDuplicates(jobs);

        return  jobs;
    }

    public static List<String> getSimilarPayJob(String sal){
        List<String> jobs = new ArrayList<>();
        List<String> param = new ArrayList<>();

        param.add("job");

        String queryString =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX job-ontology: <"+ PREFIX_JOB_OWL +">" +
                        "SELECT * WHERE {"+
                        "?job job-ontology:hasAveragePay job-ontology:" + sal +
                        "}";
        jobs.addAll(execSparql(queryString, param));
        jobs = removeDuplicates(jobs);

        return  jobs;
    }

    public static List<String> getSimilarDegreeJob(String edu){
        List<String> jobs = new ArrayList<>();
        List<String> param = new ArrayList<>();

        param.add("job");

        String queryString =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX job-ontology: <"+ PREFIX_JOB_OWL +">" +
                        "SELECT * WHERE {"+
                        "?job job-ontology:requiresEducation job-ontology:" + edu +
                        "}";
        jobs.addAll(execSparql(queryString, param));
        jobs = removeDuplicates(jobs);

        return  jobs;
    }

    /**
     * This method gets the semantic matching results from the various methods called in here and loads them into a HashMap.
     * @param pos job position name.
     * @param loc job location city.
     * @param exp experience level.
     * @param sal Average pay for a given job role.
     * @param edu Education requirement.
     */
    public static void getSemantics(String pos, String loc, String exp, String sal, String edu){
//        model.write(System.out, "RDF/XML");
        Map<String, List<String>> resultMap = new LinkedHashMap<String, List<String>>();


        resultMap.put("location", getNearLocations(loc));
        resultMap.put("jobs-pos", getSimilarPositions(pos));
        resultMap.put("jobs-exp", getSimilarExpJob(exp));
        resultMap.put("jobs-sal", getSimilarPayJob(sal));
        resultMap.put("jobs-edu", getSimilarDegreeJob(edu));
        System.out.println(resultMap);
    }
}

// "{ ?s job-ontology:isNearTo job-ontology:"+loc + " .}" +
//                        "UNION" +

