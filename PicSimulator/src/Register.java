import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.DefaultListModel;


public class Register {
	
	private static int pc;
	private static boolean bFlanke = false;
	private static int refRB47Val = 0;
	public static int cycleCounter;
	private Stack<Integer> stack = new Stack<Integer>();
	
	/**Häufig verwendete Adressen**/
	public final int TMR0 = 1;
	public final int OPTION = 1;
	public final int PCL = 2;
	public final int STATUS = 3;
	public final int FSR = 4;
	public final int PORTA = 5;
	public final int TRISA = 5;
	public final int PORTB = 6;
	public final int TRISB = 6;
	public final int EEDATA = 8;
	public final int EECON1 = 8;
    public final int EEADR = 9;
    public final int EECON2 = 9;
    public final int PCLATH = 10;
    public final int INTCON = 11;
    
    /**Häufig verwendete Bitpositionen im Statusregister**/
    public final int CFLAG = 0; 
    public final int DCFLAG = 1;
    public final int ZFLAG = 2;
    public final int NOTPD = 3;
    public final int NOTTO = 4;
    public final int RP0 = 5;
    public final int RP1 = 6;
    public final int IRP = 7;
    
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
		this.cycleCounter=0;
		this.bank1[TRISA] = 31;
		this.bank1[TRISB] = 255;
	}
	
	/**Speichert den PC für den Folgebefehl nach z.B. einem Call**/
	public void pushPCtoStack(){
		stack.push(pc+1);
	}
	
	/**Holt den den obersten PC vom Stack**/
	public int popPCfromStack(){
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
			if(this.bank0[FSR] <= 127){
				activeBank=0;
				return bank0[FSR];
			} else{
				activeBank=1;
				return bank0[FSR] - 128;
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
	
	/**Holt sich den Stack als Objekt und schreibt ihn dann auf der Gui**/
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
		MainGUI.textField_Status.setText(Integer.toHexString(bank0[STATUS]));
		MainGUI.textField_Z.setText(Integer.toHexString(getStatusReg(2)));
		MainGUI.textField_C.setText(Integer.toHexString(getStatusReg(0)));
		MainGUI.textField_DC.setText(Integer.toHexString(getStatusReg(1)));
		MainGUI.textField_PCL.setText(Integer.toHexString(bank0[PCL]));
		MainGUI.textField_Cycle.setText(Integer.toString(cycleCounter));

	}
	
	/**Ruft die Methoden zum Lesen der Radiobuttons für die PortPins auf**/
	public void readGui(){
		bank0[PORTA]=MainGUI.getPinsPortA();
		bank0[PORTB]=MainGUI.getPinsPortB();
	}
	
	/**Methode um aus der Dezimalzahl ein Array mit 0/1 zu machen.
	 * Bsp: a = 5 -> 5%2=1 ->trisBBits[0]=1 ->5/2=2.5=2
	 * 		a = 2 -> 2%2=0 ->.....
	 */
	public int[] getTrisB() {
        int a = this.bank1[TRISB];
        int[] trisBBits = new int[8];

        for (int i = 0; i <8; i++) {
            trisBBits[i] = a % 2;
            a /= 2;
        }

        return trisBBits;
    }
	
	public int[] getTrisA() {
        int a = this.bank1[TRISA];
        int[] trisABits = new int[5];

        for (int i = 0; i <5; i++) {
            trisABits[i] = a % 2;
            a /= 2;
        }

        return trisABits;
    }
	
	public int[] getPortA(){
		int a = this.bank0[PORTA];
        int[] portABits = new int[5];

        for (int i = 0; i <=4; i++) {
            portABits[i] = a % 2;
            a /= 2;
        }

        return portABits;
	}
	
	public int[] getPortB(){
		int a = this.bank0[PORTB];
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
        bank0[STATUS] = buf;
        bank1[STATUS] = buf;
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
		if((bank0[STATUS]&32)==32){
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
	public void increasePC(){
		pc++;
		bank0[PCL]=pc;
		bank1[PCL]=pc;
		System.out.println("PCL erhöht");
	}
	
	public void addCycle(){
		cycleCounter++;
	}
	
	/**Hält die Registerzellen fsr, status, pclath und intcon synchron**/
	public void synchronizeBothBanks(int f, int val) {
        if (f == FSR || f == STATUS || f == PCLATH || f == INTCON) {
            bank0[f] = val;
            bank1[f] = val;
        }
    }
	
	public Object[] getStack(){
		
		return (Object[]) stack.toArray();
		
	}
	
	public boolean get_GIE(){
		if(activeBank==0){
			return ((bank0[INTCON] & 128)==128);
		}else{
			return ((bank1[INTCON] & 128)==128);
		}
	}
	
	public boolean get_INTE(){
		if(activeBank==0){
			return ((bank0[INTCON] & 16)==16);
		}else{
			return ((bank1[INTCON] & 16)==16);
		}
	}
	
	public boolean get_INTF(){
		if(activeBank==0){
			return ((bank0[INTCON] & 2)==2);
		}else{
			return ((bank1[INTCON] & 2)==2);
		}
	}

	public boolean get_RBIE(){
		if(activeBank==0){
			return ((bank0[INTCON] & 8)==8);
		}else{
			return ((bank1[INTCON] & 8)==8);
		}
	}
	
	public boolean get_RBIF(){
		if(activeBank==0){
			return ((bank0[INTCON] & 1)==1);
		}else{
			return ((bank1[INTCON] & 1)==1);
		}
	}
	
	public boolean get_T0IE(){
		if(activeBank==0){
			return ((bank0[INTCON] & 32)==32);
		}else{
			return ((bank1[INTCON] & 32)==32);
		}
	}
	
	public boolean get_BINT(){
		return((bank0[PORTB] & 1)==1);
	}
	
	public boolean get_INTEDG(){
		return ((bank0[STATUS] & 64) ==64);
	}

	public boolean get_T0CS(){
		return ((bank1[OPTION] & 32)==32);
	}
	
	public boolean get_PSA(){
		return ((bank1[OPTION] & 8)==8);
	}
	
	
	public void checkInterrupt(){
		checkRB0Int();
		checkRB47Int();
		checkInt();
	}
	
	public void checkRB0Int(){
		if((get_BINT() == get_INTEDG()) && (get_BINT() != (bFlanke==true))){
			if(activeBank==0){
				synchronizeBothBanks(INTCON, bank0[INTCON] | 2);
			}else{
				synchronizeBothBanks(INTCON, bank1[INTCON] | 2);
			}
		}
		bFlanke = get_BINT();
	}
	
	public void checkRB47Int(){
		if((bank0[PORTB]&240)!=refRB47Val){
			if(activeBank==0){
				synchronizeBothBanks(INTCON, bank0[INTCON] | 1);
			}else{
				synchronizeBothBanks(INTCON, bank1[INTCON] | 1);
			}
		}
		refRB47Val=bank0[INTCON]&240;
	}
	
	public void checkInt(){
		if(get_GIE()){
			if(get_INTE() && get_INTF()){
				handleInt();
			}
			if(get_RBIE() && get_RBIF()){
				handleInt();
			}
		}
	}
	
	public void handleInt(){
		synchronizeBothBanks(INTCON, 127);
		ParDecInt.cpu.call(4);
	}
	
	public void setPC(int actualPC){
		int pcLath = bank0[PCLATH];
		pcLath = (Integer.rotateRight(pcLath, 3)) & 0b11;
		pcLath = Integer.rotateLeft(pcLath, 11);
		pc= pcLath | actualPC;
		int newpcl = pc & 0b11111111;
		bank0[PCL]= newpcl;
		bank1[PCL]= newpcl;
	}
	
	public void checkPCL(int val){
		if(val==2){
			int pcl = bank0[PCL];
			int pclath = bank0[PCLATH];
			int temp =(Integer.rotateLeft(pclath, 8));
			pc= temp+pcl;
		}
	}
}
