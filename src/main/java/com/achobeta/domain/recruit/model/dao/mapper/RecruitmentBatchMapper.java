package com.achobeta.domain.recruit.model.dao.mapper;

import com.achobeta.domain.recruit.model.entity.RecruitmentBatch;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【recruitment_batch(招新批次表)】的数据库操作Mapper
* @createDate 2024-07-06 12:33:02
* @Entity com.achobeta.domain.recruit.model.entity.RecruitmentBatch
*/
public interface RecruitmentBatchMapper extends BaseMapper<RecruitmentBatch> {

    List<SimpleStudentVO> getStuResumeByBatchId(@Param("batchId") Long batchId);

}




