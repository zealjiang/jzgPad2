package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zealjiang on 2017/5/12 14:59.
 * Email: zealjiang@126.com
 */

public class SubmitPartModel implements Serializable {
    //统计时间 统计手续信息、车型选择、车况检测、车辆照片、其他信息的时间
    // TaskTimeRegionList 数据集合      TimeType  操作类型    BeginTime   开始时间   EndTime   结束时间
    private List<LinkedHashMap<String,String>> TaskTimeRegionList = new ArrayList<>();

    public List<LinkedHashMap<String,String>> getTaskTimeRegionList() {
        return TaskTimeRegionList;
    }

    public void setTaskTimeRegionList(List<LinkedHashMap<String,String>> taskTimeRegionList) {
        TaskTimeRegionList = taskTimeRegionList;
    }
}
