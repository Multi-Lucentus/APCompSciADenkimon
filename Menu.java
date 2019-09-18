/*
*
*/

//Imports
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState
{
    //Properties
    private Player userGriffin;
    private Image playerSprite;

    private String aboutQuote = "This game was created by Parker Brandt \nfor AP Computer Science A \nas an almost parody to one of his \nfavorite game series, Pokemon.";

    /*
    * Constructor
    * Will be passed in the mainPlayer object to work with
    */
    public Menu(Player mainPlayer)
    {
        userGriffin = mainPlayer;
    }


    //Methods
    @Override
    public int getID()
    {
        return 3;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException
    {
        gameContainer.setShowFPS(false);

        //Init player sprite
        playerSprite = new Image("data/images/griffinSprites/griffinDown2.png");

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException
    {
        Input input = gameContainer.getInput();

        //Check if user clicks 'M' again and if so, go back to roam
        if(input.isKeyDown(Input.KEY_M))
        {
            stateBasedGame.enterState(0);
        }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException
    {
        //Background
        graphics.setColor(Color.darkGray);
        graphics.fillRect(0, 0, 640, 640);

        //Draw a square for the user
        graphics.setColor(Color.lightGray);
        graphics.fillRect(32, 32, 100, 100);

        //Draw the player's name, level, and sprite
        playerSprite.draw(52, 52, 1.5F);
        graphics.setColor(Color.black);
        graphics.drawString(userGriffin.myName, 44, 92);
        graphics.drawString("Level: " + userGriffin.myLevel, 44, 114);

        //Draw each of their denki's names
        graphics.setColor(Color.lightGray);
        graphics.fillRect(32, 150, 570, 140);

        graphics.setColor(Color.black);
        graphics.drawString(userGriffin.myName + "'s DenkiMon", 230, 155);

        //Coordinates for Denki names
        float i = 275, j = 170;

        for(Denkimon denki : userGriffin.myDenkimon)
        {
            graphics.drawString(denki.myName, i, j);

            j += 15;
        }

        //About this game
        graphics.setColor(Color.lightGray);
        graphics.fillRect(32, 300, 570, 200);

        graphics.setColor(Color.black);
        graphics.drawString("__About this Game__", 230, 315);
        graphics.drawString(aboutQuote, 140, 330);

        //Note at bottom
        graphics.drawString("(Press M again to return\n     to main screen)", 415, 590);
    }
}
