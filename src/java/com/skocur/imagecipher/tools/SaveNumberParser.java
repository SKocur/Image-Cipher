package com.skocur.imagecipher.tools;

public class SaveNumberParser {

    public static int getParsedNumber(String text) {
        int res;
        try {
            res = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }

        return res;
    }
}
