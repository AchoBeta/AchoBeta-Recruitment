package com.achobeta.domain.recruit.service;

import com.achobeta.domain.recruit.model.entity.RecruitmentBatch;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【recruitment_batch(招新批次表)】的数据库操作Service
* @createDate 2024-07-06 12:33:02
*/
public interface RecruitmentBatchService extends IService<RecruitmentBatch> {

    // 查询 ------------------------------------------

    Optional<RecruitmentBatch> getRecruitmentBatch(Long batchId);

    /**
     * 活动招新批次列表
     * @param isRun （null：所有、true：启动了的、false：未启动的）
     * @return
     */
    List<RecruitmentBatch> getRecruitmentBatches(Boolean isRun);

    List<Long> getStuIdsByBatchId(Long batchId);

    // 写入 ------------------------------------------

    Long createRecruitmentBatch(Integer batch, String title, Date deadline);

    void updateRecruitmentBatch(Long batchId, String title, Date deadline);

    void shiftRecruitmentBatch(Long batchId, Boolean isRun);

    // 检测 ------------------------------------------

    void checkRecruitmentBatchExists(Long batchId);

    RecruitmentBatch checkAndGetRecruitmentBatch(Long batchId);

    RecruitmentBatch checkAndGetRecruitmentBatchIsRun(Long batchId, Boolean isRun);

}
