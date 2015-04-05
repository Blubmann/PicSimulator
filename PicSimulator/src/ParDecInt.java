
public class ParDecInt extends Thread{
	public String[] code;
	public static int[] instructions;
	private Thread t;
	
	public void run() {
		//System.out.println(instructions[0]);
		//System.out.println("Test");
		for (int i = 0; i <= (instructions.length - 1); i++) {
			decode(i);
		}
		
	}
	
	  public void start ()
	   {
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
		int[] newInst = new int[this.code.length + 1]; 
		
		for(String singleLines : code){			
			int opcodeInt;
			String opcode = singleLines.substring(5, 9);
			opcodeInt = Integer.parseInt(opcode, 16);
			System.out.println(opcodeInt);
			newInst[CodeCount] = opcodeInt;
			CodeCount++;
		}
		instructions=newInst;
		//System.out.println(instructions[0]);
	}
	
	/**Die Methode bekommt beim Drücken der Starttaste den extrahierten Opcode übergeben und 
	 * prüft mithilfe der der Integerzahl welcher Befehl vorliegt.
	 */
	public void decode(int line){
		if (instructions[line] >= 1792 && instructions[line] <= 2047) {
            int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("ADDWF, f ist " + f +" d ist " + d);
		}
		else if (instructions[line] >= 1792 && instructions[line] <= 2047) {
            int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            System.out.println("ANDWF, f ist " + f +" d ist " + d);
		}
		else if (instructions[line] >= 10240 && instructions[line] <= 12287) {
            int k = instructions[line] & 2047;
            System.out.println("GOTO, k ist " + k);
           // System.out.println("Test");
		}
	}

	
}
