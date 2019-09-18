/*
* Holds data for the Denkimon, including stats and graphics
*/

//Imports
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Denkimon
{
    //Class-Level Properties
    public String myName;
    public String myType;
    public int hp, battleHP, atkDmg, defense, speed;
    public String move1Name, move2Name, move3Name, move4Name;
    public ArrayList<DenkiAbility> myAbilities = new ArrayList<>();
    public Animation idleAnimation;

    /*
    * Constructor
    * Will initialize the Denkimon's stats and moves
    */
    public Denkimon(String denkiName)
    {
        //Initialize variables
        myName = denkiName;

        //Initialize stats and graphics
        initStats();
        initGraphics();
    }

    //Methods
    /*
    * Will initialize the Denkimon's graphics
    */
    private void initGraphics()
    {
        try
        {
            Image[] idleMovement = {new Image("data/images/battleSprite/denkiSprites/" + myName.toLowerCase() + "1.png"), new Image("data/images/battleSprite/denkiSprites/" + myName.toLowerCase() + "2.png")};

            int[] duration = {300, 300};

            idleAnimation = new Animation(idleMovement, duration, true);
        }
        catch(SlickException e)
        {
            System.out.println("Exception while initializing graphics of Denkimon.");
        }

    }

    /*
    * Will initialize the Denkimon's moves
    */
    private void initMoves(String denkiMoveName)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/moves.csv"));

            String line;

            while ((line = reader.readLine()) != null)
            {
                //Split the line
                String data[] = line.split(",");

                //Check if the name of the Denkimon matches the name in the parameter
                //If it doesn't match, will just continue
                if (denkiMoveName.equals(data[0]))
                {
                    //It has the name of the Denkimon's ability, so it should grab it's data from moves.csv
                    //Create temp variable to hold Type, Dmg, Uses, and Accuracy
                    String type = data[1];
                    int damage = Integer.parseInt(data[2]);
                    int uses = Integer.parseInt(data[3]);
                    double accuracy = Double.parseDouble(data[4]);

                    //Create the new DenkiAbility object and add to myAbilities
                    myAbilities.add(new DenkiAbility(denkiMoveName, type, damage, accuracy, uses));
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception while initializing denkimon's moves.\n Exception: " + e.getMessage());
        }
    }

    /*
    * Will initialize the Denkimon's stats
    */
    private void initStats()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/denkiStats.csv"));

            String line;

            while((line = reader.readLine()) != null)
            {
                String[] denkiStats = line.split(",");

                //Look for the specific Denkimon's data
                if(denkiStats[0].equals(myName))
                {
                    //Init the stats
                    //Init the type of the denki
                    myType = denkiStats[1];

                    //Get the names of the moves and then initialize the stats of those
                    move1Name = denkiStats[2];
                    initMoves(move1Name);
                    move2Name = denkiStats[3];
                    initMoves(move2Name);
                    move3Name = denkiStats[4];
                    initMoves(move3Name);
                    move4Name = denkiStats[5];
                    initMoves(move4Name);

                    //Init the base stats of the Denkimon
                    hp = Integer.parseInt(denkiStats[6]);
                    battleHP = hp;
                    atkDmg = Integer.parseInt(denkiStats[7]);
                    defense = Integer.parseInt(denkiStats[8]);
                    speed = Integer.parseInt(denkiStats[9]);

                }

            }

        }
        catch(Exception e)
        {
            System.out.println("Exception while initializing Denkimon's stats.");
        }
    }

    /*
    * Will return the damage of the move used
    * @param the move used from the denkimon
    */
    public int moveUsed(DenkiAbility abilityUsed, Denkimon enemyMon)
    {
        //Variables
        float damage;

        //First get the power of the move
        damage = abilityUsed.abilityPower;

        //Now check for super effectiveness
        if(isSuperEffective(abilityUsed.abilityType, enemyMon))
        {
            //Multiply by 2
            damage *= 2;
        }
        else if(isNotVeryEffective(abilityUsed.abilityType, enemyMon))
        {
            //Multiply by 0.25
            damage *= 0.25;
        }

        //Compare users strength and enemy's defense
        damage += 0.50 * atkDmg;
        damage -= 0.50 * enemyMon.defense;

        return (int)damage;
    }

    /*
    * Will check if a move is not very effective and if so will return true
    */
    private boolean isNotVeryEffective(String abilityType, Denkimon enemyMon)
    {
        //Variables
        String actualWEffect;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/superEffective.csv"));
            BufferedReader reader2 = new BufferedReader(new FileReader("data/denkiStats.csv"));

            String line, line2;

            while((line = reader.readLine()) != null)
            {
                String[] data = line.split(",");

                //Read if abilityType is weak against the enemy's type
                //Find abilityType in first arraySpot
                if(abilityType.equals(data[0]))
                {
                    //Find type that is actually weak against
                    actualWEffect = data[1];

                    //Find mon in denkiStats.csv
                    while((line2 = reader2.readLine()) != null)
                    {
                        String[] data2 = line2.split(",");

                        if(data2[0].equals(enemyMon.myName))
                        {
                            //Check if actualWEffect matches
                            if(data2[1].equals(actualWEffect))
                            {
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception while checking if not very effective");
        }

        return false;
    }

    /*
    * Will check if a move is super effective and if so will return true
    */
    private boolean isSuperEffective(String abilityType, Denkimon enemyMon)
    {
        //Variables
        String actualSEffect;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/superEffective.csv"));
            BufferedReader reader2 = new BufferedReader(new FileReader("data/denkiStats.csv"));

            String line, line2;

            while((line = reader.readLine()) != null)
            {
                String data[] = line.split(",");

                //Read if the moveType is strong against the Denki's type
                //Find moveType in first arraySpot
                if(abilityType.equals(data[0]))
                {
                    //Find the type thats super effective and see if that matches that denkis type
                    actualSEffect = data[2];

                    //Find the mon in denkiStats.csv
                    while((line2 = reader2.readLine()) != null)
                    {
                        //Check if the mon's name matches what we need
                        String data2[] = line2.split(",");

                        if(data2[0].equals(enemyMon.myName))
                        {
                            //Check if the denkiHit's type matches actualSEffect, and if so return true
                            if(data2[1].equals(actualSEffect))
                            {
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                    }
                }
            }

        }
        catch(Exception e)
        {
            System.out.println("Exception while checking if super effective.");
        }

        return false;

    }


}
