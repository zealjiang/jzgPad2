package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.BMWCarGoodsBean;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.widget.BmwCarGoodsItemView;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by wujj on 2017/10/25.
 * 邮箱：wujj@jingzhengu.com
 * 作用：随车附件
 */

public class BMWCarGoodsFragment extends BaseFragment {
    @BindView(R.id.tvNext)
    CustomRippleButton tvNext;
    Unbinder unbinder;
    @BindView(R.id.trunkShieldPlate)
    BmwCarGoodsItemView trunkShieldPlate;
    @BindView(R.id.spareTire)
    BmwCarGoodsItemView spareTire;
    @BindView(R.id.antiTheftScrew)
    BmwCarGoodsItemView antiTheftScrew;
    @BindView(R.id.liftingJack)
    BmwCarGoodsItemView liftingJack;
    @BindView(R.id.wheelBoltWrench)
    BmwCarGoodsItemView wheelBoltWrench;
    @BindView(R.id.inflatableSuit)
    BmwCarGoodsItemView inflatableSuit;
    @BindView(R.id.toolKit)
    BmwCarGoodsItemView toolKit;
    @BindView(R.id.firstAidKit)
    BmwCarGoodsItemView firstAidKit;
    @BindView(R.id.warningTriangleMark)
    BmwCarGoodsItemView warningTriangleMark;
    @BindView(R.id.licensePlateRack)
    BmwCarGoodsItemView licensePlateRack;
    @BindView(R.id.antenna)
    BmwCarGoodsItemView antenna;
    @BindView(R.id.backRowPad)
    BmwCarGoodsItemView backRowPad;
    @BindView(R.id.padCharger)
    BmwCarGoodsItemView padCharger;
    @BindView(R.id.portableCharger)
    BmwCarGoodsItemView portableCharger;
    @BindView(R.id.headPillow)
    BmwCarGoodsItemView headPillow;
    @BindView(R.id.aromatherapyBox)
    BmwCarGoodsItemView aromatherapyBox;
    @BindView(R.id.cigaretteLighter)
    BmwCarGoodsItemView cigaretteLighter;
    @BindView(R.id.startDoorMat)
    BmwCarGoodsItemView startDoorMat;
    @BindView(R.id.ashtray)
    BmwCarGoodsItemView ashtray;
    @BindView(R.id.remoteControl)
    BmwCarGoodsItemView remoteControl;
    @BindView(R.id.headset)
    BmwCarGoodsItemView headset;
    @BindView(R.id.reflectiveVest)
    BmwCarGoodsItemView reflectiveVest;
    @BindView(R.id.usbCharger)
    BmwCarGoodsItemView usbCharger;
    @BindView(R.id.fireExtinguisher)
    BmwCarGoodsItemView fireExtinguisher;
    private INextStepListener iNextStepListener;
    private BMWCarGoodsBean bmwCarGoodsBean;
    private ArrayList<BmwCarGoodsItemView> tagLists;
    private SubmitModel submitModel;

