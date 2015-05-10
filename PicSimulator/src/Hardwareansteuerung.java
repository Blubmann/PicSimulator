import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * TODO
 */
public class Hardwareansteuerung {
  private static OutputStreamWriter out;
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
  
  /** Variable zum speichern des ausgewählten CommPort.
   */
  CommPort commPort;
  
  /** 
   * Konstruktor für ComPort.
   * Initialisiert die Werte von TrisA, TrisB, PortA und PortB, so kann
   * der CommPort auch im laufenden Betrieb zugeschaltet werden.
   */
  public Hardwareansteuerung(int aTrisA, int aPortA, int aTrisB, int aPortB) {
      trisA = aTrisA;
      portA = aPortA;
      trisB = aTrisB;
      portB = aPortB;
  }
  
  
  /** 
   * Methode um den CommPort zu vebinden.
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
          out = new OutputStreamWriter(serialPort.getOutputStream());
      }
  }
  
  
  /** 
   * Methode um den aktuell verbundenen CommPort zu trennen und wieder frei zu geben.
   */
  public void close(){
      serialPort.close();
  }
  
  public static void sendRS232() throws Exception {
    
    String p_a = encodeData(Worker.reg.bank0[Worker.reg.PORTA]);
    String t_a = encodeData(Worker.reg.bank1[Worker.reg.TRISA]);
    String p_b = encodeData(Worker.reg.bank0[Worker.reg.PORTB]);
    String t_b = encodeData(Worker.reg.bank1[Worker.reg.TRISB]);
    String send = t_a + p_a + t_b + p_b;
    write(send + CR);
    
  }
  
  public static void write(String s) throws Exception {
    out.write(s);
    out.flush();
    
  }
  
  
  public static ArrayList<Integer> read() throws Exception {
    int n;
    char c = 0;
    String answer = new String("");
    int index = 5;
    
    while (c != CR && index > 0 && in.ready()) {
      
      n = in.read();
      
      if (n != -1) {
        
        c = (char) n;
        answer += c;
        index--;
        
      }
    }
    
    if (index <= 0 && c != CR) {
      
      return null;
      
    }
    
    //delay(1);

    ArrayList<Integer> decodedValues = new ArrayList<Integer>();
    decodedValues = decodeData(answer);
    
    if (decodedValues.size() > 0) {
      
      return decodedValues;
      
    } else {
      
      return null;
      
    }
  }
  
  private static void delay(int a) {
    
    try {
      
      Thread.sleep(a);
      
    } catch (InterruptedException e) {
      
      
    }
  }
  
  private static String encodeData(int b) {
    
    char c1 = (char) (0x30 + ((b & 0xF0) >> 4));
    //System.out.println("C1: "+ c1);
    char c2 = (char) (0x30 + (b & 0x0F));
    //System.out.println("C2: "+c2);
    return "" + c1 + c2;
    
  }
  
  private static ArrayList<Integer> decodeData(String s) {
    
    int i0 = s.charAt(0) - 0x30;
    //System.out.println("i0: "+i0);
    int i1 = s.charAt(1) - 0x30;
    //System.out.println("i1: "+i1);
    int i2 = s.charAt(2) - 0x30;
    //System.out.println("i2: "+i2);
    int i3 = s.charAt(3) - 0x30;
    //System.out.println("i3: "+i3);
    
    ArrayList<Integer> tokens = new ArrayList<Integer>();
    
    if (i0 >= 0 && i1 >= 0 && i2 >= 0 && i3 >= 0 && i0 <= 0xF && i1 <= 0xF&& i2 <= 0xF && i3 <= 0xF) {
      
      int a = (((int) i0 & 0x0F) << 4) | ((int) i1 & 0x0F);
      tokens.add(a);
      int b = (((int) i2 & 0x0F) << 4) | ((int) i3 & 0x0F);
      tokens.add(b);
    }  
    
    if (tokens.size() > 0) {
      
      return tokens;
      
    } else {
      
      return null;
    } 
  }
}