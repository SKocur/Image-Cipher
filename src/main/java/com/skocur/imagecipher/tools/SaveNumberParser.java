package com.skocur.imagecipher.tools;

import org.jetbrains.annotations.Nullable;

public class SaveNumberParser {

    public static int getParsedNumber(@Nullable String text) {
        int res;
        try {
            res = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }

        return res;
    }
}
