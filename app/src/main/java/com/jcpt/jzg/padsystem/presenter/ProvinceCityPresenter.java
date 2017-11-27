package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IProvinceCity;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.vo.ProvinceCityModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市
 * Created by zealjiang on 2016/11/17 15:39.
 * Email: zealjiang@126.com
 */

public class ProvinceCityPresenter extends BasePresenter<IProvinceCity> {

    public ProvinceCityPresenter(IProvinceCity from) {
        super(from);
    }

    /**
     * 省
     * @author zealjiang
     * @time 2016/11/17 15:49
     */
    public void getProvince(){
        Map<String,String> params = new HashMap<>();
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getProvince(params)
                .compose(RxThreadUtil.<ResponseJson<List<ProvinceCityModel>>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<List<ProvinceCityModel>>>(){
                    @Override
                    public void onSuccess(ResponseJson<List<ProvinceCityModel>> response) {
                        baseView.dismissDialog();
                        List<ProvinceCityModel> listData = response.getMemberValue();
                        baseView.succeedProvince(listData);
                    }
                },new RequestFailedAction(baseView));
    }

    public void getCity(final String provinceId){
        Map<String,String> params = new HashMap<>();
        params.put("provinceId",provinceId);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getCity(params)
                .compose(RxThreadUtil.<ResponseJson<List<ProvinceCityModel>>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<List<ProvinceCityModel>>>(){
                    @Override
                    public void onSuccess(ResponseJson<List<ProvinceCityModel>> response) {
                        baseView.dismissDialog();
                        List<ProvinceCityModel> listData = response.getMemberValue();
                        baseView.succeedCity(listData);
                    }
                },new RequestFailedAction(baseView));
    }
}
