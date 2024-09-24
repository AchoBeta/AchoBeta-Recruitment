package com.achobeta.domain.student.service;

import com.achobeta.domain.student.model.entity.StuAttachment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86150
* @description 针对表【stu_attachment(学生附件表)】的数据库操作Service
* @createDate 2024-07-08 23:01:40
*/
public interface StuAttachmentService extends IService<StuAttachment> {

    List<StuAttachment> listByResumeId(Long resumeId);

}
