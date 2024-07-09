package com.achobeta.domain.student.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/8
 */
@Data
public class StuAttachmentDTO implements Serializable {
    @NotNull(message = "用户id不能为空")
    private Long stuId;
    @NotBlank(message = "文件名不能为空")
    private String fileName;
    @NotBlank(message = "附件为空")
    private String attachment;

}
