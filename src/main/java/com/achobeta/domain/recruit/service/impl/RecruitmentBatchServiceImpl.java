package com.achobeta.domain.recruit.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruit.model.dao.mapper.RecruitmentBatchMapper;
import com.achobeta.domain.recruit.model.entity.RecruitmentBatch;
import com.achobeta.domain.recruit.service.RecruitmentBatchService;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【recruitment_batch(招新批次表)】的数据库操作Service实现
* @createDate 2024-07-06 12:33:02
*/
@Service
@RequiredArgsConstructor
public class RecruitmentBatchServiceImpl extends ServiceImpl<RecruitmentBatchMapper, RecruitmentBatch>
    implements RecruitmentBatchService{

    private final RecruitmentBatchMapper recruitmentBatchMapper;

    @Override
    public Optional<RecruitmentBatch> getRecruitmentBatch(Long batchId) {
        return this.lambdaQuery().eq(RecruitmentBatch::getId, batchId).oneOpt();
    }

    @Override
    public List<RecruitmentBatch> getRecruitmentBatches(Boolean isRun) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(isRun), RecruitmentBatch::getIsRun, isRun)
                .list();
    }

    @Override
    public List<SimpleStudentVO> getStuResumeByBatchId(Long batchId) {
        return recruitmentBatchMapper.getStuResumeByBatchId(batchId);
    }

    @Override
    public Long createRecruitmentBatch(Integer batch, String title, Date deadline) {
        RecruitmentBatch recruitmentBatch = new RecruitmentBatch();
        recruitmentBatch.setBatch(batch);
        recruitmentBatch.setTitle(title);
        recruitmentBatch.setDeadline(deadline);
        this.save(recruitmentBatch);
        return recruitmentBatch.getId();
    }

    @Override
    public void updateRecruitmentBatch(Long batchId, String title, Date deadline) {
        this.lambdaUpdate()
                .eq(RecruitmentBatch::getId, batchId)
                .set(RecruitmentBatch::getTitle, title)
                .set(RecruitmentBatch::getDeadline, deadline)
                .update();
    }

    @Override
    public void shiftRecruitmentBatch(Long batchId, Boolean isRun) {
        this.lambdaUpdate()
                .eq(RecruitmentBatch::getId, batchId)
                .set(RecruitmentBatch::getIsRun, isRun)
                .update();
    }

    @Override
    public void checkRecruitmentBatchExists(Long batchId) {
        getRecruitmentBatch(batchId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_BATCH_NOT_EXISTS));
    }

    @Override
    public RecruitmentBatch checkAndGetRecruitmentBatch(Long batchId) {
        return getRecruitmentBatch(batchId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_BATCH_NOT_EXISTS));
    }

    @Override
    public RecruitmentBatch checkAndGetRecruitmentBatchIsRun(Long batchId, Boolean isRun) {
        RecruitmentBatch recruitmentBatch = checkAndGetRecruitmentBatch(batchId);
        if(!recruitmentBatch.getIsRun().equals(isRun)) {
            if(Boolean.TRUE.equals(isRun)) {
                // recruitmentBatch 为 false，未启动
                throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_BATCH_IS_NOT_RUN);
            }else {
                // recruitmentBatch 为 true，未启动
                throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_BATCH_IS_RUN);
            }
        }
        return recruitmentBatch;
    }
}




