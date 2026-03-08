package com.shop.config;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.options.BaseOptions;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();

    private static final AppConfig appConfig = AppConfig.getInstance();
    private static final BrowserStackConfig bsConfig = BrowserStackConfig.getInstance();

    private DriverFactory() {
    }

    public static RemoteWebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static RemoteWebDriver createDriver() {
        String runMode = appConfig.runMode();
        String platform = appConfig.platform();

        log.info("Creating driver: mode={}, platform={}", runMode, platform);

        RemoteWebDriver driver = createDriverWithRetry(runMode, platform, 3);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(appConfig.implicitTimeout()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(appConfig.pageLoadTimeout()));

        driverThreadLocal.set(driver);
        return driver;
    }

    private static RemoteWebDriver createDriverWithRetry(String runMode, String platform, int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if ("browserstack".equalsIgnoreCase(runMode)) {
                    return createBrowserStackDriver(platform);
                } else if ("desktop".equalsIgnoreCase(runMode)) {
                    return createDesktopChromeDriver();
                } else {
                    return createLocalDriver(platform);
                }
            } catch (Exception e) {
                log.warn("Driver creation attempt {}/{} failed: {}", attempt, maxAttempts, e.getMessage());
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Failed to create driver after " + maxAttempts + " attempts", e);
                }
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }
        throw new RuntimeException("Unreachable");
    }

    public static void quitDriver() {
        RemoteWebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("Quitting driver");
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    // --- Desktop Chrome Driver (for local development/debugging without Appium) ---

    private static RemoteWebDriver createDesktopChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=412,915");
        // Mobile emulation: simulate a mobile viewport
        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", Map.of("width", 412, "height", 915, "pixelRatio", 2.625));
        mobileEmulation.put("userAgent",
                "Mozilla/5.0 (Linux; Android 13; SM-S911B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        log.info("Creating desktop Chrome driver with mobile emulation");
        return new ChromeDriver(options);
    }

    // --- Local Appium Driver ---

    private static RemoteWebDriver createLocalDriver(String platform) {
        try {
            URL appiumUrl = new URL(appConfig.appiumUrl());

            if ("ios".equalsIgnoreCase(platform)) {
                return createLocalIosDriver(appiumUrl);
            } else {
                return createLocalAndroidDriver(appiumUrl);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium URL: " + appConfig.appiumUrl(), e);
        }
    }

    private static AndroidDriver createLocalAndroidDriver(URL appiumUrl) {
        var caps = new BaseOptions<>()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2");

        caps.setCapability("browserName", "chrome");
        caps.setCapability("appium:deviceName", "Android Emulator");
        caps.setCapability("appium:chromedriverAutodownload", true);

        log.info("Creating local Android driver at {}", appiumUrl);
        return new AndroidDriver(appiumUrl, caps);
    }

    private static IOSDriver createLocalIosDriver(URL appiumUrl) {
        var caps = new BaseOptions<>()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest");

        caps.setCapability("browserName", "safari");
        caps.setCapability("appium:deviceName", "iPhone Simulator");

        log.info("Creating local iOS driver at {}", appiumUrl);
        return new IOSDriver(appiumUrl, caps);
    }

    // --- BrowserStack Driver ---

    private static RemoteWebDriver createBrowserStackDriver(String platform) {
        try {
            String hubUrl = String.format(
                    "https://%s:%s@hub.browserstack.com/wd/hub",
                    bsConfig.username(), bsConfig.accessKey()
            );

            MutableCapabilities caps = new MutableCapabilities();
            Map<String, Object> bstackOptions = buildBrowserStackOptions();

            if ("ios".equalsIgnoreCase(platform)) {
                caps.setCapability("browserName", bsConfig.iosBrowser());
                bstackOptions.put("deviceName", bsConfig.iosDevice());
                bstackOptions.put("osVersion", bsConfig.iosOsVersion());
                bstackOptions.put("os", "ios");
            } else {
                caps.setCapability("browserName", bsConfig.androidBrowser());
                bstackOptions.put("deviceName", bsConfig.androidDevice());
                bstackOptions.put("osVersion", bsConfig.androidOsVersion());
                bstackOptions.put("os", "android");
            }

            caps.setCapability("bstack:options", bstackOptions);

            log.info("Creating BrowserStack {} driver on {}",
                    platform, bstackOptions.get("deviceName"));

            if ("ios".equalsIgnoreCase(platform)) {
                return new IOSDriver(new URL(hubUrl), caps);
            } else {
                return new AndroidDriver(new URL(hubUrl), caps);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid BrowserStack Hub URL", e);
        }
    }

    private static Map<String, Object> buildBrowserStackOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("projectName", bsConfig.project());
        options.put("buildName", bsConfig.build());
        options.put("sessionName", "Mobile Web Test");
        options.put("local", String.valueOf(bsConfig.local()));
        options.put("debug", String.valueOf(bsConfig.debug()));
        options.put("networkLogs", String.valueOf(bsConfig.networkLogs()));
        options.put("consoleLogs", bsConfig.consoleLogs());
        return options;
    }
}
