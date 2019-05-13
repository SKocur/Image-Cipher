package application;

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

    public void encryptBitwise(String text) {
        int index = 0;
        boolean hasDone = false;

        for (int i = 0; i < originalImage.getHeight() && !hasDone; ++i) {
            for (int j = 0; j < originalImage.getWidth() && !hasDone; ++j) {
                if (index < text.length()) {
                    Color color = encryptBlue(text.charAt(index++));

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

    public Color encryptBlue(char character) {
        int argb = originalImage.getRGB(123 ,13);

        int r = (argb >> 16) & 0b11111111;
        int g = (argb >> 8) & 0b11111111;

        int newB = 0;

        if (character < 255) {
            newB = character & 0b11111111;
        }

        return new Color(r, g, newB);
    }

    public int getImageWidth() {
        return this.width;
    }
}

