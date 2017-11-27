package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.vo.VinCheckedModel;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;

/**
 * Created by wujj on 2017/9/14.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public interface IVInChecked extends IBaseView {
    void requestVinCheckedSucceed(ResponseJson<VinCheckedModel> response,MyUniversalDialog myUniversalDialog,String vin);
    void requestVinCheckedFailed(String message,MyUniversalDialog myUniversalDialog);
}
