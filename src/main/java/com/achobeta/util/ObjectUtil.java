package com.achobeta.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
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
@Slf4j
public class ObjectUtil {

    public static <C, F> F readByProperty(C object, Field field, Class<F> fieldClazz) {
        try {
            return fieldClazz.cast(field.get(object));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public static <C, F> F readByMethod(C object, Field field, Class<F> fieldClazz) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), object.getClass());
            return fieldClazz.cast(propertyDescriptor.getReadMethod().invoke(object));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * 读取对象的某一个字段的值
     * 1. 若字段不是指定类型 F 或者不是 F 的子类，则返回 null
     * 2. 尝试获取字段的 Getter 方法获取值，若没有可以访问的 Getter，就访问字段，若字段不能直接访问，则返回 null
     * 
     * @param object 对象
     * @param field 字段
     * @param fieldClazz 字段类对象
     * @return 字段值
     * @param <C> 对象类型
     * @param <F> 字段类型
     */
    public static <C, F> F read(C object, Field field, Class<F> fieldClazz) {
        if (fieldClazz.isAssignableFrom(field.getType())) {
            return Optional.ofNullable(readByMethod(object, field, fieldClazz))
                    .orElseGet(() -> readByProperty(object, field, fieldClazz));
        } else {
            return null;
        }
    }

    // 滤出 C 类内部不包括父类的字段列表中，「可通过字段或者 Getter 访问的 F/F子类的属性」，其他均为 null
    public static <C, F> Stream<F> stream(C object, Class<F> fieldClazz) {
        // object.getClass() 会获取实例的实现类的类型
        return Arrays.stream(object.getClass().getDeclaredFields())
                .map(field -> read(object, field, fieldClazz));
    }

    public static <T> Stream<T> stream(Collection<T> collection) {
        return Optional.ofNullable(collection).stream().flatMap(Collection::stream);
    }

    public static <C, F, T> T reduce(C object, Class<F> fieldClazz, Function<F, T> mapper,
                                     T identity, BinaryOperator<T> accumulator) {
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
