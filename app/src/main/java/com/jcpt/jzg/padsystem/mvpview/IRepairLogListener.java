package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.Repairlog;

import java.util.Map;

/**
 * Created by 郑有权 on 2016/11/15.
 */

public interface IRepairLogListener extends IBaseView{

    void repairLogSucceed(Repairlog repairlog);

    void showRepairLogError(String error);


}
