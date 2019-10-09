
/****************************************************************************
Written by: Robert Carff
Date: October 7, 2019

This project was written for Dr. Buells 513 Computer Archtecture class  
*****************************************************************************/

/*IMPORTS */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream; 
import java.net.HttpURLConnection;
import java.net.MalformedURLException; 
import java.net.URL;
import java.io.File;
import java.util.Scanner;
import java.io.*;

/*MAIN CLASS */
public class Main {
	//this is what parses the text file
	public static void readAssembly(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Reading in file");
		while(scan.hasNextLine()){
			System.out.println(scan.nextLine());
			//PARSE HERE
			
			//3 different sets of lists?


		}
		//planning to write instructions to a vector
	}

	/****************************************************************************
	* The idea of generateSchedule() relies on the fact the the once an instruction
	* is executed, the time until the next execute in will be constant.
	* Because of this, we can calculate instructions five variable's clock cycles
	* 
	*
	* Five Variables of each instruction: 
	*		Fetch, Decode, Execute, Decode, Write
	*
	****************************************************************************/
	public static void generateSchedule(){
		//pass in lists and calculate times
		



		
	}


	public static void main(String[] args){
		readAssembly();
		generateSchedule();

	}
}