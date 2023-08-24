package com.dxjunkyard.opendata.platform.domain.service

import com.atilika.kuromoji.ipadic.Tokenizer
import com.ibm.icu.text.Transliterator
import spock.lang.Specification

class RomajiConverterDomainServiceSpec extends Specification {

    private final Tokenizer tokenizer = new Tokenizer()

    private final Transliterator katakanaToHiraganaTranslator = Transliterator.getInstance("Katakana-Hiragana")

    private final Transliterator hiraganaToRomajiTranslator = Transliterator.getInstance("Hiragana-Latin")

    def "convertToRomaji"() {
        given:
        def target = new RomajiConverterDomainService(tokenizer, katakanaToHiraganaTranslator, hiraganaToRomajiTranslator)
        when:
        def result = target.convert("東京都")
        then:
        result == "toukyouto"
    }
}
