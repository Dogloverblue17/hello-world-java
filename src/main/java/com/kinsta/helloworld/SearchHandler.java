package com.kinsta.helloworld;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SearchHandler implements HttpHandler {

	private HashMap<String, String> equalsParams;
	private HashMap<String, String> containsParams;
	private HashMap<String, String> data;
	public SearchHandler(HashMap<String, String> data) {
		equalsParams = new HashMap<>();
		containsParams = new HashMap<>();
		this.data = data;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        
		this.queryToMap(exchange.getRequestURI().toString());
		ArrayList<String> passedCards = new ArrayList<>();
		
		for (String key: data.keySet()) {
			try {
			boolean passed = true;
			JSONObject jsonO = new JSONObject(data.get(key));
			for (String key2: equalsParams.keySet()) {
				if (!(String.valueOf(jsonO.get(key2)).replace(" ", "_").equalsIgnoreCase(String.valueOf(equalsParams.get(key2))))) {
				System.out.println("false on the equals:" + (String.valueOf(jsonO.get(key2))) + " : " + equalsParams.get(key2));
					passed = false;
				} else {
					System.out.println("true on the equalsa");
				}
			}
			for (String key3: containsParams.keySet()) {
				if (!(String.valueOf(jsonO.get(key3)).replace(" ", "_").toLowerCase().contains(String.valueOf(containsParams.get(key3)).toLowerCase()))) {
					passed = false;
				}
			}
			if (passed == true) {
				passedCards.add(key);
			}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		 OutputStream os = exchange.getResponseBody();
		 JSONArray ar = new JSONArray(passedCards);
		 exchange.sendResponseHeaders(200, ar.toString().length());
         os.write(ar.toString().getBytes());
         os.close();
		
	}
	public void queryToMap(String query){

	    equalsParams.clear();
	    containsParams.clear();
	    query = query.substring(query.indexOf("?") + 1, query.length());
	    for (String param : query.split("&")) {

	    	if(param.contains("~")) {
	    		String pair[] = param.split("~");

		        if (pair.length>1) {
		            containsParams.put(pair[0], pair[1]);

		        }else{
		            containsParams.put(pair[0], "");
		        }
		        continue;
	    	}
	        if (param.contains("=")) {
	        	String pair[] = param.split("=");
		        if (pair.length>1) {

		            equalsParams.put(pair[0], pair[1]);

		        }else{

		            equalsParams.put(pair[0], "");

		        }
	        }

	    }

}
}
