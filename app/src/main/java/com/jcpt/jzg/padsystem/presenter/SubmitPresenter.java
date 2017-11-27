package com.jcpt.jzg.padsystem.presenter;

import com.blankj.utilcode.utils.StringUtils;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.ISubmit;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.CheckPriceBean;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * @author zealjiang
 * @time 2016/12/5 10:49
 */
public class SubmitPresenter extends BasePresenter<ISubmit> {
    public SubmitPresenter(ISubmit from) {
        super(from);
    }

    public void submit(String json, final int ifConfirmPrice, String confirmPrice) {

        Map<String, String> params = new HashMap<>();
        params.put("jsonString", json);
        params.put("UserId", PadSysApp.getUser().getUserId() + "");
        params.put("confirmPrice", confirmPrice);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        //注意： 这个地方不能showDialog()因为当前在"评估结果检查"界面，此处的baseView在DeteckMainActivity,
        // 如果showDialog,dialog是显示在了DeteckMainActivity，当前"评估结果检查"显示不了
        PadSysApp.getApiServer().submit(params)
                .compose(RxThreadUtil.<ResponseJson<CheckPriceBean>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<CheckPriceBean>>() {

                    @Override
                    public void onSuccess(ResponseJson<CheckPriceBean> response) {
                        baseView.requestTxtSucceed(response, ifConfirmPrice);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        baseView.submitError(StringUtils.isEmpty(throwable.getMessage()) ? "提交出错" : throwable.getMessage());
                    }
                });
    }

}
