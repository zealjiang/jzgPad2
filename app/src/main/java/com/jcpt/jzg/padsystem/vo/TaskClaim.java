package com.jcpt.jzg.padsystem.vo;

/**
 * 郑有权
 * 任务认领
 */
public class TaskClaim {


    /**
     * status : 304
     * msg : 任务被其他人认领
     */

    private int status;
    private String msg;

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
