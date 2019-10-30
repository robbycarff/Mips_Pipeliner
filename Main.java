
/****************************************************************************
Written by: Robert Carff
Date: October 7, 2019
Use: written for Dr. Buells 513 Computer Archtecture class 

everything is global because im lazy and this is a simulattor.
I know this is horrible practice.

This program takes the same approach as a two-pass assembler.

I loop over my list of lists, reading each instruction. 
I then increment my global resource values acordingly and move on.

I then re-iterate over the list, adding in stalls to prevent hazards
and shifting each ensuing value according to how long I had to stall.

This program is executed by typing the following command in your terminal
	java Main.java < file_containing_mips.txt

	java Main.java < C_Loop.txt

I am using the RV64G Instruction subset from the back of our text book
"Computer architecture, a quantitative approach" by Hennesy and patterson
 - I also just added in all the instructions from your examples online 

each type of instruction has a set of steps 
*****************************************************************************/
/* IMPORTS */
import java.io.*;
import java.util.*;

/* MAIN CLASS */
public class Main {
	// I can use these lists if I want to see comments or instructions
	private static ArrayList<String> Comments = new ArrayList<>();
	private static ArrayList<String> assembley = new ArrayList<>();
	private static ArrayList<String> instruction = new ArrayList<>();
	//2x2 dothework list (first list in parse funct)
	private static ArrayList<ArrayList<String>> instructionList = new ArrayList<ArrayList<String>>();
	//2x2 answer list (first list in parse funct)
	private static ArrayList<ArrayList<String>> Answer = new ArrayList<ArrayList<String>>();

	/****************************************************************************
	* Methods for printing out each of my lists 
	****************************************************************************/
	//Prints the lines of comments throughout the file.
	public static void printComments(){
	System.out.println("THESE ARE YOUR COMMENTS");
		for(String line : Comments)
		  { 		      
	           System.out.println(line); 		
	      }
	      System.out.println("\n");
	}
	//Prints the header lines in the file.
	public static void printAssembley(){
		System.out.println("THIS IS THE ASSEMBLY PART");
		for(String line : assembley)
		  { 		      
	           System.out.println(line); 		
	      }
	  		System.out.println("\n");
	}
	//this prints just the lines read in, they ARE NOT PARSED
	public static void printinstruction(){
		System.out.println("THESE ARE THE INSTRUCTIONS");
		for(String line : instruction)
		  { 		      
	           System.out.println(line); 		
	      }
	      System.out.println("\n");
	}
	//Prints the 2x2 lists of lists
	//This contains the instruction and the registers needed (PARSED)
	public static void printinstructionList(){
		System.out.println("THESE ARE THE INSTRUCTIONS");
		for(ArrayList line : instructionList)
		  { 		      
	           System.out.println(line); 		
	      }
	      System.out.println("\n");
	}
	//Prints the 2x2 lists of lists
	//This list contains the instruction and the resource clock times scheduled
	public static void printAnswer(){
		for(ArrayList line : Answer)
		  { 		      
	           System.out.println(line); 		
	      }
	      System.out.println("\n");
	}

	/****************************************************************************
	* readAssembly() takes a piped in file of assembley instructions, and breaks 
	* it apart into comments, assembly, and MIPS.
	* there is a function for 
	****************************************************************************/
	public static void readAssembly(){
		Scanner scan = new Scanner(System.in);
		System.out.println("\nReading in file\n");
		while(scan.hasNextLine()){
			String checkLine = scan.nextLine();
			try {
				//IF THE LINE STARTS WITHS A # SIGN
				if(checkLine.contains("#")){
					Comments.add(checkLine);
				}
				//IF THE LINE STARTS WITH A .
				else if(checkLine.contains(".")){
					assembley.add(checkLine);
				}
				//IF THE LINE HAS A COLON IN IT
				else if(checkLine.contains(":")){
					assembley.add(checkLine);
				}
				//ELSE IF THE LINE IS IN OUR DICTIONARY OF RISC-V INSTRUCTIONS
				else if(true){
					instruction.add(checkLine); // add the line to the list of instructions
					ArrayList<String> instructionParsed = new ArrayList<>(); //creating my new list
					ArrayList<String> instructionAnswer = new ArrayList<>(); //creating my new list
					String[] parsedInstruction = checkLine.split("\\s+");

					/****************************************************************************
					This check is used to change the no-ops into add instructions. 
					We do this so that the stalls are implemented right when generatingSchedule()
					****************************************************************************/
					if (parsedInstruction[1].equals("nop")){
						instructionParsed.add("add");
						instructionParsed.add("$0");
						instructionParsed.add("$0");
						instructionParsed.add("0");
						instructionList.add(instructionParsed);
						// populating my answer 2x2 matrix with 0s
						instructionAnswer.add("add");
						for(int i = 0; i <= 4; i++){
							instructionAnswer.add(Integer.toString(0));
						}
						Answer.add(instructionAnswer);
					}
					//otherwise slap it in the list
					else{
						//adding to the compute list
						instructionParsed.add(parsedInstruction[1]);
						try{
							String[] parsedRegister = parsedInstruction[2].split(",");
							for (String a : parsedRegister){
								instructionParsed.add(a);
							}
						}catch(Exception e){
							//System.out.println("you broke it dude");
						}
						instructionList.add(instructionParsed);

						//adding to the answer list
						instructionAnswer.add(parsedInstruction[1]);
						for(int i = 0; i <= 4; i++){
							instructionAnswer.add(Integer.toString(0));
						}
						Answer.add(instructionAnswer);
					}
				}
				else{
					System.out.println("Cant classify that line");
				}
			}
			catch(Exception e){
				//System.out.println("you broke it dude");
			}
		}
	}

