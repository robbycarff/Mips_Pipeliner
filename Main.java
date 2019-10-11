
/****************************************************************************
Written by: Robert Carff
Date: October 7, 2019

This project was written for Dr. Buells 513 Computer Archtecture class 

everything is global because im lazy, I know this is horrible practice

	Instruction fetchCC decodeCC executeCC decodeCC writeCC
	    lw         0.      1.        2.       3.       4.
	    lw         1.      2.        3.       4.       5.  
	   addi        2       3         4        5        6
*****************************************************************************/

/* IMPORTS */
import java.io.*;
import java.util.*;
/* MAIN CLASS */
public class Main {
/* GLOBAL RESOURCE VARIABLES */
	int _fetch_ = 0;
	int _decode_1_ = 1;
	int _execute_ = 2;
	int _decode_2_ = 3;
	int _write_ = 4;
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
						instructionAnswer.add(Integer.toString(i));
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
	****************************************************************************/
	public static void generateSchedule(){
		//walking through a list of lists
		for (int j = 0; j < Answer.size(); j++){
			for (int i = 0; i <= 5; i++){


				//System.out.print(Answer.get(j).get(i) + " ");

			
			}
			//System.out.println("\n");
		}
	}

	public static void printSchedule(){
		//format and print the final answer here
		System.out.println(" * Answer Table * ");
		System.out.println(" Instruction - fetchCC - decodeCC - executeCC - decodeCC - writeCC ");
		//pretty format
		for (int j = 0; j < Answer.size(); j++){
			for (int i = 0; i <= 5; i++){
				System.out.print(Answer.get(j).get(i) + "       ");

			
			}
			System.out.println("\n");
		}
	}


	public static void main(String[] args){
		readAssembly();
		//generateSchedule();
		//printComments();
		printinstructionList();
		printSchedule();
		generateSchedule();
	}
}