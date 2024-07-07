package com.achobeta.domain.paper.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName library_paper_link
 */
@TableName(value ="library_paper_link")
@Data
public class LibraryPaperLink extends BaseIncrIDEntity implements Serializable {

    private Long libId;

    private Long paperId;

    private static final long serialVersionUID = 1L;
}