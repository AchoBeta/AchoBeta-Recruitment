package com.achobeta.domain.feishu.model.entity;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 飞书资源表
 * @TableName feishu_resource
 */
@TableName(value ="feishu_resource")
@Data
public class FeishuResource extends BaseIncrIDEntity implements Serializable {

    /**
     * 任务 ID
     */
    private String ticket;

    /**
     * 上传时的文件名
     */
    private String originalName;

    /**
     * 导入云文档的 token
     */
    private String token;

    /**
     * 导入的在线云文档类型
     */
    private String type;

    /**
     * 导入云文档的 URL
     */
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}