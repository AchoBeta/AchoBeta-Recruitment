package com.achobeta.domain.recruitment.model.dao.mapper;

import com.achobeta.domain.recruitment.model.entity.Recruitment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【recruitment(招新表)】的数据库操作Mapper
* @createDate 2024-05-11 02:30:58
* @Entity com.achobeta.domain.recruitment.model.entity.Recruitment
*/
public interface RecruitmentMapper extends BaseMapper<Recruitment> {

    List<Long> getStuIdsByRecId(@Param("recId") Long recId);

}




