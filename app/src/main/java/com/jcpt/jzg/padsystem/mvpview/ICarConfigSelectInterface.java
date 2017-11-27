package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.CarConfigModel;

/**
 * Created by zealjiang on 2016/11/1 18:00.
 * Email: zealjiang@126.com
 */

public interface ICarConfigSelectInterface extends IBaseView {

    void succeed(CarConfigModel carConfigModel);
}
