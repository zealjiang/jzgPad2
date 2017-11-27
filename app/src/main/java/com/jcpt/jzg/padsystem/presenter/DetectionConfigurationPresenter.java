package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IDetectionConfiguration;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.StartCheck;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/12/6 11:25
 * @desc:
 */
public class DetectionConfigurationPresenter extends BasePresenter<IDetectionConfiguration> {
    public DetectionConfigurationPresenter(IDetectionConfiguration from) {
        super(from);
    }

    public void getConfigureByTaskId(String taskId){
        if(NetWorkTool.isConnect()) {
            Map<String, String> params = new HashMap<>();
            params.put("taskId", taskId);
            params.put("UserId", PadSysApp.getUser().getUserId() + "");
            params = MD5Utils.encryptParams(params);
            LogUtil.e(TAG, UIUtils.getUrl(params));
            baseView.showDialog();
            PadSysApp.getApiServer().getConfigure(params)
                    .compose(RxThreadUtil.<ResponseJson<DetectionWrapper>>networkSchedulers())
                    .subscribe(new RequestSuccessAction<ResponseJson<DetectionWrapper>>() {

                        @Override
                        public void onSuccess(ResponseJson<DetectionWrapper> response) {
                            baseView.dismissDialog();
                            DetectionWrapper data = response.getMemberValue();
                            baseView.requestSucceed(data);
                        }
                    }, new RequestFailedAction(baseView));
        }else{
            baseView.showError("没有网络");
        }
    }


    /**
     * 开始检测 zyq
     * @param taskId
     */
    public void startDetection(String taskId) {
        if (NetWorkTool.isConnect()) {
            Map<String, String> params = new HashMap<>();
            params.put("taskId", taskId);
            params.put("UserId", PadSysApp.getUser().getUserId() + "");
            params = MD5Utils.encryptParams(params);
            LogUtil.e(TAG, UIUtils.getUrl(params));
            baseView.showDialog();
            PadSysApp.getApiServer().getStartCheck(params)
                    .compose(RxThreadUtil.<ResponseJson<StartCheck>>networkSchedulers())
                    .subscribe(new RequestSuccessAction<ResponseJson<StartCheck>>() {

                        @Override
                        public void onSuccess(ResponseJson<StartCheck> response) {
                            baseView.dismissDialog();
                            baseView.startCheckSucceed();
                        }
                    }, new RequestFailedAction(baseView));
        } else {
            baseView.showError("没有网络");
        }
    }
}
