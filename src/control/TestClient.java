package control;

import game.*;
import gui.actions.Action;
import gui.actions.ActionHandler;
import gui.actions.GameActions;
import gui.actions.GameActions.GameAction;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import gui.renderer.Door;

public class TestClient {
	public static void main(String[] args) {
		new TestClient("localhost", 8080);
	}

	private int port = 9000;
	private SimpleServer server = new SimpleServer(port, new MyHandler());
	private String url;
	private String charset;

	private final String playerName = "myPlayer";
	private Player player;
	private Game game;

    public TestClient(String serverUrl, int serverPort) {
        this.charset = "UTF-8";
        this.url = "http://" + serverUrl + ":" + serverPort;

        try {
            // Initial connection - send the server the port our client server is listening on
			String query = String.format("playerName=%s&spriteName=%s&port=%s",
					URLEncoder.encode(playerName, charset),
					URLEncoder.encode("characters/alien4.png", charset),
					URLEncoder.encode(Integer.toString(port), charset));

            URLConnection connection = new URL(url + "/connect?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();
            readXML(response);
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
        }
    }

	public void sendAction(GameAction action) throws IOException {
		URLConnection connection = new URL(url + "/action?" + action.serialize()).openConnection();
		connection.setRequestProperty("Accept-Charset", charset);

		InputStream response = connection.getInputStream();
		response.close();
	}

	public void readAction(HttpExchange exchange) {
		String content = exchange.getRequestURI().getQuery();
		GameAction g = GameAction.deserialize(content, game);
		g.apply();
	}

	public void readXML(InputStream response) {
		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			// receive file
			byte[] mybytearray = new byte[1000000];
			fos = new FileOutputStream("resources/clientGame.xml");
			bos = new BufferedOutputStream(fos);
			bytesRead = response.read(mybytearray, 0, mybytearray.length);
			current = bytesRead;

			while (bytesRead > -1){
				bytesRead = response.read(mybytearray, current,
						(mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			}
			bos.write(mybytearray, 0, current);
			bos.flush();
			System.out.println("File " + "resources/clientGame.xml" + " downloaded ("
					+ current + " bytes read)");

			game = new Game("resources/clientGame.xml", "resources/clientGame.xml");
			player = game.getPlayer(playerName);
			sendAction(new GameActions.Turn(player, Direction.SOUTH));
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

			System.out.println("Got request for " + exchange.getRequestURI());
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
				System.out.println("Reading action " + exchange.getRequestURI().getQuery());
            	readAction(exchange);
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
