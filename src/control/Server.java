
// This file is part of the Multi-player Pacman Game.
//
// Pacman is free software; you can redistribute it and/or modify 
// it under the terms of the GNU General Public License as published 
// by the Free Software Foundation; either version 3 of the License, 
// or (at your option) any later version.
//
// Pacman is distributed in the hope that it will be useful, but 
// WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
// the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public 
// License along with Pacman. If not, see <http://www.gnu.org/licenses/>
//
// Copyright 2010, David James Pearce. 


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

	private final int broadcastClock;
	private final int uid;
	private final Socket socket;

	public Server(Socket socket, int uid, int broadcastClock, Board board) {
		this.broadcastClock = broadcastClock;
		this.socket = socket;
		this.uid = uid;
	}

	public void run() {		
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			// First, write the period to the stream				
			boolean exit=false;
			while(!exit) {
				try {
					if(input.available() != 0) {

						Thread.sleep(broadcastClock);
					}
				} catch(InterruptedException e) {					
				}
			}
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println("PLAYER " + uid + " DISCONNECTED");

		}		
	}
}
