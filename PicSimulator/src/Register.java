import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.DefaultListModel;


public class Register {
	
	/** Programmcounter des Simulators**/
	private static int pc;
	
	/**Variable zum Speichern der Referenz für RB0**/
	private static boolean bFlanke = false;
	
	/**Variable zum Speichern der Referenz für RB4:7**/
	private static int refRB47Val = 0;
	
	/**Variable zum Speichern der Referenz für RA4**/
	private static int refRA4Val = 0;
	
	/**Variable, ob Prescaler zu Watchdog gehört**/
	private static boolean prescalertoWatchdog=true;
	
	/**Initwert des Watchdog**/
	public static int watchDog = 18000;
	
	/**Variable, ob Prescaler zu Timer0 gehört**/
	private static boolean prescalertoTimer=false;
	
	/**Geben an on eine fallende oder steigende Flanke vorliegt**/
    private static boolean fallenEdgeOnRA4 = false;
    private static boolean risingEdgeOnRA4 = false;

	/**Speichern der Referenzfaktoren des Prescalers**/
	private static int refPrescalerFactor =7;
	
	/**Variable die den aktuellen Prescalerwert enthält**/
	private static int prescaler = 127;
	
	/**Variable zum Zählen der Cycles, um die Laufzeit zu berechnen**/
	public static int cycleCounter;
	public double runtime;
	
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
		this.bank0[STATUS] = 24;
		this.bank1[STATUS] = 24;
		this.bank1[OPTION] = 255;
		this.bank1[TRISA] = 31;
		this.bank1[TRISB] = 255;
	}
	
	public void reset(){
		this.pc=0;
		this.bank0[PCL]=pc;
		this.bank1[PCL]=pc;
		this.bank0[STATUS]=bank0[STATUS]&7;
		this.bank1[STATUS]=bank1[STATUS]&7;
		this.bank1[OPTION] = 255;
		this.bank0[PCLATH] = 0;
		this.bank1[PCLATH] = 0;
		this.bank0[INTCON] = bank0[INTCON]&1;
		this.bank1[INTCON] = bank1[INTCON]&1;
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
		MainGUI.textField_Watchdog.setText(Integer.toString(watchDog));
		MainGUI.textField_Frequenz.setText(Integer.toString(MainGUI.slider.getValue()));
		MainGUI.textField_Laufzeit.setText(Double.toString(getRuntime()));
		//TODO Bug, da die Laufzeit bei größeren Quarzfrequenzen nicht angezeigt wird
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
		this.runtime = runtime + (4000000/MainGUI.slider.getValue());
		checkRA4Int();
		increaseTimer0();
		decreaseWDT();
	}
	
	public double getRuntime(){
		return this.runtime;
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
	
	/**Methoden um bestimmte Bits im Intcon und Option 
	 * Register zu prüfen
	 */
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
	
	public boolean get_T0IF(){
		if(activeBank==0){
			return ((bank0[INTCON] & 4)==4);
		}else{
			return ((bank1[INTCON] & 4)==4);
		}
	}
	
	public boolean get_BINT(){
		return((bank0[PORTB] & 1)==1);
	}
	
	public int get_AInt(){
		return (bank0[PORTA] & 16);
	}
	
	public boolean get_INTEDG(){
		return ((bank0[STATUS] & 64) ==64);
	}

	public boolean get_T0CS(){
		return ((bank1[OPTION] & 32)==32);
	}
	
	public boolean get_T0SE(){
		return ((bank1[OPTION] & 16)==16);
	}
	
	public boolean get_PSA(){
		return ((bank1[OPTION] & 8)==8);
	}
	
	public int get_PS(){
		return (bank1[OPTION] & 7);
	}
	
	/**Ruft die einzelnen Methoden zum Prüfen, ob
	 * Interruptflags gesetzt werden müssen. Hinterher
	 * werden die Flags geprüft
	 */
	public void checkInterrupt(){
		checkRB0Int();
		checkRB47Int();
		checkInt();
	}
	
	/**Methode zur Erhöhung des Timer0**/
	public void increaseTimer0(){
		checkPrescaler();
		increaseTimer_Mode();
	}
	
	/**Methode um den Timer0 entsprechend des gesetzten
	 * Modus zu erhöhen
	 */
	public void increaseTimer_Mode(){
		if(get_T0CS()){
			increaseTimer_CounterMode();
		}else{
			increaseTimer_TimerMode();
		}
		checkTimer0Overflow();
	}
	
	/**Methode um Timer0 im RA4-Modus zu erhöhen**/
	public void increaseTimer_CounterMode(){
		if(get_T0SE() && fallenEdgeOnRA4){
			if(prescalertoTimer && prescaler !=0){
				prescaler--;
			}else{
				if(prescalertoTimer && prescaler ==0){
					bank0[TMR0]++;
					setPrescaler();
				}else{
					bank0[TMR0]++;
				}
			}
		}else if(!get_T0SE() && risingEdgeOnRA4){
			if(prescalertoTimer && prescaler != 0){
                prescaler--;
            } else{
                if(prescalertoTimer && prescaler == 0){
                	bank0[TMR0]++;
                    setPrescaler();
                } else{
                	bank0[TMR0]++;
                }
            }
		}
	}
	
	/**Methode um Timer0 im Timermodus zu erhöhen**/
	public void increaseTimer_TimerMode(){
		if(prescalertoTimer && prescaler !=0){
			prescaler--;
		}else{
			if(prescalertoTimer && prescaler==0){
				bank0[TMR0]++;
				setPrescaler();
			}else{
				bank0[TMR0]++;
			}
		}
	}
	
	/**Prüft, ob der Timer0 übergelaufen ist und setzt
	 * entsprechend das T0IF-Bit
	 */
	public void checkTimer0Overflow(){
		if(bank0[TMR0]>255){
			bank0[TMR0]=0;
			if(activeBank==0){
				synchronizeBothBanks(INTCON, bank0[INTCON] | 4);
			}else{
				synchronizeBothBanks(INTCON, bank1[INTCON] | 4);
			}
			setPrescaler();
		}
	}
	
	/**Überprüft, wem der Prescaler zugeordnet ist**/
	public void checkPrescaler(){
		if(prescalertoWatchdog != get_PSA()){
			setPrescaler();
		}else if(refPrescalerFactor != get_PS()){
			setPrescaler();
		}
	}
	
	/**Methode um den Prescaler zu setzen. Jenachdem 
	 * ob er dem Timer oder dem Watchdog zugeordnet wird
	 */
	public void setPrescaler(){
		if(get_PSA()){
			setPrescalertoWatchdog();
		}else{
			setPrescalertoTimer();
		}
		refPrescalerFactor=get_PS();
	}
	
	/**Initialisiert den Prescaler für den Watchdog**/
	public void setPrescalertoWatchdog(){
		int preFactor = get_PS();
		prescaler = (int) Math.pow(2,preFactor) -1;
		prescalertoWatchdog = true;
		prescalertoTimer = false;
	}
	
	/**Initialisiert den Prescaler für den Timer0**/
	public void setPrescalertoTimer(){
		int preFactor = get_PS();
		prescaler = (int) Math.pow(2,preFactor+1) -1;
		prescalertoWatchdog = false;
		prescalertoTimer = true;
	}
	
	public void decreaseWDT(){
		if(MainGUI.getWatchdogEnable()){
			checkPrescaler();
			if(prescalertoWatchdog && prescaler !=0){
				prescaler--;
			}else{
				if(prescalertoWatchdog && prescaler ==0){
					watchDog--;
					setPrescaler();
				}else{
					watchDog--;
				}
				checkWDTReset();
			}
		}
	}
	
	public void checkWDTReset(){
		if(watchDog==0){
			watchDog=18000;
			setPrescaler();
			refreshGUI();
		}
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
	
	public void checkRA4Int(){
		if(refRA4Val > get_AInt()){
			fallenEdgeOnRA4 = true;
			risingEdgeOnRA4 = false;
		}else if(refRA4Val<get_AInt()){
			fallenEdgeOnRA4 = false;
			risingEdgeOnRA4 = true;
		}else{
			fallenEdgeOnRA4 = false;
			risingEdgeOnRA4 = false;
		}
		refRA4Val=get_AInt();
	}
	
	public void checkInt(){
		if(get_GIE()){
			if(get_T0IE() && get_T0IF()){
				handleInt();
			}
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
		pushPCtoStack();
		synchronizeBothBanks(PCL,4);
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
