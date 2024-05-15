package com.achobeta.domain.paper.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 0:26
 */
@Data
public class PaperQuestionsVO {

    private Long id;

    private String type;

    private String title;

    private List<QuestionEntryVO> questions;

}
