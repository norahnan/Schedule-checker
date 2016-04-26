import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JPanel {

	// the frame
	static JFrame guiFrame;

	// the buttons
	private JButton submit = new JButton("Submit");
	private JButton delete = new JButton("Delete(enter activity)");
	private JButton upload = new JButton("upload map");
	private JButton save = new JButton("save schedule");

	// the text fields for user input
	private JTextArea Start_time = new JTextArea("Start time: ");
	// different period of time the user can choose from
	private String[] hours = { "00", "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
			"19", "20", "21", "22", "23" };
	private String[] minutes = { "00", "05", "10", "15", "20", "25", "30",
			"35", "40", "45", "50", "55" };
	private JComboBox minutelist = new JComboBox(minutes);
	private JComboBox hourlist = new JComboBox(hours);

	// the title for schedule
	private JTextArea scheduleTitle = new JTextArea("My Schdule is here! ");

	// different time of the day to select from
	private JTextArea endTime = new JTextArea("End_time: ");
	private JComboBox endhour = new JComboBox(hours);
	private JComboBox endminute = new JComboBox(minutes);

	private JTextArea place = new JTextArea("Place name: ");
	protected JTextField placeinput = new JTextField();
	// protected JTextArea placeinput = new JTextArea();

	private JTextArea activity = new JTextArea("Activity: ");
	protected JTextField activityinput = new JTextField();

	// the map width and length
	public static final int WIDTH = 500;
	public static final int LENGTH = 500;

	// the speed is 0.3 unit per minute
	public static final int SPEED = 3;

	// to temporary store the data from the map
	private ArrayList myMap = new ArrayList();
	// to draw on the interface
	private String[][] map = new String[20][20];

	// to write in to a txt file
	private String scheduleFile;
	// to temporary store the start and end time input from the user
	private LinkedList<Date> schedule = new LinkedList<Date>();
	// a list of locations and activities
	static protected ArrayList<String[]> placeActivity = new ArrayList<String[]>();
	// a list of time left in between
	private ArrayList<Integer> interval = new ArrayList<Integer>();
	// list of then time needed
	private ArrayList<Integer> timeNeeded = new ArrayList<Integer>();
	// list of time that internal is not enough
	private ArrayList notEnoughTime = new ArrayList<Integer>();

	/** constructor **/

	public GUI() {
		super();
		// repaint();
		initPanel();

	}

	/** start configuration **/
	public void initPanel() {

		this.setLayout(new BorderLayout());

		// add the components to the panel
		this.add(createButtons(), BorderLayout.NORTH);
		// see the map part
		createmap();

		// Create a panel to hold GUI elements
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new GridLayout(2, 1));
		newPanel.add(new MapPanel(map));
		newPanel.add(showSchedule());

		this.add(newPanel, BorderLayout.CENTER);

		// this.add(showSchedule(), BorderLayout.SOUTH);

	}

	/** add buttons **/
	public JPanel createButtons() {
		// Create a panel to hold GUI elements
		JPanel buttons = new JPanel();
		// set the layout
		buttons.setLayout(new GridLayout(2, 1));

		// Create a panel to hold GUI elements
		JPanel inputfields = new JPanel();
		// set the layout
		inputfields.setLayout(new GridLayout(2, 5));

		// Create a panel to hold GUI elements
		JPanel buttonFields = new JPanel();
		// set the layout
		buttonFields.setLayout(new GridLayout(2, 2));

		// set the text fields not editable
		Start_time.setEditable(false);
		endTime.setEditable(false);
		place.setEditable(false);
		activity.setEditable(false);

		// set the buttons
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pressedSubmit();
			}
		});

		upload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pressedUpload();
			}
		});

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pressedSave();
			}
		});

		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pressedDelete();
			}
		});

		// add to the panel

		inputfields.add(place);
		inputfields.add(placeinput);
		inputfields.add(Start_time);
		inputfields.add(hourlist);
		inputfields.add(minutelist);
		inputfields.add(activity);
		inputfields.add(activityinput);
		inputfields.add(endTime);
		inputfields.add(endhour);
		inputfields.add(endminute);

		buttonFields.add(submit);
		buttonFields.add(delete);
		buttonFields.add(upload);
		buttonFields.add(save);

		buttons.add(inputfields);
		buttons.add(buttonFields);

		// return
		return buttons;
	}

	/** find the time need for going from places **/
	public void getTimeNeeded() {

		// a temp list to store the coordinates of the user's locations
		ArrayList<int[]> coordinates = new ArrayList<int[]>();

		// go through the list of places, look in the map for its location
		for (int i = 0; i < placeActivity.size(); i++) {
			// record the name of the location in the user is schedule
			String locationName = "";
			locationName = placeActivity.get(i)[0];

			// go through every slot in the map array
			for (int m = 0; m < map.length; m++) {
				for (int n = 0; n < map[i].length; n++) {
					// find the location in the array
					if (locationName.equals(map[m][n])) {
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

		// loop through the location list
		for (int q = 0; q < coordinates.size() - 1; q++) {
			// int to store the time needed
			int time = 0;
			// calculate the distance between the two locations and divide that
			// with the speed
			time = (int) ((((coordinates.get(q)[0] - coordinates.get(q + 1)[0])
					^ 2 + (coordinates.get(q)[1] - coordinates.get(q + 1)[1]) ^ 2) ^ (1 / 2)) / SPEED);
			// add to the list of time needed for travel
			timeNeeded.add(time);

		}

	}

	/** get a list of time that has problems **/
	public void compareTime() {
		// to go though every element of the array lists and compare the time
		for (int i = 0; i < timeNeeded.size(); i++) {
			// if the time between is not enough for commute
			if (interval.get(i) < timeNeeded.get(i)) {
				// add the according index to the array list
				notEnoughTime.add(i);
			}
		}

	}

	/**
	 * read the time input
	 * 
	 * @throws ParseException
	 **/
	private Date[] readTime() throws ParseException {

		// create the date
		Date[] mythistime = new Date[2];

		// get the start time
		String start_hour = hourlist.getSelectedItem().toString();
		String start_minute = minutelist.getSelectedItem().toString();

		// get the duration
		String dur_hour = endhour.getSelectedItem().toString();
		String dur_minute = endminute.getSelectedItem().toString();

		// record the two times in the good form
		String time1 = start_hour + ":" + start_minute;
		String time2 = dur_hour + ":" + dur_minute;

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date1 = timeFormat.parse(time1);
		Date date2 = timeFormat.parse(time2);

		mythistime[0] = date1;
		mythistime[1] = date2;

		long sum = date2.getTime() - date1.getTime();
		System.out.println("This is the duration in Long:  " + sum);

		// add the interval to the list get it in the minute form
		interval.add((int) (sum / 60000));

		String date3 = timeFormat.format(new Date(sum));
		System.out.println("The interval is " + date3);

		return mythistime;

	}

	/** opens a map file and update the map data in the application **/
	public void pressedUpload() {

		// Create a new FileChooser
		JFileChooser fileChooser = new JFileChooser();
		String fileString = "";

		// Open the fileChooser dialog
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			// Create a Buffered Reader to enable readLine functionality
			BufferedReader reader;
			try {

				// Create a new fileRead with the file selected from the
				// fileChooser
				FileReader fileRead = new FileReader(
						fileChooser.getSelectedFile());

				// Populate the BufferedReader with the FileReader data
				reader = new BufferedReader(fileRead);

				// Get the first line of the file
				String currentLine = reader.readLine();

				// While the current line isn't null
				while (currentLine != null) {

					myMap.add(currentLine);

				}

				// update the map array get ready to show
				readMap(myMap);

				// Close the BufferedReader to prevent memory leaks
				reader.close();
			}
			// If an error occurs throw an exception
			catch (FileNotFoundException ex) {
				System.err.println(ex.getMessage());
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}

	}

	/** save the schedule **/
	public void pressedSave() {

		// Create a new FileChooser
		JFileChooser chooser = new JFileChooser();

		// Open the fileChooser dialog
		int userChoice = chooser.showSaveDialog(this);
		if (userChoice == JFileChooser.APPROVE_OPTION) {
			try {
				// Create a new fileWrite with the file selected from the
				// fileChooser
				FileWriter writer = new FileWriter(chooser.getSelectedFile());

				// Write out the data inside of the instance String fileData to
				// the writer
				writer.write(scheduleFile);

				// Flush and Close the file which will save it to location
				writer.flush();
				writer.close();
			}
			// If an error occurs throw an exception
			catch (FileNotFoundException ex) {
				System.err.println(ex.getMessage());
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}

	}

	/** happens when the user want to delete an item on the schedule **/
	public void pressedDelete() {

		// search and delete the element in the array list
		String dontwant = "";
		dontwant = activityinput.getText();

		// go though the list of activities
		for (int i = 0; i < placeActivity.size(); i++) {
			// find the one with the name the user want to delete
			if (placeActivity.get(i).equals(dontwant)) {
				// remove it from the place list
				placeActivity.remove(i);
				// recalculate the time need the the time allowed
				// case 1
				// if the deleted item is the first of the list
				// case2
				// if the item is the last in the list
				if (i == 0 || (i == placeActivity.size() - 1)) {
					// just delete the first element of the other two lists
					interval.remove(i);
					timeNeeded.remove(i);

				}

				/*
				 * else if (i == placeActivity.size() - 1) { // just delete the
				 * first element of the other two lists interval.remove(i);
				 * timeNeeded.remove(i); }
				 */
				// case 3
				// if the item in the middle of the list
				else {
					// assign (i-1) to be the new time interval
					// find start and end time for that activity
					// add the duration to the sum of the original two intervals
					int newtimehere = 0;
					int temp = 0;
					// temp node to go through the list
					LinkedListNode<Date> tempNode1 = schedule.getFirstNode();

					// get the corresponding node we are deleting
					while (temp < i) {
						tempNode1 = tempNode1.getNext();
						newtimehere++;
					}
					// calculate the new time interval
					int sum = (int) ((tempNode1.getEnd().getTime() - tempNode1
							.getStart().getTime()) / 60000);

					newtimehere = interval.get(i) + interval.get(i - 1) + sum;

					// delete item i
					interval.set(i - 1, newtimehere);
					interval.remove(i);

				}

				// update the not enough time list
				compareTime();
			}

		}

	}

	/** update the display according to the new information **/
	public void pressedSubmit() {

		// temp array to store start time and end time
		Date[] tempTime = new Date[2];
		try {
			tempTime = readTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// temp node help to go through the list
		LinkedListNode<Date> thisnode = new LinkedListNode<Date>();

		// a temp array to store the place and activity
		String[] theTwo = new String[2];
		// store them
		theTwo[0] = placeinput.getText();

		theTwo[1] = activityinput.getText();
		// public LinkedList<Date[]> schedule;

		// if this is the first element added to the schedule
		if (schedule.isEmpty()) {
			// just add the things to the list
			schedule.insertFirst(tempTime[0], tempTime[1]);

		}

		// compare and then add
		else {
			// start from the first of the list
			thisnode = schedule.getFirstNode();

			// while we are still in the list and the time is still before what
			// we need to enter
			while (thisnode.getNext() != null
					&& thisnode.getStart().before(tempTime[0])) {

				// loop to the next in the list
				thisnode = thisnode.getNext();
			}

			// insert after the final node or the last one before the new node
			schedule.insertAfter(thisnode, tempTime[0], tempTime[1]);

		}

		placeActivity.add(theTwo);
		// System.out.println(theTwo[1]);

		showSchedule();
		// repaint();

		// if there is an element in the list where time is not enough

		if (!notEnoughTime.isEmpty()) {
			String message = "";
			for (int i = 0; i < notEnoughTime.size(); i++) {
				message = message + placeActivity.get(i) + "\n";
			}

			JOptionPane.showMessageDialog(guiFrame, message);
		}
	}

	/** print the schedule from the user **/
	public JPanel showSchedule() {
		// Create a panel to hold GUI elements
		JPanel biggerPanel = new JPanel();
		biggerPanel.setSize(900, 300);

		// the schedule part
		JPanel mySchedule = new JPanel();

		// set the layout for the title and the body
		biggerPanel.setLayout(new BorderLayout());

		// use the box layout because we don't know how may items the user will
		// input
		mySchedule.setLayout(new BoxLayout(mySchedule, BoxLayout.Y_AXIS));
		// mySchedule.setLayout(new GridLayout(10, 1));

		// create a temp node to go through the list
		LinkedListNode temp = new LinkedListNode();

		/*
		 * // when the user has not input any data if (schedule == null) {
		 * return mySchedule; }
		 */

		temp = schedule.getFirstNode();
		int i = 0;
		JTextField newLine = new JTextField();
		String printdchedule = "";
		// while we are still in the list
		while (temp != null) {

			String println;
			println = "This is the " + i + " th activity:  "
					+ placeActivity.get(i)[1];
			// add the data to a text field
			//System.out.println(println);
			// make it not editable

			// add the line to the panel
			printdchedule = printdchedule + println + "\n";

			// validate();

			//mySchedule.add(new JTextField(i));

			// update the temp node
			temp = temp.getNext();
			// update the number
			i = i + 1;
			validate();

		}

		newLine.setText(printdchedule);
		// newLine.setEditable(false);
		mySchedule.add(new JTextField(printdchedule));
		System.out.println("This is " + printdchedule);

		mySchedule.add(newLine);

		// mySchedule.add(new JButton("dont do this"));
		scheduleTitle.setEditable(false);
		biggerPanel.add(scheduleTitle, BorderLayout.NORTH);
		biggerPanel.add(mySchedule, BorderLayout.CENTER);

		// validate();

		// return
		return biggerPanel;
	}

	/** create the default map **/
	public void createmap() {

		// create a random map
		map[16][11] = "library";
		map[6][16] = "skinner green";
		map[8][4] = "porter";
		map[9][17] = "blanchard";
		map[10][7] = "wilder";
		map[14][9] = "kendade";
		map[12][12] = "clapp";
		map[7][4] = "gym";
		map[7][5] = "prospect";
		map[9][13] = "skinner";
		map[2][14] = "ciruti";
		map[15][6] = "reese";
		map[6][17] = "carr";
		map[8][12] = "dwight";
		// map[15][7] = "rocky";
		// map[5][10] = "ham";

	}

	/** put the map in the array format **/
	public void readMap(ArrayList m) {
		// create an array to hold the coordinates
		// array to hold the names
		ArrayList locations = new ArrayList();
		ArrayList names = new ArrayList();

		// divide the data to places and locations
		for (int i = 0; i < m.size(); i++) {
			// change the data to string
			String line = m.get(i).toString();
			// if the length of the string is smaller than or equal to 2,
			// it is a coordinate
			if (line.length() <= 2) {
				locations.add(m.get(i));

			}
			// otherwise add it to the name list
			else {
				names.add(m.get(i));
			}
		}

	}

	/** main method to start the application **/
	public static void main(String[] args) {
		// create the frame to hold the game
		guiFrame = new JFrame("Route Check-master");

		// set size
		guiFrame.setSize(900, 1000);
		// add the panel
		guiFrame.add(new GUI());
		// exit normally on closing the window
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// show frame
		guiFrame.setVisible(true);

	}

}
