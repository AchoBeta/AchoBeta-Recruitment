package com.achobeta.domain.student.model.dto;

import lombok.Data;

import javax.validation.Valid;
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
    @Valid
    @NotNull
    private StuSimpleResumeDTO stuSimpleResumeDTO;

    // 附件列表可以为 null 但是不为 null 的时候进行循环检测
    @Valid
    private List<StuAttachmentDTO> stuAttachmentDTOList;

}
