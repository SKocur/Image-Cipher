package com.skocur.imagecipher.tools

import spock.lang.Specification

class SaveNumberParserTest extends Specification {

    def "wrong input should give zero"() {
        expect:
        SaveNumberParser.getParsedNumber("11x") == 0
        SaveNumberParser.getParsedNumber("x11") == 0
        SaveNumberParser.getParsedNumber("0x1221") == 0
        SaveNumberParser.getParsedNumber("") == 0
        SaveNumberParser.getParsedNumber("-") == 0
        SaveNumberParser.getParsedNumber(null) == 0
        SaveNumberParser.getParsedNumber(SaveNumberParserTest.class.getName()) == 0
    }

    def "correct input should give parsed number"() {
        expect:
        SaveNumberParser.getParsedNumber("111") == 111
        SaveNumberParser.getParsedNumber(Integer.MAX_VALUE + "") == Integer.MAX_VALUE
        SaveNumberParser.getParsedNumber("-213") == -213
        SaveNumberParser.getParsedNumber("1111111") == 1_111_111
    }
}