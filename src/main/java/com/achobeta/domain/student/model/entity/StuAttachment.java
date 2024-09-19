package com.achobeta.domain.student.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生附件表
 * @TableName stu_attachment
 */
@TableName(value ="stu_attachment")
@Data
public class StuAttachment extends BaseIncrIDEntity implements Serializable {


    /**
     * 学生表主键id
     */
    private Long resumeId;

    /**
     * 附件名
     */
    private String filename;

    /**
     * 附件路径
     */
    private String attachment;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}