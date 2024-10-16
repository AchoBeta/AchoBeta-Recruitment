package com.achobeta.domain.paper.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.paper.constants.QuestionPaperConstants;
import com.achobeta.domain.paper.model.dao.mapper.PaperQuestionLinkMapper;
import com.achobeta.domain.paper.model.dao.mapper.QuestionPaperLibraryMapper;
import com.achobeta.domain.paper.model.entity.PaperQuestionLink;
import com.achobeta.domain.paper.model.vo.PaperLibraryVO;
import com.achobeta.domain.paper.model.vo.QuestionPaperDetailVO;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 马拉圈
* @description 针对表【paper_question_link(试卷-问题关联表)】的数据库操作Service实现
* @createDate 2024-07-05 22:38:52
*/
@Service
@RequiredArgsConstructor
public class PaperQuestionLinkServiceImpl extends ServiceImpl<PaperQuestionLinkMapper, PaperQuestionLink>
    implements PaperQuestionLinkService{

    private final PaperQuestionLinkMapper paperQuestionLinkMapper;

    private final QuestionPaperLibraryMapper questionPaperLibraryMapper;

    private final QuestionPaperService questionPaperService;

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    @Override
    public List<QuestionVO> getQuestionsOnPaper(Long paperId) {
        return paperQuestionLinkMapper.getQuestionsOnPaper(paperId);
    }

    @Override
    public Optional<PaperQuestionLink> getPaperQuestionLink(Long paperId, Long questionId) {
        return this.lambdaQuery()
                .eq(PaperQuestionLink::getPaperId, paperId)
                .eq(PaperQuestionLink::getQuestionId, questionId)
                .oneOpt();
    }

    @Override
    @Transactional
    public void addQuestionsForPaper(Long paperId, List<Long> questionIds) {
        redisLock.tryLockDoSomething(QuestionPaperConstants.PAPER_ADD_QUESTIONS_LOCK + paperId, () -> {
            // 获取试卷的所有题
            Set<Long> hash = getQuestionsOnPaper(paperId).stream().map(QuestionVO::getId).collect(Collectors.toSet());
            // 将不存在于原试卷的题滤出来
            List<PaperQuestionLink> paperQuestionLinks = questionIds.stream()
                    .distinct()
                    .filter(questionId -> Objects.nonNull(questionId) && !hash.contains(questionId))
                    .map(questionId -> {
                        PaperQuestionLink paperQuestionLink = new PaperQuestionLink();
                        paperQuestionLink.setPaperId(paperId);
                        paperQuestionLink.setQuestionId(questionId);
                        return paperQuestionLink;
                    }).toList();
            this.saveBatch(paperQuestionLinks);
        }, () -> {}, simpleLockStrategy);
    }

    @Override
    public void removeQuestionsFromPaper(Long paperId, List<Long> questionIds) {
        if(!CollectionUtils.isEmpty(questionIds)) {
            this.lambdaUpdate()
                    .eq(PaperQuestionLink::getPaperId, paperId)
                    .in(PaperQuestionLink::getQuestionId, questionIds)
                    .remove();
        }
    }

    @Override
    public QuestionPaperDetailVO getPaperDetail(Long paperId) {
        QuestionPaperDetailVO questionPaperDetail = questionPaperLibraryMapper.getQuestionPaperDetail(paperId);
        List<QuestionVO> questions = getQuestionsOnPaper(paperId);
        questionPaperDetail.setQuestions(questions);
        return questionPaperDetail;
    }

    @Override
    @Transactional
    public Long cloneQuestionPaper(Long paperId, String title) {
        // 拷贝试卷的定义
        QuestionPaperDetailVO paperDetail = getPaperDetail(paperId);
        List<Long> libIds = paperDetail.getTypes()
                .stream()
                .map(PaperLibraryVO::getId)
                .toList();
        Long newPaperId = questionPaperService.addQuestionPaper(
                libIds,
                Optional.ofNullable(title).filter(StringUtils::hasText).orElseGet(paperDetail::getTitle),
                paperDetail.getDescription()
        );
        // 拷贝试卷的题目
        List<Long> questionIds = paperDetail.getQuestions()
                .stream()
                .map(QuestionVO::getId)
                .toList();
        addQuestionsForPaper(newPaperId, questionIds);
        return newPaperId;
    }

    @Override
    public void checkQuestionExistInPaper(Long paperId, Long questionId) {
        getPaperQuestionLink(paperId, questionId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTION_NOT_EXISTS_IN_PAPER));
    }
}
