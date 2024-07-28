package com.achobeta.domain.paper.service.impl;

import com.achobeta.domain.paper.model.dao.mapper.PaperQuestionLinkMapper;
import com.achobeta.domain.paper.model.dao.mapper.QuestionPaperLibraryMapper;
import com.achobeta.domain.paper.model.entity.PaperQuestionLink;
import com.achobeta.domain.paper.model.vo.QuestionPaperDetailVO;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        Set<Long> hash = new HashSet<>();
        // 获取试卷的所有题
        getQuestionsOnPaper(paperId).forEach(questionVO -> hash.add(questionVO.getId()));
        // 将不存在于原试卷的题滤出来
        List<PaperQuestionLink> paperQuestionLinks = questionIds.stream()
                .filter(questionId -> !hash.contains(questionId))
                .map(questionId -> {
                    PaperQuestionLink paperQuestionLink = new PaperQuestionLink();
                    paperQuestionLink.setPaperId(paperId);
                    paperQuestionLink.setQuestionId(questionId);
                    return paperQuestionLink;
                }).toList();
        this.saveBatch(paperQuestionLinks);
    }

    @Override
    public void removeQuestionsFromPaper(Long paperId, List<Long> questionIds) {
        this.lambdaUpdate()
                .eq(PaperQuestionLink::getPaperId, paperId)
                .in(PaperQuestionLink::getQuestionId, questionIds)
                .remove();
    }

    @Override
    public QuestionPaperDetailVO getPaperDetail(Long paperId) {
        QuestionPaperDetailVO questionPaperDetail = questionPaperLibraryMapper.getQuestionPaperDetail(paperId);
        List<QuestionVO> questions = getQuestionsOnPaper(paperId);
        questionPaperDetail.setQuestions(questions);
        return questionPaperDetail;
    }
}




