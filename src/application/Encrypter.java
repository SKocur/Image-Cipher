package application;

import java.awt.Color;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Encrypter {
	static int width;
	String fileName;

	ArrayList<Character> chars;
	ArrayList<Integer> asciiChars;
	BufferedImage originalImage;

	File file;

	Encrypter(String fileName) throws IOException {
		chars = new ArrayList<Character>();
		asciiChars = new ArrayList<Integer>();
		originalImage = ImageIO.read(new File(fileName));

		file = null;
		this.width = originalImage.getWidth();
	}

	public void encrypt(String text) {
		int index = 0;

		for (char c : text.toCharArray())
			chars.add(c);

		for(char c : chars)
			asciiChars.add((int) c);

		for(int x = 0; x < width; x++){
			if(x % Config.SPACING_CIPHER == 0 && index < asciiChars.size()){
				Color greenShade = new Color(1, asciiChars.get(index), 1);
				originalImage.setRGB(x, Config.IMAGE_MARGIN_TOP, greenShade.getRGB());
				index++;
			}
		}

		try{
			file = new File("output.png");
			ImageIO.write(originalImage, "png", file);
		}catch(IOException e){
			System.out.println("Error: " + e);
		}
	}

	public int getImageWidth() {
		return this.width;
	}
}

