package com.shop.helpers;

import net.datafaker.Faker;

public class TestDataHelper {

    private static final Faker faker = new Faker();

    private TestDataHelper() {
    }

    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    public static String randomPassword() {
        return faker.internet().password(8, 20, true, true);
    }

    public static String randomName() {
        return faker.name().fullName();
    }

    public static String randomProductName() {
        return faker.commerce().productName();
    }

    public static String randomProductDescription() {
        return faker.lorem().sentence(10);
    }

    public static double randomPrice() {
        return Double.parseDouble(faker.commerce().price(5.0, 500.0));
    }

    public static int randomStock() {
        return faker.number().numberBetween(10, 100);
    }
}
