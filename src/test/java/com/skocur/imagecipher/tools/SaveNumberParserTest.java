package com.skocur.imagecipher.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaveNumberParserTest {

    @Test
    void shouldReturnZeroForWrongInput() {
        // when & then
        assertEquals(0, SaveNumberParser.getParsedNumber("11x"));
        assertEquals(0, SaveNumberParser.getParsedNumber("x11"));
        assertEquals(0, SaveNumberParser.getParsedNumber("0x1221"));
        assertEquals(0, SaveNumberParser.getParsedNumber(""));
        assertEquals(0, SaveNumberParser.getParsedNumber("-"));
        assertEquals(0, SaveNumberParser.getParsedNumber(null));
        assertEquals(0, SaveNumberParser.getParsedNumber(SaveNumberParserTest.class.getName()));
    }

    @Test
    void shouldReturnParsedNumberForCorrectInput() {
        // when & then
        assertEquals(111, SaveNumberParser.getParsedNumber("111"));
        assertEquals(Integer.MAX_VALUE, SaveNumberParser.getParsedNumber(Integer.MAX_VALUE + ""));
        assertEquals(-213, SaveNumberParser.getParsedNumber("-213"));
        assertEquals(1_111_111, SaveNumberParser.getParsedNumber("1111111"));
    }
}
