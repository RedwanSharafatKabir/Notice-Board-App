package com.example.noticeapp.ModelClasses;

public class StoreDepartmentNoticeData {
    String departmentName, noticeTitle, noticeDate, noticeDay, noticeTime, noticeDetails;

    public StoreDepartmentNoticeData(String departmentName, String noticeTitle, String noticeDate,
                                     String noticeDay, String noticeTime, String noticeDetails) {
        this.departmentName = departmentName;
        this.noticeTitle = noticeTitle;
        this.noticeDate = noticeDate;
        this.noticeDay = noticeDay;
        this.noticeTime = noticeTime;
        this.noticeDetails = noticeDetails;
    }

    public StoreDepartmentNoticeData() {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
