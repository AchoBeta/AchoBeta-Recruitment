package com.achobeta.domain.interview.machine.context;

import com.achobeta.domain.interview.model.entity.Interview;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 0:41
 */
@Data
public class InterviewContext {

    private Long managerId;

    private Interview interview;

}
