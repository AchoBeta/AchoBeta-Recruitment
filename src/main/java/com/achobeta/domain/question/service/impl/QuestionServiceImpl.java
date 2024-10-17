package com.achobeta.domain.question.service.impl;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.question.constants.QuestionLibraryConstants;
import com.achobeta.domain.question.model.converter.QuestionConverter;
import com.achobeta.domain.question.model.dao.mapper.QuestionLibraryMapper;
import com.achobeta.domain.question.model.dao.mapper.QuestionMapper;
import com.achobeta.domain.question.model.dto.QuestionDTO;
import com.achobeta.domain.question.model.dto.QuestionLibraryDTO;
import com.achobeta.domain.question.model.dto.QuestionQueryDTO;
import com.achobeta.domain.question.model.dto.QuestionSaveBatchDTO;
import com.achobeta.domain.question.model.entity.LibraryQuestionLink;
import com.achobeta.domain.question.model.entity.Question;
import com.achobeta.domain.question.model.entity.QuestionLibrary;
import com.achobeta.domain.question.model.vo.QuestionDetailVO;
import com.achobeta.domain.question.model.vo.QuestionQueryVO;
import com.achobeta.domain.question.service.LibraryQuestionLinkService;
import com.achobeta.domain.question.service.QuestionLibraryService;
import com.achobeta.domain.question.service.QuestionService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 马拉圈
* @description 针对表【question(问题表)】的数据库操作Service实现
* @createDate 2024-07-05 20:03:35
*/
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    private final QuestionMapper questionMapper;

    private final QuestionLibraryMapper questionLibraryMapper;

    private final QuestionLibraryService questionLibraryService;

    private final LibraryQuestionLinkService libraryQuestionLinkService;

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    /**
     * 流程：dto -> BasePageQuery -> page -> BasePageResult -> vo
     * @param questionQueryDTO 分页参数
     * @return 分页结果
     */
    @Override
    public QuestionQueryVO queryQuestions(QuestionQueryDTO questionQueryDTO) {
        // 解析分页参数获取 page
        IPage<Question> page = null;
        List<Long> libIds = new ArrayList<>();
        if(Objects.isNull(questionQueryDTO)) {
            page = new BasePageQuery().toMpPage();
        } else {
            page = QuestionConverter.INSTANCE.questionQueryDTOToBasePageQuery(questionQueryDTO).toMpPage();
            libIds = questionQueryDTO.getLibIds();
        }
        // 分页
        IPage<Question> questionIPage = questionMapper.queryQuestions(page, libIds);
        // 封装
        BasePageResult<Question> pageResult = BasePageResult.of(questionIPage);
        // 转化
        return QuestionConverter.INSTANCE.basePageResultToQuestionQueryVO(pageResult);
    }

    @Override
    public Optional<Question> getQuestion(Long questionId) {
        return this.lambdaQuery()
                .eq(Question::getId, questionId)
                .oneOpt();
    }

    @Override
    @Transactional
    public Long addQuestion(List<Long> libIds, String title, String standard) {
        Question question = new Question();
        question.setTitle(title);
        question.setStandard(standard);
        this.save(question);
        Long questionId = question.getId();
        libraryQuestionLinkService.addLibraryQuestionLinkBatch(libIds, questionId);
        return questionId;
    }

    @Override
    public void referenceQuestions(Long libId, List<Long> questionIds) {
        redisLock.tryLockDoSomething(QuestionLibraryConstants.QUESTION_LIBRARY_REFERENCE_QUESTIONS_LOCK + libId, () -> {
            Set<Long> hash = questionMapper.getQuestions(List.of(libId)).stream().map(Question::getId).collect(Collectors.toSet());
            List<LibraryQuestionLink> libraryQuestionLinkList = questionIds.stream()
                    .distinct()
                    .filter(questionId -> Objects.nonNull(questionId) && !hash.contains(questionId))
                    .map(questionId -> {
                        LibraryQuestionLink libraryQuestionLink = new LibraryQuestionLink();
                        libraryQuestionLink.setQuestionId(questionId);
                        libraryQuestionLink.setLibId(libId);
                        return libraryQuestionLink;
                    }).toList();
            libraryQuestionLinkService.saveBatch(libraryQuestionLinkList);
        }, () -> {}, simpleLockStrategy);
    }

    @Override
    @Transactional
    public void saveBatchQuestion(QuestionSaveBatchDTO questionSaveBatchDTO) {
        // 批量插入题库
        List<QuestionLibraryDTO> libraries = questionSaveBatchDTO.getLibraries();
        if(!CollectionUtils.isEmpty(libraries)) {
            List<QuestionLibrary> questionLibraries = QuestionConverter.INSTANCE.questionLibraryDTOListToQuestionLibraryList(libraries);
            questionLibraryService.saveBatch(questionLibraries);
        }
        // 批量插入题
        List<QuestionDTO> questions = questionSaveBatchDTO.getQuestions();
        if(!CollectionUtils.isEmpty(questions)) {
            List<Question> questionList = QuestionConverter.INSTANCE.questionDTOListToQuestionList(questions);
            this.saveBatch(questionList);
            // 关联题库
            int size = questionList.size();
            List<LibraryQuestionLink> libraryQuestionLinks = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Long questionId = questionList.get(i).getId();
                List<LibraryQuestionLink> list = questions.get(i).getLibIds().stream().map(libId -> {
                    LibraryQuestionLink libraryQuestionLink = new LibraryQuestionLink();
                    libraryQuestionLink.setLibId(libId);
                    libraryQuestionLink.setQuestionId(questionId);
                    return libraryQuestionLink;
                }).toList();
                libraryQuestionLinks.addAll(list);
            }
            libraryQuestionLinkService.saveBatch(libraryQuestionLinks);
        }
    }

    @Override
    @Transactional
    public void updateQuestion(Long questionId, List<Long> libIds, String title, String standard) {
        this.lambdaUpdate()
                .eq(Question::getId, questionId)
                .set(Question::getTitle, title)
                .set(Question::getStandard, standard)
                .update();
        // 获取这个问题所在的题库（旧）
        Map<Long, Boolean> hash = new HashMap<>();
        libraryQuestionLinkService.lambdaQuery()
                .eq(LibraryQuestionLink::getQuestionId, questionId)
                .list()
                .forEach(link -> hash.put(link.getLibId(), Boolean.FALSE));
        // 这样之后，新增的就是 true，需要删除的就是 false，已存在的则不出现在 hash 里
        libIds.forEach(libId -> {
            if (hash.containsKey(libId)) {
                hash.remove(libId);
            } else {
                hash.put(libId, Boolean.TRUE);
            }
        });
        // 遍历 hash 进行新增/删除
        hash.forEach((libId, flag) -> {
            if (Boolean.TRUE.equals(flag)) {
                libraryQuestionLinkService.addLibraryQuestionLink(libId, questionId);
            } else {
                libraryQuestionLinkService.removeLibraryQuestionLink(libId, questionId);
            }
        });

    }

    @Override
    public void removeQuestion(Long questionId) {
        this.lambdaUpdate()
                .eq(Question::getId, questionId)
                .remove();
    }

    @Override
    public void checkQuestionExists(Long questionId) {
        getQuestion(questionId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.QUESTION_NOT_EXISTS));
    }

    @Override
    public QuestionDetailVO getQuestionDetail(Long questionId) {
        return questionLibraryMapper.getQuestionDetail(questionId);
    }

}




