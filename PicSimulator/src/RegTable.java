import java.util.Vector;

import javax.swing.table.*;
import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RegTable extends JTable {
	private Object[][] bank0 = new Object[128][2];
	private Object[][] bank1 = new Object[128][2];
    private String[] titel = {"Register(in hex)","Wert(in hex)"};
    private int[] register0 = ParDecInt.reg.bank0;
    private int[] register1 = ParDecInt.reg.bank1;
    private JTable table = new JTable();
    
    public RegTable(JScrollPane scrollPane){
    	int count=0;
    	for(int k=0;k<128;k++){
    		bank0[k][0]=("Register " +Integer.toHexString(k));
    		bank0[k][1]=Integer.toHexString(register0[count]);
    		count++;
    	}
    	bank0[1][0]= "TMR0";
    	bank0[2][0] = "PCL";
    	bank0[3][0] = "STATUS";
    	bank0[4][0] = "FSR";
    	bank0[5][0] = "PORTA";
    	bank0[6][0] = "PORTB";
    	bank0[8][0] = "EEDATA";
    	bank0[9][0] = "EEADR";
    	bank0[10][0] = "PCLATH";
    	bank0[11][0] = "INTCON";
    	
    	bank1[1][0] = "OPTION";
    	bank1[2][0] = "PCL";
    	bank1[3][0] = "STATUS";
    	bank1[4][0] = "FSR";
    	bank1[5][0] = "TRISA";
    	bank1[6][0] = "TRISB";
    	bank1[8][0] = "ECON1";
    	bank1[9][0] = "ECON2";
    	bank1[10][0] = "PCLATH";
    	bank1[11][0] = "INTCON";
    	
    	scrollPane.setViewportView(table);	
    	table.setModel(new DefaultTableModel(bank0,titel){
    		boolean[] canEdit = new boolean [] {false, true};
    		public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
    	});	
    	table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMaxWidth(250);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);	
    }
    
    public void updateTable0(JScrollPane scrollPane){
    	int count=0;
    	for(int k=0;k<128;k++){
    		if (k != 1 && k != 2 && k != 3 && k != 4 && k != 5 && k != 6 && k != 8 && k != 9 && k != 10 && k != 11){
    			bank0[k][0]=("Register " +Integer.toHexString(k));
    		}
			bank0[k][1]=Integer.toHexString(register0[count]);
    		count++;
    	}
    	scrollPane.setViewportView(table);
    	table.setModel(new DefaultTableModel(bank0,titel){
    		boolean[] canEdit = new boolean [] {false, true};
    		public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
    	});	
    	table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMaxWidth(250);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);	
    }
    
    public void updateTable1(JScrollPane scrollPane){
    	int count=0;
    	for(int k=0;k<128;k++){
    		if (k != 1 && k != 2 && k != 3 && k != 4 && k != 5 && k != 6 && k != 8 && k != 9 && k != 10 && k != 11){
    			bank1[k][0]=("Register " + Integer.toHexString(k+128));
    		}
			bank1[k][1]=Integer.toHexString(register1[count]);
    		count++;
    	}
    	scrollPane.setViewportView(table);	
    	table.setModel(new DefaultTableModel(bank1,titel){
    		boolean[] canEdit = new boolean [] {false, true};
    		public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
    	});	
    	table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMaxWidth(250);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);	
    }
}
