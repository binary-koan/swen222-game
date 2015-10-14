package control;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import game.Game;
import game.Player;
import gui.actions.GameActions.GameAction;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * A game server. Creates a new game data file and allows clients to connect, handles action requests and sends actions
 * back to all connected clients
 *
 * @author Scott Holdaway, Jono Mingard
 */
public class Server {
    /**
     * Run a server. Can be passed in arguments to set the game filename and port
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        String filename = "resources/serverGame.xml";
        int port = 8080;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--filename")) {
                filename = args[i + 1];
            }
            else if (args[i].equals("--port")) {
                port = Integer.parseInt(args[i + 1]);
            }
        }

        new Server(filename, port);
    }

    /**
     * Handles requests made to the server
     */
    class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Handling request for " + exchange.getRequestURI());

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");

            try {
                if (exchange.getRequestURI().getPath().equals("/connect")) {
                    Map<String, String> queryString = parseQueryString(exchange.getRequestURI().getRawQuery());
                    String address = exchange.getRemoteAddress().getAddress().getHostAddress();

                    String playerName = queryString.get("playerName");
                    String spriteName = queryString.get("spriteName");
                    int port = Integer.parseInt(queryString.get("port"));

                    if (playerName == null || spriteName == null || game.getPlayer(playerName) != null) {
                        exchange.sendResponseHeaders(403, 0);
                        exchange.getResponseBody().close();
                    }
                    else {
                        game.addPlayer(new Player(playerName, spriteName, game.getRooms().values().iterator().next()));
                        game.getData().saveWholeGame();

                        clients.put(address, "http://" + address + ":" + port);
                        System.out.println("Client connected: " + clients.get(address));

                        exchange.sendResponseHeaders(200, 0);
                        sendXML(exchange.getResponseBody());
                    }
                }
                else if (exchange.getRequestURI().getPath().equals("/action")) {
                    exchange.sendResponseHeaders(200, 0);
                    sendEmptyResponse(exchange);
                    processAction(exchange);
                }
                else if (exchange.getRequestURI().getPath().equals("/disconnect")) {
                    exchange.sendResponseHeaders(200, 0);
                    sendEmptyResponse(exchange);

                    clients.remove(exchange.getRemoteAddress().getAddress().getHostAddress());
                }
                else {
                    exchange.sendResponseHeaders(404, 0);
                    sendEmptyResponse(exchange);
                }
            }
            catch (Exception e) {
                System.out.println("Request failed: " + e + " (" + e.getMessage() + ")");
                e.printStackTrace();

                exchange.sendResponseHeaders(500, 0);
                exchange.getResponseBody().close();
                throw e;
            }
        }

        private Map<String, String> parseQueryString(String query) {
            if (query == null) {
                return new HashMap<>();
            }

            try {
                Map<String, String> queryPairs = new LinkedHashMap<>();
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                }
                return queryPairs;
            }
            catch (UnsupportedEncodingException e) {
                System.out.println("Could not parse query string: " + e);
                return new HashMap<>();
            }
        }

        private void sendEmptyResponse(HttpExchange exchange) {
            try {
                exchange.getResponseBody().close();
            }
            catch (IOException e) {
                System.out.println("Failed to send response: " + e);
            }
        }
    }

    private Game game;
    private String gameFilename;

    Map<String, String> clients = new HashMap<>();

    /**
     * Create a new server
     *
     * @param filename filename to save the server's copy of the game to
     * @param port port to run the server on
     */
    public Server(String filename, int port) {
        game = new Game("resources/mainGame.xml", filename);
        gameFilename = filename;

        try {
            InetSocketAddress addr = new InetSocketAddress(port);
            HttpServer server = HttpServer.create(addr, 0);

            server.createContext("/", new RequestHandler());
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            System.out.println("Server started. Use the following connection settings:");
            System.out.println("   IP address: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("   Port: " + port);
        }
        catch (IOException e) {
            System.out.println("Server crahed!");
            System.out.println(e.toString());
        }
    }

	private void sendAction(String url, GameAction action) throws IOException {
        System.out.println("Sending action to " + url);
        try {
            URLConnection connection = new URL(url + "/action?" + action.serialize())
                    .openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            InputStream response = connection.getInputStream();
            response.close();
        }
        catch (Exception e) {
            System.out.println("Unable to send action: " + e);
            e.printStackTrace();
            throw e;
        }
	}

    private void processAction(HttpExchange exchange) throws IOException {
        try {
            String content = exchange.getRequestURI().getQuery();
            GameAction g = GameAction.deserialize(content, game);
            if (g != null && g.apply()) {
                for (String clientUrl : clients.values()) {
                    sendAction(clientUrl, g);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Unable to process action: " + e);
            e.printStackTrace();
        }
    }

    private void sendXML(OutputStream fileStream) throws IOException {
        try {
            byte[] mybytearray = readFile(gameFilename);
            System.out.println("Sending " + gameFilename + "(" + mybytearray.length + " bytes)");

            fileStream.write(mybytearray, 0, mybytearray.length);
            fileStream.flush();
            fileStream.close();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
            throw e;
        }
    }

    private byte[] readFile(String filename) throws IOException {
        File myFile = new File(filename);
        byte[] mybytearray = new byte[(int) myFile.length()];
        FileInputStream fileInputStream = new FileInputStream(myFile);
        BufferedInputStream bufInputStream = new BufferedInputStream(fileInputStream);

        bufInputStream.read(mybytearray, 0, mybytearray.length);
        bufInputStream.close();
        return mybytearray;
    }
}
