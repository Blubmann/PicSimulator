import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RegTable extends JTable {
	private String[][] bank0 = new String[128][2];
    private String[] titel = new String[2];
    private DefaultTableModel dm0;
	public RegTable(JScrollPane scrollPane){
		JTable table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new javax.swing.table.DefaultTableModel(
	            new Object [][] {
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null},
	                {null, null}
	            },
	            new String [] {
	                "Adresse", "Value"
	            }
	        ) {
	            Class[] types = new Class [] {
	                java.lang.String.class, java.lang.Integer.class
	            };
	            boolean[] canEdit = new boolean [] {
	                false, false
	            };

	            public Class getColumnClass(int columnIndex) {
	                return types [columnIndex];
	            }

	            public boolean isCellEditable(int rowIndex, int columnIndex) {
	                return canEdit [columnIndex];
	            }
	        });
		dm0 = new DefaultTableModel(bank0, titel);
        for (int i = 0; i < 128; i++) {
            if (i != 1 && i != 2 && i != 3 && i != 4 && i != 5 && i != 6 && i != 8 && i != 9 && i != 10 && i != 11) {
                dm0.setValueAt("Register " + i, i, 0);
            }
            dm0.setValueAt(ParDecInt.reg.bank0[i], i, 1);
        }
        table.setModel(dm0);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMaxWidth(250);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
	}
}
