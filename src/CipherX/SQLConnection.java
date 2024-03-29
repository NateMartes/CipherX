package CipherX;
import java.sql.*;

/**
 * SQLConnection
 * 
 * contains all methods needed to communitcate with local CipherX MySQL databse
 */
public class SQLConnection {
    private static final String URL = "jdbc:mysql://localhost";
    private String USER;
    private Connection connection;
    private Statement stmt;
    SQLConnection(String user, String password) throws SQLException{
        /**
         * Connects to local database and checks if CipherX database exists
         * 
         * @param user : user to login to local MySQL database (normally 'root')
         * @param password: password to login to local MySQL database (this password is set during MySQL installation)
         * 
         * @return none
         */
        connection = DriverManager.getConnection(URL, user, password);
        stmt = connection.createStatement();
        this.USER = user;
        System.out.println("Successfully Connected to Database");
        if (!(databaseExist())){
            setupDatabase();
        }
    }
    private boolean databaseExist() throws SQLException{
        /**
         * Checks if 'cipherX' database exists
         * 
         * @param none
         * 
         * @return boolean, if 'cipherX' database exists return true, otherwise false
         */
        String query = "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'cipherX'";
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next();

    }
    private void setupDatabase() throws SQLException {
        /**
         * creates 'cipherX' database and all necessities
         * 
         * @param none
         * 
         * @return none
         */
        String query = "CREATE DATABASE cipherX";
        stmt.executeUpdate(query);
        query = "USE cipherX";
        stmt.executeUpdate(query);
        query = "CREATE TABLE passwords (tag_name VARCHAR(100), username VARCHAR(100) password VARCHAR(255), passKey VARCHAR(255))";
        stmt.executeUpdate(query);     
    }
    public boolean setRootPassword(String newPassword) throws SQLException{
        /**
         * change MySQL local password of logged in user (does not log out of database).
         * 
         * @param newPassword : String to overwirte old password
         * 
         * @return boolean if process was completed true; otherwise false
         */
        try {
            String query = "ALTER USER ?@'localhost' IDENTIFIED BY ?";
            PreparedStatement stmtPrepared = connection.prepareStatement(query);
            stmtPrepared.setString(1, USER);
            stmtPrepared.setString(2, newPassword);
            stmtPrepared.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }
    private ResultSet getRows() throws SQLException{
        /**
         * gets all rows from password table in 'cipherX' database.
         * 
         * @param none
         * 
         * @return rows : Result set containting all Rows from 'cipherX'
         */
        String query = "USE cipherX";
        stmt.executeUpdate(query);
        query = "SELECT * FROM passwords";
        ResultSet rows = stmt.executeQuery(query);
        return rows;
    }
    public String getColumnData(String column, int rowNumber) throws SQLException{
        /**
         * gets data from row and column in password table from 'cipherX' database.
         * 
         * @param column : String column name where method will get data
         * @param rowNumber : int rowNumber where method will get data
         * 
         * @return rowData : data at row[column]; if no data found, returns ""
         */
        ResultSet rows = getRows();
        int currentRow = 0;
        while (rows.next()){
            currentRow++;
            if (currentRow == rowNumber){
                return rows.getString(column);
            }
        }
        return "";
    }
    public boolean addRow(String tag_name, String username, String password, String passKey) throws SQLException{
        /**
         * adds row to password table in 'cipherX' database.
         * 
         * @param tag_name : String tag_name tied to password to be saved
         * @param password : String password to be saved
         * @param password : String key of password to be saved
         * 
         * @return boolean if process was completed true; otherwise false
         */
        try {
            String query = "USE cipherX";
            stmt.executeUpdate(query);
            query = "INSERT INTO passwords(tag_name, password, passKey) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtPrepared = connection.prepareStatement(query);
            stmtPrepared.setString(1, tag_name);
            stmtPrepared.setString(2, tag_name);
            stmtPrepared.setString(3, password);
            stmtPrepared.setString(4, passKey);
            stmtPrepared.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }
    public int getRowCount() throws SQLException{
        /**
         * counts rows in password table in 'cipherX' database
         * 
         * @param none
         * @return rowCount : total rows in password table
         */
        String query = "USE cipherX";
        stmt.executeUpdate(query);
        query = "SELECT COUNT(*) AS rowCount FROM passwords";
        ResultSet rows = stmt.executeQuery(query);
        int rowCount = 0;
        while (rows.next()){
            rowCount = rows.getInt("rowCount");
        }
        return rowCount;
    }

    public void exitDatabase() throws SQLException {
        /*
         * Closes the SQL connection (Zack)
         * 
         * @param none
         * @return none
         */
        connection.close();
        System.out.println("Successfully Terminated Connection to Database");
    }
}
