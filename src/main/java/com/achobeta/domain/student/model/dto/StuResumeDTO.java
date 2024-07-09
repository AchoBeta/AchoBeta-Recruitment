package com.achobeta.domain.student.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 22:52
 */
@Data
public class StuResumeDTO implements Serializable {
    @NotNull
    private StuSimpleResumeDTO stuSimpleResumeDTO;

    private List<StuAttachmentDTO> stuAttachmentDTOList;

}
