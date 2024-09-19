package com.achobeta.domain.student.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/8
 */
@Data
public class StuAttachmentVO implements Serializable {

    private String filename;

    private String attachment;

}
