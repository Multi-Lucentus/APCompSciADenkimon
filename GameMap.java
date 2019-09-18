public class GameMap
{
    //Properties
    private final int HEIGHT = 640, WIDTH = 640;

    //Methods
    /*
    * Will detect if the user is hitting the edge of the allowed area
    */
    public boolean isBlockedArea(int x, int y)
    {
        //Check if player is within 32 Pixels of the border of the map
        //NOTE: Map is 640px X 640px

        //Check x-coordinate
        if(x <= 32 || x >= 608)
        {
            return true;
        }

        //Check y-coordinate
        if(y <= 32 || y >= 570)
        {
            return true;
        }

        //Pretty much an else statement
        return false;
    }

    /*
    * Will detect if the user is in the area that
    */
    public boolean isCaptureArea(int x, int y)
    {
        //X Coordinate will be between 256 and 384
        if(x > 255 && x < 380)
        {
            //Y Coordinate will be between 288 and 416
            if(y > 237 && y < 367)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
