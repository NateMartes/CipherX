package CipherX;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * 
 * App
 * 
 * Contains All GUI methods visible to the user.
 */
public class App extends JFrame implements ActionListener, KeyListener{
    public static final Color BGCOLOR = new Color(0x757981);
    public static final String FONT = "Verdana";
    private JPasswordField fristJPasswordField;
    private TextField firstPsTextField;
    private JPasswordField secondJPasswordField;
    private TextField secondTextField;
    private JButton viewButton;
    private JButton viewButton1;
    private JButton createPassButton;
    private JButton changeRootPassButton;
    private JButton LogoutButton;
    private JButton submitButton;
    private JLabel verifyLabel;
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
        loadRootPasswordScreen();
        this.setVisible(true);
    }
    //Event Methods
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fristJPasswordField){
            secondJPasswordField.requestFocus();
        }
        if (e.getSource() == submitButton){
            if (firstPsTextField.isVisible()){
                swap(firstPsTextField, fristJPasswordField);
            }
            if (secondTextField.isVisible()) {
                swap(secondTextField, secondJPasswordField);
            }
            if ((Encryption.checkTextMacthes(secondJPasswordField, fristJPasswordField)) && Encryption.notEmpty(fristJPasswordField) && Encryption.notEmpty(secondJPasswordField)){
                try {
                    //Calls SQLConnection class to connect with local MySQL database
                    databaseConnection = new SQLConnection("root", String.valueOf(fristJPasswordField.getPassword()));
                    runstartup();
                } catch (SQLException e1) {
                    if ((e1.toString()).equals("java.sql.SQLException: Access denied for user 'root'@'localhost' (using password: YES)")){
                        verifyLabel.setText("Password Incorrect");
                    } else {
                        verifyLabel.setText(e1.toString());
                    }
                }
            } else if (!(Encryption.checkTextMacthes(secondJPasswordField, fristJPasswordField))){
                verifyLabel.setText("Passwords Must Match");
            } else {
                verifyLabel.setText("Empty Password Field");
            }
        }
        if (e.getSource() == viewButton){
            swap(firstPsTextField, fristJPasswordField);
        } else if (e.getSource() == viewButton1){
            swap(secondTextField, secondJPasswordField);
        }
        if (e.getSource() == createPassButton) {
            // create new password (Zack)
        }
        if (e.getSource() == changeRootPassButton) {
            // change root password (Zack)
        }
        if (e.getSource() == LogoutButton) {
            // log out of SQL database & return to login screen (Zack)
            try {
                databaseConnection.exitDatabase(); // attempt to exit database
            } catch (SQLException e1) {
                System.out.println(e1.toString()); // print error on fail
            }
            // Change screen to login screen
            clearFrame();
            loadRootPasswordScreen();
            this.revalidate();
            this.repaint();
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    private void runstartup(){
        /**
         * calls startup methods
         * 
         * @param none
         * @return none
         */
        clearFrame();
        loadMainScreen();
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
        this.revalidate();
        this.repaint();
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
    private void loadRootPasswordScreen(){
        /**
         * adds GUI components to JFrame needed for root password screen.
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
        
        JPanel passwordJPanel = new JPanel(new GridLayout(2,2, 20, 0));
        passwordJPanel.setBackground(BGCOLOR);

        JLabel firstPasswordLabel = new JLabel("Enter Root Password");
        firstPasswordLabel.setFont(new Font(FONT,Font.BOLD, 16));
        firstPasswordLabel.setHorizontalAlignment(JLabel.RIGHT);

        fristJPasswordField = new JPasswordField();
        fristJPasswordField.setFont(new Font(FONT,Font.BOLD, 16));
        fristJPasswordField.addKeyListener(this);
        fristJPasswordField.addActionListener(this);
        fristJPasswordField.setBounds(10,43,150,40);

        firstPsTextField = new TextField();
        firstPsTextField.setFont(new Font(FONT,Font.BOLD,22));
        firstPsTextField.addKeyListener(this);
        firstPsTextField.addActionListener(this);
        firstPsTextField.setBounds(10,43,150,40);
        firstPsTextField.setVisible(false);

        viewButton = new JButton();
        viewButton.setText("View");
        viewButton.setFocusable(false);
        viewButton.setBounds(165, 43, 80, 40);
        viewButton.setFont((new Font(FONT,Font.BOLD, 16)));
        viewButton.addActionListener(this);
        
        JPanel firstPasswordFieldPanel = new JPanel();
        firstPasswordFieldPanel.setLayout(null);
        firstPasswordFieldPanel.setBackground(BGCOLOR);
        firstPasswordFieldPanel.add(fristJPasswordField);
        firstPasswordFieldPanel.add(firstPsTextField);
        firstPasswordFieldPanel.add(viewButton);

        JLabel secondPasswordLabel = new JLabel("Re-enter Root Password");
        secondPasswordLabel.setFont(new Font(FONT,Font.BOLD, 16));
        secondPasswordLabel.setHorizontalAlignment(JLabel.RIGHT);

        secondJPasswordField = new JPasswordField();
        secondJPasswordField.setFont(new Font(FONT,Font.BOLD, 16));
        secondJPasswordField.addKeyListener(this);
        secondJPasswordField.addActionListener(this);
        secondJPasswordField.setBounds(10,43,150,40);

        secondTextField = new TextField();
        secondTextField.setFont(new Font(FONT,Font.BOLD, 22));
        secondTextField.addKeyListener(this);
        secondTextField.addActionListener(this);
        secondTextField.setBounds(10,43,150,40);
        secondTextField.setVisible(false);

        viewButton1 = new JButton();
        viewButton1.setText("View");
        viewButton1.setFocusable(false);
        viewButton1.setBounds(165, 43, 80, 40);
        viewButton1.setFont((new Font(FONT,Font.BOLD, 16)));
        viewButton1.addActionListener(this);

        JPanel secondPasswordFieldPanel = new JPanel();
        secondPasswordFieldPanel.setLayout(null);
        secondPasswordFieldPanel.setBackground(BGCOLOR);
        secondPasswordFieldPanel.add(secondJPasswordField);
        secondPasswordFieldPanel.add(secondTextField);
        secondPasswordFieldPanel.add(viewButton1);

        JPanel submitPanel = new JPanel();
        submitPanel.setBackground(BGCOLOR);
        submitPanel.setLayout(null);

        verifyLabel = new JLabel();
        verifyLabel.setBounds(250,25,300,50);
        verifyLabel.setFont(new Font(FONT,Font.BOLD, 16));
        verifyLabel.setHorizontalAlignment(JLabel.CENTER);

        submitButton = new JButton("Enter");
        submitButton.setFocusable(false);
        submitButton.setFont(new Font(FONT,Font.BOLD, 16));
        submitButton.addActionListener(this);
        submitButton.setBounds(350,75,100,50);

        passwordJPanel.add(firstPasswordLabel);
        passwordJPanel.add(firstPasswordFieldPanel);
        passwordJPanel.add(secondPasswordLabel);
        passwordJPanel.add(secondPasswordFieldPanel);

        submitPanel.add(submitButton);
        submitPanel.add(verifyLabel);

        this.add(IntroPanel);
        this.add(passwordJPanel);
        this.add(submitPanel);
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

        createPassButton = new JButton("Create New Password");
        createPassButton.setBounds(10,10,170,50);
        createPassButton.setFocusable(false);
        createPassButton.addActionListener(this);

        changeRootPassButton = new JButton("Change Root Password");
        changeRootPassButton.setBounds(430,10,170,50);
        changeRootPassButton.setFocusable(false);
        changeRootPassButton.addActionListener(this);

        LogoutButton = new JButton("Logout");
        LogoutButton.setBounds(610,10,160,50);
        LogoutButton.setFocusable(false);
        LogoutButton.addActionListener(this);

        buttonPanel.add(createPassButton);
        buttonPanel.add(changeRootPassButton);
        buttonPanel.add(LogoutButton);

        this.add(buttonPanel, BorderLayout.NORTH);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setBorder(BorderFactory.createLineBorder(BGCOLOR, 0));
        passwordPanel.setBackground(BGCOLOR);

        loadPasswords(passwordPanel);
        
        this.add(new JScrollPane(passwordPanel), BorderLayout.CENTER);
        this.setVisible(true);
         
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

            JPasswordField passwordPasswordField = new JPasswordField("No password found");
            try {
                passwordPasswordField.setText(databaseConnection.getColumnData("tag_name", i+1)+"   ");
            } catch (SQLException e){
                System.out.println(e);
            }
            passwordPasswordField.setFont(new Font(FONT, Font.PLAIN, 22));
            passwordPasswordField.setEditable(false);

            JButton viewButton = new JButton("View");
            viewButton.setFocusable(false);
            viewButton.setFont((new Font(FONT,Font.BOLD, 18)));
            viewButton.addActionListener(this);

            JButton editButton = new JButton("Edit");
            editButton.setFocusable(false);
            editButton.setFont((new Font(FONT,Font.BOLD, 18)));
            editButton.addActionListener(this);

            JButton removeButton = new JButton("X");
            removeButton.setFocusable(false);
            removeButton.setFont((new Font(FONT,Font.BOLD, 18)));
            removeButton.addActionListener(this);

            passwordPanel.add(Box.createRigidArea(new Dimension(0, 75)));

            passwordPanel.add(passwordTagLabel);
            passwordPanel.add(passwordPasswordField);
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
}
