package control;

import game.Game;
import java.util.*;
import java.io.*;
import java.net.*;

/**
 * A master connection receives events from a slave connection via a socket.
 * These events are registered with the board. The master connection is also
 * responsible for transmitting information to the slave about the current board
 * state.
 */
public final class Server extends Thread {

	private final Game game;
	private final int broadcastClock;
	private final int uid;
	private final Socket socket;

	public Server(Socket socket, int uid, int broadcastClock, Game game) {
		this.game = game;
		this.broadcastClock = broadcastClock;
		this.socket = socket;
		this.uid = uid;
	}

	public void run() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(
					socket.getOutputStream());
			// First, write the period to the stream
			boolean exit = false;
			while (!exit) {
				try {
					if (input.available() != 0) {

						Thread.sleep(broadcastClock);
					}
				} catch (InterruptedException e) {
				}
			}
			socket.close(); // release socket ... v.important!
		} catch (IOException e) {
			System.err.println("PLAYER " + uid + " DISCONNECTED");

		}
	}
}