    @Override
    protected void initData() {
        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        //查询此条缓存是否存在
        boolean isExist = DBManager.getInstance().isExist(Constants.DATA_TYPE_BMW_CARGOODS, taskId, PadSysApp.getUser().getUserId());
        DBManager.getInstance().closeDB();
        if (isExist) {
            List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_BMW_CARGOODS, PadSysApp.getUser().getUserId());
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (list.get(i).getDataType().equals(Constants.DATA_TYPE_BMW_CARGOODS)) {
                        String json = list.get(i).getJson();
                        if (!TextUtils.isEmpty(json)) {
                            bmwCarGoodsBean = new Gson().fromJson(json, BMWCarGoodsBean.class);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void setView() {
        //一进来默认选中达标
        trunkShieldPlate.getRbtnStandardFront().setChecked(true);
        spareTire.getRbtnStandardFront().setChecked(true);
        antiTheftScrew.getRbtnStandardFront().setChecked(true);
        liftingJack.getRbtnStandardFront().setChecked(true);
        wheelBoltWrench.getRbtnStandardFront().setChecked(true);
        inflatableSuit.getRbtnStandardFront().setChecked(true);
        toolKit.getRbtnStandardFront().setChecked(true);
        firstAidKit.getRbtnStandardFront().setChecked(true);
        warningTriangleMark.getRbtnStandardFront().setChecked(true);
        licensePlateRack.getRbtnStandardFront().setChecked(true);
        antenna.getRbtnStandardFront().setChecked(true);
        backRowPad.getRbtnStandardFront().setChecked(true);
        padCharger.getRbtnStandardFront().setChecked(true);
        portableCharger.getRbtnStandardFront().setChecked(true);
        headPillow.getRbtnStandardFront().setChecked(true);
        aromatherapyBox.getRbtnStandardFront().setChecked(true);
        cigaretteLighter.getRbtnStandardFront().setChecked(true);
        startDoorMat.getRbtnStandardFront().setChecked(true);
        ashtray.getRbtnStandardFront().setChecked(true);
        remoteControl.getRbtnStandardFront().setChecked(true);
        headset.getRbtnStandardFront().setChecked(true);
        reflectiveVest.getRbtnStandardFront().setChecked(true);
        usbCharger.getRbtnStandardFront().setChecked(true);
        fireExtinguisher.getRbtnStandardFront().setChecked(true);
        showData();
    }

    /*
    *回显数据
    **/
    private void showData() {
        Set<Integer> set = new HashSet<Integer>();
        if (bmwCarGoodsBean != null) {
            //行李箱遮挡板
            int trunkShieldPlatePos = bmwCarGoodsBean.getSCFJ_TrunkShieldPlate();
            if (trunkShieldPlatePos == -1){
                set = null;
            }else {
                set.clear();
                set.add(trunkShieldPlatePos);
            }
            TagFlowLayout tfltrunkShieldPlate = trunkShieldPlate.getTfl();
            tfltrunkShieldPlate.getAdapter().setSelectedList(set);
            tfltrunkShieldPlate.getOnSelectListener().onSelected(set);

            int scfj_trunkShieldPlateStandard = bmwCarGoodsBean.getSCFJ_TrunkShieldPlateStandard();
            switch (scfj_trunkShieldPlateStandard) {
                case 0:
                    trunkShieldPlate.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    trunkShieldPlate.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //备胎
            if (set == null){
                set = new HashSet<>();
            }
            int spareTirePos = bmwCarGoodsBean.getSCFJ_SpareTire();
            if (spareTirePos == -1){
                set = null;
            }else {
                set.clear();
                set.add(spareTirePos);
            }
            TagFlowLayout tflspareTire = spareTire.getTfl();
            tflspareTire.getAdapter().setSelectedList(set);
            tflspareTire.getOnSelectListener().onSelected(set);

            int scfj_spareTireStandard = bmwCarGoodsBean.getSCFJ_SpareTireStandard();
            switch (scfj_spareTireStandard) {
                case 0:
                    spareTire.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    spareTire.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //防盗螺栓
            if (set == null){
                set = new HashSet<>();
            }
            int antiTheftScrewPos = bmwCarGoodsBean.getSCFJ_AntiTheftScrew();
            if (antiTheftScrewPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(antiTheftScrewPos);
            }
            TagFlowLayout tflantiTheftScrew = antiTheftScrew.getTfl();
            tflantiTheftScrew.getAdapter().setSelectedList(set);
            tflantiTheftScrew.getOnSelectListener().onSelected(set);

            int scfj_antiTheftScrewStandard = bmwCarGoodsBean.getSCFJ_AntiTheftScrewStandard();
            switch (scfj_antiTheftScrewStandard) {
                case 0:
                    antiTheftScrew.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    antiTheftScrew.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //千斤顶
            if (set == null){
                set = new HashSet<>();
            }
            int liftingJackPos = bmwCarGoodsBean.getSCFJ_LiftingJack();
            if (liftingJackPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(liftingJackPos);
            }
            TagFlowLayout tflliftingJack = liftingJack.getTfl();
            tflliftingJack.getAdapter().setSelectedList(set);
            tflliftingJack.getOnSelectListener().onSelected(set);

            int scfj_liftingJackStandard = bmwCarGoodsBean.getSCFJ_LiftingJackStandard();
            switch (scfj_liftingJackStandard) {
                case 0:
                    liftingJack.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    liftingJack.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //车轮螺栓扳手
            if (set == null){
                set = new HashSet<>();
            }
            int wheelBoltWrenchPos = bmwCarGoodsBean.getSCFJ_WheelBoltWrench();
            if (wheelBoltWrenchPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(wheelBoltWrenchPos);
            }
            TagFlowLayout tflwheelBoltWrench = wheelBoltWrench.getTfl();
            tflwheelBoltWrench.getAdapter().setSelectedList(set);
            tflwheelBoltWrench.getOnSelectListener().onSelected(set);

            int scfj_wheelBoltWrenchStandard = bmwCarGoodsBean.getSCFJ_WheelBoltWrenchStandard();
            switch (scfj_wheelBoltWrenchStandard) {
                case 0:
                    wheelBoltWrench.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    wheelBoltWrench.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //充气套件
            if (set == null){
                set = new HashSet<>();
            }
            int inflatableSuitPos = bmwCarGoodsBean.getSCFJ_InflatableSuit();
            if (inflatableSuitPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(inflatableSuitPos);
            }
            TagFlowLayout tflinflatableSuit = inflatableSuit.getTfl();
            tflinflatableSuit.getAdapter().setSelectedList(set);
            tflinflatableSuit.getOnSelectListener().onSelected(set);

            int scfj_inflatableSuitStandard = bmwCarGoodsBean.getSCFJ_InflatableSuitStandard();
            switch (scfj_inflatableSuitStandard) {
                case 0:
                    inflatableSuit.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    inflatableSuit.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //工具包
            if (set == null){
                set = new HashSet<>();
            }
            int toolKitPos = bmwCarGoodsBean.getSCFJ_ToolKit();
            if (toolKitPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(toolKitPos);
            }
            TagFlowLayout tfltoolKit = toolKit.getTfl();
            tfltoolKit.getAdapter().setSelectedList(set);
            tfltoolKit.getOnSelectListener().onSelected(set);

            int scfj_toolKitStandard = bmwCarGoodsBean.getSCFJ_ToolKitStandard();
            switch (scfj_toolKitStandard) {
                case 0:
                    toolKit.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    toolKit.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //急救包
            if (set == null){
                set = new HashSet<>();
            }
            int firstAidKitPos = bmwCarGoodsBean.getSCFJ_FirstAidKit();
            if (firstAidKitPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(firstAidKitPos);
            }
            TagFlowLayout tflfirstAidKit = firstAidKit.getTfl();
            tflfirstAidKit.getAdapter().setSelectedList(set);
            tflfirstAidKit.getOnSelectListener().onSelected(set);

            int scfj_firstAidKitStandard = bmwCarGoodsBean.getSCFJ_FirstAidKitStandard();
            switch (scfj_firstAidKitStandard) {
                case 0:
                    firstAidKit.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    firstAidKit.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //警示三角标志
            if (set == null){
                set = new HashSet<>();
            }
            int warningTriangleMarkPos = bmwCarGoodsBean.getSCFJ_WarningTriangleMark();
            if (warningTriangleMarkPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(warningTriangleMarkPos);
            }
            TagFlowLayout tflwarningTriangleMark = warningTriangleMark.getTfl();
            tflwarningTriangleMark.getAdapter().setSelectedList(set);
            tflwarningTriangleMark.getOnSelectListener().onSelected(set);

            int scfj_warningTriangleMarkStandard = bmwCarGoodsBean.getSCFJ_WarningTriangleMarkStandard();
            switch (scfj_warningTriangleMarkStandard) {
                case 0:
                    warningTriangleMark.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    warningTriangleMark.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //牌照架
            if (set == null){
                set = new HashSet<>();
            }
            int licensePlateRackPos = bmwCarGoodsBean.getSCFJ_LicensePlateRack();
            if (licensePlateRackPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(licensePlateRackPos);
            }
            TagFlowLayout tfllicensePlateRack = licensePlateRack.getTfl();
            tfllicensePlateRack.getAdapter().setSelectedList(set);
            tfllicensePlateRack.getOnSelectListener().onSelected(set);

            int scfj_licensePlateRackStandard = bmwCarGoodsBean.getSCFJ_LicensePlateRackStandard();
            switch (scfj_licensePlateRackStandard) {
                case 0:
                    licensePlateRack.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    licensePlateRack.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //天线
            if (set == null){
                set = new HashSet<>();
            }
            int antennaPos = bmwCarGoodsBean.getSCFJ_Antenna();
            if (antennaPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(antennaPos);
            }
            TagFlowLayout tflantenna = antenna.getTfl();
            tflantenna.getAdapter().setSelectedList(set);
            tflantenna.getOnSelectListener().onSelected(set);

            int scfj_antennaStandard = bmwCarGoodsBean.getSCFJ_AntennaStandard();
            switch (scfj_antennaStandard) {
                case 0:
                    antenna.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    antenna.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //后排PAD
            if (set == null){
                set = new HashSet<>();
            }
            int backRowPadPos = bmwCarGoodsBean.getSCFJ_BackRowPad();
            if (backRowPadPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(backRowPadPos);
            }
            TagFlowLayout tflbackRowPad = backRowPad.getTfl();
            tflbackRowPad.getAdapter().setSelectedList(set);
            tflbackRowPad.getOnSelectListener().onSelected(set);

            int scfj_backRowPadStandard = bmwCarGoodsBean.getSCFJ_BackRowPadStandard();
            switch (scfj_backRowPadStandard) {
                case 0:
                    backRowPad.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    backRowPad.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //PAD充电器
            if (set == null){
                set = new HashSet<>();
            }
            int padChargerPos = bmwCarGoodsBean.getSCFJ_PadCharger();
            if (padChargerPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(padChargerPos);
            }
            TagFlowLayout tflpadCharger = padCharger.getTfl();
            tflpadCharger.getAdapter().setSelectedList(set);
            tflpadCharger.getOnSelectListener().onSelected(set);

            int scfj_padChargerStandard = bmwCarGoodsBean.getSCFJ_PadChargerStandard();
            switch (scfj_padChargerStandard) {
                case 0:
                    padCharger.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    padCharger.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //便携式充电器NEV
            if (set == null){
                set = new HashSet<>();
            }
            int portableChargerPos = bmwCarGoodsBean.getSCFJ_PortableCharger();
            if (portableChargerPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(portableChargerPos);
            }
            TagFlowLayout tflportableCharger = portableCharger.getTfl();
            tflportableCharger.getAdapter().setSelectedList(set);
            tflportableCharger.getOnSelectListener().onSelected(set);

            int scfj_portableChargerStandard = bmwCarGoodsBean.getSCFJ_PortableChargerStandard();
            switch (scfj_portableChargerStandard) {
                case 0:
                    portableCharger.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    portableCharger.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //头枕
            if (set == null){
                set = new HashSet<>();
            }
            int headPillowPos = bmwCarGoodsBean.getSCFJ_HeadPillow();
            if (headPillowPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(headPillowPos);
            }
            TagFlowLayout tflheadPillow = headPillow.getTfl();
            tflheadPillow.getAdapter().setSelectedList(set);
            tflheadPillow.getOnSelectListener().onSelected(set);

            int scfj_headPillowStandard = bmwCarGoodsBean.getSCFJ_HeadPillowStandard();
            switch (scfj_headPillowStandard) {
                case 0:
                    headPillow.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    headPillow.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //香薰盒
            if (set == null){
                set = new HashSet<>();
            }
            int aromatherapyBoxPos = bmwCarGoodsBean.getSCFJ_AromatherapyBox();
            if (aromatherapyBoxPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(aromatherapyBoxPos);
            }
            TagFlowLayout tflaromatherapyBox = aromatherapyBox.getTfl();
            tflaromatherapyBox.getAdapter().setSelectedList(set);
            tflaromatherapyBox.getOnSelectListener().onSelected(set);

            int scfj_aromatherapyBoxStandard = bmwCarGoodsBean.getSCFJ_AromatherapyBoxStandard();
            switch (scfj_aromatherapyBoxStandard) {
                case 0:
                    aromatherapyBox.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    aromatherapyBox.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //点烟器
            if (set == null){
                set = new HashSet<>();
            }
            int cigaretteLighterPos = bmwCarGoodsBean.getSCFJ_CigaretteLighter();
            if (cigaretteLighterPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(cigaretteLighterPos);
            }
            TagFlowLayout tflcigaretteLighter = cigaretteLighter.getTfl();
            tflcigaretteLighter.getAdapter().setSelectedList(set);
            tflcigaretteLighter.getOnSelectListener().onSelected(set);

            int scfj_cigaretteLighterStandard = bmwCarGoodsBean.getSCFJ_CigaretteLighterStandard();
            switch (scfj_cigaretteLighterStandard) {
                case 0:
                    cigaretteLighter.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    cigaretteLighter.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //原厂脚垫
            if (set == null){
                set = new HashSet<>();
            }
            int startDoorMatPos = bmwCarGoodsBean.getSCFJ_StartDoorMat();
            if (startDoorMatPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(startDoorMatPos);
            }
            TagFlowLayout tflstartDoorMat = startDoorMat.getTfl();
            tflstartDoorMat.getAdapter().setSelectedList(set);
            tflstartDoorMat.getOnSelectListener().onSelected(set);

            int scfj_startDoorMatStandard = bmwCarGoodsBean.getSCFJ_StartDoorMatStandard();
            switch (scfj_startDoorMatStandard) {
                case 0:
                    startDoorMat.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    startDoorMat.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //烟灰缸
            if (set == null){
                set = new HashSet<>();
            }
            int ashtrayPos = bmwCarGoodsBean.getSCFJ_Ashtray();
            if (ashtrayPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(ashtrayPos);
            }
            TagFlowLayout tflashtray = ashtray.getTfl();
            tflashtray.getAdapter().setSelectedList(set);
            tflashtray.getOnSelectListener().onSelected(set);

            int scfj_ashtrayStandard = bmwCarGoodsBean.getSCFJ_AshtrayStandard();
            switch (scfj_ashtrayStandard) {
                case 0:
                    ashtray.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    ashtray.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //遥控器
            if (set == null){
                set = new HashSet<>();
            }
            int remoteControlPos = bmwCarGoodsBean.getSCFJ_RemoteControl();
            if (remoteControlPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(remoteControlPos);
            }
            TagFlowLayout tflremoteControl = remoteControl.getTfl();
            tflremoteControl.getAdapter().setSelectedList(set);
            tflremoteControl.getOnSelectListener().onSelected(set);

            int scfj_remoteControlStandard = bmwCarGoodsBean.getSCFJ_RemoteControlStandard();
            switch (scfj_remoteControlStandard) {
                case 0:
                    remoteControl.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    remoteControl.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //耳机
            if (set == null){
                set = new HashSet<>();
            }
            int headsetPos = bmwCarGoodsBean.getSCFJ_Headset();
            if (headsetPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(headsetPos);
            }
            TagFlowLayout tflheadset = headset.getTfl();
            tflheadset.getAdapter().setSelectedList(set);
            tflheadset.getOnSelectListener().onSelected(set);

            int scfj_headsetStandard = bmwCarGoodsBean.getSCFJ_HeadsetStandard();
            switch (scfj_headsetStandard) {
                case 0:
                    headset.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    headset.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //反光背心
            if (set == null){
                set = new HashSet<>();
            }
            int reflectiveVestPos = bmwCarGoodsBean.getSCFJ_ReflectiveVest();
            if (reflectiveVestPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(reflectiveVestPos);
            }
            TagFlowLayout tflreflectiveVest = reflectiveVest.getTfl();
            tflreflectiveVest.getAdapter().setSelectedList(set);
            tflreflectiveVest.getOnSelectListener().onSelected(set);

            int scfj_reflectiveVestStandard = bmwCarGoodsBean.getSCFJ_ReflectiveVestStandard();
            switch (scfj_reflectiveVestStandard) {
                case 0:
                    reflectiveVest.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    reflectiveVest.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //USB充电器
            if (set == null){
                set = new HashSet<>();
            }
            int usbChargerPos = bmwCarGoodsBean.getSCFJ_USBCharger();
            if (usbChargerPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(usbChargerPos);
            }
            TagFlowLayout tflusbChargerPos = usbCharger.getTfl();
            tflusbChargerPos.getAdapter().setSelectedList(set);
            tflusbChargerPos.getOnSelectListener().onSelected(set);

            int scfj_usbChargerStandard = bmwCarGoodsBean.getSCFJ_USBChargerStandard();
            switch (scfj_usbChargerStandard) {
                case 0:
                    usbCharger.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    usbCharger.getRbtnstandardBack().setChecked(true);
                    break;
            }

            //灭火器
            if (set == null){
                set = new HashSet<>();
            }
            int fireExtinguisherPos = bmwCarGoodsBean.getSCFJ_FireExtinguisher();
            if (fireExtinguisherPos == -1){
                set = null;
            }else {
                set.clear();
                set.add(fireExtinguisherPos);
            }
            TagFlowLayout tflfireExtinguisher = fireExtinguisher.getTfl();
            tflfireExtinguisher.getAdapter().setSelectedList(set);
            tflfireExtinguisher.getOnSelectListener().onSelected(set);

            int scfj_fireExtinguisherStandard = bmwCarGoodsBean.getSCFJ_FireExtinguisherStandard();
            switch (scfj_fireExtinguisherStandard) {
                case 0:
                    fireExtinguisher.getRbtnStandardFront().setChecked(true);
                    break;
                case 1:
                    fireExtinguisher.getRbtnstandardBack().setChecked(true);
                    break;
            }
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_goods_bmw, container, false);
        unbinder = ButterKnife.bind(this, view);
        initListener();
        return view;
    }

    private void initListener() {
        //行李箱遮挡板
        trunkShieldPlate.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {
                MyToast.showLong("-" + position);
            }
        });
        //备胎
        spareTire.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //防盗螺栓
        antiTheftScrew.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //千斤顶
        liftingJack.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //车轮螺栓扳手
        wheelBoltWrench.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //充气套件
        inflatableSuit.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //工具包
        toolKit.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //急救包
        firstAidKit.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //警示三角标志
        warningTriangleMark.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //牌照架
        licensePlateRack.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //天线
        antenna.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //后排PAD
        backRowPad.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //PAD充电器
        padCharger.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //便携式充电器NEV
        portableCharger.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //头枕
        headPillow.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //香薰盒
        aromatherapyBox.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //点烟器
        cigaretteLighter.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //原厂脚垫
        startDoorMat.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //烟灰缸
        ashtray.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //遥控器
        remoteControl.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //耳机
        headset.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //反光背心
        reflectiveVest.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });
        //usb充电器
        usbCharger.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {
                MyToast.showLong(position + "");
            }
        });
        //灭火器
        fireExtinguisher.setMyOnclickLister(new BmwCarGoodsItemView.IMyOnclickLister() {
            @Override
            public void OnTagclick(int position) {

            }
        });


    }


    public void setOnNextStepListener(INextStepListener iNextStepListener) {
        this.iNextStepListener = iNextStepListener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tvNext)
    public void onClick() {
        if (iNextStepListener != null) {
            iNextStepListener.nextStep(3);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    /*
    *保存数据
    **/
    private void saveData() {
        if (bmwCarGoodsBean == null) {
            bmwCarGoodsBean = new BMWCarGoodsBean();
        }
        if (submitModel == null){
            submitModel = ((BMWDetectMainActivity) getActivity()).getSubmitModel();
        }
        List<SubmitModel.SpCheckItemGroupListBean> spCheckItemGroupList = ((BMWDetectMainActivity) getActivity()).getSpCheckItemGroupList();
        //防止提交时重复add数据进spCheckItemGroupList中
        if (spCheckItemGroupList!=null&&spCheckItemGroupList.size()>0){
            for (int i = 0; i < spCheckItemGroupList.size(); i++){
                if (spCheckItemGroupList.get(i).getGroupId() == 3){
                    spCheckItemGroupList.remove(i);
                }
            }
        }
        SubmitModel.SpCheckItemGroupListBean spCheckItemGroupListBean = new SubmitModel.SpCheckItemGroupListBean();
        spCheckItemGroupListBean.setGroupId(3);
        List<SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean> SpCheckItemList = new ArrayList<>();

        //行李箱遮挡板
        int trunkShieldPlatePos = trunkShieldPlate.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_TrunkShieldPlate(trunkShieldPlatePos);
        int rbtnClickPos = trunkShieldPlate.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_TrunkShieldPlateStandard(rbtnClickPos);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean1 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean1.setNameEn("SCFJ_TrunkShieldPlate");//字段
        spCheckItemListBean1.setValueStr(trunkShieldPlatePos+"");
        switch (rbtnClickPos){
            case 0:
                spCheckItemListBean1.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean1.setIsQualified("0");
                break;
            default:spCheckItemListBean1.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean1);

        //备胎
        int spareTirePos = spareTire.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_SpareTire(spareTirePos);
        int rbtnClickPos1 = spareTire.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_SpareTireStandard(rbtnClickPos1);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean2 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean2.setNameEn("SCFJ_SpareTire");//字段
        spCheckItemListBean2.setValueStr(spareTirePos+"");
        switch (rbtnClickPos1){
            case 0:
                spCheckItemListBean2.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean2.setIsQualified("0");
                break;
            default:spCheckItemListBean2.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean2);

        //防盗螺栓
        int antiTheftScrewPos = antiTheftScrew.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_AntiTheftScrew(antiTheftScrewPos);
        int rbtnClickPos2 = antiTheftScrew.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_AntiTheftScrewStandard(rbtnClickPos2);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean3 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean3.setNameEn("SCFJ_AntiTheftScrew");//字段
        spCheckItemListBean3.setValueStr(antiTheftScrewPos+"");
        switch (rbtnClickPos2){
            case 0:
                spCheckItemListBean3.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean3.setIsQualified("0");
                break;
            default:spCheckItemListBean3.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean3);

        //千斤顶
        int liftingJackPos = liftingJack.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_LiftingJack(liftingJackPos);
        int rbtnClickPos3 = liftingJack.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_LiftingJackStandard(rbtnClickPos3);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean4 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean4.setNameEn("SCFJ_LiftingJack");//字段
        spCheckItemListBean4.setValueStr(liftingJackPos+"");
        switch (rbtnClickPos3){
            case 0:
                spCheckItemListBean4.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean4.setIsQualified("0");
                break;
            default:spCheckItemListBean4.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean4);

        //车轮螺栓扳手
        int wheelBoltWrenchPos = wheelBoltWrench.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_WheelBoltWrench(wheelBoltWrenchPos);
        int rbtnClickPos4 = wheelBoltWrench.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_WheelBoltWrenchStandard(rbtnClickPos4);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean5 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean5.setNameEn("SCFJ_WheelBoltWrench");//字段
        spCheckItemListBean5.setValueStr(wheelBoltWrenchPos+"");
        switch (rbtnClickPos4){
            case 0:
                spCheckItemListBean5.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean5.setIsQualified("0");
                break;
            default:spCheckItemListBean5.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean5);

        //充气套件
        int inflatableSuitPos = inflatableSuit.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_InflatableSuit(inflatableSuitPos);
        int rbtnClickPos5 = inflatableSuit.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_InflatableSuitStandard(rbtnClickPos5);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean6 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean6.setNameEn("SCFJ_InflatableSuit");//字段
        spCheckItemListBean6.setValueStr(inflatableSuitPos+"");
        switch (rbtnClickPos5){
            case 0:
                spCheckItemListBean6.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean6.setIsQualified("0");
                break;
            default:spCheckItemListBean6.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean6);

        //工具包
        int toolKitPos = toolKit.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_ToolKit(toolKitPos);
        int rbtnClickPos6 = toolKit.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_ToolKitStandard(rbtnClickPos6);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean7 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean7.setNameEn("SCFJ_ToolKit");//字段
        spCheckItemListBean7.setValueStr(toolKitPos+"");
        switch (rbtnClickPos6){
            case 0:
                spCheckItemListBean7.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean7.setIsQualified("0");
                break;
            default:spCheckItemListBean7.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean7);

        //急救包
        int firstAidKitPos = firstAidKit.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_FirstAidKit(firstAidKitPos);
        int rbtnClickPos7 = firstAidKit.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_FirstAidKitStandard(rbtnClickPos7);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean8 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean8.setNameEn("SCFJ_FirstAidKit");//字段
        spCheckItemListBean8.setValueStr(firstAidKitPos+"");
        switch (rbtnClickPos7){
            case 0:
                spCheckItemListBean8.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean8.setIsQualified("0");
                break;
            default:spCheckItemListBean8.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean8);

        //警示三角标志
        int warningTriangleMarkPos = warningTriangleMark.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_WarningTriangleMark(warningTriangleMarkPos);
        int rbtnClickPos8 = warningTriangleMark.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_WarningTriangleMarkStandard(rbtnClickPos8);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean9 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean9.setNameEn("SCFJ_WarningTriangleMark");//字段
        spCheckItemListBean9.setValueStr(warningTriangleMarkPos+"");
        switch (rbtnClickPos8){
            case 0:
                spCheckItemListBean9.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean9.setIsQualified("0");
                break;
            default:spCheckItemListBean9.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean9);

        //牌照架
        int licensePlateRackPos = licensePlateRack.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_LicensePlateRack(licensePlateRackPos);
        int rbtnClickPos9 = licensePlateRack.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_LicensePlateRackStandard(rbtnClickPos9);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean10 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean10.setNameEn("SCFJ_LicensePlateRack");//字段
        spCheckItemListBean10.setValueStr(licensePlateRackPos+"");
        switch (rbtnClickPos9){
            case 0:
                spCheckItemListBean10.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean10.setIsQualified("0");
                break;
            default:spCheckItemListBean10.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean10);

        //天线
        int antennaPos = antenna.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_Antenna(antennaPos);
        int rbtnClickPos10 = antenna.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_AntennaStandard(rbtnClickPos10);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean11 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean11.setNameEn("SCFJ_Antenna");//字段
        spCheckItemListBean11.setValueStr(antennaPos+"");
        switch (rbtnClickPos10){
            case 0:
                spCheckItemListBean11.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean11.setIsQualified("0");
                break;
            default:spCheckItemListBean11.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean11);

        //后排PAD
        int backRowPadPos = backRowPad.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_BackRowPad(backRowPadPos);
        int rbtnClickPos11 = backRowPad.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_BackRowPadStandard(rbtnClickPos11);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean12 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean12.setNameEn("SCFJ_BackRowPad");//字段
        spCheckItemListBean12.setValueStr(backRowPadPos+"");
        switch (rbtnClickPos11){
            case 0:
                spCheckItemListBean12.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean12.setIsQualified("0");
                break;
            default:spCheckItemListBean12.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean12);

        //PAD充电器
        int padChargerPos = padCharger.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_PadCharger(padChargerPos);
        int rbtnClickPos12 = padCharger.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_PadChargerStandard(rbtnClickPos12);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean13 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean13.setNameEn("SCFJ_PadCharger");//字段
        spCheckItemListBean13.setValueStr(padChargerPos+"");
        switch (rbtnClickPos12){
            case 0:
                spCheckItemListBean13.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean13.setIsQualified("0");
                break;
            default:spCheckItemListBean13.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean13);

        //便携式充电器NEV
        int portableChargerPos = portableCharger.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_PortableCharger(portableChargerPos);
        int rbtnClickPos13 = portableCharger.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_PortableChargerStandard(rbtnClickPos13);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean14 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean14.setNameEn("SCFJ_PortableCharger");//字段
        spCheckItemListBean14.setValueStr(portableChargerPos+"");
        switch (rbtnClickPos13){
            case 0:
                spCheckItemListBean14.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean14.setIsQualified("0");
                break;
            default:spCheckItemListBean14.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean14);

        //头枕
        int headPillowPos = headPillow.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_HeadPillow(headPillowPos);
        int rbtnClickPos14 = headPillow.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_HeadPillowStandard(rbtnClickPos14);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean15 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean15.setNameEn("SCFJ_HeadPillow");//字段
        spCheckItemListBean15.setValueStr(headPillowPos+"");
        switch (rbtnClickPos14){
            case 0:
                spCheckItemListBean15.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean15.setIsQualified("0");
                break;
            default:spCheckItemListBean15.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean15);

        //香薰盒
        int aromatherapyBoxPos = aromatherapyBox.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_AromatherapyBox(aromatherapyBoxPos);
        int rbtnClickPos15 = aromatherapyBox.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_AromatherapyBoxStandard(rbtnClickPos15);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean16 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean16.setNameEn("SCFJ_AromatherapyBox");//字段
        spCheckItemListBean16.setValueStr(aromatherapyBoxPos+"");
        switch (rbtnClickPos15){
            case 0:
                spCheckItemListBean16.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean16.setIsQualified("0");
                break;
            default:spCheckItemListBean16.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean16);

        //点烟器
        int cigaretteLighterPos = cigaretteLighter.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_CigaretteLighter(cigaretteLighterPos);
        int rbtnClickPos16 = cigaretteLighter.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_CigaretteLighterStandard(rbtnClickPos16);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean17 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean17.setNameEn("SCFJ_CigaretteLighter");//字段
        spCheckItemListBean17.setValueStr(cigaretteLighterPos+"");
        switch (rbtnClickPos16){
            case 0:
                spCheckItemListBean17.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean17.setIsQualified("0");
                break;
            default:spCheckItemListBean17.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean17);

        //原厂脚垫
        int startDoorMatPos = startDoorMat.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_StartDoorMat(startDoorMatPos);
        int rbtnClickPos17 = startDoorMat.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_StartDoorMatStandard(rbtnClickPos17);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean18 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean18.setNameEn("SCFJ_StartDoorMat");//字段
        spCheckItemListBean18.setValueStr(startDoorMatPos+"");
        switch (rbtnClickPos17){
            case 0:
                spCheckItemListBean18.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean18.setIsQualified("0");
                break;
            default:spCheckItemListBean18.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean18);

        //烟灰缸
        int ashtrayPos = ashtray.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_Ashtray(ashtrayPos);
        int rbtnClickPos18 = ashtray.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_AshtrayStandard(rbtnClickPos18);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean19 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean19.setNameEn("SCFJ_Ashtray");//字段
        spCheckItemListBean19.setValueStr(ashtrayPos+"");
        switch (rbtnClickPos18){
            case 0:
                spCheckItemListBean19.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean19.setIsQualified("0");
                break;
            default:spCheckItemListBean19.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean19);

        //遥控器
        int remoteControlPos = remoteControl.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_RemoteControl(remoteControlPos);
        int rbtnClickPos19 = remoteControl.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_RemoteControlStandard(rbtnClickPos19);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean20 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean20.setNameEn("SCFJ_RemoteControl");//字段
        spCheckItemListBean20.setValueStr(remoteControlPos+"");
        switch (rbtnClickPos19){
            case 0:
                spCheckItemListBean20.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean20.setIsQualified("0");
                break;
            default:spCheckItemListBean20.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean20);

        //耳机
        int headsetPos = headset.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_Headset(headsetPos);
        int rbtnClickPos20 = headset.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_HeadsetStandard(rbtnClickPos20);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean21 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean21.setNameEn("SCFJ_Headset");//字段
        spCheckItemListBean21.setValueStr(headsetPos+"");
        switch (rbtnClickPos20){
            case 0:
                spCheckItemListBean21.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean21.setIsQualified("0");
                break;
            default:spCheckItemListBean21.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean21);

        //反光背心
        int reflectiveVestPos = reflectiveVest.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_ReflectiveVest(reflectiveVestPos);
        int rbtnClickPos21 = reflectiveVest.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_ReflectiveVestStandard(rbtnClickPos21);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean22 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean22.setNameEn("SCFJ_ReflectiveVest");//字段
        spCheckItemListBean22.setValueStr(reflectiveVestPos+"");
        switch (rbtnClickPos21){
            case 0:
                spCheckItemListBean22.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean22.setIsQualified("0");
                break;
            default:spCheckItemListBean22.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean22);

        //USB充电器
        int usbChargerPos = usbCharger.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_USBCharger(usbChargerPos);
        int rbtnClickPos22 = usbCharger.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_USBChargerStandard(rbtnClickPos22);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean23 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean23.setNameEn("SCFJ_USBCharger");//字段
        spCheckItemListBean23.setValueStr(usbChargerPos+"");
        switch (rbtnClickPos22){
            case 0:
                spCheckItemListBean23.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean23.setIsQualified("0");
                break;
            default:spCheckItemListBean23.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean23);

        //灭火器
        int fireExtinguisherPos = fireExtinguisher.getTagClickPos();
        bmwCarGoodsBean.setSCFJ_FireExtinguisher(fireExtinguisherPos);
        int rbtnClickPos23 = fireExtinguisher.getRbtnClickPos();
        bmwCarGoodsBean.setSCFJ_FireExtinguisherStandard(rbtnClickPos23);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean24 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean24.setNameEn("SCFJ_FireExtinguisher");//字段
        spCheckItemListBean24.setValueStr(fireExtinguisherPos+"");
        switch (rbtnClickPos23){
            case 0:
                spCheckItemListBean24.setIsQualified("1");
                break;
            case 1:
                spCheckItemListBean24.setIsQualified("0");
                break;
            default:spCheckItemListBean24.setIsQualified("-1");
        }
        SpCheckItemList.add(spCheckItemListBean24);

        spCheckItemGroupListBean.setSpCheckItemList(SpCheckItemList);
        spCheckItemGroupList.add(spCheckItemGroupListBean);
        submitModel.setSpCheckItemGroupList(spCheckItemGroupList);
        ((BMWDetectMainActivity) getActivity()).setSubmitModel(submitModel);

        //保存到数据库
        String json = new Gson().toJson(bmwCarGoodsBean);
        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_BMW_CARGOODS, taskId, PadSysApp.getUser().getUserId(), json);
    }

    //检查不可为空项是否都已填写并保存
    public boolean checkAndSaveData() {
        if (tagLists == null){
            tagLists = new ArrayList<>();
        }
        tagLists.clear();
        tagLists.add(trunkShieldPlate);
        tagLists.add(spareTire);
        tagLists.add(antiTheftScrew);
        tagLists.add(liftingJack);
        tagLists.add(wheelBoltWrench);
        tagLists.add(inflatableSuit);
        tagLists.add(toolKit);
        tagLists.add(firstAidKit);
        tagLists.add(warningTriangleMark);
        tagLists.add(licensePlateRack);
        tagLists.add(antenna);
        tagLists.add(backRowPad);
        tagLists.add(padCharger);
        tagLists.add(portableCharger);
        tagLists.add(headPillow);
        tagLists.add(aromatherapyBox);
        tagLists.add(cigaretteLighter);
        tagLists.add(startDoorMat);
        tagLists.add(ashtray);
        tagLists.add(remoteControl);
        tagLists.add(headset);
        tagLists.add(reflectiveVest);
        tagLists.add(usbCharger);
        tagLists.add(fireExtinguisher);
        if (tagLists != null && tagLists.size()>0){
            for (int i = 0; i < tagLists.size(); i++){
                int tagClickPos = tagLists.get(i).getTagClickPos();
                int rbtnClickPos = tagLists.get(i).getRbtnClickPos();
                if (tagClickPos == -1 || rbtnClickPos == -1){
                    MyToast.showLong("随车附件填写不完整");
                    return false;
                }
            }
        }
        saveData();
        return true;
    }
}
