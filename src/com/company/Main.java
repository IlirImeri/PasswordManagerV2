package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
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
                    System.out.println("Password:");
                    password = input.nextLine();
                    System.out.println("Email:");
                    email = input.nextLine();
                    System.out.println("Website:");
                    website = input.nextLine();
                    database.insertData(username,password,email,website);
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
                    System.out.println("ID del row:");
                    ID = input.nextLine();
                    database.updateData(newPassword,ID);
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
                    break;
                default:
            }
        } while (scelta != 0);
    }
}