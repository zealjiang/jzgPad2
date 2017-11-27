package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/6/22.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.HistoryPriceModel;

import java.util.List;


public interface IHistoryPrice extends IBaseView {
    void succeedDetail(List<HistoryPriceModel> listData);
    void succeedCount(int count);
}
