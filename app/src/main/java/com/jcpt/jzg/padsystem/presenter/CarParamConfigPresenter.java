package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IAdmixedInf;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.vo.AdmixedData;

import java.util.HashMap;
import java.util.Map;

/**
 * 车辆参配
 * Created by zealjiang on 2016/12/6 14:04.
 * Email: zealjiang@126.com
 */

public class CarParamConfigPresenter extends BasePresenter<IAdmixedInf> {
    public CarParamConfigPresenter(IAdmixedInf from) {
        super(from);
    }
    public void requestAdmixedData(final String styleid,final String IsDiff){
        Map<String,String> params = new HashMap<>();
        params.put("userId", PadSysApp.getUser().getUserId()+"");
        params.put("StyleID", styleid);
        params.put("IsDiff", IsDiff);
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getAdmixedData(params)
                .compose(RxThreadUtil.<ResponseJson<AdmixedData>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<AdmixedData>>(){
                    @Override
                    public void onSuccess(ResponseJson<AdmixedData> response) {
                        baseView.dismissDialog();
                        AdmixedData admixedData = response.getMemberValue();
                        baseView.succeed(admixedData);
                    }
                },new RequestFailedAction(baseView));
    }
}