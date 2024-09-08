package com.achobeta.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
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

    public static <C, F> F readByProperty(C object, Field field, Class<F> fieldClazz) {
        try {
            return fieldClazz.cast(field.get(object));
        } catch (Exception e) {
            return null;
        }
    }

    public static <C, F> F readByMethod(C object, Field field, Class<F> fieldClazz) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), object.getClass());
            return fieldClazz.cast(propertyDescriptor.getReadMethod().invoke(object));
        } catch (Exception e) {
            return null;
        }
    }

    public static <C, F> F read(C object, Field field, Class<F> fieldClazz) {
        if (fieldClazz.isAssignableFrom(field.getType())) {
            return Optional.ofNullable(readByProperty(object, field, fieldClazz))
                    .orElse(readByMethod(object, field, fieldClazz));
        } else {
            return null;
        }
    }

    // 滤出 C 类内部不包括父类的字段列表中，「可通过字段或者 Getter 访问的 F/F子类的属性」，其他均为 null
    public static <C, F> Stream<F> stream(C object, Class<F> fieldClazz) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .map(field -> read(object, field, fieldClazz));
    }

    public static <C, F, P> P reduce(C object, Class<F> fieldClazz, Function<F, P> mapper,
                                     P identity, BinaryOperator<P> accumulator) {
        return stream(object, fieldClazz)
                .filter(Objects::nonNull)
                .map(mapper)
                .reduce(identity, accumulator);
    }

    public static <C, F> void forEach(C object, Class<F> fieldClazz, Consumer<F> consumer) {
        stream(object, fieldClazz)
                .filter(Objects::nonNull)
                .forEach(consumer);
    }

}
