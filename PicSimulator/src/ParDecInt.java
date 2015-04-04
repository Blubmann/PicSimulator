
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

	public void decode(int line){
		if (instructions[line] >= 10240 && instructions[line] <= 12287) {
            int k = instructions[line] & 2047;
            System.out.println("GOTO " + k);
           // System.out.println("Test");
		}
	}

	
}
