package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/11/15.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.BMWReCheckBean;
import com.jcpt.jzg.padsystem.vo.TrafficViolationBean;

/**
 * 违章
 */
public interface IBMWTrafficViolationInf extends IBaseView {
    void requestDataSucceed(TrafficViolationBean data);
    void reqTraViolationFaild(String data);
}
