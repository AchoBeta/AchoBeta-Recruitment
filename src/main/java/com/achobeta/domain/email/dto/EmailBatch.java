package com.achobeta.domain.email.dto;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.domain.email.component.annotations.EmailPattern;
import com.achobeta.domain.email.component.po.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

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
    @jakarta.validation.constraints.Email
    @NotBlank
    private String sender;

    private String[] cc;

    private String title;

    @NotBlank(message = "邮件内容不能为空")
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