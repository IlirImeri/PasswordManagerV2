package com.company;

import AES256.AES256;
import Validators.EmailValidator;
import Validators.WebsiteValidator;

import java.util.Scanner;

import static Validators.PasswordValidator.checkPassword;

public class LogInSuccessful {
    public static void logInSuccessful(Database database){
        int scelta;
        String username, password, email, website, search, ID, newPassword;
        Scanner input = new Scanner(System.in);


        do {
            System.out.println("-----------MENU-----------");
            System.out.println("\t1 - VIEW PASSWORDS");
            System.out.println("\t2 - ADD");
            System.out.println("\t3 - DELETE");
            System.out.println("\t4 - UPDATE PASSWORD");
            System.out.println("\t5 - SEARCH BY URL");
            System.out.println("\t6 - PASSWORD GENERATOR");
            System.out.println("\t0 - EXIT");
            System.out.println();
            System.out.println("Your choice?:");
            scelta = input.nextInt();
            System.out.println("--------------------------");
            switch(scelta) {
                case 1:
                    System.out.println("Database:");
                    System.out.println();
                    if (database.isEmpty())
                        System.out.println("There aren't any passwords saved");
                    database.printDatabase();
                    break;
                case 2:
                    System.out.print("Username:");
                    input.nextLine();
                    username = input.nextLine();
                    System.out.println("Do you want to generate a random password? (YES or NO)");
                    String risposta = input.nextLine();
                    String hashedPassword;
                    if (risposta.equalsIgnoreCase("yes")){
                        PasswordGenerator p = new PasswordGenerator();
                        p.generatePassword();
                        password = p.toString();
                        checkPassword(password);
                        hashedPassword = AES256.encrypt(password);
                        System.out.println("Password: " + password);
                    }else{
                        System.out.print("Password: ");
                        password = input.nextLine();
                        checkPassword(password);
                        hashedPassword = AES256.encrypt(password);
                    }

                    System.out.print("Email: ");
                    email = input.nextLine();
                    if (!EmailValidator.isValid(email)){
                        do {
                            System.out.println("Please insert a valid email");
                            email = input.nextLine();
                        }while (!EmailValidator.isValid(email));
                    }

                    System.out.print("Website:");
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
                    if(!database.isEmpty()){
                        input.nextLine();
                        System.out.println("ID of the password you want to delete");
                        search = input.nextLine();
                        database.deleteData(search);
                    }else
                        System.out.println("Empty Database");
                    break;
                case 4:
                    if (!database.isEmpty()) {
                        System.out.println("New Password:");
                        input.nextLine();
                        newPassword = input.nextLine();
                        hashedPassword = AES256.encrypt(newPassword);
                        System.out.println("Row ID:");
                        ID = input.nextLine();
                        database.updateData(hashedPassword, ID);
                    }else
                        System.out.println("Empty Database");
                    break;
                case 5:
                    if (!database.isEmpty()) {
                        System.out.println("Search by URL:");
                        input.nextLine();
                        search = input.nextLine();
                        database.selectData(search);
                    }else
                        System.out.println("Empty Database");
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
