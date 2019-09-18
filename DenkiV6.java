/*
* Parker Brandt
* Java Application
* @title Denkimon
* @author Lucentus
* @version Beta 1.6.0
*/

//Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DenkiV6 implements ActionListener
{
    //Class-Level Properties
    private Player userGriffin;
    private GameMap mainMap;
    private SaveData currentSave;

    //Java Swing Windows Properties
    //Title Window Properties
    private JFrame titleFrame;
    private JPanel titlePanel;
    private JPanel imagePanel;
    private JPanel buttonPanel;

    private JButton newGameButton;
    private JButton openSaveButton;

    //Opening Window Properties
    private JFrame openFrame;
    private JPanel openPanel;
    private JPanel openImagePanel;
    private JPanel dialoguePanel;

    private JTextArea dialogueArea;
    private JButton nextButton;
    private int nextCounter = 0;

    //User Info Window Properties
    private JPanel userPanel;

    private JTextField nameField;
    private JButton enterButton;


    //Constructor
    public DenkiV6()
    {
        //Initialize variables
        userGriffin = new Player();
        mainMap = new GameMap();
        currentSave = new SaveData(userGriffin);

        //Create the title screen
        openTitleScreen();
    }

    //Secondary Constructor
    //Will start a New Game for the user
    public DenkiV6(Player mainPlayer, boolean newGame)
    {
        //Initialize variables
        userGriffin = mainPlayer;
        mainMap = new GameMap();
        currentSave = new SaveData(userGriffin);

        //Start the New Game
        createOpeningScene();
    }


    //Methods
    /*
    * Will start the logic of the game
    */
    public static void main(String args[])
    {
        //Create new instance of DenkiV6
        new DenkiV6();
    }


    /*
    * Will show the opening scene with Prof Oak
    * Will introduce the user to the game and get their name
    * Only used for new games
    */
    private void createOpeningScene()
    {
        //Create the openFrame
        openFrame = new JFrame();
        openFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        openFrame.setLocation(600, 200);

        //Create the openPanel, the main panel for openFrame
        openPanel = (JPanel)openFrame.getContentPane();
        openPanel.setLayout(new BorderLayout());

        //Create the image panel
        //GIF of Prof Oak will be held here
        openImagePanel = new JPanel();
        JLabel profOakLabel = new JLabel();
        profOakLabel.setOpaque(true);
        ImageIcon oakIcon = new ImageIcon("data/images/oak.gif");
        profOakLabel.setIcon(oakIcon);
        openImagePanel.add(profOakLabel);

        //Create the dialogue panel
        dialoguePanel = new JPanel();
        dialogueArea = new JTextArea(5, 40);
        dialogueArea.setEditable(false);
        dialoguePanel.add(dialogueArea);

        //Add beginning dialogue
        dialogueArea.setText("Hello, and welcome, Trainer!");

        //Add a nextButton
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        dialoguePanel.add(nextButton);

        //Add the child panels to the openPanel
        openPanel.add(openImagePanel, BorderLayout.CENTER);
        openPanel.add(dialoguePanel, BorderLayout.PAGE_END);

        //Pack the frame and make it visible
        openFrame.pack();
        openFrame.setVisible(true);

    }


    /*
    * Will get the user's name
    */
    private void getUserInfo()
    {
        //Hide the dialoguePanel and replace with userPanel
        openPanel.remove(dialoguePanel);

        //Create the userPanel
        userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout());

        //Add a JTextField for the name
        userPanel.add(new JLabel("What is your name?: "));
        nameField = new JTextField(20);
        userPanel.add(nameField);

        //Create the enterButton that will end user data collection
        enterButton = new JButton("Enter");
        enterButton.addActionListener(this);
        userPanel.add(enterButton);

        //Add userPanel to where dialoguePanel was
        openPanel.add(userPanel, BorderLayout.PAGE_END);

    }


    /*
    * Will create the title screen
    * Will show the title screen for Denkimon and allow the user to choose "New Game" or "Open Save"
    * Complete
    */
    private void openTitleScreen()
    {
        //Create the titleFrame which will hold the title card
        titleFrame = new JFrame();
        titleFrame.setLocation(600, 200);
        titleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create the main panel for titleFrame and set a BorderLayout
        titlePanel = (JPanel)titleFrame.getContentPane();
        titlePanel.setLayout(new BorderLayout());

        //Create the image panel, where the main title card will be displayed
        imagePanel = new JPanel();
        JLabel titleLabel = new JLabel();
        titleLabel.setOpaque(true);
        ImageIcon titleCardImage = new ImageIcon("data/images/denkititle.jpg");
        titleLabel.setIcon(titleCardImage);
        imagePanel.add(titleLabel);

        //Create the button panel, where the user will select either the newGameButton or openSaveButton
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(this);
        buttonPanel.add(newGameButton);
        openSaveButton = new JButton("Open Save");
        openSaveButton.addActionListener(this);
        buttonPanel.add(openSaveButton);

        //Add the child panels to the main panel
        titlePanel.add(imagePanel, BorderLayout.CENTER);
        titlePanel.add(buttonPanel, BorderLayout.PAGE_END);

        //Pack the frame and make it visible
        titleFrame.pack();
        titleFrame.setVisible(true);

    }


    /*
    * Methods Required by Interfaces
    */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object control = e.getSource();

        //Check source
        if(control == newGameButton)
        {
            //Dispose of the titleFrame
            titleFrame.dispose();

            //First check the number of saves created
            if(currentSave.checkNumSaves())
            {
                //User has 5 or more saves
                //Open the save select screen
                currentSave.displaySaveData();
            }
            else
            {
                //User has less than 5 saves
                //New save data can be written
                createOpeningScene();

            }

        }
        else if(control == openSaveButton)
        {
            //Dispose of the titleFrame
            titleFrame.dispose();

            //Open the save select screen
            currentSave.displaySaveData();

        }
        else if(control == nextButton && nextCounter == 0)
        {
            //Begin the queue of dialogue
            dialogueArea.setText("I am the regional researcher of this world!\nBut you can call me The Professor!");
            dialogueArea.append("\nNow, tell me...");

            nextCounter++;
        }
        else if(control == nextButton && nextCounter == 1)
        {
            dialogueArea.setText("Now, tell me about yourself...\nWhat is your name?");

            //Start the getUserInfo() method to get the user's name
            getUserInfo();

            nextCounter++;
        }
        else if(control == enterButton)
        {
            //Init main player's name
            userGriffin.myName = nameField.getText();

            //Check to make sure name is correct
            //If it is basically empty, then change it to "Trainer"
            if(userGriffin.myName.equals(" ") || userGriffin.myName.equals("") || userGriffin.myName.isEmpty())
            {
                userGriffin.myName = "Trainer";
            }

            //Write the save data
            currentSave.writeSaveData();

            //Dispose of the userPanel briefly
            openPanel.remove(userPanel);

            //Readd the dialogue panel
            openPanel.add(dialoguePanel, BorderLayout.PAGE_END);

            //Adjust output text
            dialogueArea.setText("Welcome " + userGriffin.myName + "!\nEnjoy your journey!");

            nextCounter++;
        }
        else if(control == nextButton && nextCounter == 3)
        {
            //Dispose of opening frame
            openFrame.dispose();

            //Start the game loop
            new Gameplay(true, userGriffin);
        }

    }
}
