package com.jcpt.jzg.padsystem.vo;

/**
 * 郑有权
 * 任务退回
 */
public class TaskBack {


    private int status;
    private String msg;
    private String MemberValue;

    public String getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(String memberValue) {
        MemberValue = memberValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
