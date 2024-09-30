package com.achobeta.domain.feedback.model.dto;

import com.achobeta.domain.message.model.dto.MessageContentDTO;
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
    private Long feedbackId;

    /**
     * 反馈处理内容
     */
    private MessageContentDTO messageContentDTO;
}
