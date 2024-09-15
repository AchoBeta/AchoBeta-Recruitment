package com.achobeta.domain.resumestate.service;

import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.model.entity.ResumeStatusProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【resume_status_process.sql(招新简历状态过程表)】的数据库操作Service
* @createDate 2024-09-15 20:22:47
*/
public interface ResumeStatusProcessService extends IService<ResumeStatusProcess> {


    Long createResumeStatusProcess(Long resumeId, ResumeStatus resumeStatus, ResumeEvent resumeEvent);

    List<ResumeStatusProcess> getProcessByResumeId(Long resumeId);

}
