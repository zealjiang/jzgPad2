package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * 违章
 * Created by zealjiang on 2017/11/13 14:15.
 * Email: zealjiang@126.com
 */

public class TrafficViolationBean implements Serializable {
    private boolean HasData;

    public boolean isHasData() {
        return HasData;
    }

    public void setHasData(boolean hasData) {
        HasData = hasData;
    }
}
