package com.jcpt.jzg.padsystem.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IRepairLogListener;
import com.jcpt.jzg.padsystem.utils.CarVINCheck;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.vo.Repairlog;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * 维保
 * author: zyq
 */
public class ActiveRepairLogPresenter extends BasePresenter<IRepairLogListener> {


    public ActiveRepairLogPresenter(IRepairLogListener from) {
        super(from);
    }

    /**
     * 查看维保记录
     * @param vin       vin
     * @param taskId    任务id
     */
    public void loadActiveRepairLog(String vin, String taskId,String userid,String taskSourceId) {
        Map<String, String> params = new HashMap<String,String>();
        params.put("Vin", vin.toUpperCase());
        params.put("taskId",taskId);
        params.put("userid",userid);
        params.put("TaskSourceId",taskSourceId);
        params = MD5Utils.encryptParams(params);

        if (NetWorkTool.isConnect()) {
            PadSysApp.getApiServer().getActiveRepairLog(params)
                    .compose(RxThreadUtil.<ResponseJson<Repairlog>>networkSchedulers())
                    .subscribe(new RequestSuccessAction<ResponseJson<Repairlog>>() {
                        @Override
                        public void onSuccess(ResponseJson<Repairlog> response) {
                            if (response.getStatus() == 100) {
                                //返回成功
                                baseView.repairLogSucceed(response.getMemberValue());
                            } else {
                                baseView.showRepairLogError(response.getMsg());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            baseView.showRepairLogError(throwable.getMessage());
                        }
                    });
        }else{
            baseView.showRepairLogError("没有网络");
        }
    }

    /**
     * 查询维保
     * @param vin
     * @param taskId
     * @param userid
     */
    public void getCarInfoController(String vin, String taskId,String userid,String taskSourceId) {
        if (!NetWorkTool.isConnect()) {
            MyToast.showShort("没有网络");
            return;
        }
        Map<String, String> params = new HashMap<String,String>();
        params.put("Vin", vin.toUpperCase());
        params.put("taskId",taskId);
        params.put("userid",userid);
        params.put("TaskSourceId",taskSourceId);
        params = MD5Utils.encryptParams(params);


        PadSysApp.getApiServer().getCarInfoController(params)
                .compose(RxThreadUtil.<ResponseJson<Repairlog>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<Repairlog>>() {
                    @Override
                    public void onSuccess(ResponseJson<Repairlog> response) {
                        if (response.getStatus() == 100) {
                            //返回成功
                        }else{
                            baseView.showError(response.getMsg());
                        }
                    }
                },new RequestFailedAction(baseView));

    }
}
