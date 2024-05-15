package com.achobeta.domain.recruitment.model.dao.mapper;

import com.achobeta.domain.recruitment.model.entity.RecruitmentActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【recruitment_activity(招新活动表)】的数据库操作Mapper
* @createDate 2024-05-15 11:17:37
* @Entity com.achobeta.domain.recruitment.model.entity.RecruitmentActivity
*/
public interface RecruitmentActivityMapper extends BaseMapper<RecruitmentActivity> {

    List<Long> getStuIdsByRecId(@Param("recId") Long recId);

}




