package com.sure.enums;

import com.sure.configuration.ConfigManager;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApiPath {

    static String baseAPIPath;
    static String environmentName = new ConfigManager().getProperty("environmentName");

    public static String setBaseAPIPath() {
        switch (environmentName) {
            case "demo":
                baseAPIPath = "https://mtv2-backend.stage-majles.tech/api";
                break;
            case "pre-production":
                baseAPIPath = "https://prebackend.mjls.tech";
                break;
            default:
                log.info("couldn't find any value .. setting the default value...");
                baseAPIPath = "https://backend.stage-majles.tech";
                break;
        }
        log.info("Base API is: " + baseAPIPath);
        return baseAPIPath;
    }

    @Getter
    public enum apiPath {
        LOGIN(new ConfigManager().getProperty("baseAPIPath") + "/login-by-criteria"),
        ADD_ACTIONS(new ConfigManager().getProperty("baseAPIPath") + "/actions"),
        START_VOTING(new ConfigManager().getProperty("baseAPIPath") + "/actions/start-vote");

        private final String value;

        apiPath(String value) {
            this.value = value;
        }

    }

}
