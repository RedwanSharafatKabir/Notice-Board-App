package com.example.noticeapp.ModelClasses;

public class StoreTeacherData {
    String teachername, teacherPhone, teacherEmail, teacherId;

    public StoreTeacherData(String teachername, String teacherPhone, String teacherEmail, String teacherId) {
        this.teachername = teachername;
        this.teacherPhone = teacherPhone;
        this.teacherEmail = teacherEmail;
        this.teacherId = teacherId;
    }

    public StoreTeacherData() {
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
