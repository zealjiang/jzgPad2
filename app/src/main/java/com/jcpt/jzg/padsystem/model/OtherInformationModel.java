package com.jcpt.jzg.padsystem.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.ui.fragment.OtherInformationFragment;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.OtherCheckInfo;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel.BasicBean;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by libo on 2016/12/5.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 其他信息- - 核对信息界面 model
 */
public class OtherInformationModel {

    /**
     * item 横向填充满 TagFlowLayout 布局时的宽度
     */
    private int tagFlowLayoutItemMatchSize;
    /**
     * item 横向占据一半 TagFlowLayout 布局时的宽度
     */
    private int tagFlowLayoutItemHalfSize;
    /**
     * item 横向占据三分之一 TagFlowLayout 布局时的宽度
     */
    private int tagFlowLayoutItemOneThird;

    //公里数与实车不符
    HashSet<Integer> selecteshowmileage;
    //公里数与实车不符
    HashSet<Integer> selecteshowGaugeLamp;
    //汽车颜色单选
    HashSet<Integer> selecteColor;
    //汽车铭牌
    HashSet<Integer> selecteNameplate;
    //汽车VIN
    HashSet<Integer> selecteVIN;
    //轮胎规格
    HashSet<Integer> selecteTyre;
    //行驶证车辆照片
    HashSet<Integer> selecteLicense;

    private OtherCheckInfo otherCheckInfo;
    private SubmitModel submitModel;
    private String taskId;
    private Context context;
    private OtherInformationFragment otherInformationFragment;

    //GaugeLamp int 仪表故障灯常亮 0否 1是  郑有权 3.28
    private static final int GaugeLampNo = 0;
    private static final int GaugeLampYes = 1;

    private final int PHOTO_BIG_PHOTO = 2;
    private SimpleDraweeView sdvCarTyre;
    private LinearLayout llCarTyre;
    private boolean carTyreFlag;
    private FrescoImageLoader frescoImageLoader;

    public OtherInformationModel(OtherInformationFragment otherInformationFragment, SubmitModel submitModel, OtherCheckInfo otherCheckInfo, String taskId, Context context) {
        this.otherInformationFragment = otherInformationFragment;
        this.context = context;
        this.taskId = taskId;
        this.otherCheckInfo = otherCheckInfo;
        this.submitModel = submitModel;
        frescoImageLoader = FrescoImageLoader.getSingleton();
    }

    public HashSet<Integer> getSelecteColor() {
        return selecteColor;
    }

    public void initData(TagFlowLayout tflShowMileage, TagFlowLayout tflShowMileageLight, TagFlowLayout tflCarColor, TagFlowLayout tflCarNameplate,
                         TagFlowLayout tflCarVin, TagFlowLayout tflCarTyre, TagFlowLayout tflCarLicense) {

        //公里数与实车不符
        selecteshowmileage = (HashSet<Integer>) tflShowMileage.getSelectedList();
        //仪表故障灯常亮
        selecteshowGaugeLamp = (HashSet<Integer>) tflShowMileageLight.getSelectedList();
        //汽车颜色单选
        selecteColor = (HashSet<Integer>) tflCarColor.getSelectedList();
        //汽车铭牌
        selecteNameplate = (HashSet<Integer>) tflCarNameplate.getSelectedList();
        //汽车VIN
        selecteVIN = (HashSet<Integer>) tflCarVin.getSelectedList();
        //轮胎规格
        selecteTyre = (HashSet<Integer>) tflCarTyre.getSelectedList();
        //行驶证车辆照片
        selecteLicense = (HashSet<Integer>) tflCarLicense.getSelectedList();

        //是否存在详情数据
        TaskDetailModel taskDetailModel = ((DetectMainActivity) context).getTaskDetailModel();
        if (taskDetailModel != null) {
            BasicBean Basic = taskDetailModel.getBasic();
            submitModel.setMileage(Basic.getMileage());
            submitModel.setMileageSame(Basic.getMileageSame());
            submitModel.setGaugeLamp(Basic.getGaugeLamp());
            submitModel.setCarColorActual(Basic.getCarColorActual());
            submitModel.setNameplateProperty(Basic.getNameplateProperty());
            submitModel.setVinProperty(Basic.getVinProperty());
            submitModel.setTyreProperty(Basic.getTyreProperty());
            submitModel.setDrivingLicenseCheckEx(Basic.getDrivingLicenseCheckEx());
            submitModel.setOtherColor(Basic.getOtherColor());
        }else if(otherCheckInfo!=null&&submitModel != null){
            submitModel.setMileage(otherCheckInfo.getMileage());
            submitModel.setMileageSame(otherCheckInfo.getMileageSame());
            submitModel.setGaugeLamp(otherCheckInfo.getGaugeLamp());
            submitModel.setCarColorActual(otherCheckInfo.getCarColorActual());
            submitModel.setNameplateProperty(otherCheckInfo.getNameplateProperty());
            submitModel.setVinProperty(otherCheckInfo.getVinProperty());
            submitModel.setTyreProperty(otherCheckInfo.getTyreProperty());
            submitModel.setDrivingLicenseCheckEx(otherCheckInfo.getDrivingLicenseCheckEx());
            submitModel.setOtherColor(otherCheckInfo.getOtherColor());
        }

    }

