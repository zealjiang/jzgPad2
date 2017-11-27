package com.jcpt.jzg.padsystem.bmw.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IBMWSubmitRecheckInf;
import com.jcpt.jzg.padsystem.mvpview.ISucceedInf;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 复检信息提交
 * http://192.168.0.140:8066/Api/tasksave/SaveCosmetology
 */
public class SubmitReCheckPresenter extends BasePresenter<IBMWSubmitRecheckInf> {
    public SubmitReCheckPresenter(IBMWSubmitRecheckInf from) {
        super(from);
    }


    public void saveCosmetology(String taskId,String jsonStr){
        if(!PadSysApp.networkAvailable){
            MyToast.showShort("没有网络");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("taskId",taskId);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params.put("Jsonstring",jsonStr);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().saveCosmetology(params)
                .compose(RxThreadUtil.<ResponseJson<String>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<String>>(){

                    @Override
                    public void onSuccess(ResponseJson<String> response) {
                        baseView.dismissDialog();
                        baseView.requestDataSucceed("");
                    }
                },new RequestFailedAction(baseView));
    }
}
