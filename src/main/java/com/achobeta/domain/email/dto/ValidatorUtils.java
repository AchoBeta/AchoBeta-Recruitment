package com.achobeta.domain.email.dto;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.annotation.Resource;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


@Component
public class ValidatorUtils {

//	@Autowired
	private Validator validator = SpringUtil.getBean(Validator.class);


	public String validateEntity(Object object, Class<?>... groups) {
		Set<ConstraintViolation<Object>> validate = validator.validate(object, groups);
		StringBuilder stringBuilder = new StringBuilder();
		for (ConstraintViolation<Object> o : validate) {
			stringBuilder.append(o.getMessage() + "\n");
		}
		return stringBuilder.toString();
	}
}