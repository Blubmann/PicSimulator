import com.sun.org.apache.bcel.internal.util.ByteSequence;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/** ComPort dient zur Ansteuerung des Comm-Ports �ber die Oberfl�che.
 * Es k�nnen die Register RA und RB mit den jeweiligen TRIS angesteuert
 * und ausgelesen werden.
 */
public class ComPort {
    
    /** Variable zum zwischenspeichern des TRIS-A Wertes.
     * Initialisert mit 31, Wert nach Power-On-Reset
     */
    private int trisA = 31;
    
    /** Variable zum zwischenspeichern des PORT-A Wertes.
     * Initialisert mit 0, Wert nach Power-On-Reset
     */
    private int portA = 0;
    
    /** Variable zum zwischenspeichern des TRIS-B Wertes.
     * Initialisert mit 255, Wert nach Power-On-Reset
     */
    private int trisB = 255;
    
    /** Variable zum zwischenspeichern des PORT-B Wertes.
     * Initialisert mit 0, Wert nach Power-On-Reset
     */
    private int portB = 0;
    
    /** Variable zum speichern des ausgew�hlten SerialPort.
     */
    SerialPort serialPort;
    
    /** Variable zum speichern des ausgew�hlten CommPort.
     */
    CommPort commPort;
    
    /** Konstruktor f�r ComPort.
     * Initialisiert die Werte von TrisA, TrisB, PortA und PortB, so kann
     * der CommPort auch im laufenden Betrieb zugeschaltet werden.
     */
    public ComPort(int aTrisA, int aPortA, int aTrisB, int aPortB) {
        trisA = aTrisA;
        portA = aPortA;
        trisB = aTrisB;
        portB = aPortB;
    }
    
    /** Methode um den CommPort zu vebinden.
     */
    void connect( String portName ) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() ) {
            System.out.println("Error: Port is currently in use");
        } else {
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(4800,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            writeOut();
            readIn();
        }
    }
    
    /** Methode um den aktuell verbundenen CommPort zu trennen und wieder frei zu geben.
     */
    public void close(){
        serialPort.close();
    }
    
    /** Methode um die obersten Bits 4-7 einer Integer-Zahl zu bekommen.
     */
    private int getHighNibble(int value){
        for(int i = 0; i < 4; i++){
            value = value & ~(1 << i);
        }
        for(int i = 31; i > 7; i--){
            value = value & ~(1 << i);
        }
        value = value >> 4;
        return value;
    }
    
    /** Methode um die obersten Bits 0-3 einer Integer-Zahl zu bekommen.
     */
    private int getLowNibble(int value){
        for(int i = 31; i > 3; i--){
            value = value & ~(1 << i);
        }
        return value;
    }
    
    /** Methode um ein Nibble so zu ver�ndern, dass es von der Hardware gelesen werden kann.
     */
    private int setNibbleToSend(int nibble) throws Exception{
        if( nibble < 0 || nibble > 15){
            Exception exception = new Exception(); throw exception;
        }
        int valueToSend = 0x30 + nibble;
        return valueToSend;
    }
    
    /** Methode um den im Zwischenspeicher liegenden PortA-Wert auszugeben.
     */
    public int getInputPortA(){
        return portA;
    }
    
    /** Methode um den im Zwischenspeicher liegenden PortB-Wert auszugeben.
     */
    public int getInputPortB(){
        return portB;
    }
    
    /** Methode um einen neuen Wert in PortA zu speichern und diesen an die Hardware zu senden.
     */
    public void updatePortA(int portAnewValue){
        portA = portAnewValue;
        writeOut();
        readIn();
    }
    
    /** Methode um einen neuen Wert in PortB zu speichern und diesen an die Hardware zu senden.
     */
    public void updatePortB(int portBnewValue){
        portB = portBnewValue;
        writeOut();
        readIn();
    }
    
    /** Methode um einen neuen Wert in TrisA zu speichern und diesen an die Hardware zu senden.
     */
    public void updateTrisA(int trisAnewValue){
        trisA = trisAnewValue;
        writeOut();
        readIn();
    }
    
    /** Methode um einen neuen Wert in TrisB zu speichern und diesen an die Hardware zu senden.
     */
    public void updateTrisB(int trisBnewValue){
        trisB = trisBnewValue;
        writeOut();
        readIn();
    }
    
    /** Methode um die Werte von PortA, TrisA, PortB und TrisB als Byte-Array zu bekommen.
     * Die Daten sidn dabei bereits sendefertig, also mit 0x30h best�ckt.
     * @return Gibt die aktuellen Werte von TrisA, PortA, TrisB und PortB in dieser Reihenfolge
     * sendefertig als Byte-Array zur�ck.
     */
    private byte[] getValuesAsByteArray(){
        byte data[] = new byte[9];
        try {
            data[0] = (byte) setNibbleToSend(getHighNibble(trisA));    //Tris A 3xH
            data[1] = (byte) setNibbleToSend(getLowNibble(trisA));    //Tris A 3xL
            data[2] = (byte) setNibbleToSend(getHighNibble(portA));    //Port A 3xH
            data[3] = (byte) setNibbleToSend(getLowNibble(portA));    //Port A 3xL
            data[4] = (byte) setNibbleToSend(getHighNibble(trisB));    //Tris B 3xH
            data[5] = (byte) setNibbleToSend(getLowNibble(trisB));    //Tris B 3xL
            data[6] = (byte) setNibbleToSend(getHighNibble(portB));    //Port B 3xH
            data[7] = (byte) setNibbleToSend(getLowNibble(portB));    //Port B 3xL
            data[8] = (byte) '\r'; // CR
        } catch (Exception ex) {
            System.out.println("Fehler: Tris oder PortWerte ausserhalb des Bereichs");
            ex.printStackTrace();
            System.exit(1);
        }
        return data;
    }
    
    /** Methode um die Daten an die Hardware zu senden.
     * Die aktuellen Daten aus den Variablen TrisA, PortA, TrisB und PortB werden
     * an die Hardware gesendet.
     */
    public void writeOut(){
        try {
            OutputStream out = serialPort.getOutputStream();
            out.write(getValuesAsByteArray());
            out.close();
        } catch (IOException ex) {
            System.out.println("Fehler: Outstream konnte nicht ge�ffent werden!");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    /** Methode um die aktuellen daten aus der Hardware auszulesen.
     * Die Variablen PortA und PortB werden mit den aktuellen
     * Hardwaredaten �berschrieben. Dies geht nur, wenn vorher ein writeOut()
     * gemacht wurde.
     */
    public void readIn(){
        try {
            InputStream in = serialPort.getInputStream();
            byte c = 0;
            int portAB[] = new int[100];
            int counter = 0;
            while ( c > -1 ) {
                c = (byte)in.read();
                if(c < 0){break;}
                portAB[counter] = (int) c;
                counter++;
            }
            if(portAB[0] > 48){
                updatePortsFromReadIn(portAB);
            }
            in.close();
        } catch ( IOException e ) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**  Methode um die von der Hardware zur�ckgegebenen Werte zu dekodieren und
     * den Variablen zuzuweisen.
     */
    private void updatePortsFromReadIn(int portAB[]){
        portA = ((portAB[0]-0x32) << 4) + portAB[1]-0x30;
        portB = ((portAB[2]-0x30) << 4) + portAB[3]-0x30;
    }
}

