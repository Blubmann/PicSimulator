import java.util.*;

import gnu.io.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/** ComPortConnector ist eine kleine GUI, die zum auswählen eines der der verfügbaren
 * CommPorts dient.
 */
public class ComPortConnector extends JFrame {
	

    private JComboBox comPortChooser = new JComboBox();
	private JButton btnConnect = new JButton("Connect");
    private JButton btnCancel = new JButton("Cancel");
    private JLabel jLabel1 = new JLabel();;
    
    /** Variable die eine Auflistung der verfügbaren CommPorts ermöglicht
     */
    static Enumeration portList;
    
    /** Variable, die die ID des CommPorts speichert
     */
    static CommPortIdentifier portId;
    
    
    /** Konstruktor der die Komponenten initialisert.
     */
    public ComPortConnector() {
    	setResizable(false);
        initComponents();
        findComPorts();
        setVisible(true);
    }
    
    /** Methode um die verfügbaren CommPorts auszulesen und anzuzeigen.
     */
    private void findComPorts(){
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                comPortChooser.addItem(portId.getName());
            }
        }
    }
    /** Methode die zum Initialiseren der Komponenten dient.
     */
    private void initComponents() {
    	
		setBounds(100, 100, 400, 100);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        
        comPortChooser.setBounds(24, 43, 75, 23);
        getContentPane().add(comPortChooser);
        
        btnConnect.setBounds(133, 43, 89, 23);
        getContentPane().add(btnConnect);
        
        btnCancel.setBounds(255, 42, 89, 23);
        getContentPane().add(btnCancel);

        jLabel1.setText("Please choose the COM-Port you want to connect to.");

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
    }

    /** Methode zum Bedienen des Connect-Buttons.
     * Beim Drücken des Connect-Buttons wird zu dem ausgewählten CommPort verbunden
     */
    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        MainGUI.connectComPort((String)comPortChooser.getSelectedItem());
        this.dispose();
    }

    /** Methode zum Bedienen des Cancel-Buttons.
     * Beim Drücken des Cancel-Buttons wird das Fenster wieder geschlossen, eine 
     * Verbindung wird nicht hergestellt
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }
}


