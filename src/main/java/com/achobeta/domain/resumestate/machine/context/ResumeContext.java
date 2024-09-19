package com.achobeta.domain.resumestate.machine.context;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.model.dto.ResumeExecuteDTO;
import com.achobeta.domain.student.model.entity.StuResume;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 1:51
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ResumeContext {

    private Long managerId;

    private StuResume resume;

    private Boolean hit;

    ResumeExecuteDTO executeDTO;

    public void log(ResumeStatus from, ResumeStatus to, ResumeEvent event) {
        log.info("resume state from {} to {} run {} currentResume {} managerId {} executeDTO {}",
                from, to, event, resume.getId(), managerId, executeDTO);
    }

}
