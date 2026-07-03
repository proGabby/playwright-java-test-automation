package com.mycompany;

import com.microsoft.playwright.*;

public class App {
    public static void main(String[] args) {

        try (Playwright playwright = Playwright.create()) {
            // Launch a headless Chromium browser instance
            Browser browser = playwright.chromium().launch();

            // Open a new page (tab) within the browser
            Page page = browser.newPage();

            // Navigate to the Playwright homepage
            page.navigate("https://playwright.dev/");

            // Verify that the title contains 'Playwright' to ensure the page loaded
            // correctly
            if (!page.title().contains("Playwright")) {
                throw new RuntimeException(
                        "Smoke test failed! Title did not contain 'Playwright'. "
                                + "Current title: '" + page.title() + "'");
            }

            System.out.println("Smoke test passed successfully!");
        }
    }
}
