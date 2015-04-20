
public class ParDecInt extends Thread{
	public String[] code;
	public static int[] instructions;
	private Thread t;
	private int i=0;
	private static PicCPU cpu = new PicCPU();
	public static Register reg = new Register();
	
	public void run(){
		//System.out.println(instructions[0]);
		//System.out.println("Test");
		if(MainGUI.run==true){
			for (i = 0; i <= (instructions.length - 1); i++) {
				i=reg.getPC();
				decode(i);
			}
			MainGUI.run=false;
		}
		i=reg.getPC();
		if(MainGUI.step=true && i<=(instructions.length-1)){
				System.out.println("I: " +i);
				decode(i);
				System.out.println("PCL: "+reg.getPC());
				MainGUI.step=false;
		}
		//reg.printRegister();
		
	}
	
    public void start (){
	      //System.out.println("Starting");
	      if (t == null)
	      {
	         t = new Thread (this);
	         t.start ();
	      }
	}
	public ParDecInt(){
		
	}
	
	/**ParDecInt bekommt ein Array übergeben, indem der die relevanten Quellcodezeilen als String gespechert sind.
	 * Der Opcode wird aus den Zeilen extrahiert und in eine Integerzahl umgewandelt.
	 */
	public ParDecInt(String[] icode){
		this.code = icode;
		int CodeCount = 0;		
		int[] newInst = new int[this.code.length]; 
		
		for(String singleLines : code){			
			int opcodeInt;
			String opcode = singleLines.substring(5, 9);
			opcodeInt = Integer.parseInt(opcode, 16);
			System.out.println("Dezimaler Befehlscode "+opcodeInt);
			newInst[CodeCount] = opcodeInt;
			CodeCount++;
		}
		instructions=newInst;
	}
	
	/**Die Methode bekommt beim Drücken der Starttaste den extrahierten Opcode übergeben und 
	 * prüft mithilfe der der Integerzahl welcher Befehl vorliegt.
	 */
	public void decode(int line){
		if (instructions[line] >= 1792 && instructions[line] <= 2047) {
            int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("ADDWF, f ist " + f +" d ist " + d);
            cpu.addWF(f, d);

		}
		else if (instructions[line] >= 1792 && instructions[line] <= 2047) {
            int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("ANDWF, f ist " + f +" d ist " + d);
            cpu.andWF(f, d);
		}
		else if (instructions[line] >= 384 && instructions[line] <= 511) {
            int f = instructions[line] & 127;
            System.out.println("CLRF, f ist "+f);
            cpu.clrF(f);
		}
		else if (instructions[line] >= 256 && instructions[line] <= 383) {
            System.out.println("CLRW");
		}
		else if(instructions[line] >= 2304 && instructions[line] <= 2559){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("COMF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 768 && instructions[line] <= 1023){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("DECF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 2816 && instructions[line] <= 3071){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("DECFSZ, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 2560 && instructions[line] <= 2815){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("INCF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 3840 && instructions[line] <= 4095){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("INCFSZ, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 1024 && instructions[line] <= 1279){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("IORWF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 2048 && instructions[line] <= 2303){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("MOVF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 128 && instructions[line] <= 255){
			int f = instructions[line] & 127;
            System.out.println("MOVWF, f ist " + f);
		}
		else if(instructions[line] >= 128 && instructions[line] <= 255){
			int f = instructions[line] & 127;
            System.out.println("MOVWF, f ist " + f);
		}
		else if(instructions[line] == 0 || instructions[line] == 32 || instructions[line] == 64 || instructions[line] == 96){
            System.out.println("NOP");
		}
		else if(instructions[line] >= 3328 && instructions[line] <= 3583){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("RLF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 3072 && instructions[line] <= 3327){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("RRF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 1024 && instructions[line] <= 1279){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("SUBWF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 3584 && instructions[line] <= 3839){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("SWAPF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 1536 && instructions[line] <= 1791){
			int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("XORWF, f ist " + f +" d ist " + d);
		}
		else if(instructions[line] >= 4096 && instructions[line] <= 5119){
			int f = instructions[line] & 127;
            int b = getBits(instructions[line] & 896);
            System.out.println("BCF, f ist " + f +" b ist " + b);
		}
		else if(instructions[line] >= 5120 && instructions[line] <= 6143){
			int f = instructions[line] & 127;
            int b = getBits(instructions[line] & 896);
            System.out.println("BSF, f ist " + f +" b ist " + b);
		}
		else if(instructions[line] >= 6144 && instructions[line] <= 7167){
			int f = instructions[line] & 127;
            int b = getBits(instructions[line] & 896);
            System.out.println("BTFSC, f ist " + f +" b ist " + b);
		}
		else if(instructions[line] >= 7168 && instructions[line] <= 8191){
			int f = instructions[line] & 127;
            int b = getBits(instructions[line] & 896);
            System.out.println("BTFSS, f ist " + f +" b ist " + b);
		}
		else if (instructions[line] >= 15872 && instructions[line] <= 16383) {
            int k = instructions[line] & 255;
            System.out.println("ADDLW, k ist " + k);
		}
		else if (instructions[line] >= 15872 && instructions[line] <= 16383) {
            int k = instructions[line] & 255;
            System.out.println("ANDLW, k ist " + k);
		}
		else if (instructions[line] >= 8192 && instructions[line] <= 10239) {
            int k = instructions[line] & 255;
            System.out.println("CALL, k ist " + k);
            cpu.call(k);
            System.out.println(ParDecInt.reg.getPC());
		}
		else if (instructions[line] == 100) {
            System.out.println("CLRWDT");
		}
		else if (instructions[line] >= 10240 && instructions[line] <= 12287) {
            int k = instructions[line] & 2047;
            System.out.println("GOTO, k ist " + k);
            cpu.instGoto(k);
            System.out.println(ParDecInt.reg.getPC());
		}
		else if (instructions[line] >= 14336 && instructions[line] <= 14591) {
            int k = instructions[line] & 255;
            System.out.println("IORLW, k ist " + k);
		}
		else if (instructions[line] >= 12288 && instructions[line] <= 13311) {
            int k = instructions[line] & 255;
            System.out.println("MOVLW, k ist " + k);
		}
		else if (instructions[line] == 9) {
            System.out.println("RETFIE");
		}
		else if (instructions[line] >= 13312 && instructions[line] <= 14335) {
            int k = instructions[line] & 255;
            System.out.println("RETLW, k ist " + k);
		}
		else if (instructions[line] == 8) {
            System.out.println("RETRUN");
		}
		else if (instructions[line] == 99) {
            System.out.println("SLEEP");
		}
		else if (instructions[line] >= 15360 && instructions[line] <= 15615) {
            int k = instructions[line] & 255;
            System.out.println("SUBLW, k ist " + k);
		}
		else if (instructions[line] >= 14848 && instructions[line] <= 15103) {
            int k = instructions[line] & 255;
            System.out.println("XORLW, k ist " + k);
		}
	}
	
	public int getBits(int b){
		switch(b){
		case 0:
			return 0;
		case 128:
			return 1;
		case 384:
            return 3;
        case 512:
            return 4;
        case 640:
            return 5;
        case 768:
            return 6;
        case 896:
            return 7;
		default:
            System.err.println("Fehler beim Bits lesen");
            return -1;
		}
	}

	
}
