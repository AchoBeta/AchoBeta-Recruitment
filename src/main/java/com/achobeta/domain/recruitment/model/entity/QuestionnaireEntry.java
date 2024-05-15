package com.achobeta.domain.recruitment.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName questionnaire_entry
 */
@TableName(value ="questionnaire_entry")
@Data
public class QuestionnaireEntry implements Serializable {

    private Long questionnaireId;

    private Long entryId;

    private String answer;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 乐观锁
     */
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Integer version;

    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}