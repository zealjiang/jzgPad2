package com.jcpt.jzg.padsystem.vo.detection;

import com.jcpt.jzg.padsystem.vo.TaskDetailModel;

import java.io.Serializable;
import java.util.List;

/**
 * 附加照片
 * Created by zealjiang on 2017/7/24 18:08.
 * Email: zealjiang@126.com
 */

public class CarPicModel implements Serializable {

    private List<TaskDetailModel.TaskCarPicAdditionalListBean> TaskCarPicAdditionalList;//附加照片

    public List<TaskDetailModel.TaskCarPicAdditionalListBean> getTaskCarPicAdditionalList() {
        return TaskCarPicAdditionalList;
    }

    public void setTaskCarPicAdditionalList(List<TaskDetailModel.TaskCarPicAdditionalListBean> taskCarPicAdditionalList) {
        TaskCarPicAdditionalList = taskCarPicAdditionalList;
    }
}
