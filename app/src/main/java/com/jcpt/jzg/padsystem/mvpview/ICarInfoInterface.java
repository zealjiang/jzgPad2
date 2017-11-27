package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.CarInfoModel;


/**
 * 通过VIN获取（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
 */
public interface ICarInfoInterface extends IBaseView {

    void succeedCarInfo(CarInfoModel carInfoModel);
}
