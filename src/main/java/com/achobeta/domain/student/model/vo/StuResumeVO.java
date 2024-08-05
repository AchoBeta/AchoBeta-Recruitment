package com.achobeta.domain.student.model.vo;

import com.achobeta.domain.student.model.dto.StuAttachmentDTO;
import com.achobeta.domain.student.model.dto.StuSimpleResumeDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
