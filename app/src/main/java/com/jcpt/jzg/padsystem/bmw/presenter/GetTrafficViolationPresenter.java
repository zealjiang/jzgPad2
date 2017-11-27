package com.jcpt.jzg.padsystem.bmw.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IBMWTrafficViolationInf;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.TrafficViolationBean;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * 宝马违章查询
 * http://192.168.0.140:8066/Api/CarInfo/TrafficViolation
 */
public class GetTrafficViolationPresenter extends BasePresenter<IBMWTrafficViolationInf> {
    public GetTrafficViolationPresenter(IBMWTrafficViolationInf from) {
        super(from);
    }


    public void getTrafficViolation(String taskId,String vin,String engineNume,String plateNum){
        if(!PadSysApp.networkAvailable){
            MyToast.showShort("没有网络");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("taskId",taskId);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params.put("VIN",vin+"");
        params.put("EngineNum",engineNume+"");
        params.put("PlateNum",plateNum+"");
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getTrafficViolation(params)
                .compose(RxThreadUtil.<ResponseJson<TrafficViolationBean>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<TrafficViolationBean>>() {
                    @Override
                    public void onSuccess(ResponseJson<TrafficViolationBean> response) {
                        baseView.dismissDialog();
                        TrafficViolationBean data = response.getMemberValue();
                        baseView.requestDataSucceed(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        baseView.dismissDialog();
                        baseView.reqTraViolationFaild(throwable.getMessage());
                    }
                });
    }
}
