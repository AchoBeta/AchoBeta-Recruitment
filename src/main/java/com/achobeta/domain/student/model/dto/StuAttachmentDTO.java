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

    @NotBlank(message = "文件名不能为空")
    private String filename;
    @NotBlank(message = "附件路径不能为空")
    private String attachment;

}
