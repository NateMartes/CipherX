package CipherX;
import java.io.*;
import java.nio.charset.Charset;
import javax.swing.*;
import java.util.*;
import org.apache.commons.csv.*;

public class FileScreen extends JFileChooser{
    private static final String FILE_NAME = "cipherxPasswords.csv";
    private JFrame parent;
    private String[][] records;
    FileScreen(JFrame screen, String[][] records){
        /**
         * constructs FileScreen obj and takes parent JFrame and String[][] records as parameters. If records is null, client is assumed
         * to be importing a CSV file
         * 
         * @param screen : JFrame parent of FileScreen()
         * @param records : String[][] matrix of formatted records, may be null
         * 
         * @return none
         */
        this.parent = screen;
        if (records == null){
            importFile();
        } else {
            exportFile(records);
        }
    }
    private void exportFile(String[][] records){
        /**
         * prompts client to enter output file path and creates csv file and stores it in the path
         * 
         * @param records : String[][] data to insert into output file
         * @return none
         */

        this.setSelectedFile(new File(FILE_NAME));
        this.showSaveDialog(parent);
        File outputFile = this.getSelectedFile();

        try {
            CSVPrinter csvStream;
            csvStream = new CSVPrinter(new FileWriter(outputFile), CSVFormat.EXCEL);
            csvStream.printRecord("name","username","password", "passKey");
            csvStream.printRecords((Object[])records);
            csvStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void importFile(){
        /**
         * pulls data from csv file and formats said data into matrix of Strings (records field)
         * 
         * @param records : String[][] the matrix to be filled with data
         * @return none
         */
        try{

        this.showOpenDialog(parent);
        File importFile = this.getSelectedFile();
        CSVParser csvParser = CSVParser.parse(importFile, Charset.forName("UTF-8") ,CSVFormat.EXCEL);
        List<CSVRecord> allCSVRecords = csvParser.getRecords();

        allCSVRecords.remove(0); //remove header record

        this.records = new String[allCSVRecords.size()][4]; /*name,username,password,passkey */

        int recordCount = 0;
            for (CSVRecord record : allCSVRecords){
                for (int i=0; i<record.size(); i++){
                    String data = record.get(i);
                    records[recordCount][i] = data;
                    if (i == 2){ //index 2 is the password column
                        Encryption encryption = new Encryption(data, record.get(i+1));
                        records[recordCount][i] = encryption.getPassword();
                        records[recordCount][i+1] = encryption.getKey();
                        break; 
                    }
                }
                recordCount++;
            }

        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public String[][] getRecords(){
        /**
         * returns records
         * 
         * @param none
         * @return records : String[][] records from CSV file
         */
        return this.records;
    }
}
