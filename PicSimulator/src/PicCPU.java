import java.awt.PageAttributes;

public class PicCPU {
	
    /*----------------------------------------
     * BYTE-ORIENTED FILE REGISTER OPERATIONS
     *----------------------------------------*/

	public void addWF(int f, int d){
		f= getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg(); 
		int buf = getValFromBank(f)+w;
		setCFlag(buf);
		setZFlag(buf);
		setDCFlag(buf);
		buf = valbigger255(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
	}
	
	public void andWF(int f, int d){
		f = getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		int buf = getValFromBank(f)&w;
		setZFlag(buf);
		buf = valbigger255(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
	}
	
	public void clrF(int f){
		f = getIndirectAdress(f);
		setZFlag(0);
		checkDandInsert(0, f, 1);
		ParDecInt.reg.increasePC();
	}
	
	public void clrW(){
		setZFlag(0);
		ParDecInt.reg.setWReg(0);
		ParDecInt.reg.increasePC();
	}
	
	public void comF(int f, int d){
		f = getIndirectAdress(f);
		int buf =getValFromBank(f)^255;
		setZFlag(buf);
		checkDandInsert(buf, f, d);
		ParDecInt.reg.increasePC();
	}
	
	public void decF(int f, int d){
		f = getIndirectAdress(f);
		int buf = getValFromBank(f)-1;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
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
    }
	
	public void iorWF(int f, int d){
		f= getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		int buf = getValFromBank(f)|w;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
	}
	
	public void movF(int f, int d){
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		//TODO Wird das Z-Flag garantiert gesetzt?
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
	}
	
	public void movWF(int f){
		f = getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		checkDandInsert(w, f, 1);
		ParDecInt.reg.increasePC();
	}
	
    public void nop() {
    	ParDecInt.reg.increasePC();
    }
    
    public void rlf(int f, int d) {
    	f = getIndirectAdress(f);
    	int msb = getValFromBank(f)&128;
    	int buf = getValFromBank(f)<<1;
    	if(msb==128){
    		msb=1;
    	}
    	if(ParDecInt.reg.getStatusReg(ParDecInt.reg.cFlag)!=0){
    		buf = buf +1;
    	}
    	ParDecInt.reg.setStatusReg(ParDecInt.reg.cFlag, msb);
    	ParDecInt.reg.increasePC();
    }

    public void rrf(int f, int d) {
    	f = getIndirectAdress(f);
    	int lsb = getValFromBank(f)&1;
    	int buf = getValFromBank(f)>>1;
    	if(ParDecInt.reg.getStatusReg(ParDecInt.reg.cFlag)!=0){
    		buf = buf + 128;
    	}
    	ParDecInt.reg.setStatusReg(ParDecInt.reg.cFlag, lsb);
    	ParDecInt.reg.increasePC();
    }
    
    public void subWF(int f, int d) {
    	f = getIndirectAdress(f);
    	int buf = getValFromBank(f)-ParDecInt.reg.getWReg();
    	if(buf>=0){
			setCFlag(1337);
		}else{
			setCFlag(buf);
		}
    	setDCFlag(buf);
    	setZFlag(buf);
    	buf = valsmaller0(buf);
    	checkDandInsert(buf, f, d);
    	//TODO Handelt es sich hierbei um ein Sonderfall zwecks C-Flag?
    	ParDecInt.reg.increasePC();
    }
    
    public void swapF(int f, int d) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
        int buf2 = ( (buf & 0x0F)<<4 | (buf & 0xF0)>>4 );
        checkDandInsert(buf2, f, d);
    	ParDecInt.reg.increasePC();
    }
    
    public void xorWF(int f, int d) {
    	f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		int w = ParDecInt.reg.getWReg();
		buf = buf ^ w;
		setZFlag(buf);
        checkDandInsert(buf, f, d);
    	ParDecInt.reg.increasePC();
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
        if(f== ParDecInt.reg.status){
			ParDecInt.reg.setStatusReg(b, 0);
		}else{
			int buf = getValFromBank(f)^mask;
	    	checkDandInsert(buf,f,1);
		}
    	ParDecInt.reg.increasePC();
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
		if(f== ParDecInt.reg.status){
			ParDecInt.reg.setStatusReg(b, 1);
		}else{
			int buf = getValFromBank(f)^mask;
	    	checkDandInsert(buf,f,1);
		}
    	ParDecInt.reg.increasePC();
    }   
 
    public void btfsc(int f, int b) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		if  ((buf & (1 << b))==0){
			nop();
		}
    	ParDecInt.reg.increasePC();
    }    