    /**
     * Created by 李波 on 2016/12/4.
     * 保存数据 （下一步，保存按钮，切换，DelectMainActivity back）
     */
    public void saveDataToLocal(String mileage,String otherColor) {
        String carShowmileage = "";
        String carColor = "";
        String carNameplate = "";
        String carVin = "";
        String carTyre = "";
        String carLicense = "";

        //公里数
        if (!TextUtils.isEmpty(mileage)) {
            int Mileage = Integer.parseInt(mileage);
            saveMileage(Mileage);
        } else {
            saveMileage(-1);
        }

        //公里数与实车不符
        Iterator its = selecteshowmileage.iterator();
        if (its.hasNext()) {
            while (its.hasNext()) {
                Integer i = (Integer) its.next();
                saveShowmileage(i);
                carShowmileage = i + "";
            }
        } else {
            saveShowmileage(1);
        }
        //仪表故障灯常亮
        Iterator itsGaugeLamp = selecteshowGaugeLamp.iterator();
        if (itsGaugeLamp.hasNext()) {
            saveShowGaugeLamp(GaugeLampYes);
        } else {
            saveShowGaugeLamp(GaugeLampNo);
        }

        //汽车颜色-----实车颜色（ 1 白、 2 灰、 3 红、 4 蓝、 5 绿、 6 紫、 7 粉、 8 黄、 9 黑、 10 棕、 11 橙、 12 银、 13 金、 14 青、 15 多彩、16、双色 99 其他）
        Iterator itc = selecteColor.iterator();
        if (itc.hasNext()) {
            while (itc.hasNext()) {
                Integer i = (Integer) itc.next();
                if (i == 15) {
                    saveCarColorActual(99, otherColor);
                } else if (i == 14) {
                    saveCarColorActual(16, otherColor);
                } else {
                    i++;
                    saveCarColorActual(i, otherColor);
                }
                carColor += i + "";
            }
        } else {
            saveCarColorActual(-1, otherColor);
        }


        //车身铭牌-----车身铭牌（ 1 破损、 2 实车铭牌未见、 3 铭牌出厂日期与登记证 yyyy 年 MM 月 dd 日不一致）
        Iterator itn = selecteNameplate.iterator();
        while (itn.hasNext()) {
            Integer i = (Integer) itn.next();
            carNameplate += i + 1 + ",";
        }
        if (!TextUtils.isEmpty(carNameplate)) {
            //把最后一位逗号去掉
            String substring = carNameplate.substring(0, carNameplate.length() - 1);
            savaNameplateProperty(substring);
        } else {
            savaNameplateProperty(null);
        }

        //汽车VIN--1 实车车架号未见、 2 可以看清无法拓印、 3 锈蚀、 4 与登记证不一致、 5 打磨修复痕迹、 6 打刻较浅
        Iterator itv = selecteVIN.iterator();
        while (itv.hasNext()) {
            Integer i = (Integer) itv.next();
            carVin += i + 1 + ",";
        }
        if (!TextUtils.isEmpty(carVin)) {

            //把最后一位逗号去掉
            String substring1 = carVin.substring(0, carVin.length() - 1);
            savaVinProperty(substring1);
        } else {
            savaVinProperty(null);
        }

        //轮胎规则--1 实车与登记证不一致， 2 同轴花纹不一致
        Iterator itt = selecteTyre.iterator();
        while (itt.hasNext()) {
            Integer i = (Integer) itt.next();
            carTyre += i + 1 + ",";
        }
        if (!TextUtils.isEmpty(carTyre)) {

            //把最后一位逗号去掉
            String substring2 = carTyre.substring(0, carTyre.length() - 1);
            savaTyreProperty(substring2);
        } else {
            savaTyreProperty(null);
        }


        //行驶证车辆照片---1 颜色不一致（贴纸）、 2 天窗不一致、 3 轮毂不一致、 4 车身覆盖件不一致、 5 前照灯总成不一致
        Iterator itl = selecteLicense.iterator();
        while (itl.hasNext()) {
            Integer i = (Integer) itl.next();
            carLicense += i + 1 + ",";
        }
        if (!TextUtils.isEmpty(carLicense)) {

            //把最后一位逗号去掉
            String substring3 = carLicense.substring(0, carLicense.length() - 1);
            savaDrivingLicenseCheckEx(substring3);
        } else {
            savaDrivingLicenseCheckEx(null);
        }
        saveOtherInformToBD();

    }

