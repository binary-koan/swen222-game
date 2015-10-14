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

	public final static int SOCKET_PORT = 13250; // you may change this
	public final static String FILE_TO_SEND = "resources/continueGame.xml"; // you may
																	// change
																	// this

	public static void main(String[] args) throws IOException {
		FileInputStream fileInputStream = null;
		BufferedInputStream bufInputStream = null;
		OutputStream os = null;
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(SOCKET_PORT);
			while (true) {
				System.out.println("Waiting...");
				try {
					socket = serverSocket.accept();
					System.out.println("Accepted connection : " + socket);
					// send file
					File myFile = new File(FILE_TO_SEND);
					byte[] mybytearray = new byte[(int) myFile.length()];
					fileInputStream = new FileInputStream(myFile);
					bufInputStream = new BufferedInputStream(fileInputStream);
					bufInputStream.read(mybytearray, 0, mybytearray.length);
					os = socket.getOutputStream();
					System.out.println("Sending " + FILE_TO_SEND + "("
							+ mybytearray.length + " bytes)");
					os.write(mybytearray, 0, mybytearray.length);
					os.flush();
					System.out.println("Done.");
				} finally {
					if (bufInputStream != null)
						bufInputStream.close();
					if (os != null)
						os.close();
					if (socket != null)
						socket.close();
				}
			}
		} finally {
			if (serverSocket != null)
				serverSocket.close();
		}
	}
}
