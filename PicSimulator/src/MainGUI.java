import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToggleButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.table.DefaultTableModel;


public class MainGUI extends JFrame{

	private JPanel contentPane;
	public ParDecInt pdi;
	private JButton btnStart;
	public ParDecInt pardecint;
	private RegTable regtab;
	private JScrollPane scrollPane = new JScrollPane();
	private Boolean checkbox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1095, 754);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("");
		menuBar.add(menu);
		
		JMenu mnMen = new JMenu("Men\u00FC");
		menuBar.add(mnMen);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				openclicked();
			}
		});
		mnMen.add(mntmOpen);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnMen.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
		        btnStart.setEnabled(false);
		        ParDecInt pardecintThread = new ParDecInt();
		        pardecintThread.start(); 
		        //System.out.println("Test");
			}
		});
		btnStart.setBounds(10, 11, 89, 23);
		contentPane.add(btnStart);

		
		
		/**JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(358, 15, 698, 465);
		contentPane.add(scrollPane);		
		table_1 = new JTable();
		upt.UpdateTable(table_1,null);
		scrollPane.setViewportView(table_1);**/
		
		
		scrollPane.setBounds(358, 15, 698, 465);
		contentPane.add(scrollPane);		
		regtab = new RegTable(scrollPane);
		
	}	
	
	public void openclicked() {
		
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
            regtab.removeAll();
            readandwrite(file);
        }else if(rVal==JFileChooser.CANCEL_OPTION){
        	System.out.println("JFileChooser canceled");
        }
	}
		
	private void readandwrite(File file){
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
			Vector tableData = new Vector(); //Vector für Tabellen Daten
	        while ((record = br.readLine()) != null) {
	        	Vector vec = new Vector();
	        	if (!record.startsWith("    ")) {
	        		Buffer[j] = record; //Schreibt Programmcode in den Buffer
	        		j++;
	        	}
	        	vec.add(checkbox); //Erste Spalte der Tabelle ist eine Checkbox
	        	record = record.substring(20); //schneidet die ersten 20 Zeichen vom Code weg
	        	vec.add(record + "\n"); //Übergibt den Codeteil an Vector in Spalte zwei
	        	tableData.addElement(vec); //Schreibt Spalte eins und zwei in den Tabellenvektor
	        	
	        	
	           /**Vector<Object> vector = new Vector<Object>();
	           for (int i = 0; i < list.getModel().getSize(); i++) {
	                vector.add(list.getModel().getElementAt(i));
	            }
	            Buffer[j] = record;
	            j++;
	            record = record.substring(20);
	            vector.add(record + "\n");
				list.setListData(vector);**/
	        	//System.out.println(tableData);
	        }
	        regtab.setTable(scrollPane, tableData);
	        pardecint = new ParDecInt(Buffer);
	        br.close();
			countbr.close();
			//System.out.println(Buffer[1]);
		}catch (IOException e) {
            System.out.println("Es ist ein Fehler beim lesen der Datei aufgetreten");
            e.printStackTrace();
        }
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
