package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.CarTypeModel;


/**
 * 车型
 * @author zealjiang
 * @time 2017/4/19 18:10
 */
public interface ICarTypeInterface extends IBaseView {

    void succeed(CarTypeModel carTypeModel);
}
