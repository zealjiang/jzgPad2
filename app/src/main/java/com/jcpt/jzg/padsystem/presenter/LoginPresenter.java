package com.jcpt.jzg.padsystem.presenter;


import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.ILogin;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.User;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录
 * @author zealjiang
 * @time 2016/11/17 15:40
 */
public class LoginPresenter extends BasePresenter<ILogin> {
    public LoginPresenter(ILogin from) {
        super(from);
    }
    public void login(final String username,final String password,final boolean isShowDialog){
        Map<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        if(isShowDialog){
            baseView.showDialog();
        }
        PadSysApp.getApiServer().login(params)
                .compose(RxThreadUtil.<ResponseJson<User>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<User>>(){
                    @Override
                    public void onSuccess(ResponseJson<User> response) {
                        baseView.dismissDialog();
                        User user = response.getMemberValue();
                        baseView.succeed(user);
                    }
                },new RequestFailedAction(baseView));
    }
}
