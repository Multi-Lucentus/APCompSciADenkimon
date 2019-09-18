//Imports
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Random;

public class Roam extends BasicGameState
{
    //Properties
    private GameMap mainMap;
    private Image background;
    private Animation griffinSprite, up, down, left, right;
    private long delta = 11;
    private int numSteps = 7;
    private int numBattlesOccurred = 0;
    private float x = 64F, y = 64F;


    /*
    * Will take user's x and y coordinates and Math.random() to determine if a battle will be initiated
    */
    private boolean doesBattleOccur()
    {
        //Take x and y and them, multiply by 6 and see if % 2 = 1, if s
        //int numDetermine = (numSteps * ((int)x * (int)y)) / 10;

        if(numSteps == 50)
        {
            numSteps = 0;

            return true;
        }
        else
        {
            numSteps++;

            return false;
        }

    }

    @Override
    public int getID()
    {
        return 0;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException
    {

        //Initialize the images and animations
        background = new Image("data/images/finalDenkiMap.png");
        mainMap = new GameMap();

        //Create the movement Image arrays
        Image[] movementUp = {new Image("data/images/griffinSprites/griffinUp1.png"), new Image("data/images/griffinSprites/griffinUp2.png")};
        Image[] movementDown = {new Image("data/images/griffinSprites/griffinDown1.png"), new Image("data/images/griffinSprites/griffinDown2.png")};
        Image[] movementRight = {new Image("data/images/griffinSprites/griffinRight1.png"), new Image("data/images/griffinSprites/griffinRight2.png")};
        Image[] movementLeft = {new Image("data/images/griffinSprites/griffinLeft1.png"), new Image("data/images/griffinSprites/griffinLeft2.png")};

        int[] duration = {300, 300};

        //Create the animations for the sprite
        up = new Animation(movementUp, duration, false);
        down = new Animation(movementDown, duration, false);
        right = new Animation(movementRight, duration, false);
        left = new Animation(movementLeft, duration, false);

        //Set the original sprite orientation
        griffinSprite = right;
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException
    {
        //Find what the input is
        Input input = gameContainer.getInput();

        //Find what the input was
        if(input.isKeyDown(Input.KEY_W))
        {
            //Move up
            griffinSprite = up;

            //Check if on edge of map
            if(mainMap.isBlockedArea((int)x, (int)y))
            {
                //Set y coordinate to 3 pixels below limit
                y = 35;
            }
            else
            {
                //Check for battle w/ isCaptureArea()
                if(mainMap.isCaptureArea((int)x, (int)y))
                {
                    if(doesBattleOccur())
                    {
                        numBattlesOccurred++;

                        //Create the battle
                        stateBasedGame.enterState(2);
                    }
                }

                //Allow normal sprite movement
                griffinSprite.update(delta);

                y -= delta * 0.1F;
            }

        }
        else if(input.isKeyDown(Input.KEY_A))
        {
            //Move to the left
            griffinSprite = left;

            if (mainMap.isBlockedArea((int) x, (int) y))
            {
                //Set to 3 pixels to the left of limit
                x = 35;
            }
            else
            {
                //First check for battle w/ isCaptureArea()
                if (mainMap.isCaptureArea((int) x, (int) y))
                {

                    if(doesBattleOccur())
                    {
                        numBattlesOccurred++;

                        //Create the battle
                        stateBasedGame.enterState(2);
                    }
                }

                griffinSprite.update(delta);

                x -= delta * 0.1F;
            }

        }
        else if(input.isKeyDown(Input.KEY_S))
        {
            //Move down
            griffinSprite = down;

            if(mainMap.isBlockedArea((int)x, (int)y))
            {
                //Set to 3 pixels up of limit
                y = 567;
            }
            else
            {
                //First check for battle w/ isCaptureArea()
                if(mainMap.isCaptureArea((int)x, (int)y))
                {
                    //Random 25% chance
                    if(doesBattleOccur())
                    {
                        numBattlesOccurred++;

                        //Create the battle
                        stateBasedGame.enterState(2);
                    }
                }

                griffinSprite.update(delta);

                y += delta * 0.1F;
            }

        }
        else if(input.isKeyDown(Input.KEY_D))
        {
            //Move to the right
            griffinSprite = right;

            if(mainMap.isBlockedArea((int)x, (int)y))
            {
                //Set to 3 pixels to the left
                x = 605;
            }
            else
            {
                //First check for battle w/ isCaptureArea()
                if(mainMap.isCaptureArea((int)x, (int)y))
                {
                    //Random 25% chance
                    if(doesBattleOccur())
                    {
                        numBattlesOccurred++;

                        //Create the battle
                        stateBasedGame.enterState(2);
                    }

                }

                griffinSprite.update(delta);

                x += delta * 0.1F;
            }

        }
        else if(input.isKeyDown(Input.KEY_M))
        {
            //Open the menu
            stateBasedGame.enterState(3);
        }


        //leave(gameContainer, stateBasedGame);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException
    {
        //Draw the background
        background.draw(0, 0);

        //Write a note towards the bottom of the screen
        graphics.setColor(Color.black);
        graphics.drawString("Walk into the grass to encounter a wild Denkimon using W A S D\nPress 'M'  to open the menu", 32, 570);

        //Render the player's sprite
        griffinSprite.draw((int)x, (int)y);
    }


}
