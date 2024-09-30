package com.achobeta.domain.message.model.vo;

import com.achobeta.domain.message.model.entity.AttachmentFile;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 生命周期只有一次，成员变量对修改关闭，直接通过 builder 构建对象
 */
@Getter
@Builder
public class EmailMessageSendTemplate {

   private String tittle;

   private String content;

   private String stuName;

   private String sendTime;

   private List<AttachmentFile> attachmentInfoList;

}
