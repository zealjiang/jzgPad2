package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.InsUseRecordModel;

/**
 * 出险记录
 * @author zealjiang
 * @time 2017/6/13 10:39
 */
public interface InsUseRecordInf extends IBaseView {
    void insUseRecordSucceed(InsUseRecordModel insUseRecordModel);
    void insUseRecordError(String msg);
}
