package com.achobeta.domain.message.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/21
 */
@Getter
@Setter
public class MessageContentDTO implements Serializable {
    /**
     * 消息发送用户对象列表
     */
    @NotEmpty(message = "消息发送用户对象列表不能为空")
    List<Long> userIdList;

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

    /**
     * 附件url
     */
    private String attachment;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 延时，立即
     */
    @NotBlank(message = "消息发送类型不能为空")
    private String sendType;

}
