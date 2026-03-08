plugins {
    java
    id("io.qameta.allure") version "2.12.0"
}

group = "com.shop"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val selenideVersion = "7.7.3"
val appiumVersion = "9.4.0"
val cucumberVersion = "7.21.1"
val allureVersion = "2.29.1"
val restAssuredVersion = "5.5.1"
val ownerVersion = "1.0.12"
val fakerVersion = "2.4.2"
val junitPlatformVersion = "1.11.4"
val slf4jVersion = "2.0.16"

dependencies {
    // Selenide (mobile support via selenide-appium)
    implementation("com.codeborne:selenide-appium:$selenideVersion")

    // Appium Java Client
    implementation("io.appium:java-client:$appiumVersion")

    // Cucumber
    implementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-picocontainer:$cucumberVersion")
    testImplementation("org.junit.platform:junit-platform-suite:$junitPlatformVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")

    // Allure
    implementation("io.qameta.allure:allure-cucumber7-jvm:$allureVersion")
    implementation("io.qameta.allure:allure-selenide:$allureVersion")

    // REST Assured
    implementation("io.rest-assured:rest-assured:$restAssuredVersion")

    // Owner (configuration)
    implementation("org.aeonbits.owner:owner:$ownerVersion")

    // Faker (test data generation)
    implementation("net.datafaker:datafaker:$fakerVersion")

    // Selenium (for desktop mode ChromeDriver)
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.28.1")

    // Logging
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()

    systemProperty("cucumber.plugin", listOf(
        "pretty",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "html:build/reports/cucumber/cucumber.html",
        "json:build/reports/cucumber/cucumber.json"
    ).joinToString(","))

    systemProperty("cucumber.publish.quiet", "true")

    // Pass system properties from command line
    listOf("run.mode", "platform", "device", "os_version", "browser", "base.url", "api.url").forEach { prop ->
        System.getProperty(prop)?.let { systemProperty(prop, it) }
    }

    // Pass environment variables as system properties
    mapOf(
        "BROWSERSTACK_USERNAME" to "browserstack.username",
        "BROWSERSTACK_ACCESS_KEY" to "browserstack.accessKey"
    ).forEach { (env, prop) ->
        System.getenv(env)?.let { systemProperty(prop, it) }
    }
}

tasks.register<Test>("smokeTest") {
    useJUnitPlatform {
        includeTags("smoke")
    }
    systemProperties(tasks.named<Test>("test").get().systemProperties)
}

tasks.register<Test>("regressionTest") {
    useJUnitPlatform {
        includeTags("regression")
    }
    systemProperties(tasks.named<Test>("test").get().systemProperties)
}
