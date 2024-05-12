package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.recruitment.model.entity.Recruitment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
* @author 马拉圈
* @description 针对表【recruitment(招新表)】的数据库操作Service
* @createDate 2024-05-11 02:30:58
*/
public interface RecruitmentService extends IService<Recruitment> {

    Long createRecruitment(Integer batch, Date deadline);

    Recruitment getRecruitmentById(Long id);

    void checkNotExists(Long id);

    void checkNotRun(Long id);

    void checkRun(Long id);

    List<Long> getStuIdsByRecId(Long recId);

    void shiftRecruitment(Long recId, Boolean isRun);

}
