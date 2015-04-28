import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.DefaultListModel;


public class Register {
	
	private static int pc;
	private Stack<Integer> stack = new Stack<Integer>();
	
	/**Häufig verwendete Adressen**/
	public final int tmr0 = 1;
	public final int option = 1;
	public final int pcl = 2;
	public final int status = 3;
	public final int fsr = 4;
	public final int portA = 5;
	public final int trisA = 5;
	public final int portB = 6;
	public final int trisB = 6;
	public final int eedata = 8;
	public final int eecon1 = 8;
    public final int eeadr = 9;
    public final int eecon2 = 9;
    public final int pclath = 10;
    public final int intcon = 11;
    
    /**Häufig verwendete Bitpositionen im Statusregister**/
    public final int cFlag = 0; 
    public final int dcFlag = 1;
    public final int zFlag = 2;
    public final int notPD = 3;
    public final int notTO = 4;
    public final int rp0 = 5;
    public final int rp1 = 6;
    public final int irp = 7;
    
    /**Speicheraufbau**/
    private int[] statusReg = new int[8];
	int[] bank0 = new int[128];
	int[] bank1 = new int[128];
    public static int activeBank;
	private int wReg;
	
	
	/**Belegung mit Initwerten**/
	public Register(){
		this.wReg=0;
		this.pc=0;
		this.bank1[trisA] = 31;
		this.bank1[trisB] = 255;
	}
	
	/**Speichert den PC für den Folgebefehl nach z.B. einem Call
	 */
	public synchronized void pushPCtoStack(){
		stack.push(pc+1);
	}
	
	/**Holt den den obersten PC vom Stack*/
	public synchronized int popPCfromStack(){
		try{
			return stack.pop();
		}catch(EmptyStackException est){
            System.err.println("Stack ist leer! " + est.fillInStackTrace());
            return -1;
		}
	}
	/**Der Inhalt von Bank0 und Bank1 sind identisch. Wenn f=0 ist,
	 * wird geprüft was im FSR steht. Wenn < 127 kann es so direkt 
	 * eingesezt werden. Bei > 127 muss entweder der Inhalt 
	 * -128 gerechnet werden, da die Register nur bis 127 gehen.
	 * Dabei ist es egal ob von Bank0 oder Bank1**/
	public int getIndirectAddress(int f){
		if(f==0){
			if(this.bank0[fsr] <= 127){
				return bank0[fsr];
			} else{
				return bank0[fsr] - 128;
			}
		}else{
			return f;
		}
	}
	
	/**Methode um die Gui auf Basis des Regiters zu aktualisieren. 
	 * Die RadioButtons für Pins und Tris werden somit aktualisiert.
	 */
	public void refreshGUI(){
		setGuiFlags();
		MainGUI.setPinsPortA();
		MainGUI.setPinsPortB();
		MainGUI.setTrisPortA();
		MainGUI.setTrisPortB();
		updateStack();
		if(MainGUI.bank==true){
			MainGUI.regtab.updateTable0(MainGUI.scrollPane_1);
		}else{
			MainGUI.regtab.updateTable1(MainGUI.scrollPane_1);
		}
	}
	
	/**Holt sich den Stack als Objekt und schreibt ihn dann auf de Gui**/
	public void updateStack(){
		final Object[] stack = getStack();
		@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
		DefaultListModel model = new DefaultListModel() {
	            {
	            	for (int i=0;i<stack.length;i++){
	                addElement(stack[i]);
	            	}
	            }
	        };
		MainGUI.stackList.setModel(model);
	}
	/**Hier werden alle wichtigen Flags auf die Gui geschrieben**/
	public void setGuiFlags(){
		MainGUI.textField_WReg.setText(Integer.toHexString(getWReg())); 
		MainGUI.textField_Bank.setText(Integer.toHexString(getBank()));
		MainGUI.textField_Status.setText(Integer.toHexString(bank0[status]));
		MainGUI.textField_Z.setText(Integer.toHexString(getStatusReg(2)));
		MainGUI.textField_C.setText(Integer.toHexString(getStatusReg(0)));
		MainGUI.textField_DC.setText(Integer.toHexString(getStatusReg(1)));
	}
	
	/**Ruft die Methoden zum Lesen der Radiobuttons für die PortPins auf**/
	public void readGui(){
		bank0[portA]=MainGUI.getPinsPortA();
		bank0[portB]=MainGUI.getPinsPortB();
	}
	
