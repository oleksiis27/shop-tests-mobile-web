package com.shop.helpers;

import com.shop.config.AppConfig;
import com.shop.config.DriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.List;

public class GestureHelper {

    private static final PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

    private GestureHelper() {
    }

    @Step("Swipe up on screen")
    public static void swipeUp() {
        swipe(Direction.UP);
    }

    @Step("Swipe down on screen")
    public static void swipeDown() {
        swipe(Direction.DOWN);
    }

    @Step("Swipe left on screen")
    public static void swipeLeft() {
        swipe(Direction.LEFT);
    }

    @Step("Swipe right on screen")
    public static void swipeRight() {
        swipe(Direction.RIGHT);
    }

    private static boolean isDesktopMode() {
        return "desktop".equalsIgnoreCase(AppConfig.getInstance().runMode());
    }

    private static void swipe(Direction direction) {
        RemoteWebDriver driver = DriverFactory.getDriver();

        // In desktop mode, use JavaScript scrolling instead of touch gestures
        if (isDesktopMode()) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            switch (direction) {
                case UP -> js.executeScript("window.scrollBy(0, 500)");
                case DOWN -> js.executeScript("window.scrollBy(0, -500)");
                case LEFT -> js.executeScript("window.scrollBy(500, 0)");
                case RIGHT -> js.executeScript("window.scrollBy(-500, 0)");
            }
            return;
        }

        Dimension size = driver.manage().window().getSize();

        int centerX = size.width / 2;
        int centerY = size.height / 2;

        int startX, startY, endX, endY;

        switch (direction) {
            case UP -> {
                startX = centerX;
                startY = (int) (size.height * 0.7);
                endX = centerX;
                endY = (int) (size.height * 0.3);
            }
            case DOWN -> {
                startX = centerX;
                startY = (int) (size.height * 0.3);
                endX = centerX;
                endY = (int) (size.height * 0.7);
            }
            case LEFT -> {
                startX = (int) (size.width * 0.8);
                startY = centerY;
                endX = (int) (size.width * 0.2);
                endY = centerY;
            }
            case RIGHT -> {
                startX = (int) (size.width * 0.2);
                startY = centerY;
                endX = (int) (size.width * 0.8);
                endY = centerY;
            }
            default -> throw new IllegalArgumentException("Unknown direction: " + direction);
        }

        performSwipe(driver, startX, startY, endX, endY);
    }

    @Step("Tap at coordinates ({x}, {y})")
    public static void tap(int x, int y) {
        RemoteWebDriver driver = DriverFactory.getDriver();

        Sequence tap = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(100)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(tap));
    }

    @Step("Long press at coordinates ({x}, {y})")
    public static void longPress(int x, int y) {
        RemoteWebDriver driver = DriverFactory.getDriver();

        Sequence longPress = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(1500)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(longPress));
    }

    private static void performSwipe(RemoteWebDriver driver, int startX, int startY, int endX, int endY) {
        Sequence swipe = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(swipe));
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
