package application;

/**
 * <h1>Config</h1>
 * <br>
 * This class contains public constants used in other parts of Image Cipher.
 *
 * @author Szymon Kocur
 */
public class Config {

    // To prevent instantiating of this class
    private Config() {

    }

    public static final int IMAGE_MARGIN_TOP = 13;

    /*
     * Spacing between changed pixels is 13
     * AND BECAUSE OF IT
     * SPACING_CIPHER has to be equal to 14
     *
     * */
    public static final int SPACING_CIPHER = 14;
}
