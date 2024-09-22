package com.achobeta.domain.question.model.vo;

import com.achobeta.common.base.BasePageResult;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 14:42
 */
@Data // @Data 无法在本类生成父类的 Setter 等相关的方法
public class QuestionQueryVO extends BasePageResult<QuestionVO> {

}
