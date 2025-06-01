package com.system.design.urlshortener.domain.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ShortCodeGeneratorTest {

    @Test
    void generate_shouldReturnConsistentCodeForSameInput() {
        String url = "https://example.com";
        String code1 = ShortUrlGenerator.generate(url);
        String code2 = ShortUrlGenerator.generate(url);
        assertThat(code1).isEqualTo(code2);
    }

    @Test
    void generate_shouldReturnDifferentCodesForDifferentInputs() {
        String code1 = ShortUrlGenerator.generate("https://foo.com");
        String code2 = ShortUrlGenerator.generate("https://bar.com");
        assertThat(code1).isNotEqualTo(code2);
    }

    @Test
    void generate_shouldReturnHexStringOfLength12() {
        String code = ShortUrlGenerator.generate("https://example.com");
        assertThat(code).hasSize(12);
        assertThat(code).matches("[0-9a-f]+");
    }
}

