package com.achobeta.domain.recruitment.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dao.mapper.RecruitmentMapper;
import com.achobeta.domain.recruitment.model.entity.Recruitment;
import com.achobeta.domain.recruitment.service.RecruitmentService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author 马拉圈
* @description 针对表【recruitment(招新表)】的数据库操作Service实现
* @createDate 2024-05-11 02:30:58
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitmentServiceImpl extends ServiceImpl<RecruitmentMapper, Recruitment>
    implements RecruitmentService{

    @Override
    public Long createRecruitment(Integer batch) {
        Recruitment recruitment = new Recruitment();
        recruitment.setBatch(batch);
        this.save(recruitment);
        return recruitment.getId();
    }

    @Override
    public void checkNotExists(Long id) {
        if(Objects.isNull(this.getById(id))) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_NOT_EXISTS);
        }
    }

}




