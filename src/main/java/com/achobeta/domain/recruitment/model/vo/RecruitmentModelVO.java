package com.achobeta.domain.recruitment.model.vo;

import lombok.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 23:49
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentModelVO {

    private RecruitmentVO recruitmentVO;

    private List<CustomEntryVO> customEntryVOS;

    private List<TimePeriodVO> timePeriodVOS;
}
