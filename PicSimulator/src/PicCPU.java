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
		buf = valbigger255(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
	}
	
	public void andWF(int f, int d){
		f = getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		int buf = getValFromBank(f)&w;
		buf = valbigger255(buf);
		setZFlag(buf);
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
		int buf = getValFromBank(f)-1;
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		if(buf==0){
			nop();
		}
		ParDecInt.reg.increasePC();
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
    	setZFlag(buf);
    	buf = valbigger255(buf);
    	checkDandInsert(buf,f,d);
		if(buf==0){
			nop();
		}
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
		setZFlag(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
	}
	
	public void movWF(int f){
		f = getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		checkDandInsert(w, f, 0);
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
    	//TODO
    	ParDecInt.reg.increasePC();
    }
    
    public void subWF(int f, int d) {
    	//TODO
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
		int buf = getValFromBank(f);
		buf = buf  & ~(1 << b);
    	checkDandInsert(buf,f,1);
    	ParDecInt.reg.increasePC();
    }    
    
    public void bsf(int f, int b) {
		f= getIndirectAdress(f); 
		int buf = getValFromBank(f);
		buf = buf  | (1 << b);
    	checkDandInsert(buf,f,1);
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
		ParDecInt.reg.pushPCtoStack();
		ParDecInt.reg.setPC(k);
	}
	
	public void clrwdt(){
		//TODO
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
		int buf = ParDecInt.reg.popPCfromStack(); 
		checkDandInsert(k,0,0);	
		ParDecInt.reg.setPC(buf);
	}
	
	public void instReturn(){
		int buf = ParDecInt.reg.popPCfromStack(); 	
		ParDecInt.reg.setPC(buf);
	}
		
	public void sleep(){
		//TODO
	}
	
	public void subLW(int k){
		//TODO
	}
	
	public void xorLW(int k){
		int w = ParDecInt.reg.getWReg();
		int buf = k ^ w;
		setZFlag(buf);
        checkDandInsert(buf, 0, 0);
    	ParDecInt.reg.increasePC();
	}
	
	
    /*---------------------------------------
     * anderer K�se
     *---------------------------------------*/
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
		}
		if (val>0){
			ParDecInt.reg.setStatusReg(ParDecInt.reg.zFlag, 0);
		}
	}
	
	private int valbigger255(int val){
		if(val>255){
			return (val-255);
		}else{
			return val;
		}
	}
	
	/**Prüft ob d gesetzt, um zu entscheiden ob in W 
	 * oder ins Register geschrieben wird. 
	 */
	private void checkDandInsert(int buf, int f, int d){
			if(d==0){
					ParDecInt.reg.setWReg(buf);
			}else{
				if(ParDecInt.reg.getBank()==0){
					ParDecInt.reg.setRegister0(f, buf);
					ParDecInt.reg.synchronizeBothBanks(f, buf);
				}else{
					ParDecInt.reg.setRegister1(f, buf);
					ParDecInt.reg.synchronizeBothBanks(f, buf);
				}
			}
	}
}
