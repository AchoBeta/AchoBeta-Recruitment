package com.achobeta.domain.recruit.model.vo;

import com.achobeta.domain.student.model.vo.StuSimpleResumeVO;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 0:51
 */

@Data
public class ParticipationQuestionVO {

    private Long id;

    private StuSimpleResumeVO stuSimpleResumeVO;

    private List<QuestionAnswerVO> questionAnswerVOS;
}
