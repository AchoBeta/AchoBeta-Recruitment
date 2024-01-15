package com.achobeta.domain.email.component.po;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 封装这个对象，传给AtomicEmailSender就可以了
 */
@Data
public class Email implements Serializable {


    private String sender;

    String[] recipient;

    String[] cc;

    private String title;

    private String content;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public void setRecipient(String... recipient) {
        this.recipient = recipient;
    }

    public void setCc(String... cc) {
        this.cc = cc;
    }
    public void setRecipient(@NonNull String recipient) {
        this.recipient = new String[]{recipient};
    }

    public void setCc(@NonNull String cc) {
        this.cc = new String[]{cc};
    }

    public String[] getRecipient() {
        return recipient;
    }


    public String[] getCc() {
        return cc;
    }

    public String getRecipient(int i) {
        return recipient != null ? recipient[i] : null;
    }

    public String getCc(int i) {
        return cc != null ? cc[i] : null;
    }

}