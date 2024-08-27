package org.smoodi.core.loader;

/**
 * Error during module initialization
 *
 *
 */
public class ModuleDeclareError extends Error {

    public ModuleDeclareError(String message) {
        super(message);
    }

    public ModuleDeclareError(String message, Throwable cause) {
        super(message, cause);
    }
}
