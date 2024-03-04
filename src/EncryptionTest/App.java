package EncryptionTest;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.NoSuchAlgorithmException;

import javax.swing.*;
public class App extends JFrame implements ActionListener, KeyListener{
    public static final Color BGCOLOR = new Color(0x1F35A4);
    private JPasswordField MsJPasswordField;
    private JPasswordField MsValidateJPasswordField;
    private JButton submitButton;
    App(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setTitle("Password Manager");
        this.getContentPane().setBackground(BGCOLOR);
        loadMasterPasswordScreen();
        this.setVisible(true);
    }
    //Event Methods
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == MsJPasswordField){
            MsValidateJPasswordField.requestFocus();
        }
        if (e.getSource() == MsValidateJPasswordField || e.getSource() == submitButton){  //(if submit is pressed); Must press ENTER 2 times on MsValidateJPasswordField to hash Plz fix
            if (Encryption.checkPasswordRequirments(MsValidateJPasswordField) && (Encryption.checkTextMacthes(MsValidateJPasswordField, MsJPasswordField))){
                try {
                    new HashText(String.valueOf((MsJPasswordField.getPassword())));
                } catch (NoSuchAlgorithmException error){
                    System.err.println(error);
                }
                
            }
        }
    }
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());
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
        this.setLayout(new GridLayout(2,1,0,10));
        JPanel MsPanel = new JPanel();
        MsPanel.setLayout(new GridLayout(2,2, 20, 20));
        MsPanel.setBackground(Color.GREEN);
        JPanel submitPanel = new JPanel();
        submitPanel.setBackground(Color.RED);

        JLabel MsJPasswordLabel = new JLabel("Enter Password");
        MsJPasswordField = new JPasswordField();
        MsJPasswordField.addKeyListener(this);
        MsJPasswordField.addActionListener(this);

        JLabel MsValidateJPasswordLabel = new JLabel("Re-enter Password");
        MsValidateJPasswordField = new JPasswordField();
        MsValidateJPasswordField.addKeyListener(this);
        MsValidateJPasswordField.addActionListener(this);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        MsPanel.add(MsJPasswordLabel);
        MsPanel.add(MsJPasswordField);
        MsPanel.add(MsValidateJPasswordLabel);
        MsPanel.add(MsValidateJPasswordField);
        submitPanel.add(submitButton);

        this.add(MsPanel);
        this.add(submitPanel);

    }
}
