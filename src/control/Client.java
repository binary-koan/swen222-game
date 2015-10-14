package control;

import gui.actions.GameActions.GameAction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Client {
	public static void main(String[] args) {
		new Client("130.195.6.194", 8080);
	}

	private int port = 8080;
	private SimpleServer server = new SimpleServer(port, new MyHandler());
	private String url;
	private String charset;

    public Client(String serverUrl, int serverPort) {
        this.charset = "UTF-8";
        this.url = "http://" + serverUrl + ":" + serverPort;

        try {
            // Initial connection - send the server the client IP and port
            String query = String.format("ip=%s&port=%s",
                    URLEncoder.encode(InetAddress.getLocalHost().getHostAddress(), charset),
                    URLEncoder.encode(Integer.toString(port), charset));
            System.out.println(query);

            URLConnection connection = new URL(url + "/xml?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();
            readXML(response);

            //TODO read from input stream
        }
        catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

	public void sendAction(String url, GameAction action) throws IOException {
		URLConnection connection = new URL(url + "?" + action.serialize()).openConnection();
		connection.setRequestProperty("Accept-Charset", charset);

		InputStream response = connection.getInputStream();
		response.close();

	}

	public void readAction(HttpExchange exchange) {
//		String content = exchange.getRequestURI().getQuery();
//		GameAction g = GameAction.deserialize(content, client_game);
//		g.apply();
	}

	public void readXML(InputStream response){
		System.out.println("reading response");
		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			// receive file
			byte[] mybytearray = new byte[1000000];
			System.out.println("one");
			fos = new FileOutputStream("resources/continueGame.xml");
			bos = new BufferedOutputStream(fos);
			bytesRead = response.read(mybytearray, 0, mybytearray.length);
			System.out.println("two");
			current = bytesRead;

			 while (bytesRead > -1){
				bytesRead = response.read(mybytearray, current,
						(mybytearray.length - current));
				if (bytesRead >= 0) {
					current += bytesRead;
				}
				System.out.println(bytesRead + " bytes read");
			}
			bos.write(mybytearray, 0, current);
			bos.flush();
			System.out.println("File " + "resources/continueGame.xml" + " downloaded ("
					+ current + " bytes read)");
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
				if (fos != null)
					fos.close();
				if (bos != null)
					bos.close();
			}
			catch (IOException e) {
				//TODO
			}
		}
	}





    class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            // GET <ip>:<port>/xml





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



            if(requestMethod.equalsIgnoreCase("GET") && exchange.getRequestURI().getPath().equals("/action")){

            	Client.this.readAction(exchange);
//
//            	// read string from that
//            	GameAction action = GameActions.GameAction.deserialize(exchange.getRequestURI().getQuery(), server_game);
//            	//error handling
//            	//game
//            	action.apply();
//
//            	String gameAction = action.serialize();
//
//
//            	OutputStreamWriter responseBody = new OutputStreamWriter(exchange.getResponseBody());
//            	responseBody.write(gameAction);



            }
        }
    }
}
