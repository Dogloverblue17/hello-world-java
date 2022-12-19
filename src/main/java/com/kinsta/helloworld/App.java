package com.kinsta.helloworld;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.net.URISyntaxException;



import java.io.File;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class App {
	static HttpServer server;
    public static void main(String[] args) throws Exception {
        Integer port = Integer.parseInt(
          Optional.ofNullable(System.getenv("PORT")).orElse("8080")
        );
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler("TEST"));
        server.createContext("/meth", new MyHandler2());
	    doMainSetupStuff();
        server.setExecutor(null);
        server.start();

    }
public static void doMainSetupStuff() {
	String line;
	try {
		File srv = new File("srv//");
		System.out.println("srv does it?: " + srv.exists());
		File src = new File("src//");
		System.out.println("src does it?: " + src.exists());

		 File dir = new File(App.class.getProtectionDomain().getCodeSource().getLocation()
        	    .toURI());
		
		File filed = new File("burger.txt");
		System.out.println("does filed?: " + filed.exists());
		filed.createNewFile();
		System.out.println("does filed2?: " + filed.exists());
		 System.out.println(dir.exists());
		 System.out.println(dir.getPath());
		File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		    	String content = new String(Files.readAllBytes(Paths.get("readMe.txt")), StandardCharsets.UTF_8);
		    	System.out.println("/api/cards/" + child.getName().replaceFirst("[.][^.]+$", ""));
		     // API.method("/api/cards/" + child.getName().replaceFirst("[.][^.]+$", ""), content);
		      server.createContext("/api/cards/" + child.getName().replaceFirst("[.][^.]+$", ""), new MyHandler(content));
		    }
		  } else {
		    // Handle the case where dir is not really a directory.
		    // Checking dir.isDirectory() above would not be sufficient
		    // to avoid race conditions with another process that deletes
		    // directories.
		  }
		
	
	} catch (IOException | URISyntaxException e) {
		e.printStackTrace();
	}
}

    static class MyHandler implements HttpHandler {
    	String response;
        @Override
        public void handle(HttpExchange t) throws IOException {
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        public MyHandler(String response) {
        	this.response = response;
        }
    }
    static class MyHandler2 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Jesse! we need to make more methanpohetamyne";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}

