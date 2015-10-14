package control;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

public class SimpleServer {
    HttpServer server;

    public SimpleServer() {
        try {
            InetSocketAddress addr = new InetSocketAddress(8080);
            HttpServer server = HttpServer.create(addr, 0);

            server.createContext("/", new MyHandler());
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            System.out.println("Server is listening on port 8080");
        }
        catch (IOException e) {
            System.out.println("failed to create server ...");
        }
    }

    class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GETXML")) {
                Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, 0);




                OutputStream responseBody = exchange.getResponseBody();

                File myFile = new File("fileName");
    			byte[] mybytearray = new byte[(int) myFile.length()];
    			FileInputStream fileInputStream = new FileInputStream(myFile);
    			BufferedInputStream bufInputStream = new BufferedInputStream(fileInputStream);
    			bufInputStream.read(mybytearray, 0, mybytearray.length);
    			System.out.println("Sending " + FILE_TO_SEND + "("
    					+ mybytearray.length + " bytes)");
    			responseBody.write(mybytearray, 0, mybytearray.length);
    			responseBody.flush();
    			responseBody.flush();
    			System.out.println("Done.");




//    			Headers requestHeaders = exchange.getRequestHeaders();
//                Set<String> keySet = requestHeaders.keySet();
//                Iterator<String> iter = keySet.iterator();
//                while (iter.hasNext()) {
//                    String key = iter.next();
//                    List values = requestHeaders.get(key);
//                    String s = key + " = " + values.toString() + "\n";
//                    responseBody.write(s.getBytes());
//                }
//                responseBody.close();
            }
        }
    }
}
