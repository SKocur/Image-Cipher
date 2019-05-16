package com.skocur.imagecipher.encrypters;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LowLevelBitEncrypter extends Encrypter {

    private int row;
    private int col;

    public LowLevelBitEncrypter(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void encrypt(String text) {
        for (char c : text.toCharArray()) {
            encryptByte(c);
        }

        // Resetting values of cursor coordinates
        col = 0;
        row = 0;
    }


    private void encryptByte(char character) {
        for (int i = 0; i < 8; ++i) {
            encryptBitCharacter((character >> i) & 0b1);
        }
    }

    private void encryptBitCharacter(int c) {
        if (col < image.getWidth() - 1) {
            int mask = 0b11111111;
            int rgb = image.getRGB(col, row);
            int r = (rgb >> 16) & mask;
            int g = (rgb >> 8) & mask;

            int b = image.getRGB(col + 1, row) & mask;
            b -= c;

            Color color = new Color(r, g, b);
            image.setRGB(col, row, color.getRGB());

            col += 2;
        } else {
            row++;
            col = 0;
        }
    }
}
