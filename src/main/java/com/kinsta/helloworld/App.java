package com.kinsta.helloworld;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.json.JSONObject;

import java.net.URISyntaxException;



import java.io.File;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;


public class App {
	static HashMap<String, String> data;
	static HttpServer server;
    public static void main(String[] args) throws Exception {
        Integer port = Integer.parseInt(
          Optional.ofNullable(System.getenv("PORT")).orElse("8080")
        );
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new TextHandler("That's not a card! To learn the proper syntax, go to https://lorcana-api.com"));
	    fuzzySetup();
        doMainSetupStuff();
	   
        server.setExecutor(null);
        server.start();

    }
    static void testDepends() {
    	System.out.println("all good");
    	JSONObject so;
    	System.out.println("still good");
    	so = new JSONObject();
    	System.out.println("chillin");
    	System.out.println("noggin: " + JSONObject.getNames(so));
    	System.out.println("bing chillin");
    }
    static void fuzzySetup() {
    	data = new HashMap<String, String>();
    	File dir = new File("src//data/cards");
    	File[] directoryListing = dir.listFiles();
    	 if (directoryListing != null) {
 		    for (File child : directoryListing) {
 		    	try {
					data.put(child.getName().replaceFirst("[.][^.]+$", ""), Files.readString(Paths.get(child.getPath())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 		    }}
 		    
    //	FuzzySearch.extractOne(null, allNames);
    }
public static void doMainSetupStuff() {
	String line;
	String name;
	try {

	
		File dir = new File("src//data");

		JSONObject s;
		server.createContext("/fuzzy/", new FuzzyHandler());
		 System.out.println(dir.exists());
		 System.out.println(dir.getPath());
		File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File childdir : directoryListing) {
		    	
		    	name = childdir.getName();
		    	File[] fileListing = childdir.listFiles();
				if (fileListing != null) {
				    for (File child : fileListing) {
		    	String content = new String(Files.readAllBytes(Paths.get("src//data/" + name + "/" + child.getName())), StandardCharsets.UTF_8);
		    	System.out.println("/" + name + "/" + child.getName().replaceFirst("[.][^.]+$", ""));
		     
		      server.createContext("/" + name + "/" + child.getName().replaceFirst("[.][^.]+$", ""), new JSONHandler(content));
		    }
		  } else {
		    
		  }}}
		
	testDepends();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

    static class JSONHandler implements HttpHandler {
    	String response;
        @Override
        public void handle(HttpExchange t) throws IOException {
	    t.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
            t.sendResponseHeaders(200, response.length());
      
            OutputStream os = t.getResponseBody();
            
            os.write(response.getBytes());
            os.close();
        }
        public JSONHandler(String response) {
        	this.response = response;
        }
    }
    static class FuzzyHandler implements HttpHandler {
 	   String response = "sd";
         @Override
         public void handle(HttpExchange t) throws IOException {
             t.sendResponseHeaders(200, response.length());
             ExtractedResult result = FuzzySearch.extractOne(String.valueOf(t.getRequestURI()).replace("/fuzzy/", ""), data.keySet());
             response = data.get(result.getString());
             System.out.println("URI: " + t.getRequestURI());
             System.out.println("response: " + response);
             System.out.println("sus input: " + String.valueOf(t.getRequestURI()).replace("/fuzzy/", ""));
             OutputStream os = t.getResponseBody();
             os.write(response.getBytes());
             os.close();
         }
 	    
     }
    static class TextHandler implements HttpHandler {
	   String response;
        @Override
        public void handle(HttpExchange t) throws IOException {
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
	    public TextHandler(String response) {
		    this.response = response;
	    }
    }

}

