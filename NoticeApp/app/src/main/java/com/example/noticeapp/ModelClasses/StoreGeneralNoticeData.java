package com.example.noticeapp.ModelClasses;

public class StoreGeneralNoticeData {
    String noticeTitle, noticeDate, noticeDay, noticeTime, noticeDetails;

    public StoreGeneralNoticeData(String noticeTitle, String noticeDate, String noticeDay, String noticeTime, String noticeDetails) {
        this.noticeTitle = noticeTitle;
        this.noticeDate = noticeDate;
        this.noticeDay = noticeDay;
        this.noticeTime = noticeTime;
        this.noticeDetails = noticeDetails;
    }

    public StoreGeneralNoticeData() {
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeDay() {
        return noticeDay;
    }

    public void setNoticeDay(String noticeDay) {
        this.noticeDay = noticeDay;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getNoticeDetails() {
        return noticeDetails;
    }

    public void setNoticeDetails(String noticeDetails) {
        this.noticeDetails = noticeDetails;
    }
}
