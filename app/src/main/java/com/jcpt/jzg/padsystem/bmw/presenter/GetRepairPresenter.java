package com.jcpt.jzg.padsystem.bmw.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IBMWRepairListInf;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.BMWReCheckBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取复检需要维修列表
 * http://192.168.0.140:8066/api/task/GetRepairList?taskid=148883
 */
public class GetRepairPresenter extends BasePresenter<IBMWRepairListInf> {
    public GetRepairPresenter(IBMWRepairListInf from) {
        super(from);
    }


    public void getBMWBackInfo(String taskId){
        if(!PadSysApp.networkAvailable){
            MyToast.showShort("没有网络");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("taskId",taskId);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getRepairList(params)
                .compose(RxThreadUtil.<ResponseJson<BMWReCheckBean>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<BMWReCheckBean>>(){

                    @Override
                    public void onSuccess(ResponseJson<BMWReCheckBean> response) {
                        baseView.dismissDialog();
                        BMWReCheckBean data = response.getMemberValue();
                        baseView.requestDataSucceed(data);
                    }
                },new RequestFailedAction(baseView));
    }
}
