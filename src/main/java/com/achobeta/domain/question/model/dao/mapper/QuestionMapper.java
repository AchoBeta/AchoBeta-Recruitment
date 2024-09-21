package com.achobeta.domain.question.model.dao.mapper;

import com.achobeta.domain.question.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
* @author 马拉圈
* @description 针对表【question(问题表)】的数据库操作Mapper
* @createDate 2024-07-05 20:03:35
* @Entity com.achobeta.domain.question.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    // 并不会将结果集加入 page，而是返回值
    IPage<Question> queryQuestions(IPage<?> page, @Param("libId") Long libId);

}




