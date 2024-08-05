package com.achobeta.domain.interview.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.domain.interview.model.converter.InterviewConverter;
import com.achobeta.domain.interview.model.dto.InterviewCreateDTO;
import com.achobeta.domain.interview.model.dto.InterviewUpdateDTO;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.achobeta.domain.paper.model.vo.PaperLibraryVO;
import com.achobeta.domain.paper.model.vo.QuestionPaperDetailVO;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.domain.schedule.service.InterviewScheduleService;
import com.achobeta.domain.schedule.service.InterviewerService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.interview.model.enity.Interview;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.interview.model.dao.mapper.InterviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview(面试表)】的数据库操作Service实现
* @createDate 2024-08-05 23:45:13
*/
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview>
    implements InterviewService{

    private final InterviewMapper interviewMapper;

    private final InterviewerService interviewerService;

    private final PaperQuestionLinkService paperQuestionLinkService;

    private final QuestionPaperService questionPaperService;

    @Override
    public Optional<Interview> getInterview(Long interviewId) {
        return this.lambdaQuery()
                .eq(Interview::getId, interviewId)
                .oneOpt();
    }

    @Override
    public List<InterviewVO> managerGetInterviewList(Long managerId) {
        return interviewMapper.managerGetInterviewList(managerId);
    }

    @Override
    public List<InterviewVO> userGetInterviewList(Long userId) {
        return interviewMapper.userGetInterviewList(userId);
    }

    @Override
    public InterviewDetailVO getInterviewDetail(Long interviewId) {
        return interviewMapper.getInterviewDetail(interviewId);
    }

    @Override
    @Transactional
    public Long createInterview(InterviewCreateDTO interviewCreateDTO, Long managerId) {
        Interview interview = InterviewConverter.INSTANCE.interviewCreateDTOtoInterview(interviewCreateDTO);
        this.save(interview);
        // 管理员参与面试预约
        interviewerService.createInterviewer(managerId, interviewCreateDTO.getScheduleId());
        // 返回面试 id
        return interview.getId();
    }

    @Override
    public void updateInterview(InterviewUpdateDTO interviewUpdateDTO) {
        Interview interview = InterviewConverter.INSTANCE.interviewUpdateDTOtoInterview(interviewUpdateDTO);
        this.updateById(interview);
    }

    @Override
    public void switchInterview(Long interviewId, InterviewStatusEnum interviewStatusEnum) {
        Interview interview = new Interview();
        interview.setId(interviewId);
        interview.setStatus(interviewStatusEnum);
        this.updateById(interview);
    }

    @Override
    @Transactional
    public void setPaperForInterview(Long interviewId, Long paperId) {
        // 拷贝一份试卷
        QuestionPaperDetailVO paperDetail = paperQuestionLinkService.getPaperDetail(paperId);
        List<Long> libIds = paperDetail.getTypes()
                .stream()
                .map(PaperLibraryVO::getId)
                .toList();
        Long newPaperId = questionPaperService.addQuestionPaper(libIds,
                paperDetail.getTitle(), paperDetail.getDescription());
        List<Long> questionIds = paperDetail.getQuestions()
                .stream()
                .map(QuestionVO::getId)
                .toList();
        paperQuestionLinkService.addQuestionsForPaper(paperId, questionIds);
        // 设置试卷
        this.lambdaUpdate()
                .eq(Interview::getId, interviewId)
                .set(Interview::getPaperId, paperId)
                .update();
    }

    @Override
    public void checkInterviewExists(Long interviewId) {
        getInterview(interviewId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_NOT_EXISTS));
    }

    @Override
    public Interview checkAndGetInterviewExists(Long interviewId) {
        return getInterview(interviewId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_NOT_EXISTS));
    }

    @Override
    public void checkInterviewStatus(Long interviewId, InterviewStatusEnum interviewStatusEnum) {
        checkAndGetInterviewExists(interviewId)
                .getStatus()
                .check(interviewStatusEnum);
    }
}




