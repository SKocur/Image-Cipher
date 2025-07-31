package com.skocur.imagecipher.tools.imageprocessing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ColorFilterTest {

    private String imagePath = "src/test/resources/images/test_1";
    private File imageFile;

    @Test
    void shouldManuallySaveRedImageNextToOldOne() throws IOException {
        // given
        FilteringColorMode red = FilteringColorMode.RED;

        // when
        ColorFilter.saveColorData(imageFile.getAbsolutePath(),
                "red",
                ColorFilter.getColorOf(imageFile, red)
        );

        // then
        assertTrue(new File(imagePath + "_red.png").exists());
    }

    @Test
    void shouldManuallySaveGreenImageNextToOldOne() throws IOException {
        // given
        FilteringColorMode green = FilteringColorMode.GREEN;

        // when
        ColorFilter.saveColorData(imageFile.getAbsolutePath(),
                "green",
                ColorFilter.getColorOf(imageFile, green)
        );

        // then
        assertTrue(new File(imagePath + "_green.png").exists());
    }

    @Test
    void shouldManuallySaveBlueImageNextToOldOne() throws IOException {
        // given
        FilteringColorMode blue = FilteringColorMode.BLUE;

        // when
        ColorFilter.saveColorData(imageFile.getAbsolutePath(),
                "blue",
                ColorFilter.getColorOf(imageFile, blue)
        );

        // then
        assertTrue(new File(imagePath + "_blue.png").exists());
    }

    @Test
    void shouldAutomaticallySaveRedImageNextToOldOne() throws IOException {
        // given
        FilteringColorMode red = FilteringColorMode.RED;

        // when
        ColorFilter.getColorAndSave(imageFile, red);

        // then
        assertTrue(new File(imagePath + "_red.png").exists());
    }

    @Test
    void shouldAutomaticallySaveGreenImageNextToOldOne() throws IOException {
        // given
        FilteringColorMode green = FilteringColorMode.GREEN;

        // when
        ColorFilter.getColorAndSave(imageFile, green);

        // then
        assertTrue(new File(imagePath + "_green.png").exists());
    }

    @Test
    void shouldAutomaticallySaveBlueImageNextToOldOne() throws IOException {
        // given
        FilteringColorMode blue = FilteringColorMode.BLUE;

        // when
        ColorFilter.getColorAndSave(imageFile, blue);

        // then
        assertTrue(new File(imagePath + "_blue.png").exists());
    }

    @Test
    void shouldThrowIOExceptionWhenGettingColorFromWrongImage() {
        // when & then
        assertThrows(IOException.class, () -> {
            ColorFilter.getColorOf(new File(imagePath + "__.jpeg"), FilteringColorMode.RED);
        });
    }

    @BeforeEach
    void setup() {
        imageFile = new File(imagePath + ".png");
    }

    @AfterEach
    void cleanup() {
        new File(imagePath + "_red.png").delete();
        new File(imagePath + "_green.png").delete();
        new File(imagePath + "_blue.png").delete();
    }
}
