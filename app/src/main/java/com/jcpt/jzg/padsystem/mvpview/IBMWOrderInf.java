package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/11/15.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;

/**
 * 进入手续信息前获取宝马传过来的信息
 */
public interface IBMWOrderInf extends IBaseView {
    void requestDataSucceed(BMWOrderInfBean data);
}
