package com.jcpt.jzg.padsystem.vo.detection;

import java.io.Serializable;
import java.util.List;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 15:09
 * @desc:
 */
public class ImportantItem implements Serializable{
    private String ImportantId;
    private String ImportantName;
    private List<CheckItem> CheckItemList;

    public String getImportantId() {
        return ImportantId;
    }

    public void setImportantId(String importantId) {
        ImportantId = importantId;
    }

    public String getImportantName() {
        return ImportantName;
    }

    public void setImportantName(String importantName) {
        ImportantName = importantName;
    }

    public List<CheckItem> getCheckItemList() {
        return CheckItemList;
    }

    public void setCheckItemList(List<CheckItem> checkItemList) {
        CheckItemList = checkItemList;
    }

}
