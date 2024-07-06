package com.achobeta.domain.qpaper.service.impl;

import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.achobeta.domain.qpaper.model.dao.mapper.QuestionPaperLibraryMapper;
import com.achobeta.domain.qpaper.model.vo.QuestionPaperDetailVO;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.qpaper.model.entity.PaperQuestionLink;
import com.achobeta.domain.qpaper.service.PaperQuestionLinkService;
import com.achobeta.domain.qpaper.model.dao.mapper.PaperQuestionLinkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void addQuestionsForPaper(Long paperId, List<Long> questionIds) {
        Map<Long, Boolean> hash = new HashMap<>();
        // 获取试卷的所有题，并标记为 false
        getQuestionsOnPaper(paperId).forEach(questionVO -> hash.put(questionVO.getId(), Boolean.FALSE));
        // questionIds 导入 hash
        questionIds.forEach(questionId -> {
            if(hash.containsKey(questionId)) {
                hash.remove(questionId);
            }else {
                hash.put(questionId, Boolean.TRUE);
            }
        });
        // true 的新增，false 的忽略
        List<PaperQuestionLink> paperQuestionLinks = hash.entrySet().stream()
                .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
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




