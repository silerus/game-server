package com.example.demo.infrastructure;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppEnvironment {

    private final Environment environment;

    public AppEnvironment(Environment environment) {
        this.environment = environment;
    }

    public boolean isDev() {
        for (String profile : environment.getActiveProfiles()) {
            if (profile.equalsIgnoreCase("dev")) return true;
        }
        return false;
    }

    public boolean isProd() {
        for (String profile : environment.getActiveProfiles()) {
            if (profile.equalsIgnoreCase("prod")) return true;
        }
        return false;
    }

    public String getActiveProfile() {
        String[] profiles = environment.getActiveProfiles();
        return profiles.length > 0 ? profiles[0] : "default";
    }
}
