package com.jcpt.jzg.padsystem.vo;

/**
 * Created by 郑有权 on 2016/11/10.
 */

public class DetectItem {

    private int id;
    private String name;
    private int checkResult;

    public DetectItem(int id, String name, int checkResult) {
        this.id = id;
        this.name = name;
        this.checkResult = checkResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(int checkResult) {
        this.checkResult = checkResult;
    }
}
