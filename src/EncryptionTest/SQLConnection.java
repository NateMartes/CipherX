package EncryptionTest;
import java.sql.*;
public class SQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USER = "root";
    public Connection connection;
    private boolean connectionStatus = false;
    SQLConnection(String password) {
        try (Connection connection = DriverManager.getConnection(URL, USER, password);)
            {
                System.out.println("Successfully Connected to Database");
                this.connection = connection;
                this.connectionStatus = true;
                if (!(doesDatabaseExist())){
                    setupDatabase();
                }
            } catch (SQLException e){
             System.out.println(e);
        }
    }
    public boolean getConnectionStatus(){
        return connectionStatus;
    }
    private boolean doesDatabaseExist() throws SQLException{
        String query = "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'user'";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next();

    }
    private void setupDatabase() throws SQLException {
        String query = "CREATE DATABASE user";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
        query = "USE user";
        stmt.executeUpdate(query);
        query = "CREATE TABLE passwords (tag_name VARCHAR(100), password VARCHAR(255), passKey VARCHAR(255))";
        stmt.executeUpdate(query);   
        stmt.close();   
    }
    private void changeRootPassword() throws SQLException{
        String query = "ALTER USER 'root'@'localhost' IDENTIFIED BY _______"; //_______ is new root password
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }
    private ResultSet getRows() throws SQLException{
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM user";
        ResultSet rows = stmt.executeQuery(query);
        return rows;
    }
    public int getRowCount() throws SQLException{
        Statement stmt = connection.createStatement();
        String query = "SELECT COUNT(*) AS rowCount FROM user";
        ResultSet rows = stmt.executeQuery(query);
        int rowCount = 0;
        while (rows.next()){
            rowCount = rows.getInt("rowCount");
        }
        return rowCount;
    }
}
