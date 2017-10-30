package com.techscl.btsearch;

/**
 * Created by songchunlin on 16/7/6.
 */
public class ResponseInfo {
    private int messageCode;
    private String message;
    private Object data;

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
