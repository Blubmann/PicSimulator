import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
 
public class CodeTable extends JTable{
	JCheckBox checkBox = new javax.swing.JCheckBox(); 
	JTable table = new JTable();

	/**Initialisiert die Tabelle beim Starten des Programms**/
	public CodeTable(JScrollPane scrollPane){
		Vector title = new Vector();
		title.add( "BP" );
		title.add( "Code" );
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(null,title) {
			
			boolean[] columnEditables = new boolean[] {
				true, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(23);
		table.getColumnModel().getColumn(0).setMaxWidth(25);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(475);
	}
	
	/**Setzt den Inhalt der Tabelle nach dem öffnen eines lst-Files**/
	public void setTable(JScrollPane scrollPane, Vector vec){
		Vector title = new Vector();
		title.add( "BP" );
		title.add( "Code" );
		//System.out.println(vec);
		table.setModel(new DefaultTableModel(vec,title) {
			
			boolean[] columnEditables = new boolean[] {
				true, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
				
			}
			Class[] types = new Class [] {
			        java.lang.Boolean.class, java.lang.Object.class
			    };

			public Class getColumnClass(int columnIndex) {
			       return types [columnIndex];
			} 
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(23);
		table.getColumnModel().getColumn(0).setMaxWidth(25);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(475);
		table.getColumn("BP").setCellEditor(new DefaultCellEditor(checkBox)); 
		scrollPane.setViewportView(table);
	}
	
	public void updateHighliner(){
		table.setRowSelectionInterval(ParDecInt.lineMarker[ParDecInt.reg.getPC()]-1, ParDecInt.lineMarker[ParDecInt.reg.getPC()]-1);
	}
	
	public void getBreakpointandStop(){
		int row = ParDecInt.lineMarker[ParDecInt.reg.getPC()];
		Boolean nextBP=(Boolean) table.getValueAt(row, 0);
		System.out.println("Breakpoint "+nextBP);
		if(nextBP!=null){
			MainGUI.run=false;
			MainGUI.btnStart.setText("Start");
			MainGUI.btnStart.setEnabled(true);
			MainGUI.btnStepbystep.setEnabled(true);
		}
	}
	
}