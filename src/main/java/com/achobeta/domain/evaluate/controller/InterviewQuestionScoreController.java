package com.achobeta.domain.evaluate.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.evaluate.model.dto.QuestionScoreDTO;
import com.achobeta.domain.evaluate.model.vo.InterviewPaperDetailVO;
import com.achobeta.domain.evaluate.service.InterviewQuestionScoreService;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 2:52
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/evaluate/score")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class InterviewQuestionScoreController {

    private final InterviewQuestionScoreService interviewQuestionScoreService;

    private final InterviewService interviewService;

    private final PaperQuestionLinkService paperQuestionLinkService;

    @PostMapping("/mark")
    public SystemJsonResponse markInterviewQuestion(@RequestBody QuestionScoreDTO questionScoreDTO) {
        // 检查
        ValidatorUtils.validate(questionScoreDTO);
        Long interviewId = questionScoreDTO.getInterviewId();
        Long paperId = interviewService.getInterviewPaperId(interviewId);
        Long questionId = questionScoreDTO.getQuestionId();
        paperQuestionLinkService.checkQuestionExistInPaper(paperId, questionId);
        // 评分
        interviewQuestionScoreService.scoreQuestion(interviewId, questionId, questionScoreDTO.getScore());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/detail/{interviewId}")
    public SystemJsonResponse getInterviewPaperDetail(@PathVariable("interviewId") @NotNull Long interviewId) {
        // 查询
        InterviewPaperDetailVO interviewPaperDetail = interviewQuestionScoreService.getInterviewPaperDetail(interviewId);
        return SystemJsonResponse.SYSTEM_SUCCESS(interviewPaperDetail);
    }

}
