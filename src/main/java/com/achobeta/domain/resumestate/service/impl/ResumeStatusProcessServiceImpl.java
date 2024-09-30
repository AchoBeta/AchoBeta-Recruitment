package com.achobeta.domain.resumestate.service.impl;

import com.achobeta.domain.resumestate.constants.ResumeStateConstants;
import com.achobeta.domain.resumestate.enums.ResumeEvent;
import com.achobeta.domain.resumestate.enums.ResumeStatus;
import com.achobeta.domain.resumestate.model.dao.mapper.ResumeStatusProcessMapper;
import com.achobeta.domain.resumestate.model.entity.ResumeStatusProcess;
import com.achobeta.domain.resumestate.service.ResumeStatusProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【resume_status_process.sql(招新简历状态过程表)】的数据库操作Service实现
* @createDate 2024-09-15 20:22:47
*/
@Service
public class ResumeStatusProcessServiceImpl extends ServiceImpl<ResumeStatusProcessMapper, ResumeStatusProcess>
    implements ResumeStatusProcessService{

    @Override
    public ResumeStatusProcess createResumeStatusProcess(Long resumeId, ResumeStatus resumeStatus, ResumeEvent resumeEvent) {
        ResumeStatusProcess resumeStatusProcess = new ResumeStatusProcess();
        resumeStatusProcess.setResumeId(resumeId);
        resumeStatusProcess.setResumeStatus(resumeStatus);
        resumeStatusProcess.setResumeEvent(Optional.ofNullable(resumeEvent).orElse(ResumeStateConstants.DEFAULT_RESUME_EVENT));
        this.save(resumeStatusProcess);
        return resumeStatusProcess;
    }

    @Override
    public List<ResumeStatusProcess> getProcessByResumeId(Long resumeId) {
        return this.lambdaQuery()
                .eq(ResumeStatusProcess::getResumeId, resumeId)
                .orderBy(Boolean.TRUE, Boolean.TRUE, ResumeStatusProcess::getCreateTime)
                .list();
    }
}




