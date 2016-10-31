package com.rflpazini.sdf.model;

/**
 * Created by rflpazini on 10/29/16.
 */

public class Message {
    private Integer id;
    private String msgBody;
    private String msgDate;
    private String msgFrom;
    private boolean itsMe;

    public Message() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate.toString();
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public boolean itsMe() {
        return itsMe;
    }

    public void setItsMe(boolean itsMe) {
        this.itsMe = itsMe;
    }
}
