package com.achobeta.domain.recruit.model.vo;

import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-07
 * Time: 13:22
 */
@Data
public class ParticipationDetailVO extends ParticipationVO{

    private SimpleStudentVO simpleStudentVO;

}
