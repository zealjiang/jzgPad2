package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IGetTopWarning;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.CheckPriceBean;
import com.jcpt.jzg.padsystem.vo.TopWarningValueBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wujj on 2017/4/24.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class TopWarningPresenter extends BasePresenter<IGetTopWarning> {
    public TopWarningPresenter(IGetTopWarning from) {
        super(from);
    }
    public void getTopWraningValue(String taskID,String styleID,String recordDate,String mileage,String recordCityID,String salePrice){
        Map<String,String> params = new HashMap<>();
        params.put("taskID",taskID);
        params.put("styleID", styleID);
        params.put("recordDate",recordDate);
        params.put("mileage",mileage);
        params.put("recordCityID",recordCityID);
        params.put("SalePrice",salePrice);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getTopWarningValue(params)
                .compose(RxThreadUtil.<ResponseJson<TopWarningValueBean>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<TopWarningValueBean>>(){

                    @Override
                    public void onSuccess(ResponseJson<TopWarningValueBean> response) {
                        baseView.dismissDialog();
                        baseView.requestTopWarningValueSucceed(response);
                    }
                },new RequestFailedAction(baseView));

    }
}
