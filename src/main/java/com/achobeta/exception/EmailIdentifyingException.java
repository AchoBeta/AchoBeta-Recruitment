package com.achobeta.exception;

import com.achobeta.common.constants.EmailStatusCode;
import com.achobeta.common.constants.GlobalServiceStatusCode;

import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 马拉圈
 * Date: 2024-01-17
 * Time: 12:14
 */
public class EmailIdentifyingException extends RuntimeException {

    private List<EmailStatusCode> codeList;

    private void setCodeList(List<EmailStatusCode> codeList) {
        this.codeList = codeList;
    }

    public String getMessageList() {
        if(Objects.isNull(this.codeList)) {
            return "";
        }else {
            return this.codeList.stream()
                    .distinct()
                    .map(EmailStatusCode::getMessage)
                    .reduce((x, y) ->  String.format("%s、%s", x, y))
                    .orElse("");

        }
    }

    public EmailIdentifyingException(String message) {
        super(message);
    }

    public EmailIdentifyingException(List<EmailStatusCode> codeList) {
        super("");
        setCodeList(codeList);
    }

    public EmailIdentifyingException(String message, List<EmailStatusCode> codeList) {
        super(message);
        setCodeList(codeList);
    }

}