	/**Methode um aus der Dezimalzahl ein Array mit 0/1 zu machen.
	 * Bsp: a = 5 -> 5%2=1 ->trisBBits[0]=1 ->5/2=2.5=2
	 * 		a = 2 -> 2%2=0 ->.....
	 */
	public int[] getTrisB() {
        int a = this.bank1[trisB];
        int[] trisBBits = new int[8];

        for (int i = 0; i <8; i++) {
            trisBBits[i] = a % 2;
            a /= 2;
        }

        return trisBBits;
    }
	
	public int[] getTrisA() {
        int a = this.bank1[trisA];
        int[] trisABits = new int[5];

        for (int i = 0; i <5; i++) {
            trisABits[i] = a % 2;
            a /= 2;
        }

        return trisABits;
    }
	
	public int[] getPortA(){
		int a = this.bank0[portA];
        int[] portABits = new int[5];

        for (int i = 0; i <=4; i++) {
            portABits[i] = a % 2;
            a /= 2;
        }

        return portABits;
	}
	
	public int[] getPortB(){
		int a = this.bank0[portB];
        int[] portBBits = new int[8];

        for (int i = 0; i <=7; i++) {
            portBBits[i] = a % 2;
            a /= 2;
        }

        return portBBits;
	}
	
	/**Statusregister ist ein 8-Zellenarray, damit
	 * ist ein einfacheres Bitsetzen möglich 
	 */
	public void setStatusReg(int pos, int val){
		statusReg[pos]=val;
	}
	
	public int getStatusReg(int pos){
		return statusReg[pos];
	}
	
	/**Synchronisiert das Statusarray mit der dem Statusregister**/
	public void statusToMemory() {
		int buf=0;
        for (int i = 0; i <=7; i++) {
        	if(statusReg[i]==1){
        		buf=(int) (buf+Math.pow(2, i));
        	}
        }
        System.out.println("Es wird "+buf+" in die Register geschrieben");
        bank0[status] = buf;
        bank1[status] = buf;
    }
	
	/**Inhalt einer Zelle auf Bank0 setzen**/
	public void setRegister0(int f, int result){
		bank0[f]= result;
	}
	
	/**Inhalt einer Zelle auf Bank1 setzen**/
	public void setRegister1(int f, int result){
		bank1[f]= result;
	}
	
	/**Inhalt einer Zelle auf Bank0 auslesen**/
	public int getRegister0(int f){
		return bank0[f];
	}
	
	/**Inhalt einer Zelle auf Bank1 auslesen**/
	public int getRegister1(int f){
		return bank1[f];
	}
	
	
	public void printRegister(){
		for(int i=0; i <= 127; i++){
			System.out.println("Registernummer "+i+" Bank0: " +bank0[i]);
			System.out.println("Registernummer "+i+" Bank1: " +bank1[i]);
		}
		System.out.println("W: "+getWReg());
	}
	
	/**W-Register(Akku) auslesen**/
	public int getWReg(){
		return wReg;
	}
	
	/**W-Register(Akku) setzen**/
	public void setWReg(int val){
		wReg = val;
	}
	
	/**Setzen der Bank aufgrund des Bits im Statusregister**/
	public void setBank(){
		if((bank0[status]&32)==32){
			activeBank =1;
		} else{
			activeBank =0;
		}
	}
	
	/**Übergibt die aktuelle Banknummer**/
	public int getBank(){
		if(activeBank==0){
			return 0;
		} else{
			return 1;
		}
	}
	
	/**Übergibt den aktuellen Programmcounterwert**/
	public int getPC(){
		return pc;
	}
	
	/**Erhöht den PC im eins und aktualisiert ihn auf beiden Bänken**/
	public synchronized void increasePC(){
		pc++;
		bank0[pcl]=pc;
		bank1[pcl]=pc;
		System.out.println("PCL erhöht");
	}
	
	/**Hält die Registerzellen fsr, status, pclath und intcon synchron**/
	public void synchronizeBothBanks(int f, int val) {
        if (f == fsr || f == status || f == pclath || f == intcon) {
            bank0[f] = val;
            bank1[f] = val;
        }
    }
	
	public Object[] getStack(){
		
		return (Object[]) stack.toArray();
		
	}
	
	
	public void setPC(int actualPC){
		int pcLath = bank0[pclath];
		pcLath = (Integer.rotateRight(pcLath, 3)) & 0b11;
		pcLath = Integer.rotateLeft(pcLath, 11);
		pc= pcLath | actualPC;
		int pcl = pc & 0b11111111;
		int newPclath = (Integer.rotateRight(pc, 8)) & 0b11111;
		synchronizeBothBanks(pcl, pcl);
		synchronizeBothBanks(pclath, newPclath);
	}
}
