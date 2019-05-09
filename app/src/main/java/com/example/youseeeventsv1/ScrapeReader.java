package com.example.youseeeventsv1;
import java.util.Scanner;
import java.lang.Character;

public class ScrapeReader {



    public static void main(String[] args){
        //create Scanner object
        Scanner eventScanner = new Scanner("./scrape_fitness&well-being.txt");
        //read the whole file
        while(eventScanner.hasNextLine() == true){
            String current = eventScanner.nextLine();

            int first = current.indexOf(',');
            String eventName = current.substring(0, first);
            String remainder = current.substring(first + 1);

            int second = remainder.indexOf(',');
            String eventLocation = remainder.substring(first + 1, second);
            String remainder2 = remainder.substring(second + 1);

            int third = remainder2.indexOf(',');
            String eventTime = remainder2.substring(second + 1, third);
            String remainder3 = remainder2.substring(third + 1);

            int fourth = remainder3.indexOf(',');
            String eventDate = remainder3.substring(third + 1, fourth);

            //last thing in line
            String eventType = remainder3.substring(fourth + 1);

            //get rid of whitespace
            eventName = eventName.trim();
            eventLocation = eventLocation.trim();
            eventTime = eventTime.trim();
            eventDate = eventDate.trim();
            eventType = eventType.trim();

            //convert eventDate to a Date object
            int month = 0;
            int date = 0;
            int seperate = 0;
            for(int x = 0; x < eventDate.length(); x++){
                char index = eventDate.charAt(x);
                if(Character.isDigit(index) == true){
                    seperate = x;
                    break;
                }
            }
            //create Date object
            //date = parseInt(eventDate.substring(seperate));

            //month = ;

            //Date realDate = new Date(2019,,date);


            //create an event object
            //Event temp = new Event(eventName, eventDescription, realDate, eventTime, eventLocation, eventType);



        }
    }

}
