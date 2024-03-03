package EncryptionTest;
import java.util.*;

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
}
