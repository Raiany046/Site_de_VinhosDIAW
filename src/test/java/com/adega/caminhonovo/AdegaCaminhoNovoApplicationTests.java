package com.adega.caminhonovo;

import com.adega.caminhonovo.application.AdegaCaminhoNovoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = AdegaCaminhoNovoApplication.class)
@TestPropertySource(properties = {
        // usa um banco em memoria para nao mexer no arquivo de desenvolvimento
        "spring.datasource.url=jdbc:h2:mem:teste;DB_CLOSE_DELAY=-1"
})
class AdegaCaminhoNovoApplicationTests {

    @Test
    void contextLoads() {
    }
}
