package com.achobeta.domain.recruitment.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 16:19
 */
@Data
public class EntryVO {

    @TableField("id")
    private Long id;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

}
