package com.company;

import AES256.AES256;
import dnl.utils.text.table.TextTable;

import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

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

                List<List<String>> rows = new ArrayList<>();
                List<String> headers = Arrays.asList("Username", "Password", "Email", "URL", "ID");
                rows.add(headers);
                rows.add(Arrays.asList(UserName, AES256.decrypt(Password), Email, Website, ID));
                System.out.println(formatAsTable(rows));
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

    public boolean isEmpty(){
        boolean isEmpty = true;
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

    public void dropDatabase(){
        String sql3 = "DELETE FROM PasswordManager";
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
                int rows, columns;
                Object[][] data = new Object[100][50];

                for (rows = 0; rows < 100 ; rows++) {
                    for (columns = 0; columns < 50; columns++) {
                        data[rows][columns] = rows + columns;
                    }
                }
                String[] columnNames = {"Username","Password","Email","URL","ID"};
                TextTable tt = new TextTable(columnNames,data);
                tt.printTable();
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

    private String formatAsTable(List<List<String>> rows){
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows)
        {
            for (int i = 0; i < row.size(); i++)
            {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths)
        {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (List<String> row : rows)
        {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }

}
