package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.ICorrector;
import com.jcpt.jzg.padsystem.utils.MD5Utils;

import java.util.HashMap;
import java.util.Map;


/**
 * 纠错
 * @time 2016/12/8 10:57
 * Email: zealjiang@126.com
 */
public class CorrectorPresenter extends BasePresenter<ICorrector> {
    public CorrectorPresenter(ICorrector iCorrector) {
        super(iCorrector);
    }

    /**
     * 纠错
     * @author zealjiang
     * @time 2016/12/8 11:18
     */
    public void reqCorrector(final String content,final String styleId){
        Map<String,String> params = new HashMap<>();
        params.put("Content",content);
        params.put("StyleId",styleId);
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();

        PadSysApp.getApiServer().reqCorrector(params)
                .compose(RxThreadUtil.<ResponseJson<Object>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<Object>>(){
                    @Override
                    public void onSuccess(ResponseJson<Object> response) {
                        baseView.dismissDialog();
                        baseView.succeed("");
                    }
                },new RequestFailedAction(baseView));
    }
}
