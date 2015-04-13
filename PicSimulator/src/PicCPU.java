public class PicCPU {
	
	
	public void addWF(int f, int d){
		f= getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg(); 
		int buf;
		if(ParDecInt.reg.getBank()==0){
			buf = ParDecInt.reg.getRegister0(f) + w;
		}else{
			buf = ParDecInt.reg.getRegister1(f) + w;
		}
		setCFlag(buf);
		setZFlag(buf);
		buf = valbigger255(buf);
		checkDandInsert(buf,f,d);
		ParDecInt.reg.increasePC();
	}
	
	public void andWF(int f, int d){
		f = getIndirectAdress(f);
		int w = ParDecInt.reg.getWReg();
		int buf;
		if(ParDecInt.reg.getBank()==0){
			buf = ParDecInt.reg.getRegister0(f) & w;
		}else{
			buf = ParDecInt.reg.getRegister1(f) & w;
		}
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
	
	/**Prüft ob f gesetzt ist. Wenn nein, wird 
	 * der Inhalt im FSR übergeben
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
	 * oder ins Register geschrieben wird. Bei Zahlen > 255
	 * wird entsprechend subtrahiert 
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
