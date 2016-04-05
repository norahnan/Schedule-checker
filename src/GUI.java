import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GUI extends JPanel{
	
	
	public ArrayList read()
	{
		ArrayList m = new ArrayList();
		//read from the txt file
				try(BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"))) {
				    
				    String line = br.readLine();

				    while ((line=br.readLine()) !=null) {
				    	
				    	//add each line to the array list
				    	m.add(line);
				    	
				    }
				   
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return m;
	}
	

}
