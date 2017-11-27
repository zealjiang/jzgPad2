package com.jcpt.jzg.padsystem.presenter;

import android.text.TextUtils;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.mvpview.ICarTypeInterface;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.NetworkExceptionUtils;
import com.jcpt.jzg.padsystem.vo.CarTypeModel;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 车型选择
 * @author zealjiang
 * @time 2017/4/19 17:54
 */
public class CarTypePresenter extends BasePresenter<ICarTypeInterface> {
    public CarTypePresenter(ICarTypeInterface iCarTypeInterface) {
        super(iCarTypeInterface);
    }


    /**
     * 请求车型列表
     * @author zealjiang
     * @time 2017/4/19 17:58
     */
    public void requestCarType(String modelid) {
        Map<String, String> params = new HashMap<>();
        params.put("modelid", modelid);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        //加sign
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getStyleList(params).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<CarTypeModel>() {
            @Override
            public void onCompleted() {
                baseView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                baseView.dismissDialog();
                if (e != null && baseView != null) {
                    String error = NetworkExceptionUtils.getErrorByException(e);
                    if (!TextUtils.isEmpty(error)) {
                        baseView.showError(error);
                    }
                }
            }

            @Override
            public void onNext(CarTypeModel data) {
                int status = data.getStatus();
                if (status == 100) {
                    baseView.succeed(data);
                } else {
                    baseView.showError(data.getMsg());
                }
            }
        });
    }

}
