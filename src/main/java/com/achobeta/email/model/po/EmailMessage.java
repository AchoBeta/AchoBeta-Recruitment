package com.achobeta.email.model.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 封装这个对象，传给EmailSender就可以了
 */
@Data
public class EmailMessage implements Serializable {

    private String sender; // 可以为空，默认按照邮件发送器实现的用户名

    private String[] recipient;

    private String[] carbonCopy; // 可以为空，默认是 sender，若 sender 也为空，默认按照邮件发送器实现的用户名

    private String title;

    private String content;

    private Date createTime; // 大部分邮箱都不支持根据时间发送邮件，其实可以通过任务调度平台或者消息队列等实现定时发送功能，也更受我们控制

    private static final long serialVersionUID = 1L;

    public void setRecipient(String... recipient) {
        this.recipient = recipient;
    }

    public void setCarbonCopy(String... cc) {
        this.carbonCopy = cc;
    }

    public void setRecipient(String recipient) {
        this.recipient = new String[]{recipient};
    }

    public void setCarbonCopy(String cc) {
        this.carbonCopy = new String[]{cc};
    }

    public String getRecipient(int i) {
        return recipient != null ? recipient[i] : null;
    }

    public String getCarbonCopy(int i) {
        return carbonCopy != null ? carbonCopy[i] : null;
    }

}