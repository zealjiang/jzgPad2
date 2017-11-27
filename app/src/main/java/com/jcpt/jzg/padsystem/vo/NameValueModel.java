package com.jcpt.jzg.padsystem.vo;

/**
 * Created by zealjiang on 2016/11/16 15:05.
 * Email: zealjiang@126.com
 */

public class NameValueModel {

    private String name;
    private String value;

    public NameValueModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
