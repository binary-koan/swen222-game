package control;

/**
 * The Clock Thread is responsible for producing a consistent "pulse" which is
 * used to showObject the game state, and refresh the display. Setting the pulse
 * rate too high may cause problems, when the point is reached at which the work
 * done to service a given pulse exceeds the time between pulses.
 * 
 * @author djp
 * 
 * 
 * Hey guys, this is a imported clock thread from the pacman game. This class
 * is critical for any timed action, and is mainly used for the client, renderer
 * and logic utilized inside of it.
 */
public class ClockThread extends Thread {
	private final int delay; // delay between pulses in us
	
	public ClockThread(int delay) {
		this.delay = delay;
	}
	
	public void run() {
		while(true) {
			// Loop forever			
			try {
				Thread.sleep(delay);
			} 
			catch(InterruptedException e)
			{
				// should never happen
			}			
		}
	}
}
