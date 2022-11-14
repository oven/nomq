package no.kantega;

import static no.kantega.App.sleepRandom;

public class PasswordCracker {

    public static String crack(String input) {
        StringBuilder result = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            result.append(c);
        }

        sleepRandom(5000, 8000);

        return result.toString();
    }


}

