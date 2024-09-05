package com.achobeta.domain.feedback.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/22
 */
@Getter
@Setter
public class FeedbackMessageVO {

    /**
     * 处理反馈的管理员id
     */
    private Long managerId;

    /**
     * 处理反馈的管理员姓名
     */
    private String managerName;
    /**
     * 消息标题
     */
    private String tittle;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 反馈处理时间
     */
    private LocalDateTime sendTime;

    /**
     * 附件url
     */
    private String attachment;
}