    public void saveOtherInformToBD() {

        //不是修改的情况才保存
        if (!DetectMainActivity.detectMainActivity.isModify()) {

            //转成json
            String json = new Gson().toJson(otherCheckInfo);
            //保存到数据库
            DBManager.getInstance().updateOrInsert(Constants.OTHER_INFROMATION, taskId, PadSysApp.getUser().getUserId(), json);

        }
    }


    private void saveMileage(int mileage) {
        submitModel.setMileage(mileage);
        otherCheckInfo.setMileage(mileage);
    }

    private void saveShowmileage(int showMileage) {
        submitModel.setMileageSame(showMileage);
        otherCheckInfo.setMileageSame(showMileage);
    }

    private void saveShowGaugeLamp(int showGaugeLamp) {
        submitModel.setGaugeLamp(showGaugeLamp);
        otherCheckInfo.setGaugeLamp(showGaugeLamp);
    }

    private void saveCarColorActual(int color, String otherColorDes) {
        submitModel.setCarColorActual(color);
        otherCheckInfo.setCarColorActual(color);
        if (color == 99) {
            submitModel.setOtherColor(otherColorDes);
            otherCheckInfo.setOtherColor(otherColorDes);
        } else {
            submitModel.setOtherColor("");
            otherCheckInfo.setOtherColor("");
        }
    }

    private void savaNameplateProperty(String carNameplate) {
        submitModel.setNameplateProperty(carNameplate);
        otherCheckInfo.setNameplateProperty(carNameplate);
    }

    private void savaVinProperty(String vinProperty) {
        submitModel.setVinProperty(vinProperty);
        otherCheckInfo.setVinProperty(vinProperty);
    }

    private void savaTyreProperty(String tyreProperty) {
        submitModel.setTyreProperty(tyreProperty);
        otherCheckInfo.setTyreProperty(tyreProperty);
    }

    private void savaDrivingLicenseCheckEx(String License) {
        submitModel.setDrivingLicenseCheckEx(License);
        otherCheckInfo.setDrivingLicenseCheckEx(License);
    }

    public void saveData(String mileage,String otherColor) {
        if (mileage == null || mileage.length() == 0) {
            MyToast.showLong("表显里程未填写");
        } else if (mileage != null || mileage.length() != 0) {
            Double d = Double.valueOf(mileage);
            if (d == 0) {
                MyToast.showLong("表显里程必须大于 0 ");
            } else if (selecteColor.size() == 0) {
                MyToast.showLong("实车颜色必须选择一项");
            } else {
                saveDataToLocal(mileage, otherColor);
            }
        }
    }

    public void saveChange(String mileage,String otherColor) {
        saveDataToLocal(mileage, otherColor);
    }

