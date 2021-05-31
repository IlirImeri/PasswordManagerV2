package com.company;

import java.sql.*;
import java.util.UUID;

public class UserDatabase {
    private Connection c = null;
    private Statement stmt = null;
    private String UserName;
    private String Password;
    private String ID;

    String sql = "INSERT INTO USERS (UserName,Password) " +
            "VALUES (?, ?)";

    public UserDatabase(String userName, String password) {
        UserName = userName;
        Password = password;
    }

    public UserDatabase() {

    }

    public Connection connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:users.db");
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
            String sql = "CREATE TABLE IF NOT EXISTS USERS " +
                    "(UserName           TEXT     NOT NULL," +
                    " Password           TEXT     NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void insertData(String UserName, String Password){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.executeUpdate();
            ps.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public boolean searchUsers (String username){
        boolean isFound=false;
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS WHERE UserName LIKE '%" +username+ "%'");

            while ( rs.next() ) {
                UserName = rs.getString("UserName");
                Password = rs.getString("Password");
                if(UserName.equals(username)){
                    isFound = true;
                }
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return isFound;
    }

    public String selectData(String username){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS WHERE UserName LIKE '%" +username+ "%'");

            while ( rs.next() ) {
                UserName = rs.getString("UserName");
                Password = rs.getString("Password");
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return Password;
    }

    public void deleteData(String user){
        String sql3 = "DELETE FROM USERS WHERE UserName LIKE '%" +user+ "%'";
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

            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS");

            while ( rs.next() ) {
                UserName = rs.getString("UserName");
                Password = rs.getString("Password");

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

    public void updateData(String newPassword, String user){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);
            PreparedStatement ps = null;
            String sql = "UPDATE USERS set password = ? WHERE UserName = ? ";

            ps = c.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, user);
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
                "Password: " + Password);
    }
}
