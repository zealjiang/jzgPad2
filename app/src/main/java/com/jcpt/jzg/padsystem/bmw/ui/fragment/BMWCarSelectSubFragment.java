package com.jcpt.jzg.padsystem.bmw.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.AppManager;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.activity.AdmixedContrastActivity;
import com.jcpt.jzg.padsystem.ui.activity.CarPicComparedActivity;
import com.jcpt.jzg.padsystem.ui.activity.CarSelectActivity;
import com.jcpt.jzg.padsystem.ui.activity.PlateTypeActivity;
import com.jcpt.jzg.padsystem.ui.activity.WebviewActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.view.BMWCarDiffSelectView;
import com.jcpt.jzg.padsystem.view.BMWCarTypeListView;
import com.jcpt.jzg.padsystem.view.CarDiffSelectView;
import com.jcpt.jzg.padsystem.view.CarTypeListView;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;
import com.jcpt.jzg.padsystem.vo.CarTypeSelectModel;
import com.jcpt.jzg.padsystem.vo.EventModel;
import com.jcpt.jzg.padsystem.vo.LocalCarConfigModel;
import com.jcpt.jzg.padsystem.vo.NewStyle;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.widget.AutoFitTextView;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车型选择
 * 变速器（1自动，2手动，3电动） 驱动方式（3四驱，4两驱）
 * @author zealjiang
 */
