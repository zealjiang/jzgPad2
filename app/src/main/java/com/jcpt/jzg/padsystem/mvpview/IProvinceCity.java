package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/6/22.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.ProvinceCityModel;

import java.util.List;


public interface IProvinceCity extends IBaseView {
    void succeedProvince(List<ProvinceCityModel> listData);
    void succeedCity(List<ProvinceCityModel> listData);
}
