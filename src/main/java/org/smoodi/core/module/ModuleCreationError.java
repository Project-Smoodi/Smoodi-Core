package org.smoodi.core.module;

/**
 * <p>모듈 객체를 생성하는 과정에서 발생한, 회복 불가능한 에러.</p>
 *
 * @author Daybreak312
 * @see ModuleDeclareError
 */
public class ModuleCreationError extends Error {

    public ModuleCreationError(String message) {
        super(message);
    }

    public ModuleCreationError(String message, Throwable cause) {
        super(message, cause);
    }
}
