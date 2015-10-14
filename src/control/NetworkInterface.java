package control;
//Author: Aidan Carter

public interface NetworkInterface {
	/**
	 * Hey guys, i'm making this class as a open interface for any data 
	 * that needs to be transmitted back and forth from the game client
	 * This is more or less the game state and client problem,
	 * but the reason why i'm doing this is so that we at least
	 *  have a agreed upon set of objects to pass back and forth.
	 *  NOTE: Both Server AND TestClient Classes will implement this
	 *  Interface, as it will transmit only the necessary data over
	 *  the network!
	 */
	
	
}
