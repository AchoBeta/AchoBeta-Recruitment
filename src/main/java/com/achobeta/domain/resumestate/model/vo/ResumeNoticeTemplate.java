package com.achobeta.domain.resumestate.model.vo;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 17:13
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeNoticeTemplate {

    private String studentId;

    private String status;

    private String event;

}
