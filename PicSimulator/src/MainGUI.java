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
	private JTable table;
	private GUIOperations guiop = new GUIOperations();
	public ParDecInt pdi;
	private JButton btnStart;
	private JButton btnStop;
	private JTable table_1;
	

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
				guiop.openclicked(ev, table_1);
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
				btnStop.setEnabled(true);
		        btnStart.setEnabled(false);
		        ParDecInt pardecintThread = new ParDecInt();
		        pardecintThread.start(); 
		        //System.out.println("Test");
			}
		});
		btnStart.setBounds(10, 11, 89, 23);
		contentPane.add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.setBounds(109, 11, 89, 23);
		contentPane.add(btnStop);
		
		JButton btnStepbystep = new JButton("Step-by-Step");
		btnStepbystep.setBounds(208, 11, 104, 23);
		contentPane.add(btnStepbystep);
		
		JButton btnBank = new JButton("Bank 1");
		btnBank.setBounds(10, 45, 89, 23);
		contentPane.add(btnBank);
		
		JButton btnBank_1 = new JButton("Bank 2");
		btnBank_1.setBounds(109, 45, 89, 23);
		contentPane.add(btnBank_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 79, 302, 404);
		contentPane.add(scrollPane_1);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"New column", "New column"
			}
		));
		scrollPane_1.setViewportView(table);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(358, 15, 698, 465);
		contentPane.add(scrollPane);
		
		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"BP", "Code"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				true, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_1.getColumnModel().getColumn(0).setPreferredWidth(23);
		table_1.getColumnModel().getColumn(0).setMaxWidth(25);
		table_1.getColumnModel().getColumn(1).setResizable(false);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(475);
		scrollPane.setViewportView(table_1);
		
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
