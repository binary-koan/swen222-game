package control;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import game.StateChangeListener;
import gui.actions.GameActions;
import gui.actions.GameActions.GameAction;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Executors;

public class Server {
    HttpServer server;
    String filename;

    List<String> clientUrls = new ArrayList<>();

    public Server(HttpHandler handler) {
        try {
            InetSocketAddress addr = new InetSocketAddress(8080);
            HttpServer server = HttpServer.create(addr, 0);

            server.createContext("/", handler);
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            System.out.println("Server is listening on port 8080");
        }
        catch (IOException e) {
            System.out.println("failed to create server ...");
        }
    }




	public void sendAction(String url, GameAction action) throws IOException {

		URLConnection connection = new URL(url + "?" + action.serialize())
				.openConnection();
		connection.setRequestProperty("Accept-Charset", "UTF-8");

		InputStream response = connection.getInputStream();
		response.close();

	}




    public void readAction(HttpExchange exchange) throws IOException {
//    	String content = exchange.getRequestURI().getQuery();
//        GameAction g = GameAction.deserialize(content, server_game);
//        if (g.apply()) {
//            for (String clientUrl : clientUrls) {
//            	sendAction(clientUrl, g);
//            }
//        }

    }

    public void sendXML(OutputStream fileStream) throws IOException{

    	File myFile = new File(filename);
		byte[] mybytearray = new byte[(int) myFile.length()];
		FileInputStream fileInputStream = new FileInputStream(myFile);
		BufferedInputStream bufInputStream = new BufferedInputStream(fileInputStream);
		bufInputStream.read(mybytearray, 0, mybytearray.length);
		bufInputStream.close();

		System.out.println("Sending " + "FILE_TO_SEND" + "("
				+ mybytearray.length + " bytes)");
		fileStream.write(mybytearray, 0, mybytearray.length);
		fileStream.flush();
		fileStream.close();
    }





    class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            // GET <ip>:<port>/xml
            if (requestMethod.equalsIgnoreCase("GET") && exchange.getRequestURI().getPath().equals("/action")) {
            	readAction(exchange);
            }

            if(requestMethod.equalsIgnoreCase("GET") &&exchange.getRequestURI().getPath().equals("/xml")){
            	OutputStream responseBody = exchange.getResponseBody();
            	sendXML(responseBody);
            }





            	Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, 0);





            }


//            if(requestMethod.equalsIgnoreCase("GET") && exchange.getRequestURI().getPath().equals("/action")){
//
//            	Client.this.readAction(exchange);
////
////            	// read string from that
////            	GameAction action = GameActions.GameAction.deserialize(exchange.getRequestURI().getQuery(), server_game);
////            	//error handling
////            	//game
////            	action.apply();
////
////            	String gameAction = action.serialize();
////
////
////            	OutputStreamWriter responseBody = new OutputStreamWriter(exchange.getResponseBody());
////            	responseBody.write(gameAction);
//



        }






}
