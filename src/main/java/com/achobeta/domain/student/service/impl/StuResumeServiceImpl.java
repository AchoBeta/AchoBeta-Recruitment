package com.achobeta.domain.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.student.model.dao.mapper.StuResumeMapper;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【stu_resume(学生简历表)】的数据库操作Service实现
* @createDate 2024-07-06 14:20:47
*/
@Service
public class StuResumeServiceImpl extends ServiceImpl<StuResumeMapper, StuResume>
    implements StuResumeService{

    @Override
    public Optional<StuResume> getStuResume(Long batchId, Long stuId) {
        return this.lambdaQuery()
                .eq(StuResume::getUserId, stuId)
                .eq(StuResume::getBatchId, batchId)
                .oneOpt();
    }

    @Override
    public SimpleStudentVO getSimpleStuResume(Long batchId, Long stuId) {
        return getStuResume(batchId, stuId)
                .map(stuResume -> BeanUtil.copyProperties(stuResume, SimpleStudentVO.class))
                .orElseThrow(() ->
                    new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS));
    }

    @Override
    public Integer getGradeByBatchIdAndStuId(Long batchId, Long stuId) {
        return getStuResume(batchId, stuId)
                .orElseThrow(() ->
                        new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_NOT_EXISTS))
                .getGrade();
    }
}




