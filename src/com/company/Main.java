package com.company;

import AES256.AES256;
import BCrypt.BCrypt;
import Validators.EmailValidator;
import Validators.WebsiteValidator;

import java.util.Scanner;

import static Validators.PasswordValidator.checkPassword;


public class Main {

    public static void main(String[] args) {

        UserDatabase user = new UserDatabase();
        user.createTable();
        String loginUsername, loginPassword;
        Scanner logOrSign = new Scanner(System.in);
        Scanner loginCredentials = new Scanner(System.in);

        System.out.println("Log In (1)");
        System.out.println("Sign Up (2)");
        String in =  logOrSign.nextLine();
        if (!in.equals("1") && !in.equals("2")){
            do {
                System.out.println("Please chose a valid option !!!");
                in = logOrSign.nextLine();
            }while (!in.equals("1") && !in.equals("2"));
        }

        if (in.equals("1")){
            System.out.println("Username: ");
            loginUsername = loginCredentials.nextLine();
            System.out.println("Password: ");
            loginPassword = loginCredentials.nextLine();
            while(!user.searchUsers(loginUsername)) {
                System.out.println("User doesn't exist");
                System.out.println("Don't have an account ? (1)");
                System.out.println("Try again (0)");
                loginUsername = loginCredentials.nextLine();
                if (loginUsername.equals("1")){
                    System.out.println("Crea Username");
                    loginUsername = loginCredentials.nextLine();
                    System.out.println("Inserisci Password");
                    loginPassword = loginCredentials.nextLine();
                    String hashedPassword = BCrypt.hashpw(loginPassword, BCrypt.gensalt(12));
                    user.insertData(loginUsername, hashedPassword);
                    System.out.println("User created successfully");
                }else if (loginUsername.equals("0")) {
                    System.out.println("Inserisci Username");
                    loginUsername = loginCredentials.nextLine();
                    System.out.println("Password: ");
                    loginPassword = loginCredentials.nextLine();
                }
            }
            while (!BCrypt.checkpw(loginPassword, user.selectData(loginUsername))){
                System.err.println("Wrong Username or Password");
                System.out.println("Enter Username");
                loginUsername = loginCredentials.nextLine();
                System.out.println("Enter Password");
                loginPassword = loginCredentials.nextLine();
            }
        }else{
            System.out.println("Crea Username");
            loginUsername = loginCredentials.nextLine();
            while(user.searchUsers(loginUsername)) {
                System.out.println("Username already exists");
                System.out.println("Inserisci Username");
                loginUsername = loginCredentials.nextLine();
            }
            System.out.println("Inserisci Password");
            loginPassword = loginCredentials.nextLine();
            String hashedPassword = BCrypt.hashpw(loginPassword, BCrypt.gensalt(12));
            user.insertData(loginUsername, hashedPassword);
        }

        if (BCrypt.checkpw(loginPassword, user.selectData(loginUsername))){
            int scelta;
            String username, password, email, website, search, ID, newPassword;
            Scanner input = new Scanner(System.in);
            Database database = new Database();

            database.createTable();

            do {
                System.out.println("CIAO - Cosa vuoi fare?");
                System.out.println("\t1 - Visualizza valori del database");
                System.out.println("\t2 - Inserire valori nel database");
                System.out.println("\t3 - Rimuovere valori dal database");
                System.out.println("\t4 - Aggiornare valori dal database");
                System.out.println("\t5 - Cercare valori nel database");
                System.out.println("\t6 - Generate password");
                System.out.println("\t0 - Uscita");
                System.out.println();
                System.out.println("Scelta:");
                scelta = input.nextInt();
                switch(scelta) {
                    case 1:
                        System.out.println("Valori del database:");
                        System.out.println();
                        database.printDatabase();
                        break;
                    case 2:
                        System.out.println("Username:");
                        input.nextLine();
                        username = input.nextLine();
                        System.out.println("Vuoi generare una password casuale? (SI o NO)");
                        String risposta = input.nextLine();
                        String hashedPassword;
                        if (risposta.equalsIgnoreCase("si")){
                            PasswordGenerator p = new PasswordGenerator();
                            p.generatePassword();
                            password = p.toString();
                            checkPassword(password);
                            hashedPassword = AES256.encrypt(password);
                            System.out.println("Password: " + password);
                        }else{
                            System.out.println("Password:");
                            password = input.nextLine();
                            checkPassword(password);
                            hashedPassword = AES256.encrypt(password);
                        }

                        System.out.println("Email:");
                        email = input.nextLine();
                        if (!EmailValidator.isValid(email)){
                            do {
                                System.out.println("Please insert a valid email");
                                email = input.nextLine();
                            }while (!EmailValidator.isValid(email));
                        }

                        System.out.println("Website:");
                        website = input.nextLine();
                        if (!WebsiteValidator.isValid(website)){
                            do {
                                System.out.println("Please insert a valid URL");
                                website = input.nextLine();
                            }while (!WebsiteValidator.isValid(website));
                        }

                        database.insertData(username,hashedPassword,email.toLowerCase(),website);
                        break;
                    case 3:
                        input.nextLine();
                        System.out.println("ID della riga che vuoi cancellare");
                        search = input.nextLine();
                        database.deleteData(search);
                        break;
                    case 4:
                        System.out.println("Nuova password:");
                        input.nextLine();
                        newPassword = input.nextLine();
                        hashedPassword = AES256.encrypt(newPassword);
                        System.out.println("ID del row:");
                        ID = input.nextLine();
                        database.updateData(hashedPassword,ID);
                        break;
                    case 5:
                        System.out.println("Ricerca:");
                        input.nextLine();
                        search = input.nextLine();
                        database.selectData(search);
                        break;
                    case 6:
                        PasswordGenerator p = new PasswordGenerator();
                        p.generatePassword();
                        System.out.println(p);
                        checkPassword(p.toString());
                        break;
                    default:
                }
            } while (scelta != 0);
        }
    }
}