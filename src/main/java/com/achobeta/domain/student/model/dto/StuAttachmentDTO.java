package com.achobeta.domain.student.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/8
 */
@Data
public class StuAttachmentDTO implements Serializable {

    @NotBlank(message = "文件名不能为空")
    private String filename;
    @NotNull(message = "附件路径不能为空")
    private Long attachment;

}
