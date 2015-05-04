import java.awt.PageAttributes;

public class PicCPU {
	
    /*----------------------------------------
     * BYTE-ORIENTED FILE REGISTER OPERATIONS
     *----------------------------------------*/

	public void addWF(int f, int d){
		f= getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg(); 
		int buf = getValFromBank(f)+w;
		int dc = (w & 15) + getValFromBank(f) & 15;
		setDCFlag(dc);
		setCFlag(buf);
		setZFlag(buf);
		buf = valbigger255(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void andWF(int f, int d){
		f = getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		int buf = getValFromBank(f)&w;
		setZFlag(buf);
		buf = valbigger255(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void clrF(int f){
		f = getIndirectAdress(f);
		setZFlag(0);
		checkDandInsert(0, f, 1);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void clrW(){
		setZFlag(0);
		ParDecInt.reg.setWReg(0);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void comF(int f, int d){
		f = getIndirectAdress(f);
		int buf =getValFromBank(f)^255;
		setZFlag(buf);
		checkDandInsert(buf, f, d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void decF(int f, int d){
		f = getIndirectAdress(f);
		int fBuf = getValFromBank(f);
		fBuf=valsmaller0(fBuf);
		int buf = fBuf-1;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}

	public void decFSZ(int f, int d){
		f= getIndirectAdress(f);
		int buf = getValFromBank(f);
		if(buf==0){
			buf=256;
		}
		buf=buf-1;
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
		if(buf==0){
			nop();
		}
		
	}
	
	public void incF(int f, int d){
    	f = getIndirectAdress(f);
    	int buf = getValFromBank(f)+1;
    	setZFlag(buf);
    	buf = valbigger255(buf);
    	checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
    }
	
	public void incFSZ(int f, int d){
    	f = getIndirectAdress(f);
    	int buf = getValFromBank(f)+1;
    	if(buf>255){
    		buf=0;
    		nop();
    	}
    	checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
    }
	
	public void iorWF(int f, int d){
		f= getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		int buf = getValFromBank(f)|w;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void movF(int f, int d){
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void movWF(int f){
		f = getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		checkDandInsert(w, f, 1);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
    public void nop() {
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
    }
    
    public void rlf(int f, int d) {
    	f = getIndirectAdress(f);
    	int msb = getValFromBank(f)&128;
    	int buf = getValFromBank(f)<<1;
    	if(msb==128){
    		msb=1;
    	}
    	if(ParDecInt.reg.getStatusReg(ParDecInt.reg.CFLAG)!=0){
    		buf = buf +1;
    	}
    	ParDecInt.reg.setStatusReg(ParDecInt.reg.CFLAG, msb);
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
    }

    public void rrf(int f, int d) {
    	f = getIndirectAdress(f);
    	int lsb = getValFromBank(f)&1;
    	int buf = getValFromBank(f)>>1;
    	if(ParDecInt.reg.getStatusReg(ParDecInt.reg.CFLAG)!=0){
    		buf = buf + 128;
    	}
    	ParDecInt.reg.setStatusReg(ParDecInt.reg.CFLAG, lsb);
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
    }
    
    public void subWF(int f, int d) {
    	f = getIndirectAdress(f);
    	int buf = getValFromBank(f)+(-ParDecInt.reg.getWReg());
    	int dc = getValFromBank(f)& 15+(-ParDecInt.reg.getWReg()&15);
    	setDCFlag(dc);
    	setCFlag(buf);
    	setZFlag(buf);
    	buf = valsmaller0(buf);
    	checkDandInsert(buf, f, d);
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
    }
    
    public void swapF(int f, int d) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
        int buf2 = ( (buf & 0x0F)<<4 | (buf & 0xF0)>>4 );
        checkDandInsert(buf2, f, d);
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
    }
    
    public void xorWF(int f, int d) {
    	f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		int w = ParDecInt.reg.getWReg();
		buf = buf ^ w;
		setZFlag(buf);
        checkDandInsert(buf, f, d);
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
    }
    
    /*---------------------------------------
     * BIT-ORIENTED FILE REGISTER OPERATIONS
     *---------------------------------------*/
    
    public void bcf(int f, int b) {
		f= getIndirectAdress(f); 
		int mask;

        //Bitmaske aus der Zahl b erzeugen
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
        if(f== ParDecInt.reg.STATUS){
			ParDecInt.reg.setStatusReg(b, 0);
		}else{
			int buf = getValFromBank(f)^mask;
	    	checkDandInsert(buf,f,1);
		}
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
    }    
    
    public void bsf(int f, int b) {
		f= getIndirectAdress(f); 
		int mask;

        //Bitmaske aus der Zahl b erzeugen
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
		if(f== ParDecInt.reg.STATUS){
			ParDecInt.reg.setStatusReg(b, 1);
		}else{
			int buf = getValFromBank(f)^mask;
	    	checkDandInsert(buf,f,1);
		}
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
    }   
 
    public void btfsc(int f, int b) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
		if  ((buf & (1 << b))==0){
			nop();
		}
    }    

    public void btfss(int f, int b) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
		if  ((buf & (1 << b))!=0){
			nop();
		}
    }    
 
    /*---------------------------------------
     * LITERAL AND FILE REGISTER OPERATIONS
     *---------------------------------------*/
    
	// DC einfï¿½gen
	public void addLW(int k){
		int w = ParDecInt.reg.getWReg(); 
		int buf= w + k;
		int dc = (w&15)+(k&15);
		setDCFlag(dc);
		setCFlag(buf);
		setZFlag(buf);
		buf = valbigger255(buf);
		checkDandInsert(buf,0,0);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}	
	
	
	public void andLW (int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k & w;
		buf = valbigger255(buf);
		setZFlag(buf);
		checkDandInsert(buf,0,0);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void call(int k){
		//TODO Was passiert mit PC?
		ParDecInt.reg.pushPCtoStack();
		ParDecInt.reg.setPC(k);
		ParDecInt.reg.addCycle();
		ParDecInt.reg.addCycle();
	}
	
	public void clrwdt(){
		ParDecInt.reg.watchDog=18000;
		ParDecInt.reg.setStatusReg(3, 1);
		ParDecInt.reg.setStatusReg(4, 1);
		ParDecInt.reg.addCycle();
		ParDecInt.reg.increasePC();
	}
	
	public void instGoto(int k){
		ParDecInt.reg.setPC(k);
		ParDecInt.reg.addCycle();
		ParDecInt.reg.addCycle();
	}
	
	public void iorLW(int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k|w;
		setZFlag(buf);
		checkDandInsert(buf,0,0);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void movLW(int k){
		checkDandInsert(k,0,0);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void retfie(){
		int buf = ParDecInt.reg.popPCfromStack();
		ParDecInt.reg.setPC(buf);
		ParDecInt.reg.addCycle();
		ParDecInt.reg.addCycle();
	}
	
	public void retLW(int k){
		checkDandInsert(k,0,0);	
		int buf = ParDecInt.reg.popPCfromStack(); 
		ParDecInt.reg.setPC(buf);
		//System.out.println(ParDecInt.reg.getPC());
		ParDecInt.reg.addCycle();
		ParDecInt.reg.addCycle();
	}
	
	public void instReturn(){
		int buf = ParDecInt.reg.popPCfromStack(); 	
		ParDecInt.reg.setPC(buf);
		ParDecInt.reg.addCycle();
		ParDecInt.reg.addCycle();
	}
		
	public void sleep(){
		ParDecInt.reg.watchDog=18000;
		ParDecInt.reg.setStatusReg(3, 1);
		ParDecInt.reg.setStatusReg(4, 1);	
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void subLW(int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k+(-w);
		int dc = (k & 15)+((-w) & 15);
		setDCFlag(dc);
		setCFlag(buf);
		setZFlag(buf);
		buf = valsmaller0(buf);
		checkDandInsert(buf,0,0);
		ParDecInt.reg.increasePC();
		ParDecInt.reg.addCycle();
	}
	
	public void xorLW(int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k ^ w;
		setZFlag(buf);
        checkDandInsert(buf,0,0);
    	ParDecInt.reg.increasePC();
    	ParDecInt.reg.addCycle();
	}
	
	
    /*---------------------------------------
     * anderer Käse
     *---------------------------------------*/
	/**Prüft zunähst welche Bank aktuell gewählt ist 
	 * und holt entsprechend aus dieser den Wert der Adresse
	 */
    public int getValFromBank(int f){
    	if(ParDecInt.reg.getBank()==0){
    		return ParDecInt.reg.getRegister0(f);
    	}else{
			return ParDecInt.reg.getRegister1(f);
		}
    }
    
	/**Prüft ob f gesetzt ist. Wenn nein, wird 
	 * der Inhalt im FSR übergeben
	 */
	public int getIndirectAdress(int f){
		if(f==0){
			return ParDecInt.reg.getRegister0(ParDecInt.reg.FSR);
		} else{
			return f;
		}
	}
	
	/**Wenn das Erbebnis einer Addition > 255 ist, muss
	 * das C-Flag gesetzt werden
	 */
	private void setCFlag(int val){
		if(val<255){
			ParDecInt.reg.setStatusReg(ParDecInt.reg.CFLAG, 0);
		}else {
			ParDecInt.reg.setStatusReg(ParDecInt.reg.CFLAG, 1);
		}    
	}
	
	/**Wenn das Ergebnis von arithmetischen/logischen 
	 * Operation 0 ist, wird das Z-Flag gesetzt
	 */
	private void setZFlag(int val){
		if ((val&0xFF)==0){
			ParDecInt.reg.setStatusReg(ParDecInt.reg.ZFLAG, 1);
		}else{
			ParDecInt.reg.setStatusReg(ParDecInt.reg.ZFLAG, 0);
		}
	}
	
	private void setDCFlag(int val){
		if(val<15){
			ParDecInt.reg.setStatusReg(ParDecInt.reg.DCFLAG, 0);
		}else{
			ParDecInt.reg.setStatusReg(ParDecInt.reg.DCFLAG, 1);
		}
	}
	
	private int valbigger255(int val){
		if(val>255){
			return (val-255);
		}else{
			return val;
		}
	}
	
	private int valsmaller0(int val){
		if(val<=0){
			return(256-val);
		}else{
			return val;
		}
	}
	
	/**Prüft ob d gesetzt, um zu entscheiden ob in W 
	 * oder ins f-Register geschrieben wird. 
	 */
	private void checkDandInsert(int buf, int f, int d){
			if(d==0){
					ParDecInt.reg.setWReg(buf);
			}else{
				if(ParDecInt.reg.getBank()==0){
					ParDecInt.reg.setRegister0(f, buf);
					//System.out.println("Im Register0 steht an Stelle "+f+" die Zahl"+buf);
					ParDecInt.reg.synchronizeBothBanks(f, buf);
				}else{
					ParDecInt.reg.setRegister1(f, buf);
					ParDecInt.reg.synchronizeBothBanks(f, buf);
				}
				ParDecInt.reg.checkPCL(f);
			}
	}
}
