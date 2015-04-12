public class Register {
	
	private static int pc;
	
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
    public int activeBank = 0;
	private int wReg;
	
		
	public Register(){
		this.wReg=0;
		this.pc=0;
		this.bank1[trisA] = 31;
		this.bank1[trisB] = 255;
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
	
	/**Statusregister ist ein 8-Zellenarray, damit
	 * ist ein einfacheres bitsetzen möglich 
	 */
	public void setStatusReg(int pos, int val){
		statusReg[pos]=val;
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
		if(statusReg[rp0]==0){
			activeBank =0;
		} else{
			activeBank =1;
		}
	}
	
	/**Übergibt die aktuelle Banknummer**/
	public int getBank(){
		if(statusReg[rp0]==0){
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
		bank0[pcl]=pc;
		bank1[pcl]=pc;
		System.out.println("PCL erhöht");
	}
}
