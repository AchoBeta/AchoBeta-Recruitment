package com.achobeta.util;

import com.achobeta.exception.GlobalServiceException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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

    // 滤出 C 类不包括父类的「F/F子类的属性」
    public static <C, F> Stream<F> stream(Class<C> clazz, Class<? extends F> type, C object) {
        List<F> fieldList = new ArrayList<>();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (type.isAssignableFrom(field.getType())) {
                    field.setAccessible(Boolean.TRUE);
                    fieldList.add((F) field.get(object));
                }
            }
        } catch (IllegalAccessException e) {
            throw new GlobalServiceException(e.getMessage());
        }
        return fieldList.stream();
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
