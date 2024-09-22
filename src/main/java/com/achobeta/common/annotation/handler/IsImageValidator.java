package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.IsImage;
import com.achobeta.util.ImageUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-23
 * Time: 2:07
 */
public class IsImageValidator implements ConstraintValidator<IsImage, String> {

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        // 可以为 null，但不可访问或者不是图片类型都会非法
        return Optional.ofNullable(url)
                .map(ImageUtil::isImage)
                .orElse(Boolean.TRUE);
    }

}
