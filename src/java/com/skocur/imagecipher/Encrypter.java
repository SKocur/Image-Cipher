package com.skocur.imagecipher;

import java.awt.Color;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * <h1>Encrypter</h1>
 * <br>
 * This class is responsible for encrypting text message into image.
 *
 * @author Szymon Kocur
 */
public class Encrypter {

    private int width;
    private ArrayList<Character> chars;
    private ArrayList<Integer> asciiChars;
    private BufferedImage originalImage;
    private String fileName;
    private int row;
    private int col;
    private int tempBitVal;

    /**
     * Here all common variables for this class are initialized.
     *
     * @param fileName Name of file which will be destination of encrypted message
     * @throws IOException
     */
    Encrypter(String fileName) throws IOException {
        chars = new ArrayList<>();
        asciiChars = new ArrayList<>();
        originalImage = ImageIO.read(new File(fileName));

        this.fileName = fileName;
        this.width = originalImage.getWidth();
    }

    /**
     * It encrypts data into green channel of pixel using configuration from Config class.
     *
     * @param text Text message to encrypt
     * @see Config
     */
    @Deprecated
    public void encrypt(String text) {
        for (char c : text.toCharArray()) {
            asciiChars.add((int) c);
        }

        for (int x = 0, index = 0; x < width; x++) {
            if (x % Config.SPACING_CIPHER == 0 && index < asciiChars.size()) {
                Color greenShade = new Color(1, asciiChars.get(index), 1);
                originalImage.setRGB(x, Config.IMAGE_MARGIN_TOP, greenShade.getRGB());
                index++;
            }
        }

        try {
            File file = new File(fileName);
            ImageIO.write(originalImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void simpleEncrypt(String text) {
        int index = 0;
        boolean hasDone = false;

        for (int i = 0; i < originalImage.getHeight() && !hasDone; ++i) {
            for (int j = 0; j < originalImage.getWidth() && !hasDone; ++j) {
                if (index < text.length()) {
                    Color color = encryptBlue(text.charAt(index++), j, i);

                    originalImage.setRGB(j, i, color.getRGB());
                } else {
                    hasDone = true;
                }
            }
        }

        try {
            File file = new File(fileName);
            ImageIO.write(originalImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    private Color encryptBlue(char character, int posX, int posY) {
        int argb = originalImage.getRGB(posX ,posY);

        int r = (argb >> 16) & 0b11111111;
        int g = (argb >> 8) & 0b11111111;

        int newB = 0;
        if (character < 255) {
            newB = character & 0b11111111;
        }

        return new Color(r, g, newB);
    }

    public void encryptLowLevelBits(String text) {
        for (char c : text.toCharArray()) {
            encryptByte(c);
        }

        try {
            File file = new File(fileName);
            ImageIO.write(originalImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
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
        if (col < originalImage.getWidth() - 1) {
            int mask = 0b11111111;
            int rgb = originalImage.getRGB(col, row);
            int r = (rgb >> 16) & mask;
            int g = (rgb >> 8) & mask;

            int b = originalImage.getRGB(col + 1, row) & mask;
            b -= c;

            Color color = new Color(r, g, b);
            originalImage.setRGB(col, row, color.getRGB());

            col += 2;
        } else {
            row++;
            col = 0;
        }
    }


    public int getImageWidth() {
        return this.width;
    }
}

