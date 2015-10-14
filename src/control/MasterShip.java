package control;

import game.Direction;
import game.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class MasterShip extends Thread{
	private final Game game;
	private final int broadcastClock;
	private final Socket socket;
	public DataInputStream dataInput;
	public DataOutputStream dataOutput;
	public OutputStream fileOutput = null;
	public final static String FILE_TO_SEND = "resources/continueGame.xml"; // you may
																	// change
																	// this
	public int uid;


	public MasterShip(Socket socket, int uid, int broadcastClock, Game game) {
		this.game = game;
		this.broadcastClock = broadcastClock;
		this.socket = socket;
		this.uid = uid;
	}

	public void run() {
		try {
			dataInput = new DataInputStream(socket.getInputStream());
			dataOutput = new DataOutputStream(socket.getOutputStream());
			dataOutput.writeInt(uid);

			boolean exit=false;
			while(!exit) {
				try {
					//Send the file to the client.
					System.out.println("===========");
					sendFile();
					System.out.println("============");
					System.out.println("Here");
					if(dataInput.available() != 0) {
						System.out.println("true");
						// read actions event from client.

						int dir = dataInput.readInt();
						switch(dir) {
							case 0:
								game.getPlayer(Integer.toString(uid)).turn(Direction.WEST);
								game.getData().saveWholeGame();
								break;
							case 1:
								game.getPlayer(Integer.toString(uid)).turn(Direction.EAST);
								game.getData().saveWholeGame();
								break;
						}
					}

					// Now, broadcast the state of the board to client
					System.out.println("Here");
					Thread.sleep(broadcastClock);
					System.out.println("Here");
				} catch(InterruptedException e) {
				}

			}
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println(e.getStackTrace());
			System.err.println("PLAYER " + uid + " DISCONNECTED");
			System.out.println(e.getMessage());
			//board.disconnectPlayer(uid);
			
		}
		
	}

	public void sendFile() throws IOException{

		FileInputStream fileInputStream = null;
		BufferedInputStream bufInputStream = null;
		OutputStream os = null;

		try {
			//socket = serverSocket.accept();
			//System.out.println("Accepted connection : " + socket); //already have the socket
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufInputStream != null)
				bufInputStream.close();
			if (os != null)
				os.close();
		}
	}

}
