import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MapPanel extends JPanel {

	// the internal array that hold the map
	public String[][] internalMap;

	// the map width and length
	public static final int WIDTH = 500;
	public static final int LENGTH = 500;

	// point size
	public static final int POINT_WIDTH = 7;

	public MapPanel(String[][] map) {
		super();
		internalMap = map;
		//this.setSize(900, 400);
		//srepaint();
		
		//showMap();
	}

	public void paint(Graphics g) {
		//System.out.println("hello");
		showMap(g);
	}

	/** create the panel for map **/
	public JPanel showMap(Graphics g) {
		// Create a panel to hold GUI elements
		JPanel myMap = new JPanel();
		JPanel mapGUI = new JPanel();
		JPanel placeNames = new JPanel();

		//set the layout
		placeNames.setLayout(new BoxLayout(placeNames, BoxLayout.Y_AXIS));

		// set the layout
		this.setLayout(new GridLayout(1, 2));


		// set the size of the panel
		//myMap.setSize(WIDTH, LENGTH);

		// go through the map array for points with value and show them on the
		// map area
		for (int i = 0; i < internalMap.length; i++) {
			for (int j = 0; j < internalMap[i].length; j++) {
				if (!(internalMap[i][j]==null)) {

					// get the random color variable
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();

					// create the new color
					Color myColor = new Color(red, green, blue);
					g.setColor(myColor);

					// draw the circle for the place
					g.fillOval((int) (i * 450 / 20), (int) (j * 470 / 20), POINT_WIDTH, POINT_WIDTH);

					//g.fi
					// use the same color to set the place name
					JTextField placeName = new JTextField(internalMap[i][j]);
					placeName.setEditable(false);
					placeName.setForeground(myColor);
					placeNames.add(placeName);
					placeNames.setVisible(true);
					validate();
					//JButton test = new JButton("dont do anything");
					//placeNames.add(test);
				}
			}

		}

		// add the components
		this.add(mapGUI);
		this.add(placeNames);
		//this.add(myMap);
		// return
		return myMap;

	}
	
	/**function to vistualize the route according to the start time**/
	public void showRoute(Graphics g)
	{
		// a temp list to store the coordinates of the user's locations
				ArrayList<int[]> coordinates = new ArrayList<int[]>();
				
				// go through the list of places, look in the map for its location
				for (int i = 0; i < GUI.placeActivity.size(); i++) {
					// record the name of the location in the user is schedule
					String locationName = "";
					locationName = GUI.placeActivity.get(i)[0];

					// go through every slot in the map array
					for (int m = 0; m < internalMap.length; m++) {
						for (int n = 0; n < internalMap[i].length; n++) {
							// find the location in the array
							if (locationName.equals(internalMap[m][n])) {
								// record its coordinates
								int[] temp = new int[2];
								temp[0] = m;
								temp[1] = n;
								// add this coordinate to the list
								coordinates.add(temp);

							}
						}

					}
				}
		

				for (int i = 0; i< coordinates.size()-1;i++)
				{
					g.drawLine(coordinates.get(i)[0], coordinates.get(i)[1], coordinates.get(i+1)[0], coordinates.get(i+1)[1]);
				}
	}

}
