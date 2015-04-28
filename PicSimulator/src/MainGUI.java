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
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JRadioButton;


public class MainGUI extends JFrame{

	private JPanel contentPane;
	private JButton btnStart;
	public ParDecInt pardecint;
	private CodeTable codetab;
	public static RegTable regtab;
	public static JScrollPane scrollPane = new JScrollPane();
	public static JScrollPane scrollPane_1 = new JScrollPane();
	public static JSlider slider = new JSlider(0,1000,500);
	public static JTextField textField = new JTextField();
	public static JTextField textField_1 = new JTextField();
	static JRadioButton radioButtonPortAPin0 = new JRadioButton("");
	static JRadioButton radioButtonPortAPin1 = new JRadioButton("");
	static JRadioButton radioButtonPortAPin2 = new JRadioButton("");
	static JRadioButton radioButtonPortAPin3 = new JRadioButton("");
	static JRadioButton radioButtonPortAPin4 = new JRadioButton("");
	JRadioButton radioButtonPortATris7 = new JRadioButton("");
	JRadioButton radioButtonPortATris6 = new JRadioButton("");
	JRadioButton radioButtonPortATris5 = new JRadioButton("");
	JRadioButton radioButtonPortATris4 = new JRadioButton("");
	JRadioButton radioButtonPortATris3 = new JRadioButton("");
	JRadioButton radioButtonPortATris2 = new JRadioButton("");
	JRadioButton radioButtonPortATris1 = new JRadioButton("");
	JRadioButton radioButtonPortATris0 = new JRadioButton("");
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
		setTitle("Pic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1156, 825);
		
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
		
		slider.setBounds(237, 11, 95, 23);
		contentPane.add(slider);
		
		JLabel lblPortA = new JLabel("Port A");
		lblPortA.setBounds(62, 532, 46, 14);
		contentPane.add(lblPortA);
		
		JLabel lblTris = new JLabel("Tris");
		lblTris.setBounds(110, 532, 46, 14);
		contentPane.add(lblTris);
		
		JLabel lblPin = new JLabel("Pin");
		lblPin.setBounds(22, 532, 46, 14);
		contentPane.add(lblPin);
		
		JLabel label = new JLabel("7");
		label.setBounds(22, 548, 14, 14);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("6");
		label_1.setBounds(22, 573, 14, 14);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("5");
		label_2.setBounds(22, 598, 14, 14);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("4");
		label_3.setBounds(22, 623, 14, 14);
		contentPane.add(label_3);
		
		JLabel label_4 = new JLabel("3");
		label_4.setBounds(22, 648, 14, 14);
		contentPane.add(label_4);
		
		JLabel label_5 = new JLabel("2");
		label_5.setBounds(22, 673, 14, 14);
		contentPane.add(label_5);
		
		JLabel label_6 = new JLabel("1");
		label_6.setBounds(22, 698, 14, 14);
		contentPane.add(label_6);
		
		JLabel label_7 = new JLabel("0");
		label_7.setBounds(22, 723, 14, 14);
		contentPane.add(label_7);
		
		radioButtonPortAPin4.setBounds(62, 619, 21, 23);
		contentPane.add(radioButtonPortAPin4);
		
		radioButtonPortAPin3.setBounds(62, 644, 21, 23);
		contentPane.add(radioButtonPortAPin3);
		
		radioButtonPortAPin2.setBounds(62, 669, 21, 23);
		contentPane.add(radioButtonPortAPin2);
		
		radioButtonPortAPin1.setBounds(62, 694, 21, 23);
		contentPane.add(radioButtonPortAPin1);
		
		radioButtonPortAPin0.setBounds(62, 719, 21, 23);
		contentPane.add(radioButtonPortAPin0);
		
		radioButtonPortATris7.setEnabled(false);
		radioButtonPortATris7.setBounds(110, 547, 21, 23);
		contentPane.add(radioButtonPortATris7);
		
		radioButtonPortATris6.setBounds(110, 573, 21, 23);
		contentPane.add(radioButtonPortATris6);
		
		radioButtonPortATris5.setBounds(110, 594, 21, 23);
		contentPane.add(radioButtonPortATris5);
		
		radioButtonPortATris4.setBounds(110, 619, 21, 23);
		contentPane.add(radioButtonPortATris4);
		
		radioButtonPortATris3.setBounds(110, 644, 21, 23);
		contentPane.add(radioButtonPortATris3);
		
		radioButtonPortATris2.setBounds(110, 669, 21, 23);
		contentPane.add(radioButtonPortATris2);
		
		radioButtonPortATris1.setBounds(110, 694, 21, 23);
		contentPane.add(radioButtonPortATris1);
		
		radioButtonPortATris0.setBounds(110, 719, 21, 23);
		contentPane.add(radioButtonPortATris0);
		
		JButton btnTestbutton = new JButton("Testbutton");
		btnTestbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ParDecInt.reg.setRegister0(6, 1);
			}
		});
		btnTestbutton.setBounds(243, 71, 89, 23);
		contentPane.add(btnTestbutton);
		
		JLabel lblWregister = new JLabel("W-Register");
		lblWregister.setBounds(994, 532, 62, 14);
		contentPane.add(lblWregister);
		
		
		textField.setEditable(false);
		textField.setBounds(1073, 529, 40, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1.setEditable(false);
		textField_1.setBounds(1073, 557, 40, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblBank = new JLabel("Bank");
		lblBank.setBounds(994, 557, 46, 14);
		contentPane.add(lblBank);
		
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
	
	public void setTrisPortA(){
		
	}
	
	public static int getPinsPortA(){
		int buf[] = new int[5];
		int buffer=0;
		if(radioButtonPortAPin0.isSelected()==true){
			buf[0]=1;
			System.out.println("Pin0 Port A ist aktiv");
		}else{
			buf[0]=0;
		}
		if(radioButtonPortAPin1.isSelected()==true){
			buf[1]=1;
		}else{
			buf[1]=0;
		}
		if(radioButtonPortAPin2.isSelected()==true){
			buf[2]=1;
		}else{
			buf[2]=0;
		}
		if(radioButtonPortAPin3.isSelected()==true){
			buf[3]=1;
		}else{
			buf[3]=0;
		}
		if(radioButtonPortAPin4.isSelected()==true){
			buf[4]=1;
		}else{
			buf[4]=0;
		}
		for(int i =0; i<= 4; i++){
			if(buf[i]==1)
				buffer=(int) (buffer+Math.pow(2,i));
		}
		return buffer;
	}
	
	public int[] getTrisPortA(){
		int buf[] = new int[5];
		if(radioButtonPortATris0.isSelected()==true){
			buf[0]=1;
		}else{
			buf[0]=0;
		}
		if(radioButtonPortATris1.isSelected()==true){
			buf[1]=1;
		}else{
			buf[1]=0;
		}
		if(radioButtonPortATris2.isSelected()==true){
			buf[2]=1;
		}else{
			buf[2]=0;
		}
		if(radioButtonPortATris3.isSelected()==true){
			buf[3]=1;
		}else{
			buf[3]=0;
		}
		if(radioButtonPortATris4.isSelected()==true){
			buf[4]=1;
		}else{
			buf[4]=0;
		}
		if(radioButtonPortATris5.isSelected()==true){
			buf[5]=1;
		}else{
			buf[5]=0;
		}
		if(radioButtonPortATris6.isSelected()==true){
			buf[6]=1;
		}else{
			buf[6]=0;
		}
		if(radioButtonPortATris7.isSelected()==true){
			buf[7]=1;
		}else{
			buf[7]=0;
		}
		return buf;
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
