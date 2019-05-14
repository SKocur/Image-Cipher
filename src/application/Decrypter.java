package application;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * <h1>Decrypter</h1>
 * <br>
 * This class is responsible for decrypting data.
 *
 * @author Szymon Kocur
 */
public class Decrypter {

    private int row;
    private int col;
    private int tempBitVal;
    private BufferedImage originalImage;

    public Decrypter(String fileName) throws IOException {
        this.originalImage = ImageIO.read(new File(fileName));
    }

    /**
     * This method decrypt text message from given image.
     * It reads level of shadow on green channel from pixel.
     *
     * @param fileName Name of the image
     * @return text Decrypted text
     * @throws IOException When file cannot be found.
     * @see Config
     */
    @Deprecated
    public static String decrypt(String fileName) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(fileName));

        int width = bufferedImage.getWidth();
        StringBuilder stringBuilder = new StringBuilder();

        for (int pixel = 0; pixel < width; pixel++) {
            if (pixel % Config.SPACING_CIPHER == 0) {
                Color colorShade = new Color(bufferedImage.getRGB(pixel, Config.IMAGE_MARGIN_TOP), true);
                stringBuilder.append((char) colorShade.getGreen());
            }
        }

        return stringBuilder.toString();
    }

    @Deprecated
    public static String decryptBlue(String fileName) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(fileName));

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int j = 0; j < bufferedImage.getWidth(); ++j) {
                int b = bufferedImage.getRGB(j, i) & 0b11111111;

                stringBuilder.append((char) b);
            }
        }

        return stringBuilder.toString();
    }

    public String decryptLowLevelBits() {
        StringBuilder stringBuilder = new StringBuilder();

        while (col < originalImage.getWidth() - 1 && row < originalImage.getHeight()) {
            char c = 0;

            for (int i = 0; i < 8; ++i) {
                decryptBitCharacter();

                c <<= 1;
                c |= tempBitVal;
            }

            c = (char) (Integer.reverse(c << 24) & 0b11111111);

            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    private void decryptBitCharacter() {
        if (col < originalImage.getWidth() - 1) {
            int mask = 0b11111111;
            int rgb = originalImage.getRGB(col, row);

            int b = rgb & mask;

            int newB = originalImage.getRGB(col + 1, row) & mask;

            tempBitVal = Math.abs(newB - b);

            col += 2;
        } else {
            row++;
            col = 0;
        }
    }
}
