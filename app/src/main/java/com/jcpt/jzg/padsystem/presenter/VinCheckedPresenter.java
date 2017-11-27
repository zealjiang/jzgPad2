package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IVInChecked;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.VinCheckedModel;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * Created by wujj on 2017/9/14.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class VinCheckedPresenter extends BasePresenter<IVInChecked> {
    public VinCheckedPresenter(IVInChecked from) {
        super(from);
    }
    public void getVinCheckedResult(String userID, final String vin, String taskId, final MyUniversalDialog myUniversalDialog){
        Map<String,String> params = new HashMap<>();
        params.put("userID",userID);
        params.put("vin", vin);
        params.put("taskId",taskId);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        if (NetWorkTool.isConnect()){
            PadSysApp.getApiServer().getVinCheckedResult(params)
                    .compose(RxThreadUtil.<ResponseJson<VinCheckedModel>>networkSchedulers())
                    .subscribe(new RequestSuccessAction<ResponseJson<VinCheckedModel>>() {

                        @Override
                        public void onSuccess(ResponseJson<VinCheckedModel> response) {
                            baseView.dismissDialog();
                            if (response.getStatus() == 100){
                                baseView.requestVinCheckedSucceed(response,myUniversalDialog,vin);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            baseView.dismissDialog();
                            String message = throwable.getMessage();
                            baseView.requestVinCheckedFailed(message,myUniversalDialog);
                        }
                    });
        }else {
            baseView.dismissDialog();
        }
    }
}
