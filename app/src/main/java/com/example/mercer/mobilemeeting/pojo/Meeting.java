package com.example.mercer.mobilemeeting.pojo;

public class Meeting {
    String meetingTitle;
    String meetingContent;
    String createTime;
    String time;
    String path;
    String address;

    public Meeting() {
    }

    public Meeting(String meetingTitle, String meetingContent, String createTime, String time, String path, String address) {
        this.meetingTitle = meetingTitle;
        this.meetingContent = meetingContent;
        this.createTime = createTime;
        this.time = time;
        this.path = path;
        this.address = address;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public String getMeetingContent() {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
