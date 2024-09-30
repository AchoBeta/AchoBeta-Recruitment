package com.achobeta.domain.message.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/26
 */
@Getter
@Setter
public class AttachmentFile implements Serializable {
    private String fileName;

    private String attachmentUrl;
}
