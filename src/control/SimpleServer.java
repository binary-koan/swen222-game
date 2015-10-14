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
import java.util.*;
import java.util.concurrent.Executors;

public class SimpleServer {
    HttpServer server;

    public SimpleServer(int port, HttpHandler handler) {
        try {
            InetSocketAddress addr = new InetSocketAddress(port);
            HttpServer server = HttpServer.create(addr, 0);

            server.createContext("/", handler);
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            System.out.println("Server is listening on port " + port);
        }
        catch (IOException e) {
            System.out.println("failed to create server ...");
        }
    }

}
