package com.shop.hooks;

import com.codeborne.selenide.WebDriverRunner;
import com.shop.config.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());

        RemoteWebDriver driver = DriverFactory.createDriver();
        WebDriverRunner.setWebDriver(driver);

        log.info("Driver created for scenario: {}", scenario.getName());
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        log.info("Finishing scenario: {} - Status: {}", scenario.getName(), scenario.getStatus());

        try {
            if (scenario.isFailed()) {
                attachScreenshot(scenario);
                attachPageSource(scenario);
            }
        } catch (Exception e) {
            log.warn("Failed to capture failure artifacts: {}", e.getMessage());
        } finally {
            DriverFactory.quitDriver();
            // Small delay to let Chrome release resources in constrained environments
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            log.info("Driver quit for scenario: {}", scenario.getName());
        }
    }

    private void attachScreenshot(Scenario scenario) {
        RemoteWebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot on failure");
            Allure.addAttachment("Screenshot on failure", "image/png",
                    new java.io.ByteArrayInputStream(screenshot), "png");
            log.info("Screenshot captured for failed scenario: {}", scenario.getName());
        }
    }

    private void attachPageSource(Scenario scenario) {
        RemoteWebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            String pageSource = driver.getPageSource();
            Allure.addAttachment("Page source on failure", "text/html", pageSource);
        }
    }

    private String getFeatureName(Scenario scenario) {
        String uri = scenario.getUri().toString();
        // Extract feature name from URI: .../features/login.feature → Login
        String fileName = uri.substring(uri.lastIndexOf('/') + 1).replace(".feature", "");
        return capitalize(fileName.replace('_', ' '));
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
