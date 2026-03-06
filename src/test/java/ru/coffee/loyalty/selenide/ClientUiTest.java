package ru.coffee.loyalty.selenide;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;

/**
 * UI автотесты (Selenide) для системы лояльности.
 * Запуск: приложение должно быть поднято на baseUrl (или используйте @SpringBootTest с портом).
 * Требуется Chrome. Чтобы не падало на Netty: в JVM задано -Dwebdriver.http.factory=jdk-http-client (см. pom.xml surefire).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ClientUiTest {

    static {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
    }

    @LocalServerPort
    private int port;

    @BeforeAll
    static void setup() {
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.timeout = 10_000;
    }

    @BeforeEach
    void open() {
        Configuration.baseUrl = "http://localhost:" + port;
        Selenide.open("/");
    }

    @Test
    void pageLoadsAndShowsTitle() {
        $("h1").shouldHave(text("Система лояльности кофейни"));
        $("h2").shouldHave(text("Клиенты"));
    }

    @Test
    void canOpenClientForm() {
        $(byText("Добавить клиента")).click();
        $("#clientForm").shouldBe(visible);
        $("#clientName").shouldBe(visible);
    }

    @Test
    void canOpenRewardForm() {
        $(byText("Добавить награду")).click();
        $("#rewardForm").shouldBe(visible);
        $("#rewardName").shouldBe(visible);
    }

    @Test
    void createClientAndSeeInList() {
        $(byText("Добавить клиента")).click();
        $("#clientName").setValue("Тест Клиент UI");
        $("#clientPhone").setValue("+79001112233");
        $("#clientEmail").setValue("test@test.ru");
        $$("button").findBy(text("Сохранить")).click();
        $("#clientForm").shouldBe(hidden);
        $("#clientList").shouldHave(text("Тест Клиент UI"));
    }
}
