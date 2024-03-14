package EncryptionTest;
import java.sql.*;
public class SQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USER = "root";
    private static Connection connection;
    private static Statement stmt;
    SQLConnection(String password) {
        try 
            {
                connection = DriverManager.getConnection(URL, USER, password);
                stmt = connection.createStatement();
                System.out.println("Successfully Connected to Database");
                if (!(doesDatabaseExist())){
                    setupDatabase();
                }
            } catch (SQLException e){
             System.out.println(e);
        }
    }
    public static Connection getConnection() {
        return connection;
    }
    private static boolean doesDatabaseExist() throws SQLException{
        String query = "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'cipherX'";
        ResultSet resultSet = stmt.executeQuery(query);
        return resultSet.next();

    }
    private static void setupDatabase() throws SQLException {
        String query = "CREATE DATABASE cipherX";
        stmt.executeUpdate(query);
        query = "USE cipherX";
        stmt.executeUpdate(query);
        query = "CREATE TABLE passwords (tag_name VARCHAR(100), password VARCHAR(255), passKey VARCHAR(255))";
        stmt.executeUpdate(query);     
    }
    private static void changeRootPassword() throws SQLException{
        String query = "ALTER USER 'root'@'localhost' IDENTIFIED BY _______"; //_______ is new root password
        stmt.executeUpdate(query);
    }
    private static ResultSet getRows() throws SQLException{
        String query = "USE cipherX";
        stmt.executeUpdate(query);
        query = "SELECT * FROM passwords";
        ResultSet rows = stmt.executeQuery(query);
        return rows;
    }
    public static int getRowCount() throws SQLException{
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
}
