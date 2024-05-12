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

import java.util.Date;
import java.util.List;
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

    private final RecruitmentMapper recruitmentMapper;

    @Override
    public Long createRecruitment(Integer batch, Date deadline) {
        Recruitment recruitment = new Recruitment();
        recruitment.setBatch(batch);
        recruitment.setDeadline(deadline);
        this.save(recruitment);
        return recruitment.getId();
    }

    @Override
    public void checkNotExists(Long id) {
        if(Objects.isNull(this.getById(id))) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RECRUITMENT_NOT_EXISTS);
        }
    }

    @Override
    public List<Long> getStuIdsByRecId(Long recId) {
        return recruitmentMapper.getStuIdsByRecId(recId);
    }

    @Override
    public void shiftRecruitment(Long recId, Boolean isRun) {
        this.lambdaUpdate()
                .eq(Recruitment::getId, recId)
                .set(Recruitment::getIsRun, isRun)
                .update();
    }

}




