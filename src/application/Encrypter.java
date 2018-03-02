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
 *
 */
public class Encrypter {
	static int width;
	String fileName;

	ArrayList<Character> chars;
	ArrayList<Integer> asciiChars;
	BufferedImage originalImage;

	File file;

	/**
	 * Here all common variables for this class are initialized.
	 *
	 * @param fileName Name of file which will be destination of encrypted message
	 * @throws IOException
	 */
	Encrypter(String fileName) throws IOException {
		chars = new ArrayList<Character>();
		asciiChars = new ArrayList<Integer>();
		originalImage = ImageIO.read(new File(fileName));

		file = null;
		this.width = originalImage.getWidth();
	}

	/**
	 * It encrypts data into green channel of pixel using configuration from Config class.
	 *
	 * @param text Text message to encrypt
	 * @see Config
	 */
	public void encrypt(String text) {
		int index = 0;

		for (char c : text.toCharArray())
			chars.add(c);

		for (char c : chars)
			asciiChars.add((int) c);

		for (int x = 0; x < width; x++) {
			if (x % Config.SPACING_CIPHER == 0 && index < asciiChars.size()) {
				Color greenShade = new Color(1, asciiChars.get(index), 1);
				originalImage.setRGB(x, Config.IMAGE_MARGIN_TOP, greenShade.getRGB());
				index++;
			}
		}

		try {
			file = new File("output.png");
			ImageIO.write(originalImage, "png", file);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}

	public int getImageWidth() {
		return this.width;
	}
}

