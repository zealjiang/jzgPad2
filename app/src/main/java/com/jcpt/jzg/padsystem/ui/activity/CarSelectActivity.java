package com.jcpt.jzg.padsystem.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.view.BrandSelectView;
import com.jcpt.jzg.padsystem.view.SeriesSelectView;
import com.jcpt.jzg.padsystem.view.TypeSelectView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zealjiang on 2017/4/19 16:38.
 * Email: zealjiang@126.com
 */

public class CarSelectActivity extends BaseActivity implements BrandSelectView.OnBrandItemClickListener,SeriesSelectView.OnSeriesItemClickListener,TypeSelectView.OnTypeItemClickListener{

    @BindView(R.id.view_brand)
    BrandSelectView brandSelectView;
    @BindView(R.id.view_series)
    SeriesSelectView seriesSelectView;
    @BindView(R.id.view_type)
    TypeSelectView typeSelectView;


    private int brandId;
    private String brandName;
    private int seriesId;
    private String seriesName;
    private int typeId;
    private String typeName;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_car_select);
        ButterKnife.bind(this);

        if(!PadSysApp.networkAvailable){
            //车系
            seriesSelectView.showLoadError();
        }
        brandSelectView.setOnBrandItemClickListener(this);
        brandSelectView.init();
        seriesSelectView.setOnSeriesItemClickListener(this);
        typeSelectView.setOnTypeItemClickListener(this);

    }

    @Override
    protected void setData() {
        setTitle("直接选车");
    }

    @OnClick({R.id.ivLeft})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft://返回
                finish();
                break;
        }
    }

    @Override
    public void onBrandItemClick(int brandId, String brandName, String brandLogo) {
        this.brandId = brandId;
        this.brandName = brandName;
        //检查网络
        if(!PadSysApp.networkAvailable){
            MyToast.showShort("请检查网络");
            seriesSelectView.showLoadError();
            return;
        }
        seriesSelectView.hideLoadError();
        seriesSelectView.requestSeries(brandId,brandName,brandLogo);
    }

    @Override
    public void onSeriesItemClick(int seriesId, String seriesName, String seriesLogo) {
        this.seriesId = seriesId;
        this.seriesName = seriesName;
        //检查网络
        if(!PadSysApp.networkAvailable){
            MyToast.showShort("请检查网络");
            return;
        }
        typeSelectView.requestCarTypeList(seriesId+"");
    }

    @Override
    public void onTypeItemClick(int styleId, String styleName, String styleYear, String styleNowMsrp, String styleFullName) {
        Intent intent = new Intent();
        intent.putExtra("brandId",brandId);
        intent.putExtra("brandName",brandName);
        intent.putExtra("seriesId",seriesId);
        intent.putExtra("seriesName",seriesName);
        intent.putExtra("styleId",styleId);
        intent.putExtra("styleName",styleName);
        intent.putExtra("styleYear",styleYear);
        intent.putExtra("styleNowMsrp",styleNowMsrp);
        intent.putExtra("styleFullName",styleFullName);
        setResult(RESULT_OK,intent);
        finish();
    }
}
