package com.example.lims.app.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.example.lims.app.Application;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlaywrightIT {

    @LocalServerPort
    private int port;

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void testAppLoads() {
        page.navigate(baseUrl());
        page.waitForLoadState();
        Assertions.assertNotNull(page.content());
    }

    @Test
    @Order(2)
    void testPatientsPageLoads() {
        page.navigate(baseUrl() + "/patients");
        page.waitForLoadState();
        Assertions.assertNotNull(page.content());
    }
}
