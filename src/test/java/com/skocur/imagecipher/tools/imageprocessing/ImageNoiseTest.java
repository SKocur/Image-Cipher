package com.skocur.imagecipher.tools.imageprocessing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImageNoiseTest {

    private String imagePath = "src/test/resources/images/test_1";
    private ImageNoise imageNoise;

    @Test
    void shouldSaveNoiseImage() {
        // when
        imageNoise.saveNoiseImage(imageNoise.createRandomNoise());

        // then
        assertTrue(new File(imagePath + "_noise.png").exists());
    }

    @Test
    void shouldThrowIOExceptionBecauseOfWrongFileName() {
        // when & then
        assertThrows(IOException.class, () -> {
            new ImageNoise(imagePath + ".jpeg");
        });
    }

    @BeforeEach
    void setup() throws IOException {
        imageNoise = new ImageNoise(imagePath + ".png");
    }

    @AfterEach
    void cleanup() {
        new File(imagePath + "_noise.png").delete();
    }
}
