package com.jcpt.jzg.padsystem.bmw.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IBMWOrderInf;
import com.jcpt.jzg.padsystem.mvpview.IDetectionMain;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 进入手续信息前获取宝马传过来的信息
 * http://192.168.0.140:8066/api/task/GetBMWBackInfo?taskid=148406
 */
public class GetBMWOrderInfPresenter extends BasePresenter<IBMWOrderInf> {
    public GetBMWOrderInfPresenter(IBMWOrderInf from) {
        super(from);
    }


    public void getBMWBackInfo(String taskId){
        Map<String,String> params = new HashMap<>();
        params.put("taskId",taskId);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getBMWBackInfo(params)
                .compose(RxThreadUtil.<ResponseJson<BMWOrderInfBean>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<BMWOrderInfBean>>(){

                    @Override
                    public void onSuccess(ResponseJson<BMWOrderInfBean> response) {
                        baseView.dismissDialog();
                        BMWOrderInfBean data = response.getMemberValue();
                        baseView.requestDataSucceed(data);
                    }
                },new RequestFailedAction(baseView));
    }
}