    /**
     * 从本地取出缓存数据回显--wujj--2016/12/2
     */
    public void getSaveDataFromLocal(EditText etShowMileage, TagFlowLayout tflShowMileage, TagFlowLayout tflShowMileageLight,
                                     TagFlowLayout tflCarColor,
                                     TagFlowLayout tflCarNameplate,
                                     TagFlowLayout tflCarVin, TagFlowLayout tflCarTyre, TagFlowLayout tflCarLicense, EditText etOtherColor) {

        if (submitModel != null) {
            //公里数
            int mileage = submitModel.getMileage();
            if (mileage == -1 || mileage == 0)
                etShowMileage.setText("");
            else
                etShowMileage.setText(mileage + "");

            //公里与实车不符
            int carShowmileage = submitModel.getMileageSame();
            if (carShowmileage == 0)
                tflShowMileage.getAdapter().setSelectedList(carShowmileage);
            //仪表故障灯常亮
            int carShowGaugeLamp = submitModel.getGaugeLamp();
            if (carShowGaugeLamp == GaugeLampYes)
                tflShowMileageLight.getAdapter().setSelectedList(0);

            //实车颜色
            int carColorActual = submitModel.getCarColorActual();
            if (carColorActual == 16){
                tflCarColor.getAdapter().setSelectedList(carColorActual - 2);
            }else if (carColorActual == 99) {
                tflCarColor.getAdapter().setSelectedList(carColorActual - 84);//选的其他
                etOtherColor.setVisibility(View.VISIBLE);
                etOtherColor.setText(submitModel.getOtherColor());
            } else if (carColorActual != -1 && carColorActual != 0){
                tflCarColor.getAdapter().setSelectedList(carColorActual - 1);
            }

            //车身铭牌--1 破损、 2 实车铭牌未见、 3 铭牌出厂日期与登记证 yyyy 年 MM 月 dd 日不一致）
            String nameplateProperty = submitModel.getNameplateProperty();
            if (!TextUtils.isEmpty(nameplateProperty)) {

                String[] nameArr = nameplateProperty.split(",");
                Set<Integer> set = new HashSet<>();
                for (int i = 0; i < nameArr.length; i++) {
                    String s = nameArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        index -= 1;
                        set.add(index);
                    }
                }
                tflCarNameplate.getAdapter().setSelectedList(set);
            }

            //车架号VIN
            String vinProperty = submitModel.getVinProperty();
            if (!TextUtils.isEmpty(vinProperty)) {

                String[] vinArr = vinProperty.split(",");
                Set<Integer> set1 = new HashSet<>();
                for (int i = 0; i < vinArr.length; i++) {
                    String s = vinArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        index -= 1;
                        set1.add(index);
                    }
                }
                tflCarVin.getAdapter().setSelectedList(set1);
            }

