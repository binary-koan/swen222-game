package control;

import game.Game;
import game.Player;
import gui.ApplicationWindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SlaveShip extends Thread implements KeyListener{
	private final Socket socket;
	private Game game;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	private InputStream fileInput;
	public int uid;


	//public final static int SOCKET_PORT = 13250; // you may change this
	//public final static String SERVER = "127.0.0.1"; // localhost
	public final static String FILE_TO_RECEIVED = "/u/students/holdawscot/poorOldIreland.xml"; //

	public final static int FILE_SIZE = 9999999;

	/**
	 * Construct a slave connection from a socket. A slave connection does no
	 * local computation, other than to display the current state of the board;
	 * instead, board logic is controlled entirely by the server, and the slave
	 * display is only refreshed when data is received from the master
	 * connection.
	 *
	 * @param socket
	 * @param dumbTerminal
	 */
	public SlaveShip(Socket socket) {
		this.socket = socket;
	}

	public void run() {

		try {

			dataInput = new DataInputStream(socket.getInputStream());
			dataOutput = new DataOutputStream(socket.getOutputStream());

			dataOutput.writeInt(4);				///
			this.uid = dataInput.readInt();

			receiveFile();


			this.game = new Game("/u/students/holdawscot/poorOldIreland.xml", "/u/students/holdawscot/poorOldIreland.xml");

			if(game.getRoom("rx0y4") != null){
				System.out.println("true");
			}
			else{
				System.out.println("false");
			}
			//changeable
			Player newPlayer = game.getPlayer(Integer.toString(uid));


			ApplicationWindow clientWindow = new ApplicationWindow(game, newPlayer);
			clientWindow.run();
			//game.loadWholeGame
			//Application window?
			//BoardFrame display = new BoardFrame("Pacman (client@" + socket.getInetAddress() + ")",game,uid,this);
			boolean exit=false;

			while(!exit) {
				dataOutput.writeInt(4);					/////
				Thread.sleep(3000);
				receiveFile();
				// read event
				// write file!

				game.getData().loadWholeGame();
				clientWindow.canvas.update();

			}
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println("I/O Error: " + e.getMessage());
			e.printStackTrace(System.err);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void receiveFile() throws IOException{

		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try{

			fileInput = socket.getInputStream();


			// First job, is to read the file so we can create the game
			byte[] gameByteArray = new byte[FILE_SIZE];
			fos = new FileOutputStream(FILE_TO_RECEIVED);
			bos = new BufferedOutputStream(fos);
			bytesRead = fileInput.read(gameByteArray, 0, gameByteArray.length);
			System.out.println(bytesRead);
			current = bytesRead;

			while (bytesRead != -1){
				bytesRead = fileInput.read(gameByteArray, current,
						(gameByteArray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			}

			if(current != -1){
				bos.write(gameByteArray, 0, current);
			}



			bos.flush();
			System.out.println("File " + FILE_TO_RECEIVED + " downloaded ("
					+ current + " bytes read)");

			}
			finally {
				if (fos != null)
					fos.close();
				if (bos != null)
					bos.close();
			}

	}



	// Action listeners?

	public void keyPressed(KeyEvent e) {
		try {
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_RIGHT) {
				dataOutput.writeInt(0);
			} else if(code == KeyEvent.VK_LEFT) {
				dataOutput.writeInt(1);
			}
			dataOutput.flush();
		} catch(IOException ioe) {
			// something went wrong trying to communicate the key press to the
			// server.  So, we just ignore it.
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}
}
