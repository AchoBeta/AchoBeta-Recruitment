package com.achobeta.domain.interview.machine.context;

import com.achobeta.common.enums.InterviewEvent;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.domain.interview.model.dto.InterviewExecuteDTO;
import com.achobeta.domain.interview.model.entity.Interview;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 0:41
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class InterviewContext {

    private Long managerId;

    private Interview interview;

    private InterviewExecuteDTO executeDTO;

    public void log(InterviewStatus from, InterviewStatus to, InterviewEvent event) {
        log.info("interview state from {} to {} run {} currentInterview {} managerId {} executeDTO {}",
                from, to, event, interview.getId(), managerId, executeDTO);
    }

}
