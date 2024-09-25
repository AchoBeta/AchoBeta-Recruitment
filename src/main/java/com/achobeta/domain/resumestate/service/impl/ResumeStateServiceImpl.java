package com.achobeta.domain.resumestate.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resumestate.enums.ResumeEvent;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.constants.ResumeStateMachineConstants;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.domain.resumestate.service.ResumeStateService;
import com.achobeta.domain.resumestate.service.ResumeStatusProcessService;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.machine.StateMachineUtil;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final static String EXECUTE_RESUME_EVENT_LOCK = "executeResumeEventLock:";

    private final StuResumeService stuResumeService;

    private final ResumeStatusProcessService resumeStatusProcessService;

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

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
        Long resumeId = currentResume.getId();
        return redisLock.tryLockGetSomething(EXECUTE_RESUME_EVENT_LOCK + resumeId, () -> {
            // 获取当前状态
            ResumeStatus fromState = currentResume.getStatus();
            // 执行状态机
            ResumeStatus finalState = StateMachineUtil.fireEvent(
                    ResumeStateMachineConstants.RESUME_STATE_MACHINE_ID,
                    fromState,
                    resumeEvent,
                    resumeContext
            );
            // 能执行到这里，只有两种情况：1. 未命中、2. 轮转成功
            if(!Boolean.TRUE.equals(resumeContext.getHit())) {
                throw new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_STATUS_TRANS_EVENT_ERROR);
            }
            // 更新状态
            switchResumeState(currentResume.getId(), finalState);
            // 记录状态过程
            resumeStatusProcessService.createResumeStatusProcess(resumeId, finalState, resumeEvent);
            // 返回最终状态
            return finalState;
        }, () -> null, simpleLockStrategy);
    }
}
