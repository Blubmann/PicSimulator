import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
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
import javax.swing.JLayeredPane;


public class MainGUI extends JFrame{

	private JPanel contentPane;
	private JButton btnStart;
	public ParDecInt pardecint;
	private CodeTable codetab;
	public static RegTable regtab;
	private JScrollPane scrollPane = new JScrollPane();
	public static JScrollPane scrollPane_1 = new JScrollPane();
	private Boolean checkbox;
	public static boolean run;
	public static boolean step;

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
				run= true;
				step =false;
		        btnStart.setEnabled(false);
		        ParDecInt pardecintThread = new ParDecInt();
		        pardecintThread.start(); 
		        //System.out.println("Test");
			}
		});
		btnStart.setBounds(10, 11, 89, 23);
		contentPane.add(btnStart);		
		
		
		scrollPane.setBounds(358, 15, 698, 465);
		contentPane.add(scrollPane);		
		codetab = new CodeTable(scrollPane);
		
		scrollPane_1.setBounds(22, 95, 310, 385);
		contentPane.add(scrollPane_1);
		regtab = new RegTable(scrollPane_1);
		
		JButton btnBank = new JButton("Bank0");
		btnBank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				regtab.updateTable0(scrollPane_1);
			}
		});
		btnBank.setBounds(22, 71, 89, 23);
		contentPane.add(btnBank);
		
		JButton button = new JButton("Bank1");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regtab.updateTable1(scrollPane_1);
			}
		});
		button.setBounds(110, 71, 89, 23);
		contentPane.add(button);
		
		JButton btnStepbystep = new JButton("Step-by-Step");
		btnStepbystep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run= false;
				step =true;
				ParDecInt pardecintThread = new ParDecInt();
		        pardecintThread.start(); 
			}
		});
		btnStepbystep.setBounds(98, 11, 101, 23);
		contentPane.add(btnStepbystep);
		
	}	
	
	public void openclicked() {
		File file =null;
		/** Öffnen des Dialogs für die Auswahl der Datei**/
		JFileChooser open = new JFileChooser();
		open.setDialogTitle("Datei öffnen");
		/**Begrenzt die Auswahl auf lst-Files**/
		open.setAcceptAllFileFilterUsed(false);
		open.addChoosableFileFilter(new FileNameExtensionFilter("LST-Files", "LST"));
		/** rVal bekommt bei bestätigen eine Konstante übergeben**/
		int rVal = open.showOpenDialog(null);
		/**Prüfen einer Konstante, welcher Button gedrückt wurde**/
		if (rVal == JFileChooser.APPROVE_OPTION) {
			/**Ausgewählte Datei wird gespeichert, die Codetabelle auf der GUI wird gelöscht, die Datei wird weitergereicht**/
            file = open.getSelectedFile();
            codetab.removeAll();
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
			/**Zählen der Zeilen mit Programmcode um ein entsprechende Arraygröße für relevanten Code zu erstellen**/
			while ((record = countbr.readLine()) != null) {
	            if (!record.startsWith("    ")) {
	                lineCount++;
	            }
	        }
			Buffer = new String[lineCount];
			Vector tableData = new Vector(); //Vector für Tabellen Daten
			/**File wird Zeile für Zeile ausgelesen und die relevanten Codezeilen in das Array Buffer geschrieben,
			 * Der komplette Quellcode wird in ein Vektor geschrieben und über setTable auf der GUI angezeigt
			 */
	        while ((record = br.readLine()) != null) {
	        	Vector vec = new Vector();
	        	/**Relevanten Code filtern**/
	        	if (!record.startsWith("    ")) {
	        		Buffer[j] = record;
	        		j++;
	        	}
	        	/**Vektor eins wird ein Boolean(für die Checkboxes). Vektor zwei wird der Sourcecode, 
	        	 * wobei die ersten 20 Zeichen abgeschnitten werden, da diese nicht dargestellt werden müssen
	        	 */
	        	vec.add(checkbox); 
	        	record = record.substring(20);
	        	vec.add(record + "\n");
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
	        	//System.out.println(tableData);
	        }
	        codetab.setTable(scrollPane, tableData);
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
