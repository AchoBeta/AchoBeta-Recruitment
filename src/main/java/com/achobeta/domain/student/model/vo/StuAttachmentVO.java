package com.achobeta.domain.student.model.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/8
 */
@Data
public class StuAttachmentVO implements Serializable {

    private Long stuId;

    private String fileName;

    private String attachment;

}
