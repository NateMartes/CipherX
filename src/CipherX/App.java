package CipherX;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * 
 * App
 * 
 * Contains All GUI methods visible to the user.
 */
public class App extends JFrame implements ActionListener, KeyListener{
    public static final Color BGCOLOR = new Color(0x757981);
    public static final String FONT = "Verdana";
    private JPasswordField firstJPasswordField;
    private TextField firstPsTextField;
    private JPasswordField secondJPasswordField;
    private TextField secondTextField;
    private TextField usernameTextField;
    private TextField tagnameTextField;
    private JButton viewButton;
    private JButton viewButton1;
    private JButton createPassButton;
    private JButton changeRootPassButton;
    private JButton LogoutButton;
    private JButton submitButton;

    private JLabel verifyLabel;
    private boolean loginScreen, mainScreen, createPassScreen;
    private ArrayList<Boolean> allScreens = new ArrayList<Boolean>();
    private ArrayList<Component> screenComponents = new ArrayList<Component>();
    private JPanel[] passwordPanels;
    private SQLConnection databaseConnection;

    App() {
        /**
         * Constructs app's JFrame and add necessities needed for JFrame
         * 
         * @param none
         * 
         * @return none
         */
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setResizable(false);
        this.setTitle("CipherX");
        this.getContentPane().setBackground(BGCOLOR);
        this.setLocationRelativeTo(null);

        //Setup allScreens
        allScreens.add(loginScreen);
        allScreens.add(mainScreen);
        allScreens.add(createPassScreen);
        loadLoginScreen();
        this.setVisible(true);
    }

