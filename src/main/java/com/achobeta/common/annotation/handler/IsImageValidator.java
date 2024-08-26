package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.IntRange;
import com.achobeta.common.annotation.IsImage;
import com.achobeta.util.ImageUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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
        return ImageUtil.isImage(url);
    }

}
