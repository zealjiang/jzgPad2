package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseErrorException;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.InsUseRecordInf;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.InsUseRecordModel;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;


/**
 * 出险记录
 * @author zealjiang
 * @time 2017/6/13 10:33
 */
public class InsUseRecordPresenter extends BasePresenter<InsUseRecordInf> {

    public InsUseRecordPresenter(InsUseRecordInf from) {
        super(from);
    }

    public void requestInsUseRecord(String vin,String taskId) {
        if(!PadSysApp.networkAvailable){
            MyToast.showShort("没有网络");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("userId",PadSysApp.getUser().getUserId()+"");
        params.put("vin",vin+"");
        params.put("taskId",taskId+"");
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();

        PadSysApp.getApiServer().getInsUseRecord(params)
                .compose(RxThreadUtil.<ResponseJson<InsUseRecordModel>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<InsUseRecordModel>>() {
                    @Override
                    public void onSuccess(ResponseJson<InsUseRecordModel> response) {
                        baseView.dismissDialog();
                        InsUseRecordModel insUseRecordModel = response.getMemberValue();
                        baseView.insUseRecordSucceed(insUseRecordModel);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        baseView.dismissDialog();
                        if(throwable instanceof ResponseErrorException){
                            String error = throwable.getMessage();
                            baseView.insUseRecordError(error);
                        }
                    }
                });

    }
}