    //Event Methods
    @Override
    public void actionPerformed(ActionEvent e) {
        Component c = ((Component) e.getSource());
        System.out.println(c.getName());
        switch (c.getName()) {
            case "submitButton","firstJPasswordField":

                TextField textfield = null;
                JPasswordField passwordField = null;
                for (Component component : screenComponents){
                    if (component.getName() == "firstPsTextField"){textfield = (TextField) component;}
                    if (component.getName() == "firstJPasswordField"){passwordField = (JPasswordField) component;}
                }
                runstartup(textfield, passwordField);

                break;
            case "viewButton":

                textfield = null;
                passwordField = null;
                for (Component component : screenComponents){
                    if (component.getName() == "firstPsTextField"){textfield = (TextField) component;}
                    if (component.getName() == "firstJPasswordField"){passwordField = (JPasswordField) component;}
                }
                swap(textfield, passwordField);

                break;
            case "viewButton1":

                textfield = null;
                passwordField = null;
                for (Component component : screenComponents){
                    if (component.getName() == "secondTextField"){textfield = (TextField) component;}
                    if (component.getName() == "secondJPasswordField"){passwordField  = (JPasswordField) component;}
                }
                swap(textfield, passwordField);

                break;
            case "createPassButton":

                clearFrame();
                loadCreatePassScreen();

                break;
            case "changeRootPassButton":

                // change root password (Zack)

                break;
            case "LogoutButton":

                // log out of SQL database & return to login screen (Zack) -- Complete
                try {
                    databaseConnection.exitDatabase(); // attempt to exit database
                } catch (SQLException e1) {
                    System.out.println(e1.toString()); // print error on fail
                }
                // Change screen to login screen
                clearFrame();
                loadLoginScreen();
                this.revalidate();
                this.repaint();

            default:
                System.err.println("No Case Found for this Componenet");
                break;
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    private void runstartup(TextField firstPsTextField, JPasswordField firstJPasswordField){
        /*
         * calls startup methods
         * 
         * @param none
         * @return none
         */
        if (firstPsTextField.isVisible()){
            swap(firstPsTextField, firstJPasswordField);
        }
        if (Encryption.notEmpty(firstJPasswordField)){
            try {
                //Calls SQLConnection class to connect with local MySQL database
                databaseConnection = new SQLConnection("root", String.valueOf(firstJPasswordField.getPassword()));
                clearFrame();
                loadMainScreen();
            } catch (SQLException e1) {
                if ((e1.toString()).equals("java.sql.SQLException: Access denied for user 'root'@'localhost' (using password: YES)")){
                    verifyLabel.setText("Password Incorrect");
                } else {
                    verifyLabel.setText(e1.toString());
                }
            }
        } else {
            verifyLabel.setText("Empty Password Field");
        }
    }
    //GUI Methods
    private void clearFrame(){
        /**
         * clears JFrame of all components
         * 
         * @param none
         * @return none
         */
        this.getContentPane().removeAll();
        removeAllComponents();
        removeAllComponents();
        this.revalidate();
        this.repaint();
    }
    private void setCurrentVisibleScreen(boolean visibleScreen){
        /**
         * set visibleScreen to true and all other screens false as only
         * one visible screen can be visible at a time
         * 
         * @param visibleScreen : boolean to be set true that correlates to current visible screen
         * @return none
         */
        for (int i=0; i<allScreens.size(); i++){
            if (allScreens.get(i) == visibleScreen){
                visibleScreen = true;
                continue;
            }
            Boolean screen = allScreens.get(i);
            screen = false;
        }
    }
    private void saveComponent(Component component){
        /**
         * Save component into screenComponents screenComponents
         * 
         * @param component : JComponent to be saved in screenComponents
         * @return none
         */
        screenComponents.add(component);
    }
    private void removeAllComponents(){
        /**
         * removes all components and all refercenes to those compoenents from screenCompoenents
         * 
         * @param none
         * @return none
         */
        while (screenComponents.size() > 0){
            Component currentComponent = screenComponents.get(0);
            if (currentComponent instanceof JButton){
                ((JButton) currentComponent).removeActionListener(this);
            }
            if (currentComponent instanceof JTextField){
                ((JTextField) currentComponent).removeKeyListener(this);
            }
            screenComponents.remove(currentComponent);
            currentComponent = null;
        }
    }
    private void swap(TextField textField, JPasswordField jPasswordField){
        /**
         * swap JPassword Field with TextField for a user to view password.
         * whichever Field is visible will become invisible and the other will become visible.
         * this assumes that only one of the fields is visible
         * 
         * @param jPasswordFiled : JPasswordField to be swapped with textField
         * @param textField : TextField to be swapped with jPasswordField
         * @return none
         */
        if (textField.isVisible()){
            textField.setVisible(false);
            jPasswordField.setText(textField.getText());
            jPasswordField.setVisible(true);
        } else {
            textField.setVisible(true);
            textField.setText(String.valueOf(jPasswordField.getPassword()));
            jPasswordField.setVisible(false);
        }
    }
    private void loadLoginScreen(){
        /**
         * adds GUI components to JFrame needed for login screen.
         * 
         * @param none
         * @return none
         * 
         */
        this.setLayout(new GridLayout(3,1,0,10));

        JPanel IntroPanel = new JPanel();
        JLabel IntroLabel = new JLabel("CipherX");
        IntroPanel.setLayout(null);
        IntroLabel.setBounds(300,130,400,80);
        IntroLabel.setFont(new Font(FONT,Font.BOLD, 50));
        IntroPanel.add(IntroLabel);
        IntroPanel.setBackground(BGCOLOR);
        IntroLabel.setName("IntroLabel");
        IntroPanel.setName("IntroPanel");
        saveComponent(IntroLabel);
        saveComponent(IntroPanel);
        
        JPanel passwordJPanel = new JPanel(new GridLayout(1,2, 20, 0));
        passwordJPanel.setBackground(BGCOLOR);
        passwordJPanel.setName("IntroPanel");
        saveComponent(passwordJPanel);

        JLabel firstPasswordLabel = new JLabel("Enter Root Password");
        firstPasswordLabel.setFont(new Font(FONT,Font.BOLD, 16));
        firstPasswordLabel.setHorizontalAlignment(JLabel.RIGHT);
        firstPasswordLabel.setName("firstPasswordLabel");
        saveComponent(passwordJPanel);

        firstJPasswordField = new JPasswordField();
        firstJPasswordField.setFont(new Font(FONT,Font.BOLD, 16));
        firstJPasswordField.addKeyListener(this);
        firstJPasswordField.addActionListener(this);
        firstJPasswordField.setBounds(10,103,150,40);
        firstJPasswordField.setName("firstJPasswordField");
        saveComponent(firstJPasswordField);

        firstPsTextField = new TextField();
        firstPsTextField.setFont(new Font(FONT,Font.BOLD,22));
        firstPsTextField.addKeyListener(this);
        firstPsTextField.addActionListener(this);
        firstPsTextField.setBounds(10,103,150,40);
        firstPsTextField.setVisible(false);
        firstPsTextField.setName("firstPsTextField");
        saveComponent(firstPsTextField);

        viewButton = new JButton();
        viewButton.setText("View");
        viewButton.setFocusable(false);
        viewButton.setBounds(165, 103, 80, 40);
        viewButton.setBounds(165, 103, 80, 40);
        viewButton.setFont((new Font(FONT,Font.BOLD, 16)));
        viewButton.addActionListener(this);
        viewButton.setName("viewButton");
        saveComponent(viewButton);
        
        JPanel firstPasswordFieldPanel = new JPanel();
        firstPasswordFieldPanel.setLayout(null);
        firstPasswordFieldPanel.setBackground(BGCOLOR);
        firstPasswordFieldPanel.add(firstJPasswordField);
        firstPasswordFieldPanel.add(firstPsTextField);
        firstPasswordFieldPanel.add(viewButton);
        firstPasswordFieldPanel.setName("firstPasswordFieldPanel");
        saveComponent(firstPasswordFieldPanel);

        JPanel submitPanel = new JPanel();
        submitPanel.setBackground(BGCOLOR);
        submitPanel.setLayout(null);
        submitPanel.setName("submitPanel");
        saveComponent(submitPanel);

        verifyLabel = new JLabel();
        verifyLabel.setBounds(250,25,300,50);
        verifyLabel.setFont(new Font(FONT,Font.BOLD, 16));
        verifyLabel.setHorizontalAlignment(JLabel.CENTER);
        verifyLabel.setName("verifyLabel");
        saveComponent(verifyLabel);

        submitButton = new JButton("Enter");
        submitButton.setFocusable(false);
        submitButton.setFont(new Font(FONT,Font.BOLD, 16));
        submitButton.addActionListener(this);
        submitButton.setBounds(350,75,100,50);
        submitButton.setName("submitButton");
        saveComponent(submitButton);

        passwordJPanel.add(firstPasswordLabel);
        passwordJPanel.add(firstPasswordFieldPanel);

        submitPanel.add(submitButton);
        submitPanel.add(verifyLabel);

        this.add(IntroPanel);
        this.add(passwordJPanel);
        this.add(submitPanel);

        setCurrentVisibleScreen(loginScreen);

    }
    private void loadMainScreen(){
        /**
         * adds GUI comopenets needed for main screen.
         * 
         * @param none
         * @return none
         */
        this.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BGCOLOR);
        buttonPanel.setLayout(null);
        buttonPanel.setPreferredSize(new Dimension(800,100));
        buttonPanel.setName("buttonPanel");
        saveComponent(buttonPanel);

        createPassButton = new JButton("Create New Password");
        createPassButton.setBounds(10,10,170,50);
        createPassButton.setFocusable(false);
        createPassButton.addActionListener(this);
        createPassButton.setName("createPassButton");
        saveComponent(createPassButton);

        changeRootPassButton = new JButton("Change Root Password");
        changeRootPassButton.setBounds(430,10,170,50);
        changeRootPassButton.setFocusable(false);
        changeRootPassButton.addActionListener(this);
        changeRootPassButton.setName("changeRootPassButton");
        saveComponent(changeRootPassButton);

        LogoutButton = new JButton("Logout");
        LogoutButton.setBounds(610,10,160,50);
        LogoutButton.setFocusable(false);
        LogoutButton.addActionListener(this);
        LogoutButton.setName("LogoutButton");
        saveComponent(LogoutButton);

        buttonPanel.add(createPassButton);
        buttonPanel.add(changeRootPassButton);
        buttonPanel.add(LogoutButton);

        this.add(buttonPanel, BorderLayout.NORTH);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setBorder(BorderFactory.createLineBorder(BGCOLOR, 0));
        passwordPanel.setBackground(BGCOLOR);
        passwordPanel.setName("passwordPanel");
        saveComponent(passwordPanel);

        loadPasswords(passwordPanel);
        
        this.add(new JScrollPane(passwordPanel), BorderLayout.CENTER);
        this.setVisible(true);

        setCurrentVisibleScreen(mainScreen);

        setCurrentVisibleScreen(mainScreen);
         
    }
    private void loadPasswords(JPanel ALLpasswordPanel){
        /**
         * adds JPanels contanting components tied to user entered passwords to ALLpasswordPanel.
         * 
         * @param ALLpasswordPanel : main password JPanel where all sub JPanels will be added to.
         * @return none
         */
        try {
            this.passwordPanels = new JPanel[databaseConnection.getRowCount()/* Change this to some number to see passowrd panels*/];
        } catch (SQLException e){
            System.out.println(e);
        }
        for (int i=0; i<passwordPanels.length; i++){
            //Create JPanel and add components
            JPanel passwordPanel = new JPanel();
            passwordPanel.setBackground(BGCOLOR);

            JLabel passwordTagLabel = new JLabel("No tag_name found");
            try {
                passwordTagLabel.setText(databaseConnection.getColumnData("tag_name", i+1)+"   ");
            } catch (SQLException e){
                System.out.println(e);
            }
            passwordTagLabel.setFont(new Font(FONT, Font.PLAIN, 22));

            JLabel usernameLabel = new JLabel("No username found");
            try {
                usernameLabel.setText(databaseConnection.getColumnData("username", i+1));
            } catch (SQLException e) {
                System.out.println(e);
            }
            usernameLabel.setFont(new Font(FONT, Font.PLAIN, 22));

            JPasswordField passwordPasswordField = new JPasswordField("No password found");
            try {
                passwordPasswordField.setText(databaseConnection.getColumnData("password", i+1)+"   ");
            } catch (SQLException e){
                System.out.println(e);
            }
            passwordPasswordField.setFont(new Font(FONT, Font.PLAIN, 22));
            passwordPasswordField.setEditable(false);

            int buttonW = 45;
            int buttonH = 45;
            Border empty = BorderFactory.createEmptyBorder();

            JButton copyButton = new JButton();
            copyButton.setFocusable(false);
            // copyButton.setFont((new Font(FONT,Font.BOLD, 18)));
            try {
                Image copyImg = ImageIO.read(getClass().getResource("copy.png")); // edit as image icons become available
                copyButton.setIcon(new ImageIcon(copyImg));
            } catch (Exception e) {
                System.out.println(e);
            }
            copyButton.setPreferredSize(new Dimension(buttonW, buttonH));
            copyButton.setBorder(empty);
            copyButton.addActionListener(this);
            
            JButton viewButton = new JButton(); // new JButton("View");
            viewButton.setFocusable(false);
            try {
                Image viewImg = ImageIO.read(getClass().getResource("view.png")); // edit as image icons become available
                viewButton.setIcon(new ImageIcon(viewImg));
            } catch (Exception e) {
                System.out.println(e);
            }
            // viewButton.setFont((new Font(FONT,Font.BOLD, 18)));
            viewButton.setPreferredSize(new Dimension(buttonW, buttonH));
            viewButton.setBorder(empty);
            try {
                Image viewImg = ImageIO.read(getClass().getResource("view.png")); // edit as image icons become available
                viewButton.setIcon(new ImageIcon(viewImg));
            } catch (Exception e) {
                System.out.println(e);
            }
            // viewButton.setFont((new Font(FONT,Font.BOLD, 18)));
            viewButton.setPreferredSize(new Dimension(buttonW, buttonH));
            viewButton.setBorder(empty);
            viewButton.addActionListener(this);

            JButton editButton = new JButton(); // new JButton("Edit");
            editButton.setFocusable(false);
            try {
                Image editImg = ImageIO.read(getClass().getResource("edit.png")); // edit as image icons become available
                editButton.setIcon(new ImageIcon(editImg));
            } catch (Exception e) {
                System.out.println(e);
            }
            // editButton.setFont((new Font(FONT,Font.BOLD, 18)));
            editButton.setPreferredSize(new Dimension(buttonW, buttonH));
            editButton.setBorder(empty);
            try {
                Image editImg = ImageIO.read(getClass().getResource("edit.png")); // edit as image icons become available
                editButton.setIcon(new ImageIcon(editImg));
            } catch (Exception e) {
                System.out.println(e);
            }
            // editButton.setFont((new Font(FONT,Font.BOLD, 18)));
            editButton.setPreferredSize(new Dimension(buttonW, buttonH));
            editButton.setBorder(empty);
            editButton.addActionListener(this);

            JButton removeButton = new JButton(); // new JButton("X");
            removeButton.setFocusable(false);
            try {
                Image delImg = ImageIO.read(getClass().getResource("del.png")); // edit as image icons become available
                removeButton.setIcon(new ImageIcon(delImg));
            } catch (Exception e) {
                System.out.println(e);
            }
            // removeButton.setFont((new Font(FONT,Font.BOLD, 18)));
            removeButton.setPreferredSize(new Dimension(buttonW, buttonH));
            removeButton.setBorder(empty);
            try {
                Image delImg = ImageIO.read(getClass().getResource("del.png")); // edit as image icons become available
                removeButton.setIcon(new ImageIcon(delImg));
            } catch (Exception e) {
                System.out.println(e);
            }
            // removeButton.setFont((new Font(FONT,Font.BOLD, 18)));
            removeButton.setPreferredSize(new Dimension(buttonW, buttonH));
            removeButton.setBorder(empty);
            removeButton.addActionListener(this);

            passwordPanel.add(Box.createRigidArea(new Dimension(0, 75)));

            passwordPanel.add(passwordTagLabel);
            passwordPanel.add(usernameLabel);
            passwordPanel.add(usernameLabel);
            passwordPanel.add(passwordPasswordField);
            passwordPanel.add(copyButton);
            passwordPanel.add(copyButton);
            passwordPanel.add(viewButton);
            passwordPanel.add(editButton);
            passwordPanel.add(removeButton);
            ALLpasswordPanel.add(passwordPanel);

            passwordPanels[i] = passwordPanel;
            //passwordPanels.getComponents() returns :
            //{Box$Filler, JLabel, JPasswordField, JButton (view), JButton (edit), JButton (remove)}
            
            //Getting Components:
            //JLabel component = (JLabel) passwordPanels[0].getComponents()[1];
        }
    }
    private void loadCreatePassScreen(){
        /**
         * loads create password screen and all needed components
         * 
         * @param none
         * @return none
         */
        
        this.setLayout(new GridLayout(0,2,0,0));

        JPanel IntroPanel = new JPanel();
        JLabel IntroLabel = new JLabel("Create Password");
        IntroPanel.setLayout(null);
        IntroLabel.setBounds(20,5,300,60);
        IntroLabel.setFont(new Font(FONT,Font.BOLD, 30));
        IntroPanel.add(IntroLabel);
        IntroPanel.setBackground(BGCOLOR);
        IntroPanel.setName("IntroPanel");
        IntroLabel.setName("IntroLabel");
        saveComponent(IntroPanel);
        saveComponent(IntroLabel);

        JPanel BestPracticePanel = new JPanel();
        JTextArea InfoTextArea = new JTextArea();
        BestPracticePanel.setLayout(null);
        InfoTextArea.setBounds(20,100,200,200);
        InfoTextArea.setSize(400,300);
        InfoTextArea.setText("""
            Best Practices for a Secure Password:


                * 12-16 Characters in Length

                * A Mix of Uppercase, Numbers, 
                  and Lowercase letters

                * Multiple Symbols (!, @, etc.)

                * Don't Reuse Passwords

                * Avoid Personal Information
                  (Birthday, Names, Usernames, etc.)

                * Avoid Using Complete Words

                """);
        InfoTextArea.setBackground(BGCOLOR);
        InfoTextArea.setFont(new Font(FONT,Font.BOLD, 16));
        InfoTextArea.setEditable(false);
        IntroLabel.setName("BestPracticePanel");
        saveComponent(BestPracticePanel);
        InfoTextArea.setName("InfoTextArea");
        saveComponent(InfoTextArea);


        JButton generatePassButton = new JButton("Use Strong Password");
        generatePassButton.addActionListener(this);
        generatePassButton.setFont(new Font(FONT,Font.BOLD, 16));
        generatePassButton.setFocusable(false);
        generatePassButton.setBounds(70, 450, 250, 50);
        generatePassButton.setName("generatePassButton");
        saveComponent(generatePassButton);

        IntroPanel.add(InfoTextArea);
        IntroPanel.add(generatePassButton);
    
        JPanel passwordJPanel = new JPanel();
        passwordJPanel.setLayout(new BoxLayout(passwordJPanel, BoxLayout.Y_AXIS));
        passwordJPanel.setBorder(BorderFactory.createLineBorder(BGCOLOR, 0));
        passwordJPanel.setBackground(BGCOLOR);
        passwordJPanel.setName("passwordJPanel");
        saveComponent(passwordJPanel);

        JLabel tagnameLabel = new JLabel("Password Name");
        tagnameLabel.setFont(new Font(FONT, Font.BOLD, 16));
        tagnameLabel.setBounds(0,17,150,30);
        tagnameLabel.setHorizontalAlignment(JLabel.LEFT);
        tagnameLabel.setName("tagnameLabel");
        saveComponent(tagnameLabel);

        tagnameTextField = new TextField();
        tagnameTextField.setFont(new Font(FONT,Font.BOLD, 16));
        tagnameTextField.addActionListener(this);
        tagnameTextField.setBounds(150,17,150,30);
        tagnameTextField.setName("tagnameTextField");
        saveComponent(tagnameTextField);

        JPanel tagnameJPanel = new JPanel();
        tagnameJPanel.setLayout(null);
        tagnameJPanel.setBackground(BGCOLOR);
        tagnameJPanel.add(tagnameLabel);
        tagnameJPanel.add(tagnameTextField);
        tagnameJPanel.setName("tagnameJPanel");
        saveComponent(tagnameJPanel);

        JLabel usernameLabel = new JLabel("User/Email");
        usernameLabel.setFont(new Font(FONT, Font.BOLD, 16));
        usernameLabel.setBounds(0,17,150,30);
        usernameLabel.setHorizontalAlignment(JLabel.LEFT);
        usernameLabel.setName("usernameLabel");
        saveComponent(usernameLabel);

        usernameTextField = new TextField();
        usernameTextField.setFont(new Font(FONT,Font.BOLD, 16));
        usernameTextField.addActionListener(this);
        usernameTextField.setBounds(150,17,150,30);
        usernameTextField.setName("usernameTextField");
        saveComponent(usernameTextField);

        JPanel usernameJPanel = new JPanel();
        usernameJPanel.setLayout(null);
        usernameJPanel.setBackground(BGCOLOR);
        usernameJPanel.add(usernameLabel);
        usernameJPanel.add(usernameTextField);
        usernameJPanel.setName("usernameJPanel");
        saveComponent(usernameJPanel);

        JLabel firstPasswordLabel = new JLabel("Password");
        firstPasswordLabel.setFont(new Font(FONT,Font.BOLD, 16));
        firstPasswordLabel.setBounds(0,17,150,30);
        firstPasswordLabel.setHorizontalAlignment(JLabel.LEFT);
        firstPasswordLabel.setName("firstPasswordLabel");
        saveComponent(firstPasswordLabel);

        firstJPasswordField = new JPasswordField();
        firstJPasswordField.setFont(new Font(FONT,Font.BOLD, 16));
        firstJPasswordField.addKeyListener(this);
        firstJPasswordField.addActionListener(this);
        firstJPasswordField.setBounds(150,17,150,30);
        firstJPasswordField.setName("firstJPasswordField");
        saveComponent(firstJPasswordField);

        firstPsTextField = new TextField();
        firstPsTextField.setFont(new Font(FONT,Font.BOLD,22));
        firstPsTextField.addKeyListener(this);
        firstPsTextField.addActionListener(this);
        firstPsTextField.setBounds(150,17,150,30);
        firstPsTextField.setVisible(false);
        firstPsTextField.setName("firstPsTextField");
        saveComponent(firstPsTextField);

        viewButton = new JButton();
        viewButton.setText("View");
        viewButton.setFocusable(false);
        viewButton.setBounds(305, 13, 80, 35);
        viewButton.setFont((new Font(FONT,Font.BOLD, 16)));
        viewButton.addActionListener(this);
        viewButton.setName("viewButton");
        saveComponent(viewButton);
        
        JPanel firstPasswordFieldPanel = new JPanel();
        firstPasswordFieldPanel.setLayout(null);
        firstPasswordFieldPanel.setBackground(BGCOLOR);
        firstPasswordFieldPanel.add(firstPasswordLabel);
        firstPasswordFieldPanel.add(firstJPasswordField);
        firstPasswordFieldPanel.add(firstPsTextField);
        firstPasswordFieldPanel.add(viewButton);
        firstPasswordFieldPanel.setName("firstPasswordFieldPanel");
        saveComponent(firstPasswordFieldPanel);

        JLabel secondPasswordLabel = new JLabel("Verify Password");
        secondPasswordLabel.setFont(new Font(FONT,Font.BOLD, 16));
        secondPasswordLabel.setBounds(0,17,150,30);
        secondPasswordLabel.setHorizontalAlignment(JLabel.LEFT);
        secondPasswordLabel.setName("secondPasswordLabel");
        saveComponent(secondPasswordLabel);

        secondJPasswordField = new JPasswordField();
        secondJPasswordField.setFont(new Font(FONT,Font.BOLD, 16));
        secondJPasswordField.addKeyListener(this);
        secondJPasswordField.addActionListener(this);
        secondJPasswordField.setBounds(150,17,150,30);
        secondJPasswordField.setName("secondJPasswordField");
        saveComponent(secondJPasswordField);

        secondTextField = new TextField();
        secondTextField.setFont(new Font(FONT,Font.BOLD, 22));
        secondTextField.addKeyListener(this);
        secondTextField.addActionListener(this);
        secondTextField.setBounds(150,17,150,30);
        secondTextField.setVisible(false);
        secondTextField.setName("secondTextField");
        saveComponent(secondTextField);

        viewButton1 = new JButton();
        viewButton1.setText("View");
        viewButton1.setFocusable(false);
        viewButton1.setBounds(305, 13, 80, 35);
        viewButton1.setFont((new Font(FONT,Font.BOLD, 16)));
        viewButton1.addActionListener(this);
        viewButton1.setName("viewButton1");
        saveComponent(viewButton1);

        JPanel secondPasswordFieldPanel = new JPanel();
        secondPasswordFieldPanel.setLayout(null);
        secondPasswordFieldPanel.setBackground(BGCOLOR);
        secondPasswordFieldPanel.add(secondPasswordLabel);
        secondPasswordFieldPanel.add(secondJPasswordField);
        secondPasswordFieldPanel.add(secondTextField);
        secondPasswordFieldPanel.add(viewButton1);
        secondPasswordFieldPanel.setName("secondPasswordFieldPanel");
        saveComponent(secondPasswordFieldPanel);

        submitButton = new JButton("Enter");
        submitButton.setFocusable(false);
        submitButton.setFont(new Font(FONT,Font.BOLD, 16));
        submitButton.addActionListener(this);
        submitButton.setBounds(170,0,100,30);
        submitButton.setName("submitButton");
        saveComponent(submitButton);

        verifyLabel = new JLabel();
        verifyLabel.setBounds(135,50,200,20);
        verifyLabel.setFont(new Font(FONT,Font.BOLD, 16));
        verifyLabel.setName("verifyLabel");
        saveComponent(verifyLabel);

        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(null);
        submitPanel.setBackground(BGCOLOR);
        submitPanel.add(submitButton);
        submitPanel.add(verifyLabel);
        submitPanel.setName("submitPanel");
        saveComponent(submitPanel);

        JLabel spacer = new JLabel("");
        spacer.setPreferredSize(new Dimension(100,106));

        JLabel spacer2 = new JLabel("");
        spacer2.setPreferredSize(new Dimension(100,250));

        passwordJPanel.add(spacer);
        passwordJPanel.add(tagnameJPanel);
        passwordJPanel.add(usernameJPanel);
        passwordJPanel.add(firstPasswordFieldPanel);
        passwordJPanel.add(secondPasswordFieldPanel);
        passwordJPanel.add(submitPanel);
        passwordJPanel.add(spacer2);

        this.add(IntroPanel);
        this.add(passwordJPanel);
        
        this.setVisible(true);

        setCurrentVisibleScreen(createPassScreen);
    }

}
