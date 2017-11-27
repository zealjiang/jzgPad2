package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.BrandList;
import com.jcpt.jzg.padsystem.vo.SeriesList;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zealjiang on 2016/11/1 18:00.
 * Email: zealjiang@126.com
 */

public interface ICarTypeSelectInterface extends IBaseView {


    /**
     * 显示品牌列表
     *
     * @param items
     */
    void showBrand(ArrayList<Map<String, Object>> items);

    /**
     * 显示车系列表
     */
    void showSeries();
    /**
     * 显示车型列表
     */
    //void showType(CarType carStyle);

    /**
     * 获取字体默认颜色
     *
     * @return
     */
    int getDefaultFontColor();

    /**
     * 设置右侧index列表需要的数据
     *
     * @param indexData
     */
    void setIndexData(Map<String, Integer> indexData);

    /**
     * 设置品牌数据
     *
     * @param makes
     */
    void setBrands(ArrayList<BrandList.MemberValueBean> makes);

    /**
     * 设置具体的车系数据
     *
     * @param modelGroupListBeen
     */
    void setSeries(ArrayList<SeriesList.MemberValueBean.ModelGroupListBean> modelGroupListBeen);

    /**
     * 设置车系分组头名称数据
     *
     * @param seriesGroupNames
     */
    void setSeriesGroupNames(ArrayList<String> seriesGroupNames);


    /**
     *
     */
    boolean readFromCache(BrandList makeList);

}
