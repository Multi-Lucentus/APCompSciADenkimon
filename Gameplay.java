//Imports
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gameplay extends StateBasedGame
{
    //Properties
    private Player userGriffin;
    private AppGameContainer gameApp;

    //Application Properties
    private final int HEIGHT = 640, WIDTH = 640;
    private final int FPS = 60;

    public Gameplay(boolean firstOpen, Player mainPlayer)
    {
        super("Denkimon");
        userGriffin = mainPlayer;

        if(firstOpen)
        {
            runGame();
        }
    }


    //Methods
    /*
    * Will start the game
    */
    private void runGame()
    {
        try
        {
            gameApp = new AppGameContainer(new Gameplay(false, userGriffin));
            gameApp.setDisplayMode(640, 640, false);
            gameApp.setTargetFrameRate(FPS);
            gameApp.start();
        }
        catch(SlickException e)
        {
            System.out.println("Exception while starting the game.\n Exception: " + e.getMessage());
        }

    }


    @Override
    /*
    * Will initialize the states
    * Required by StateBasedGame
    */
    public void initStatesList(GameContainer gameContainer) throws SlickException
    {
        this.addState(new Roam());
        this.addState(new Battle(userGriffin));
        this.addState(new Menu(userGriffin));
    }


}
