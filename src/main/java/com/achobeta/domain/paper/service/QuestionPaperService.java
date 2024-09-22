package com.achobeta.domain.paper.service;

import com.achobeta.domain.paper.model.dto.PaperQueryDTO;
import com.achobeta.domain.paper.model.entity.QuestionPaper;
import com.achobeta.domain.paper.model.vo.PaperQueryVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【question_paper(试卷表)】的数据库操作Service
* @createDate 2024-07-05 22:38:52
*/
public interface QuestionPaperService extends IService<QuestionPaper> {

    PaperQueryVO queryPapers(PaperQueryDTO paperQueryDTO);

    Optional<QuestionPaper> getQuestionPaper(Long paperId);

    @Transactional
    Long addQuestionPaper(List<Long> libIds, String title, String description);

    @Transactional
    void updateQuestionPaper(Long paperId, List<Long> libIds, String title, String description);

    /**
     * 删除一张试卷，可能会导致一些副作用
     * 使用组卷系统的模块需要提供一个事件处理类（继承 RemovePaperHandler 并重写对应方法）
     * 在此方法中不应该调用责任链的入口方法，极易出现循环依赖
     * 在 controller 层调用责任链的入口方法，这样就不挑使用组卷系统的其他模块的事件处理类
     * @param paperId
     */
    @Transactional
    void removeQuestionPaper(Long paperId);

    void checkPaperExists(Long paperId);

}
