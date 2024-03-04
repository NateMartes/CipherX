package EncryptionTest;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
public class App extends JFrame implements ActionListener{
    public static final Color BGCOLOR = new Color(0x1F35A4);
    private TextField MsTextField;
    private TextField MsValidateTextField;
    private JButton submitButton;
    App(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setTitle("Password Manager");
        this.getContentPane().setBackground(BGCOLOR);
        loadMasterPasswordScreen();
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       
    }
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

        JLabel MsTextFieldLabel = new JLabel("Enter Password");
        MsTextField = new TextField();
        JLabel MsValidateTextFieldLabel = new JLabel("Re-enter Password");
        MsValidateTextField = new TextField();
        submitButton = new JButton("Submit");

        MsPanel.add(MsTextFieldLabel);
        MsPanel.add(MsTextField);
        MsPanel.add(MsValidateTextFieldLabel);
        MsPanel.add(MsValidateTextField);
        submitPanel.add(submitButton);

        this.add(MsPanel);
        this.add(submitPanel);

    }
    
}
