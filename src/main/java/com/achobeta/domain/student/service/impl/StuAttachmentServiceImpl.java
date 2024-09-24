package com.achobeta.domain.student.service.impl;

import com.achobeta.domain.student.model.dao.mapper.StuAttachmentMapper;
import com.achobeta.domain.student.model.entity.StuAttachment;
import com.achobeta.domain.student.service.StuAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author cattleyuan
* @description 针对表【stu_attachment(学生附件表)】的数据库操作Service实现
* @createDate 2024-07-08 23:01:40
*/
@Service
public class StuAttachmentServiceImpl extends ServiceImpl<StuAttachmentMapper, StuAttachment>
    implements StuAttachmentService{

    @Override
    public List<StuAttachment> listByResumeId(Long resumeId) {
        return this.lambdaQuery()
                .eq(StuAttachment::getResumeId, resumeId)
                .list();
    }
}




