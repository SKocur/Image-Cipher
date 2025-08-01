package com.imagecipher.app.tools

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SafeNumberParserTest {

    @Test
    fun shouldReturnZeroForWrongInput() {
        // when & then
        assertEquals(0, SafeNumberParser.getParsedNumber("11x"))
        assertEquals(0, SafeNumberParser.getParsedNumber("x11"))
        assertEquals(0, SafeNumberParser.getParsedNumber("0x1221"))
        assertEquals(0, SafeNumberParser.getParsedNumber(""))
        assertEquals(0, SafeNumberParser.getParsedNumber("-"))
        assertEquals(0, SafeNumberParser.getParsedNumber(null))
        assertEquals(0, SafeNumberParser.getParsedNumber(SafeNumberParserTest::class.java.name))
    }

    @Test
    fun shouldReturnParsedNumberForCorrectInput() {
        // when & then
        assertEquals(111, SafeNumberParser.getParsedNumber("111"))
        assertEquals(Int.MAX_VALUE, SafeNumberParser.getParsedNumber(Int.MAX_VALUE.toString()))
        assertEquals(-213, SafeNumberParser.getParsedNumber("-213"))
        assertEquals(1_111_111, SafeNumberParser.getParsedNumber("1111111"))
    }
}
