package org.smoodi.core.module;

/**
 * <p>모듈의 선언이 잘못되어 사용할 수 없는 경우 발생하는 에러.</p>
 *
 * @author Daybreak312
 * @see ModuleCreationError
 */
public class ModuleDeclareError extends Error {

    public ModuleDeclareError(String message) {
        super(message);
    }

    public ModuleDeclareError(String message, Throwable cause) {
        super(message, cause);
    }
}
