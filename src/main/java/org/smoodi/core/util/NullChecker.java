package org.smoodi.core.util;

import org.smoodi.annotation.Api;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;

/**
 * <p>메소드 체이닝을 이용해 다수의 객체의 null checking을 수행.</p>
 *
 * <p>아래와 같이 사용</p>
 * <pre>
 *     NullChecker.checker()
 *         .notNull(a)
 *         .notNull(b)
 *         .notNull(c)
 *         .notNull(d)
 * </pre>
 *
 * @author Daybreak312
 * @since v0.1.0
 */
public class NullChecker {

    public static NullChecker checker() {
        return new NullChecker();
    }

    @NotNull
    @Api
    public NullChecker notNull(@Nullable Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return this;
    }
}
