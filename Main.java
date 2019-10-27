
/****************************************************************************
Written by: Robert Carff
Date: October 7, 2019

This project was written for Dr. Buells 513 Computer Archtecture class 

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

*****************************************************************************/

/* IMPORTS */
import java.io.*;
import java.util.*;
/* MAIN CLASS */
public class Main {

	// I can use these lists if I want to see comments or instructions
	private static ArrayList<String> Comments = new ArrayList<>();
	private static ArrayList<String> Header = new ArrayList<>();
	private static ArrayList<String> instruction = new ArrayList<>();
	//2x2 dothework list (first list in parse funct)
	private static ArrayList<ArrayList<String>> instructionList = new ArrayList<ArrayList<String>>();
	//2x2 answer list (first list in parse funct)
	private static ArrayList<ArrayList<String>> Answer = new ArrayList<ArrayList<String>>();
	/****************************************************************************
	* Methods for printing out each of my lists 
	****************************************************************************/
	public static void printComments(){
		for(String line : Comments)
		  { 		      
	           System.out.println(line); 		
	      }
	      System.out.println("\n");
	}
	public static void printHeader(){
		for(String line : Header)
		  { 		      
	           System.out.println(line); 		
	      }
	  		System.out.println("\n");
	}
	public static void printinstruction(){
		for(String line : instruction)
		  { 		      
	           System.out.println(line); 		
	      }
	      System.out.println("\n");
	}
	//2X2 DOTHEWORK LIST PRINT METHODS
		public static void printinstructionList(){
		for(ArrayList line : instructionList)
		  { 		      
	           System.out.println(line); 		
	      }
	      System.out.println("\n");
	}	
	//2X2 ANSWER LIST PRINT METHODS
	public static void printAnswer(){
		for(ArrayList line : Answer)
		  { 		      
	           System.out.println(line); 		
	      }
	      System.out.println("\n");
	}
	/****************************************************************************
	* readAssembly() takes a piped in file of assembley instructions, and then 
	* turns each line of the file into a list. 
	****************************************************************************/
	public static void readAssembly(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Reading in file");
		while(scan.hasNextLine()){
			//System.out.println(scan.nextLine());
			String checkLine = scan.nextLine();
			//PARSE HERE
			try {
				//IF THE LINE STARTS WITHS A # SIGN
				if(checkLine.contains("#")){
					Comments.add(checkLine);
				}
				//IF THE LINE STARTS WITH A .
				else if(checkLine.contains(".")){
					Header.add(checkLine);
				}
				//IF THE LINE HAS A COLON IN IT
				else if(checkLine.contains(":")){
				}
				//ELSE IF THE LINE IS IN OUR DICTIONARY OF RISC-V INSTRUCTIONS
				else if(true){
					instruction.add(checkLine); // add the line to the list of instructions
					ArrayList<String> instructionParsed = new ArrayList<>(); //creating my new list
					ArrayList<String> instructionAnswer = new ArrayList<>(); //creating my new list
					String[] parsedInstruction = checkLine.split("\\s+");

					//ADDING TO COMPUTE LIST
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

					//ADDING TO ANSWER LIST
					instructionAnswer.add(parsedInstruction[1]);
					for(int i = 0; i <= 4; i++){
						instructionAnswer.add(Integer.toString(0));
					}
					Answer.add(instructionAnswer);
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
	* is executed, the time until the next execute in will be constant. 
	* (one set of resources)
	* Because of this, we can calculate instructions five variable's clock cycles
	* 
	* add in the rising/falling clock logic?
	*
	* Five Variables of each instruction: 
	*		Fetch, Decode, Execute, Decode, Write
	*
	* answer list has 6 spaces
	* 0 = MIPS instruction, 1 = fetch, 2 = decode, 3 = execute, 4 = decode, 5 = write
	****************************************************************************/
	public static void generateSchedule(){
		/* RESOURCE VARIABLES */
		int _if_  = 0;
		int _id_  = 1;
		int _ex_  = 2;
		int _mem_ = 3;
		int _wb_  = 4;
		//THESE ARE OUR DIFFERENT INSTRUCTION LISTS
		ArrayList<String> datatransfer = 
			new ArrayList<String>(Arrays.asList("lb","lbu","lh","lhu","lw","lwu","ld","sd","sb","sh","sw","fld","flw","fsd","fsw"));

		ArrayList<String> aluoperation = 
			new ArrayList<String>(Arrays.asList("add","addi","addw","addiw", "addiu"));

		//THIS IS THE FIRST PASS, IT LOOKS AT THE INSTRUCTION AND INCREMETS
		//THE RESOUCES TIMES REQUIRED, it then stores them in our answer table
		for (int j = 0; j < Answer.size(); j++){
			//if its a data transfer operation, increment
			if(datatransfer.contains(instructionList.get(j).get(0))){
				Answer.get(j).set(1, String.valueOf(_if_));
				_if_++;
				Answer.get(j).set(2, String.valueOf(_id_));
				_id_++;
				Answer.get(j).set(3, String.valueOf(_ex_));
				_ex_++;
				Answer.get(j).set(4, String.valueOf(_mem_));
				_mem_++;
				Answer.get(j).set(5, "-1");
				_wb_++;
			}
			if(aluoperation.contains(instructionList.get(j).get(0))){
				Answer.get(j).set(1, String.valueOf(_if_));
				_if_++;
				Answer.get(j).set(2, String.valueOf(_id_));
				_id_++;
				Answer.get(j).set(3, String.valueOf(_ex_));
				_ex_++;
				Answer.get(j).set(4, String.valueOf(_mem_));
				_mem_++;
				Answer.get(j).set(5, String.valueOf(_wb_));
				_wb_++;
			}

		}

		// SECOND PASS -INSERTING STALLS 
		for (int j = 0; j < Answer.size(); j++){

		}


	}
	/****************************************************************************
	Instruction fetchCC decodeCC executeCC decodeCC writeCC
	    lw         0.      1.        2.       3.       4.
	    lw         1.      2.        3.       4.       5.  
	   addi        2       3         4        5        6
	****************************************************************************/
	public static void printSchedule(){
		//format and print the final answer here
		System.out.println("                      *** ANSWER TABLE *** ");
		System.out.println(" Instruction - fetchCC - decodeCC - executeCC - decodeCC - writeCC ");
		//pretty format
		for (int j = 0; j < Answer.size(); j++){
				System.out.format(" %8s%11s%11s%11s%11s%11s", Answer.get(j).get(0), Answer.get(j).get(1), 
															  Answer.get(j).get(2), Answer.get(j).get(3), 
															  Answer.get(j).get(4), Answer.get(j).get(5));
			System.out.println("\n");
		}
	}


	public static void main(String[] args){
		readAssembly();
		//generateSchedule();
		//printComments();
		printinstructionList();
		generateSchedule();
		printSchedule();

	}
}