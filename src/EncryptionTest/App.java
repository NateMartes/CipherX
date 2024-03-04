package EncryptionTest;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;

public class App extends JFrame implements ActionListener, KeyListener{
    public static final Color BGCOLOR = new Color(0x757981);
    public static final String FONT = "Verdana";
    private JPasswordField MsJPasswordField;
    private JPasswordField MsValidateJPasswordField;
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
        if (e.getSource() == MsJPasswordField){
            MsValidateJPasswordField.requestFocus();
        }
        if (e.getSource() == submitButton){
            if (Encryption.checkPasswordRequirments(MsValidateJPasswordField) && (Encryption.checkTextMacthes(MsValidateJPasswordField, MsJPasswordField))){
                try {
                    new HashText(String.valueOf((MsJPasswordField.getPassword())));
                    clearFrame();
                    loadMainScreen();
                } catch (NoSuchAlgorithmException error){
                    System.err.println(error);
                }
            } else if (Encryption.checkPasswordRequirments(MsValidateJPasswordField)){
                verifyLabel.setText("Passwords Must Match");
            } else {
                verifyLabel.setText("Read Password Requierments");
            }
        }
    }
    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {

    }
    public void keyReleased(KeyEvent e) {
        
    }
    //GUI Methods
    private void clearFrame(){
        this.getContentPane().removeAll();
        this.revalidate(); //recomputes layout of componets in frame
        this.repaint(); //redraws components to frame
    }
    private void loadMasterPasswordScreen(){
        this.setLayout(new GridLayout(3,1,0,10));

        JPanel IntroPanel = new JPanel();
        IntroPanel.setLayout(new BorderLayout());
        JLabel IntroLabel = new JLabel(" Welcome to CipherX!");
        IntroLabel.setBounds(0,0,800,35);
        IntroLabel.setFont(new Font("Verdana",Font.BOLD, 30));
        JTextArea IntroText = new JTextArea();
        IntroText.setText("""
                \n Before you can begin saving your passwords, you need to set up a Master Password.\n
                 What is a Master Password? It's one password that will allow you to access all\n your other passwords to ensure proper security.\n
                 Please make sure you save your password somewhere\n you won't forget as this password can not be changed once set.\n
                 Password Requirments:
                 At least length 8
                 1 symbol (!, #, $, etc.)
                 1 number
                 1 uppercase letter
                 1 lowercase letter
                """);
        IntroText.setBounds(0,35,800,150);
        IntroText.setFont(new Font("Verdana",Font.PLAIN, 16));
        IntroText.setEditable(false);
        IntroPanel.add(IntroLabel, BorderLayout.NORTH);
        IntroPanel.setBackground(BGCOLOR);
        IntroText.setBackground(BGCOLOR);
        IntroPanel.add(new JScrollPane(IntroText));
        

        JPanel MsPanel = new JPanel(new GridLayout(2,2, 20, 0));
        MsPanel.setBackground(BGCOLOR);

        JLabel MsJPasswordLabel = new JLabel("Enter Password");
        MsJPasswordLabel.setFont(new Font("Verdana",Font.BOLD, 16));
        MsJPasswordLabel.setHorizontalAlignment(JLabel.RIGHT);
        MsJPasswordField = new JPasswordField();
        MsJPasswordField.setFont(new Font("Verdana",Font.BOLD, 16));
        MsJPasswordField.addKeyListener(this);
        MsJPasswordField.addActionListener(this);
        MsJPasswordField.setBounds(10,43,150,40);
        JPanel MsJPasswordFieldPanel = new JPanel();
        MsJPasswordFieldPanel.setLayout(null);
        MsJPasswordFieldPanel.setBackground(BGCOLOR);
        MsJPasswordFieldPanel.add(MsJPasswordField);

        JLabel MsValidateJPasswordLabel = new JLabel("Re-enter Password");
        MsValidateJPasswordLabel.setFont(new Font("Verdana",Font.BOLD, 16));
        MsValidateJPasswordLabel.setHorizontalAlignment(JLabel.RIGHT);
        MsValidateJPasswordField = new JPasswordField();
        MsValidateJPasswordField.setFont(new Font("Verdana",Font.BOLD, 16));
        MsValidateJPasswordField.addKeyListener(this);
        MsValidateJPasswordField.addActionListener(this);
        MsValidateJPasswordField.setBounds(10,43,150,40);
        JPanel MsValidateJPasswordFieldPanel = new JPanel();
        MsValidateJPasswordFieldPanel.setLayout(null);
        MsValidateJPasswordFieldPanel.setBackground(BGCOLOR);
        MsValidateJPasswordFieldPanel.add(MsValidateJPasswordField);

        JPanel submitPanel = new JPanel();
        submitPanel.setBackground(BGCOLOR);
        submitPanel.setLayout(null);
        verifyLabel = new JLabel();
        verifyLabel.setBounds(250,25,300,50);
        verifyLabel.setFont(new Font("Verdana",Font.BOLD, 16));
        verifyLabel.setHorizontalAlignment(JLabel.CENTER);
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Verdana",Font.BOLD, 16));
        submitButton.addActionListener(this);
        submitButton.setBounds(350,75,100,50);

        MsPanel.add(MsJPasswordLabel);
        MsPanel.add(MsJPasswordFieldPanel);
        MsPanel.add(MsValidateJPasswordLabel);
        MsPanel.add(MsValidateJPasswordFieldPanel);
        submitPanel.add(submitButton);
        submitPanel.add(verifyLabel);

        this.add(IntroPanel);
        this.add(MsPanel);
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
