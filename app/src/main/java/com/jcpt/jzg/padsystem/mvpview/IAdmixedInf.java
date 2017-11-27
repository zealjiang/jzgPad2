package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.AdmixedData;

/**
 * 车辆参配
 * Created by zealjiang on 2016/12/6 14:29.
 * Email: zealjiang@126.com
 */

public interface IAdmixedInf extends IBaseView {
    void succeed(AdmixedData admixedData);
}
