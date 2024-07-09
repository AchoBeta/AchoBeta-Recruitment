package com.achobeta.domain.users.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 学生附件表
 * @TableName stu_attachment
 */
@TableName(value ="stu_attachment")
@Data
public class StuAttachment extends BaseIncrIDEntity implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学生表主键id
     */
    private Long stuId;

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