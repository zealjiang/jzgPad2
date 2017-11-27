package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * 出险记录
 * @author zealjiang
 * @time 2017/6/13 10:41
 */
public class InsUseRecordModel implements Serializable{

    private String ClaimsHistoryURL;

    public String getClaimsHistoryURL() {
        return ClaimsHistoryURL;
    }

    public void setClaimsHistoryURL(String claimsHistoryURL) {
        ClaimsHistoryURL = claimsHistoryURL;
    }
}
