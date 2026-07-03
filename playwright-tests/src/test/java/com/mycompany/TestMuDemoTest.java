package com.mycompany;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestMuDemoTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    // JUnit 5 TestWatcher to report status and clean up resources
    @RegisterExtension
    final TestWatcher watcher = new TestWatcher() {
        @Override
        public void testSuccessful(ExtensionContext context) {
            reportStatus("passed");
        }

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            reportStatus("failed");
        }

        private void reportStatus(String status) {
            System.out.println("--- STATUS REPORTED: " + status + " ---");
            try {
                if (page != null) {
                    String action = String.format(
                            "lambdatest_action: {\"action\": \"setTestStatus\", "
                                    + "\"arguments\": {\"status\":\"%s\"}}",
                            status);
                    page.evaluate("_ => {}", action);
                }
            } finally {
                // Ensure browser resources close cleanly
                if (TestMuDemoTest.this.context != null) {
                    TestMuDemoTest.this.context.close();
                }
                if (TestMuDemoTest.this.playwright != null) {
                    TestMuDemoTest.this.playwright.close();
                }
            }
        }
    };

    @BeforeEach
    void setUp(TestInfo testInfo) {
        String username = System.getenv("LT_USERNAME");
        String accessKey = System.getenv("LT_ACCESS_KEY");

        if (username == null || accessKey == null) {
            throw new IllegalStateException(
                    "Missing LT_USERNAME or LT_ACCESS_KEY environment variables.");
        }

        playwright = Playwright.create();
        String testName = testInfo.getDisplayName();

        String capabilities = """
                {
                    "browserName": "Chrome",
                    "browserVersion": "latest",
                    "LT:Options": {
                        "platform": "Windows 10",
                        "project": "E-commerce automation",
                        "build": "Playwright Java parallel build",
                        "name": "%s",
                        "user": "%s",
                        "accessKey": "%s",
                        "video": true,
                        "network": true,
                        "console": true
                    }
                }
                """.formatted(testName, username, accessKey);

        try {
            String capsEncoded = URLEncoder.encode(
                    capabilities,
                    StandardCharsets.UTF_8.toString());
            String connectUrl = "wss://cdp.lambdatest.com/playwright?"
                    + "capabilities=" + capsEncoded;

            browser = playwright.chromium().connect(connectUrl);
            context = browser.newContext();
            page = context.newPage();
        } catch (Exception e) {
            if (playwright != null) {
                playwright.close();
            }
            throw new RuntimeException(
                    "Failed to connect to TestMu AI grid. Verify credentials, "
                            + "CDP URL, and concurrent limits on your dashboard.",
                    e);
        }
    }

    @Test
    @DisplayName("Should search for a product and verify results on the cloud grid")
    void shouldSearchAndFindProduct() {
        page.navigate("https://ecommerce-playground.lambdatest.io/");

        // Locate search bar and fill query
        page.getByPlaceholder("Search For Products").first().fill("HTC");

        // Click search button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Search")).click();

        // Assertions
        assertThat(page.locator("h1")).containsText("Search - HTC");

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions()
                .setName("HTC Touch HD")).first()).isVisible();
    }
}
