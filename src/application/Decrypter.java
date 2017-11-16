package application;

import java.awt.Color;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Decrypter {
	public static String decrypt(String fileName) throws IOException {
		  BufferedImage bufferedImage = ImageIO.read(new File(fileName));

		  int width = bufferedImage.getWidth();
		  String text = "";

		  ArrayList<Character> characters = new ArrayList<Character>();

		  for(int pixel = 0; pixel < width; pixel++){
			  if(pixel % Config.SPACING_CIPHER == 0){
				  Color colorShade = new Color(bufferedImage.getRGB(pixel, Config.IMAGE_MARGIN_TOP), true);
				  characters.add((char) colorShade.getGreen());
			  }
		  }

		  for(char character : characters)
			  text += character;

		  return text;
	}
}
