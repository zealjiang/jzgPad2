package com.jcpt.jzg.padsystem.http;

import com.jcpt.jzg.padsystem.global.Constants;

import rx.functions.Action1;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 14:37
 * @desc:
 */
public abstract class RequestSuccessAction<T extends ResponseJson> implements Action1<T>{
    @Override
    public void call(T t) {
        int status = t.getStatus();
        if(status!= Constants.SUCCESS_STATUS_CODE){

            throw new ResponseErrorException(t.getMsg());
        }
        onSuccess(t);
    }

    public abstract void onSuccess(T response);
}
