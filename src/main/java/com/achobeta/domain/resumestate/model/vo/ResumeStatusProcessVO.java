package com.achobeta.domain.resumestate.model.vo;

import com.achobeta.domain.resumestate.enums.ResumeEvent;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-15
 * Time: 21:02
 */
@Data
public class ResumeStatusProcessVO {

    private ResumeStatus  resumeStatus;

    private ResumeEvent resumeEvent;

    private LocalDateTime createTime;

}
