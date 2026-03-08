package com.shop.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class OrdersPage extends BaseMobilePage {

    private final SelenideElement pageTitle = $(By.xpath("//h1[text()='My Orders']"));
    private final SelenideElement emptyMessage = $(By.xpath("//p[text()='No orders yet.']"));
    private final ElementsCollection orderCards = $$("div.bg-white.p-6.rounded-lg.shadow");

    @Step("Open orders page")
    public OrdersPage open() {
        navigateTo(config.baseUrl() + "/orders");
        pageTitle.shouldBe(Condition.visible, TIMEOUT);
        return this;
    }

    @Step("Check if orders page is displayed")
    public boolean isPageDisplayed() {
        return pageTitle.isDisplayed();
    }

    @Step("Check if no orders message is displayed")
    public boolean isEmptyMessageDisplayed() {
        return emptyMessage.isDisplayed();
    }

    @Step("Get number of orders")
    public int getOrderCount() {
        return orderCards.size();
    }

    @Step("Get order ID text at index: {index}")
    public String getOrderTitle(int index) {
        return orderCards.get(index).$("h3").getText();
    }

    @Step("Get order status at index: {index}")
    public String getOrderStatus(int index) {
        return orderCards.get(index).$("span.rounded-full").getText();
    }

    @Step("Get order total at index: {index}")
    public String getOrderTotal(int index) {
        return orderCards.get(index).$("p.text-lg.font-bold").getText();
    }

    @Step("Check if order with ID exists: #{orderId}")
    public boolean hasOrderWithId(int orderId) {
        return $(By.xpath("//h3[text()='Order #" + orderId + "']")).isDisplayed();
    }
}
