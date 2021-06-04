package com.company;
import BCrypt.BCrypt;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        UserDatabase user = new UserDatabase();
        user.createTable();
        String loginUsername, loginPassword;
        Scanner loginUser = new Scanner(System.in);
        Scanner loginPass = new Scanner(System.in);
        Database database = new Database();
        database.createTable();

        if (user.isEmpty()) {
            database.dropDatabase();
            System.out.println("WARNING (you can only do this once)");
            System.out.println("-----Remember your credentials-----");
            System.out.println("Sign Up");
            System.out.print("Username: ");
            loginUsername = loginUser.nextLine();
            System.out.print("Password: ");
            loginPassword = loginPass.nextLine();
            String ID = user.generateID();
            System.out.println("Save this ID in case you forget your password: " + ID);
            String hashedPassword = BCrypt.hashpw(loginPassword, BCrypt.gensalt(12));
            String hashedID =  BCrypt.hashpw(ID, BCrypt.gensalt(12));
            user.insertData(loginUsername, hashedPassword, hashedID);
        }else{
            System.out.println("Log In");
            System.out.print("Username: ");
            loginUsername = loginUser.nextLine();
            System.out.print("Password: ");
            loginPassword = loginPass.nextLine();
            while(!user.searchUsers(loginUsername)) {
                int cont = 0;
                while (!BCrypt.checkpw(loginPassword, user.findPassword(loginUsername))){
                    cont++;
                    System.out.println("Wrong Username or Password");
                    System.out.print("Enter Username: ");
                    loginUsername = loginUser.nextLine();
                    System.out.print("Enter Password: ");
                    loginPassword = loginPass.nextLine();
                    if (cont == 2 ) {
                        System.out.println("Forgot password? (YES or NO)");
                        Scanner inputLine = new Scanner(System.in);
                        String in = inputLine.nextLine();
                        if (in.equalsIgnoreCase("yes")) {
                            System.out.println("User ID: ");
                            String userInputID = inputLine.nextLine();
                            if (BCrypt.checkpw(userInputID, user.selectID(loginUsername))) {
                                System.out.println("Welcome " + loginUsername.toUpperCase());
                                LogInSuccessful.logInSuccessful(database);
                            }
                        }else if (in.equalsIgnoreCase("no")) {
                            cont=0;
                            System.out.print("Enter Username: ");
                            loginUsername = loginUser.nextLine();
                            System.out.print("Enter Password: ");
                            loginPassword = loginPass.nextLine();
                        }
                    }
                }
            }
        }

        if (BCrypt.checkpw(loginPassword, user.findPassword(loginUsername))){
            System.out.println("Welcome " + loginUsername.toUpperCase());
            LogInSuccessful.logInSuccessful(database);
        }
    }
}