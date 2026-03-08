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
        "classpath:application.properties"
})
public interface AppConfig extends Config {

    @Key("base.url")
    @DefaultValue("http://localhost:3000")
    String baseUrl();

    @Key("api.url")
    @DefaultValue("http://localhost:8000")
    String apiUrl();

    @Key("run.mode")
    @DefaultValue("local")
    String runMode();

    @Key("platform")
    @DefaultValue("android")
    String platform();

    @Key("appium.url")
    @DefaultValue("http://localhost:4723")
    String appiumUrl();

    @Key("timeout.implicit")
    @DefaultValue("10")
    int implicitTimeout();

    @Key("timeout.explicit")
    @DefaultValue("15")
    int explicitTimeout();

    @Key("timeout.page.load")
    @DefaultValue("30")
    int pageLoadTimeout();

    static AppConfig getInstance() {
        return ConfigFactory.create(AppConfig.class, System.getProperties(), System.getenv());
    }
}
