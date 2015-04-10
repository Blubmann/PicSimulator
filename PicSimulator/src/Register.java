public class Register {
	
	private int pc;
	
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
	private int[] bank0 = new int[128];
	private int[] bank1 = new int[128];
    public int activeBank = 0;
	private int wReg;
	
		
	public Register(){
		wReg=0;
		pc=0;
		bank0[pcl] = 0;
		bank1[pcl] = 0;
        bank1[trisA] = 31;
        bank1[trisB] = 255;
	}
	
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
	
	public void setStatusReg(int pos, int val){
		statusReg[pos]=val;
	}
	
	public void setRegister0(int f, int result){
		bank0[f]= result;
	}
	
	public void setRegister1(int f, int result){
		bank1[f]= result;
	}
	
	public int getRegister0(int f){
		return bank0[f];
	}
	
	public int getRegister1(int f){
		return bank1[f];
	}
	
	public void printRegister(){
		for(int i=0; i <= 128; i++){
			System.out.println("Registernummer Bank0: " +bank0[i]);
			System.out.println("Registernummer Bank1: " +bank1[i]);
		}
	}
	
	public int getWReg(){
		return wReg;
	}
	
	public void setWReg(int val){
		wReg = val;
	}
	
	public void setBank(){
		if(statusReg[rp0]==0){
			activeBank =0;
		} else{
			activeBank =1;
		}
	}
	
	public int getBank(){
		if(statusReg[rp0]==0){
			return 0;
		} else{
			return 1;
		}
	}
	
	public int getPC(){
		return pc;
	}
	
	public void increasePC(){
		pc++;
		bank0[pcl]=pc;
		bank1[pcl]=pc;
	}
	public void changeStatusReg(int pos, int val){
		statusReg[pos]=val;
	}
}
