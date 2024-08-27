package org.smoodi.core.loader;

/**
 * Error during module initialization
 *
 *
 */
public class ModuleCreationError extends Error {

    public ModuleCreationError(String message) {
        super(message);
    }

    public ModuleCreationError(String message, Throwable cause) {
        super(message, cause);
    }
}
