package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.vo.CheckPriceBean;

/**
 * 提交文字
 * Created by zealjiang on 2016/12/5 11:10.
 * Email: zealjiang@126.com
 */

public interface ISubmit extends IBaseView{
    void requestTxtSucceed(ResponseJson<CheckPriceBean> response,int ifConfirmPrice);
    void submitError(String error);
}
