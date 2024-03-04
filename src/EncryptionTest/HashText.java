package EncryptionTest;
import java.math.BigInteger;
import java.security.*;
public class HashText {
    private String password;
    HashText(String password) throws NoSuchAlgorithmException{
        this.password = password;
        preformHash();
    }
    private void preformHash() throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        save(bigInt.toString(16));
    }
    public void save(String hashedPassword){
        System.out.println(hashedPassword);
    }
}
