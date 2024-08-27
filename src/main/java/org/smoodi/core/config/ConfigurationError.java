package org.smoodi.core.config;

public class ConfigurationError extends Error {

    public ConfigurationError(String message) {
        super(message);
    }

    public ConfigurationError(String message, Throwable cause) {
        super(message, cause);
    }
}
