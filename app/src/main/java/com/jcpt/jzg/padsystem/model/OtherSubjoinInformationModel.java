package com.jcpt.jzg.padsystem.model;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.OtherSubjoinInfoBean;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectDetailItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectType;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.vo.detection.LocalDetectionData;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wujj on 2016/12/5.
 * 邮箱：wujj@jingzhengu.com
 * 作用：其他信息--附加信息Model
 */

public class OtherSubjoinInformationModel {
    private Context context;
    private String taskId;

    public OtherSubjoinInformationModel(Context context, String taskId) {
        this.context = context;
        this.taskId = taskId;
    }

    public void showImgStarCheckedOut5(View imgStarNormalOut5, View imgStarCheckedOut5) {
        imgStarNormalOut5.setVisibility(View.GONE);
        imgStarCheckedOut5.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedOut4(View imgStarNormalOut4, View imgStarCheckedOut4) {
        imgStarNormalOut4.setVisibility(View.GONE);
        imgStarCheckedOut4.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedOut3(View imgStarNormalOut3, View imgStarCheckedOut3) {
        imgStarNormalOut3.setVisibility(View.GONE);
        imgStarCheckedOut3.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedOut2(View imgStarNormalOut2, View imgStarCheckedOut2) {
        imgStarNormalOut2.setVisibility(View.GONE);
        imgStarCheckedOut2.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedOut1(View imgStarNormalOut1, View imgStarCheckedOut1) {
        imgStarNormalOut1.setVisibility(View.GONE);
        imgStarCheckedOut1.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedAccident5(View imgStarNormalAccident5, View imgStarCheckedAccident5) {
        imgStarNormalAccident5.setVisibility(View.GONE);
        imgStarCheckedAccident5.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedAccident4(View imgStarNormalAccident4, View imgStarCheckedAccident4) {
        imgStarNormalAccident4.setVisibility(View.GONE);
        imgStarCheckedAccident4.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedAccident3(View imgStarNormalAccident3, View imgStarCheckedAccident3) {
        imgStarNormalAccident3.setVisibility(View.GONE);
        imgStarCheckedAccident3.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedAccident2(View imgStarNormalAccident2, View imgStarCheckedAccident2) {
        imgStarNormalAccident2.setVisibility(View.GONE);
        imgStarCheckedAccident2.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedAccident1(View imgStarNormalAccident1, View imgStarCheckedAccident1) {
        imgStarNormalAccident1.setVisibility(View.GONE);
        imgStarCheckedAccident1.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedCabin5(View imgStarNormalCabin5, View imgStarCheckedCabin5) {
        imgStarNormalCabin5.setVisibility(View.GONE);
        imgStarCheckedCabin5.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedCabin4(View imgStarNormalCabin4, View imgStarCheckedCabin4) {
        imgStarNormalCabin4.setVisibility(View.GONE);
        imgStarCheckedCabin4.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedCabin3(View imgStarNormalCabin3, View imgStarCheckedCabin3) {
        imgStarNormalCabin3.setVisibility(View.GONE);
        imgStarCheckedCabin3.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedCabin2(View imgStarNormalCabin2, View imgStarCheckedCabin2) {
        imgStarNormalCabin2.setVisibility(View.GONE);
        imgStarCheckedCabin2.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedCabin1(View imgStarNormalCabin1, View imgStarCheckedCabin1) {
        imgStarNormalCabin1.setVisibility(View.GONE);
        imgStarCheckedCabin1.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedIn5(View imgStarNormalIn5, View imgStarCheckedIn5) {
        imgStarNormalIn5.setVisibility(View.GONE);
        imgStarCheckedIn5.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedIn4(View imgStarNormalIn4, View imgStarCheckedIn4) {
        imgStarNormalIn4.setVisibility(View.GONE);
        imgStarCheckedIn4.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedIn3(View imgStarNormalIn3, View imgStarCheckedIn3) {
        imgStarNormalIn3.setVisibility(View.GONE);
        imgStarCheckedIn3.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedIn2(View imgStarNormalIn2, View imgStarCheckedIn2) {
        imgStarNormalIn2.setVisibility(View.GONE);
        imgStarCheckedIn2.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedIn1(View imgStarNormalIn1, View imgStarCheckedIn1) {
        imgStarNormalIn1.setVisibility(View.GONE);
        imgStarCheckedIn1.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedChassis5(View imgStarNormalChassis5, View imgStarCheckedChassis5) {
        imgStarNormalChassis5.setVisibility(View.GONE);
        imgStarCheckedChassis5.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedChassis4(View imgStarNormalChassis4, View imgStarCheckedChassis4) {
        imgStarNormalChassis4.setVisibility(View.GONE);
        imgStarCheckedChassis4.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedChassis3(View imgStarNormalChassis3, View imgStarCheckedChassis3) {
        imgStarNormalChassis3.setVisibility(View.GONE);
        imgStarCheckedChassis3.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedChassis2(View imgStarNormalChassis2, View imgStarCheckedChassis2) {
        imgStarNormalChassis2.setVisibility(View.GONE);
        imgStarCheckedChassis2.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedChassis1(View imgStarNormalChassis1, View imgStarCheckedChassis1) {
        imgStarNormalChassis1.setVisibility(View.GONE);
        imgStarCheckedChassis1.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedElectric5(View imgStarNormalElectric5, View imgStarCheckedElectric5) {
        imgStarNormalElectric5.setVisibility(View.GONE);
        imgStarCheckedElectric5.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedElectric4(View imgStarNormalElectric4, View imgStarCheckedElectric4) {
        imgStarNormalElectric4.setVisibility(View.GONE);
        imgStarCheckedElectric4.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedElectric3(View imgStarNormalElectric3, View imgStarCheckedElectric3) {
        imgStarNormalElectric3.setVisibility(View.GONE);
        imgStarCheckedElectric3.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedElectric2(View imgStarNormalElectric2, View imgStarCheckedElectric2) {
        imgStarNormalElectric2.setVisibility(View.GONE);
        imgStarCheckedElectric2.setVisibility(View.VISIBLE);
    }

    public void showImgStarCheckedElectric1(View imgStarNormalElectric1, View imgStarCheckedElectric1) {
        imgStarNormalElectric1.setVisibility(View.GONE);
        imgStarCheckedElectric1.setVisibility(View.VISIBLE);
    }

    /**
     * 判断所有选项是否为空
     */
    public boolean ifAllNotEmpty(LinearLayout llOut, RadioGroup rgOut, LinearLayout llAccident, RadioGroup rgAccident, LinearLayout llCabin, RadioGroup rgCabin,
                                 LinearLayout llIn, RadioGroup rgIn, LinearLayout llChassis, RadioGroup rgChassis, LinearLayout llElectric, RadioGroup rgElectric,
                                 RadioGroup rgFire, RadioGroup rgWater, RadioGroup rgHoldingvolume, RadioGroup rgRecognition, RadioGroup rgHedgeRatio,
                                 LinearLayout llBuyPrice, EditText etBuy, LinearLayout llSalePrice, EditText etSale, int hideSize,int isAuto,LinearLayout llFire,LinearLayout llWater) {
        int i = 0;
        if (llOut.getVisibility() == View.VISIBLE) {
            if (rgOut.getCheckedRadioButtonId() != -1)//选中了一项
                i += 1;
        }
        if (llAccident.getVisibility() == View.VISIBLE) {
            if (rgAccident.getCheckedRadioButtonId() != -1)//选中了一项
                i += 1;
        }
        if (llCabin.getVisibility() == View.VISIBLE) {
            if (rgCabin.getCheckedRadioButtonId() != -1)//选中了一项
                i += 1;
        }
        if (llIn.getVisibility() == View.VISIBLE) {
            if (rgIn.getCheckedRadioButtonId() != -1)//选中了一项
                i += 1;
        }
        if (llChassis.getVisibility() == View.VISIBLE) {
            if (rgChassis.getCheckedRadioButtonId() != -1)//选中了一项
                i += 1;
        }
        if (llElectric.getVisibility() == View.VISIBLE) {
            if (rgElectric.getCheckedRadioButtonId() != -1)//选中了一项
                i += 1;
        }
        if (llFire.getVisibility() == View.VISIBLE){
            if (rgFire.getCheckedRadioButtonId() != -1) {
                i += 1;
            }
        }
        if (llWater.getVisibility() == View.VISIBLE){
            if (rgWater.getCheckedRadioButtonId() != -1) {
                i += 1;
            }
        }
        if (rgHoldingvolume.getCheckedRadioButtonId() != -1) {
            i += 1;
        }
        if (rgRecognition.getCheckedRadioButtonId() != -1) {
            i += 1;
        }
        if (rgHedgeRatio.getCheckedRadioButtonId() != -1) {
            i += 1;
        }
        if (llBuyPrice.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(etBuy.getText())) {
                MyToast.showShort(context.getString(R.string.please_type_buy_price));
                return false;
            }
        }
        String strBuy = etBuy.getText().toString();
        float buyPrice = Float.parseFloat(strBuy);
        if (buyPrice == 0) {
            MyToast.showShort(context.getString(R.string.buy_price_can_not_be_zero));
            return false;
        }
        if (buyPrice > 999.98){
            MyToast.showShort(context.getString(R.string.buy_price_can_not_be_greater_than_999_98));
            return false;
        }
        if (llSalePrice.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(etSale.getText())) {
                MyToast.showShort(context.getString(R.string.please_type_sale_price));
                return false;
            }
        }
        String strSale = etSale.getText().toString();
        float salePrice = Float.parseFloat(strSale);
        if (salePrice == 0) {
            MyToast.showShort(context.getString(R.string.sale_price_can_not_be_zero));
            return false;
        }
        if (salePrice > 999.99){
            MyToast.showShort(context.getString(R.string.sale_price_can_not_be_greater_than_999_99));
            return false;
        }
        if (buyPrice >= salePrice) {
            MyToast.showShort(context.getString(R.string.sale_price_must_be_greater_than_buy_price));
            return false;
        }
        if (isAuto == 0){//手动
            if (i < 11 - hideSize) {
                MyToast.showLong(context.getString(R.string.content_not_complete));
                return false;
            }
        }else {//自动
            if (i < 3) {
                MyToast.showLong(context.getString(R.string.content_not_complete));
                return false;
            }
        }
        return true;
    }

    /**
     * 保存数据到数据库
     *
     * @param submitModel
     * @param llBuyPrice
     * @param llSalePrice
     * @param etBuy--
     * @param etSale
     * @param rgOut
     * @param rgAccident
     * @param rgCabin
     * @param rgIn
     * @param rgChassis
     * @param rgElectric
     * @param rgFire
     * @param rgWater
     * @param rgHoldingvolume
     * @param rgRecognition
     * @param rgHedgeRatio
     * @param etOtherInfo
     */
    public void saveDataToDB(OtherSubjoinInfoBean otherSubjoinInfoBean, SubmitModel submitModel, LinearLayout llBuyPrice, LinearLayout llSalePrice, EditText etBuy, EditText etSale,
                             RadioGroup rgOut, RadioGroup rgAccident, RadioGroup rgCabin, RadioGroup rgIn, RadioGroup rgChassis, RadioGroup rgElectric,
                             RadioGroup rgFire, RadioGroup rgWater, RadioGroup rgHoldingvolume, RadioGroup rgRecognition, RadioGroup rgHedgeRatio,
                             EditText etOtherInfo) {
        String buyPrice;
        String salePrice;
        if (llBuyPrice.getVisibility() == View.VISIBLE && llSalePrice.getVisibility() == View.VISIBLE) {
            buyPrice = etBuy.getText().toString().trim();
            salePrice = etSale.getText().toString().trim();
        } else {
            buyPrice = "";
            salePrice = "";
        }
        if (!TextUtils.isEmpty(buyPrice)) {
            float v = Float.parseFloat(buyPrice);
            //车商收车价
            otherSubjoinInfoBean.setAssessmentPrace(v);
            submitModel.setAssessmentPrace(v);
        } else {
            otherSubjoinInfoBean.setAssessmentPrace(-1);
            submitModel.setAssessmentPrace(-1);
        }
        if (!TextUtils.isEmpty(salePrice)) {
            float v = Float.parseFloat(salePrice);
            //车商售车价
            otherSubjoinInfoBean.setSalePrice(v);
            submitModel.setSalePrice(v);
        } else {
            otherSubjoinInfoBean.setSalePrice(-1);
            submitModel.setSalePrice(-1);
        }
        //外观检查选中的下标
        int rbtnOutId = rgOut.getCheckedRadioButtonId();
        int rbtnOutIndex = rgOut.indexOfChild(rgOut.findViewById(rbtnOutId));
        int outIndex = 0;
        switch (rbtnOutIndex) {
            case 0:
                outIndex = rbtnOutIndex + 5;
                break;
            case 1:
                outIndex = rbtnOutIndex + 3;
                break;
            case 2:
                outIndex = rbtnOutIndex + 1;
                break;
            case 3:
                outIndex = rbtnOutIndex - 1;
                break;
            case 4:
                outIndex = rbtnOutIndex - 3;
                break;
            default:
        }
        otherSubjoinInfoBean.setAppearanceCheck(outIndex);
        submitModel.setAppearanceCheck(outIndex);
        //事故检查选中的下标
        int rbtnAccidentId = rgAccident.getCheckedRadioButtonId();
        int rbtnAccidentIndex = rgAccident.indexOfChild(rgAccident.findViewById(rbtnAccidentId));
        int accidentIndex = 0;
        switch (rbtnAccidentIndex) {
            case 0:
                accidentIndex = rbtnAccidentIndex + 5;
                break;
            case 1:
                accidentIndex = rbtnAccidentIndex + 3;
                break;
            case 2:
                accidentIndex = rbtnAccidentIndex + 1;
                break;
            case 3:
                accidentIndex = rbtnAccidentIndex - 1;
                break;
            case 4:
                accidentIndex = rbtnAccidentIndex - 3;
                break;
            default:
        }
        otherSubjoinInfoBean.setAccidentCheck(accidentIndex);
        submitModel.setAccidentCheck(accidentIndex);
        //机舱检查选中的下标
        int rbtnCabinId = rgCabin.getCheckedRadioButtonId();
        int rbtnCabinIndex = rgCabin.indexOfChild(rgCabin.findViewById(rbtnCabinId));
        int cabinIndex = 0;
        switch (rbtnCabinIndex) {
            case 0:
                cabinIndex = rbtnCabinIndex + 5;
                break;
            case 1:
                cabinIndex = rbtnCabinIndex + 3;
                break;
            case 2:
                cabinIndex = rbtnCabinIndex + 1;
                break;
            case 3:
                cabinIndex = rbtnCabinIndex - 1;
                break;
            case 4:
                cabinIndex = rbtnCabinIndex - 3;
                break;
            default:
        }
        otherSubjoinInfoBean.setEngineCheck(cabinIndex);
        submitModel.setEngineCheck(cabinIndex);
        //内饰检查选中的下标
        int rbtnInId = rgIn.getCheckedRadioButtonId();
        int rbtnInIndex = rgIn.indexOfChild(rgIn.findViewById(rbtnInId));
        int inIndex = 0;
        switch (rbtnInIndex) {
            case 0:
                inIndex = rbtnInIndex + 5;
                break;
            case 1:
                inIndex = rbtnInIndex + 3;
                break;
            case 2:
                inIndex = rbtnInIndex + 1;
                break;
            case 3:
                inIndex = rbtnInIndex - 1;
                break;
            case 4:
                inIndex = rbtnInIndex - 3;
                break;
            default:
        }
        otherSubjoinInfoBean.setDecorateCheck(inIndex);
        submitModel.setDecorateCheck(inIndex);
        //底盘检查选中的下标
        int rbtnChassisId = rgChassis.getCheckedRadioButtonId();
        int rbtnChassisIndex = rgChassis.indexOfChild(rgChassis.findViewById(rbtnChassisId));
        int chassisIndex = 0;
        switch (rbtnChassisIndex) {
            case 0:
                chassisIndex = rbtnChassisIndex + 5;
                break;
            case 1:
                chassisIndex = rbtnChassisIndex + 3;
                break;
            case 2:
                chassisIndex = rbtnChassisIndex + 1;
                break;
            case 3:
                chassisIndex = rbtnChassisIndex - 1;
                break;
            case 4:
                chassisIndex = rbtnChassisIndex - 3;
                break;
            default:
        }
        otherSubjoinInfoBean.setChassisCheck(chassisIndex);
        submitModel.setChassisCheck(chassisIndex);
        //电气检查选中的下标
        int rbtnElectricId = rgElectric.getCheckedRadioButtonId();
        int rbtnElectricIndex = rgElectric.indexOfChild(rgElectric.findViewById(rbtnElectricId));
        int electricIndex = 0;
        switch (rbtnElectricIndex) {
            case 0:
                electricIndex = rbtnElectricIndex + 5;
                break;
            case 1:
                electricIndex = rbtnElectricIndex + 3;
                break;
            case 2:
                electricIndex = rbtnElectricIndex + 1;
                break;
            case 3:
                electricIndex = rbtnElectricIndex - 1;
                break;
            case 4:
                electricIndex = rbtnElectricIndex - 3;
                break;
            default:
        }
        otherSubjoinInfoBean.setElectricalCheck(electricIndex);
        submitModel.setElectricalCheck(electricIndex);
        //火烧迹象选中的下标--火烧迹象： 0 未见火烧迹象、 1 存在火烧迹象、 2 经综合判断为火烧车
        int rbtnFireId = rgFire.getCheckedRadioButtonId();
        int rbtnFireIndex = rgFire.indexOfChild(rgFire.findViewById(rbtnFireId));
        otherSubjoinInfoBean.setBurningMark(rbtnFireIndex);
        submitModel.setBurningMark(rbtnFireIndex);
        //泡水迹象选中的下标--泡水迹象： 0 未见泡水迹象、 1 存在泡水迹象、 2 经综合判断为泡水车
        int rbtnWaterId = rgWater.getCheckedRadioButtonId();
        int rbtnWaterIndex = rgWater.indexOfChild(rgWater.findViewById(rbtnWaterId));
        otherSubjoinInfoBean.setWaterMark(rbtnWaterIndex);
        submitModel.setWaterMark(rbtnWaterIndex);
        //市场保有量选中的下标--1 较高， 2 一般， 3 较低
        int rbtnHoldingId = rgHoldingvolume.getCheckedRadioButtonId();
        int rbtnHoldingIndex = rgHoldingvolume.indexOfChild(rgHoldingvolume.findViewById(rbtnHoldingId));
        int holdingIndex = rbtnHoldingIndex + 1;
        otherSubjoinInfoBean.setMarketOwnership(holdingIndex);
        submitModel.setMarketOwnership(holdingIndex);
        //市场认可度选中的下标--1 较高， 2 一般， 3 较低
        int rbtnRecognitionId = rgRecognition.getCheckedRadioButtonId();
        int rbtnRecognitionIndex = rgRecognition.indexOfChild(rgRecognition.findViewById(rbtnRecognitionId));
        int recognitionIndex = rbtnRecognitionIndex + 1;
        otherSubjoinInfoBean.setMarketAcceptance(recognitionIndex);
        submitModel.setMarketAcceptance(recognitionIndex);
        //市场保值率选中的下标--1 较高， 2 一般， 3 较低
        int rbtnHedgeRatioId = rgHedgeRatio.getCheckedRadioButtonId();
        int rbtnHedgeRatioIndex = rgHedgeRatio.indexOfChild(rgHedgeRatio.findViewById(rbtnHedgeRatioId));
        int hedgeRationIndex = rbtnHedgeRatioIndex + 1;
        otherSubjoinInfoBean.setMarketHedgeRatio(hedgeRationIndex);
        submitModel.setMarketHedgeRatio(hedgeRationIndex);
        //其他信息
        String otherInfo = etOtherInfo.getText().toString();
        otherSubjoinInfoBean.setAssessmentDes(otherInfo);
        submitModel.setAssessmentDes(otherInfo);

        //保存到数据库--不是修改的情况下才保存到数据库
        if (!DetectMainActivity.detectMainActivity.isModify()) {
            String json = new Gson().toJson(otherSubjoinInfoBean);
            DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_SUBJOININFO, taskId, PadSysApp.getUser().getUserId(), json);
        }
    }

    /**
     * 从本地取出数据并展示
     */
    public void getSaveDataFromDB(OtherSubjoinInfoBean otherSubjoinInfoBean, EditText etBuy, EditText etSale, EditText etOtherInfo, RadioButton rbtnOut1,
                                  RadioButton rbtnOut2, RadioButton rbtnOut3, RadioButton rbtnOut4, RadioButton rbtnOut5,
                                  RadioButton rbtnAccident1, RadioButton rbtnAccident2, RadioButton rbtnAccident3, RadioButton rbtnAccident4, RadioButton rbtnAccident5,
                                  RadioButton rbtnCabin1, RadioButton rbtnCabin2, RadioButton rbtnCabin3, RadioButton rbtnCabin4, RadioButton rbtnCabin5,
                                  RadioButton rbtnIn1, RadioButton rbtnIn2, RadioButton rbtnIn3, RadioButton rbtnIn4, RadioButton rbtnIn5,
                                  RadioButton rbtnChassis1, RadioButton rbtnChassis2, RadioButton rbtnChassis3, RadioButton rbtnChassis4, RadioButton rbtnChassis5,
                                  RadioButton rbtnElectric1, RadioButton rbtnElectric2, RadioButton rbtnElectric3, RadioButton rbtnElectric4, RadioButton rbtnElectric5,
                                  RadioButton rbtnFireNo, RadioButton rbtnFireYes, RadioButton rbtnFire, RadioButton rbtnWaterNo, RadioButton rbtnWaterYes, RadioButton rbtnWater,
                                  RadioButton rbtnHoldingHigh, RadioButton rbtnHoldingGeneral, RadioButton rbtnHoldingLow,
                                  RadioButton rbtnAcceptHigh, RadioButton rbtnAcceptGeneral, RadioButton rbtnAcceptLow,
                                  RadioButton rbtnHedgeRatioHigh, RadioButton rbtnHedgeRatioGeneral, RadioButton rbtnHedgeRatioLow) {
        if (otherSubjoinInfoBean != null) {
            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            float f1 = otherSubjoinInfoBean.getAssessmentPrace();
            if (f1 != -1) {
                if (String.valueOf(f1).startsWith("0")) {
                    String priceBuy = decimalFormat.format(f1);
                    etBuy.setText(0 + priceBuy);
                } else {
                    String priceBuy = decimalFormat.format(f1);
                    etBuy.setText(priceBuy);
                }
            } else {
                etBuy.setText("");
            }
            float f2 = otherSubjoinInfoBean.getSalePrice();
            if (f2 != -1) {
                if (String.valueOf(f2).startsWith("0")) {
                    String priceSale = decimalFormat.format(f2);
                    etSale.setText(0 + priceSale);
                } else {
                    String priceSale = decimalFormat.format(f2);
                    etSale.setText(priceSale);
                }
            } else {
                etSale.setText("");
            }
            //其他信息
            String otherInfo = otherSubjoinInfoBean.getAssessmentDes();
            if (otherInfo != null && otherInfo.trim().length() > 0)
                etOtherInfo.setText(otherInfo);
            //外观检查
            int appearanceCheckIndex = otherSubjoinInfoBean.getAppearanceCheck();
            switch (appearanceCheckIndex) {
                case 0:
                    rbtnOut1.setChecked(false);
                    rbtnOut2.setChecked(false);
                    rbtnOut3.setChecked(false);
                    rbtnOut4.setChecked(false);
                    rbtnOut5.setChecked(false);
                    break;
                case 1:
                    rbtnOut1.setChecked(true);
                    break;
                case 2:
                    rbtnOut2.setChecked(true);
                    break;
                case 3:
                    rbtnOut3.setChecked(true);
                    break;
                case 4:
                    rbtnOut4.setChecked(true);
                    break;
                case 5:
                    rbtnOut5.setChecked(true);
                    break;
                default:
            }
            //事故检查
            int accidentCheckIndex = otherSubjoinInfoBean.getAccidentCheck();
            switch (accidentCheckIndex) {
                case 0:
                    rbtnAccident1.setChecked(false);
                    rbtnAccident2.setChecked(false);
                    rbtnAccident3.setChecked(false);
                    rbtnAccident4.setChecked(false);
                    rbtnAccident5.setChecked(false);
                    break;
                case 1:
                    rbtnAccident1.setChecked(true);
                    break;
                case 2:
                    rbtnAccident2.setChecked(true);
                    break;
                case 3:
                    rbtnAccident3.setChecked(true);
                    break;
                case 4:
                    rbtnAccident4.setChecked(true);
                    break;
                case 5:
                    rbtnAccident5.setChecked(true);
                    break;
                default:
            }
            //机舱检查
            int engineCheckIndex = otherSubjoinInfoBean.getEngineCheck();
            switch (engineCheckIndex) {
                case 0:
                    rbtnCabin1.setChecked(false);
                    rbtnCabin2.setChecked(false);
                    rbtnCabin3.setChecked(false);
                    rbtnCabin4.setChecked(false);
                    rbtnCabin5.setChecked(false);
                    break;
                case 1:
                    rbtnCabin1.setChecked(true);
                    break;
                case 2:
                    rbtnCabin2.setChecked(true);
                    break;
                case 3:
                    rbtnCabin3.setChecked(true);
                    break;
                case 4:
                    rbtnCabin4.setChecked(true);
                    break;
                case 5:
                    rbtnCabin5.setChecked(true);
                    break;
                default:
            }
            //内饰检查
            int decorateCheckIndex = otherSubjoinInfoBean.getDecorateCheck();
            switch (decorateCheckIndex) {
                case 0:
                    rbtnIn1.setChecked(false);
                    rbtnIn2.setChecked(false);
                    rbtnIn3.setChecked(false);
                    rbtnIn4.setChecked(false);
                    rbtnIn5.setChecked(false);
                    break;
                case 1:
                    rbtnIn1.setChecked(true);
                    break;
                case 2:
                    rbtnIn2.setChecked(true);
                    break;
                case 3:
                    rbtnIn3.setChecked(true);
                    break;
                case 4:
                    rbtnIn4.setChecked(true);
                    break;
                case 5:
                    rbtnIn5.setChecked(true);
                    break;
                default:
            }
            //底盘检查
            int chassisCheckIndex = otherSubjoinInfoBean.getChassisCheck();
            switch (chassisCheckIndex) {
                case 0:
                    rbtnChassis1.setChecked(false);
                    rbtnChassis2.setChecked(false);
                    rbtnChassis3.setChecked(false);
                    rbtnChassis4.setChecked(false);
                    rbtnChassis5.setChecked(false);
                    break;
                case 1:
                    rbtnChassis1.setChecked(true);
                    break;
                case 2:
                    rbtnChassis2.setChecked(true);
                    break;
                case 3:
                    rbtnChassis3.setChecked(true);
                    break;
                case 4:
                    rbtnChassis4.setChecked(true);
                    break;
                case 5:
                    rbtnChassis5.setChecked(true);
                    break;
                default:
            }
            //电气检查
            int electricalCheckIndex = otherSubjoinInfoBean.getElectricalCheck();
            switch (electricalCheckIndex) {
                case 0:
                    rbtnElectric1.setChecked(false);
                    rbtnElectric2.setChecked(false);
                    rbtnElectric3.setChecked(false);
                    rbtnElectric4.setChecked(false);
                    rbtnElectric5.setChecked(false);
                    break;
                case 1:
                    rbtnElectric1.setChecked(true);
                    break;
                case 2:
                    rbtnElectric2.setChecked(true);
                    break;
                case 3:
                    rbtnElectric3.setChecked(true);
                    break;
                case 4:
                    rbtnElectric4.setChecked(true);
                    break;
                case 5:
                    rbtnElectric5.setChecked(true);
                    break;
                default:
            }
            //火烧迹象： 0 未见火烧迹象、 1 存在火烧迹象、 2 经综合判断为火烧车，单选
            if (otherSubjoinInfoBean.getBurningMark() != -1) {//选中一项
                if (otherSubjoinInfoBean.getBurningMark() == 0) {
                    rbtnFireNo.setChecked(true);
                } else if (otherSubjoinInfoBean.getBurningMark() == 1) {
                    rbtnFireYes.setChecked(true);
                } else {
                    rbtnFire.setChecked(true);
                }
            } else {
                rbtnFireNo.setChecked(false);
                rbtnFireYes.setChecked(false);
                rbtnFire.setChecked(false);
            }
            //泡水迹象： 0 未见泡水迹象、 1 存在泡水迹象、 2 经综合判断为泡水车，单选
            if (otherSubjoinInfoBean.getWaterMark() != -1) {//选中一项
                if (otherSubjoinInfoBean.getWaterMark() == 0) {
                    rbtnWaterNo.setChecked(true);
                } else if (otherSubjoinInfoBean.getWaterMark() == 1) {
                    rbtnWaterYes.setChecked(true);
                } else {
                    rbtnWater.setChecked(true);
                }
            } else {
                rbtnWaterNo.setChecked(false);
                rbtnWaterYes.setChecked(false);
                rbtnWater.setChecked(false);
            }
            //市场保有量（ 1 较高， 2 一般， 3 较低），单选
            if (otherSubjoinInfoBean.getMarketOwnership() != 0) {//选中一项
                if (otherSubjoinInfoBean.getMarketOwnership() == 1) {
                    rbtnHoldingHigh.setChecked(true);
                } else if (otherSubjoinInfoBean.getMarketOwnership() == 2) {
                    rbtnHoldingGeneral.setChecked(true);
                } else {
                    rbtnHoldingLow.setChecked(true);
                }
            } else {
                rbtnHoldingHigh.setChecked(false);
                rbtnHoldingGeneral.setChecked(false);
                rbtnHoldingLow.setChecked(false);
            }
            //市场认可度（ 1 较高， 2 一般， 3 较低），单选
            if (otherSubjoinInfoBean.getMarketAcceptance() != 0) {//选中一项
                if (otherSubjoinInfoBean.getMarketAcceptance() == 1) {
                    rbtnAcceptHigh.setChecked(true);
                } else if (otherSubjoinInfoBean.getMarketAcceptance() == 2) {
                    rbtnAcceptGeneral.setChecked(true);
                } else {
                    rbtnAcceptLow.setChecked(true);
                }
            } else {
                rbtnAcceptHigh.setChecked(false);
                rbtnAcceptGeneral.setChecked(false);
                rbtnAcceptLow.setChecked(false);
            }
            //市场保值率（ 1 较高， 2 一般， 3 较低），单选
            if (otherSubjoinInfoBean.getMarketHedgeRatio() != 0) {//选中一项
                if (otherSubjoinInfoBean.getMarketHedgeRatio() == 1) {
                    rbtnHedgeRatioHigh.setChecked(true);
                } else if (otherSubjoinInfoBean.getMarketHedgeRatio() == 2) {
                    rbtnHedgeRatioGeneral.setChecked(true);
                } else {
                    rbtnHedgeRatioLow.setChecked(true);
                }
            } else {
                rbtnHedgeRatioHigh.setChecked(false);
                rbtnHedgeRatioGeneral.setChecked(false);
                rbtnHedgeRatioLow.setChecked(false);
            }
        }
    }

    /**
     * 根据车况检测里的缺陷选择是否有选中火烧、泡水相关的缺陷值动态显示红色提示信息
     */
    public void ifShowFireAndWaterInfo(TextView tvFireNotice, TextView tvWaterNotice) {
        /**
         * 火烧检查缺陷值选中数量
         */
        int sum = 0;
        /**
         * 泡水检查缺陷值选中数量
         */
        int sum1 = 0;
        LocalDetectionData data = DetectMainActivity.detectMainActivity.getLocalDetectionData();
        if (data != null) {
            List<CheckPositionItem> checkPositionList = data.getCheckPositionList();//所有检测方位的集合
            if (checkPositionList != null) {
                int checkPositionListLen = checkPositionList.size();
                if (checkPositionListLen > 0){
                    for (int i = 0; i < checkPositionListLen; i++) {//遍历每个检测方位
                        List<ImportantItem> importantList = checkPositionList.get(i).getImportantList();//每一个检测方位下的重点与非重点项
                        if (importantList != null) {
                            int importantListLen = importantList.size();
                            if (importantListLen > 0) {
                                for (int q = 0; q < importantListLen; q++) {
                                    ImportantItem importantItem = importantList.get(q);//重点或非重点
                                    if (importantItem != null) {
                                        String importantId = importantItem.getImportantId();
                                        List<CheckItem> checkItemList = importantItem.getCheckItemList();//一个方位下的所有检测项
                                        if (checkItemList != null) {
                                            int checkItemListLen = checkItemList.size();
                                            switch (importantId) {
                                                case "0"://非重点
                                                    if (checkItemListLen > 0) {
                                                        for (int w = 0; w < checkItemListLen; w++) {//遍历所有检测项
                                                            List<DefectType> defectTypeList = checkItemList.get(w).getDefectTypeList();//一个检测项下的所有缺陷值
                                                            if (defectTypeList != null) {
                                                                int defectTypeListLen = defectTypeList.size();
                                                                if (defectTypeListLen > 0) {
                                                                    for (int e = 0; e < defectTypeListLen; e++) {//遍历缺陷值
                                                                        DefectType defectType = defectTypeList.get(e);
                                                                        String defectTypeId = defectType.getDefectTypeId();
                                                                        switch (defectTypeId) {
                                                                            case "F06"://火烧检查
                                                                                List<DefectDetailItem> defectDetailList = defectType.getDefectDetailList();
                                                                                if (defectDetailList != null) {
                                                                                    int defectDetailListLen = defectDetailList.size();
                                                                                    for (int m = 0; m < defectDetailListLen; m++) {
                                                                                        if (sum >= 1)
                                                                                            break;
                                                                                        int num = defectDetailList.get(m).getStatus();
                                                                                        sum += num;
                                                                                    }
                                                                                    break;
                                                                                }
                                                                            case "F05"://泡水检查
                                                                                List<DefectDetailItem> defectDetailList1 = defectType.getDefectDetailList();
                                                                                if (defectDetailList1 != null) {
                                                                                    int defectDetailList1Len = defectDetailList1.size();
                                                                                    for (int m = 0; m < defectDetailList1Len; m++) {
                                                                                        if (sum1 >= 1)
                                                                                            break;
                                                                                        int num1 = defectDetailList1.get(m).getStatus();
                                                                                        sum1 += num1;
                                                                                    }
                                                                                    break;
                                                                                }
                                                                            default:
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    break;
                                                case "1"://重点
                                                    if (checkItemListLen > 0) {
                                                        for (int w = 0; w < checkItemListLen; w++) {
                                                            List<DefectType> defectTypeList = checkItemList.get(w).getDefectTypeList();//一个检测项下的所有缺陷值
                                                            if (defectTypeList != null) {
                                                                int defectTypeListLen = defectTypeList.size();
                                                                for (int e = 0; e < defectTypeListLen; e++) {//遍历缺陷值
                                                                    DefectType defectType = defectTypeList.get(e);
                                                                    String defectTypeId = defectType.getDefectTypeId();
                                                                    switch (defectTypeId) {
                                                                        case "F06"://火烧检查
                                                                            List<DefectDetailItem> defectDetailList = defectType.getDefectDetailList();
                                                                            if (defectDetailList != null) {
                                                                                int defectDetailListLen = defectDetailList.size();
                                                                                for (int m = 0; m < defectDetailListLen; m++) {
                                                                                    if (sum >= 1)
                                                                                        break;
                                                                                    int num = defectDetailList.get(m).getStatus();
                                                                                    sum += num;
                                                                                }
                                                                                break;
                                                                            }
                                                                        case "F05"://泡水检查
                                                                            List<DefectDetailItem> defectDetailList1 = defectType.getDefectDetailList();
                                                                            if (defectDetailList1 != null) {
                                                                                int defectDetailList1Len = defectDetailList1.size();
                                                                                for (int m = 0; m < defectDetailList1Len; m++) {
                                                                                    if (sum1 >= 1)
                                                                                        break;
                                                                                    int num1 = defectDetailList1.get(m).getStatus();
                                                                                    sum1 += num1;
                                                                                }
                                                                                break;
                                                                            }
                                                                        default:
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
            }
        }
        if (sum > 0) {//说明至少选中一项
            tvFireNotice.setVisibility(View.VISIBLE);
        } else {
            tvFireNotice.setVisibility(View.GONE);
        }
        if (sum1 > 0) {//说明至少选中一项
            tvWaterNotice.setVisibility(View.VISIBLE);
        } else {
            tvWaterNotice.setVisibility(View.GONE);
        }
    }

    /**
     * 点击修改按钮进来后回显数据
     */
    public void showData(TaskDetailModel taskDetailModel, EditText etBuy, EditText etSale, EditText etOtherInfo, RadioButton rbtnOut1,
                         RadioButton rbtnOut2, RadioButton rbtnOut3, RadioButton rbtnOut4, RadioButton rbtnOut5,
                         RadioButton rbtnAccident1, RadioButton rbtnAccident2, RadioButton rbtnAccident3, RadioButton rbtnAccident4, RadioButton rbtnAccident5,
                         RadioButton rbtnCabin1, RadioButton rbtnCabin2, RadioButton rbtnCabin3, RadioButton rbtnCabin4, RadioButton rbtnCabin5,
                         RadioButton rbtnIn1, RadioButton rbtnIn2, RadioButton rbtnIn3, RadioButton rbtnIn4, RadioButton rbtnIn5,
                         RadioButton rbtnChassis1, RadioButton rbtnChassis2, RadioButton rbtnChassis3, RadioButton rbtnChassis4, RadioButton rbtnChassis5,
                         RadioButton rbtnElectric1, RadioButton rbtnElectric2, RadioButton rbtnElectric3, RadioButton rbtnElectric4, RadioButton rbtnElectric5,
                         RadioButton rbtnFireNo, RadioButton rbtnFireYes, RadioButton rbtnFire, RadioButton rbtnWaterNo, RadioButton rbtnWaterYes, RadioButton rbtnWater,
                         RadioButton rbtnHoldingHigh, RadioButton rbtnHoldingGeneral, RadioButton rbtnHoldingLow,
                         RadioButton rbtnAcceptHigh, RadioButton rbtnAcceptGeneral, RadioButton rbtnAcceptLow,
                         RadioButton rbtnHedgeRatioHigh, RadioButton rbtnHedgeRatioGeneral, RadioButton rbtnHedgeRatioLow) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        float assessmentPrace = taskDetailModel.getBasic().getAssessmentPrace();//车商收车价
        if (assessmentPrace != -1) {
            if (String.valueOf(assessmentPrace).startsWith("0")) {
                String priceBuy = decimalFormat.format(assessmentPrace);
                etBuy.setText(0 + priceBuy);
            } else {
                String priceBuy = decimalFormat.format(assessmentPrace);
                etBuy.setText(priceBuy);
            }
        } else {
            etBuy.setText("");
        }
        float salePrice = taskDetailModel.getBasic().getSalePrice();//车商售车价
        if (salePrice != -1) {
            if (String.valueOf(salePrice).startsWith("0")) {
                String priceSale = decimalFormat.format(salePrice);
                etSale.setText(0 + priceSale);
            } else {
                String priceSale = decimalFormat.format(salePrice);
                etSale.setText(priceSale);
            }
        } else {
            etSale.setText("");
        }
        int appearanceCheck = taskDetailModel.getBasic().getAppearanceCheck();//外观检查
        switch (appearanceCheck) {
            case 0:
                rbtnOut1.setChecked(false);
                rbtnOut2.setChecked(false);
                rbtnOut3.setChecked(false);
                rbtnOut4.setChecked(false);
                rbtnOut5.setChecked(false);
                break;
            case 1:
                rbtnOut1.setChecked(true);
                break;
            case 2:
                rbtnOut2.setChecked(true);
                break;
            case 3:
                rbtnOut3.setChecked(true);
                break;
            case 4:
                rbtnOut4.setChecked(true);
                break;
            case 5:
                rbtnOut5.setChecked(true);
                break;
            default:
        }
        int accidentCheck = taskDetailModel.getBasic().getAccidentCheck();//事故检查
        switch (accidentCheck) {
            case 0:
                rbtnAccident1.setChecked(false);
                rbtnAccident2.setChecked(false);
                rbtnAccident3.setChecked(false);
                rbtnAccident4.setChecked(false);
                rbtnAccident5.setChecked(false);
                break;
            case 1:
                rbtnAccident1.setChecked(true);
                break;
            case 2:
                rbtnAccident2.setChecked(true);
                break;
            case 3:
                rbtnAccident3.setChecked(true);
                break;
            case 4:
                rbtnAccident4.setChecked(true);
                break;
            case 5:
                rbtnAccident5.setChecked(true);
                break;
            default:
        }
        int engineCheck = taskDetailModel.getBasic().getEngineCheck();//机舱检查
        switch (engineCheck) {
            case 0:
                rbtnCabin1.setChecked(false);
                rbtnCabin2.setChecked(false);
                rbtnCabin3.setChecked(false);
                rbtnCabin4.setChecked(false);
                rbtnCabin5.setChecked(false);
                break;
            case 1:
                rbtnCabin1.setChecked(true);
                break;
            case 2:
                rbtnCabin2.setChecked(true);
                break;
            case 3:
                rbtnCabin3.setChecked(true);
                break;
            case 4:
                rbtnCabin4.setChecked(true);
                break;
            case 5:
                rbtnCabin5.setChecked(true);
                break;
            default:
        }
        int decorateCheck = taskDetailModel.getBasic().getDecorateCheck();//内饰检查
        switch (decorateCheck) {
            case 0:
                rbtnIn1.setChecked(false);
                rbtnIn2.setChecked(false);
                rbtnIn3.setChecked(false);
                rbtnIn4.setChecked(false);
                rbtnIn5.setChecked(false);
                break;
            case 1:
                rbtnIn1.setChecked(true);
                break;
            case 2:
                rbtnIn2.setChecked(true);
                break;
            case 3:
                rbtnIn3.setChecked(true);
                break;
            case 4:
                rbtnIn4.setChecked(true);
                break;
            case 5:
                rbtnIn5.setChecked(true);
                break;
            default:
        }
        int chassisCheck = taskDetailModel.getBasic().getChassisCheck();//底盘检查
        switch (chassisCheck) {
            case 0:
                rbtnChassis1.setChecked(false);
                rbtnChassis2.setChecked(false);
                rbtnChassis3.setChecked(false);
                rbtnChassis4.setChecked(false);
                rbtnChassis5.setChecked(false);
                break;
            case 1:
                rbtnChassis1.setChecked(true);
                break;
            case 2:
                rbtnChassis2.setChecked(true);
                break;
            case 3:
                rbtnChassis3.setChecked(true);
                break;
            case 4:
                rbtnChassis4.setChecked(true);
                break;
            case 5:
                rbtnChassis5.setChecked(true);
                break;
            default:
        }
        int electricalCheck = taskDetailModel.getBasic().getElectricalCheck();//电气检查
        switch (electricalCheck) {
            case 0:
                rbtnElectric1.setChecked(false);
                rbtnElectric2.setChecked(false);
                rbtnElectric3.setChecked(false);
                rbtnElectric4.setChecked(false);
                rbtnElectric5.setChecked(false);
                break;
            case 1:
                rbtnElectric1.setChecked(true);
                break;
            case 2:
                rbtnElectric2.setChecked(true);
                break;
            case 3:
                rbtnElectric3.setChecked(true);
                break;
            case 4:
                rbtnElectric4.setChecked(true);
                break;
            case 5:
                rbtnElectric5.setChecked(true);
                break;
            default:
        }
        int burningMark = taskDetailModel.getBasic().getBurningMark();//火烧迹象
        if (burningMark != -1) {//选中一项
            if (burningMark == 0) {
                rbtnFireNo.setChecked(true);
            } else if (burningMark == 1) {
                rbtnFireYes.setChecked(true);
            } else {
                rbtnFire.setChecked(true);
            }
        }
        int waterMark = taskDetailModel.getBasic().getWaterMark();//泡水迹象
        if (waterMark != -1) {//选中一项
            if (waterMark == 0) {
                rbtnWaterNo.setChecked(true);
            } else if (waterMark == 1) {
                rbtnWaterYes.setChecked(true);
            } else {
                rbtnWater.setChecked(true);
            }
        }
        int marketOwnership = taskDetailModel.getBasic().getMarketOwnership();//市场保有量
        if (marketOwnership != 0) {//选中一项
            if (marketOwnership == 1) {
                rbtnHoldingHigh.setChecked(true);
            } else if (marketOwnership == 2) {
                rbtnHoldingGeneral.setChecked(true);
            } else {
                rbtnHoldingLow.setChecked(true);
            }
        }
        int marketAcceptance = taskDetailModel.getBasic().getMarketAcceptance();//市场认可度
        if (marketAcceptance != 0) {//选中一项
            if (marketAcceptance == 1) {
                rbtnAcceptHigh.setChecked(true);
            } else if (marketAcceptance == 2) {
                rbtnAcceptGeneral.setChecked(true);
            } else {
                rbtnAcceptLow.setChecked(true);
            }
        }
        int marketHedgeRatio = taskDetailModel.getBasic().getMarketHedgeRatio();//市场保值率
        if (marketHedgeRatio != 0) {//选中一项
            if (marketHedgeRatio == 1) {
                rbtnHedgeRatioHigh.setChecked(true);
            } else if (marketHedgeRatio == 2) {
                rbtnHedgeRatioGeneral.setChecked(true);
            } else {
                rbtnHedgeRatioLow.setChecked(true);
            }
        }
        String assessmentDes = taskDetailModel.getBasic().getAssessmentDes();//其他说明信息
        if (!TextUtils.isEmpty(assessmentDes))
            etOtherInfo.setText(assessmentDes);
    }
}