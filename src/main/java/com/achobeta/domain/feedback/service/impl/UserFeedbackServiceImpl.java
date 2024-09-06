package com.achobeta.domain.feedback.service.impl;

import com.achobeta.domain.feedback.model.dao.mapper.UserFeedbackMapper;
import com.achobeta.domain.feedback.model.entity.UserFeedback;
import com.achobeta.domain.feedback.service.UserFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author cattleyuan
* @description 针对表【user_feedback】的数据库操作Service实现
* @createDate 2024-07-10 15:30:01
*/
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback>
    implements UserFeedbackService{

}




