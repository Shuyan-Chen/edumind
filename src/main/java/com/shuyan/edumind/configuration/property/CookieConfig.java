package com.shuyan.edumind.configuration.property;


public class CookieConfig {

    public static String getName() {
        return "edumind";
    }

    public static Integer getInterval() {
        return 30 * 24 * 60 * 60;
    }
}
