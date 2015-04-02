import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.text.TabExpander;



public class GUIOperations {
	
	public ParDecInt pardecint;
		
	public void openclicked(ActionEvent evt, JTable table_1) {
		
		File file =null;
		//boolean markerZeileLeer = false;
		/** Öffnen des Dialogs für die Auswahl der Datei**/
		JFileChooser open = new JFileChooser();
		open.setDialogTitle("Datei öffnen");
		/** rVal bekommt bei bestätigen eine Konstante übergeben**/
		int rVal = open.showOpenDialog(null);
		/**Prüfen der Konstante**/
		if (rVal == JFileChooser.APPROVE_OPTION) {
            file = open.getSelectedFile();
            readandwrite(file, table_1);
        }else if(rVal==JFileChooser.CANCEL_OPTION){
        	System.out.println("JFileChooser canceled");
        }
		
		
	}
	
	private void readandwrite(File file, JTable table_1){
		String[] Buffer; //Buffer für Programmcode
		int j= 0;
		int lineCount = 0;
		String record = null;
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			BufferedReader countbr = new BufferedReader(new FileReader(file));
			/**Zählen der Zeilen mit Programmcode**/
			while ((record = countbr.readLine()) != null) {
	            if (!record.startsWith("    ")) {
	                lineCount++;
	            }
	        }
			Buffer = new String[lineCount];
			Vector tableData = new Vector();
	        Vector tabellenkopf = new Vector(); 
	                tabellenkopf.add("Spalte 1"); 
	                tabellenkopf.add("Spalte 2"); 
	        while ((record = br.readLine()) != null) {
	        	Vector vec = new Vector();
	        	vec.add("");
	        	vec.add("Test");
	        	tableData.addElement(vec);
	        	
	        	
	           /**Vector<Object> vector = new Vector<Object>();
	           for (int i = 0; i < list.getModel().getSize(); i++) {
	                vector.add(list.getModel().getElementAt(i));
	            }
	            Buffer[j] = record;
	            j++;
	            record = record.substring(20);
	            vector.add(record + "\n");
				list.setListData(vector);**/
	        }
	        table_1 = new JTable(tableData, tabellenkopf);
	        pardecint = new ParDecInt(Buffer);
	        br.close();
			countbr.close();
			//System.out.println(Buffer[1]);
		}catch (IOException e) {
            System.out.println("Es ist ein Fehler beim lesen der Datei aufgetreten");
            e.printStackTrace();
        }
	}

}
