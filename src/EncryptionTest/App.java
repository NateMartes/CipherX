package EncryptionTest;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class App extends JFrame implements ActionListener, KeyListener{
    public static final Color BGCOLOR = new Color(0x757981);
    public static final String FONT = "Verdana";
    private JPasswordField fristJPasswordField;
    private TextField firstPsTextField;
    private JPasswordField secondJPasswordField;
    private TextField secondTextField;
    private JButton swapButton;
    private JButton swapButton1;
    private JButton submitButton;
    private JLabel verifyLabel;
    App(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setResizable(false);
        this.setTitle("CipherX");
        this.getContentPane().setBackground(BGCOLOR);
        loadMasterPasswordScreen();
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
            if ((Encryption.checkTextMacthes(secondJPasswordField, fristJPasswordField))){
                SQLConnection connection = new SQLConnection(String.valueOf(fristJPasswordField.getPassword()));
                if (connection.getConnectionStatus()) {
                    runstartup();
                } else { 
                    verifyLabel.setText("Password Incorrect");
                }
            } else {
                verifyLabel.setText("Passwords Must Match");
            }
        }
        if (e.getSource() == swapButton){
            swap(firstPsTextField, fristJPasswordField);
        } else if (e.getSource() == swapButton1){
            swap(secondTextField, secondJPasswordField);
        }
    }
    public void keyTyped(KeyEvent e) {
        
    }
    public void keyPressed(KeyEvent e) {

    }
    public void keyReleased(KeyEvent e) {
        
    }
    private void runstartup() {
        clearFrame();
        loadMainScreen();
    }
    //GUI Methods
    private void clearFrame(){
        this.getContentPane().removeAll();
        this.revalidate(); //recomputes layout of componets in frame
        this.repaint(); //redraws components to frame
    }
    private void swap(TextField textField, JPasswordField jPasswordField){
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
    private void loadMasterPasswordScreen(){
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

        swapButton = new JButton();
        swapButton.setText("View");
        swapButton.setFocusable(false);
        swapButton.setBounds(165, 43, 80, 40);
        swapButton.setFont((new Font(FONT,Font.BOLD, 16)));
        swapButton.addActionListener(this);
        
        JPanel firstPasswordFieldPanel = new JPanel();
        firstPasswordFieldPanel.setLayout(null);
        firstPasswordFieldPanel.setBackground(BGCOLOR);
        firstPasswordFieldPanel.add(fristJPasswordField);
        firstPasswordFieldPanel.add(firstPsTextField);
        firstPasswordFieldPanel.add(swapButton);

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

        swapButton1 = new JButton();
        swapButton1.setText("View");
        swapButton1.setFocusable(false);
        swapButton1.setBounds(165, 43, 80, 40);
        swapButton1.setFont((new Font(FONT,Font.BOLD, 16)));
        swapButton1.addActionListener(this);

        JPanel secondPasswordFieldPanel = new JPanel();
        secondPasswordFieldPanel.setLayout(null);
        secondPasswordFieldPanel.setBackground(BGCOLOR);
        secondPasswordFieldPanel.add(secondJPasswordField);
        secondPasswordFieldPanel.add(secondTextField);
        secondPasswordFieldPanel.add(swapButton1);

        JPanel submitPanel = new JPanel();
        submitPanel.setBackground(BGCOLOR);
        submitPanel.setLayout(null);

        verifyLabel = new JLabel();
        verifyLabel.setBounds(250,25,300,50);
        verifyLabel.setFont(new Font(FONT,Font.BOLD, 16));
        verifyLabel.setHorizontalAlignment(JLabel.CENTER);

        submitButton = new JButton("Submit");
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
        /*
         * Compoents needed
         * 
         * - Add password btn
         * - Password List
         *   List of passwords, with each of them having the 
         *   option to remove, view, and edit. Also each password
         *   has a label to intendify password
         * -Change MasterPassword
         */
    }
}
