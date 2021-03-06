
package edu.umw.cpsc430spring16s1.tda;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 *Contains a program to take a csv filename as input
 *and convert it into the proper format for file insert
 *into temp database. 
 */

public class DataParser {



    public static void main(String args[]) {

        Hashtable<String,String> deviceIds 
            = new Hashtable<String,String>();

        Hashtable<String,String> locIds
            = new Hashtable<String,String>();


        //temporarily hardcode the serials into an array
        int[] serials = 
            new int[]{1001871,1001872,1001873,1001874,441384,532797,532798,
                532800,532801,533551,533560,555494,555497,555498,555501,555502,
                555505,555506,625100,625102,695788,710736,710737,710738,710739,
                733935,733936,733937,733938,733939,984395,1094012};

        //make ht withs id's and serials as keys
        for (int i=0; i< serials.length; i++){
            deviceIds.put(String.valueOf(serials[i]),String.valueOf(i+1));
        }

        //temporarily hardcode the loc codes into an array
        String[] locCodes = 
            new String[]{"GRV","HWY","BRR","LPJ","CCR","LGP","SBM","DIN",
                "BMS","BBR","CLK","BOR","WME","LSS","WFM","HCR","GBR",
                "EMP","LSP","LBB","GOB","MEM","GEP","NPE","DFM","EDC",
                "MOM","BPT","MER","UBB","BPA","MEL","UDC","UD2","HCB"};

        //make ht withs id's and loc codes as keys
        for (int i=0; i< locCodes.length; i++){
            locIds.put(locCodes[i],String.valueOf(i+1));
        }

        //loop through all files passed
        for (int j=0; j < args.length;j++){

            try{

                //make a new scanner object to read from file
                Scanner s = new Scanner(new FileReader(args[j]));

                //make outfile name
                String outFileName = 
                    args[j].substring(0,args[j].lastIndexOf(".")) + "Out.csv";

                //make a writer for output
                PrintWriter w = new PrintWriter(new FileWriter(outFileName));

                //trash the first two lines  
                for(int i=0; i<2;i++){s.nextLine();}        

                //save the serials from file 
                String [] fileSerials = s.nextLine().split(",");

                //trash the next line - elevation  
                s.nextLine();

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
                    s = new Scanner(new FileReader(args[j]));

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
                        if (!lineArray[locNum].equals("") 
                                && !lineArray[locNum].equals(" ") 
                                && !lineArray[0].equals("")){

                            //format datetime
                            lineArray[0] = lineArray[0].replace('/',',');
                            lineArray[0] = lineArray[0].replace(' ',',');
                            lineArray[0] = lineArray[0].replace(":00","");

                            //form new line
                            String newLine = lineArray[0] + "," 
                                + lineArray[locNum] + "," 
                                + locIds.get(locs[locNum-1]) + ","
                                + deviceIds.get(fileSerials[locNum]);

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
        }
    }

}//end class
