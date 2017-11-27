package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IProvinceCityUnique;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.ProvinceCityUniqueModel;

import java.util.HashMap;
import java.util.Map;


/**
 * 根据车牌号码获取上牌地区
 * @author zealjiang
 * @time 2017/4/20 18:54
 */
public class ProvinceCityUniquePresenter extends BasePresenter<IProvinceCityUnique> {

    public ProvinceCityUniquePresenter(IProvinceCityUnique from) {
        super(from);
    }


    public void getProvinceCityUnique(String plateNumber){
        Map<String,String> params = new HashMap<>();
        params.put("plateNumber",plateNumber);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getProvinceCityUnique(params)
                .compose(RxThreadUtil.<ResponseJson<ProvinceCityUniqueModel>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<ProvinceCityUniqueModel>>(){
                    @Override
                    public void onSuccess(ResponseJson<ProvinceCityUniqueModel> response) {
                        baseView.dismissDialog();
                        ProvinceCityUniqueModel data = response.getMemberValue();
                        baseView.succeed(data);
                    }
                },new RequestFailedAction(baseView));
    }



}
