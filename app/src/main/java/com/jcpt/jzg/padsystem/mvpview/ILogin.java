package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/6/22.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.User;


public interface ILogin extends IBaseView {
    void succeed(User user);
}
