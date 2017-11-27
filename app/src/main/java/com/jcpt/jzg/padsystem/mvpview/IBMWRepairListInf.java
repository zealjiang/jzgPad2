package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/11/15.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.BMWReCheckBean;

import java.util.List;

/**
 * 复检需要维修列表
 */
public interface IBMWRepairListInf extends IBaseView {
    void requestDataSucceed(BMWReCheckBean data);
}
