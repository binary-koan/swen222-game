package control;

import com.sun.net.httpserver.HttpServer;
import game.*;
import gui.actions.Action;
import gui.actions.ActionHandler;
import gui.actions.GameActions.GameAction;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import gui.actions.SinglePlayerClient;

/**
 * An action handler for the single player game client. This requests the initial XML from the server, sends local
 * actions to the server and applies server-sent actions to the game
 *
 * @author Scott Holdaway, Jono Mingard
 */
public class ClientActionHandler implements ActionHandler, HttpHandler {
    private static final int PORT = 9000;

    /**
     * An object which will be notified when the game is loaded from the server
     */
    public interface LoadListener {
        /**
         * Called when the game is loaded from the server
         *
         * @param game the loaded game instance
         * @param player the player that should be used locally
         */
        void onGameLoaded(Game game, Player player);
    }

	private String url;

    private List<LoadListener> loadListeners = new ArrayList<>();

	private final String playerName;
    private final String spriteName;
	private Game game;

    // Used to get allowed actions
    private SinglePlayerClient actionHandler = new SinglePlayerClient();

    /**
     * Create a new client action handler
     *
     * @param playerName name of the player to request
     * @param spriteName sprite that the player should use
     * @param serverUrl server URL to connect to
     * @param serverPort port to connect to on the server
     */
    public ClientActionHandler(String playerName, String spriteName, String serverUrl, int serverPort) {
        this.playerName = playerName;
        this.spriteName = spriteName;
        this.url = "http://" + serverUrl + ":" + serverPort;

        startLocalServer();
        requestGameXML();
    }

    /**
     * Add a load listener, which will be called when the game is loaded from the server
     *
     * @param listener load listener to add
     */
    public void addLoadListener(LoadListener listener) {
        if (game == null) {
            loadListeners.add(listener);
        }
        else {
            listener.onGameLoaded(game, game.getPlayer(playerName));
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Action> getAllowedActions(Player player, Drawable drawable) {
        return actionHandler.getAllowedActions(player, drawable);
    }

    /** {@inheritDoc} */
    @Override
    public void requestAction(Action action) {
        if (action instanceof GameAction) {
            try {
                URLConnection connection = new URL(url + "/action?" + ((GameAction)action).serialize()).openConnection();
                connection.setRequestProperty("Accept-Charset", "UTF-8");

                InputStream response = connection.getInputStream();
                response.close();
            }
            catch (IOException e) {
                System.out.println("Failed to send action: " + e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Handle action requests coming from the game server to the local server
     *
     * @param exchange HTTP request to handle
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        System.out.println("Got request for " + exchange.getRequestURI());

        if (requestMethod.equalsIgnoreCase("GET") && exchange.getRequestURI().getPath().equals("/action")) {
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();

            System.out.println("Reading action " + exchange.getRequestURI().getQuery());
            readAction(exchange);
        }
        else {
            exchange.sendResponseHeaders(404, 0);
            exchange.getResponseBody().close();
        }
    }

    /**
     * Start a local server, which will receive actions from the actual server
     */
    private void startLocalServer() {
        try {
            InetSocketAddress addr = new InetSocketAddress(PORT);
            HttpServer server = HttpServer.create(addr, 0);

            server.createContext("/", this);
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
        }
        catch (IOException e) {
            System.out.println("Server crashed!");
            System.out.println(e.toString());
        }
    }

    /**
     * Request the complete game XML from the game server
     */
    private void requestGameXML() {
        try {
            // Initial connection - send the server the port our client server is listening on
            String query = String.format("playerName=%s&spriteName=%s&port=%s",
                    URLEncoder.encode(playerName, "UTF-8"),
                    URLEncoder.encode(spriteName, "UTF-8"),
                    URLEncoder.encode(Integer.toString(PORT), "UTF-8"));

            URLConnection connection = new URL(url + "/connect?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            InputStream response = connection.getInputStream();
            readXML(response);
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read an action from the given HTTP request
     */
	private void readAction(HttpExchange exchange) {
		String content = exchange.getRequestURI().getQuery();
		GameAction g = GameAction.deserialize(content, game);
		if (g != null) {
            g.apply();
        }
	}

    /**
     * Read XML from the given response and create a game from it
     */
	private void readXML(InputStream response) {
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

			while (bytesRead > -1) {
				bytesRead = response.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) {
                    current += bytesRead;
                }
			}
			bos.write(mybytearray, 0, current);
			bos.flush();
			System.out.println("File " + "resources/clientGame.xml" + " downloaded ("
					+ current + " bytes read)");

			game = new Game("resources/clientGame.xml", "resources/clientGame2.xml");
			Player player = game.getPlayer(playerName);
            for (LoadListener listener : loadListeners) {
                listener.onGameLoaded(game, player);
            }
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
				System.out.println("Failed to read XML");
                e.printStackTrace();
			}
		}
	}
}
