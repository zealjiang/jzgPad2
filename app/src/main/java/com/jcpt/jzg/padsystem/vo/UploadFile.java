package com.jcpt.jzg.padsystem.vo;

/**
 * 郑有权
 *
 */
public class UploadFile {

    private String taskId;
    private String fileName;
    private String MD5;
    private int uploadSuccessNo;    //上传成功条数
    private String TaskStatus;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public int getUploadSuccessNo() {
        return uploadSuccessNo;
    }

    public void setUploadSuccessNo(int uploadSuccessNo) {
        this.uploadSuccessNo = uploadSuccessNo;
    }

    public String getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        TaskStatus = taskStatus;
    }

    public UploadFile(String taskId, String fileName, String MD5, int uploadSuccessNo, String taskStatus) {
        this.taskId = taskId;
        this.fileName = fileName;
        this.MD5 = MD5;
        this.uploadSuccessNo = uploadSuccessNo;
        TaskStatus = taskStatus;
    }
}
