package control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import game.Game;
import game.Player;

public class MainServer {
	private static final int DEFAULT_CLK_PERIOD = 20;
	private static final int DEFAULT_BROADCAST_CLK_PERIOD = 5;
	public static int port = 13219;

	public static int numberOfClients = 1;


	public static String filename = "src/board.txt";

	public static String url = "130.195.6.48";


	public static void main(String[] args) {

		Game game = new Game("resources/mainGame.xml", "resources/continueGame.xml");

		//ClockThread clock = new ClockThread(DEFAULT_CLK_PERIOD, game);

		System.out.println("SERVER LISTENING ON PORT " + port);
		System.out.println("SERVER AWAITING " + numberOfClients + " CLIENTS");

		try {
			MasterShip[] connections = new MasterShip[numberOfClients];
			// Now, we await connections.
			ServerSocket serverSocket = new ServerSocket(port);
			while (1 == 1) {
				// 	Wait for a socket
				Socket socket = serverSocket.accept();
				System.out.println("ACCEPTED CONNECTION FROM: " + socket.getInetAddress());


				Player newPlayer = new Player("1", "characters/alien1.png", game.getRoom("rx1y2"));
				game.addPlayer(newPlayer);
				int uid = Integer.parseInt(newPlayer.getName());
				game.getData().saveWholeGame();
				//Make a new Player here
				//Get player ID
				//int uid = game.registerPacman();


				connections[--numberOfClients] = new MasterShip(socket,uid,DEFAULT_BROADCAST_CLK_PERIOD,game);
				connections[numberOfClients].start();
//				if(numberOfClients == 0) {
//					System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
//					//multiUserGame(clock,game,connections);
//					//System.out.println("ALL CLIENTS DISCONNECTED --- GAME OVER");
//					return; // done
//				}
				while(true){
					System.out.println("holding");
				}
			}
		} catch(IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		}





	}





//	private static void multiUserGame(ClockThread clock, Game game, MasterShip... connections) {
//		// save initial state of board, so we can reset it.
//
//		clock.start(); // start the clock ticking!!!
//
//		// loop forever
//		while (atleastOneConnection(connections)) {
//			game.setState(Board.READY);
//			pause(3000);
//			game.setState(Board.PLAYING);
//			// now, wait for the game to finish
//			while (game.state() == Board.PLAYING) {
//				Thread.yield();
//			}
//			// If we get here, then we're in game over mode
//			pause(3000);
//			// Reset board state
//			game.setState(Board.WAITING);
//			game.fromByteArray(state);
//		}
//	}

	private static void pause(int delay) {
		try {
			Thread.sleep(delay);
		} catch(InterruptedException e){
		}
	}


	private static boolean atleastOneConnection(MasterShip... connections) {
		for (MasterShip m : connections) {
			if (m.isAlive()) {
				return true;
			}
		}
		return false;
	}



}
