package com.achobeta.domain.student.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author cattleYuan
 * @date 2024/7/8
 */
@Data
public class StuResumeVO implements Serializable {

    private List<StuAttachmentVO> stuAttachmentVOList;

    private StuSimpleResumeVO stuSimpleResumeVO;

}
