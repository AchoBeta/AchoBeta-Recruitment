package com.achobeta.domain.feedback.model.dto;

import com.achobeta.domain.message.model.dto.MessageContentDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/5
 */
@Getter
@Setter
public class HandleFeedbackDTO implements Serializable {
    /**
     * 反馈id
     */
    @NotNull(message = "反馈 id 不能为空")
    private Long feedbackId;

    /**
     * 反馈处理内容
     */
    @Valid
    @NotNull(message = "反馈处理内容不能为空")
    private MessageContentDTO messageContentDTO;
}
