## Mips Pipeliner

#### Abstract


The Mips_Pipeliner is essentially a two-pass scheduler. I liked this approach because when only operating with one entity of each hardware resource, we only need to check two things. If the specific hardware is free, and if the registers need to be stalled for. 

The next instruction can not be executed until the required hardware resource is free. To keep track of this, we can keep a global variable for each resource that will be its "next available time". On the first pass of each instruction we store the current resource tick count, and then add the "completion time" of that instruction. This cascades across each instruction, allowing us to see their "soft" execution times. "Soft" meaning that we have not yet checked if the data being executed on is free. This turns out to be the C.1 diagram in our textbook. 
	
The second pass is used to check of the two registers being operated on require a variable that is still being calculated. In our project, for simplification, we assumed that each 4 or 5 cycle instructions would lock a register - and force stalls on the ensuing instructions. If this be the case, we simply check how long we must stall for, and add cascade that many cycles down the pipeline until the end. 

I added MIPS instructions from the back of the book as well as all four of your example programs (mipscode1-4.txt). Being that we reduced each instruction to either two cycle, four cycle, or five cycle operation, I created a list that contains each type of instruction. When the instruction is checked in the second pass, it is classified as one of the three types and then stalled for appropriately. 


### Implementation

I decided to implement my scheduler in java. The program takes in a file similar to the examples shown in class. I read in data and created two, two-by-two matricies. Matrix one stores the actual instruction and its required registers. This is incase we want to create a more agressive approach and check each of the registers in use. Matrix two holds my formatted output. Each line contains an instruction, along with its resouce-variable calculated time.

##### How to use on your own code

I used Duke Universities' [C to Mips compiler](http://reliant.colab.duke.edu/c2mips/ "C compiler") to convert typical  C code examples into Mips instructions. Feel free to convert your own code, and save it as a text file. The text file can then be piped into the program and scheduled, as shown below.

MAKE SURE ALL COMMENTS ARE REMOVED FROM CODE LINES

### How to compile

	make

### How to execute

So far I am running my application by passing in text files that are in the directory.

Example:

	java Main.java < C_Loop.txt


	java Main.java < mipscode2.txt


	java Main.java < mipscode3.txt



