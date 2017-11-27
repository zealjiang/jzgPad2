package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/11/15.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;

/**
 * 通用zj
 */
public interface ISucceedInf<T> extends IBaseView {
    void requestDataSucceed(T data);
}
