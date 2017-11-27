package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IDetectionMain;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 18:34
 * @desc:
 */
public class GetConfigurePresenter extends BasePresenter<IDetectionMain> {
    public GetConfigurePresenter(IDetectionMain from) {
        super(from);
    }

    /**
     * 检测数据详情接口
     * @author zealjiang
     * @time 2016/11/22 20:24
     */
    public void getDetailData(String taskId){
        Map<String,String> params = new HashMap<>();
        params.put("taskId",taskId);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getDetailData(params)
                .compose(RxThreadUtil.<ResponseJson<TaskDetailModel>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<TaskDetailModel>>(){

                    @Override
                    public void onSuccess(ResponseJson<TaskDetailModel> response) {
                        baseView.dismissDialog();
                        TaskDetailModel data = response.getMemberValue();
                        baseView.requestDataSucceed(data);
                    }
                },new RequestFailedAction(baseView));
    }
}
