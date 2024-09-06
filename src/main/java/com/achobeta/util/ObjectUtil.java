package com.achobeta.util;

import com.achobeta.exception.GlobalServiceException;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-06
 * Time: 13:04
 */
public class ObjectUtil {

    // 滤出 C类（不包括父类） 的 F / F子类 的属性
    public static <C, F> Stream<F> stream(Class<C> clazz, Class<? extends F> type, C object) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> type.isAssignableFrom(field.getType()))
                .map(field -> {
                    try {
                        field.setAccessible(Boolean.TRUE);
                        return (F) field.get(object);
                    } catch (IllegalAccessException e) {
                        throw new GlobalServiceException(e.getMessage());
                    }
                });
    }

    public static <C, F, P> P reduceObject(Class<C> clazz, Class<? extends F> type,
                                           C object, Function<F, P> mapper,
                                           P identity, BinaryOperator<P> accumulator) {
        return stream(clazz, type, object)
                .filter(Objects::nonNull)
                .map(mapper)
                .reduce(identity, accumulator);
    }

    public static <C, F> void forEachObject(Class<C> clazz, Class<? extends F> type,
                                            C object, Consumer<F> consumer) {
        stream(clazz, type, object)
                .filter(Objects::nonNull)
                .forEach(consumer);
    }

}
