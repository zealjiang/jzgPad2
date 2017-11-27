package com.jcpt.jzg.padsystem.presenter;

import android.text.TextUtils;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.mvpview.ICarDiffInterface;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.NetworkExceptionUtils;
import com.jcpt.jzg.padsystem.vo.CarTypeSelectModel;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 车型选择
 * Created by zealjiang on 2016/11/4 21:05.
 * Email: zealjiang@126.com
 */

public class CarTypeSelectPresenter extends BasePresenter<ICarDiffInterface> {
    public CarTypeSelectPresenter(ICarDiffInterface iCarDiffInterface) {
        super(iCarDiffInterface);
    }


    /**
     * 请求差异项配置
     * 车系Id、车架号、排量、变速器（1自动，2手动，3电动）、驱动方式（3四驱，4两驱）、出厂日期、车辆铭牌
     * @author zealjiang
     * @time 2016/11/9 9:44
     */
    public void requestCarDiff(String modelid,String vin,String pl,String bsx,String qdfs,String productYear,String noticeNo) {
        Map<String, String> params = new HashMap<>();
        params.put("modelid", modelid);
        params.put("vin", vin);
        params.put("pl", pl);
        params.put("bsx", bsx);
        params.put("qdfs", qdfs);
        params.put("productYear", productYear);
        params.put("noticeNo", noticeNo);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        //加sign
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getCarDiff(params).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<CarTypeSelectModel>() {
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
            public void onNext(CarTypeSelectModel data) {
                int status = data.getStatus();
                if (status == 100) {
                    baseView.succeed(data);
                } else {
                    baseView.showError(data.getMsg());
                }
            }
        });
    }


    /**
     * 请求差异项配置
     * 车系Id、车架号、排量、变速器（1自动，2手动，3电动）、驱动方式（3四驱，4两驱）、出厂日期、车辆铭牌
     * @author zealjiang
     * @time 2016/11/9 9:44
     */
    public void requestCarDiff0417(String vin,String productYear,String noticeNo) {
        Map<String, String> params = new HashMap<>();
        params.put("vin", vin);
        params.put("productYear", productYear);
        params.put("noticeNo", noticeNo);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        //加sign
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getCarDiff0417(params).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<CarTypeSelectModel>() {
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
            public void onNext(CarTypeSelectModel data) {
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
