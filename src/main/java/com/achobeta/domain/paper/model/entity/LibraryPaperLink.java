package com.achobeta.domain.paper.model.entity;

import com.achobeta.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;

/**
 * @TableName library_paper_link
 */
@TableName(value ="library_paper_link")
@Data
public class LibraryPaperLink extends BaseEntity implements Serializable {

    private Long libId;

    private Long paperId;

    private static final long serialVersionUID = 1L;
}