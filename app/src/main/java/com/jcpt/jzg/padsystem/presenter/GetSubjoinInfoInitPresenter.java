package com.jcpt.jzg.padsystem.presenter;


import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.ISubjoinInfoView;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wujj on 2016/11/23.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class GetSubjoinInfoInitPresenter extends BasePresenter<ISubjoinInfoView> {
    public GetSubjoinInfoInitPresenter(ISubjoinInfoView from) {
        super(from);
    }
    public void getConfigureByTaskId(String taskId){
        Map<String,String> params = new HashMap<>();
        params.put("taskId",taskId);
        params.put("UserId", PadSysApp.getUser().getUserId()+"");
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        PadSysApp.getApiServer().getConfigure(params)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
                .compose(RxThreadUtil.<ResponseJson<DetectionWrapper>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<DetectionWrapper>>() {
                    @Override
                    public void onSuccess(ResponseJson<DetectionWrapper> response) {
                        if (response.getStatus() == 100){
                            DetectionWrapper data = response.getMemberValue();
                            if (data!=null){
                                baseView.requestSubjoinSucceed(data);
                            }
                        }
                    }
                },new RequestFailedAction(baseView));
    }
}
