package EncryptionTest;
import java.util.*;
import javax.swing.*;


/**
 * Encryption
 * 
 * Contains all methods needed for encrypting passwords and verifying
 * attributes of passwords.
 * 
 */
public class Encryption {
    private char[] asciiTable;
    private char[] key;
    private char[] password;
    private Random random;
    Encryption(String password){
        /**
         * takes password and calls methods for encrypting password.
         * 
         * @param password: String which contains plaintext password
         */
        this.password = password.toCharArray();
        createKey();
        encryptPassword();
    }
    private void createKey(){
        /**
         * Creates password key by shuffling ascii table (One time pad).
         * 
         * @param none
         * @return none
         */
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
        /**
         * Uses created password key and plaintext password and encrypts password.
         * 
         * @param none
         * @return none
         */
        char[] passwordArray = password;
        for (int i=0; i<passwordArray.length; i++){
            for (int j=0; j<asciiTable.length; j++){
                if (asciiTable[j] == passwordArray[i]){
                    passwordArray[i] = key[j];
                    break;
                }
            }
        }
        this.password = passwordArray;
    }
    public String getPassword(){
        /**
         * clear password field and returns encrypted password
         * 
         * this method is intented to be call right after initialization of encrpytion class.
         * 
         * @param none
         * @return tmp : containig encryted password
         */
        String tmp = String.valueOf(password);
        Arrays.fill(password, '\0');  //wipe ecrypted password from object's field
        return tmp;
    }
    public String getKey(){
        /**
         * clear key field and returns password key
         * 
         * this method is intented to be call right after initialization of encrpytion class.
         * 
         * @param none
         * @return tmp : containig password key
         */
        String tmp = String.valueOf(key);
        Arrays.fill(key, '\0');
        return tmp;
    }
    private char[] decryptPassword(char[] passwordArray,  char[] key){
        /**
         * decrpyts password (assumming password passed is encrypted).
         * 
         * @param passwordArray : char[] containing encrypted password
         * @param key : char[] containing password key
         * 
         * @return passwordArray : decrypted password
         */
        for (int i=0; i<passwordArray.length; i++){   
            for (int j=0; j<key.length; j++){
                if (key[j] == passwordArray[i]){
                    passwordArray[i] = asciiTable[j];
                    break;
                }
            }
        }
        return passwordArray;
    }
    public static boolean checkTextMacthes(JPasswordField a, JPasswordField b){
        /**
         * validates that both passwords from JPasswordFields are the same.
         * 
         * @param a : JPasswordField containing text
         * @param b : JPasswordField containing text
         * 
         * @returns boolean: true if passwords are the same; otherwise false
         */
        return (String.valueOf(a.getPassword())).equals(String.valueOf(b.getPassword()));
    }
    public static boolean notEmpty(JPasswordField a){
        /**
         * validates that JPasswordField is not empty (!= "").
         * 
         * @param a : JPasswordField possibly containting text
         * 
         * @returns boolean: true if JPasswordField is not empty; otherwise false
         */
        System.out.println(!(String.valueOf(a.getPassword())).equals(""));
        return !(String.valueOf(a.getPassword())).equals("");
    }
    public static boolean checkPasswordRequirments(JPasswordField a){
        /**
         * validates that JPasswordField meets requierments in method.
         * 
         * @param a : JPasswordField containting text
         * 
         * @returns boolean: true if JPasswordField is passes requierments; otherwise false
         */
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
