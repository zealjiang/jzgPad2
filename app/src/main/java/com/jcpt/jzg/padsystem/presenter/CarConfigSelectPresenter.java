package com.jcpt.jzg.padsystem.presenter;

import android.text.TextUtils;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.mvpview.ICarConfigSelectInterface;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.NetworkExceptionUtils;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.CarConfigModel;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 车系选择---参数选择
 * Created by zealjiang on 2016/11/2 20:14.
 * Email: zealjiang@126.com
 */

public class CarConfigSelectPresenter extends BasePresenter<ICarConfigSelectInterface> {


    public CarConfigSelectPresenter(ICarConfigSelectInterface from) {
        super(from);
    }

    /**
     * 请求车系参数
     * @author zealjiang
     * @time 2016/11/2 20:30
     */
    public void requestCarConfig(int ModelId) {
        Map<String, String> params = new HashMap<>();
        params.put("ModelId", ModelId+"");
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        //加sign
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getCarConfig(params).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<CarConfigModel>() {
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
                    public void onNext(CarConfigModel data) {
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
