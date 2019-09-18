/*
* Will be our secondary game state for when a battle takes place
*/

//Imports
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class Battle extends BasicGameState
{
    //Properties
    private Player userGriffin;
    private Denkimon userMon, enemyMon;
    private GameMap mainMap;
    private String dialogueQuote;
    private boolean leaveBattle;
    private int userMonOut = 0;
    private int elapsedTime = 0, time1 = 0, time2 = 0;
    private final int delay = 2000;

    private Image battleBackground;

    /*
     * Constructor
     * Will initialize variables
     */
    public Battle(Player mainPlayer)
    {
        userGriffin = mainPlayer;
        userGriffin.initDenkis();

        //Get the user's first Denkimon
        userMon = userGriffin.myDenkimon.get(0);
    }


    //Methods
    /*
     * Will be called at the end of a battle
     * @param userWon tells if the user either won, or caught the wild mon, will be false if all of the user's mons are killed
     */
    private void battleEnd(boolean userWon)
    {
        dialogueQuote = "";

        //Check if the user won
        if(userWon)
        {
            //Congratulatory Message in dialogue box
            //Check exp gained and compare to exp to next level
            userGriffin.exp += 50;

            //Check if exp >= expToNextLevel
            if(userGriffin.exp >= userGriffin.expToNextLevel)
            {
                //Trigger Level Up
                userGriffin.levelUp();
                dialogueQuote += "You leveled up!\n";
            }

            //Reset all the user's Denkimons HP
            for(Denkimon denki : userGriffin.myDenkimon)
            {
                denki.battleHP = denki.hp;
            }

            //Message in dialogue box
            dialogueQuote += "Congratulations!\nYou won!";

            leaveBattle = true;
        }
        else
        {
            dialogueQuote = "You lost...\nPlease try again...";

            leaveBattle = true;
        }

    }

    /*
     * Will initialize a new wild Denkimon at the beginning of each battle
     * @return Will return the wild Denkimon created
     */
    private Denkimon initWildDenki()
    {
        //Variables
        String wildDenkiName = "";
        Random rand = new Random();
        int randDenkiNum = rand.nextInt(6);
        int currentLine = 0;

        //Read denkiStats.csv
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/denkiStats.csv"));

            String line;

            while(currentLine <= randDenkiNum)
            {
                line = reader.readLine();

                //Check if the current line equals the randDenkiNum
                if(currentLine == randDenkiNum)
                {
                    //Split the line
                    String[] data = line.split(",");

                    //Make sure the line isn't the very first line, if so just go to the next line
                    if(data[0].equals("Denkimon Name"))
                    {
                        //Set the name to Stinger
                        wildDenkiName = "Stinger";
                    }
                    else
                    {
                        //Normal Progression
                        wildDenkiName = data[0];
                    }
                }

                currentLine++;
            }

        }
        catch(Exception e)
        {
            System.out.println("Exception while initializing wild Denkimon.\nException: " + e.getMessage());

        }

        //In case it failed, init as Magico
        if(wildDenkiName.isEmpty())
        {
            wildDenkiName = "Magico";
        }

        return new Denkimon(wildDenkiName);
    }

    /*
     * Will create a 25% chance for each of the enemyMon's moves to be used
     */
    private void enemyMove(int userMoveUsed)
    {
        //Will return a number 0 <= n < 1
        double enemyRandMove = Math.random();

        dialogueQuote = userMon.myName + " used " + userMon.myAbilities.get(userMoveUsed - 1).abilityName + "!\n";

        //0 <= n <= 0.25 will be move 1
        if(enemyRandMove <= 0.25)
        {
            dialogueQuote += "Enemy " + enemyMon.myName + " used " + enemyMon.move1Name + "!";
            userMon.battleHP -= enemyMon.moveUsed(enemyMon.myAbilities.get(0), userMon);
        }
        else if(enemyRandMove > 0.25 && enemyRandMove <= 0.50)
        {
            //Move 2
            dialogueQuote += "Enemy " + enemyMon.myName + " used " + enemyMon.move2Name + "!";
            userMon.battleHP -= enemyMon.moveUsed(enemyMon.myAbilities.get(1), userMon);
        }
        else if(enemyRandMove > 0.50 && enemyRandMove <= 0.75)
        {
            //Move 3
            dialogueQuote += "Enemy " + enemyMon.myName + " used " + enemyMon.move3Name + "!";
            userMon.battleHP -= enemyMon.moveUsed(enemyMon.myAbilities.get(2), userMon);
        }
        else
        {
            //Move 4
            dialogueQuote += "Enemy " + enemyMon.myName + " used " + enemyMon.move4Name + "!";
            userMon.battleHP -= enemyMon.moveUsed(enemyMon.myAbilities.get(3), userMon);
        }
    }

    /*
     * Will allow the user to catch the wild Denkimon that they are facing and add to the end of their party
     * TODO
     */
    private void userCatchWildDenki()
    {
        int denkiCounter = 0;

        //Need to check if user has 6 Denkimon
        //If not, allow them to catch, then write that to csv file
        for(Denkimon denki : userGriffin.myDenkimon)
        {
            denkiCounter++;
        }

        if(denkiCounter == 6)
        {
            //Return, too many denkimon
            dialogueQuote = "Cannot capture wild Denkimon.\nHave 6 Denkis in party currently";
        }
        else
        {
            //User can catch the denkimon
            userGriffin.myDenkimon.add(new Denkimon(enemyMon.myName));

            dialogueQuote = "You caught " + enemyMon.myName + "!";

            //End the battle
            battleEnd(true);
        }
    }

    /*
     * Will allow the user to run from the battle at hand and go back to Roam.java
     */
    private void userRunFromBattle()
    {
        //Don't reset the user's HP
        leaveBattle = true;
    }


    /*
     * Will check and see if the user still has alive Denkimons in their party
     */
    private boolean areUserMonsAlive()
    {
        //Variables
        int aliveDenkiCounter = 0;

        //Will iterate through userGriffin.myDenkimon and see if the user has any alive mons
        for(Denkimon denkimon : userGriffin.myDenkimon)
        {
            if(denkimon.battleHP > 0)
            {
                aliveDenkiCounter++;
            }
        }

        //Check how many are alive
        if(aliveDenkiCounter > 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /*
     * Will allow the user to switch the current Denkimon with one in their part
     */
    private void userSwitchMon()
    {
        //First check with areUserMonsAlive()
        if(areUserMonsAlive())
        {
            //If so, send out the next mon that is not dead
            userMonOut++;

            for(int i = userMonOut; i < userGriffin.myDenkimon.size(); i++)
            {
                if(userGriffin.myDenkimon.get(i).battleHP > 0)
                {
                    userMon = userGriffin.myDenkimon.get(i);
                    break;
                }

            }

        }
        else
        {
            battleEnd(false);
        }

    }



    //Methods from BasicGameState
    @Override
    public void enter(GameContainer container, StateBasedGame game)
    {
        enemyMon = initWildDenki();

        dialogueQuote = "A wild " + enemyMon.myName + " has appeared!";
        dialogueQuote += "\nYou sent out " + userMon.myName + "!";

        leaveBattle = false;
    }

    @Override
    public int getID()
    {
        return 2;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException
    {
        //Initialize properties
        battleBackground = new Image("data/images/battleSprite/battleBackground.png");

        //Init the enemyMon
        enemyMon = initWildDenki();

        //beginning quote of the battle
        dialogueQuote = "A wild " + enemyMon.myName + " has appeared!";
        dialogueQuote += "\nYou sent out " + userMon.myName + "!";
    }


    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException
    {
        //Find the input given
        Input input = gameContainer.getInput();

        //Check if the user wants to leave the battle
        if(leaveBattle)
        {
            //Reset leaveBattle

            elapsedTime += delta;

            if(elapsedTime >= 3000)
            {
                elapsedTime = 0;
                stateBasedGame.enterState(0);
            }
            else
            {
                elapsedTime += delta;
            }
        }

        //Check if either the enemyMon, or the userMon has died
        if(enemyMon.hp <= 0)
        {
            //Trigger happy ending WOOO
            battleEnd(true);
        }

        if(userMon.battleHP <= 0)
        {
            //Check for switching mons
            //Will take care of no other usermon's available in function
            userSwitchMon();
        }

        //Get the user's input
        int mouseX, mouseY;

        //Check for where the user's mouse location is
        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
        {
            //Get the mouse's x and y coordinates
            mouseX = input.getMouseX();
            mouseY = input.getMouseY();


            //First check for switching and capturing and running
            //Switch Mon
            if(mouseX > 470 && mouseX < 570 && mouseY > 575 && mouseY < 600)
            {
                userSwitchMon();
            }

            //Capturing
            if(mouseX > 470 && mouseX < 570 && mouseY > 610 && mouseY < 635)
            {
                dialogueQuote = "You captured " + enemyMon.myName + "!\n";

                userCatchWildDenki();
            }

            //Run from Battle
            if(mouseX > 580 && mouseX < 630 && mouseY > 590 && mouseY < 615)
            {
                userRunFromBattle();
            }

            //Move 1 (myAbilities.get(0)
            if(mouseX > 32 && mouseX < 132 && mouseY > 575 && mouseY < 625)
            {
                dialogueQuote = userMon.myName + " used " + userMon.move1Name + "!";
                enemyMon.hp -= userMon.moveUsed(userMon.myAbilities.get(0), enemyMon);

                enemyMove(1);
            }
            else if(mouseX > 135 && mouseX < 235 && mouseY > 575 && mouseY < 625)
            {
                //Move 2
                dialogueQuote = userMon.myName + " used " + userMon.move2Name + "!";
                enemyMon.hp -= userMon.moveUsed(userMon.myAbilities.get(1), enemyMon);

                enemyMove(2);
            }
            else if(mouseX > 238 && mouseX < 338 && mouseY > 575 && mouseY < 625)
            {
                //Move 3
                dialogueQuote = userMon.myName + " used " + userMon.move3Name + "!";
                enemyMon.hp -= userMon.moveUsed(userMon.myAbilities.get(2), enemyMon);

                enemyMove(3);
            }
            else if(mouseX > 341 && mouseX < 441 && mouseY > 575 && mouseY < 625)
            {
                //Move 4
                dialogueQuote = userMon.myName + " used " + userMon.move4Name + "!";
                enemyMon.hp -= userMon.moveUsed(userMon.myAbilities.get(3), enemyMon);

                enemyMove(4);
            }

        }

    }


    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException
    {
        //Draw the background
        battleBackground.draw(0, 0);

        //Draw the Mon's sprites
        if(!leaveBattle)
        {
            userMon.idleAnimation.draw(180, 370, 120, 120);
            enemyMon.idleAnimation.draw(457, 227, 80, 80);
        }

        //Write the enemyMon's name above the sprite
        graphics.setColor(Color.black);
        graphics.drawString(enemyMon.myName + "  HP: " + enemyMon.hp, 460, 210);
        graphics.drawString(userMon.myName + "  HP: " + userMon.battleHP, 190, 350);

        //Draw the dialogue box
        graphics.drawRect(32, 500, 576, 70);

        graphics.setColor(Color.lightGray);
        graphics.fillRect(32, 500, 576, 70);

        graphics.setColor(Color.black);
        graphics.drawString(dialogueQuote, 40, 508);

        //Draw the move boxes
        //Move 1
        graphics.setColor(Color.lightGray);
        graphics.fillRect(32, 575, 100, 50);
        graphics.setColor(Color.black);
        graphics.drawString("Move 1", 52, 590);

        //Move 2
        graphics.setColor(Color.lightGray);
        graphics.fillRect(135, 575, 100, 50);
        graphics.setColor(Color.black);
        graphics.drawString("Move 2", 155, 590);

        //Move 3
        graphics.setColor(Color.lightGray);
        graphics.fillRect(238, 575, 100, 50);
        graphics.setColor(Color.black);
        graphics.drawString("Move 3", 258, 590);

        //Move 4
        graphics.setColor(Color.lightGray);
        graphics.fillRect(341, 575, 100, 50);
        graphics.setColor(Color.black);
        graphics.drawString("Move 4", 361, 590);

        //Switch Box (Blue)
        graphics.setColor(Color.cyan);
        graphics.fillRect(470, 575, 100, 25);
        graphics.setColor(Color.black);
        graphics.drawString("Switch", 495, 580);

        //Capture Box (Red)
        graphics.setColor(Color.red);
        graphics.fillRect(470, 610, 100, 25);
        graphics.setColor(Color.black);
        graphics.drawString("Capture", 495, 613);

        //Run Box (Black with White Lettering)
        graphics.setColor(Color.black);
        graphics.fillRect(580, 590, 50, 25);
        graphics.setColor(Color.white);
        graphics.drawString("RUN", 590, 595);
    }



}
