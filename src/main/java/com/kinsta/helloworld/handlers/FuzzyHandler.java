package com.kinsta.helloworld.handlers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class FuzzyHandler implements HttpHandler {
		int FuzzyRequestCount = 0;
	    String response = "sd";
	    static HashMap<String, String> data;
      @Override
      public void handle(HttpExchange t) throws IOException {
		 System.out.println("Fuzzy response made; it was fuzzy request number " + FuzzyRequestCount);
		 FuzzyRequestCount++;
     	 t.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
          t.sendResponseHeaders(200, response.length());
          ExtractedResult result = FuzzySearch.extractOne(String.valueOf(t.getRequestURI()).replace("/fuzzy/", ""), data.keySet());
          response = data.get(result.getString());
       //   System.out.println("URI: " + t.getRequestURI());
        //  System.out.println("response: " + response);
         // System.out.println("sus input: " + String.valueOf(t.getRequestURI()).replace("/fuzzy/", ""));
          OutputStream os = t.getResponseBody();
          os.write(response.getBytes());
          os.close();
      }
      public FuzzyHandler(HashMap<String, String> cardData) {
    	  data = cardData;
      }
     
	    
  }
