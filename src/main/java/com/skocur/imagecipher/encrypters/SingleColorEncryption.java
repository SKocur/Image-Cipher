package com.skocur.imagecipher.encrypters;

import com.skocur.imagecipher.Config;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SingleColorEncryption extends Encrypter {

    private ArrayList<Character> chars;
    private ArrayList<Integer> asciiChars;

    public SingleColorEncryption(String fileName) throws IOException {
        super(fileName);

        chars = new ArrayList<>();
        asciiChars = new ArrayList<>();
    }

    @Override
    public void encrypt(String text) {
        for (char c : text.toCharArray()) {
            asciiChars.add((int) c);
        }

        for (int i = 0, index = 0; i < getImageWidth(); i++) {
            if (i % Config.SPACING_CIPHER == 0 && index < asciiChars.size()) {
                Color greenShade = new Color(1, asciiChars.get(index), 1);
                image.setRGB(i, Config.IMAGE_MARGIN_TOP, greenShade.getRGB());
                index++;
            }
        }

        saveEncryptedData();
    }
}