	/****************************************************************************
	* The idea of generateSchedule() relies on the fact the the once an instruction
	* is executed, the next instruction can only be executed when that resource is then free. 
	*
	* Because of this, we can calculate instructions five variable's clock cycles in a two pass
	* 
	* add in the rising/falling clock logic?
	* add in logic for forwarding?
	*
	* answer list has 6 spaces
	* 0 = MIPS instruction, 1 = fetch, 2 = decode, 3 = execute, 4 = decode, 5 = write
	****************************************************************************/
	public static void generateSchedule(){
		//THESE ARE OUR DIFFERENT INSTRUCTION LISTS - slow b/c of the way lists search (binary tree)

		ArrayList<String> controltransfer = // these should take 2 cycles
			new ArrayList<String>(Arrays.asList("bne","j","b"));

		ArrayList<String> datatransfer = //these should take 4 cycles - no wb
			new ArrayList<String>(Arrays.asList("lb","ld","sw","fld","flw","fsd","fsw"));
		
		ArrayList<String> aluoperation = // all others should take 5 cycles
			new ArrayList<String>(Arrays.asList("add","addi","addw","addiw", "addu", "addiu","move","movz","lw","mult","mflo","li","sll","slt"));

		/****************************************************************************
		This is the first pass of the schedule generator, it is where we loop throough
		and line everything up in the "ideal pipeline" - meaning we have not added in
		stalls to account for waiting on memory.
		****************************************************************************/
		/* RESOURCE VARIABLES */
		int _if_  = 0;
		int _id_  = 1;
		int _ex_  = 2;
		int _mem_ = 3;
		int _wb_  = 4;

		// running through the list of instructions line by line 
		for (int j = 0; j < Answer.size(); j++){
			//if it is a branch operation - no EX, MEM, or WB
			if(controltransfer.contains(instructionList.get(j).get(0)) ){
				Answer.get(j).set(1, String.valueOf(_if_));
				Answer.get(j).set(2, String.valueOf(_id_));
				Answer.get(j).set(3, String.valueOf(-1));
				Answer.get(j).set(4, String.valueOf(-1));
				Answer.get(j).set(5, String.valueOf(-1));
			}

			//if its a store operarion, takes 4 cycles
			if(datatransfer.contains(instructionList.get(j).get(0))){
				Answer.get(j).set(1, String.valueOf(_if_));
				Answer.get(j).set(2, String.valueOf(_id_));
				Answer.get(j).set(3, String.valueOf(_ex_));
				Answer.get(j).set(4, String.valueOf(_mem_));
				Answer.get(j).set(5, "-1");
			}

			// all other operations take five cycles - not checking for RR or MEM cycles
			if( aluoperation.contains(instructionList.get(j).get(0))){
				Answer.get(j).set(1, String.valueOf(_if_));
				Answer.get(j).set(2, String.valueOf(_id_));
				Answer.get(j).set(3, String.valueOf(_ex_));
				Answer.get(j).set(4, String.valueOf(_mem_));
				Answer.get(j).set(5, String.valueOf(_wb_));
			}

			//increment our clock cycles
			_if_++;
			_id_++;
			_ex_++;
			_mem_++;
			_wb_++;
		}

		// ** PRINTING OUT THE TABLE BEFORE WE INSERT STALLS AND ALL OUR WEIRD LOGIC ** //
		System.out.println("ANSWER TABLE BEFORE INSTERTING STALLS (FIGURE C.1 IN TEXTBOOK) ");
		printSchedule();

		/****************************************************************************
		This is the second pass of the scheduleGenerator - it inserts stalls if 
		we need to wait on other registers being calculated
		 - ASSUMING ANY WRITE TO REGISTER STALLS IN READING (WE HAVE ONE RESOURCE)
		****************************************************************************/
		/* RESOURCE VARIABLES */
		int _if2_  = 0; // can just set this to be before ALU each time
		int _REGREAD_  = 0;

		//ignoring first line b/c its always set to 0 1 2 3 4
		for (int j = 1; j < Answer.size(); j++){
			//if it is a branch operation - used two cycles, dont need to shift down registers
			if(controltransfer.contains(instructionList.get(j-1).get(0)) ){
				//this will be the next time that we can read the register
				_REGREAD_ = Integer.parseInt(Answer.get(j-1).get(2));
				System.out.println("REG READ TIME: " + _REGREAD_);
			}

			//if its a store option - takes four cycles - no wb
			if(datatransfer.contains(instructionList.get(j-1).get(0))){
				//this will be the next time that we can read the register
				_REGREAD_ = Integer.parseInt(Answer.get(j-1).get(2));
				System.out.println("REG READ TIME: " + _REGREAD_);
			}

			// its an ALU operation - takes 5 cycles, SO WE ADD IN SOME STALLS
			if(aluoperation.contains(instructionList.get(j-1).get(0)) ){
				//this will be the next time that we can read the register
				_REGREAD_ = Integer.parseInt(Answer.get(j-1).get(4));
				System.out.println("REG READ TIME: "  + _REGREAD_);
			}

			/****************************************************************************
			This is how I make sure I dont over write all my first pass work
			****************************************************************************/
			if(controltransfer.contains(instructionList.get(j).get(0)) ){
				Answer.get(j).set(1, String.valueOf(_REGREAD_));
				Answer.get(j).set(2, String.valueOf(_REGREAD_ + 1));
				Answer.get(j).set(3, String.valueOf(-1));
				Answer.get(j).set(4, String.valueOf(-1));
				Answer.get(j).set(5, String.valueOf(-1));
			}

			//if its a store operarion, takes 4 cycles
			if(datatransfer.contains(instructionList.get(j).get(0))){
				Answer.get(j).set(1, String.valueOf(_REGREAD_));
				Answer.get(j).set(2, String.valueOf(_REGREAD_ + 1));
				Answer.get(j).set(3, String.valueOf(_REGREAD_ + 2));
				Answer.get(j).set(4, String.valueOf(_REGREAD_ + 3));
				Answer.get(j).set(5, "-1");
			}

			// all other operations take five cycles - not checking for RR or MEM cycles
			if( aluoperation.contains(instructionList.get(j).get(0))){
				Answer.get(j).set(1, String.valueOf(_REGREAD_));
				Answer.get(j).set(2, String.valueOf(_REGREAD_ + 1));
				Answer.get(j).set(3, String.valueOf(_REGREAD_ + 2));
				Answer.get(j).set(4, String.valueOf(_REGREAD_ + 3));
				Answer.get(j).set(5, String.valueOf(_REGREAD_ + 4));
			}
		}
	}

