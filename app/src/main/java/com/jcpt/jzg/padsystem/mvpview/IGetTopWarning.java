package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.vo.TopWarningValueBean;

/**
 * Created by wujj on 2017/4/24.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public interface IGetTopWarning extends IBaseView{
    void requestTopWarningValueSucceed(ResponseJson<TopWarningValueBean> response);
}
