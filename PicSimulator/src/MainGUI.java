import java.awt.BorderLayout;
import java.awt.Desktop;
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
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLayeredPane;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 * Die Klasse ist f�r die Darstellung und Verwaltung der GUI da.
 */
public class MainGUI extends JFrame{

	private JPanel contentPane;
	public Worker worker;
	public static CodeTable codetab;
	public static RegTable regtab;
    public static ComPort comport;
	public static JScrollPane scrollPane = new JScrollPane();
	public static JScrollPane scrollPane_1 = new JScrollPane();
	public static JScrollPane scrollPane_2 = new JScrollPane();
	public static JList stackList = new JList();
	public static JSlider slider = new JSlider(1000000,80000000,40000000);
	public static JTextField textField_WReg = new JTextField();
	public static JTextField textField_Bank = new JTextField();
	public static JTextField textField_Status = new JTextField();
	public static JTextField textField_DC = new JTextField();
	public static JTextField textField_C = new JTextField();
	public static JTextField textField_Z = new JTextField();
	public static JTextField textField_PCL = new JTextField();
	public static JTextField textField_Cycle = new JTextField();
	public static JTextField textField_Laufzeit = new JTextField();
	public static JTextField textField_Watchdog = new JTextField();
	public static JTextField textField_Frequenz = new JTextField();
	public static JTextField textField_Prescaler = new JTextField();
	static JRadioButton radioButtonPortAPin0 = new JRadioButton("");
	static JRadioButton radioButtonPortAPin1 = new JRadioButton("");
	static JRadioButton radioButtonPortAPin2 = new JRadioButton("");
	static JRadioButton radioButtonPortAPin3 = new JRadioButton("");
	static JRadioButton radioButtonPortAPin4 = new JRadioButton("");
	static JRadioButton radioButtonPortBPin0 = new JRadioButton("");
	static JRadioButton radioButtonPortBPin1 = new JRadioButton("");
	static JRadioButton radioButtonPortBPin2 = new JRadioButton("");
	static JRadioButton radioButtonPortBPin3 = new JRadioButton("");
	static JRadioButton radioButtonPortBPin4 = new JRadioButton("");
	static JRadioButton radioButtonPortBPin5 = new JRadioButton("");
	static JRadioButton radioButtonPortBPin6 = new JRadioButton("");
	static JRadioButton radioButtonPortBPin7 = new JRadioButton("");
	static JRadioButton radioButtonPortATris4 = new JRadioButton("");
	static JRadioButton radioButtonPortATris3 = new JRadioButton("");
	static JRadioButton radioButtonPortATris2 = new JRadioButton("");
	static JRadioButton radioButtonPortATris1 = new JRadioButton("");
	static JRadioButton radioButtonPortATris0 = new JRadioButton("");
	static JRadioButton radioButtonPortBTris7 = new JRadioButton("");
	static JRadioButton radioButtonPortBTris6 = new JRadioButton("");
	static JRadioButton radioButtonPortBTris5 = new JRadioButton("");
	static JRadioButton radioButtonPortBTris4 = new JRadioButton("");
	static JRadioButton radioButtonPortBTris3 = new JRadioButton("");
	static JRadioButton radioButtonPortBTris2 = new JRadioButton("");
	static JRadioButton radioButtonPortBTris1 = new JRadioButton("");
	static JRadioButton radioButtonPortBTris0 = new JRadioButton("");
	static JRadioButton radioButton_Watchdog = new JRadioButton("");
	private Boolean checkbox;
	public static boolean bank=true;
	public static boolean run;
	public static boolean step;
	public static boolean comPortEnable=false;
	public static JButton btnStart = new JButton("Start");
	public static JButton btnStepbystep = new JButton("Step-by-Step");
	private JButton btnReset = new JButton("Reset");
	private static JButton btnConnect = new JButton("Connect");
	private final JLabel lblFrequenz = new JLabel("Frequenz");
	private final JLabel lblLaufzeit = new JLabel("Laufzeit");
	private final JLabel lblStack = new JLabel("Stack");
	private final JLabel lblWatchdog = new JLabel("Watchdog");
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
		textField_Frequenz.setEditable(false);
		textField_Frequenz.setBounds(296, 12, 46, 20);
		textField_Frequenz.setColumns(10);
		textField_Watchdog.setEditable(false);
		textField_Watchdog.setBounds(797, 727, 86, 20);
		textField_Watchdog.setColumns(10);
		textField_Laufzeit.setEditable(false);
		textField_Laufzeit.setBounds(392, 503, 86, 20);
		textField_Laufzeit.setColumns(10);
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
		
		
		/**
		 * actionListener f�r den Men�punkt "Help" um damit eine PDF aufzurufen
		 */
		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File("Datenblatt.pdf"));
				} catch (IOException e1) {
	
					e1.printStackTrace();
				}
			}
		});
		mnMen.add(mntmHelp);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnMen.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		/**
		 * Actionlistener f�r den Startknopf. Wenn noch keine Ausf�hrung stattfindet(run=false), wird die Variable run auf 
		 * true gesetzt(dies wird sp�ter f�r die �berpr�fung ben�tigt, ob der Start oder Step-by-Step Button gedr�ckt wurde)
		 * der Button bekommt das Label "Stop". Au�erdem wird der Step-by-Step Button deaktiviert, damit dieser nicht gedr�ckt
		 * werden kann und unter Umst�nden zu einem unvorhergesehenen Verhalten f�hrt. Es wird auch noch der Thread f�r die
		 * Abarbeitung des Programmcodes gestartet. Wird der Button danach ncoheinmal gedr�ckt, wird wieder in
		 * den Normalzustand gewechselt.
		 */
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(run==false){
					run= true;
					step =false;
					btnStart.setEnabled(false);
					Worker WorkerThread = new Worker();
					WorkerThread.start(); 
					btnStart.setText("Stop");
					btnStart.setEnabled(true);
					btnStepbystep.setEnabled(false);
				}else if(run==true){
					run= false;
					btnStart.setText("Start");
					btnStart.setEnabled(true);
					btnStepbystep.setEnabled(true);
				}
				
		        //System.out.println("Test");
			}
		});
		btnStart.setBounds(10, 11, 89, 23);
		contentPane.add(btnStart);	
		btnStart.setEnabled(false);
		
		
		scrollPane.setBounds(358, 15, 698, 465);
		contentPane.add(scrollPane);		
		codetab = new CodeTable(scrollPane);
		
		scrollPane_1.setBounds(22, 95, 310, 385);
		contentPane.add(scrollPane_1);
		regtab = new RegTable(scrollPane_1);
		
		/**
		 * Knopf um die Ansicht der Bank auf Bank0 zu wechseln
		 */
		JButton btnBank = new JButton("Bank0");
		btnBank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bank=true;
				regtab.updateTable0(scrollPane_1);
			}
		});
		btnBank.setBounds(22, 71, 89, 23);
		contentPane.add(btnBank);
		
		/**
		 * Knopf um die Ansicht der Bank auf Bank1 zu wechseln
		 */
		JButton button = new JButton("Bank1");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bank=false;
				regtab.updateTable1(scrollPane_1);
			}
		});
		button.setBounds(110, 71, 89, 23);
		contentPane.add(button);
		
		/**
		 * ActionListener f�r den Step-by-Step Button
		 */
		btnStepbystep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run= false;
				step =true;
				Worker WorkerThread = new Worker();
		        WorkerThread.start(); 
			}
		});
		btnStepbystep.setBounds(98, 11, 101, 23);
		contentPane.add(btnStepbystep);
		btnStepbystep.setEnabled(false);
		
		slider.setBounds(237, 37, 95, 23);
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
		radioButtonPortATris4.setEnabled(false);
		
		radioButtonPortATris4.setBounds(110, 619, 21, 23);
		contentPane.add(radioButtonPortATris4);
		radioButtonPortATris3.setEnabled(false);
		
		radioButtonPortATris3.setBounds(110, 644, 21, 23);
		contentPane.add(radioButtonPortATris3);
		radioButtonPortATris2.setEnabled(false);
		
		radioButtonPortATris2.setBounds(110, 669, 21, 23);
		contentPane.add(radioButtonPortATris2);
		radioButtonPortATris1.setEnabled(false);
		
		radioButtonPortATris1.setBounds(110, 694, 21, 23);
		contentPane.add(radioButtonPortATris1);
		radioButtonPortATris0.setEnabled(false);
		
		radioButtonPortATris0.setBounds(110, 719, 21, 23);
		contentPane.add(radioButtonPortATris0);
		
		/**
		 * ActonListener f�r den Comport Button. Besteht noch keine Verbindung mit einer seriellen Schnittstelle, wird ein 
		 * Objekt der Klasse ComPortConnector erzeugt. Es wird die entsprechende GUI angezeigt. ISt eine serielle Schnittstelle
		 * verbunden, so wird diese beim nochmaligen Dr�cken des Buttons unterbrochen
		 */
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!comPortEnable){
					ComPortConnector commy = new ComPortConnector();
				}else{
					disconnectComPort();
				}
			}
		});
		btnConnect.setBounds(243, 71, 89, 23);
		contentPane.add(btnConnect);
		
		JLabel lblWregister = new JLabel("W");
		lblWregister.setBounds(336, 534, 62, 14);
		contentPane.add(lblWregister);
		
		
		textField_WReg.setEditable(false);
		textField_WReg.setBounds(392, 531, 40, 20);
		contentPane.add(textField_WReg);
		textField_WReg.setColumns(10);
		
		textField_Bank.setEditable(false);
		textField_Bank.setBounds(392, 559, 40, 20);
		contentPane.add(textField_Bank);
		textField_Bank.setColumns(10);
		
		JLabel lblBank = new JLabel("Bank");
		lblBank.setBounds(336, 562, 62, 14);
		contentPane.add(lblBank);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(336, 590, 46, 14);
		contentPane.add(lblStatus);
		
		
		textField_Status.setEditable(false);
		textField_Status.setBounds(392, 587, 40, 20);
		contentPane.add(textField_Status);
		textField_Status.setColumns(10);
		
		JLabel lblDc = new JLabel("DC");
		lblDc.setBounds(336, 618, 46, 14);
		contentPane.add(lblDc);
		
		JLabel lblC = new JLabel("C");
		lblC.setBounds(336, 646, 62, 14);
		contentPane.add(lblC);
		
		JLabel lblZ = new JLabel("Z");
		lblZ.setBounds(336, 674, 46, 14);
		contentPane.add(lblZ);
		
		textField_DC.setEditable(false);
		textField_DC.setBounds(392, 615, 40, 20);
		contentPane.add(textField_DC);
		textField_DC.setColumns(10);
		
		textField_C.setEditable(false);
		textField_C.setBounds(392, 643, 40, 20);
		contentPane.add(textField_C);
		textField_C.setColumns(10);
		
		textField_Z.setEditable(false);
		textField_Z.setBounds(392, 671, 40, 20);
		contentPane.add(textField_Z);
		textField_Z.setColumns(10);
		
		JLabel lblPortB = new JLabel("Port B");
		lblPortB.setBounds(166, 532, 46, 14);
		contentPane.add(lblPortB);
		
		JLabel label_9 = new JLabel("Tris");
		label_9.setBounds(214, 532, 46, 14);
		contentPane.add(label_9);
		
		radioButtonPortBTris7.setEnabled(false);
		radioButtonPortBTris7.setBounds(214, 547, 21, 23);
		contentPane.add(radioButtonPortBTris7);
		
		radioButtonPortBTris6.setEnabled(false);
		radioButtonPortBTris6.setBounds(214, 573, 21, 23);
		contentPane.add(radioButtonPortBTris6);
		
		radioButtonPortBTris5.setEnabled(false);
		radioButtonPortBTris5.setBounds(214, 594, 21, 23);
		contentPane.add(radioButtonPortBTris5);
		
		radioButtonPortBTris4.setEnabled(false);
		radioButtonPortBTris4.setBounds(214, 619, 21, 23);
		contentPane.add(radioButtonPortBTris4);
		
		radioButtonPortBTris3.setEnabled(false);
		radioButtonPortBTris3.setBounds(214, 644, 21, 23);
		contentPane.add(radioButtonPortBTris3);
		
		radioButtonPortBTris2.setEnabled(false);
		radioButtonPortBTris2.setBounds(214, 669, 21, 23);
		contentPane.add(radioButtonPortBTris2);
		
		radioButtonPortBTris1.setEnabled(false);
		radioButtonPortBTris1.setBounds(214, 694, 21, 23);
		contentPane.add(radioButtonPortBTris1);
		radioButtonPortBTris0.setEnabled(false);
		radioButtonPortBTris0.setBounds(214, 719, 21, 23);
		contentPane.add(radioButtonPortBTris0);
		
		radioButtonPortBPin0.setBounds(166, 719, 21, 23);
		contentPane.add(radioButtonPortBPin0);
		
		radioButtonPortBPin1.setBounds(166, 694, 21, 23);
		contentPane.add(radioButtonPortBPin1);
		
		radioButtonPortBPin2.setBounds(166, 669, 21, 23);
		contentPane.add(radioButtonPortBPin2);
		
		radioButtonPortBPin3.setBounds(166, 644, 21, 23);
		contentPane.add(radioButtonPortBPin3);
		
		radioButtonPortBPin4.setBounds(166, 619, 21, 23);
		contentPane.add(radioButtonPortBPin4);
		
		radioButtonPortBPin5.setBounds(166, 594, 21, 23);
		contentPane.add(radioButtonPortBPin5);
		
		radioButtonPortBPin6.setBounds(166, 569, 21, 23);
		contentPane.add(radioButtonPortBPin6);
		
		radioButtonPortBPin7.setBounds(166, 544, 21, 23);
		contentPane.add(radioButtonPortBPin7);
		
		
		scrollPane_2.setBounds(704, 530, 130, 171);
		contentPane.add(scrollPane_2);
		scrollPane_2.setViewportView(stackList);
		
		JLabel lblPcl = new JLabel("PCL");
		lblPcl.setBounds(336, 702, 46, 14);
		contentPane.add(lblPcl);
		
		textField_PCL.setEditable(false);
		textField_PCL.setBounds(392, 699, 40, 20);
		contentPane.add(textField_PCL);
		textField_PCL.setColumns(10);
		
		JLabel lblCycle = new JLabel("Cycle");
		lblCycle.setBounds(336, 730, 46, 14);
		contentPane.add(lblCycle);
		
		textField_Cycle.setEditable(false);
		textField_Cycle.setBounds(392, 727, 40, 20);
		contentPane.add(textField_Cycle);
		textField_Cycle.setColumns(10);
		lblFrequenz.setBounds(237, 15, 46, 14);
		
		contentPane.add(lblFrequenz);
		lblLaufzeit.setBounds(336, 506, 46, 14);
		
		contentPane.add(lblLaufzeit);
		contentPane.add(textField_Laufzeit);
		lblStack.setBounds(704, 506, 46, 14);
		
		contentPane.add(lblStack);
		lblWatchdog.setBounds(704, 730, 62, 14);
		
		contentPane.add(lblWatchdog);
		contentPane.add(textField_Watchdog);
		radioButton_Watchdog.setBounds(770, 725, 21, 23);
		contentPane.add(radioButton_Watchdog);
		
		contentPane.add(textField_Frequenz);
		
		JLabel lblPrescaler = new JLabel("Prescaler");
		lblPrescaler.setBounds(520, 506, 46, 14);
		contentPane.add(lblPrescaler);
		

		textField_Prescaler.setEditable(false);
		textField_Prescaler.setBounds(586, 503, 46, 20);
		contentPane.add(textField_Prescaler);
		textField_Prescaler.setColumns(10);
		
		/**
		 * ActionListener f�r den Reset Button. Ruft passende Methoden auf um den Pic in den Resetzustand zu bringen. Es werden
		 * entsprechende Anpassungen an der GUI durchgef�hrt.
		 */
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run = false;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Worker.reg.reset();
				Worker.reg.refreshGUI();
				btnStart.setText("Start");
				btnStepbystep.setEnabled(true);
			}
		});
		btnReset.setEnabled(false);
		btnReset.setBounds(10, 37, 89, 23);
		contentPane.add(btnReset);
	}	
	
	
	/**
	 * Erstellt den "Datei �ffnen"-Dialog(es werden nur .lst-Dateien akzeptiert), pr�ft ob eine Datei ausgew�hlt wurde und 
	 * �bergibt diese an die Methode readandwrite(). Die Buttons werden f�r den Gebrauch freigegeben.
	 */
	public void openclicked() {
		File file = null;
		JFileChooser open = new JFileChooser();
		open.setDialogTitle("Datei �ffnen");
		open.setAcceptAllFileFilterUsed(false);
		open.addChoosableFileFilter(new FileNameExtensionFilter("LST-Files", "LST"));
		int rVal = open.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
            file = open.getSelectedFile();
            codetab.removeAll();
            readandwrite(file);
            btnStart.setEnabled(true);
            btnStepbystep.setEnabled(true);
            btnReset.setEnabled(true);
        }else if(rVal==JFileChooser.CANCEL_OPTION){
        	System.out.println("JFileChooser canceled");
        }
	}
	
	
	/**
	 * readandwrite lie�t die Datei zeilenweise aus. Es werden relevante Codezeilen gez�hlt und ein Array mit dieser Gr��e 
	 * angelegt. Dann wird die Datei ein zweites Mal gelesen. Es werden die relevanten von den "unrelevante" Codezeilen
	 * getrennt und seperat gespeichert. Die Klasse Codetabelle bekommt den Vector mit allen Codezeilen �bergeben und stellt
	 * diese entsprechend da. Der Worker erh�lt das Array mit den relevanten Codezeilen.
	 */
	private void readandwrite(File file){
		String[] Buffer; //Buffer f�r Programmcode
		int j = 0;
		int lineCount = 0;
		String record = null;
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			BufferedReader countbr = new BufferedReader(new FileReader(file));
			while ((record = countbr.readLine()) != null) {
	            if (!record.startsWith("    ")) {
	                lineCount++;
	            }
	        }
			Buffer = new String[lineCount];	
			Vector tableData = new Vector();
	        while ((record = br.readLine()) != null) {
	        	Vector vec = new Vector();
	        	if (!record.startsWith("    ")) {
	        		Buffer[j] = record;
	        		j++;
	        	}
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
	        worker = new Worker(Buffer);
	        codetab.updateHighliner();
	        br.close();
			countbr.close();
			//System.out.println(Buffer[1]);
		}catch (IOException e) {
            System.out.println("Es ist ein Fehler beim lesen der Datei aufgetreten");
            e.printStackTrace();
        }
	}
	
	
	/**
	 * Gibt zur�ck ob der Radiobutton Watchdog gesetzt ist
	 */
	public static boolean getWatchdogEnable(){
		if(radioButton_Watchdog.isSelected() == true){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * Folgende Methoden lesen die gesetzten Radiobuttons aus, 
	 * bzw. holen sich den aktuellen Registerinhalt und setzen die Radobuttons entsprechend
	 */
	public static int getPinsPortA(){
		int buf[] = new int[5];
		int buffer = 0;
		if(radioButtonPortAPin0.isSelected()==true){
			buf[0] = 1;
		}else{
			buf[0] = 0;
		}
		if(radioButtonPortAPin1.isSelected()==true){
			buf[1] = 1;
		}else{
			buf[1] = 0;
		}
		if(radioButtonPortAPin2.isSelected()==true){
			buf[2] = 1;
		}else{
			buf[2] = 0;
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
	
	 public static void setPinsPortA() {
        int[] port = Worker.reg.getPortA();

        if (port[0] == 1) {
            radioButtonPortAPin0.setSelected(true);
        } else {
        	radioButtonPortAPin0.setSelected(false);
        }

        if (port[1] == 1) {
        	radioButtonPortAPin1.setSelected(true);
        } else {
        	radioButtonPortAPin1.setSelected(false);
        }

        if (port[2] == 1) {
        	radioButtonPortAPin2.setSelected(true);
        } else {
        	radioButtonPortAPin2.setSelected(false);
        }

        if (port[3] == 1) {
        	radioButtonPortAPin3.setSelected(true);
        } else {
        	radioButtonPortAPin3.setSelected(false);
        }

        if (port[4] == 1) {
        	radioButtonPortAPin4.setSelected(true);
        } else {
        	radioButtonPortAPin4.setSelected(false);
        }
	}
	      
	public static void setTrisPortA(){
		int[] trisA = Worker.reg.getTrisA();

        if (trisA[0] == 1) {
        	radioButtonPortATris0.setSelected(true);
        } else {
        	radioButtonPortATris0.setSelected(false);
        }
        if (trisA[1] == 1) {
        	radioButtonPortATris1.setSelected(true);
        } else {
        	radioButtonPortATris1.setSelected(false);
        }
        if (trisA[2] == 1) {
        	radioButtonPortATris2.setSelected(true);
        } else {
        	radioButtonPortATris2.setSelected(false);
        }
        if (trisA[3] == 1) {
        	radioButtonPortATris3.setSelected(true);
        } else {
        	radioButtonPortATris3.setSelected(false);
        }
        if (trisA[4] == 1) {
        	radioButtonPortATris4.setSelected(true);
        } else {
        	radioButtonPortATris4.setSelected(false);
        }
	}
	
	public static int getPinsPortB(){
		int buf[] = new int[8];
		int buffer=0;
		if(radioButtonPortBPin0.isSelected()==true){
			buf[0]=1;
		}else{
			buf[0]=0;
		}
		if(radioButtonPortBPin1.isSelected()==true){
			buf[1]=1;
		}else{
			buf[1]=0;
		}
		if(radioButtonPortBPin2.isSelected()==true){
			buf[2]=1;
		}else{
			buf[2]=0;
		}
		if(radioButtonPortBPin3.isSelected()==true){
			buf[3]=1;
		}else{
			buf[3]=0;
		}
		if(radioButtonPortBPin4.isSelected()==true){
			buf[4]=1;
		}else{
			buf[4]=0;
		}
		if(radioButtonPortBPin5.isSelected()==true){
			buf[5]=1;
		}else{
			buf[5]=0;
		}
		if(radioButtonPortBPin6.isSelected()==true){
			buf[6]=1;
		}else{
			buf[6]=0;
		}
		if(radioButtonPortBPin7.isSelected()==true){
			buf[7]=1;
		}else{
			buf[7]=0;
		}
		for(int i =0; i<= 7; i++){
			if(buf[i]==1)
				buffer=(int) (buffer+Math.pow(2,i));
		}
		return buffer;
	}
	
	public static void setPinsPortB() {
        int[] port = Worker.reg.getPortB();

        if (port[0] == 1) {
            radioButtonPortBPin0.setSelected(true);
        } else {
        	radioButtonPortBPin0.setSelected(false);
        }

        if (port[1] == 1) {
        	radioButtonPortBPin1.setSelected(true);
        } else {
        	radioButtonPortBPin1.setSelected(false);
        }

        if (port[2] == 1) {
        	radioButtonPortBPin2.setSelected(true);
        } else {
        	radioButtonPortBPin2.setSelected(false);
        }

        if (port[3] == 1) {
        	radioButtonPortBPin3.setSelected(true);
        } else {
        	radioButtonPortBPin3.setSelected(false);
        }

        if (port[4] == 1) {
        	radioButtonPortBPin4.setSelected(true);
        } else {
        	radioButtonPortBPin4.setSelected(false);
        }
        
        if (port[5] == 1) {
        	radioButtonPortBPin5.setSelected(true);
        } else {
        	radioButtonPortBPin5.setSelected(false);
        }
        
        if (port[6] == 1) {
        	radioButtonPortBPin6.setSelected(true);
        } else {
        	radioButtonPortBPin6.setSelected(false);
        }
        
        if (port[7] == 1) {
        	radioButtonPortBPin7.setSelected(true);
        } else {
        	radioButtonPortBPin7.setSelected(false);
        }
	}
	
	public static void setTrisPortB(){
		int[] trisB = Worker.reg.getTrisB();

		 if (trisB[0] == 1) {
			 radioButtonPortBTris0.setSelected(true);
        } else {
        	radioButtonPortBTris0.setSelected(false);
        }
        if (trisB[1] == 1) {
        	radioButtonPortBTris1.setSelected(true);
        } else {
        	radioButtonPortBTris1.setSelected(false);
        }
        if (trisB[2] == 1) {
        	radioButtonPortBTris2.setSelected(true);
        } else {
        	radioButtonPortBTris2.setSelected(false);
        }
        if (trisB[3] == 1) {
        	radioButtonPortBTris3.setSelected(true);
        } else {
        	radioButtonPortBTris3.setSelected(false);
        }
        if (trisB[4] == 1) {
        	radioButtonPortBTris4.setSelected(true);
        } else {
        	radioButtonPortBTris4.setSelected(false);
        }
        if (trisB[5] == 1) {
        	radioButtonPortBTris5.setSelected(true);
        } else {
        	radioButtonPortBTris5.setSelected(false);
        }
        if (trisB[6] == 1) {
        	radioButtonPortBTris6.setSelected(true);
        } else {
        	radioButtonPortBTris6.setSelected(false);
        }
        if (trisB[7] == 1) {
        	radioButtonPortBTris7.setSelected(true);
        } else {
        	radioButtonPortBTris7.setSelected(false);
        }
	}
	
	
	/**
	 * Methode um ausgew�hlten Comport zu initialisieren und aktivieren.
	 */
	public static void connectComPort(String comPortName){
		comport = new ComPort();
		try{
			comport.connect(comPortName);
			comPortEnable= true;
			btnConnect.setText("Disconnect");
			btnConnect.setEnabled(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * MEthode um den aktuellen Comport zu schlie�en.
	 */
	public static void disconnectComPort(){
		comport.close();
        btnConnect.setText("Connect");
		btnConnect.setEnabled(true);
        comPortEnable = false;
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