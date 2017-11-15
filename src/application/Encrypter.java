package application;

import java.awt.Color;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Encrypter {
	public static void encrypt(String fileName, String text) throws IOException {
		int width = 0;
		int index = 0;

		File f = null;

		ArrayList<Character> chars = new ArrayList<Character>();
		ArrayList<Integer> asciiChars = new ArrayList<Integer>();
		BufferedImage originalImage = ImageIO.read(new File(fileName));

		for (char c : text.toCharArray())
			chars.add(c);

		for(char c : chars)
			asciiChars.add(Character.getNumericValue(c));

		width = originalImage.getWidth();

		for(int x = 0; x < width; x++){
			if(x % Config.SPACING_CIPHER == 0 && index < asciiChars.size()){
				int iCharacter = asciiChars.get(index) + Config.COLOR_BOOST;
				Color greenShade = new Color(1, iCharacter, 1);
				originalImage.setRGB(x, Config.IMAGE_MARGIN_TOP, greenShade.getRGB());
				index++;
			}
		}

		try{
			f = new File("output.png");
			ImageIO.write(originalImage, "png", f);
		}catch(IOException e){
			System.out.println("Error: " + e);
		}
	}
}

