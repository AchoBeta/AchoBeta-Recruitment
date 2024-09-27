package com.achobeta.domain.resumestate.model.dto;

import com.achobeta.domain.users.model.dto.MemberDTO;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 13:36
 */
@Data
public class ResumeExecuteDTO {

    @Valid
//    @NotNull(message = "成员信息不能为空")
    private MemberDTO memberDTO;

//    @NotNull(message = "是否通知不能为空")
    private Boolean isNotified;

}
