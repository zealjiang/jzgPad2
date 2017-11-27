package com.jcpt.jzg.padsystem.presenter;

import android.text.TextUtils;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.mvpview.ICarTypeSelectInterface;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.NetworkExceptionUtils;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.BrandList;
import com.jcpt.jzg.padsystem.vo.SeriesList;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 车系选择
 * Created by zealjiang on 2016/11/1 17:58.
 * Email: zealjiang@126.com
 */

public class CarSeriesSelectPresenter extends BasePresenter<ICarTypeSelectInterface> {

    public CarSeriesSelectPresenter(ICarTypeSelectInterface iCarTypeSelectInterface) {
        super(iCarTypeSelectInterface);
    }

    /**
     * 获取品牌数据
     *
     * @author zealjiang
     * @time 2016/11/1 18:28
     */
    public void getBrandList() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        //加sign
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getBrandList(params).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<BrandList>() {
            @Override
            public void onCompleted() {
                baseView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                baseView.dismissDialog();
                if (e != null && baseView != null) {
                    String error = NetworkExceptionUtils.getErrorByException(e);
                    if (!TextUtils.isEmpty(error)) {
                        baseView.showError(error);
                    }
                }
            }

            @Override
            public void onNext(BrandList data) {
                int status = data.getStatus();
                if (status == 100) {
                    ArrayList<BrandList.MemberValueBean> brandList = (ArrayList<BrandList.MemberValueBean>) data.getMemberValue();
                    if (brandList != null) {
                        //设置品牌列表数据用于ItemClick
                        if (!baseView.readFromCache(data)) {  //如果缓存与网络不一致，更新列表
                            showBrand(brandList);
                        }
                    }
                } else {
                    baseView.showError(data.getMsg());
                }
            }
        });
    }

    public void showBrand(ArrayList<BrandList.MemberValueBean> makeList) {
        baseView.setBrands(makeList);
        ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Integer> indexData = new HashMap<String, Integer>();
        //显示分组数据用于分组adapter
        getItems(makeList, items);
        baseView.showBrand(items);

        //设置index索引数据用于index索引监听
        getIndexData(items, indexData);
        baseView.setIndexData(indexData);
    }

    private void getIndexData(ArrayList<Map<String, Object>> items, Map<String, Integer> indexData) {
        for (int i = 0; i < items.size(); i++) {
            // 当前汉语拼音首字母
            String currentStr = items.get(i).get("Sort").toString();
            // 上一个汉语拼音首字母，如果不存在为“ ”
            String previewStr = (i - 1) >= 0 ? items.get(i - 1).get("Sort")
                    .toString() : " ";
            if (!previewStr.equals(currentStr)) {
                String name = items.get(i).get("Sort").toString();
                indexData.put(name, i);
            }
        }
    }

    private void getItems(ArrayList<BrandList.MemberValueBean> makes, ArrayList<Map<String, Object>> items) {
        if (makes == null || makes.size() == 0)
            return;
        Map<String, Object> map = null;
        for (int i = 0; i < makes.size(); i++) {
            map = new HashMap<String, Object>();
            map.put("name", makes.get(i).getMakeName());
            map.put("fontColor", baseView.getDefaultFontColor());
            map.put("logo", makes.get(i).getMakeLogo());
            //String contactSort = ChineseUtil.getFullSpell(map.get("name").toString()).toUpperCase().substring(0, 1);
            String contactSort = makes.get(i).getGroupName();
            map.put("Sort", contactSort);
            items.add(map);
        }
        Comparator comp = new Mycomparator();
        Collections.sort(items, comp);
    }

    // 按中文拼音排序
    public class Mycomparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Map<String, Object> c1 = (Map<String, Object>) o1;
            Map<String, Object> c2 = (Map<String, Object>) o2;
            Comparator cmp = Collator.getInstance(java.util.Locale.ENGLISH);
            return cmp.compare(c1.get("Sort"), c2.get("Sort"));
        }
    }

    /**
     * 获取车系
     *
     * @author zealjiang
     * @time 2016/11/1 19:51
     */
    public void getSeriesList(int makeId) {
        Map<String, String> params = new HashMap<>();
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params.put("makeId", makeId + "");
        //加sign
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));
        baseView.showDialog();
        PadSysApp.getApiServer().getSeriesList(params).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<SeriesList>() {
            @Override
            public void onCompleted() {
                baseView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                baseView.dismissDialog();
                if (e != null && baseView != null) {
                    String error = NetworkExceptionUtils.getErrorByException(e);
                    if (!TextUtils.isEmpty(error)) {
                        baseView.showError(error);
                    }
                }
            }

            @Override
            public void onNext(SeriesList data) {
                int status = data.getStatus();
                if (status == 100) {
                    ArrayList<SeriesList.MemberValueBean> seriesList = (ArrayList<SeriesList.MemberValueBean>) data.getMemberValue();
                    ArrayList<SeriesList.MemberValueBean.ModelGroupListBean> newModelGroupListBeen = new ArrayList<>();
                    ArrayList<String> seriesGroupNames = new ArrayList<>();
                    for (SeriesList.MemberValueBean serie : seriesList) {
                        SeriesList.MemberValueBean.ModelGroupListBean modelGroupListBean = new SeriesList.MemberValueBean.ModelGroupListBean();
                        String manufacturerName = serie.getManufacturerName();
                        seriesGroupNames.add(manufacturerName);
                        //增加一个分组item项
                        modelGroupListBean.setName(manufacturerName);
                        modelGroupListBean.setManufacturerName(Constants.IS_TITLE);
                        modelGroupListBean.setFontColor(R.color.common_text_gray_dark);
                        //添加车系分组头
                        newModelGroupListBeen.add(modelGroupListBean);
                        //添加具体的车系
                        for (SeriesList.MemberValueBean.ModelGroupListBean item : serie.getModelGroupList()) {
                            item.setFontColor(R.color.common_text_gray_dark);
                            newModelGroupListBeen.add(item);
                        }
                    }

                    baseView.setSeries(newModelGroupListBeen);
                    baseView.setSeriesGroupNames(seriesGroupNames);
                    baseView.showSeries();
                } else {
                    baseView.showError(data.getMsg());
                }
            }
        });
    }
}
