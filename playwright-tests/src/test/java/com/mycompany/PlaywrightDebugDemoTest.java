package com.mycompany;

import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlaywrightDebugDemoTest {
    private Playwright playwright;
    private Browser browser;

    private BrowserContext context;
    private Page page;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();

        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(false));

    }

    @AfterAll
    void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void createContextAndPage(TestInfo testInfo) {
        context = browser.newContext();
        // Start tracing before the test execution begins
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
    }

    @AfterEach
    void closeContext(TestInfo testInfo) {
        if (context != null) {
            String className = testInfo
                    .getTestClass()
                    .map(Class::getSimpleName)
                    .orElse("Test");

            String safeDisplayName = testInfo
                    .getDisplayName()
                    .replaceAll("[^a-zA-Z0-9-]", "_");

            String safeName = className + "-" + safeDisplayName;

            context.tracing()
                    .stop(new Tracing.StopOptions()
                            .setPath(java.nio.file.Paths
                                    .get("target/trace-"
                                            + safeName + ".zip")));
            context.close();
        }
    }

    @Test
    @DisplayName("Should search for a product and verify results")
    void shouldSearchAndFindProduct() {

        page.navigate("https://ecommerce-playground.lambdatest.io/");

        // Locate the search bar and type the product name
        page.getByPlaceholder("Search For Products")
                .first()
                .fill("HTC");

        // Click the search submit button
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search"))
                .click();

        // Verify the search results header contains the query
        assertThat(page.locator("h1"))
                .containsText("Search - HTC");

        // Verify that the product link is visible in the search results
        assertThat(
                page.getByRole(AriaRole.LINK,
                        new Page.GetByRoleOptions().setName("HTC Touch HD"))
                        .first())
                .isVisible();
    }

}
