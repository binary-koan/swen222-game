package control;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainClient{
	public static String filename = "src/board.txt";
	public static String ipAddress = "130.195.6.48";		//the server's IP address
	public static int port = 13219; 						//the server's port

	public static void main(String[] args) {
		Socket socket;
		try {
			socket = new Socket(ipAddress ,port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR : Invalid port/ipAdress");
			return;
		}
		System.out.println("CLIENT CONNECTED TO " + ipAddress + ":" + port);

		new SlaveShip(socket).start();
	}

}