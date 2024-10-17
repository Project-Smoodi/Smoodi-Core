package org.smoodi.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.smoodi.annotation.Api;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;

import java.util.function.Supplier;

/**
 * <p>Null Safety를 위한 유틸 함수 모음.</p>
 *
 * @author Daybreak312
 * @since v0.1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Nullability {

    /**
     * <p>단일 객체가 {@code null}이 아닌지 확인한 후 반환</p>
     *
     * <p>아래와 같이 사용</p>
     * <pre>
     *     var notNullValue = Nullability.notNull(value);
     * </pre>
     *
     * @param value null checking 대상
     * @param <T>   입력값의 타입과 반환형
     * @return null checking을 마친 입력값
     * @throws NullPointerException 입력값이 {@code null}일 경우
     */
    @NotNull
    @Api
    public static <T> T notNull(@Nullable final T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

    /**
     * <p>여러 객체를 null checking</p>
     *
     * <p>아래와 같이 사용</p>
     * <pre>
     *     Nullability.notNull(a, b, c, d, e);
     * </pre>
     *
     * @param objects null checking 대상
     * @throws NullPointerException 입력값이 {@code null}일 경우
     */
    @Api
    public static void notNull(@Nullable final Object... objects) {
        for (Object object : objects) {
            notNull(object);
        }
    }

    /**
     * <p>두 값을 받아 첫번째 값이 {@code null}일 경우 두번째 값을, 아닐 경우 첫번째 값 반환</p>
     *
     * @param first  첫번째 값
     * @param second 두번째 값
     * @param <T>    매개변수 타입과 반환형
     * @return 첫번째 값이 {@code null}일 경우 두번째 값, {@code null}이 아닐 경우 첫번째 값 반환
     */
    @Nullable
    @Api
    public static <T> T firstOrSecondIfNull(
            @Nullable final T first,
            @NotNull final Supplier<T> second) {
        return first == null ? second.get() : first;
    }

    /**
     * <p>두 문자열을 받아 첫번째 값이 {@code null} 혹은 비어있을 경우 두번째 값을, 아닐 경우 첫번째 값 반환</p>
     *
     * @param first  첫번째 값
     * @param second 두번째 값
     * @return 첫번째 값이 {@code null} 혹은 비어있을 경우 두번째 값, 아닐 경우 첫번째 값 반환
     */
    @Nullable
    @Api
    public static String firstOrSecondIfBlank(
            @Nullable final String first,
            @Nullable final Supplier<String> second
    ) {
        return first == null || first.isBlank() ? second.get() : first;
    }

}
