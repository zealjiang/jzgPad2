package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.ICarInfoInterface;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.vo.CarInfoModel;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * 通过VIN获取（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
 */
public class CarDataByVinPresenter extends BasePresenter<ICarInfoInterface> {
    public CarDataByVinPresenter(ICarInfoInterface iCarInfoInterface) {
        super(iCarInfoInterface);
    }

    /**
     * 通过VIN获取车辆数据
     * @param vin
     */
    public void requestCarInfo(String vin) {
        Map<String, String> params = new HashMap<>();
        params.put("vin", vin);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        //加sign
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getDataByVin(params)
                .compose(RxThreadUtil.<ResponseJson<CarInfoModel>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<CarInfoModel>>() {
                    @Override
                    public void onSuccess(ResponseJson<CarInfoModel> response) {
                        baseView.dismissDialog();
                        CarInfoModel carInfo = response.getMemberValue();
                        baseView.succeedCarInfo(carInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        baseView.dismissDialog();
                        String error = throwable.getMessage();
                        baseView.showError(error);
                        baseView.succeedCarInfo(null);
                    }
                });
    }


}
