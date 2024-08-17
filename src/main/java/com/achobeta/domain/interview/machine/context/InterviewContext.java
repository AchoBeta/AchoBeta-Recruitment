package com.achobeta.domain.interview.machine.context;

import com.achobeta.common.enums.InterviewEvent;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.domain.interview.model.entity.Interview;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 0:41
 */
@Data
@Slf4j
public class InterviewContext {

    private Long managerId;

    private InterviewStatus toState;

    private Interview interview;

    public void log(InterviewStatus from, InterviewStatus to, InterviewEvent event) {
        log.info("interview state from {} to {} run {} currentInterview {} managerId {}",
                from, to, event, interview.getId(), managerId);
    }

}