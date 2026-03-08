package com.shop.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;

@LoadPolicy(LoadType.MERGE)
@Sources({
        "system:properties",
        "system:env",
        "classpath:browserstack.properties"
})
public interface BrowserStackConfig extends Config {

    @Key("browserstack.hub")
    @DefaultValue("https://hub.browserstack.com/wd/hub")
    String hub();

    @Key("BROWSERSTACK_USERNAME")
    String username();

    @Key("BROWSERSTACK_ACCESS_KEY")
    String accessKey();

    // Android
    @Key("android.device")
    @DefaultValue("Samsung Galaxy S23")
    String androidDevice();

    @Key("android.os.version")
    @DefaultValue("13.0")
    String androidOsVersion();

    @Key("android.browser")
    @DefaultValue("chrome")
    String androidBrowser();

    // iOS
    @Key("ios.device")
    @DefaultValue("iPhone 15")
    String iosDevice();

    @Key("ios.os.version")
    @DefaultValue("17")
    String iosOsVersion();

    @Key("ios.browser")
    @DefaultValue("safari")
    String iosBrowser();

    // BrowserStack options
    @Key("browserstack.local")
    @DefaultValue("true")
    boolean local();

    @Key("browserstack.debug")
    @DefaultValue("true")
    boolean debug();

    @Key("browserstack.networkLogs")
    @DefaultValue("true")
    boolean networkLogs();

    @Key("browserstack.consoleLogs")
    @DefaultValue("info")
    String consoleLogs();

    @Key("browserstack.project")
    @DefaultValue("shop-tests-mobile-web")
    String project();

    @Key("browserstack.build")
    @DefaultValue("mobile-web-local")
    String build();

    static BrowserStackConfig getInstance() {
        return ConfigFactory.create(BrowserStackConfig.class, System.getProperties(), System.getenv());
    }
}
