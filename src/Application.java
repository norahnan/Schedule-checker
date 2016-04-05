import javax.swing.JFrame;

public class Application {

	/** main method to start the application **/
	public static void main(String[] args) {
		// create the frame to hold the game
		JFrame guiFrame;
		guiFrame = new JFrame("Route Check-master");

		// set size
		guiFrame.setSize(1200, 500);
		// add the panel
		guiFrame.add(new GUI());
		// exit normally on closing the window
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// show frame
		guiFrame.setVisible(true);

	}

}
