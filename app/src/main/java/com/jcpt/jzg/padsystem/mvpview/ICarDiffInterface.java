package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.CarTypeSelectModel;

/**
 * 备选车型--差异项配置
 * Created by zealjiang on 2016/11/1 18:00.
 * Email: zealjiang@126.com
 */

public interface ICarDiffInterface extends IBaseView {

    void succeed(CarTypeSelectModel carTypeSelectModel);
}
