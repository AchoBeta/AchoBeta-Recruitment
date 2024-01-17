package com.achobeta.domain.email.dto;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;

public class ValidatorUtils {

//	@Autowired
	private static final Validator validator = SpringUtil.getBean(Validator.class);

	public static <T> String validateEntity(T object, Class<?>... groups) {
		Set<ConstraintViolation<T>> validate = validator.validate(object, groups);
		StringBuilder stringBuilder = new StringBuilder();
		for (ConstraintViolation<T> o : validate) {
			stringBuilder.append(o.getMessage() + "\n");
		}
		return stringBuilder.toString();
	}
}