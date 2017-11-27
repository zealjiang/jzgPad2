package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.ITaskStatus;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * Created by wujj on 2017/10/17.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class TaskStatusPresenter extends BasePresenter<ITaskStatus> {
    public TaskStatusPresenter(ITaskStatus from) {
        super(from);
    }
    public void GetTaskStatus(String taskId){
        Map<String,String> params = new HashMap<>();
        params.put("taskId",taskId);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        if (NetWorkTool.isConnect()){
            PadSysApp.getApiServer().GetTaskStatus(params)
                    .compose(RxThreadUtil.<ResponseJson<Integer>>networkSchedulers())
                    .subscribe(new RequestSuccessAction<ResponseJson<Integer>>() {

                        @Override
                        public void onSuccess(ResponseJson<Integer> response) {
                            baseView.dismissDialog();
                            if (response.getStatus() == 100){
                                baseView.requestTaskStatusSucceed(response);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            baseView.dismissDialog();
                            String message = throwable.getMessage();
                            baseView.requestTaskStatusFailed(message);
                        }
                    });
        }else {
            baseView.dismissDialog();
        }
    }
}
