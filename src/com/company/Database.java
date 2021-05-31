package com.company;

import AES256.AES256;

import java.sql.*;
import java.util.UUID;

public class Database {
    private Connection c = null;
    private Statement stmt = null;
    private String UserName;
    private String Password;
    private String Email;
    private String Website;
    private String ID;

    public String generateID(){
        ID = String.valueOf(UUID.randomUUID());
        return ID;
    }

    public Connection connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return c;
    }

    public void createTable(){
        try {
            c=connect();
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS PasswordManager " +
                    "(UserName           TEXT     NOT NULL," +
                    " Password           TEXT     NOT NULL," +
                    " Email              TEXT     NOT NULL," +
                    " Website            TEXT     NOT NULL," +
                    " ID                 TEXT     NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void insertData(String UserName, String Password, String Email, String Website){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);
            String sql = "INSERT INTO PasswordManager (UserName,Password,Email,Website,ID) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.setString(3, Email);
            ps.setString(4, Website);
            ps.setString(5, generateID());
            ps.executeUpdate();
            ps.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void selectData(String selectByURL){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery( "SELECT * FROM PasswordManager WHERE Website LIKE '%" +selectByURL+ "%'");

            while ( rs.next() ) {
                UserName = rs.getString("UserName");
                Password = rs.getString("Password");
                Email = rs.getString("Email");
                Website = rs.getString("Website");
                ID = rs.getString("ID");
                printRow();
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void deleteData(String ID){
        String sql3 = "DELETE FROM PasswordManager WHERE ID LIKE '%" +ID+ "%'";
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            PreparedStatement pstmt = c.prepareStatement(sql3);
            pstmt.executeUpdate();

            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void printDatabase(){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery( "SELECT * FROM PasswordManager");

            while ( rs.next() ) {
                UserName = rs.getString("UserName");
                Password = rs.getString("Password");
                Email = rs.getString("Email");
                Website = rs.getString("Website");
                ID = rs.getString("ID");

                printRow();
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void updateData(String newPassword, String rowID){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);
            PreparedStatement ps = null;
            String sql = "UPDATE PasswordManager set password = ? WHERE ID = ? ";

            ps = c.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, rowID);
            ps.execute();
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void printRow() {
        System.out.println("UserName: " + UserName + "\n" +
                "Password: " + AES256.decrypt(Password) + "\n" +
                "Email: " + Email + "\n" +
                "Website: " + Website + "\n" +
                "ID: " + ID + "\n");
    }
}