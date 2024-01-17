package com.achobeta.domain.email.dto;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.domain.email.component.annotations.EmailPattern;
import com.achobeta.domain.email.component.po.Email;
import lombok.Data;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 马拉圈
 * Date: 2024-01-15
 * Time: 13:01
 */
@Data
// 测试 email表单对象
public class EmailBatch {

    private String[] recipient;

    @EmailPattern
    private String sender;

    private String[] cc;

    private String title;

    private String content;

    public Email transfer() {
        Email email = BeanUtil.copyProperties(this, Email.class);
        email.setRecipient(this.getRecipient());
        email.setCarbonCopy(this.getCc());
        email.setCreateTime(new Date());
        return email;
    }

    public static void main(String[] args) {

    }


    private static final long serialVersionUID = 1L;
}