package com.achobeta.domain.paper.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.paper.model.dao.mapper.QuestionPaperMapper;
import com.achobeta.domain.paper.model.entity.LibraryPaperLink;
import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import com.achobeta.domain.paper.service.LibraryPaperLinkService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_paper(试卷表)】的数据库操作Service实现
* @createDate 2024-07-05 22:38:52
*/
@Service
@RequiredArgsConstructor
public class QuestionPaperServiceImpl extends ServiceImpl<QuestionPaperMapper, QuestionPaper>
    implements QuestionPaperService{

    private final QuestionPaperMapper questionPaperMapper;

    private final LibraryPaperLinkService libraryPaperLinkService;

    @Override
    public List<QuestionPaperVO> getQuestionPapers() {
        return BeanUtil.copyToList(this.list(), QuestionPaperVO.class);
    }

    @Override
    public List<QuestionPaperVO> getQuestionPapersByLibId(Long libId) {
        return questionPaperMapper.getQuestionPapersByLibId(libId);
    }

    @Override
    public Optional<QuestionPaper> getQuestionPaper(Long paperId) {
        return this.lambdaQuery()
                .eq(QuestionPaper::getId, paperId)
                .oneOpt();
    }

    @Override
    public Long addQuestionPaper(List<Long> libIds, String title, String description) {
        QuestionPaper questionPaper = new QuestionPaper();
        questionPaper.setTitle(title);
        questionPaper.setDescription(description);
        this.save(questionPaper);
        Long paperId = questionPaper.getId();
        libraryPaperLinkService.addLibraryPaperLinkBatch(libIds, paperId);
        return paperId;
    }

    @Override
    public void updateQuestionPaper(Long paperId, List<Long> libIds, String title, String description) {
        this.lambdaUpdate()
                .eq(QuestionPaper::getId, paperId)
                .set(QuestionPaper::getTitle, title)
                .set(QuestionPaper::getDescription, description)
                .update();
        // 获得这张试卷所在的试卷库（旧）
        Map<Long, Boolean> hash = new HashMap<>();
        libraryPaperLinkService.lambdaQuery()
                .eq(LibraryPaperLink::getPaperId, paperId)
                .list()
                .forEach(link -> hash.put(link.getLibId(), Boolean.FALSE));
        // 这样之后，新增的就是 true，需要删除的就是 false，已存在的则不会出现在 hash 里
        libIds.forEach(libId -> {
            if(hash.containsKey(libId)) {
                hash.remove(libId);
            }else {
                hash.put(libId, Boolean.TRUE);
            }
        });
        // 遍历 hash 进行新增/删除
        hash.forEach((libId, flag) -> {
            if(Boolean.TRUE.equals(flag)) {
                libraryPaperLinkService.addLibraryPaperLink(libId, paperId);
            }else {
                libraryPaperLinkService.removeLibraryPaperLink(libId, paperId);
            }
        });

    }

    @Override
    public void removeQuestionPaper(Long paperId) {
        this.lambdaUpdate()
                .eq(QuestionPaper::getId, paperId)
                .remove();
    }

    @Override
    public void checkPaperExists(Long paperId) {
        getQuestionPaper(paperId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTION_PAPER_NOT_EXISTS));
    }
}