	/****************************************************************************
	This is our answer formatting function.
	Instruction fetchCC decodeCC executeCC decodeCC writeCC
	    lw         0.      1.        2.       3.       4.
	    lw         1.      2.        3.       4.       5.  
	   addi        2       3         4        5        6
	****************************************************************************/
	public static void printSchedule(){
		System.out.println(" Instruction - fetchCC - decodeCC - executeCC - decodeCC - writeCC \n");
		//pretty format
		for (int j = 0; j < Answer.size(); j++){
				System.out.format(" %8s%11s%11s%11s%11s%11s", Answer.get(j).get(0), Answer.get(j).get(1), 
															  Answer.get(j).get(2), Answer.get(j).get(3), 
															  Answer.get(j).get(4), Answer.get(j).get(5));
			System.out.println("\n");
		}
	}

	/****************************************************************************
	The main method is where we can choose that functions we want called and executed.
	I have written methods to print out each part of the file after its been read in
	and sorted. I have commented them out for terminal readability
	****************************************************************************/
	public static void main(String[] args){
		readAssembly();				// read in our file
		//printComments();			// print out comments if you want to see them
		printinstructionList(); 	// print out the instructions we parsed
		generateSchedule(); 		// go ahead and do the work generating the schedule
		System.out.println("\nANSWER TABLE AFTER INSTERTING STALLS");
		printSchedule();			// pring out our generated schedule

	}
}