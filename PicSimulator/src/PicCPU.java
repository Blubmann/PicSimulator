public class PicCPU {
	Register reg = new Register();
	
	
	public void addWF(int f, int d){
		int fBuf = f;
		int w = reg.getWReg();
		int buf;
		if(reg.getBank()==0){
			buf = reg.getRegister0(fBuf) + w;
		}else{
			buf = reg.getRegister1(fBuf) + w;
		}
		setCZFlag(buf);
		checkDandInsert(buf,f,d);
		reg.increasePC();
	}
	
	public void setCZFlag(int val){
		if(val>255){
			reg.setStatusReg(reg.cFlag, 1);
			if (val - 255 == 0) {
                reg.setStatusReg(reg.zFlag, 1);
            } else {
                reg.setStatusReg(reg.zFlag, 0);
            }
		} else {
            reg.setStatusReg(reg.cFlag, 0);
            if (val == 0) {
                reg.setStatusReg(reg.zFlag, 1);
            } else {
                reg.setStatusReg(reg.zFlag, 0);
            }

        }
	}
	
	public void checkDandInsert(int buf, int f, int d){
			if(d==0){
				if(buf > 255){
					buf = buf-255;
					reg.setWReg(buf);
				}else{
					reg.setWReg(buf);
				}
			}else{
				if(buf > 255){
					buf = buf -255;
					reg.setRegister0(f, buf);
				}else{
					reg.setRegister0(f, buf);
				}
			}
	}
}
