package CipherX;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
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
    private JLabel verifyLabel;
    private Boolean loginScreen = false , mainScreen = false, createPassScreen = false, changeRootScreen = false, editScreen = false;
    private ArrayList<Component> screenComponents = new ArrayList<Component>();
    private JLabel strengthLabel;
    private JPanel[] passwordPanels;
    private SQLConnection databaseConnection;
    private int currentPasswordPanelIndex;

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
        loadLoginScreen();
        Encryption.setupAsciiTable();
        this.setVisible(true);
    }

    //Event Methods
    @Override
    public void actionPerformed(ActionEvent e) {
        Component c = ((Component) e.getSource());
        JTextField textfield = null;
        JPasswordField passwordField = null;
        
        System.out.println(c.getName());
        switch (c.getName()) {
        case "submitButton","firstJPasswordField","firstPsTextField":
            if (loginScreen) {
                textfield = null;
                passwordField = null;
                for (Component component : screenComponents){
                    if (component.getName() == "firstPsTextField") textfield = (JTextField) component;
                    if (component.getName() == "firstJPasswordField") passwordField = (JPasswordField) component;
                }
                runstartup(textfield, passwordField);

            } else if (createPassScreen){
                JTextField textfield2 = null;
                JPasswordField passwordField2 = null;
                for (Component component : screenComponents){
                    if (component.getName() == "secondTextField") textfield2 = (JTextField) component;
                    if (component.getName() == "secondJPasswordField") passwordField2 = (JPasswordField) component;
                }
                if (c.getName() == "submitButton") validateInputOnCreatePassScreen(0);
                if (c.getName() == "firstJPasswordField" || c.getName() == "firstJPsTextField") getVisibleComponent(textfield2, passwordField2).requestFocus();

            } else if (changeRootScreen){
                JTextField textfield2 = null;
                JPasswordField passwordField2 = null;
                for (Component component : screenComponents){
                    if (component.getName() == "secondTextField") textfield2 = (JTextField) component;
                    if (component.getName() == "secondJPasswordField") passwordField2 = (JPasswordField) component;
                }
                if (c.getName() == "submitButton") validateInputOnChangeRootScreen();
                if (c.getName() == "firstJPasswordField" || c.getName() == "firstPsTextField") getVisibleComponent(textfield2, passwordField2).requestFocus();
            } else if (editScreen){
                JTextField textfield2 = null;
                JPasswordField passwordField2 = null;
                for (Component component : screenComponents){
                    if (component.getName() == "secondTextField") textfield2 = (JTextField) component;
                    if (component.getName() == "secondJPasswordField") passwordField2 = (JPasswordField) component;
                }
                if (c.getName() == "submitButton") validateInputOnCreatePassScreen(currentPasswordPanelIndex);
                if (c.getName() == "firstJPasswordField" || c.getName() == "firstJPsTextField") getVisibleComponent(textfield2, passwordField2).requestFocus();
            }
            break;

            case "viewButton":
                if (mainScreen) {
                    int count = 0;
                    for (JPanel panel : passwordPanels) {
                        if (panel.getName().equals(c.getParent().getName())) {
                            break;
                        }
                        count ++;
                    }
                    
                    for (Component component : c.getParent().getComponents()){
                        if (component.getName() == "passwordTextField") {
                            try {
                                if (((JTextField) component).getText().equals(Encryption.getDecryptedPassword(databaseConnection.getColumnData("password", count+1), databaseConnection.getColumnData("passKey", count+1)))) {
                                    ((JTextField) component).setText("••••••");
                                }
                                else {
                                    ((JTextField) component).setText(Encryption.getDecryptedPassword(databaseConnection.getColumnData("password", count +1), databaseConnection.getColumnData("passKey", count+1)));
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
                else {
                    textfield = null;
                    passwordField = null;
                    for (Component component : screenComponents){
                        if (component.getName() == "firstPsTextField") textfield = (JTextField) component;
                        if (component.getName() == "firstJPasswordField") passwordField = (JPasswordField) component;
                    }
                    swap(textfield, passwordField);
                }
                break;

            case "viewButton1":

                textfield = null;
                passwordField = null;
                for (Component component : screenComponents){
                    if (component.getName() == "secondTextField") textfield = (JTextField) component;
                    if (component.getName() == "secondJPasswordField") passwordField  = (JPasswordField) component;
                }
                swap(textfield, passwordField);
                break;

            case "createPassButton":

                clearFrame();
                loadPassScreen("Create Password", 0);
                createPassScreen = true;
                break;

            case "editButton":
                // edit password (Zack)

                clearFrame();
                editScreen = true;

                int count = 1;
                for (JPanel panel : passwordPanels) {
                    if (panel.getName().equals(c.getParent().getName())) {
                        break;
                    }
                    count ++;
                }
                currentPasswordPanelIndex = count;

                loadPassScreen("Edit Password", currentPasswordPanelIndex);
                
                
                break;

            case "changeRootPassButton":
                
                clearFrame();
                loadSubScreen("Change Vault Password");
                changeRootScreen = true; /* change root password screen is now visible */
                
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
                break;

            case "goBackButton":

                clearFrame();
                try {
                    loadMainScreen();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;

            case "secondTextField","secondJPasswordField":
                if (createPassScreen){
                    validateInputOnCreatePassScreen(0);
                } else if (changeRootScreen){
                    validateInputOnChangeRootScreen();
                }
                break;

            case "tagnameTextField":

                for (Component component : screenComponents){
                    if (component.getName() == "usernameTextField") component.requestFocus();
                }
                break;

            case "usernameTextField":

                textfield = null;
                passwordField = null;
                for (Component component : screenComponents){
                    if (component.getName() == "firstPsTextField") textfield = (JTextField)component;
                    if (component.getName() == "firstJPasswordField") passwordField = (JPasswordField)component;
                }
                getVisibleComponent(textfield, passwordField).requestFocus();
                break;
            case "generatePassButton":

                JPasswordField passwordField1 = null;
                JTextField textfield1 = null;
                JPasswordField passwordField2 = null;
                JTextField textfield2 = null;
                for (Component component : screenComponents){
                    if (component.getName() == "firstJPasswordField") passwordField1 = (JPasswordField) component;
                    if (component.getName() == "secondJPasswordField") passwordField2 = (JPasswordField) component;
                    if (component.getName() == "firstPsTextField") textfield1 = (JTextField) component;
                    if (component.getName() == "secondTextField") textfield2 = (JTextField) component;
                }
                if (!passwordField1.isVisible()){
                    swap(textfield1, passwordField1);
                }
                if (!passwordField2.isVisible()){
                    swap(textfield2, passwordField2);
                }
                Encryption.setStrongPassword(passwordField1, passwordField2);
                changePasswordStrength(31);
                break;

            case "copyUsrButton":
                // Copy password (Zack)
                int count1 = 0;
                for (JPanel panel : passwordPanels) {
                    if (panel.getName().equals(c.getParent().getName())) {
                        break;
                    }
                    count1 ++;
                }
                try {
                    StringSelection usernamestring = new StringSelection(databaseConnection.getColumnData("username", count1+1));
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(usernamestring, null);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                System.out.println("copyPassButton Recieved");
                break;

            case "copyPassButton":
                // Copy user (Zack)
                int count2 = 0;
                for (JPanel panel : passwordPanels) {
                    if (panel.getName().equals(c.getParent().getName())) {
                        break;
                    }
                    count2 ++;
                }
                try {
                    StringSelection passwordstring = new StringSelection(Encryption.getDecryptedPassword(databaseConnection.getColumnData("password", count2+1), databaseConnection.getColumnData("passKey", count2+1)));
                    System.out.println(passwordstring.toString());
                    Clipboard clipboard1 = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard1.setContents(passwordstring, null);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
                System.out.println("copyUsrButton Recieved");
                break;

            case "exportButton":
                String[][] records = databaseConnection.getRowsAsRecords();
                new FileScreen(this, records);
                break;

            case "importButton":
                FileScreen fileScreen = new FileScreen(this, null);
                int status = fileScreen.getReturnStatus();
                records = fileScreen.getRecords();

                if (status != 0){
                    FileScreen.alertBox(status,this);
                    break;
                }

                for (int i=0; i<records.length; i++){
                    try {
                        if (databaseConnection.isInDatabase(records[i][0])){ /*check if tag name is in the database */
                            continue;
                        }
                        saveData(records[i][0],records[i][1],records[i][2],records[i][3]);
                    } catch (SQLException e1){
                        e1.printStackTrace();
                    }
                }
                clearFrame();
                try {
                    loadMainScreen();
                } catch (IOException e1){
                    e1.printStackTrace();
                }

                break;

            case "removeButton":
                // drop password
                String tag_name = c.getParent().getName();
                System.out.println("'" + tag_name + "'");
                try {
                    databaseConnection.dropRow(tag_name);
                }
                catch (SQLException err) {  
                    System.err.println(err);
                }

                clearFrame();
                try {
                    loadMainScreen();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                System.out.println("removeButton recieved");
                break;

            default:
                
                System.err.println("No Case Found for this Componenet");
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {
        Component component = e.getComponent();
        String componentName = component.getName();
        switch (componentName) {
            case "firstJPasswordField", "firstPsTextField":
                if (createPassScreen || changeRootScreen){
                    String text = "";
                    if (componentName == "firstJPasswordField") {
                        text = String.valueOf(((JPasswordField)component).getPassword()); 
                    } else {
                        text = ((JTextField)component).getText();
                    }
                    changePasswordStrength(Encryption.checkPasswordRequirments(text));
                }
                break;
            default:
                break;
        }
    }
    private void runstartup(JTextField firstPsTextField, JPasswordField firstJPasswordField){
        /*
         * calls startup methods
         * 
         * @param firstPsTextField : JTextField which is in loginScreen
         * @param firstJPasswordField : JPasswordField which is in loginScreen
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
                try {
                    loadMainScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    private void changePasswordStrength(int strength){
        /**
         * adjusts password strength label's color to represent the strength of password
         * to the client
         * 
         * @param strength : int strength of password represented as an int
         * 
         * @return : none
         */
        int rgbGreenVal = 0;
        int rgbRedVal = 255;
        int Gincrement = 50;
        int Rincrement = 40;

        int[] masks = {1,2,4,8,16};
        for (int mask : masks){
            if ((strength & mask) == mask){rgbGreenVal+=Gincrement; rgbRedVal -= Rincrement;} 
        }

        rgbGreenVal = Math.min(Math.max(rgbGreenVal, 0), 255);
        rgbRedVal = Math.min(Math.max(rgbRedVal, 0), 255);

        strengthLabel.setBackground(new Color(rgbRedVal, rgbGreenVal, 0));

    }
    private void clearFrame(){
        /**
         * clears JFrame of all components
         * 
         * @param none
         * @return none
         */
        this.getContentPane().removeAll();
        removeAllComponents();
        resetVisibleScreen();
        this.revalidate();
        this.repaint();
    }
    private void resetVisibleScreen(){
        /**
         * resets all screens
         * 
         * @param none 
         * @return none
         */
        loginScreen = false;
        mainScreen = false;
        createPassScreen = false;
        changeRootScreen = false;
        editScreen = false;
    }
    private Component getVisibleComponent(Component a, Component b){
        /**
         * checks which componenet is visible
         * 
         * @param a : Componenet that may be visible;
         * @param b : Componenet that may be visible;
         * @return : the visible component unless both are invisible
         */
        if (a.isVisible()){
            return a;
        } else if (b.isVisible()) {
            return b;
        } else {
            return null;
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
            if (currentComponent instanceof JTextField || ((currentComponent instanceof JTextField) && (currentComponent instanceof JPasswordField))){
                ((JTextField) currentComponent).removeKeyListener(this);
            }
            screenComponents.remove(currentComponent);
            currentComponent = null;
        }
    }
    private void swap(JTextField textField, JPasswordField jPasswordField){
        /**
         * swap JPassword Field with JTextField for a user to view password.
         * whichever Field is visible will become invisible and the other will become visible.
         * this assumes that only one of the fields is visible
         * 
         * @param jPasswordFiled : JPasswordField to be swapped with textField
         * @param JTextField : JTextField to be swapped with jPasswordField
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
    private JButton createButton(String name, String text, int fontSize, int x, int y, int width, int height){
        /**
         * Creates JButton with all reqiured methods
         * 
         * @param name : String name of the button
         * @param text : String text in the Button
         * @param fontSize : int font size of text in button. If fontsize is -1, use normal fontsize
         * @param x : Int if container of button has a null layout, this is the x starting postion
         * @param y : Int if container of button has a null layout, this is the y starting postion
         * @param width : Int if container of button has a null layout, this is the width
         * @param height : Int if container of button has a null layout, this is the height
         * 
         * @return newButton : JButton that was created
         */
        JButton newButton = new JButton(text);
        newButton.addActionListener(this);
        newButton.setFocusable(false);
        if (fontSize != -1){
            newButton.setFont(new Font(FONT, Font.BOLD, fontSize));
        }
        newButton.setBounds(x,y,width,height);
        newButton.setName(name);
        saveComponent(newButton);
        return newButton;
    }
    private JButton createButton(String name, Image img, int width, int height){
        /**
         * Creates JButton with all reqiured methods
         * 
         * @param name : String name of the button
         * @param img : Image object that is used in button
         * @param x : Int if container of button has a null layout, this is the x starting postion
         * @param y : Int if container of button has a null layout, this is the y starting postion
         * @param width : Int if container of button has a null layout, this is the width
         * @param height : Int if container of button has a null layout, this is the height
         * 
         * @return newButton : JButton that was created
         */
        JButton newButton = new JButton();

        Border empty = BorderFactory.createEmptyBorder();
        newButton.addActionListener(this);
        newButton.setFocusable(false);

        try {
            newButton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println(e);
        }
        newButton.setPreferredSize(new Dimension(width, height));
        newButton.setBorder(empty);
        newButton.setName(name);
        saveComponent(newButton);
        return newButton;
    }
    private JLabel createLabel(String name, String text, int fontSize, int x, int y, int width, int height){
        /**
         * Creates JLabel with all reqiured methods
         * 
         * @param name : String name of the Label
         * @param text : String text in the Label
         * @param fontSize : Int size of text in Label
         * @param x : Int if container of Label has a null layout, this is the x starting postion
         * @param y : Int if container of Label has a null layout, this is the y starting postion
         * @param width : Int if container of Label has a null layout, this is the width
         * @param height : Int if container of Label has a null layout, this is the height
         * 
         * @return newButton : JLabel that was created
         */
        JLabel newLabel = new JLabel(text);
        newLabel.setFocusable(false);
        newLabel.setFont(new Font(FONT, Font.BOLD, fontSize));
        newLabel.setBounds(x,y,width,height);
        newLabel.setName(name);
        saveComponent(newLabel);
        return newLabel;
    }
    private JTextField createTextField(String name, int fontSize, int x, int y, int width, int height){
        /**
         * Creates JTextField with all reqiured methods
         * JTextFields are set invisable at first as they are 
         * meant to be paried with JPasswordFields
         * 
         * @param name : String name of the JTextField
         * @param fontSize : Int size of text in JTextField
         * @param x : Int if container of JTextField has a null layout, this is the x starting postion
         * @param y : Int if container of JTextField has a null layout, this is the y starting postion
         * @param width : Int if container of JTextField has a null layout, this is the width
         * @param height : Int if container of JTextField has a null layout, this is the height
         * 
         * @return newTextField : TextField that was created
         */
        JTextField newTextField = new JTextField();
        newTextField.setFont(new Font(FONT,Font.BOLD, fontSize));
        newTextField.addKeyListener(this);
        newTextField.addActionListener(this);
        newTextField.setBounds(x,y,width,height);
        newTextField.setVisible(false);
        newTextField.setName(name);
        saveComponent(newTextField);
        return newTextField;
    }
    private JPasswordField createJPasswordField(String name, int fontSize, int x, int y, int width, int height){
        /**
         * Creates JPasswordField with all reqiured methods
         * JPasswordFields are set visable at first as they are 
         * meant to be paried with JPasswordFields
         * 
         * @param name : String name of the JPasswordField
         * @param fontSize : Int size of text in JPasswordField
         * @param x : Int if container of JPasswordField has a null layout, this is the x starting postion
         * @param y : Int if container of JPasswordField has a null layout, this is the y starting postion
         * @param width : Int if container of JPasswordField has a null layout, this is the width
         * @param height : Int if container of JPasswordField has a null layout, this is the height
         * 
         * @return newJPasswordField : JPasswordField that was created
         */
        JPasswordField newJPasswordField = new JPasswordField();
        newJPasswordField.setFont(new Font(FONT,Font.BOLD, fontSize));
        newJPasswordField.addKeyListener(this);
        newJPasswordField.addActionListener(this);
        newJPasswordField.setBounds(x,y,width,height);
        newJPasswordField.setVisible(true);
        newJPasswordField.setName(name);
        saveComponent(newJPasswordField);
        return newJPasswordField;
    }
    private void validateInputOnCreatePassScreen(int edit_field) {
        // TODO: update to support edit screen
        /**
         * Validates all textfield and JPasswrod Field inputs and if correct
         * Saves data and returns to main screen
         * 
         * @param edit_field : 0 if creating a new password, any other positive
         * integer for replacing the frame at that index
         * @return none
         */
        //Get Compoenets
        
        JTextField tagNameTextField = null;
        JTextField usernameTextField = null;
        JPasswordField password1JPasswordField= null;
        JTextField password1TextField = null;
        JPasswordField password2JPasswordField= null;
        JTextField password2TextField = null;
        
        for (Component component : screenComponents ){
            if (component.getName() == "tagnameTextField") tagNameTextField = (JTextField) component;
            if (component.getName() == "usernameTextField") usernameTextField = (JTextField) component;
            if (component.getName() == "firstJPasswordField") password1JPasswordField = (JPasswordField) component;
            if (component.getName() == "firstPsTextField") password1TextField = (JTextField) component;
            if (component.getName() == "secondJPasswordField") password2JPasswordField = (JPasswordField) component;
            if (component.getName() == "secondTextField") password2TextField = (JTextField) component;
        }
        //swap and reswap fields to ensure both fields have the same string
        swap(password1TextField, password1JPasswordField);
        swap(password1TextField, password1JPasswordField);
        swap(password2TextField, password2JPasswordField);
        swap(password2TextField, password2JPasswordField);

        Component[] components = {tagNameTextField, usernameTextField, password1JPasswordField,
            password1TextField, password2JPasswordField, password2TextField};
        
        //Check input for emptyness
        for (Component c : components){
            if (c instanceof JPasswordField){
                if (!(Encryption.notEmpty((JPasswordField)c))){
                    verifyLabel.setText("One of the fields is empty");
                    return;
                }
            } else {
                if (!(Encryption.notEmpty((JTextField)c))){
                    verifyLabel.setText("One of the fields is empty");
                    return;
                }
            }
        }
        if (tagNameTextField.getText().length() > 50){
            verifyLabel.setText("Max Username Length : 50");
            return;
        }
        //Check that passwords match
        if (!(Encryption.checkTextMatches(password1JPasswordField, password2JPasswordField))){
            verifyLabel.setText("Passwords Must Match");
            return;
        }

        if (editScreen) {
            // remove old password field and replace
            try {
                String tag = (databaseConnection.getColumnData("tag_name", edit_field));
                databaseConnection.dropRow(tag);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
        //Check that dtag_name does not exist in Database
            try {
                if (databaseConnection.isInDatabase(tagNameTextField.getText())){
                    verifyLabel.setText("Name Already Exists");
                    return;
                }
            } catch (SQLException e){
                System.out.println(e);
            }
        }

        //Save
        saveData(tagNameTextField, usernameTextField, password1JPasswordField);

        clearFrame();
        
        try {
            loadMainScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void validateInputOnChangeRootScreen(){
        /**
         * check inputs on chnage root password screen and if all inputs are correct,
         * change root password.
         * 
         * @param none
         * @return none
         */
        JTextField textfield = null;
        JPasswordField passwordField = null;
        JTextField textfield1 = null;
        JPasswordField passwordField1 = null;
        for (Component component : screenComponents){
            if (component.getName() == "firstPsTextField") textfield = (JTextField) component;
            if (component.getName() == "firstJPasswordField") passwordField = (JPasswordField) component;
            if (component.getName() == "secondTextField") textfield1 = (JTextField) component;
            if (component.getName() == "secondJPasswordField") passwordField1 = (JPasswordField) component;
        }
        
        /* swap and reswap compoenents to ensure password is properly collected */
        swap(textfield1, passwordField1);
        swap(textfield1, passwordField1);

        swap(textfield, passwordField);
        swap(textfield, passwordField);

        if (Encryption.notEmpty(passwordField) && Encryption.notEmpty(passwordField1) && Encryption.checkTextMatches(passwordField, passwordField1)){

            int output = JOptionPane.showOptionDialog(this,(Object)"You will be logged out and your password will be changed. Do you wish to proceed?","Caution",
                          JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, new Object[]{"Yes","No","Cancel"}, "No");
            if (output == JOptionPane.YES_OPTION){
                try{
                    databaseConnection.setRootPassword(String.valueOf(passwordField.getPassword()));
                } catch (SQLException e1){
                    e1.printStackTrace();
                    verifyLabel.setText("An Error Occured");
                }
                clearFrame();
                loadLoginScreen();
            }
        } else if (!Encryption.checkTextMatches(passwordField, passwordField1)){
            verifyLabel.setText("Passwords Must Match");
        } else {
            verifyLabel.setText("Password is Empty");
        }
    }
    private void saveData(JTextField tagNameTextField, JTextField usernameTextField, JPasswordField password1JPasswordField){
        /**
         * saves password information into database
         * 
         * @param tagNameTextField : JTextField that contains tag name of password
         * @param usernameTextField : JTextField that contains username of password
         * @param password1JPasswordField : JPasswordField that contains password
         */
        Encryption encryptionClass = new Encryption(String.valueOf(password1JPasswordField.getPassword()));
        try {
            databaseConnection.addRow(tagNameTextField.getText(), usernameTextField.getText(), encryptionClass.getPassword(), encryptionClass.getKey());
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    private void saveData(String tagName, String username, String password, String key){
        /**
         * saves password information into database
         * 
         * @param tagName : String that contains tag name of password
         * @param username : String that contains username of password
         * @param password : String that contains password
         * @param key : String that contains password key
         */
        try {
            databaseConnection.addRow(tagName, username, password, key);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }




    //GUI methods
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

        JLabel firstPasswordLabel = createLabel("firstPasswordLabel","Enter Vault Password", 16, 0, 0,0, 0);
        firstPasswordLabel.setHorizontalAlignment(JLabel.RIGHT);

        JPasswordField firstJPasswordField = createJPasswordField("firstJPasswordField",16, 10, 103, 150, 40);

        JTextField firstPsTextField = createTextField("firstPsTextField", 16, 10, 103, 150, 40);

        JButton viewButton = createButton("viewButton", "View", 16, 165, 103, 80 ,40);
        
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

        verifyLabel = createLabel("verifyLabel","", 16, 250, 25,300, 50);
        verifyLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton submitButton = createButton("submitButton", "Enter", 16, 350, 75, 100 ,50);

        passwordJPanel.add(firstPasswordLabel);
        passwordJPanel.add(firstPasswordFieldPanel);

        submitPanel.add(submitButton);
        submitPanel.add(verifyLabel);

        this.add(IntroPanel);
        this.add(passwordJPanel);
        this.add(submitPanel);

        this.setVisible(true);

        loginScreen = true;

    }
    private void loadMainScreen() throws IOException {
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
        buttonPanel.setPreferredSize(new Dimension(800,150));
        buttonPanel.setName("buttonPanel");
        saveComponent(buttonPanel);  

        JButton createPassButton = createButton("createPassButton", "Create Password", -1, 10, 10, 170, 50);

        JButton changeRootPassButton = createButton("changeRootPassButton", "Change Vault Passkey", -1, 430, 10, 170, 50);

        JButton LogoutButton = createButton("LogoutButton", "Logout", -1, 610, 10, 160, 50);

        JButton exportButton = createButton("exportButton", "Export", -1, 610, 70, 160, 50);

        JButton importButton = createButton("importButton", "Import", -1, 10, 70, 170, 50);

        buttonPanel.add(createPassButton);
        buttonPanel.add(changeRootPassButton);
        buttonPanel.add(LogoutButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(importButton);

        this.add(buttonPanel, BorderLayout.NORTH);

        // lower password segment
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setBorder(BorderFactory.createLineBorder(BGCOLOR, 0));
        passwordPanel.setBackground(BGCOLOR);
        passwordPanel.setName("passwordPanel");
        loadPasswords(passwordPanel);
        saveComponent(passwordPanel);
        
        JScrollPane paneScroller = new JScrollPane(passwordPanel);
        paneScroller.getVerticalScrollBar().setUnitIncrement(5);
        this.add(paneScroller, BorderLayout.CENTER);
        this.setVisible(true);

        mainScreen = true;
         
    }
    private void loadPasswords(JPanel ALLpasswordPanel) throws IOException {
        /**
         * adds JPanels contanting components tied to user entered passwords to ALLpasswordPanel.
         * 
         * @param ALLpasswordPanel : main password JPanel where all sub JPanels will be added to.
         * @return none
         */
        try {
            this.passwordPanels = new JPanel[databaseConnection.getRowCount() /* Change this to some number to see passowrd panels*/];
        } catch (SQLException e){
            System.out.println(e);
        }
        for (int i=0; i<passwordPanels.length; i++){
            //Create JPanel and add components
            JPanel passwordPanel = new JPanel();
            passwordPanel.setBackground(BGCOLOR);

            JLabel passwordTagLabel = new JLabel("No tag_name found");
            passwordTagLabel.setPreferredSize(new Dimension(passwordTagLabel.getWidth()+200, passwordTagLabel.getHeight()+45));
            try {
                String tag_name = databaseConnection.getColumnData("tag_name", i+1);  
                passwordTagLabel.setText(tag_name);
                passwordPanel.setName(tag_name);
            } catch (SQLException e){
                System.out.println(e);
            }
            passwordTagLabel.setFont(new Font(FONT, Font.PLAIN, 22));

            JLabel usernameLabel = new JLabel("No username found");
            usernameLabel.setPreferredSize(new Dimension(usernameLabel.getWidth()+200, usernameLabel.getHeight()+45));
            try {
                usernameLabel.setText(databaseConnection.getColumnData("username", i+1));
            } catch (SQLException e) {
                System.out.println(e);
            }
            usernameLabel.setFont(new Font(FONT, Font.PLAIN, 22));

            JTextField passwordTextField = new JTextField("••••••");
            passwordTextField.setPreferredSize(new Dimension(passwordTextField.getWidth()+80, passwordTextField.getHeight()+45));
            passwordTextField.setName("passwordTextField");
            passwordTextField.setFont(new Font(FONT, Font.PLAIN, 16));
            passwordTextField.setEditable(false);

            int buttonW = 45;
            int buttonH = 45;

            Image copyImg = ImageIO.read(getClass().getResource("copy.png"));
            Image viewImg = ImageIO.read(getClass().getResource("view.png"));
            Image editImg = ImageIO.read(getClass().getResource("edit.png"));
            Image delImg = ImageIO.read(getClass().getResource("del.gif"));

            JButton copyPassButton = createButton("copyPassButton", copyImg, buttonW, buttonH);
            JButton copyUsrButton = createButton("copyUsrButton", copyImg, buttonW, buttonH);
            JButton viewButton = createButton("viewButton", viewImg, buttonW, buttonH);
            JButton editButton = createButton("editButton", editImg, buttonW, buttonH);
            JButton removeButton = createButton("removeButton", delImg, buttonW, buttonH);

            passwordPanel.add(Box.createRigidArea(new Dimension(0, 75)));

            passwordPanel.add(passwordTagLabel);
            passwordPanel.add(usernameLabel);
            passwordPanel.add(copyUsrButton);
            passwordPanel.add(passwordTextField);
            passwordPanel.add(copyPassButton);
            passwordPanel.add(viewButton);
            passwordPanel.add(editButton);
            passwordPanel.add(removeButton);
            ALLpasswordPanel.add(passwordPanel);

            passwordPanels[i] = passwordPanel;
        }
    }
    private void loadPassScreen(String name, int edit_field) {
        /**
         * loads password screen and all needed components
         * 
         * @param name : String name of the screen 
         * @param edit_field : 0 for creating a new password, any other
         * positive integer for replacing a password of that given index
         * @return none
         */
        
        this.setLayout(new GridLayout(0,2,0,0));

        JPanel IntroPanel = new JPanel();
        IntroPanel.setLayout(null);
        
        JLabel IntroLabel = createLabel("IntroLabel", name, 30, 20, 5, 400, 60);

        IntroPanel.add(IntroLabel);
        IntroPanel.setBackground(BGCOLOR);
        IntroPanel.setName("IntroPanel");
        saveComponent(IntroPanel);

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

        JButton generatePassButton = createButton("generatePassButton", "Use Strong Password", 16, 70, 450, 250, 50);

        IntroPanel.add(InfoTextArea);
        IntroPanel.add(generatePassButton);
    
        JPanel passwordJPanel = new JPanel();
        passwordJPanel.setLayout(new BoxLayout(passwordJPanel, BoxLayout.Y_AXIS));
        passwordJPanel.setBorder(BorderFactory.createLineBorder(BGCOLOR, 0));
        passwordJPanel.setBackground(BGCOLOR);
        passwordJPanel.setName("passwordJPanel");
        saveComponent(passwordJPanel);

        JLabel tagnameLabel = createLabel("tagnamLabel", "Password Name", 16, 0, 17, 150, 30);

        // initialize text fields
        JTextField tagnameTextField = createTextField("tagnameTextField", 16, 150, 17, 150, 30);
        tagnameTextField.setDocument(new JLimitedTextField(12));
        tagnameTextField.setVisible(true);
        JTextField usernameTextField = createTextField("usernameTextField", 16, 150, 17, 150, 30);
        usernameTextField.setVisible(true);
        if (editScreen) {
            try {
                tagnameTextField.setText(databaseConnection.getColumnData("tag_name", edit_field));
                usernameTextField.setText(databaseConnection.getColumnData("username", edit_field));

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        JPanel tagnameJPanel = new JPanel();
        tagnameJPanel.setLayout(null);
        tagnameJPanel.setBackground(BGCOLOR);
        tagnameJPanel.add(tagnameLabel);
        tagnameJPanel.add(tagnameTextField);
        tagnameJPanel.setName("tagnameJPanel");
        saveComponent(tagnameJPanel);

        JLabel usernameLabel = createLabel("usernameLabel", "Username", 16, 0, 17, 150, 30);
        usernameLabel.setHorizontalAlignment(JLabel.LEFT);

        JPanel usernameJPanel = new JPanel();
        usernameJPanel.setLayout(null);
        usernameJPanel.setBackground(BGCOLOR);
        usernameJPanel.add(usernameLabel);
        usernameJPanel.add(usernameTextField);
        usernameJPanel.setName("usernameJPanel");
        saveComponent(usernameJPanel);

        JLabel firstPasswordLabel = createLabel("firstPasswordLabel", "Password", 16, 0, 17, 150, 30);

        JPasswordField firstJPasswordField = createJPasswordField("firstJPasswordField", 16, 150, 17, 150, 30);

        JTextField firstPsTextField = createTextField("firstPsTextField", 16, 150, 17, 150, 30);
        JTextField secondTextField = createTextField("secondTextField", 16, 150, 17, 150, 30);

        JButton viewButton = createButton("viewButton", "View", 16, 305, 13, 80, 35);

        JLabel passwordStrengthLabel = createLabel("passwordStrenghtLabel", "Strength : ", 16, 0, 48, 150, 30);

        strengthLabel = createLabel("StrengthLabel", "", 16, 90, 48, 30, 30);
        strengthLabel.setOpaque(true);
        strengthLabel.setBackground(new Color(0xEB1C04));
        strengthLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        
        JPanel firstPasswordFieldPanel = new JPanel();
        firstPasswordFieldPanel.setLayout(null);
        firstPasswordFieldPanel.setBackground(BGCOLOR);
        firstPasswordFieldPanel.add(firstPasswordLabel);
        firstPasswordFieldPanel.add(firstJPasswordField);
        firstPasswordFieldPanel.add(firstPsTextField);
        firstPasswordFieldPanel.add(viewButton);
        firstPasswordFieldPanel.add(passwordStrengthLabel);
        firstPasswordFieldPanel.add(strengthLabel);
        firstPasswordFieldPanel.setName("firstPasswordFieldPanel");
        saveComponent(firstPasswordFieldPanel);

        JLabel secondPasswordLabel = createLabel("secondPasswordLabel", "Verify Password", 16, 0, 17, 150, 30);
        secondPasswordLabel.setHorizontalAlignment(JLabel.LEFT);

        JPasswordField secondJPasswordField = createJPasswordField("secondJPasswordField", 16, 150, 17, 150, 30);

        if (editScreen) {
            try {
                String password = Encryption.getDecryptedPassword(databaseConnection.getColumnData("password", edit_field), databaseConnection.getColumnData("passKey", edit_field));
                firstJPasswordField.setText(password);
                secondJPasswordField.setText(password);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        JButton viewButton1 = createButton("viewButton1", "View", 16, 305, 13, 80, 35);

        JPanel secondPasswordFieldPanel = new JPanel();
        secondPasswordFieldPanel.setLayout(null);
        secondPasswordFieldPanel.setBackground(BGCOLOR);
        secondPasswordFieldPanel.add(secondPasswordLabel);
        secondPasswordFieldPanel.add(secondJPasswordField);
        secondPasswordFieldPanel.add(secondTextField);
        secondPasswordFieldPanel.add(viewButton1);
        secondPasswordFieldPanel.setName("secondPasswordFieldPanel");
        saveComponent(secondPasswordFieldPanel);

        JButton submitButton = createButton("submitButton", "Enter", 16, 170, 0, 100, 30);

        verifyLabel = createLabel("verifyLabel", "", 16, 125, 50, 300, 20);

        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(null);
        submitPanel.setBackground(BGCOLOR);
        submitPanel.add(submitButton);
        submitPanel.add(verifyLabel);
        submitPanel.setName("submitPanel");
        saveComponent(submitPanel);

        JLabel spacer = new JLabel("");
        spacer.setPreferredSize(new Dimension(100,20));

        JButton goBackButton = createButton("goBackButton", "Cancel", 16, 100, 5, 0, 30);
        goBackButton.setSize(new Dimension(100,50));
        goBackButton.setAlignmentX(JButton.RIGHT);

        JLabel spacer2 = new JLabel("");
        spacer2.setPreferredSize(new Dimension(200,50));

        JLabel spacer3 = new JLabel("");
        spacer3.setPreferredSize(new Dimension(100,200));

        passwordJPanel.add(spacer);
        passwordJPanel.add(goBackButton);
        passwordJPanel.add(spacer2);
        passwordJPanel.add(tagnameJPanel);
        passwordJPanel.add(usernameJPanel);
        passwordJPanel.add(firstPasswordFieldPanel);
        passwordJPanel.add(secondPasswordFieldPanel);
        passwordJPanel.add(submitPanel);
        passwordJPanel.add(spacer3);

        this.add(IntroPanel);
        this.add(passwordJPanel);
        
        this.setVisible(true);
    }
    private void loadSubScreen(String name){
        /**
         * loads sub screen for changing passwords
         * 
         * @param name : String name for the screen
         * @return none
         */
        this.setLayout(new GridLayout(3,1,0,10));

        JPanel IntroPanel = new JPanel();
        IntroPanel.setLayout(null);
        
        JLabel IntroLabel = createLabel("IntroLabel", name, 30, 20, 5, 400, 60);

        JButton goBackButton = createButton("goBackButton", "Cancel", 16, 680, 20, 0, 30);
        goBackButton.setSize(new Dimension(100,30));
        goBackButton.setAlignmentX(JButton.RIGHT);

        IntroPanel.add(IntroLabel);
        IntroPanel.add(goBackButton);
        IntroPanel.setBackground(BGCOLOR);
        IntroPanel.setName("IntroPanel");
        saveComponent(IntroPanel);
        
        JPanel passwordJPanel = new JPanel();
        passwordJPanel.setLayout(new BoxLayout(passwordJPanel, BoxLayout.Y_AXIS));
        passwordJPanel.setBorder(BorderFactory.createLineBorder(BGCOLOR, 0));
        passwordJPanel.setBackground(BGCOLOR);
        passwordJPanel.setName("passwordJPanel");
        saveComponent(passwordJPanel);

        JLabel firstPasswordLabel = createLabel("firstPasswordLabel", "Password", 16, 200, 17, 150, 30);

        JPasswordField firstJPasswordField = createJPasswordField("firstJPasswordField", 16, 375, 17, 150, 30);

        JTextField firstPsTextField = createTextField("firstPsTextField", 16, 375, 17, 150, 30);

        JButton viewButton = createButton("viewButton", "View", 16, 530, 15, 80, 35);

        JLabel passwordStrengthLabel = createLabel("passwordStrenghtLabel", "Strength : ", 16, 200, 58, 150, 30);

        strengthLabel = createLabel("StrengthLabel", "", 16, 295, 58, 30, 30);
        strengthLabel.setOpaque(true);
        strengthLabel.setBackground(new Color(0xEB1C04));
        strengthLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        
        JPanel firstPasswordFieldPanel = new JPanel();
        firstPasswordFieldPanel.setLayout(null);
        firstPasswordFieldPanel.setBackground(BGCOLOR);
        firstPasswordFieldPanel.add(firstPasswordLabel);
        firstPasswordFieldPanel.add(firstJPasswordField);
        firstPasswordFieldPanel.add(firstPsTextField);
        firstPasswordFieldPanel.add(viewButton);
        firstPasswordFieldPanel.add(passwordStrengthLabel);
        firstPasswordFieldPanel.add(strengthLabel);
        firstPasswordFieldPanel.setName("firstPasswordFieldPanel");
        saveComponent(firstPasswordFieldPanel);

        JLabel secondPasswordLabel = createLabel("secondPasswordLabel", "Verify Password", 16, 200, 17, 150, 30);
        secondPasswordLabel.setHorizontalAlignment(JLabel.LEFT);

        JPasswordField secondJPasswordField = createJPasswordField("secondJPasswordField", 16, 375, 17, 150, 30);

        JTextField secondTextField = createTextField("secondTextField", 16, 375, 17, 150, 30);

        JButton viewButton1 = createButton("viewButton1", "View", 16, 530, 13, 80, 35);

        JPanel secondPasswordFieldPanel = new JPanel();
        secondPasswordFieldPanel.setLayout(null);
        secondPasswordFieldPanel.setBackground(BGCOLOR);
        secondPasswordFieldPanel.add(secondPasswordLabel);
        secondPasswordFieldPanel.add(secondJPasswordField);
        secondPasswordFieldPanel.add(secondTextField);
        secondPasswordFieldPanel.add(viewButton1);
        secondPasswordFieldPanel.setName("secondPasswordFieldPanel");
        saveComponent(secondPasswordFieldPanel);

        JButton submitButton = createButton("submitButton", "Enter", 16, 400, 0, 100, 30);

        verifyLabel = createLabel("verifyLabel", "", 16, 350, 50, 300, 20);

        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(null);
        submitPanel.setBackground(BGCOLOR);
        submitPanel.add(submitButton);
        submitPanel.add(verifyLabel);
        submitPanel.setName("submitPanel");
        saveComponent(submitPanel);

        passwordJPanel.add(firstPasswordFieldPanel);
        passwordJPanel.add(secondPasswordFieldPanel);

        this.add(IntroPanel);
        this.add(passwordJPanel);
        this.add(submitPanel);
        
        this.setVisible(true);
    }

}