public class BMWCarSelectSubFragment extends BaseFragment implements BMWCarTypeListView
        .OnListDataLoadOverListener,BMWCarDiffSelectView.OnDiffSelectedItemListener{

    @BindView(R.id.crb_modify)
    CustomRippleButton crbModify;
    @BindView(R.id.crbShowConfig)
    CustomRippleButton crbShowConfig;
    @BindView(R.id.crbSelectCar)
    CustomRippleButton crbSelectCar;
    @BindView(R.id.car_type_list_view)
    BMWCarTypeListView carTypeListView;
    @BindView(R.id.car_diff_select)
    BMWCarDiffSelectView carDiffSelectView;
    @BindView(R.id.tvTitle)
    AutoFitTextView tvTitle;
    @BindView(R.id.tvBmwCarName)
    TextView tvBmwCarName;
    @BindView(R.id.btnBmwConfig)
    Button btnBmwConfig;

    private String taskId;
    private final int REQUEST_CODE_CAR_TYPE = 101;//直接选车
    private final int REQUEST_CODE_PLATE_TYPE = 102;//铭牌选车

    private SubmitModel submitModel;
    private BMWDetectMainActivity bmwDetectMainActivity;
    private LocalCarConfigModel localCarConfigModel;//也用于本地数据库保存的车型数据

    private String vin;
    private String nameplate;
    private String productDate;
    private BMWOrderInfBean bmwOrderInfBean;
    private String bmwRecommendUrl;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmw_car_select_sub, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        bmwDetectMainActivity = (BMWDetectMainActivity)this.getActivity();
        carTypeListView.setBMWDetectMainActivity(bmwDetectMainActivity);
        carTypeListView.setOnListDataLoadOverListener(this);
        carDiffSelectView.setOnDiffSelectedItemListener(this);
        carDiffSelectView.setDetectMainActivity(bmwDetectMainActivity);

        bmwOrderInfBean = ((BMWDetectMainActivity) getActivity()).getBMWOrderInfBean();

        getData();


        return view;
    }


    @Override
    protected void setView() {
        tvTitle.setText(localCarConfigModel.getStyleFullName());
        //详情
        TaskDetailModel taskDetailModel = bmwDetectMainActivity.getTaskDetailModel();
        if(taskDetailModel!=null&&taskDetailModel.getBasic()!=null){
            tvTitle.setText(taskDetailModel.getBasic().getCarFullName());
        }
        tvBmwCarName.setText(StringUtils.null2Length0(localCarConfigModel.getCarTypeDes()));
        bmwRecommendUrl = localCarConfigModel.getBMWRecommendUrl();
    }
    @Override
    protected void initData() {}

    private void getData() {
        taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        submitModel = ((BMWDetectMainActivity)getActivity()).getSubmitModel();
        if(taskId==null||submitModel==null){
            return;
        }
        localCarConfigModel = new LocalCarConfigModel();

        //宝马推荐车型
        if(bmwOrderInfBean!=null){
            if(!StringUtils.isEmpty(bmwOrderInfBean.getCarTypeDes())){
                localCarConfigModel.setCarTypeDes(bmwOrderInfBean.getCarTypeDes());//宝马推荐车型
                localCarConfigModel.setBMWRecommendUrl(bmwOrderInfBean.getBMWRecommendUrl());//BMW推荐配置地址
                bmwRecommendUrl = bmwOrderInfBean.getBMWRecommendUrl();
            }
        }


        //onCreat
        //从本地数据库中获取此用户下的taskId
        List<DBBase> list = DBManager.getInstance().query(taskId,Constants.DATA_TYPE_CAR_TYPE,PadSysApp.getUser().getUserId());
        if(list!=null&&list.size()>0){
            String json = list.get(0).getJson();
            if(!TextUtils.isEmpty(json)){
                localCarConfigModel = new Gson().fromJson(json,LocalCarConfigModel.class);
                if(localCarConfigModel.getStyleId()>0&&(localCarConfigModel.getSerieId()==0||localCarConfigModel.getBrandId()==0)){
                    //如果有ID出错，则重置
                    localCarConfigModel.setBrandId(0);
                    localCarConfigModel.setSerieId(0);
                    localCarConfigModel.setStyleId(0);
                }

                //重新为submitModel赋上数据库数据的值
                submitModel.setMakeID(localCarConfigModel.getBrandId());
                submitModel.setModelID(localCarConfigModel.getSerieId());
                submitModel.setStyleID(localCarConfigModel.getStyleId());
                submitModel.setManufacturerPrice(localCarConfigModel.getNowMsrp());
                submitModel.setActivityId(localCarConfigModel.getActivityId());

            }
        }
        //判断这个任务是否存在检测详情数据
        TaskDetailModel taskDetailModel = ((BMWDetectMainActivity)this.getActivity()).getTaskDetailModel();
        if(taskDetailModel!=null){
            TaskDetailModel.BasicBean basicBean =  taskDetailModel.getBasic();
            if(basicBean!=null) {
                if(basicBean.getStyleID()>0&&(basicBean.getModelID()==0||basicBean.getMakeID()==0)){
                    //如果有ID出错，则重置
                    basicBean.setMakeID(0);
                    basicBean.setModelID(0);
                    basicBean.setStyleID(0);
                }
                //重新为submitModel赋上检测详情数据的值
                submitModel.setMakeID(basicBean.getMakeID());
                submitModel.setModelID(basicBean.getModelID());
                submitModel.setStyleID(basicBean.getStyleID());
                submitModel.setManufacturerPrice(basicBean.getManufacturerPrice()+"");
                submitModel.setActivityId(basicBean.getActivityId());

                localCarConfigModel.setBrandId(basicBean.getMakeID());
                localCarConfigModel.setSerieId(basicBean.getModelID());
                localCarConfigModel.setStyleId(basicBean.getStyleID());
                localCarConfigModel.setFullName(basicBean.getCarFullName());
                localCarConfigModel.setStyleFullName(basicBean.getCarFullName());
                localCarConfigModel.setNowMsrp(basicBean.getManufacturerPrice()+"");
                localCarConfigModel.setActivityId(basicBean.getActivityId());

            }
        }

        if(submitModel.getMakeID()>0&&submitModel.getModelID()>0&&submitModel.getStyleID()>0){
            //设置车况检测和车辆照片tabView可点击
            ((BMWDetectMainActivity)BMWCarSelectSubFragment.this.getActivity()).remove(2);
            ((BMWDetectMainActivity)BMWCarSelectSubFragment.this.getActivity()).remove(3);
        }else{
            //设置车况检测和车辆照片tabView不可点击
            ((BMWDetectMainActivity)BMWCarSelectSubFragment.this.getActivity()).add(2);
            ((BMWDetectMainActivity)BMWCarSelectSubFragment.this.getActivity()).add(3);
        }

        //判断车型选择
        if(submitModel.getVin()!=null&&submitModel.getVin().length()>0&&submitModel.getProductionTime()!=null&&submitModel.getProductionTime().length()>0){
            if(submitModel.getMakeID()>0&&submitModel.getModelID()>0){
                ((BMWDetectMainActivity)BMWCarSelectSubFragment.this.getActivity()).remove(1);
            }
        }
    }



    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        submitModel = ((BMWDetectMainActivity)getActivity()).getSubmitModel();
        vin = submitModel.getVin();
        productDate = submitModel.getProductionTime();
        nameplate = submitModel.getNameplate();
        if(isVisibleToUser&&submitModel.getStyleID()!=0&&submitModel.getMakeID()==0){
            MyToast.showShort("品牌、车系、车型 ID有错 CarSelectFragment");
            return;
        }
        if(isVisibleToUser&&submitModel!=null&&submitModel.getStyleID()==0){
            //vin是从手续信息界面带过来的
            localCarConfigModel.setVin(submitModel.getVin());
            //车辆铭牌 是从车系选择界面带过来的
            localCarConfigModel.setNameplate(submitModel.getNameplate());
            if(submitModel.getStyleID()==0){
                request();
            }
        }else{
            if(localCarConfigModel!=null){
                tvTitle.setText(localCarConfigModel.getStyleFullName());
            }
        }

        if(isVisibleToUser&&submitModel!=null){
            //出厂日期是从手续信息界面带过来的
            localCarConfigModel.setProductDate(submitModel.getProductionTime());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void request(){
        requestCarType(vin,productDate+"",nameplate);

    }


    /**
     * 请求车型信息
     * @author zealjiang
     * @time 2017/4/20 13:52
     */
    private void requestCarType(String vin,String productYear,String nameplate){
        //处理Null
        vin = StringUtils.null2Length0(vin);
        productYear = StringUtils.null2Length0(productYear);
        nameplate = StringUtils.null2Length0(nameplate);
        carTypeListView.requestCarDiff(vin,productYear,nameplate);
    }


    @OnClick({R.id.crb_modify,R.id.crbShowConfig,R.id.crbSelectCar,R.id.btnBmwConfig})
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.crb_modify:
                if(!PadSysApp.networkAvailable){
                    MyToast.showShort(getResources().getString(R.string.check_net));
                    return;
                }

                vin = submitModel.getVin();
                productDate = submitModel.getProductionTime();
                nameplate = submitModel.getNameplate();

                intent = new Intent(getActivity(), PlateTypeActivity.class);
                intent.putExtra("vin",vin);
                intent.putExtra("productDate",productDate);
                intent.putExtra("nameplate",nameplate);
                intent.putExtra("brandType",submitModel.getBrandType());//品牌和型号
                startActivityForResult(intent,REQUEST_CODE_PLATE_TYPE);


                break;
            case R.id.crbShowConfig://查看配置
                if(!PadSysApp.networkAvailable){
                    MyToast.showShort(getResources().getString(R.string.check_net));
                    return;
                }
                //获取备选款型styleIds
                ArrayList<String> arrayStyleId = carTypeListView.getShowStyleIdArray();
                if(arrayStyleId!=null){
                    intent = new Intent(getActivity(), AdmixedContrastActivity.class);
                    intent.putStringArrayListExtra("styleIds",arrayStyleId);
                    startActivity(intent);
                }else{
                    MyToast.showShort("备选款型不可为空");
                }
                break;
            case R.id.crbSelectCar:
                if(!PadSysApp.networkAvailable){
                    MyToast.showShort(getResources().getString(R.string.check_net));
                    return;
                }
                intent = new Intent(getActivity(), CarSelectActivity.class);
                startActivityForResult(intent,REQUEST_CODE_CAR_TYPE);
                break;
            case R.id.btnBmwConfig:
                //宝马推荐车型
                if(!StringUtils.isEmpty(localCarConfigModel.getBMWRecommendUrl())){
                    intent = new Intent(getActivity(), WebviewActivity.class);
                    intent.putExtra("title","BMW推荐配置");
                    intent.putExtra("url",localCarConfigModel.getBMWRecommendUrl());
                    startActivity(intent);
                } else{
                    MyToast.showShort("BMW推荐配置为空");
                }
                break;
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_CAR_TYPE://直接选车返回
                if(data==null) {
                    return;
                }

                int brandId = data.getIntExtra("brandId",0);
                String brandName = data.getStringExtra("brandName");
                int seriesId = data.getIntExtra("seriesId",0);
                String seriesName = data.getStringExtra("seriesName");
                int styleId = data.getIntExtra("styleId",0);
                String styleName = data.getStringExtra("styleName");
                String styleYear = data.getStringExtra("styleYear");
                String styleNowMsrp = data.getStringExtra("styleNowMsrp");
                String styleFullName = data.getStringExtra("styleFullName");

                //保存所选择的车型信息
                this.localCarConfigModel.setBrandId(brandId);
                this.localCarConfigModel.setBrandName(brandName);
                this.localCarConfigModel.setSerieId(seriesId);
                this.localCarConfigModel.setSerieName(seriesName);
                this.localCarConfigModel.setStyleId(styleId);
                this.localCarConfigModel.setStyleName(styleName);
                this.localCarConfigModel.setYear(styleYear);
                this.localCarConfigModel.setNowMsrp(styleNowMsrp);
                this.localCarConfigModel.setStyleFullName(styleFullName);

                this.submitModel.setMakeID(brandId);
                this.submitModel.setModelID(seriesId);
                this.submitModel.setStyleID(styleId);
                this.submitModel.setManufacturerPrice(styleNowMsrp);

                //更新标题
                tvTitle.setText(styleFullName);

                this.submitModel.setNameplate("");
                carTypeListView.hideHint();

                //保存品牌、车系
                checkAndSaveData(false);

                //清空备选款型和差异配置数据
                carTypeListView.clearData();
                carDiffSelectView.clearData();

                break;
            case REQUEST_CODE_PLATE_TYPE://铭牌型号选车返回
                if(data==null) {
                    return;
                }
                vin = data.getStringExtra("vin");
                nameplate = data.getStringExtra("plateType");
                productDate = data.getStringExtra("productDate");
                boolean isInputPlateType = data.getBooleanExtra("isInputPlateType",false);

                //清空车型
                this.submitModel.setMakeID(0);
                this.submitModel.setModelID(0);
                this.submitModel.setStyleID(0);

                //保存所选择的车型信息
                this.localCarConfigModel.setVin(vin);
                if(isInputPlateType) {
                    this.submitModel.setNameplate(nameplate);
                }else{
                    this.submitModel.setNameplate("");
                }
                this.localCarConfigModel.setProductDate(productDate);

                checkAndSaveData(false);
                request();
                break;
        }
    }

    @Override
    public void onListDataLoadOver(List<CarTypeSelectModel.MemberValueBean.ConfigListBean> listData,String brandSeriesName) {
        carDiffSelectView.initTagFlowData(listData);

        //更新标题
        tvTitle.setText(brandSeriesName);
    }

    @Override
    public void onFileterOutCar(List<CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean> filterCars) {
        carDiffSelectView.resetTagFlow(filterCars);
    }



    /**
     * 从车型确认处传过来的车型信息
     * @param newStyle
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NewStyle newStyle) {
        if(newStyle==null){
            return;
        }else{
            if(newStyle.isCancel()){
                return;
            }
            AppManager.getAppManager().finishActivity(CarPicComparedActivity.class);
            AppManager.getAppManager().finishActivity(AdmixedContrastActivity.class);

            if(newStyle.getMakeID()==0){
                //是从车辆对比传过来的
                return;
            }

            localCarConfigModel.setBrandId(newStyle.getMakeID());
            localCarConfigModel.setBrandName(newStyle.getMakeName());
            localCarConfigModel.setSerieId(newStyle.getModelID());
            localCarConfigModel.setSerieName(newStyle.getModelName());
            localCarConfigModel.setStyleId(newStyle.getStyleId());
            localCarConfigModel.setStyleName(newStyle.getName());
            localCarConfigModel.setYear(newStyle.getYear());
            localCarConfigModel.setNowMsrp(newStyle.getNowMsrp()+"");
            localCarConfigModel.setStyleFullName(newStyle.getName());
            localCarConfigModel.setActivityId(newStyle.getActivityId());

            this.submitModel.setMakeID(newStyle.getMakeID());
            this.submitModel.setModelID(newStyle.getModelID());
            this.submitModel.setStyleID(newStyle.getStyleId());
            this.submitModel.setManufacturerPrice(newStyle.getNowMsrp()+"");
            this.submitModel.setActivityId(newStyle.getActivityId());


            if(TextUtils.isEmpty(newStyle.getYear())){
                localCarConfigModel.setFullName(newStyle.getName()+" "+Double.valueOf(newStyle.getNowMsrp()) +"万元");
            }else{
                localCarConfigModel.setFullName(newStyle.getName()+" "+newStyle.getYear()+"款"+Double.valueOf(newStyle.getNowMsrp()) +"万元");
            }

            tvTitle.setText(localCarConfigModel.getStyleFullName());

            checkAndSaveData(false);
        }
    }

    /**
     * 选择了某个车型后 来自车辆对比后 CarTypeListView
     * @param eventModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventModel eventModel) {
        if(eventModel.getName().equals("selectedPos")){
            int selectedPosition = Integer.valueOf(eventModel.getValue());

            localCarConfigModel = carTypeListView.getSelectedStyle();

            this.submitModel.setMakeID(localCarConfigModel.getBrandId());
            this.submitModel.setModelID(localCarConfigModel.getSerieId());
            this.submitModel.setStyleID(localCarConfigModel.getStyleId());
            this.submitModel.setManufacturerPrice(localCarConfigModel.getNowMsrp());
            this.submitModel.setActivityId(localCarConfigModel.getActivityId());

            localCarConfigModel.setFullName(localCarConfigModel.getStyleFullName());

            tvTitle.setText(localCarConfigModel.getStyleFullName());

            checkAndSaveData(false);
        }

    }

    /**
     * 品牌、车系Activity点击完成
     * 清空车型 并保存品牌、车系
     */
    @Deprecated
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LocalCarConfigModel localCarConfigModel) {
        //如果重新选择了品牌车系，保存品牌、车系ID并重置车型ID
        if(submitModel!=null){
            submitModel.setMakeID(localCarConfigModel.getBrandId());
            submitModel.setModelID(localCarConfigModel.getSerieId());
            submitModel.setStyleID(0);
        }


        if(this.localCarConfigModel!=null){
            this.localCarConfigModel.setBrandId(localCarConfigModel.getBrandId());
            this.localCarConfigModel.setBrandName(localCarConfigModel.getBrandName());
            this.localCarConfigModel.setSerieId(localCarConfigModel.getSerieId());
            this.localCarConfigModel.setSerieName(localCarConfigModel.getSerieName());
            this.localCarConfigModel.setGear(localCarConfigModel.getGear());
            this.localCarConfigModel.setDrivingMode(localCarConfigModel.getDrivingMode());
            this.localCarConfigModel.setDisplacement(localCarConfigModel.getDisplacement());
            this.localCarConfigModel.setProductDate(localCarConfigModel.getProductDate());
            this.localCarConfigModel.setVin(localCarConfigModel.getVin());
            this.localCarConfigModel.setStyleId(0);

            String json = new Gson().toJson(localCarConfigModel);
            //保存到数据库
            DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_CAR_TYPE, taskId, PadSysApp.getUser().getUserId(), json);
        }
    }

    /**
     * 保存数据
     * @author zealjiang
     * @time 2016/12/2 17:35
     * @param isCheck 为true表示保存前检查必填项，false表示不检查
     * @return 返回true表示保存成功，false表示保存失败
     */
    public boolean checkAndSaveData(boolean isCheck){
        /**
         * 如果车型
         */
        if(isCheck){
            if(submitModel.getMakeID()>0&&submitModel.getModelID()>0) {
                if (submitModel.getStyleID() == 0) {
                    MyToast.showLong("您还未选择车型，请完成");//"请在车型选择备选款型中，点选与实车相符的车型");
                    return false;
                }
            }else{
                MyToast.showShort("您还未选择车型，请完成");
                return false;
            }
        }
        if(bmwDetectMainActivity ==null) {
            return false;
        }
        submitModel = bmwDetectMainActivity.getSubmitModel();
        if(submitModel==null||localCarConfigModel==null){
            return false;
        }


        //保存数据
        localCarConfigModel.setBrandId(submitModel.getMakeID());
        localCarConfigModel.setSerieId(submitModel.getModelID());
        localCarConfigModel.setStyleId(submitModel.getStyleID());
        localCarConfigModel.setNowMsrp(submitModel.getManufacturerPrice());
        localCarConfigModel.setActivityId(submitModel.getActivityId());

        localCarConfigModel.setFullName(tvTitle.getText().toString());
        localCarConfigModel.setBMWRecommendUrl(bmwRecommendUrl);
        localCarConfigModel.setCarTypeDes(tvBmwCarName.getText().toString());
        String json = new Gson().toJson(localCarConfigModel);
        //保存到数据库
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_CAR_TYPE, taskId, PadSysApp.getUser().getUserId(), json);
        return true;
    }

    /**
     *
     * @author zealjiang
     * @time 2017/5/11 16:55
     * @param isShowHint 没填写完是否显示提示信息，true显示，false不显示
     * @return 要填的项都填完返回true,反之返回false
     */
    public boolean check(boolean isShowHint){

        if(submitModel.getMakeID()>0&&submitModel.getModelID()>0) {
            if (submitModel.getStyleID() == 0) {
                if(isShowHint) {
                    MyToast.showLong("您还未选择车型，请完成");//"请在车型选择备选款型中，点选与实车相符的车型");
                }
                return false;
            }
        }else{
            if(isShowHint) {
                MyToast.showShort("您还未选择车型，请完成");
            }
            return false;
        }

        return true;
    }


    @Override
    public void onDiffSelectedItem(ArrayList<String> selectedItemPos) {
        carTypeListView.getCarTypeListByDiff(selectedItemPos);
    }

    @Override
    public void onPause() {
        super.onPause();
        //车型选择
        checkAndSaveData(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
