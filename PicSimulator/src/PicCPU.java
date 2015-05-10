import java.awt.PageAttributes;


/**
 * Klasse die die komplette Befehlslogik enthält 
 */
public class PicCPU {
	
    /**----------------------------------------
     * BYTE-ORIENTED FILE REGISTER OPERATIONS
     *----------------------------------------
     */

	public void addWF(int f, int d){
		f= getIndirectAdress(f);
		int w = Worker.reg.getWReg(); 
		int buf = getValFromBank(f)+w;
		int dc = (w & 15) + (getValFromBank(f) & 15);
		setCFlag(buf);
		setDCFlag(dc);
		buf = buf&255;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void andWF(int f, int d){
		f = getIndirectAdress(f);
		int w = Worker.reg.getWReg();
		int buf = getValFromBank(f)&w;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void clrF(int f){
		f = getIndirectAdress(f);
		setZFlag(0);
		checkDandInsert(0, f, 1);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void clrW(){
		setZFlag(0);
		Worker.reg.setWReg(0);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void comF(int f, int d){
		f = getIndirectAdress(f);
		int buf =getValFromBank(f)^255;
		setZFlag(buf);
		checkDandInsert(buf, f, d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void decF(int f, int d){
		f = getIndirectAdress(f);
		int fBuf = getValFromBank(f);
		if(fBuf==0){
			fBuf=256;
		}
		int buf = fBuf-1;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}

	public void decFSZ(int f, int d){
		f= getIndirectAdress(f);
		int buf = getValFromBank(f);
		if(buf==0){
			buf=256;
		}
		buf=buf-1;
		checkDandInsert(buf,f,d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
		if(buf==0){
			nop();
		}
		
	}
	
	public void incF(int f, int d){
    	f = getIndirectAdress(f);
    	int buf = getValFromBank(f)+1;
    	if(buf>255){
    		buf=0;
    	}
    	setZFlag(buf);
    	checkDandInsert(buf,f,d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
    }
	
	public void incFSZ(int f, int d){
    	f = getIndirectAdress(f);
    	int buf = getValFromBank(f)+1;
    	if(buf>255){
    		buf=0;
    		nop();
    	}
    	checkDandInsert(buf,f,d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
    }
	
	public void iorWF(int f, int d){
		f= getIndirectAdress(f);
		int w = Worker.reg.getWReg();
		int buf = getValFromBank(f)|w;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void movF(int f, int d){
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void movWF(int f){
		f = getIndirectAdress(f);
		int w = Worker.reg.getWReg();
		checkDandInsert(w, f, 1);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
    public void nop() {
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
    }
    
    public void rlf(int f, int d) {
    	f = getIndirectAdress(f);
    	int msb = getValFromBank(f)&128;
    	int buf = (getValFromBank(f)<<1)&255;
    	if(msb==128){
    		msb=1;
    	}
    	if(Worker.reg.getStatusReg(Worker.reg.CFLAG)!=0){
    		buf = buf +1;
    	}
    	Worker.reg.setStatusReg(Worker.reg.CFLAG, msb);
    	checkDandInsert(buf, f, d);
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
    }

    public void rrf(int f, int d) {
    	f = getIndirectAdress(f);
    	int lsb = getValFromBank(f)&1;
    	int buf = (getValFromBank(f)>>1)&255;
    	if(Worker.reg.getStatusReg(Worker.reg.CFLAG)!=0){
    		buf = buf + 128;
    	}
    	Worker.reg.setStatusReg(Worker.reg.CFLAG, lsb);
    	checkDandInsert(buf, f, d);
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
    }
    
    public void subWF(int f, int d) {
    	f = getIndirectAdress(f);
    	int buf = (Worker.reg.getWReg()^255)+1;
    	int buf2 = getValFromBank(f) + buf;
    	int dc = (getValFromBank(f)&15)+(buf&15);
    	setCFlag(buf2);
    	setDCFlag(dc);	
    	buf2=buf2&255;
    	setZFlag(buf2);
    	checkDandInsert(buf2, f, d);
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
    }
    
    public void swapF(int f, int d) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
        int buf2 = ( (buf & 0x0F)<<4 | (buf & 0xF0)>>4 );
        checkDandInsert(buf2, f, d);
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
    }
    
    public void xorWF(int f, int d) {
    	f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		int w = Worker.reg.getWReg();
		buf = buf ^ w;
		setZFlag(buf);
        checkDandInsert(buf, f, d);
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
    }
    
    /**---------------------------------------
     * BIT-ORIENTED FILE REGISTER OPERATIONS
     *---------------------------------------
     */
    
    public void bcf(int f, int b) {
		f= getIndirectAdress(f); 
		int mask;
        switch (b) {
            case 0:
                mask = 1;
                break;
            case 1:
                mask = 2;
                break;
            case 2:
                mask = 4;
                break;
            case 3:
                mask = 8;
                break;
            case 4:
                mask = 16;
                break;
            case 5:
                mask = 32;
                break;
            case 6:
                mask = 64;
                break;
            case 7:
                mask = 128;
                break;
            default:
                System.err.println("Error beim setzen des Bits!");
                return;
        }
        if(f== Worker.reg.STATUS){
			Worker.reg.setStatusReg(b, 0);
		}else{
			int buf = getValFromBank(f)^mask;
	    	checkDandInsert(buf,f,1);
		}
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
    }    
    
    public void bsf(int f, int b) {
		f= getIndirectAdress(f); 
		int mask;
        switch (b) {
            case 0:
                mask = 1;
                break;
            case 1:
                mask = 2;
                break;
            case 2:
                mask = 4;
                break;
            case 3:
                mask = 8;
                break;
            case 4:
                mask = 16;
                break;
            case 5:
                mask = 32;
                break;
            case 6:
                mask = 64;
                break;
            case 7:
                mask = 128;
                break;
            default:
                System.err.println("Error beim Setzen des Bits!");
                return;
        }
		if(f== Worker.reg.STATUS){
			Worker.reg.setStatusReg(b, 1);
		}else{
			int buf = getValFromBank(f)^mask;
	    	checkDandInsert(buf,f,1);
		}
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
    }   
 
    public void btfsc(int f, int b) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
		if  ((buf & (1 << b))==0){
			nop();
		}
    }    

    public void btfss(int f, int b) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
		if  ((buf & (1 << b))!=0){
			nop();
		}
    }    
 
    /**---------------------------------------
     * LITERAL AND FILE REGISTER OPERATIONS
     *---------------------------------------
     */
    
	// DC einfï¿½gen
	public void addLW(int k){
		int w = Worker.reg.getWReg(); 
		int buf= w + k;
		int dc = (w&15)+(k&15);
		setCFlag(buf);
		setDCFlag(dc);
		buf =buf&255;
		setZFlag(buf);
		checkDandInsert(buf,0,0);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}	
	
	
	public void andLW (int k){
		int w = Worker.reg.getWReg();
		int buf = w & k;
		setZFlag(buf);
		checkDandInsert(buf,0,0);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void call(int k){
		//TODO Was passiert mit PC?
		Worker.reg.pushPCtoStack();
		Worker.reg.setPC(k);
		Worker.reg.addCycle();
		Worker.reg.addCycle();
	}
	
	public void clrwdt(){
		Worker.reg.watchDog=18000;
		Worker.reg.setStatusReg(3, 0);
		Worker.reg.setStatusReg(4, 0);
		Worker.reg.addCycle();
		Worker.reg.increasePC();
	}
	
	public void instGoto(int k){
		Worker.reg.setPC(k);
		Worker.reg.addCycle();
		Worker.reg.addCycle();
	}
	
	public void iorLW(int k){
		int w = Worker.reg.getWReg();
		int buf = w|k;
		setZFlag(buf);
		checkDandInsert(buf,0,0);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void movLW(int k){
		checkDandInsert(k,0,0);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void retfie(){
		int buf = Worker.reg.popPCfromStack();
		Worker.reg.setPC(buf);
		Worker.reg.addCycle();
		Worker.reg.addCycle();
	}
	
	public void retLW(int k){
		checkDandInsert(k,0,0);	
		int buf = Worker.reg.popPCfromStack(); 
		Worker.reg.setPC(buf);
		//System.out.println(Worker.reg.getPC());
		Worker.reg.addCycle();
		Worker.reg.addCycle();
	}
	
	public void instReturn(){
		int buf = Worker.reg.popPCfromStack(); 	
		Worker.reg.setPC(buf);
		Worker.reg.addCycle();
		Worker.reg.addCycle();
	}
		
	public void sleep(){
		Worker.reg.watchDog=18000;
		Worker.reg.setStatusReg(3, 1);
		Worker.reg.setStatusReg(4, 1);	
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void subLW(int k){
		int w = (Worker.reg.getWReg()^255)+1;
		int buf = k+w;
		int dc = (k & 15)+(w & 15);
		setCFlag(buf);
		setDCFlag(dc);
		buf=buf&255;
		setZFlag(buf);
		checkDandInsert(buf,0,0);
		Worker.reg.increasePC();
		Worker.reg.addCycle();
	}
	
	public void xorLW(int k){
		int w = Worker.reg.getWReg();
		int buf = w ^ k;
		setZFlag(buf);
        checkDandInsert(buf,0,0);
    	Worker.reg.increasePC();
    	Worker.reg.addCycle();
	}
	
	
	/**
	 * Prüft zunächst welche Bank aktuell gewählt ist 
	 * und holt entsprechend aus dieser den Wert der Adresse
	 */
    public int getValFromBank(int f){
    	if(Worker.reg.getBank()==0){
    		return Worker.reg.getRegister0(f);
    	}else{
			return Worker.reg.getRegister1(f);
		}
    }
    
    
	/**
	 * Prüft anhand von f, ob eine indirekte Adressierung vorliegt. 
	 */
	public int getIndirectAdress(int f){
		if(f==0){
			return Worker.reg.getRegister0(Worker.reg.FSR);
		} else{
			return f;
		}
	}
	
	
	/**
	 * Methode um zu prüfen ob das setzen des C-Flags notwendig ist. Das passiert, die Value > 255 ist.
	 */
	private void setCFlag(int val){
		if((val&256)==0){
			Worker.reg.setStatusReg(Worker.reg.CFLAG, 0);
		}else {
			Worker.reg.setStatusReg(Worker.reg.CFLAG, 1);
		}    
	}
	
	
	/**
	 * Prüft ob das Ergebnis einer arithmetischen/logischen und setzt im Bedarfsfall das Z-Flag
	 */
	private void setZFlag(int val){
		if (val==0){
			Worker.reg.setStatusReg(Worker.reg.ZFLAG, 1);
		}else{
			Worker.reg.setStatusReg(Worker.reg.ZFLAG, 0);
		}
	}
	
	
	/**
	 * Prüft ob ein Überlauf der ersten 4 Bits stattgefunden hat und setzt entsprechend das DC-Flag
	 */
	private void setDCFlag(int val){
		if((val&16)==0){
			Worker.reg.setStatusReg(Worker.reg.DCFLAG, 0);
		}else{
			Worker.reg.setStatusReg(Worker.reg.DCFLAG, 1);
		}
	}
	
	
	/**
	 * Prüft ob d gesetzt, um zu entscheiden ob in W 
	 * oder ins f-Register geschrieben wird. 
	 */
	private void checkDandInsert(int buf, int f, int d){
			if(d==0){
					Worker.reg.setWReg(buf);
			}else{
				if(Worker.reg.getBank()==0){
					Worker.reg.setRegister0(f, buf);
					System.out.println("Im Register0 steht an Stelle "+f+" die Zahl"+buf);
					Worker.reg.synchronizeBothBanks(f, buf);
				}else{
					Worker.reg.setRegister1(f, buf);
					System.out.println("Im Register1 steht an Stelle "+f+" die Zahl"+buf);
					Worker.reg.synchronizeBothBanks(f, buf);
				}
				Worker.reg.checkPCL(f);
				Worker.reg.checkTimer0Manipulation(f, buf);
			}
	}
}
