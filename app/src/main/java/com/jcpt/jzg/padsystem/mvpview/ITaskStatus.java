package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.http.ResponseJson;

/**
 * Created by wujj on 2017/10/17.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public interface ITaskStatus extends IBaseView {
    void requestTaskStatusSucceed(ResponseJson<Integer> response);
    void requestTaskStatusFailed(String Msg);
}