    public void btfss(int f, int b) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		if  ((buf & (1 << b))!=0){
			nop();
		}
    	ParDecInt.reg.increasePC();
    }    
 
    /*---------------------------------------
     * LITERAL AND FILE REGISTER OPERATIONS
     *---------------------------------------*/
    
	// DC einf�gen
	public void addLW(int k){
		int w = ParDecInt.reg.getWReg(); 
		int buf;
		buf = w + k;
		setCFlag(buf);
		setZFlag(buf);
		setDCFlag(buf);
		buf = valbigger255(buf);
		checkDandInsert(buf,0,0);
		ParDecInt.reg.increasePC();
	}	
	
	
	public void andLW (int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k & w;
		buf = valbigger255(buf);
		setZFlag(buf);
		checkDandInsert(buf,0,0);
		ParDecInt.reg.increasePC();
	}
	
	public void call(int k){
		//TODO Was passiert mit PC?
		ParDecInt.reg.pushPCtoStack();
		ParDecInt.reg.setPC(k);
	}
	
	public void clrwdt(){
		//TODO
		
		ParDecInt.reg.increasePC();
	}
	
	public void instGoto(int k){
		ParDecInt.reg.setPC(k);
	}
	
	public void iorLW(int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k|w;
		setZFlag(buf);
		checkDandInsert(buf,0,0);
		ParDecInt.reg.increasePC();
	}
	
	public void movLW(int k){
		checkDandInsert(k,0,0);
		ParDecInt.reg.increasePC();
	}
	
	public void retfie(){
		//TODO
	}
	
	public void retLW(int k){
		checkDandInsert(k,0,0);	
		int buf = ParDecInt.reg.popPCfromStack(); 
		ParDecInt.reg.setPC(buf);
		System.out.println(ParDecInt.reg.getPC());
		sleep();
	}
	
	public void instReturn(){
		int buf = ParDecInt.reg.popPCfromStack(); 	
		ParDecInt.reg.setPC(buf);
	}
		
	public void sleep(){
		//TODO
	}
	
	public void subLW(int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k-w;
		/**Folgende Abfrage muss gemacht werden, da 
		 * es sich bei SUBLW um einen Sonderfall handelt
		 */
		if(buf>=0){
			setCFlag(1337);
		}else{
			setCFlag(buf);
		}
		setZFlag(buf);
		setDCFlag(buf);
		buf = valsmaller0(buf);
		checkDandInsert(buf,0,0);
		ParDecInt.reg.increasePC();
	}
	
	public void xorLW(int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k ^ w;
		setZFlag(buf);
        checkDandInsert(buf,0,0);
    	ParDecInt.reg.increasePC();
	}
	
	
    /*---------------------------------------
     * anderer K�se
     *---------------------------------------*/
	/**Pr�ft zun�hst welche Bank aktuell gew�hlt ist 
	 * und holt entsprechend aus dieser den Wert der Adresse
	 */
    public int getValFromBank(int f){
    	if(ParDecInt.reg.getBank()==0){
    		return ParDecInt.reg.getRegister0(f);
    	}else{
			return ParDecInt.reg.getRegister1(f);
		}
    }
    
	/**Pr�ft ob f gesetzt ist. Wenn nein, wird 
	 * der Inhalt im FSR �bergeben
	 */
	public int getIndirectAdress(int f){
		if(f==0){
			return ParDecInt.reg.getRegister0(ParDecInt.reg.fsr);
		} else{
			return f;
		}
	}
	
	/**Wenn das Erbebnis einer Addition > 255 ist, muss
	 * das C-Flag gesetzt werden
	 */
	private void setCFlag(int val){
		if(val>255){
			ParDecInt.reg.setStatusReg(ParDecInt.reg.cFlag, 1);
		}else {
			ParDecInt.reg.setStatusReg(ParDecInt.reg.cFlag, 0);
		}    
	}
	
	/**Wenn das Ergebnis von arithmetischen/logischen 
	 * Operation 0 ist, wird das Z-Flag gesetzt
	 */
	private void setZFlag(int val){
		if (val==0){
			ParDecInt.reg.setStatusReg(ParDecInt.reg.zFlag, 1);
		}else{
			ParDecInt.reg.setStatusReg(ParDecInt.reg.zFlag, 0);
		}
	}
	
	private void setDCFlag(int val){
		if((val&0b10000)==0){
			ParDecInt.reg.setStatusReg(ParDecInt.reg.dcFlag, 0);
		}else{
			ParDecInt.reg.setStatusReg(ParDecInt.reg.dcFlag, 1);
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
		if(val<0){
			return(256-val);
		}else{
			return val;
		}
	}
	
	/**Pr�ft ob d gesetzt, um zu entscheiden ob in W 
	 * oder ins f-Register geschrieben wird. 
	 */
	private void checkDandInsert(int buf, int f, int d){
			if(d==0){
					ParDecInt.reg.setWReg(buf);
			}else{
				if(ParDecInt.reg.getBank()==0){
					ParDecInt.reg.setRegister0(f, buf);
					System.out.println("Im Register0 steht an Stelle "+f+" die Zahl"+buf);
					ParDecInt.reg.synchronizeBothBanks(f, buf);
				}else{
					ParDecInt.reg.setRegister1(f, buf);
					ParDecInt.reg.synchronizeBothBanks(f, buf);
				}
			}
	}
}
