package com.achobeta.domain.recruitment.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 16:12
 */
@Data
public class QuestionnaireVO {

    private Long id;

    private Long stuId;

    private Long recId;

    private List<EntryVO> entryVOS;

    private List<TimePeriodVO> timePeriodVOS;
}
