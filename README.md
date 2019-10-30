## Mips Pipeliner

#### Abstract

The Mips_Pipeliner is essentually a two-pass scheduler. I liked this approach, because when only operating with one entity of each hardware resource, we only need to check two things. If the specific hardware is free, and if the registers need to be stalled for. 

The next instruction can not be executed until the required hardware resource is free. To keep track of this, we can keep a global variable for each resouce that will be its "next availible time". On the first pass of each instruction we store the current resource tick count, and then add the "completion time" of that instruction. This cascades across each instruction, allowing us to see their "soft" execution times. "Soft" meaning that we have not yet checked if the data being executed on is free.
	
The second pass is used to check of the two registers being operated on require a variable that is still being calculated. If one of the two registers required by the mips instruction is not ready yet, we simply check how long we must stall for, and add that amount of cycles to the ensuing instruction execution times.

### Implementation

I decided to implement my scheduler in java. The program takes in a file similar to the examples shown in class. I read in data and created two, two-by-two matricies. Matrix one stores the actual instruction and its required registers. Matrix two holds my formatted output. Each line contains an instruction, along with its resouce-variable calculated time.

##### How to use on your own code

I used Duke Universitie's [C to Mips compiler](http://reliant.colab.duke.edu/c2mips/ "C compiler") to convert typical  C code examples into Mips instructions. Feel free to convert your own code, and save it as a text file. The text file can then be piped into the program and scheduled, as shown below.



### How to execute

So far I am running my application by passing in text files that are in the directory.

Example:

	java Main.java < C_Loop.txt


	java Main.java < mipscode2.txt


	java Main.java < mipscode3.txt



