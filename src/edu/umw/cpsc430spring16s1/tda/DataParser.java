
package edu.umw.cpsc430spring16s1.tda;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *Contains a program to take a csv filename as input
 *and convert it into the proper format for file insert
 *into temp database. 
 */

public class DataParser {

    public static void main(String args[]) {

        try{

            //make a new scanner object to read from file
            Scanner s = new Scanner(new FileReader(args[0]));

            //make a writer for output
            PrintWriter w = new PrintWriter(new FileWriter("outfile.csv"));

            //trash the first four lines  
            for(int i=0; i<4;i++){s.nextLine();}        

            //get locations into an ArrayList
            String locLine = s.nextLine();
            locLine = (locLine.substring(locLine.indexOf(',')+1)).trim();
            String [] locs = locLine.split(",");
            ArrayList<String> locList 
                = new ArrayList<String>(Arrays.asList(locs));

            //close scanner and filereader
            s.close();

            //make a location counter
            int locNum = 0;

            //make a pass through for every location 
            for (String loc : locList) {

                //make a new scanner object to read from file
                s = new Scanner(new FileReader(args[0]));

                //increment loc counter
                locNum++;

                //advance the scanner to the beginning of temps
                for(int i=0; i<5;i++){s.nextLine();}        

                //process temps to the file's end
                while (s.hasNext()){

                    //get the full line
                    String line = s.nextLine();

                    //fix terminating comma
                    if(line.endsWith(","))
                        line = line + "X";  

                    //split on commas for processing
                    String [] lineArray = line.split(",");

                    //change X back to empty string
                    if(lineArray[lineArray.length-1].equals("X"))
                        lineArray[lineArray.length-1] = "";

                    //check for a valid temp in this row for this loc
                    if (!lineArray[locNum].equals("")){

                        //format datetime
                        lineArray[0] = lineArray[0].replace('/',',');
                        lineArray[0] = lineArray[0].replace(' ',',');
                        lineArray[0] = lineArray[0].replace(":00","");

                        //form new line
                        String newLine = lineArray[0] + "," 
                            + lineArray[locNum] + "," + loc;

                        //write to the outfile
                        w.println(newLine); 

                    }//end if valid temp

                }//end while

            }//end for loc 
        
        //close the scanner and printwriter
        s.close();
        w.close();

        
        }catch(Exception e){
            e.printStackTrace(); 
            System.out.println("it's broken");}

    }//end main

}//end class
