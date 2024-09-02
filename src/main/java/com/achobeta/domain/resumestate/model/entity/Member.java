package com.achobeta.domain.resumestate.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;

/**
 * @TableName member
 */
@TableName(value ="member")
@Data
public class Member extends BaseIncrIDEntity implements Serializable {

    private Long resumeId;

    private Long managerId;

    private Long parentId;

    private static final long serialVersionUID = 1L;
}