/*
* Holds the data for the Player
*/

//Imports
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Player
{
    //Class-Level Properties
    public String myName;
    public int myLevel, exp, expToNextLevel;
    public int saveLine;

    public ArrayList<Denkimon> myDenkimon = new ArrayList<>();

    /*
    * Constructor
    */
    public Player()
    {
        //Initialize
        myLevel = 1;
        exp = 0;
        expToNextLevel = 50;

    }

    //Methods
    /*
    * Will initialize the user's denkimon, will be called every time they catch a new mon
    * TODO: Need to add these mons to saveData.csv
    */
    public void initDenkis()
    {
        //Variables
        int i = 0;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/saveData.csv"));

            int lineCounter = 0;
            String line;

            while((line = reader.readLine()) != null)
            {
                lineCounter++;

                //Find the desired save with saveLine
                if(saveLine + 1 == lineCounter)
                {
                    //Found desired save
                    String[] data = line.split(",");

                    //E represents an empty party spot
                    for(int j = 4; j < 9; j++)
                    {
                        //First check to make sure that data[j] is not "E"
                        if(data[j].equals("E"))
                        {
                            break;
                        }

                        //Init the denki in that spot
                        myDenkimon.add(new Denkimon(data[j]));

                    }

                }

            }
        }
        catch(Exception e)
        {
            System.out.println("Exception while initializing user's mons.\nException: " + e.getMessage());
        }
    }

    /*
    * Called after a battle if the user levels up
    */
    public void levelUp()
    {
        myLevel++;
        exp = 0;
        expToNextLevel += 50;
    }
}
