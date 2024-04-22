package CipherX;
import java.io.*;
import java.nio.charset.Charset;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.util.*;
import org.apache.commons.csv.*;

public class FileScreen extends JFileChooser{
    private static final String FILE_NAME = "cipherxPasswords.csv";
    private static final String FILE_TYPE = ".csv";
    private static final String FILE_DESCRIPTOR = "Comma Seperated Values File (.csv)";
    private static final int FILE_FORMAT_ERROR = 1;
    private static final int FILE_SCREEN_CLOSED_ERROR = 2;
    private int returnVal = 0;
    private FileFilter fileFilter;
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
        setFileFilter();
        if (records == null){
            importFile();
        } else {
            exportFile(records);
        }
    }
    private void setFileFilter(){
        /**
         * sets up fileFilter to only accept FILE_TYPE
         * 
         * @param none
         * @return none
         */
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(FILE_TYPE) && f.isFile();
            }

            @Override
            public String getDescription() {
                return FILE_DESCRIPTOR;
            }
            
        };
        this.fileFilter = fileFilter;
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

        try {
            File outputFile = this.getSelectedFile();
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

        this.setFileFilter(fileFilter);
        this.showOpenDialog(parent);
        File file = this.getSelectedFile();

        if (file == null){
            this.returnVal = FILE_SCREEN_CLOSED_ERROR;
            return;
        }
        if (!fileFilter.accept(file)){
            this.returnVal = FILE_FORMAT_ERROR;
            return;
        }
        
        CSVParser csvParser = CSVParser.parse(file, Charset.forName("UTF-8") ,CSVFormat.EXCEL);
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

        } catch (Exception e){
            e.printStackTrace();
            this.returnVal = FILE_FORMAT_ERROR;
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
    public static void alertBox(int error, JFrame parent){
        /**
         * pops up a JOptionPane to notify user an error has occured
         * 
         * @param error : int error type represented as an int
         * @param parent : JFrame to display JOptionPane
         * 
         * @return none
         */
        String outputString = "";
        int errorMsg = JOptionPane.ERROR_MESSAGE;;
        switch (error) {
            case FILE_SCREEN_CLOSED_ERROR:
            outputString = "Please enter a CSV (Comma Seperated Value) file";
            errorMsg = JOptionPane.WARNING_MESSAGE;
                break;
            case FILE_FORMAT_ERROR:
            outputString = "Please check the formatting of your file";
            errorMsg = JOptionPane.WARNING_MESSAGE;
                break;
            default:
            outputString = "An Error Occurred";
                break;
        }
        JOptionPane.showMessageDialog(parent, outputString, "Import Error", errorMsg);
    }
    public int getReturnStatus(){
        /**
         * returns the status of importing the file, needed for error checking
         * 
         * @param none
         * @return returnVal : int status of import, represented as an integer
         */
        return returnVal;
    }
}
