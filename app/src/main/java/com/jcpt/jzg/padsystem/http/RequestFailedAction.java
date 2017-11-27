package com.jcpt.jzg.padsystem.http;

import android.text.TextUtils;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.utils.LogUtil;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 14:35
 * @desc:
 */
public class RequestFailedAction implements Action1<Throwable>{
    private IBaseView view;
    public RequestFailedAction(IBaseView view){
        this.view = view;
    }

    @Override
    public void call(Throwable throwable) {
        view.dismissDialog();
        String error = "";
        if(throwable instanceof ResponseErrorException){

            error = throwable.getMessage();
//            error = "服务器响应异常，请稍后重试";
        }else if(throwable instanceof IOException){
            error = "请检查您的网络后重试";
        }else if(throwable instanceof HttpException){
            HttpException httpException = (HttpException) throwable;
            error = httpException.getMessage();
        }else{
            if (!PadSysApp.networkAvailable)
            error = "没有网络";
            throwable.printStackTrace();
            LogUtil.e("RequestFailedAction",throwable.getMessage());
        }

        if (TextUtils.isEmpty(error))
            error = "服务器响应异常，请稍后重试";
        view.showError(error);
    }
}
