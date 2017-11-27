package com.jcpt.jzg.padsystem.http;

import com.google.gson.annotations.SerializedName;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 14:23
 * @desc:
 */
public class ResponseJson<T> {
    @SerializedName("MemberValue")
    private T MemberValue;
    @SerializedName("status")
    private int status;
    @SerializedName("msg")
    private String msg;

    public T getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(T memberValue) {
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
