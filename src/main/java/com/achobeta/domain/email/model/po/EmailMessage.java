package com.achobeta.domain.email.model.po;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 封装这个对象，传给EmailSender就可以了
 */
@Data
public class EmailMessage implements Serializable {

    private String sender;

    String[] recipient;

    String[] carbonCopy;

    private String title;

    private String content;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public void setRecipient(String... recipient) {
        this.recipient = recipient;
    }

    public void setCarbonCopy(String... cc) {
        this.carbonCopy = cc;
    }

    public void setRecipient(@NonNull String recipient) {
        this.recipient = new String[]{recipient};
    }

    public void setCarbonCopy(@NonNull String cc) {
        this.carbonCopy = new String[]{cc};
    }

    public String[] getRecipient() {
        return recipient;
    }


    public String[] getCarbonCopy() {
        return carbonCopy;
    }

    public String getRecipient(int i) {
        return recipient != null ? recipient[i] : null;
    }

    public String getCarbonCopy(int i) {
        return carbonCopy != null ? carbonCopy[i] : null;
    }

}