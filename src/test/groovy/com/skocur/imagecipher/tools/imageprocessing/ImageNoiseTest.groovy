package com.skocur.imagecipher.tools.imageprocessing

import spock.lang.Specification

class ImageNoiseTest extends Specification {

    String imagePath = "src/test/resources/images/test_1"
    ImageNoise imageNoise

    def "noise image should be saved"() {
        when:
        imageNoise.saveNoiseImage(imageNoise.createRandomNoise())

        then:
        new File(imagePath + ".png").exists()
    }

    def "IOException should happen, because of wrong file name"() {
        when:
        new ImageNoise(imagePath + ".jpeg")

        then:
        thrown IOException
    }

    def setup() {
        imageNoise = new ImageNoise(imagePath + ".png")
    }

    def cleanup() {
        new File(imagePath + "_noise.png").delete()
    }
}