            //轮胎规格
            String tyreProperty = submitModel.getTyreProperty();
            if (!TextUtils.isEmpty(tyreProperty)) {

                String[] tyreArr = tyreProperty.split(",");
                Set<Integer> set2 = new HashSet<>();
                for (int i = 0; i < tyreArr.length; i++) {
                    String s = tyreArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        index -= 1;
                        set2.add(index);
                    }
                }
                tflCarTyre.getAdapter().setSelectedList(set2);
            }

            //行驶证车辆照片
            String drivingLicenseCheckEx = submitModel.getDrivingLicenseCheckEx();
            if (!TextUtils.isEmpty(drivingLicenseCheckEx)) {

                String[] drivingLicenseArr = drivingLicenseCheckEx.split(",");
                Set<Integer> set3 = new HashSet<>();
                for (int i = 0; i < drivingLicenseArr.length; i++) {
                    String s = drivingLicenseArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        index -= 1;
                        set3.add(index);
                    }
                }
                tflCarLicense.getAdapter().setSelectedList(set3);
            }
        }

        //控制铭牌 和 vin 的单复选
        TagView tagView = (TagView) tflCarVin.getChildAt(OtherInformationFragment.CARVINS_CONTROL);
        if (tagView == null) {
            return;
        }
        if (tagView.isChecked()) {
            changeTagViewEnabled(0, OtherInformationFragment.CARVINS_CONTROL, tagView, tflCarVin);
        }
        TagView tagViewN = (TagView) tflCarNameplate.getChildAt(OtherInformationFragment.CARNAMEP_CONTROL);
        if (tagViewN.isChecked()) {
            changeTagViewEnabled(1, OtherInformationFragment.CARNAMEP_CONTROL, tagViewN, tflCarNameplate);
        }
    }


    /**
     * 切换 TagFlowLayout 里 item 多选 或 单选 的状态
     *
     * @param position      当前点击的 item 角标
     * @param control       控制项的角标，checked--->true：其他变为不可选状态: false:其他变为可选状态
     * @param tagV          当前Item
     * @param tagFlowLayout 当前tagFlowLayout
     */
    public void changeTagViewEnabled(int position, int control, TagView tagV, TagFlowLayout tagFlowLayout) {
        if (position == control && tagV.isChecked()) {

            for (int i = 0; i < tagFlowLayout.getChildCount(); i++) {
                if (i != control) {
                    TagView tag = (TagView) tagFlowLayout.getChildAt(i);
                    tag.isEnabled = false;
                    tag.setChecked(false);

                    //设置item背景为灰色不可选状态
                    TextView tv = (TextView) tag.getTagView();
                    tv.setBackgroundResource(R.drawable.not_optional_bg1);

                    if (tagFlowLayout.getSelectedList().contains(i)) {
                        tagFlowLayout.getSelectedList().remove(i);
                    }
                }
            }
        } else if (position == control && !tagV.isChecked()) {
            for (int i = 0; i < tagFlowLayout.getChildCount(); i++) {
                if (i != control) {
                    TagView tag = (TagView) tagFlowLayout.getChildAt(i);
                    tag.isEnabled = true;

                    //设置item背景为初始化的状态选择器
                    TextView tv = (TextView) tag.getTagView();
                    Drawable drawable = context.getResources().getDrawable(R.drawable.tag_bg1);
                    tv.setBackgroundDrawable(drawable);
                }
            }
        }
    }


    public void showRealTimeData(TagFlowLayout tflCarNameplate, TextView tvTyre) {

        if (submitModel == null) {
            return;
        }
        String productionTime = submitModel.getProductionTime();

//        String[] tagCarNameplates = {"破损", "实车铭牌未见", "铭牌出厂日期与登记证" + productionTime + "不一致"};
//        setTag(tflCarNameplate, tagCarNameplates);
        TagView childAt = (TagView) tflCarNameplate.getChildAt(2);
        TextView textView = (TextView) childAt.getTagView();
        textView.setText("铭牌出厂日期与登记证" + productionTime + "不一致");

        //手续信息中填的轮胎规格
        String tyre = submitModel.getTyre();
        if (!TextUtils.isEmpty(tyre)) {//created by wujj on 2017.5.9
            tvTyre.setVisibility(View.VISIBLE);
        } else {
            tvTyre.setVisibility(View.GONE);
        }
        tvTyre.setText(tyre);
    }

    /**
     * Created by 李波 on 2016/12/4.
     * 显示核对信息界面的 图片
     */
    public void showPic(SimpleDraweeView sdvCarLicense, SimpleDraweeView sdvCarTyre,String carTyreHttpUrl,
                        LinearLayout llCarTyre) {
        this.sdvCarTyre = sdvCarTyre;
        this.llCarTyre = llCarTyre;
        if (sdvCarLicense != null) {
            ifShowCarTyrePic(carTyreHttpUrl);
        }
    }

    /**
     * 显示轮胎规格照片
     * created by wujj on 2017.5.9
     */
    private void ifShowCarTyrePic(String carTyreHttpUrl) {
        String planId = DetectMainActivity.detectMainActivity.getTaskItem().getPlanId();
//        if (!TextUtils.isEmpty(planId)) {
        if (PadSysApp.wrapper != null) {//内存中如果存在对应planid的检测方案，则直接用
            showCarTyrePic(PadSysApp.wrapper,carTyreHttpUrl);
        } else {
            boolean isExist = DBManager.getInstance().isPlanExist(planId, Constants.DATA_TYPE_PLAN);//内存中不存在，查数据库中是否有检测方案
            if (isExist) {//数据库中存在,直接用
                String json = DBManager.getInstance().queryLocalPlan(planId, Constants.DATA_TYPE_PLAN);
                DetectionWrapper wrapper = new Gson().fromJson(json, DetectionWrapper.class);
                if (wrapper != null) {
                    PadSysApp.wrapper = wrapper;
                    showCarTyrePic(wrapper,carTyreHttpUrl);
                }
            }
        }
//        }
    }

    /**
     * created by wujj on 2017.5.9
     */
    private void showCarTyrePic(DetectionWrapper wrapper,String carTyreHttpUrl) {
        List<PictureItem> pictureList = wrapper.getPictureList();
        if (pictureList != null && pictureList.size() > 0) {
            for (int i = 0; i < pictureList.size(); i++) {
                String picId = pictureList.get(i).getPicId();
                if (picId.equals("26")) {
                    carTyreFlag = true;
                }
            }
            if (carTyreFlag == true) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llCarTyre.getLayoutParams();
                layoutParams.height = UIUtils.dip2px(context, 200);
                llCarTyre.setLayoutParams(layoutParams);
                sdvCarTyre.setVisibility(View.VISIBLE);
                if (FileUtils.isFileExists(PadSysApp.picDirPath + "26.jpg")) {
                    Fresco.getImagePipeline().evictFromCache(Uri.parse(PadSysApp.picDirPath +  "26.jpg"));
                    frescoImageLoader.displayImage(sdvCarTyre, PadSysApp.picDirPath +  "26.jpg");
                } else {
                    frescoImageLoader.displayImage(sdvCarTyre, carTyreHttpUrl);
                }
            } else {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llCarTyre.getLayoutParams();
                layoutParams.height = UIUtils.dip2px(context, 50);
                llCarTyre.setLayoutParams(layoutParams);
                sdvCarTyre.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置 etShowMileage OnTouch事件触碰时获取焦点
     * 解决点击浏览图片返回此界面时会自动回滚定位到当前焦点处。
     * 所以在点击浏览图片时让 etShowMileage 失去焦点
     */
    public void setEtshowMileageTouch(final EditText etShowMileage) {
        etShowMileage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (!etShowMileage.isFocusable()) {
                    etShowMileage.setFocusable(true);
                    etShowMileage.setFocusableInTouchMode(true);
                    etShowMileage.requestFocus();
                    etShowMileage.findFocus();
                }
                return false;
            }
        });
    }

    /**
     * 跳转浏览图片界面
     */
    public void toPictureActivity(String path, EditText etShowMileage, String picName, String picId) {
        etShowMileage.setFocusable(false); //让etShowMileage 失去焦点，否则返回此界面 ，界面会自动滑动到焦点处
        if (!TextUtils.isEmpty(path)) {

            //连续拍照图片列表
            ArrayList<PictureItem> pictureItems = new ArrayList<>();
            PictureItem p = new PictureItem();
            p.setPicPath(path);
            p.setPicId(picId);//created by wujj on 5.9
            p.setPicName(picName);//created by wujj on 5.9
            pictureItems.add(p);
            Intent i = new Intent(context, PictureZoomActivity.class);
            i.putExtra("url", path);//TODO pk add 20161128
            i.putExtra("pictureItems", pictureItems);
            i.putExtra("showRecapture", true);//created by wujj on 5.9
            i.putExtra("taskid", taskId);
            otherInformationFragment.startActivityForResult(i, PHOTO_BIG_PHOTO);//created by wujj on 5.9


        }
    }

    /**
     * 根据屏幕尺寸动态计算 TagFlowLayout 中不同情况的 Item 宽度：占满TagFlowLayout 、 一半、三分之一
     */
    public void calculationFlowLayoutItemWidth() {
        int rv_width_fragment_other = context.getResources().getDimensionPixelSize(R.dimen.fragment_other_rv_width);
        int tv_margin = context.getResources().getDimensionPixelSize(R.dimen.tv_margin);
        int fragment_other_information_view_width = context.getResources().getDimensionPixelSize(R.dimen.fragment_other_information_view_width);
        int fragment_other_information_padding = context.getResources().getDimensionPixelSize(R.dimen.fragment_other_information_padding);
        if (PadSysApp.getScreenSize() != 0) {
            int padHorizontalwidth = Constants.ScreenWidth > Constants.ScreenHeight ? Constants.ScreenWidth : Constants.ScreenHeight;
            int tagFlowLayoutWidth = (padHorizontalwidth - rv_width_fragment_other - fragment_other_information_view_width) / 2;
            tagFlowLayoutItemMatchSize = tagFlowLayoutWidth - fragment_other_information_padding * 2 - tv_margin * 2;
            tagFlowLayoutItemHalfSize = tagFlowLayoutItemMatchSize / 2 - tv_margin;
            tagFlowLayoutItemOneThird = tagFlowLayoutItemMatchSize / 3 - tv_margin * 2;
        }
    }

    /**
     * 根据屏幕尺寸和需求动态适配 TagFlowLayout布局Item的宽度，如果计算宽度失败，就用布局里写死的 dp 值
     *
     * @param position
     * @param tv            TagFlowLayout 的 item
     * @param tagFlowLayout
     */
    public void adapterItemWidth(int position, TextView tv, TagFlowLayout tagFlowLayout,
                                 TagFlowLayout tflShowMileage, TagFlowLayout tflShowMileageLight,
                                 TagFlowLayout tflCarNameplate, TagFlowLayout tflCarColor) {
        if (tagFlowLayoutItemMatchSize != 0 && tagFlowLayoutItemHalfSize != 0 && tagFlowLayoutItemOneThird != 0) {

            if (tagFlowLayout == tflShowMileage) {
                tv.getLayoutParams().width = tagFlowLayoutItemMatchSize;
                tv.setSelected(true);
            } else if (tagFlowLayout == tflShowMileageLight) {
                tv.getLayoutParams().width = tagFlowLayoutItemMatchSize;
                tv.setSelected(true);
            } else if (tagFlowLayout == tflCarNameplate && position == 2) {
                tv.getLayoutParams().width = tagFlowLayoutItemMatchSize;
                tv.setSelected(true);
            } else if (tagFlowLayout == tflCarColor) {
                tv.getLayoutParams().width = tagFlowLayoutItemOneThird;
            } else {
                tv.getLayoutParams().width = tagFlowLayoutItemHalfSize;
            }

        }
    }

    /**
     * Created by 李波 on 2017/1/5.
     * 给某个TagFlowLayout恢复初始值
     */
    public void reInitData(TagFlowLayout tagFlowLayout) {
        for (int i = 0; i < tagFlowLayout.getChildCount(); i++) {
            TagView tag = (TagView) tagFlowLayout.getChildAt(i);
            if (tag.isChecked())
                tag.setChecked(false);
            if (tagFlowLayout.getSelectedList().contains(i)) {
                tagFlowLayout.getSelectedList().remove(i);
            }
        }
    }

    /**
     * Created by 李波 on 2016/12/5.
     * 判断核对信息必填项是否已填写，确保提交
     */
    public boolean checkDataComplete(String mileage,String otherColor) {

        //点击提交时首先保存下当前数据到 提交对象，在检测当前界面必填数据的完整性
        saveDataToLocal(mileage, otherColor);

        boolean checkDataComplete = true;
        if (mileage == null || mileage.length() == 0) {
            checkDataComplete = false;
            MyToast.showLong("表显里程未填写");
        } else if (mileage != null || mileage.length() != 0) {
            Double d = Double.valueOf(mileage);
            if (d < 100) {//created by wujj on 2017/2/14
                checkDataComplete = false;
                MyToast.showLong("表显里程填写不能小于100公里");
            } else if (selecteColor.size() == 0) {
                checkDataComplete = false;
                MyToast.showLong("实车颜色必须选择一项");
            } else if (selecteColor.size() != 0) {
                Iterator itc = selecteColor.iterator();
                while (itc.hasNext()) {
                    Integer i = (Integer) itc.next();
                    if (i == 15) {
                        if (TextUtils.isEmpty(otherColor)) {
                            checkDataComplete = false;
                            MyToast.showLong("信息核对请填写实车颜色其他说明");
                        }
                    }
                }
            }
        }
        return checkDataComplete;
    }
}
