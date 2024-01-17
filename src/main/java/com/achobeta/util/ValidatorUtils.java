package com.achobeta.util;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;

public class ValidatorUtils {

	private static final Validator validator = SpringUtil.getBean(Validator.class);

	public static <T> void validate(T object, Class<?>... groups) {
		Set<ConstraintViolation<T>> validate = validator.validate(object, groups);
		if (!validate.isEmpty()) {
			throw new ConstraintViolationException("参数校验异常", validate);
		}
	}

}