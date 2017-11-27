package com.jcpt.jzg.padsystem.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.view.BrandSelectView;
import com.jcpt.jzg.padsystem.view.CarConfigSelectView;
import com.jcpt.jzg.padsystem.view.SeriesSelectView;
import com.jcpt.jzg.padsystem.vo.LocalCarConfigModel;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车型选择
 * 品牌、车系、参数选择
 * @author zealjiang
 * @time 2016/11/1 15:52
 */
public class  CarTypeSelectActivity extends BaseActivity implements BrandSelectView.OnBrandItemClickListener,SeriesSelectView.OnSeriesItemClickListener{


    @BindView(R.id.view_brand)
    BrandSelectView brandSelectView;
    @BindView(R.id.view_series)
    SeriesSelectView seriesSelectView;
    @BindView(R.id.view_car_config)
    CarConfigSelectView carConfigSelectView;

    @BindView(R.id.crb_clear)
    CustomRippleButton crbClear;
    @BindView(R.id.crb_complete)
    CustomRippleButton crbComplete;

    private int brandId;
    private String brandName;
    private int seriesId;
    private String seriesName;
    private String vin;
    private LocalCarConfigModel localCarConfigModel;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_car_type_select);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        if(!PadSysApp.networkAvailable){
            //车系
            seriesSelectView.showLoadError();
            //参数选择
            carConfigSelectView.showLoadError();
        }
        brandSelectView.setOnBrandItemClickListener(this);
        brandSelectView.init();
        seriesSelectView.setOnSeriesItemClickListener(this);

        localCarConfigModel = (LocalCarConfigModel)getIntent().getSerializableExtra("LocalCarConfigModel");
        if(localCarConfigModel==null){
            finish();
        }
        carConfigSelectView.setLocalCarConfigModel(localCarConfigModel);

        String vin = localCarConfigModel.getVin();
        if(!TextUtils.isEmpty(vin)){
            //设置VIN
            this.vin = vin;
        }

        String productDate = localCarConfigModel.getProductDate();
        if(!TextUtils.isEmpty(productDate)){
            String[] yearMonth = productDate.split("-");
            //设置出厂日期
            carConfigSelectView.setDate(Integer.valueOf(yearMonth[0]),Integer.valueOf(yearMonth[1]),1);
        }

        String nameplate = localCarConfigModel.getNameplate();
        if(!TextUtils.isEmpty(nameplate)){
            //设置车辆铭牌
            carConfigSelectView.setNameplate(nameplate);
        }
    }

    @Override
    protected void setData() {
        setTitle("车系选择");
    }


    @OnClick({R.id.ivLeft,R.id.crb_clear,R.id.crb_complete})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ivLeft://返回
                finish();
                break;
            case R.id.crb_clear://清空重选
                brandSelectView.init();
                //清空铭牌车辆型号
                carConfigSelectView.clearCarNamePlate();
                break;
            case R.id.crb_complete://完成
                //判断必填项是否都填写完成
                boolean isFill = carConfigSelectView.checkInput();
                if(!isFill){
                    return;
                }
                LocalCarConfigModel localCarConfigModel = carConfigSelectView.getSelectedSeriesData();
                localCarConfigModel.setBrandId(brandId);
                localCarConfigModel.setBrandName(brandName);
                localCarConfigModel.setSerieId(seriesId);
                localCarConfigModel.setSerieName(seriesName);
                localCarConfigModel.setVin(vin);

                Intent intent = new Intent();
                intent.putExtra("LocalCarConfigModel",localCarConfigModel);
                setResult(RESULT_OK,intent);
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
            carConfigSelectView.showLoadError();
            return;
        }
        carConfigSelectView.requestConfig(seriesId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }
}
