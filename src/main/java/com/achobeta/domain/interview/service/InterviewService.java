package com.achobeta.domain.interview.service;

import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.domain.interview.model.dto.InterviewCreateDTO;
import com.achobeta.domain.interview.model.dto.InterviewUpdateDTO;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview(面试表)】的数据库操作Service
* @createDate 2024-08-05 23:45:13
*/
public interface InterviewService extends IService<Interview> {

    // 查询 ------------------------------------------

    Optional<Interview> getInterview(Long interviewId);

    List<InterviewVO> getInterviewListByScheduleId(Long scheduleId);

    List<InterviewDetailVO> managerGetInterviewList(Long managerId, InterviewConditionDTO condition);

    OnlineResourceVO printAllInterviewList(Long managerId, InterviewConditionDTO condition, ResourceAccessLevel level, Boolean synchronous);

    List<InterviewVO> userGetInterviewList(Long userId, InterviewConditionDTO condition);

    InterviewDetailVO getInterviewDetail(Long interviewId);

    Long getInterviewPaperId(Long interviewId);

    // 写入 ------------------------------------------

    Long createInterview(InterviewCreateDTO interviewCreateDTO, Long managerId);

    void updateInterview(InterviewUpdateDTO interviewUpdateDTO);

    void switchInterview(Long interviewId, InterviewStatus interviewStatus);

    InterviewStatus executeInterviewStateEvent(InterviewEvent interviewEvent, InterviewContext interviewContext);

    void setPaperForInterview(Interview interview, Long paperId);

    // 检测 ------------------------------------------

    void checkInterviewExists(Long interviewId);

    Interview checkAndGetInterviewExists(Long interviewId);

    void checkInterviewStatus(Long interviewId, List<InterviewStatus> interviewStatus);

}
