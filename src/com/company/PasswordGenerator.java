package com.company;

import java.util.Iterator;
import java.util.LinkedList;

public class PasswordGenerator {
    private LinkedList<Character> password = new LinkedList<Character>();
    private static final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    private static final String digits = "0123456789";
    private static final String punctuation = "!@#&()–[{}]:;',?*";
    private static final String symbols = "~$^+=<>";

    public void generatePassword(){
        for (int i = 0; i <=4 ; i++) {
            int temp = i;
            i = (int)(Math.random()*(lowerCase.length()-2+1)+1);
            password.add(lowerCase.charAt(i));
            i=temp;
        }

        for (int i = 0; i <=3 ; i++) {
            int temp = i;
            i = (int)(Math.random()*(lowerCase.length()-2+1)+1);
            password.add(lowerCase.toUpperCase().charAt(i));
            i=temp;
        }

        for (int i = 0; i <=3 ; i++) {
            int temp = i;
            i = (int)(Math.random()*(punctuation.length()-2+1)+1);
            password.add(punctuation.charAt(i));
            i=temp;
        }

        for (int i = 0; i <=3 ; i++) {
            int temp = i;
            i = (int)(Math.random()*(symbols.length()-2+1)+1);
            password.add(symbols.charAt(i));
            i=temp;
        }

        for (int i = 0; i <=2 ; i++) {
            int temp = i;
            i = (int)(Math.random()*(digits.length()-2+1)+1);
            password.add(digits.charAt(i));
            i=temp;
        }
    }

    @Override
    public String toString() {
        Iterator<Character> iterator = password.iterator();
        String pass = "";
        while (iterator.hasNext()) {
            pass = pass + iterator.next();
        }
        pass=pass.replaceAll(" ", "");
        return pass;
    }
}
