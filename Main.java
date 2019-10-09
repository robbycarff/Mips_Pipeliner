
/****************************************************************************
Written by: Robert Carff
Date: October 7, 2019

This project was written for Dr. Buells 513 Computer Archtecture class 

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
	//These will be lists of lists, I can can print out the lines I want
	private static List<String> Comments = new ArrayList<>();
	private static List<String> Header = new ArrayList<>();
	private static List<String> Instructions = new ArrayList<>();
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

	public static void printInstructions(){
		for(String line : Instructions)
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
					System.out.println("This is a comment");
					Comments.add(checkLine);
				}
				//IF THE LINE STARTS WITH A .
				else if(checkLine.contains(".")){
					System.out.println("This is a Header line");
					Header.add(checkLine);
				}
				//IF THE LINE HAS A COLON IN IT
				else if(checkLine.contains(":")){
					System.out.println("This is a header");
				}
				//ELSE IF THE LINE IS IN OUR DICTIONARY OF RISC-V INSTRUCTIONS
				else if(true){
					System.out.println("This is an Instruction");
					// instruction " " reg3, reg2 ,reg1
					
					// parse this tasty morsel
					String parsedLine = checkLine.split(" ");
					Instructions.add(parsedLine);
				}
				else{
					System.out.println("Cant classify that line");
				}
			}
			catch(Exception e){
				System.out.println("you broke it dude");
			}
			//3 different sets of lists?
			System.out.println(checkLine);
			System.out.println("\n");
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
		//do the work here
	}

	public static void printSchedule(){
		//format and print the final answer here	
	}


	public static void main(String[] args){
		readAssembly();
		generateSchedule();
		printComments();
		printInstructions();

	}
}