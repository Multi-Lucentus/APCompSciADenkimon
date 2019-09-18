/*
* Will hold data for each of the Denkimon's abilities
*/

public class DenkiAbility
{
    //Properties
    String abilityName;
    String abilityType;
    int abilityPower;
    double abilityAccuracy;
    int numUses;


    //Constructor
    /*
     * Will Initialize All of the Variables
     */
    public DenkiAbility(String name, String type, int pwr, double accuracy, int uses)
    {
        //Initialize the variables
        abilityName = name;
        abilityType = type;
        abilityPower = pwr;
        abilityAccuracy = accuracy;
        numUses = uses;

    }

}
