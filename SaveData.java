//Imports
import jdk.jshell.spi.ExecutionControlProvider;
import org.lwjgl.Sys;
import org.newdawn.slick.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.Buffer;

public class SaveData implements ActionListener
{
    //Properties
    Player userGriffin;

    //Java Swing Properties
    //Display Save Data Window Properties
    private JFrame saveFrame;
    private JPanel savePanel;
    private JPanel saveOnePanel;
    private JPanel saveTwoPanel;
    private JPanel saveThreePanel;
    private JPanel saveFourPanel;
    private JPanel saveFivePanel;

    private JLabel saveOneLabel, saveTwoLabel, saveThreeLabel, saveFourLabel, saveFiveLabel;
    private JButton oneButton, twoButton, threeButton, fourButton, fiveButton;

    /*
     * Constructor
     */
    public SaveData(Player mainPlayer)
    {
        userGriffin = mainPlayer;
    }

    //Methods
    /*
     * Will check the current number of saves that the user has
     */
    public boolean checkNumSaves()
    {
        int saveCounter = 0;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/saveData.csv"));

            //Read through each line of saveData.csv and check if isEmpty is true or false
            String line;

            while ((line = reader.readLine()) != null)
            {
                //Use the comma as a separator
                String[] saveData = line.split(",");

                //Check if index 1 is true or false
                //If true, do not add 1 to the counter, if false, add 1 to the counter
                //NOTE: First line will always add 1 to saveCounter, so take into account
                if (saveData[1].equals("False"))
                {
                    saveCounter++;
                }

            }

            //Check value of saveCounter
            //Returns true if too many files, false if some are still empty
            if (saveCounter >= 5)
            {
                return true;
            }
            else
            {
                return false;
            }

        }
        catch (Exception e)
        {
            System.out.println("Exception while checking number of saves.");
            return true;
        }

    }

    /*
    * Displays the save data from saveData.csv
    */
    public void displaySaveData()
    {
        //Variables
        Dimension boxDimension = new Dimension(500, 100);

        //Create a small GUI to show the saves the user has
        saveFrame = new JFrame();
        saveFrame.setLocation(600, 200);
        saveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create main savePanel
        savePanel = (JPanel)saveFrame.getContentPane();

        //Set the layout to a vertical BoxLayout
        savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS));

        //Create each save panel and get the save data that corresponds
        //Create save panel one
        saveOnePanel = new JPanel();
        saveOnePanel.setLayout(new FlowLayout());

        //Create the label that is either empty or has data
        saveOneLabel = new JLabel(saveToString(1));

        saveOnePanel.add(new JLabel("Save One: \r"));
        saveOnePanel.add(saveOneLabel);
        oneButton = new JButton("Load Save");
        oneButton.addActionListener(this);
        saveOnePanel.add(oneButton);


        //Create save panel two
        saveTwoPanel = new JPanel();
        saveTwoPanel.setLayout(new FlowLayout());
        saveTwoPanel.setSize(boxDimension);

        saveTwoLabel = new JLabel(saveToString(2));

        saveTwoPanel.add(new JLabel("Save Two: "));
        saveTwoPanel.add(saveTwoLabel);
        twoButton = new JButton("Load Save");
        twoButton.addActionListener(this);
        saveTwoPanel.add(twoButton);


        //Create save panel three
        saveThreePanel = new JPanel();
        saveThreePanel.setLayout(new FlowLayout());
        saveThreePanel.setSize(boxDimension);

        saveThreeLabel = new JLabel(saveToString(3));

        saveThreePanel.add(new JLabel("Save Three: "));
        saveThreePanel.add(saveThreeLabel);
        threeButton = new JButton("Load Save");
        threeButton.addActionListener(this);
        saveThreePanel.add(threeButton);


        //Create save panel four
        saveFourPanel = new JPanel();
        saveFourPanel.setLayout(new FlowLayout());
        saveFourPanel.setSize(boxDimension);

        saveFourLabel = new JLabel(saveToString(4));

        saveFourPanel.add(new JLabel("Save Four: "));
        saveFourPanel.add(saveFourLabel);
        fourButton = new JButton("Load Save");
        fourButton.addActionListener(this);
        saveFourPanel.add(fourButton);

        //Create save panel five
        saveFivePanel = new JPanel();
        saveFivePanel.setLayout(new FlowLayout());
        saveFivePanel.setSize(boxDimension);

        saveFiveLabel = new JLabel(saveToString(5));

        saveFivePanel.add(new JLabel("Save Five: "));
        saveFivePanel.add(saveFiveLabel);
        fiveButton = new JButton("Load Save");
        fiveButton.addActionListener(this);
        saveFivePanel.add(fiveButton);

        //Add child panels to main panel
        savePanel.add(saveOnePanel);
        savePanel.add(saveTwoPanel);
        savePanel.add(saveThreePanel);
        savePanel.add(saveFourPanel);
        savePanel.add(saveFivePanel);

        //Pack the frame and make it visible
        saveFrame.pack();
        saveFrame.setVisible(true);
    }

    /*
    * When a file is selected, get the save data from saveData.csv
    */
    public void getSaveData(int saveFileNum)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/saveData.csv"));

            int lineCounter = 0;
            String line;

            while((line = reader.readLine()) != null)
            {
                //Find desired save
                lineCounter++;

                if(saveFileNum + 1 == lineCounter)
                {
                    //Found correct save
                    String[] data = line.split(",");

                    //Init the variables
                    userGriffin.myName = data[2];
                    userGriffin.myLevel = Integer.parseInt(data[3]);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception while retrieving save data.\nException: " + e.getMessage());
        }

    }

    /*
    * Will return a string of a summary of the save data in each file
    */
    public String saveToString(int saveNum)
    {
        //Open a BufferedReader
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/saveData.csv"));

            String line;
            int lineCounter = 0;

            while((line = reader.readLine()) != null)
            {
                lineCounter++;

                //Find if we are on the right line
                if(lineCounter == saveNum + 1)
                {
                    //Split the line
                    String[] data = line.split(",");

                    //First check if the file is empty or not
                    if(data[1].equals("True"))
                    {
                        //Return the word Empty
                        return "Empty";
                    }
                    else
                    {
                        //Return the user's name, and level
                        return data[2] + " Level: " + data[3];
                    }

                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading save data at saveToString()");
        }

        //Test if the program skips right to the end
        return "Skips";

    }

    /*
    * After a new save is created, will write new save data to the specified line
    * Will copy the entirety of saveData.csv and rewrite the intended line
    * Will first find the next open line
    */
    public void writeSaveData()
    {
        //Variables
        String[] saveLines = new String[10];
        int lineCounter = 0;
        boolean emptyLineFound = false;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("data/saveData.csv"));

            //Find the line for userGriffin.saveLine
            String line;

            while((line = reader.readLine()) != null)
            {
                //First need to detect if the line is empty
                String[] data = line.split(",");

                if(data[1].equals("True") && !emptyLineFound)
                {
                    //System.out.println("Test 1");
                    userGriffin.saveLine = lineCounter;
                    saveLines[lineCounter] = "";

                    //Adjust the values of data
                    data[1] = "False";
                    data[2] = userGriffin.myName;
                    data[3] = "1";

                    //Save the data into saveLines
                    for(int i = 0; i < 10; i++)
                    {
                        saveLines[lineCounter] += data[i] + ",";
                    }

                    emptyLineFound = true;
                }
                else
                {
                    //System.out.println("Test 2");
                    //Save the line into lineCounter
                    saveLines[lineCounter] = line;
                }

                lineCounter++;
            }

            reader.close();

            //Open the bufferedWriter
            writeToFile(saveLines);

        }
        catch(Exception e)
        {
            System.out.println("Exception while writing new save data.\nException: " + e.getMessage());
        }

    }

    /*
    * Write to the actual file
    */
    private void writeToFile(String[] lineData)
    {
        try
        {
            FileWriter fwOb = new FileWriter("data/saveData.csv", false);
            PrintWriter pwOb = new PrintWriter(fwOb, false);
            pwOb.flush();
            pwOb.close();
            fwOb.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("data/saveData.csv"));

            for(int i = 0; i < 6; i++)
            {
                writer.write(lineData[i]);
                writer.newLine();
            }

            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while writing save data.\nException: " + e.getMessage());
        }

    }


    //Method required by ActionListener interface
    public void actionPerformed(ActionEvent event)
    {
        Object control = event.getSource();

        //Find source of action
        if(control == oneButton)
        {
            userGriffin.saveLine = 1;

            if(saveToString(1).equals("Empty"))
            {
                saveFrame.dispose();

                //Create a new game
                new DenkiV6(userGriffin, true);
            }
            else
            {
                //Game is previously created
                //Open game w/ user's name, denkimon, and level
                saveFrame.dispose();

                getSaveData(1);

                //Start game loop
                new Gameplay(true, userGriffin);

            }
        }
        else if(control == twoButton)
        {
            userGriffin.saveLine = 2;

            if(saveToString(2).equals("Empty"))
            {
                saveFrame.dispose();

                //Create a new game
                new DenkiV6(userGriffin, true);
            }
            else
            {
                saveFrame.dispose();

                getSaveData(2);

                new Gameplay(true, userGriffin);
            }

        }
        else if(control == threeButton)
        {
            userGriffin.saveLine = 3;

            if(saveToString(3).equals("Empty"))
            {
                saveFrame.dispose();

                //Create a new game
                new DenkiV6(userGriffin, true);
            }
            else
            {
                saveFrame.dispose();

                getSaveData(3);

                new Gameplay(true, userGriffin);
            }

        }
        else if(control == fourButton)
        {
            userGriffin.saveLine = 4;

            if(saveToString(4).equals("Empty"))
            {
                saveFrame.dispose();

                //Create a new game
                new DenkiV6(userGriffin, true);
            }
            else
            {
                saveFrame.dispose();

                getSaveData(4);

                new Gameplay(true, userGriffin);
            }

        }
        else if(control == fiveButton)
        {
            userGriffin.saveLine = 5;

            if(saveToString(5).equals("Empty"))
            {
                saveFrame.dispose();

                //Create a new game
                new DenkiV6(userGriffin, true);
            }
            else
            {
                saveFrame.dispose();

                getSaveData(5);

                new Gameplay(true, userGriffin);
            }

        }
    }
}