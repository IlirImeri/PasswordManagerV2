package com.company;

import java.sql.*;
import java.util.UUID;

public class UserDatabase {
    private Connection c = null;
    private Statement stmt = null;
    private String UserName;
    private String Password;
    private String ID;

    String sql = "INSERT INTO USERS (UserName,Password, ID) " +
            "VALUES (?, ?, ?)";

    public UserDatabase(String userName, String password) {
        UserName = userName;
        Password = password;
    }

    public String getID() {
        return ID;
    }

    public UserDatabase() {

    }

    public String generateID(){
        ID = String.valueOf(UUID.randomUUID());
        return ID;
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
                    " Password           TEXT     NOT NULL," +
                    "ID                  TEXT     NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public boolean isEmpty(){
        boolean isEmpty = true;
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS");

            while ( rs.next() ) {
                UserName = rs.getString("UserName");
                Password = rs.getString("Password");
                ID = rs.getString("ID");
                isEmpty=false;
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return isEmpty;
    }

    public void insertData(String UserName, String Password, String ID){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.setString(3, ID);
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
                ID = rs.getString("ID");
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

    public String findPassword(String username){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS WHERE UserName LIKE '%" +username+ "%'");

            while ( rs.next() ) {
                UserName = rs.getString("UserName");
                Password = rs.getString("Password");
                ID = rs.getString("ID");
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

    public String selectID(String username){
        try {
            c=connect();
            stmt = c.createStatement();
            c.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS WHERE UserName LIKE '%" +username+ "%'");

            while ( rs.next() ) {
                UserName = rs.getString("UserName");
                Password = rs.getString("Password");
                ID = rs.getString("ID");
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return ID;
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
