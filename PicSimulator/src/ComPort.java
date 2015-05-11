import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * TODO
 */
public class ComPort {
  private static OutputStream out;
  private static InputStreamReader in;
  private static char CR = '\r';
  
  /** 
   * Variable zum zwischenspeichern des TRIS-A Wertes.
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
  
  /** Variable zum speichern des ausgewählten SerialPort.
   */
  SerialPort serialPort;
  
  /** Variable zum speichern des ausgewählten ComPort.
   */
  CommPort commPort;
  
  /** 
   * Konstruktor für ComPort.
   */
  public ComPort() {}
  
  
  /** 
   * Methode um den ComPort zu vebinden.
   */
  void connect( String portName ) throws Exception {
      CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
      if ( portIdentifier.isCurrentlyOwned() ) {
          System.out.println("Error: Port is currently in use");
      } else {
          commPort = portIdentifier.open(this.getClass().getName(),2000);
          serialPort = (SerialPort) commPort;
          serialPort.setSerialPortParams(4800,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
          in = new InputStreamReader(serialPort.getInputStream());
          out = serialPort.getOutputStream();
      }
  }
  
  
  /** 
   * Methode um den aktuell verbundenen ComPort zu trennen und wieder frei zu geben.
   */
  public void close(){
      serialPort.close();
  }
  
  
  /**
   * Die Methode gibt den Inhalt der Register an den Encoder und setzt dessen Rückgabewerte ensprechend zu einem String
   * zusammen, damit die write-Methode den String senden kann.
   */
  public static void sendRS232() throws Exception {
    byte data[] = new byte[9];
    data[0] = (byte) getHighNibbles(Worker.reg.bank1[Worker.reg.TRISA]);
    data[1] = (byte) getLowNibbles(Worker.reg.bank1[Worker.reg.TRISA]);
    data[2] = (byte) getHighNibbles(Worker.reg.bank0[Worker.reg.PORTA]);
    data[3] = (byte) getLowNibbles(Worker.reg.bank0[Worker.reg.PORTA]);
    data[4] = (byte) getHighNibbles(Worker.reg.bank1[Worker.reg.TRISB]);
    data[5] = (byte) getLowNibbles(Worker.reg.bank1[Worker.reg.TRISB]);
    data[6] = (byte) getHighNibbles(Worker.reg.bank0[Worker.reg.PORTB]);
    data[7] = (byte) getLowNibbles(Worker.reg.bank0[Worker.reg.PORTB]);
    data[8] = (byte) '\r';
    write(data);
    
  }
  
  
  public static int getHighNibbles(int val){
	  val = (0x30 + ((val & 0xF0) >> 4));
	  return val;
  }
  
  
  public static int getLowNibbles(int val){
	  val = (0x30 + (val & 0x0F));
	  return val;
  }
  /**
   * Schreibt den String per OutputStreamWriter auf den seriellen Port.
   */
  public static void write(byte[] data) throws Exception {
    out.write(data);
    out.flush();
    
  }
  
  
  /**
   * Methode um gesetzte Ports auf der Software zu erkennen, damit diese im Pic gesetzt werden können.
   */
  public static ArrayList<Integer> read() throws Exception {
    int n;
    char c = 0;
    String answer = new String("");
    int index = 5;
    
    while (c != CR && in.ready() && index > 0) {
      
      n = in.read();
      
      if (n != -1) {
        
        c = (char) n;
        answer += c;
        index--;
        
      }
    }
    
    if (index <= 0 && c != CR) {
      System.out.println("Fehle, kein Eendezeichen erfasst!");
      return null;
    }

    ArrayList<Integer> decodedValues = new ArrayList<Integer>();
    decodedValues = decodeData(answer);
    
    if (decodedValues.size() > 0) {
      
      return decodedValues;
      
    } else {
      
      return null;
      
    }
  }  
  
  
  /**
   * Dekodiert die gelesenen Werte um sie am PIC anzeigen zu können.
   */
  private static ArrayList<Integer> decodeData(String s) {
    ArrayList<Integer> tokens = new ArrayList<Integer>();
      int a = (((int) (s.charAt(0) - 0x30) & 0x0F) << 4) | ((int) (s.charAt(1) - 0x30) & 0x0F);
      tokens.add(a);
      int b = (((int) (s.charAt(2) - 0x30) & 0x0F) << 4) | ((int) (s.charAt(3) - 0x30) & 0x0F);
      tokens.add(b);
    
    if (tokens.size() > 0) {
      
      return tokens;
      
    } else {
      
      return null;
    } 
  }
}