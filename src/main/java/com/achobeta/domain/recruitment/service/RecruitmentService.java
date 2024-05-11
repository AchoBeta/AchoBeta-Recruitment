package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.recruitment.model.entity.Recruitment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 马拉圈
* @description 针对表【recruitment(招新表)】的数据库操作Service
* @createDate 2024-05-11 02:30:58
*/
public interface RecruitmentService extends IService<Recruitment> {

    Long createRecruitment(Integer batch);

    void checkNotExists(Long id);

}
