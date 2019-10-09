################################################################################
# Author: Robert Carff
# Date:  October 9th, 2019
# Use: Buells 513 Mips Pipelining Project
# Description: Builds the java files in the Mips_Pipeline Project
################################################################################
JCC = javac

JFLAGS = -g

default: Main.class 

Main.class: Main.java
	$(JCC) $(JFLAGS) Main.java

clean:
	$(RM) *.class
