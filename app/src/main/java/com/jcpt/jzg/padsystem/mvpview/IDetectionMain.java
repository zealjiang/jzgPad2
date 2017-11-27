package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/11/15.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 18:32
 * @desc:
 */
public interface IDetectionMain extends IBaseView {
    void requestDataSucceed(TaskDetailModel data);
}
