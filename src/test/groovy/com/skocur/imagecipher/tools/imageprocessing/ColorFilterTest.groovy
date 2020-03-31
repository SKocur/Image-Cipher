package com.skocur.imagecipher.tools.imageprocessing

import spock.lang.Specification

class ColorFilterTest extends Specification {

    String imagePath = "src/test/resources/images/test_1"
    File imageFile

    def "red image should be manually saved next to old one"() {
        given:
        FilteringColorMode red = FilteringColorMode.RED

        when:
        ColorFilter.saveColorData(imageFile.getAbsolutePath(),
                "red",
                ColorFilter.getColorOf(imageFile, red)
        )

        then:
        new File(imagePath + "_red.png").exists()
    }

    def "green image should be manually saved next to old one"() {
        given:
        FilteringColorMode green = FilteringColorMode.GREEN

        when:
        ColorFilter.saveColorData(imageFile.getAbsolutePath(),
                "green",
                ColorFilter.getColorOf(imageFile, green)
        )

        then:
        new File(imagePath + "_green.png").exists()
    }

    def "blue image should be manually saved next to old one"() {
        given:
        FilteringColorMode blue = FilteringColorMode.BLUE

        when:
        ColorFilter.saveColorData(imageFile.getAbsolutePath(),
                "blue",
                ColorFilter.getColorOf(imageFile, blue)
        )

        then:
        new File(imagePath + "_blue.png").exists()
    }

    def "red image should be automatically saved next to old one"() {
        given:
        FilteringColorMode red = FilteringColorMode.RED

        when:
        ColorFilter.getColorAndSave(imageFile, red)

        then:
        new File(imagePath + "_red.png").exists()
    }

    def "green image should be automatically saved next to old one"() {
        given:
        FilteringColorMode green = FilteringColorMode.GREEN

        when:
        ColorFilter.getColorAndSave(imageFile, green)

        then:
        new File(imagePath + "_green.png").exists()
    }

    def "blue image should be automatically saved next to old one"() {
        given:
        FilteringColorMode blue = FilteringColorMode.BLUE

        when:
        ColorFilter.getColorAndSave(imageFile, blue)

        then:
        new File(imagePath + "_blue.png").exists()
    }

    def "getting color from wrong image should occur in IOException"() {
        when:
        ColorFilter.getColorOf(new File(imagePath + "__.jpeg"), FilteringColorMode.RED)

        then:
        thrown IOException
    }

    def setup() {
        imageFile = new File(imagePath + ".png")
    }

    def cleanup() {
        new File(imagePath + "_red.png").delete()
        new File(imagePath + "_green.png").delete()
        new File(imagePath + "_blue.png").delete()
    }
}
