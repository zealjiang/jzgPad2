package com.jcpt.jzg.padsystem.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.ProvinceCityAdapter;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.mvpview.IProvinceCity;
import com.jcpt.jzg.padsystem.presenter.ProvinceCityPresenter;
import com.jcpt.jzg.padsystem.vo.CityListBean;
import com.jcpt.jzg.padsystem.vo.LocalProvinceCityModel;
import com.jcpt.jzg.padsystem.vo.ProvinceCityModel;
import com.zhy.base.adapter.recyclerview.DividerItemDecoration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 省市选择
 * Created by zealjiang on 2016/11/17 14:35.
 * Email: zealjiang@126.com
 */

public class ProvinceCitySelectActivity extends BaseActivity implements IProvinceCity{

    @BindView(R.id.rv_province)
    RecyclerView rvProvince;
    @BindView(R.id.rv_city)
    RecyclerView rvCity;

    private ProvinceCitySelectActivity self;
    private LinearLayoutManager linearLayoutManagerP;
    private LinearLayoutManager linearLayoutManagerC;
    private ProvinceCityAdapter adapterP;
    private ProvinceCityAdapter adapterC;
    private List<ProvinceCityModel> listProvinceData;
    private List<ProvinceCityModel> listCityData;
    private ProvinceCityPresenter provinceCityPresenter;
    private int provinceId;
    private String provinceName;
    private int cityId;
    private String cityName;
    private ArrayList<CityListBean.CityList> cityBeanArrayList;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_province_city_select);
        ButterKnife.bind(this);
        self = this;

        listProvinceData = new ArrayList<>();
        listCityData = new ArrayList<>();
        provinceCityPresenter = new ProvinceCityPresenter(this);
    }

    @Override
    protected void setData() {
        setTitle("省市选择");

        //省
        linearLayoutManagerP = new LinearLayoutManager(this);
        rvProvince.setLayoutManager(linearLayoutManagerP);
        //添加分割线
        rvProvince.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapterP = new ProvinceCityAdapter(this,listProvinceData);
        rvProvince.setAdapter(adapterP);
        adapterP.setOnItemClickListener(new ProvinceCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                adapterP.setSelectedPosition(pos);
                adapterP.notifyDataSetChanged();

                provinceId = listProvinceData.get(pos).getId();
                provinceName = listProvinceData.get(pos).getName();
                //网络获取省下的所有市
                //provinceCityPresenter.getCity(provinceId+"");
                //本地获取省下的所有市
                getCityData(provinceId);
                initCityList();
            }
        });

        //市
        linearLayoutManagerC = new LinearLayoutManager(this);
        rvCity.setLayoutManager(linearLayoutManagerC);
        //添加分割线
        rvCity.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapterC = new ProvinceCityAdapter(this,listCityData);
        rvCity.setAdapter(adapterC);
        adapterC.setOnItemClickListener(new ProvinceCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                adapterC.setSelectedPosition(pos);
                adapterC.notifyDataSetChanged();

                cityId = listCityData.get(pos).getId();
                cityName = listCityData.get(pos).getName();

                //返回数据
                LocalProvinceCityModel localProvinceCityModel = new LocalProvinceCityModel();
                localProvinceCityModel.setProvinceId(provinceId);
                localProvinceCityModel.setProvinceName(provinceName);
                localProvinceCityModel.setCityId(cityId);
                localProvinceCityModel.setCityName(cityName);

                Intent intent = new Intent();
                intent.putExtra("LocalProvinceCityModel",localProvinceCityModel);
                setResult(Activity.RESULT_OK,intent);
                self.finish();

            }
        });

        //网络请求省
        //provinceCityPresenter.getProvince();

        //本地获取省
        getProvinceData();
        initProvinceList();
    }

    @OnClick({R.id.ivLeft})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ivLeft://返回
                finish();
                break;
        }
    }

    @Override
    public void succeedProvince(List<ProvinceCityModel> listData) {
        listProvinceData.clear();
        listProvinceData.addAll(listData);
        initProvinceList();

    }

    public void initProvinceList(){
        adapterP.notifyDataSetChanged();

        adapterP.setSelectedPosition(0);
        provinceId = listProvinceData.get(0).getId();
        provinceName = listProvinceData.get(0).getName();
        //provinceCityPresenter.getCity(provinceId+"");
        //本地获取省下的所有市
        getCityData(provinceId);
        initCityList();
    }

    @Override
    public void succeedCity(List<ProvinceCityModel> listData) {
        initCityList();
        listCityData.clear();
        listCityData.addAll(listData);
    }

    public void initCityList(){
        adapterC.notifyDataSetChanged();
        adapterC.setSelectedPosition(0);
    }

    /**
     * 从文件中读取省市数据
     * @author zealjiang
     * @time 2017/2/13 18:35
     */
    public String readDataFromFile(){
        StringBuffer sb = new StringBuffer();
        AssetManager mAssetManager = this.getAssets();
        try{
            InputStream is = mAssetManager.open("city.json");
            byte[] data = new byte[is.available()];
            int len = -1;
            while((len = is.read(data)) != -1){
                sb.append(new String(data,0, len));
            }
            is.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * String数据到对象
     * @author zealjiang
     * @time 2017/2/14 9:51
     */
    public void dataToObject(String data){
        CityListBean pCityListBean = new Gson().fromJson(data, CityListBean.class);
        cityBeanArrayList = pCityListBean.getCityListBean();
    }

    /**
     * 获取省
     * @author zealjiang
     * @time 2017/2/14 9:50
     */
    public void getProvinceData(){
        String data = readDataFromFile();
        dataToObject(data);
        for (int i = 0; i < cityBeanArrayList.size(); i++) {
            CityListBean.CityList.City city = cityBeanArrayList.get(i).getCity();
            ProvinceCityModel pcm = new ProvinceCityModel();
            pcm.setId(city.getCityId());
            pcm.setName(city.getCityName());
            listProvinceData.add(pcm);
        }
    }

    /**
     * 获取市
     * @author zealjiang
     * @time 2017/2/14 9:50
     */
    public void getCityData(int provinceId){
        listCityData.clear();
        for (int i = 0; i < cityBeanArrayList.size(); i++) {
            CityListBean.CityList.City city = cityBeanArrayList.get(i).getCity();
            if(city.getCityId() == provinceId){
                ArrayList<CityListBean.CityList.City> cityList = cityBeanArrayList.get(i).getCityList();
                for (int j = 0; j < cityList.size(); j++) {
                    city = cityList.get(j);
                    ProvinceCityModel pcm = new ProvinceCityModel();
                    pcm.setId(city.getCityId());
                    pcm.setName(city.getCityName());
                    listCityData.add(pcm);
                }

            }
        }
    }
}
