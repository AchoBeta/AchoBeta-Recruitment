package com.achobeta.domain.resumestate.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resumestate.constants.ResumeStateConstants;
import com.achobeta.domain.resumestate.enums.ResumeEvent;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.constants.ResumeStateMachineConstants;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.domain.resumestate.model.entity.ResumeStatusProcess;
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
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.achobeta.domain.resumestate.constants.ResumeStateConstants.DEFAULT_RESUME_EVENT;

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

    private final ResumeStatusProcessService resumeStatusProcessService;

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    @Override
    @Transactional
    public void switchResumeState(Long resumeId, ResumeStatus status, ResumeEvent event) {
        StuResume stuResume = new StuResume();
        stuResume.setId(resumeId);
        stuResume.setStatus(status);
        stuResumeService.updateById(stuResume);
        // 记录状态过程
        resumeStatusProcessService.createResumeStatusProcess(resumeId, status, event);
    }

    @Override
    @Transactional
    public ResumeStatus executeResumeEvent(ResumeEvent resumeEvent, ResumeContext resumeContext) {
        StuResume currentResume = resumeContext.getResume();
        Long resumeId = currentResume.getId();
        return redisLock.tryLockGetSomething(ResumeStateConstants.EXECUTE_RESUME_EVENT_LOCK + resumeId, () -> {
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
            switchResumeState(currentResume.getId(), finalState, resumeEvent);
            // 返回最终状态
            return finalState;
        }, () -> null, simpleLockStrategy);
    }

    @Override
    @Transactional
    public List<ResumeStatusProcess> getProcessByResumeId(StuResume currentResume) {
        Long resumeId = currentResume.getId();
        ResumeStatus currentStatus = currentResume.getStatus();
        List<ResumeStatusProcess> statusProcesses = resumeStatusProcessService.getProcessByResumeId(resumeId);
        if(CollectionUtils.isEmpty(statusProcesses)) {
            // 设置默认节点
            ResumeStatusProcess process = resumeStatusProcessService.createResumeStatusProcess(
                    resumeId,
                    currentStatus,
                    DEFAULT_RESUME_EVENT
            );
            return List.of(process);
        } else {
            ResumeStatus lastStatus = statusProcesses.getLast().getResumeStatus();
            // 如果最后一个节点不是当前状态，则推进到当前状态
            if(!currentStatus.equals(lastStatus)) {
                ResumeStatusProcess process = resumeStatusProcessService.createResumeStatusProcess(
                        resumeId,
                        currentStatus,
                        DEFAULT_RESUME_EVENT
                );
                statusProcesses.add(process);
            }
        }
        return statusProcesses;
    }
}
