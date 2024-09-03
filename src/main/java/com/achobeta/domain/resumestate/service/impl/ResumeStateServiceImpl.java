package com.achobeta.domain.resumestate.service.impl;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.constants.ResumeStateMachineConstants;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.domain.resumestate.service.ResumeStateService;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.machine.StateMachineUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 0:13
 */
@Service
@RequiredArgsConstructor
public class ResumeStateServiceImpl implements ResumeStateService {

    private final StuResumeService stuResumeService;

    @Override
    public void switchResumeState(Long resumeId, ResumeStatus status) {
        StuResume stuResume = new StuResume();
        stuResume.setId(resumeId);
        stuResume.setStatus(status);
        stuResumeService.updateById(stuResume);
    }

    @Override
    @Transactional
    public ResumeStatus executeResumeEvent(ResumeEvent resumeEvent, ResumeContext resumeContext) {
        StuResume currentResume = resumeContext.getResume();
        // 获取当前状态
        ResumeStatus fromState = currentResume.getStatus();
        // 执行状态机
        ResumeStatus finalState = StateMachineUtil.fireEvent(
                ResumeStateMachineConstants.RESUME_STATE_MACHINE_ID,
                fromState,
                resumeEvent,
                resumeContext
        );
        // 状态发生改变则进行更新
        if(!Objects.equals(fromState, finalState)) {
            switchResumeState(currentResume.getId(), finalState);
        }
        // 返回最终状态
        return finalState;
    }
}
