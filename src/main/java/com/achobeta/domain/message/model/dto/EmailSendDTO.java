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
 * @date 2024/9/24
 */
@Getter
@Setter
public class EmailSendDTO implements Serializable {
    /**
     * 消息发送用户对象列表
     */
    @NotEmpty(message = "消息发送用户对象列表不能为空")
    List<StuBaseInfoDTO> stuInfoSendList;

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

}
