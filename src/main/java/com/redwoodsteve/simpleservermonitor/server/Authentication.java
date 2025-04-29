package com.redwoodsteve.simpleservermonitor.server;

import com.redwoodsteve.simpleservermonitor.util.Config;

public class Authentication {
    public static boolean isAuthorized(String input) {
        if (Config.getFromKey("serverAuth").equals("no")) return true; else {
            return Config.getFromKey("serverAuth").equals(input);
        }
    }
}
