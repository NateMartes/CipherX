package EncryptionTest;
import java.util.*;
import javax.swing.*;

public class Encryption {
    private char[] asciiTable;
    private char[] key;
    private String password;
    private Random random;
    Encryption(String password){
        this.password = password;
        createKey();
        encryptPassword();
    }
    private void createKey(){
        //Create ASCII table
        char[] asciiTable = new char[95];
        for (char i=' '; i<127; i++){
            asciiTable[i-32] = i;
        }
        this.asciiTable = asciiTable;
        //Create key
        char[] key = new char[95];
        this.random = new Random();
        for (char i=0; i<key.length; i++){
            int index = random.nextInt(asciiTable.length);
            boolean inArray = true; //assume ascii[index] already exist in key
            while (inArray){
                inArray = false;
                index = random.nextInt(asciiTable.length);
                for (int j=0; j<key.length; j++){
                    if (asciiTable[index] == key[j]){
                        inArray = true;
                        break;
                    }
                }
            }
            key[i] = asciiTable[index];
        }
        this.key = key;
    }
    private void encryptPassword(){
        char[] passwordArray = password.toCharArray();
        for (int i=0; i<passwordArray.length; i++){
            for (int j=0; j<asciiTable.length; j++){
                if (asciiTable[j] == passwordArray[i]){
                    passwordArray[i] = key[j];
                    break;
                }
            }
        }
        this.password = String.valueOf(passwordArray);
    }
    private void decryptPassword(){
        char[] passwordArray = password.toCharArray();
        for (int i=0; i<passwordArray.length; i++){   
            for (int j=0; j<key.length; j++){
                if (key[j] == passwordArray[i]){
                    passwordArray[i] = asciiTable[j];
                    break;
                }
            }
        }
        this.password = String.valueOf(passwordArray);
    }
    public static boolean checkTextMacthes(JPasswordField a, JPasswordField b){
        return (String.valueOf(a.getPassword())).equals(String.valueOf(b.getPassword()));
    }
    public static boolean checkPasswordRequirments(JPasswordField a){
        //Length 8, 1 symbol, 1 uppercase char
        String password = String.valueOf(a.getPassword());
        if (!(password.length() >= 8)){
            return false;
        }
        boolean hasSymbolChar = false;
        boolean hasUpperChar = false;
        boolean hasLowerChar = false;
        boolean hasNumChar = false;
        for (int i=0; i<password.length(); i++){
            char currentChar = password.charAt(i);
            if (((currentChar >= 33) && (currentChar <= 47)) || 
                ((currentChar >= 58) && (currentChar <= 64)) ||
                ((currentChar >= 91) && (currentChar <= 96)) ||
                ((currentChar >= 123) && (currentChar <= 126))) {
                hasSymbolChar = true;
            } else if ((currentChar >= 65) && (currentChar <= 90)) {
                hasUpperChar = true;
            } else if ((currentChar >= 97) && (currentChar <= 122)) {
                hasLowerChar = true;
            } else if ((currentChar >= 48) && (currentChar <= 57)) {
                hasNumChar = true;
            }
        }
        return hasSymbolChar && hasUpperChar && hasLowerChar && hasNumChar;
    }
}
