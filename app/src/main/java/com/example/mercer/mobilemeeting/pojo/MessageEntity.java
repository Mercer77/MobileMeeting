package com.example.mercer.mobilemeeting.pojo;

/**
 * ..
 * author:liangliangattack 1364744931@.qq.com
 * Administrator on 2019/1/28 10:41.
 */
public class MessageEntity {

    private String time;
    private int from;
    private int to;
    private String message;
    private int isComeMsg;//0表示自己的 1表示收到的

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int isComeMsg() {
        return isComeMsg;
    }

    public void setComeMsg(int comeMsg) {
        isComeMsg = comeMsg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "time='" + time + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", isComeMsg=" + isComeMsg +
                '}';
    }
}
