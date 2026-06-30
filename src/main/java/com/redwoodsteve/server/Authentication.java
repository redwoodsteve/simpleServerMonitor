package com.redwoodsteve.server;

import com.redwoodsteve.util.Config;

import java.util.Objects;

public class Authentication {
    public static boolean isAuthorized(String input) {
        if (((String) Objects.requireNonNull(Config.getFromKey("serverAuth"))).equalsIgnoreCase("no")) return true; else {
            return ((String) Objects.requireNonNull(Config.getFromKey("serverAuth"))).equalsIgnoreCase(input);
        }
    }
}