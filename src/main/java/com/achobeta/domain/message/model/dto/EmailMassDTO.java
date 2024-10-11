package com.achobeta.domain.message.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-11
 * Time: 10:16
 */
@Data
public class EmailMassDTO {

    /**
     * 消息标题
     */
    @NotBlank(message = "消息标题不能为空")
    private String tittle;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

    @NotEmpty(message = "用户 id 列表不能为空")
    private List<Long> userIds;

}
