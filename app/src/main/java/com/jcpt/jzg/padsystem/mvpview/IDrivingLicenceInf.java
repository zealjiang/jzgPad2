package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.DrivingLicenceModel;

/**
 * 行驶证识别
 * Created by zealjiang on 2016/11/16 11:21.
 * Email: zealjiang@126.com
 */

public interface IDrivingLicenceInf extends IBaseView {
    void succeed(DrivingLicenceModel drivingLicenceModel);
}
