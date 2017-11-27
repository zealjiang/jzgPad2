package com.jcpt.jzg.padsystem.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.dialog.ShowMsgDialog;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.model.OtherSubjoinInformationModel;
import com.jcpt.jzg.padsystem.mvpview.IHistoryPrice;
import com.jcpt.jzg.padsystem.mvpview.ISubjoinInfoView;
import com.jcpt.jzg.padsystem.presenter.GetSubjoinInfoInitPresenter;
import com.jcpt.jzg.padsystem.presenter.HistoryPricePresenter;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.HistoryPriceActivity;
import com.jcpt.jzg.padsystem.ui.activity.ResultCheckedActivity;
import com.jcpt.jzg.padsystem.ui.activity.WebviewActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.HistoryPriceModel;
import com.jcpt.jzg.padsystem.vo.OtherSubjoinInfoBean;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.StarScoreItem;
import com.jcpt.jzg.padsystem.widget.nodoubleclick.NoDoubleOnclickListener;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 *  Created by 吴佳俊 on 2016/11/11.
 *  
 */
public class OtherSubjoinInformationFragment extends BaseFragment implements ISubjoinInfoView, TextView.OnEditorActionListener, IHistoryPrice {


    @BindView(R.id.tvHistoryPrice)
    TextView tvHistoryPrice;
    @BindView(R.id.etBuy)
    EditText etBuy;
    @BindView(R.id.llBuyPrice)
    LinearLayout llBuyPrice;
    @BindView(R.id.etSale)
    EditText etSale;
    @BindView(R.id.llSalePrice)
    LinearLayout llSalePrice;
    @BindView(R.id.rbtnOut5)
    RadioButton rbtnOut5;
    @BindView(R.id.rbtnOut4)
    RadioButton rbtnOut4;
    @BindView(R.id.rbtnOut3)
    RadioButton rbtnOut3;
    @BindView(R.id.rbtnOut2)
    RadioButton rbtnOut2;
    @BindView(R.id.rbtnOut1)
    RadioButton rbtnOut1;
    @BindView(R.id.rbtnAccident5)
    RadioButton rbtnAccident5;
    @BindView(R.id.rbtnAccident4)
    RadioButton rbtnAccident4;
    @BindView(R.id.rbtnAccident3)
    RadioButton rbtnAccident3;
    @BindView(R.id.rbtnAccident2)
    RadioButton rbtnAccident2;
    @BindView(R.id.rbtnAccident1)
    RadioButton rbtnAccident1;
    @BindView(R.id.rbtnCabin5)
    RadioButton rbtnCabin5;
    @BindView(R.id.rbtnCabin4)
    RadioButton rbtnCabin4;
    @BindView(R.id.rbtnCabin3)
    RadioButton rbtnCabin3;
    @BindView(R.id.rbtnCabin2)
    RadioButton rbtnCabin2;
    @BindView(R.id.rbtnCabin1)
    RadioButton rbtnCabin1;
    @BindView(R.id.rbtnIn5)
    RadioButton rbtnIn5;
    @BindView(R.id.rbtnIn4)
    RadioButton rbtnIn4;
    @BindView(R.id.rbtnIn3)
    RadioButton rbtnIn3;
    @BindView(R.id.rbtnIn2)
    RadioButton rbtnIn2;
    @BindView(R.id.rbtnIn1)
    RadioButton rbtnIn1;
    @BindView(R.id.rbtnChassis5)
    RadioButton rbtnChassis5;
    @BindView(R.id.rbtnChassis4)
    RadioButton rbtnChassis4;
    @BindView(R.id.rbtnChassis3)
    RadioButton rbtnChassis3;
    @BindView(R.id.rbtnChassis2)
    RadioButton rbtnChassis2;
    @BindView(R.id.rbtnChassis1)
    RadioButton rbtnChassis1;
    @BindView(R.id.rbtnElectric5)
    RadioButton rbtnElectric5;
    @BindView(R.id.rbtnElectric4)
    RadioButton rbtnElectric4;
    @BindView(R.id.rbtnElectric3)
    RadioButton rbtnElectric3;
    @BindView(R.id.rbtnElectric2)
    RadioButton rbtnElectric2;
    @BindView(R.id.rbtnElectric1)
    RadioButton rbtnElectric1;
    @BindView(R.id.rbtnFireNo)
    RadioButton rbtnFireNo;
    @BindView(R.id.rbtnFireYes)
    RadioButton rbtnFireYes;
    @BindView(R.id.tvFireNotice)
    TextView tvFireNotice;
    @BindView(R.id.rbtnWaterNo)
    RadioButton rbtnWaterNo;
    @BindView(R.id.rbtnWaterYes)
    RadioButton rbtnWaterYes;
    @BindView(R.id.tvWaterNotice)
    TextView tvWaterNotice;
    @BindView(R.id.rbtnAcceptHigh)
    RadioButton rbtnAcceptHigh;
    @BindView(R.id.rbtnAcceptGeneral)
    RadioButton rbtnAcceptGeneral;
    @BindView(R.id.rbtnAcceptLow)
    RadioButton rbtnAcceptLow;
    @BindView(R.id.rbtnHedgeRatioHigh)
    RadioButton rbtnHedgeRatioHigh;
    @BindView(R.id.rbtnHedgeRatioGeneral)
    RadioButton rbtnHedgeRatioGeneral;
    @BindView(R.id.rbtnHedgeRatioLow)
    RadioButton rbtnHedgeRatioLow;
    @BindView(R.id.etOtherInfo)
    EditText etOtherInfo;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.rgOut)
    RadioGroup rgOut;
    @BindView(R.id.rgAccident)
    RadioGroup rgAccident;
    @BindView(R.id.rgCabin)
    RadioGroup rgCabin;
    @BindView(R.id.rgIn)
    RadioGroup rgIn;
    @BindView(R.id.rgChassis)
    RadioGroup rgChassis;
    @BindView(R.id.rgElectric)
    RadioGroup rgElectric;
    @BindView(R.id.rgFire)
    RadioGroup rgFire;
    @BindView(R.id.rgWater)
    RadioGroup rgWater;
    @BindView(R.id.rgRecognition)
    RadioGroup rgRecognition;
    @BindView(R.id.rgHedgeRatio)
    RadioGroup rgHedgeRatio;
    @BindView(R.id.llOut)
    LinearLayout llOut;
    @BindView(R.id.llAccident)
    LinearLayout llAccident;
    @BindView(R.id.llCabin)
    LinearLayout llCabin;
    @BindView(R.id.llIn)
    LinearLayout llIn;
    @BindView(R.id.llChassis)
    LinearLayout llChassis;
    @BindView(R.id.llElectric)
    LinearLayout llElectric;
    @BindView(R.id.rbtnHoldingHigh)
    RadioButton rbtnHoldingHigh;
    @BindView(R.id.rbtnHoldingGeneral)
    RadioButton rbtnHoldingGeneral;
    @BindView(R.id.rbtnHoldingLow)
    RadioButton rbtnHoldingLow;
    @BindView(R.id.rgHoldingvolume)
    RadioGroup rgHoldingvolume;
    @BindView(R.id.rbtnFire)
    RadioButton rbtnFire;
    @BindView(R.id.rbtnWater)
    RadioButton rbtnWater;
    @BindView(R.id.llSubjoinInfo)
    LinearLayout llSubjoinInfo;
    @BindView(R.id.ll_fire)
    LinearLayout llFire;
    @BindView(R.id.ll_water)
    LinearLayout llWater;
    private View imgStarNormalOut5;
    private View imgStarCheckedOut5;
    private View imgStarCheckedOut4;
    private View imgStarNormalOut4;
    private View imgStarCheckedOut3;
    private View imgStarNormalOut3;
    private View imgStarNormalOut2;
    private View imgStarCheckedOut2;
    private View imgStarNormalOut1;
    private View imgStarCheckedOut1;
    private View imgStarNormalAccident5;
    private View imgStarCheckedAccident5;
    private View imgStarNormalAccident4;
    private View imgStarCheckedAccident4;
    private View imgStarNormalAccident3;
    private View imgStarCheckedAccident3;
    private View imgStarNormalAccident2;
    private View imgStarCheckedAccident2;
    private View imgStarNormalAccident1;
    private View imgStarCheckedAccident1;
    private View imgStarNormalCabin5;
    private View imgStarCheckedCabin5;
    private View imgStarNormalCabin4;
    private View imgStarCheckedCabin4;
    private View imgStarNormalCabin3;
    private View imgStarCheckedCabin3;
    private View imgStarNormalCabin2;
    private View imgStarCheckedCabin2;
    private View imgStarNormalCabin1;
    private View imgStarCheckedCabin1;
    private View imgStarNormalIn5;
    private View imgStarCheckedIn5;
    private View imgStarNormalIn4;
    private View imgStarCheckedIn4;
    private View imgStarNormalIn3;
    private View imgStarCheckedIn3;
    private View imgStarNormalIn2;
    private View imgStarCheckedIn2;
    private View imgStarNormalIn1;
    private View imgStarCheckedIn1;
    private View imgStarNormalChassis5;
    private View imgStarCheckedChassis5;
    private View imgStarNormalChassis4;
    private View imgStarCheckedChassis4;
    private View imgStarNormalChassis3;
    private View imgStarCheckedChassis3;
    private View imgStarNormalChassis2;
    private View imgStarCheckedChassis2;
    private View imgStarNormalChassis1;
    private View imgStarCheckedChassis1;
    private View imgStarNormalElectric5;
    private View imgStarCheckedElectric5;
    private View imgStarNormalElectric4;
    private View imgStarCheckedElectric4;
    private View imgStarNormalElectric3;
    private View imgStarCheckedElectric3;
    private View imgStarNormalElectric2;
    private View imgStarCheckedElectric2;
    private View imgStarNormalElectric1;
    private View imgStarCheckedElectric1;

    /**
     * 需要隐藏的星级检查项个数
     */
    private int hideSize;
    private String taskId;
    private OtherSubjoinInformationModel otherSubjoinInformationModel;
    private OtherSubjoinInfoBean otherSubjoinInfoBean;
    private String planId;
    private String taskid;

    private HistoryPricePresenter historyPricePresenter;
    private String vin;
    private int isAuto;

    /**
     * 附加信息是否修改过的标记
     */
    private boolean isFix;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //显示dialog
        showDialog();
        View view = inflater.inflate(R.layout.fragment_other_subjoin_information, container, false);
        initView(view);
        ButterKnife.bind(this, view);


        TaskDetailModel taskDetailModel = DetectMainActivity.detectMainActivity.getTaskDetailModel();
        if (taskDetailModel != null) {
            //回显数据
            otherSubjoinInformationModel.showData(taskDetailModel, etBuy, etSale, etOtherInfo, rbtnOut1,
                    rbtnOut2, rbtnOut3, rbtnOut4, rbtnOut5,
                    rbtnAccident1, rbtnAccident2, rbtnAccident3, rbtnAccident4, rbtnAccident5,
                    rbtnCabin1, rbtnCabin2, rbtnCabin3, rbtnCabin4, rbtnCabin5,
                    rbtnIn1, rbtnIn2, rbtnIn3, rbtnIn4, rbtnIn5,
                    rbtnChassis1, rbtnChassis2, rbtnChassis3, rbtnChassis4, rbtnChassis5,
                    rbtnElectric1, rbtnElectric2, rbtnElectric3, rbtnElectric4, rbtnElectric5,
                    rbtnFireNo, rbtnFireYes, rbtnFire, rbtnWaterNo, rbtnWaterYes, rbtnWater,
                    rbtnHoldingHigh, rbtnHoldingGeneral, rbtnHoldingLow,
                    rbtnAcceptHigh, rbtnAcceptGeneral, rbtnAcceptLow,
                    rbtnHedgeRatioHigh, rbtnHedgeRatioGeneral, rbtnHedgeRatioLow);

            saveData();
        }

        //初始化页面
        checkDetectionPlan();
        initListener();

        //请求网络--历史评估价格
        SubmitModel submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();
        if(submitModel==null){
            getActivity().finish();
            return view;
        }
        vin = submitModel.getVin();
        requestHistoryPrice();
        return view;
    }

    @Override
    protected void setView() {
        init();
    }

    @Override
    protected void initData() {
        taskId = ((DetectMainActivity) getActivity()).getTaskid();
        otherSubjoinInformationModel = new OtherSubjoinInformationModel(context, taskId);
        //查询此条缓存是否存在
        boolean isExist = DBManager.getInstance().isExist(Constants.DATA_TYPE_SUBJOININFO, taskId, PadSysApp.getUser().getUserId());
        DBManager.getInstance().closeDB();
        if (isExist) {//缓存存在
            String json = "";
            //根据taskId、userId、Constants.DATA_TYPE_SUBJOININFO从本地数据库中获取附加信息缓存数据
            List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_SUBJOININFO, PadSysApp.getUser().getUserId());
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (list.get(i).getDataType().equals(Constants.DATA_TYPE_SUBJOININFO)) {
                        json = list.get(i).getJson();
                        if (!TextUtils.isEmpty(json)) {
                            otherSubjoinInfoBean = new Gson().fromJson(json, OtherSubjoinInfoBean.class);
                        }
                    }
                }
            }
        }
    }

    public void init() {
        //从本地取出缓存数据并显示
        otherSubjoinInformationModel.getSaveDataFromDB(otherSubjoinInfoBean, etBuy, etSale, etOtherInfo, rbtnOut1,
                rbtnOut2, rbtnOut3, rbtnOut4, rbtnOut5,
                rbtnAccident1, rbtnAccident2, rbtnAccident3, rbtnAccident4, rbtnAccident5,
                rbtnCabin1, rbtnCabin2, rbtnCabin3, rbtnCabin4, rbtnCabin5,
                rbtnIn1, rbtnIn2, rbtnIn3, rbtnIn4, rbtnIn5,
                rbtnChassis1, rbtnChassis2, rbtnChassis3, rbtnChassis4, rbtnChassis5,
                rbtnElectric1, rbtnElectric2, rbtnElectric3, rbtnElectric4, rbtnElectric5,
                rbtnFireNo, rbtnFireYes, rbtnFire, rbtnWaterNo, rbtnWaterYes, rbtnWater,
                rbtnHoldingHigh, rbtnHoldingGeneral, rbtnHoldingLow,
                rbtnAcceptHigh, rbtnAcceptGeneral, rbtnAcceptLow,
                rbtnHedgeRatioHigh, rbtnHedgeRatioGeneral, rbtnHedgeRatioLow);
        //展示被选中的radioButton对应的星星
        showIfChildChecked();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        //如果是EditText点击则重新获取焦点
        UIUtils uiUtils = new UIUtils();
        uiUtils.setupUI(rootView);
        return rootView;
    }

    public void setVin(String vin) {
        if (!TextUtils.isEmpty(this.vin) && !this.vin.equals(vin)) {
            this.vin = vin;
            requestHistoryPrice();
        }
    }

    /**
     * 请求历史价格
     *
     * @author zealjiang
     * @time 2017/2/16 10:44
     */
    private void requestHistoryPrice() {
        if (TextUtils.isEmpty(vin)) {
            //MyToast.showShort("VIN不可为空");
        } else {
            if (!PadSysApp.networkAvailable) {
                MyToast.showShort("没有网络");
                return;
            }
            historyPricePresenter = new HistoryPricePresenter(this);
            historyPricePresenter.getHistoryPriceCount(vin);
        }
    }

    private void checkDetectionPlan() {
        if(DetectMainActivity.detectMainActivity.getTaskItem()==null){
            return;
        }
        planId = DetectMainActivity.detectMainActivity.getTaskItem().getPlanId();
        taskid = DetectMainActivity.detectMainActivity.getTaskid();
        if (!TextUtils.isEmpty(planId)) {
            if (PadSysApp.wrapper != null && PadSysApp.wrapper.getPlanId().equals(planId)) {//内存中如果存在对应planid的检测方案，则直接用
                ifShowCheckedItem(PadSysApp.wrapper);
            } else {
                boolean isExist = DBManager.getInstance().isPlanExist(planId, Constants.DATA_TYPE_PLAN);//内存中不存在，查数据库中是否有检测方案
                if (isExist) {//数据库中存在,直接用
                    String json = DBManager.getInstance().queryLocalPlan(planId, Constants.DATA_TYPE_PLAN);
                    DetectionWrapper wrapper = new Gson().fromJson(json, DetectionWrapper.class);
                    if (wrapper != null) {
                        PadSysApp.wrapper = wrapper;
                        ifShowCheckedItem(wrapper);
                    }
                } else {
                    //数据库不存在，则联网请求
                    if (PadSysApp.networkAvailable) {
                        GetSubjoinInfoInitPresenter presenter = new GetSubjoinInfoInitPresenter(this);
                        presenter.getConfigureByTaskId(taskid);
                    } else {
                        //隐藏dialog
                        dismissDialog();
                        ShowMsgDialog.showMaterialDialogNoBtn(context, "提示", "网络不可用，初始化页面未完成，请检查网络后重试");
                    }
                }
            }
        } else {
            //如果PlanId为空，联网请求
            if (PadSysApp.networkAvailable) {
                GetSubjoinInfoInitPresenter presenter = new GetSubjoinInfoInitPresenter(this);
                presenter.getConfigureByTaskId(taskid);
            } else {
                //隐藏dialog
                dismissDialog();
                ShowMsgDialog.showMaterialDialogNoBtn(context, "提示", "网络不可用，初始化页面未完成，请检查网络后重试");
            }
        }
    }

    private void initListener() {
        /**
         * 精确到小数点后两位
         */
        Action1<TextViewTextChangeEvent> action = new Action1<TextViewTextChangeEvent>() {
            @Override
            public void call(TextViewTextChangeEvent event) {
                EditText priceView = (EditText) event.view();
                String price = event.text().toString();
                revert(priceView, price);
            }
        };
        RxTextView.textChangeEvents(etBuy)
                .debounce(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
        RxTextView.textChangeEvents(etSale)
                .debounce(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
        /**
         * 提交
         */
        btnSubmit.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                isFix = true;
                boolean checkDataCompleteAndSave = ((DetectMainActivity) OtherSubjoinInformationFragment.this.getActivity()).checkDataCompleteAndSave();
                if (checkDataCompleteAndSave) {
                    jump(ResultCheckedActivity.class);
                }
            }
        });


        //不能输入表情符
        etOtherInfo.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            String changedText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.e("onTextChanged string","start :"+start+"  before: "+before+"  count: "+count);
                //增加字符
                if (before == 0 && count > 0) {
                    changedText = s.subSequence(start, start + count).toString();
                } else {
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (changedText.equals("")) {
                    return;
                }
                //判断新输入的字符是否有汉字或表情符，如果是就删除新输入的，将输入光标定位在最后位置
                //LogUtil.e("afterTextChanged changedText",changedText);
                Pattern p = Pattern.compile(".*[\ud83c\udc00-\ud83c\udfff].*|.*[\ud83d\udc00-\ud83d\udfff].*|.*[\u2600-\u27ff].*");
                Matcher m = p.matcher(changedText);
                if (m.matches()) {
                    etOtherInfo.removeTextChangedListener(this);
                    etOtherInfo.setText(beforeText);
                    etOtherInfo.setSelection(etOtherInfo.getText().length());
                    MyToast.showShort("不可输入表情符");
                    etOtherInfo.addTextChangedListener(this);
                }
            }
        });
    }

    private void revert(EditText view, String price) {
        if (!TextUtils.isEmpty(price)) {
            if (price.startsWith(".")) {
                view.setText("");
                return;
            }
            if (price.startsWith("0") && price.length() > 1 && !".".equals(String.valueOf(price.charAt(1)))) {//以0开头，长度大于1，且第二个字符不是'.'，则置0
                view.setText("0");
                view.setSelection(view.getText().toString().length());
                return;
            }
            if (price.contains(".")) {
                String s1 = price.substring(price.indexOf(".") + 1, price.length());
                String s2 = price.substring(0, price.indexOf("."));
                if (s2.length() > 3) {//"."前面最多只可输入3个字符
                    int selectionEnd = view.getSelectionEnd();
                    String s = price.substring(0, selectionEnd - 1) + price.substring(selectionEnd, price.length());
                    view.setText(s);
                    view.setSelection(selectionEnd - 1);
                }
                if (s1.length() > 2) {//"."之后最多只可输入两个字符
                    int selectionEnd = view.getSelectionEnd();
                    String s = price.substring(0, selectionEnd - 1) + price.substring(selectionEnd, price.length());
                    view.setText(s);
                    view.setSelection(selectionEnd - 1);
                }
            } else {
                if (price.length() > 3) {
                    String result = price.substring(0, 3);
                    view.setText(result);
                    view.setSelection(result.length());
                }
            }
        }
        etOtherInfo.setOnEditorActionListener(this);
    }

    private void initView(View view) {
        /**
         * 外观检查
         */
        imgStarNormalOut5 = view.findViewById(R.id.imgStarNormalOut5);
        imgStarCheckedOut5 = view.findViewById(R.id.imgStarCheckedOut5);
        imgStarNormalOut4 = view.findViewById(R.id.imgStarNormalOut4);
        imgStarCheckedOut4 = view.findViewById(R.id.imgStarCheckedOut4);
        imgStarNormalOut3 = view.findViewById(R.id.imgStarNormalOut3);
        imgStarCheckedOut3 = view.findViewById(R.id.imgStarCheckedOut3);
        imgStarNormalOut2 = view.findViewById(R.id.imgStarNormalOut2);
        imgStarCheckedOut2 = view.findViewById(R.id.imgStarCheckedOut2);
        imgStarNormalOut1 = view.findViewById(R.id.imgStarNormalOut1);
        imgStarCheckedOut1 = view.findViewById(R.id.imgStarCheckedOut1);
        /**
         * 事故检查
         */
        imgStarNormalAccident5 = view.findViewById(R.id.imgStarNormalAccident5);
        imgStarCheckedAccident5 = view.findViewById(R.id.imgStarCheckedAccident5);
        imgStarNormalAccident4 = view.findViewById(R.id.imgStarNormalAccident4);
        imgStarCheckedAccident4 = view.findViewById(R.id.imgStarCheckedAccident4);
        imgStarNormalAccident3 = view.findViewById(R.id.imgStarNormalAccident3);
        imgStarCheckedAccident3 = view.findViewById(R.id.imgStarCheckedAccident3);
        imgStarNormalAccident2 = view.findViewById(R.id.imgStarNormalAccident2);
        imgStarCheckedAccident2 = view.findViewById(R.id.imgStarCheckedAccident2);
        imgStarNormalAccident1 = view.findViewById(R.id.imgStarNormalAccident1);
        imgStarCheckedAccident1 = view.findViewById(R.id.imgStarCheckedAccident1);
        /**
         * 机舱检查
         */
        imgStarNormalCabin5 = view.findViewById(R.id.imgStarNormalCabin5);
        imgStarCheckedCabin5 = view.findViewById(R.id.imgStarCheckedCabin5);
        imgStarNormalCabin4 = view.findViewById(R.id.imgStarNormalCabin4);
        imgStarCheckedCabin4 = view.findViewById(R.id.imgStarCheckedCabin4);
        imgStarNormalCabin3 = view.findViewById(R.id.imgStarNormalCabin3);
        imgStarCheckedCabin3 = view.findViewById(R.id.imgStarCheckedCabin3);
        imgStarNormalCabin2 = view.findViewById(R.id.imgStarNormalCabin2);
        imgStarCheckedCabin2 = view.findViewById(R.id.imgStarCheckedCabin2);
        imgStarNormalCabin1 = view.findViewById(R.id.imgStarNormalCabin1);
        imgStarCheckedCabin1 = view.findViewById(R.id.imgStarCheckedCabin1);
        /**
         * 内饰检查
         */
        imgStarNormalIn5 = view.findViewById(R.id.imgStarNormalIn5);
        imgStarCheckedIn5 = view.findViewById(R.id.imgStarCheckedIn5);
        imgStarNormalIn4 = view.findViewById(R.id.imgStarNormalIn4);
        imgStarCheckedIn4 = view.findViewById(R.id.imgStarCheckedIn4);
        imgStarNormalIn3 = view.findViewById(R.id.imgStarNormalIn3);
        imgStarCheckedIn3 = view.findViewById(R.id.imgStarCheckedIn3);
        imgStarNormalIn2 = view.findViewById(R.id.imgStarNormalIn2);
        imgStarCheckedIn2 = view.findViewById(R.id.imgStarCheckedIn2);
        imgStarNormalIn1 = view.findViewById(R.id.imgStarNormalIn1);
        imgStarCheckedIn1 = view.findViewById(R.id.imgStarCheckedIn1);
        /**
         * 底盘检查
         */
        imgStarNormalChassis5 = view.findViewById(R.id.imgStarNormalChassis5);
        imgStarCheckedChassis5 = view.findViewById(R.id.imgStarCheckedChassis5);
        imgStarNormalChassis4 = view.findViewById(R.id.imgStarNormalChassis4);
        imgStarCheckedChassis4 = view.findViewById(R.id.imgStarCheckedChassis4);
        imgStarNormalChassis3 = view.findViewById(R.id.imgStarNormalChassis3);
        imgStarCheckedChassis3 = view.findViewById(R.id.imgStarCheckedChassis3);
        imgStarNormalChassis2 = view.findViewById(R.id.imgStarNormalChassis2);
        imgStarCheckedChassis2 = view.findViewById(R.id.imgStarCheckedChassis2);
        imgStarNormalChassis1 = view.findViewById(R.id.imgStarNormalChassis1);
        imgStarCheckedChassis1 = view.findViewById(R.id.imgStarCheckedChassis1);
        /**
         * 电气检查
         */
        imgStarNormalElectric5 = view.findViewById(R.id.imgStarNormalElectric5);
        imgStarCheckedElectric5 = view.findViewById(R.id.imgStarCheckedElectric5);
        imgStarNormalElectric4 = view.findViewById(R.id.imgStarNormalElectric4);
        imgStarCheckedElectric4 = view.findViewById(R.id.imgStarCheckedElectric4);
        imgStarNormalElectric3 = view.findViewById(R.id.imgStarNormalElectric3);
        imgStarCheckedElectric3 = view.findViewById(R.id.imgStarCheckedElectric3);
        imgStarNormalElectric2 = view.findViewById(R.id.imgStarNormalElectric2);
        imgStarCheckedElectric2 = view.findViewById(R.id.imgStarCheckedElectric2);
        imgStarNormalElectric1 = view.findViewById(R.id.imgStarNormalElectric1);
        imgStarCheckedElectric1 = view.findViewById(R.id.imgStarCheckedElectric1);

    }


    @OnClick({R.id.etBuy, R.id.etSale, R.id.etOtherInfo, R.id.rbtnOut5, R.id.rbtnOut4, R.id.rbtnOut3, R.id.rbtnOut2, R.id.rbtnOut1, R.id.rbtnAccident5, R.id.rbtnAccident4, R.id.rbtnAccident3,
            R.id.rbtnAccident2, R.id.rbtnAccident1, R.id.rbtnCabin5, R.id.rbtnCabin4, R.id.rbtnCabin3, R.id.rbtnCabin2, R.id.rbtnCabin1, R.id.rbtnIn5, R.id.rbtnIn4, R.id.rbtnIn3,
            R.id.rbtnIn2, R.id.rbtnIn1, R.id.rbtnChassis5, R.id.rbtnChassis4, R.id.rbtnChassis3, R.id.rbtnChassis2, R.id.rbtnChassis1, R.id.rbtnElectric5, R.id.rbtnElectric4,
            R.id.rbtnElectric3, R.id.rbtnElectric2, R.id.rbtnElectric1, R.id.rbtnFireNo, R.id.rbtnFireYes, R.id.rbtnFire,
            R.id.rbtnWaterNo, R.id.rbtnWaterYes, R.id.rbtnWater, R.id.rbtnHoldingHigh, R.id.rbtnHoldingGeneral, R.id.rbtnHoldingLow, R.id.rbtnAcceptHigh, R.id.rbtnAcceptGeneral,
            R.id.rbtnAcceptLow, R.id.rbtnHedgeRatioHigh, R.id.rbtnHedgeRatioGeneral, R.id.rbtnHedgeRatioLow, R.id.tvHistoryPrice,R.id.tvNewCarPreferentialPrice})
    public void onClick(View view) {
        //只要点击就认为是修改
        isFix = true;
        switch (view.getId()) {
            //在点击下一项的时候，让其下一个EditText先获取焦点才能跳到下一项（因为点击别处的时候，已经把下一项的焦点置为false了）
            case R.id.etBuy:
                etSale.setFocusableInTouchMode(true);
                etSale.setFocusable(true);
                etOtherInfo.setFocusableInTouchMode(true);
                etOtherInfo.setFocusable(true);
                break;
            case R.id.etSale:
                etOtherInfo.setFocusableInTouchMode(true);
                etOtherInfo.setFocusable(true);
                break;
            case R.id.etOtherInfo:
                break;
            case R.id.rbtnOut5:
                resetStarts();
                if (rbtnOut5.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedOut5(imgStarNormalOut5, imgStarCheckedOut5);
                }
                break;
            case R.id.rbtnOut4:
                resetStarts();
                if (rbtnOut4.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedOut4(imgStarNormalOut4, imgStarCheckedOut4);
                }
                break;
            case R.id.rbtnOut3:
                resetStarts();
                if (rbtnOut3.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedOut3(imgStarNormalOut3, imgStarCheckedOut3);
                }
                break;
            case R.id.rbtnOut2:
                resetStarts();
                if (rbtnOut2.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedOut2(imgStarNormalOut2, imgStarCheckedOut2);
                }
                break;
            case R.id.rbtnOut1:
                resetStarts();
                if (rbtnOut1.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedOut1(imgStarNormalOut1, imgStarCheckedOut1);
                }
                break;
            case R.id.rbtnAccident5:
                resetStarts1();
                if (rbtnAccident5.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedAccident5(imgStarNormalAccident5, imgStarCheckedAccident5);
                }
                break;
            case R.id.rbtnAccident4:
                resetStarts1();
                if (rbtnAccident4.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedAccident4(imgStarNormalAccident4, imgStarCheckedAccident4);
                }
                break;
            case R.id.rbtnAccident3:
                resetStarts1();
                if (rbtnAccident3.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedAccident3(imgStarNormalAccident3, imgStarCheckedAccident3);
                }
                break;
            case R.id.rbtnAccident2:
                resetStarts1();
                if (rbtnAccident2.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedAccident2(imgStarNormalAccident2, imgStarCheckedAccident2);
                }
                break;
            case R.id.rbtnAccident1:
                resetStarts1();
                if (rbtnAccident1.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedAccident1(imgStarNormalAccident1, imgStarCheckedAccident1);
                }
                break;
            case R.id.rbtnCabin5:
                resetStarts2();
                if (rbtnCabin5.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedCabin5(imgStarNormalCabin5, imgStarCheckedCabin5);
                }
                break;
            case R.id.rbtnCabin4:
                resetStarts2();
                if (rbtnCabin4.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedCabin4(imgStarNormalCabin4, imgStarCheckedCabin4);
                }
                break;
            case R.id.rbtnCabin3:
                resetStarts2();
                if (rbtnCabin3.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedCabin3(imgStarNormalCabin3, imgStarCheckedCabin3);
                }
                break;
            case R.id.rbtnCabin2:
                resetStarts2();
                if (rbtnCabin2.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedCabin2(imgStarNormalCabin2, imgStarCheckedCabin2);
                }
                break;
            case R.id.rbtnCabin1:
                resetStarts2();
                if (rbtnCabin1.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedCabin1(imgStarNormalCabin1, imgStarCheckedCabin1);
                }
                break;
            case R.id.rbtnIn5:
                resetStarts3();
                if (rbtnIn5.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedIn5(imgStarNormalIn5, imgStarCheckedIn5);
                }
                break;
            case R.id.rbtnIn4:
                resetStarts3();
                if (rbtnIn4.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedIn4(imgStarNormalIn4, imgStarCheckedIn4);
                }
                break;
            case R.id.rbtnIn3:
                resetStarts3();
                if (rbtnIn3.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedIn3(imgStarNormalIn3, imgStarCheckedIn3);
                }
                break;
            case R.id.rbtnIn2:
                resetStarts3();
                if (rbtnIn2.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedIn2(imgStarNormalIn2, imgStarCheckedIn2);
                }
                break;
            case R.id.rbtnIn1:
                resetStarts3();
                if (rbtnIn1.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedIn1(imgStarNormalIn1, imgStarCheckedIn1);
                }
                break;
            case R.id.rbtnChassis5:
                resetStarts4();
                if (rbtnChassis5.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedChassis5(imgStarNormalChassis5, imgStarCheckedChassis5);
                }
                break;
            case R.id.rbtnChassis4:
                resetStarts4();
                if (rbtnChassis4.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedChassis4(imgStarNormalChassis4, imgStarCheckedChassis4);
                }
                break;
            case R.id.rbtnChassis3:
                resetStarts4();
                if (rbtnChassis3.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedChassis3(imgStarNormalChassis3, imgStarCheckedChassis3);
                }
                break;
            case R.id.rbtnChassis2:
                resetStarts4();
                if (rbtnChassis2.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedChassis2(imgStarNormalChassis2, imgStarCheckedChassis2);
                }
                break;
            case R.id.rbtnChassis1:
                resetStarts4();
                if (rbtnChassis1.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedChassis1(imgStarNormalChassis1, imgStarCheckedChassis1);
                }
                break;
            case R.id.rbtnElectric5:
                resetStarts5();
                if (rbtnElectric5.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedElectric5(imgStarNormalElectric5, imgStarCheckedElectric5);
                }
                break;
            case R.id.rbtnElectric4:
                resetStarts5();
                if (rbtnElectric4.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedElectric4(imgStarNormalElectric4, imgStarCheckedElectric4);
                }
                break;
            case R.id.rbtnElectric3:
                resetStarts5();
                if (rbtnElectric3.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedElectric3(imgStarNormalElectric3, imgStarCheckedElectric3);
                }
                break;
            case R.id.rbtnElectric2:
                resetStarts5();
                if (rbtnElectric2.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedElectric2(imgStarNormalElectric2, imgStarCheckedElectric2);
                }
                break;
            case R.id.rbtnElectric1:
                resetStarts5();
                if (rbtnElectric1.isChecked()) {
                    otherSubjoinInformationModel.showImgStarCheckedElectric1(imgStarNormalElectric1, imgStarCheckedElectric1);
                }
                break;
            case R.id.tvHistoryPrice:
                if (TextUtils.isEmpty(vin)) {
                    MyToast.showShort("VIN不可为空");
                    return;
                }
                if(!PadSysApp.networkAvailable){
                    MyToast.showShort("没有网络");
                    return;
                }
                Intent intent = new Intent(this.getContext(), HistoryPriceActivity.class);
                intent.putExtra("vin", vin);
                startActivity(intent);
                break;
            case R.id.tvNewCarPreferentialPrice:
                if(!PadSysApp.networkAvailable){
                    MyToast.showShort("没有网络");
                    return;
                }
                SubmitModel submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();
                int modelID = submitModel.getModelID();
                int userId = PadSysApp.getUser().getUserId();
                int cityId = PadSysApp.getUser().getCityId();
                int isApp = 1;
                //沙河地址
//                String url = "https://jiancePGS.sandbox.guchewang.com/Report/NewCarPrice.aspx?" + "modelid=" + modelID + "&userid=" + userId + "&cityid=" + cityId + "&isapp=" + isApp;
                //正式地址
                String url = "https://jiance.jingzhengu.com/Report/NewCarPrice.aspx?" + "modelid=" + modelID + "&userid=" + userId + "&cityid=" + cityId + "&isapp=" + isApp;
                Intent intent1 = new Intent(getActivity(), WebviewActivity.class);
                intent1.putExtra("url",url);
                intent1.putExtra("title", "查看新车优惠价");
                intent1.putExtra("leftRMargin", 10);
                startActivity(intent1);
                break;
            //提交的点击事件单独写在initListener()里了-请毋在这重复写
//            case R.id.btnSubmit:
//                ((DetectMainActivity) OtherSubjoinInformationFragment.this.getActivity()).checkAndSubmit();
//                break;
        }
    }

    /**
     * 检查不可为空项是否都已填写并保存
     *
     * @return
     */
    public boolean checkAndSaveData() {
        //判断所有选项是否为空
        boolean ifAllNotEmpty = otherSubjoinInformationModel.ifAllNotEmpty(llOut, rgOut, llAccident, rgAccident, llCabin, rgCabin,
                llIn, rgIn, llChassis, rgChassis, llElectric, rgElectric,
                rgFire, rgWater, rgHoldingvolume, rgRecognition, rgHedgeRatio,
                llBuyPrice, etBuy, llSalePrice, etSale, hideSize, isAuto,llFire, llWater);
        if (ifAllNotEmpty == false) {
            return false;
        }

        //保存数据到本地数据库&&保存数据到SubmitModel
        saveData();
        return true;
    }


    /**
     * 重置电气检查星星的显示，即未选中状态下显示灰色
     */
    private void resetStarts5() {
        imgStarNormalElectric5.setVisibility(View.VISIBLE);
        imgStarCheckedElectric5.setVisibility(View.GONE);
        imgStarNormalElectric4.setVisibility(View.VISIBLE);
        imgStarCheckedElectric4.setVisibility(View.GONE);
        imgStarNormalElectric3.setVisibility(View.VISIBLE);
        imgStarCheckedElectric3.setVisibility(View.GONE);
        imgStarNormalElectric2.setVisibility(View.VISIBLE);
        imgStarCheckedElectric2.setVisibility(View.GONE);
        imgStarNormalElectric1.setVisibility(View.VISIBLE);
        imgStarCheckedElectric1.setVisibility(View.GONE);

    }

    /**
     * 重置底盘检查星星的显示，即未选中状态下显示灰色
     */
    private void resetStarts4() {
        imgStarNormalChassis5.setVisibility(View.VISIBLE);
        imgStarCheckedChassis5.setVisibility(View.GONE);
        imgStarNormalChassis4.setVisibility(View.VISIBLE);
        imgStarCheckedChassis4.setVisibility(View.GONE);
        imgStarNormalChassis3.setVisibility(View.VISIBLE);
        imgStarCheckedChassis3.setVisibility(View.GONE);
        imgStarNormalChassis2.setVisibility(View.VISIBLE);
        imgStarCheckedChassis2.setVisibility(View.GONE);
        imgStarNormalChassis1.setVisibility(View.VISIBLE);
        imgStarCheckedChassis1.setVisibility(View.GONE);
    }

    /**
     * 重置内饰检查星星的显示，即未选中状态下显示灰色
     */
    private void resetStarts3() {
        imgStarNormalIn5.setVisibility(View.VISIBLE);
        imgStarCheckedIn5.setVisibility(View.GONE);
        imgStarNormalIn4.setVisibility(View.VISIBLE);
        imgStarCheckedIn4.setVisibility(View.GONE);
        imgStarNormalIn3.setVisibility(View.VISIBLE);
        imgStarCheckedIn3.setVisibility(View.GONE);
        imgStarNormalIn2.setVisibility(View.VISIBLE);
        imgStarCheckedIn2.setVisibility(View.GONE);
        imgStarNormalIn1.setVisibility(View.VISIBLE);
        imgStarCheckedIn1.setVisibility(View.GONE);
    }

    /**
     * 重置机舱检查星星的显示，即未选中状态下显示灰色
     */
    private void resetStarts2() {
        imgStarNormalCabin5.setVisibility(View.VISIBLE);
        imgStarCheckedCabin5.setVisibility(View.GONE);
        imgStarNormalCabin4.setVisibility(View.VISIBLE);
        imgStarCheckedCabin4.setVisibility(View.GONE);
        imgStarNormalCabin3.setVisibility(View.VISIBLE);
        imgStarCheckedCabin3.setVisibility(View.GONE);
        imgStarNormalCabin2.setVisibility(View.VISIBLE);
        imgStarCheckedCabin2.setVisibility(View.GONE);
        imgStarNormalCabin1.setVisibility(View.VISIBLE);
        imgStarCheckedCabin1.setVisibility(View.GONE);
    }

    /**
     * 重置事故检查星星的显示，即未选中状态下显示灰色
     */
    private void resetStarts1() {
        imgStarNormalAccident5.setVisibility(View.VISIBLE);
        imgStarCheckedAccident5.setVisibility(View.GONE);
        imgStarNormalAccident4.setVisibility(View.VISIBLE);
        imgStarCheckedAccident4.setVisibility(View.GONE);
        imgStarNormalAccident3.setVisibility(View.VISIBLE);
        imgStarCheckedAccident3.setVisibility(View.GONE);
        imgStarNormalAccident2.setVisibility(View.VISIBLE);
        imgStarCheckedAccident2.setVisibility(View.GONE);
        imgStarNormalAccident1.setVisibility(View.VISIBLE);
        imgStarCheckedAccident1.setVisibility(View.GONE);
    }

    /**
     * 重置外观检查星星的显示，即未选中状态下显示灰色
     */
    private void resetStarts() {
        imgStarNormalOut5.setVisibility(View.VISIBLE);
        imgStarCheckedOut5.setVisibility(View.GONE);
        imgStarNormalOut4.setVisibility(View.VISIBLE);
        imgStarCheckedOut4.setVisibility(View.GONE);
        imgStarNormalOut3.setVisibility(View.VISIBLE);
        imgStarCheckedOut3.setVisibility(View.GONE);
        imgStarNormalOut2.setVisibility(View.VISIBLE);
        imgStarCheckedOut2.setVisibility(View.GONE);
        imgStarNormalOut1.setVisibility(View.VISIBLE);
        imgStarCheckedOut1.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isFix) {
            saveData();
            isFix = false;
        }
    }

    /**
     * 火烧迹象、泡水迹象根据车况检测是否选中对应的缺陷项来动态显示提示信息
     */
    public void ifShowFireAndWaterInfo() {
        otherSubjoinInformationModel.ifShowFireAndWaterInfo(tvFireNotice, tvWaterNotice);
    }

    /**
     * 展示被选中的radioButton对应的星星
     */
    private void showIfChildChecked() {
        if (rbtnOut5.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedOut5(imgStarNormalOut5, imgStarCheckedOut5);
        }
        if (rbtnOut4.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedOut4(imgStarNormalOut4, imgStarCheckedOut4);
        }
        if (rbtnOut3.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedOut3(imgStarNormalOut3, imgStarCheckedOut3);
        }
        if (rbtnOut2.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedOut2(imgStarNormalOut2, imgStarCheckedOut2);
        }
        if (rbtnOut1.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedOut1(imgStarNormalOut1, imgStarCheckedOut1);
        }
        if (rbtnAccident5.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedAccident5(imgStarNormalAccident5, imgStarCheckedAccident5);
        }
        if (rbtnAccident4.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedAccident4(imgStarNormalAccident4, imgStarCheckedAccident4);
        }
        if (rbtnAccident3.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedAccident3(imgStarNormalAccident3, imgStarCheckedAccident3);
        }
        if (rbtnAccident2.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedAccident2(imgStarNormalAccident2, imgStarCheckedAccident2);
        }
        if (rbtnAccident1.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedAccident1(imgStarNormalAccident1, imgStarCheckedAccident1);
        }
        if (rbtnCabin5.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedCabin5(imgStarNormalCabin5, imgStarCheckedCabin5);
        }
        if (rbtnCabin4.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedCabin4(imgStarNormalCabin4, imgStarCheckedCabin4);
        }
        if (rbtnCabin3.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedCabin3(imgStarNormalCabin3, imgStarCheckedCabin3);
        }
        if (rbtnCabin2.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedCabin2(imgStarNormalCabin2, imgStarCheckedCabin2);
        }
        if (rbtnCabin1.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedCabin1(imgStarNormalCabin1, imgStarCheckedCabin1);
        }
        if (rbtnIn5.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedIn5(imgStarNormalIn5, imgStarCheckedIn5);
        }
        if (rbtnIn4.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedIn4(imgStarNormalIn4, imgStarCheckedIn4);
        }
        if (rbtnIn3.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedIn3(imgStarNormalIn3, imgStarCheckedIn3);
        }
        if (rbtnIn2.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedIn2(imgStarNormalIn2, imgStarCheckedIn2);
        }
        if (rbtnIn1.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedIn1(imgStarNormalIn1, imgStarCheckedIn1);
        }
        if (rbtnChassis5.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedChassis5(imgStarNormalChassis5, imgStarCheckedChassis5);
        }
        if (rbtnChassis4.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedChassis4(imgStarNormalChassis4, imgStarCheckedChassis4);
        }
        if (rbtnChassis3.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedChassis3(imgStarNormalChassis3, imgStarCheckedChassis3);
        }
        if (rbtnChassis2.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedChassis2(imgStarNormalChassis2, imgStarCheckedChassis2);
        }
        if (rbtnChassis1.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedChassis1(imgStarNormalChassis1, imgStarCheckedChassis1);
        }
        if (rbtnElectric5.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedElectric5(imgStarNormalElectric5, imgStarCheckedElectric5);
        }
        if (rbtnElectric4.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedElectric4(imgStarNormalElectric4, imgStarCheckedElectric4);
        }
        if (rbtnElectric3.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedElectric3(imgStarNormalElectric3, imgStarCheckedElectric3);
        }
        if (rbtnElectric2.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedElectric2(imgStarNormalElectric2, imgStarCheckedElectric2);
        }
        if (rbtnElectric1.isChecked()) {
            otherSubjoinInformationModel.showImgStarCheckedElectric1(imgStarNormalElectric1, imgStarCheckedElectric1);
        }
    }

    /**
     * @param data
     */
    @Override
    public void requestSubjoinSucceed(DetectionWrapper data) {
        //初始化页面，展示系统未判定的检测评级项
        ifShowCheckedItem(data);
        //转成json
        String json = new Gson().toJson(data);
        //将此方案保存到数据库
        if (!TextUtils.isEmpty(planId))
            DBManager.getInstance().insertPlan(planId, json, Constants.DATA_TYPE_PLAN);
        //保存到内存
        PadSysApp.wrapper = data;
    }

    /*
     * R01	外观检查
     * R02	内饰检查
     * R03	事故检查
     * R04	火烧检查
     * R05	泡水检查
     * R06	电气检查
     * R07	手续信息
     * R08	机舱检查
     * R09	机械检查
     * R10	底盘检查
     * R11	参数配置
     * 根据服务器返回的配置项决定隐藏哪些检测评级项
     * @param data
     */
    private void ifShowCheckedItem(DetectionWrapper data) {

        List<StarScoreItem> scoreProjectDesList = data.getScoreProjectDesList();
        if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
            for (int i = 0; i < scoreProjectDesList.size(); i++) {
                StarScoreItem starScoreItem = scoreProjectDesList.get(i);
                String reportProjectId = starScoreItem.getReportProjectId();
                int finalScoreIntervalId = starScoreItem.getFinalScoreIntervalId();
                switch (reportProjectId){
                    case "R01"://外观检查
                        switch (finalScoreIntervalId){
                            case 5:
                                rbtnOut5.setText(starScoreItem.getResultDes());
                                break;
                            case 4:
                                rbtnOut4.setText(starScoreItem.getResultDes());
                                break;
                            case 3:
                                rbtnOut3.setText(starScoreItem.getResultDes());
                                break;
                            case 2:
                                rbtnOut2.setText(starScoreItem.getResultDes());
                                break;
                            case 1:
                                rbtnOut1.setText(starScoreItem.getResultDes());
                                break;
                            default:
                        }
                        break;
                    case "R02"://内饰检查
                        switch (finalScoreIntervalId){
                            case 5:
                                rbtnIn5.setText(starScoreItem.getResultDes());
                                break;
                            case 4:
                                rbtnIn4.setText(starScoreItem.getResultDes());
                                break;
                            case 3:
                                rbtnIn3.setText(starScoreItem.getResultDes());
                                break;
                            case 2:
                                rbtnIn2.setText(starScoreItem.getResultDes());
                                break;
                            case 1:
                                rbtnIn1.setText(starScoreItem.getResultDes());
                                break;
                            default:
                        }
                        break;
                    case "R03"://事故检查
                        switch (finalScoreIntervalId){
                            case 5:
                                rbtnAccident5.setText(starScoreItem.getResultDes());
                                break;
                            case 4:
                                rbtnAccident4.setText(starScoreItem.getResultDes());
                                break;
                            case 3:
                                rbtnAccident3.setText(starScoreItem.getResultDes());
                                break;
                            case 2:
                                rbtnAccident2.setText(starScoreItem.getResultDes());
                                break;
                            case 1:
                                rbtnAccident1.setText(starScoreItem.getResultDes());
                                break;
                            default:
                        }
                        break;
                    case "R06"://电气检查
                        switch (finalScoreIntervalId){
                            case 5:
                                rbtnElectric5.setText(starScoreItem.getResultDes());
                                break;
                            case 4:
                                rbtnElectric4.setText(starScoreItem.getResultDes());
                                break;
                            case 3:
                                rbtnElectric3.setText(starScoreItem.getResultDes());
                                break;
                            case 2:
                                rbtnElectric2.setText(starScoreItem.getResultDes());
                                break;
                            case 1:
                                rbtnElectric1.setText(starScoreItem.getResultDes());
                                break;
                            default:
                        }
                        break;
                    case "R08"://机舱检查
                        switch (finalScoreIntervalId){
                            case 5:
                                rbtnCabin5.setText(starScoreItem.getResultDes());
                                break;
                            case 4:
                                rbtnCabin4.setText(starScoreItem.getResultDes());
                                break;
                            case 3:
                                rbtnCabin3.setText(starScoreItem.getResultDes());
                                break;
                            case 2:
                                rbtnCabin2.setText(starScoreItem.getResultDes());
                                break;
                            case 1:
                                rbtnCabin1.setText(starScoreItem.getResultDes());
                                break;
                            default:
                        }
                        break;
                    case "R10"://底盘检查
                        switch (finalScoreIntervalId){
                            case 5:
                                rbtnChassis5.setText(starScoreItem.getResultDes());
                                break;
                            case 4:
                                rbtnChassis4.setText(starScoreItem.getResultDes());
                                break;
                            case 3:
                                rbtnChassis3.setText(starScoreItem.getResultDes());
                                break;
                            case 2:
                                rbtnChassis2.setText(starScoreItem.getResultDes());
                                break;
                            case 1:
                                rbtnChassis1.setText(starScoreItem.getResultDes());
                                break;
                            default:
                        }
                        break;
                    default:
                }
            }
        }else {
            //外观检查
            rbtnOut5.setText("车辆外观无损伤，无修复痕迹");
            rbtnOut4.setText("车辆外观无损伤，存在少量修复痕迹");
            rbtnOut3.setText("车辆外观存在少量损伤，少量修复痕迹");
            rbtnOut2.setText("车辆外观存在少量损伤，多处修复痕迹");
            rbtnOut1.setText("车辆外观存在多处损伤，多处修复痕迹");
            //事故检查
            rbtnAccident5.setText("全车结构件无损伤，无修复痕迹");
            rbtnAccident4.setText("车辆因轻微碰撞事故，导致前防撞梁、可拆卸前纵梁吸能盒及底大边存在轻微损伤或修复痕迹");
            rbtnAccident3.setText("车辆因轻微碰撞事故，导致部分外围结构件存在损伤或修复痕迹");
            rbtnAccident2.setText("车辆因碰撞事故，导致多处外围结构件，部分重要结构件损伤或修复痕迹");
            rbtnAccident1.setText("车辆因碰撞事故，导致多处结构件严重损伤或有结构件存在切割更换修复痕迹");
            //机舱检查
            rbtnCabin5.setText("发动机怠速工况正常，舱内部件无缺失");
            rbtnCabin4.setText("发动机怠速工况正常，机舱部件少量老化");
            rbtnCabin3.setText("发动机怠速工况一般，机舱部件存在少量缺陷");
            rbtnCabin2.setText("发动机怠速工况较差，机舱部件存在较多缺陷");
            rbtnCabin1.setText("发动机怠速工况存在缺陷，机舱严重油污，机舱部件存在严重缺陷");
            //内饰检查
            rbtnIn5.setText("内饰整洁，无破损、无脏污");
            rbtnIn4.setText("内饰存在轻微破损、少量脏污");
            rbtnIn3.setText("内饰存在少量破损、少量脏污");
            rbtnIn2.setText("内饰存在少量破损、多处脏污");
            rbtnIn1.setText("内饰存在多处破损、多处脏污");
            //底盘检查
            rbtnChassis5.setText("底盘部件无损伤、无修复");
            rbtnChassis4.setText("底盘部件无损伤、存在少量修复痕迹");
            rbtnChassis3.setText("底盘部件存在少量损伤、少量修复痕迹");
            rbtnChassis2.setText("底盘部件存在少量损伤、多处修复痕迹");
            rbtnChassis1.setText("底盘部件存在多处损伤、多处修复痕迹");
            //电气检查
            rbtnElectric5.setText("全车电气部件外观无损伤，无功能故障");
            rbtnElectric4.setText("全车电气部件外观正常磨损，无功能故障");
            rbtnElectric3.setText("全车电气部件外观正常磨损，有功能故障");
            rbtnElectric2.setText("全车电气部件外观有损伤，有功能故障");
            rbtnElectric1.setText("全车电气部件外观部件有缺失，有功能故障");
        }
        isAuto = data.getIsAuto();
        if (isAuto == 0){//0---手动
            List<String> reportStarSelect = data.getReportStarSelect();
            if (reportStarSelect != null) {
                int reportStarSelectSize = reportStarSelect.size();
                for (int i = 0; i < reportStarSelectSize; i++) {
                    String R0X = reportStarSelect.get(i);
                    //外观检查
                    if (R0X.equals("R01")) {
                        llOut.setVisibility(View.GONE);
                        hideSize += 1;
                    }
                    //事故检查
                    if (R0X.equals("R03")) {
                        llAccident.setVisibility(View.GONE);
                        hideSize += 1;
                    }
                    //机舱检查
                    if (R0X.equals("R08")) {
                        llCabin.setVisibility(View.GONE);
                        hideSize += 1;
                    }
                    //内饰检查
                    if (R0X.equals("R02")) {
                        llIn.setVisibility(View.GONE);
                        hideSize += 1;
                    }
                    //底盘检查
                    if (R0X.equals("R10")) {
                        llChassis.setVisibility(View.GONE);
                        hideSize += 1;
                    }
                    //电气检查
                    if (R0X.equals("R06")) {
                        llElectric.setVisibility(View.GONE);
                        hideSize += 1;
                    }
                    //火烧检查
                    if (R0X.equals("R04")) {
                        llFire.setVisibility(View.GONE);
                        hideSize += 1;
                    }
                    //泡水检查
                    if (R0X.equals("R05")) {
                        llWater.setVisibility(View.GONE);
                        hideSize += 1;
                    }
                }
            }
        }else {//自动--隐藏泡水检查、火烧检查、事故检查、外观检查、机舱检查、内饰检查、电气检查、底盘检查
            llOut.setVisibility(View.GONE);
            llAccident.setVisibility(View.GONE);
            llCabin.setVisibility(View.GONE);
            llIn.setVisibility(View.GONE);
            llChassis.setVisibility(View.GONE);
            llElectric.setVisibility(View.GONE);
            llFire.setVisibility(View.GONE);
            llWater.setVisibility(View.GONE);
        }
        //显示布局
        llSubjoinInfo.setVisibility(View.VISIBLE);
        //隐藏dialog
        dismissDialog();
    }

    /**
     * 保存数据到本地数据库&&保存数据到SubmitModel
     */
    public void saveData() {
        if (otherSubjoinInfoBean == null) {
            otherSubjoinInfoBean = new OtherSubjoinInfoBean();
        }
        SubmitModel submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();

        otherSubjoinInformationModel.saveDataToDB(otherSubjoinInfoBean, submitModel, llBuyPrice, llSalePrice, etBuy, etSale,
                rgOut, rgAccident, rgCabin, rgIn, rgChassis, rgElectric,
                rgFire, rgWater, rgHoldingvolume, rgRecognition, rgHedgeRatio,
                etOtherInfo);
        //保存数据到大对象
        ((DetectMainActivity) getActivity()).setSubmitModel(submitModel);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            UIUtils.hideInputMethodManager(getActivity());
            etOtherInfo.setFocusableInTouchMode(false);
            etOtherInfo.setFocusable(false);
        }
        return false;
    }

    @Override
    public void succeedDetail(List<HistoryPriceModel> listData) {

    }

    @Override
    public void succeedCount(int count) {
        if (count > 0) {
            tvHistoryPrice.setVisibility(View.VISIBLE);
        } else {
            tvHistoryPrice.setVisibility(View.GONE);
        }
    }

    public boolean isFix() {
        return isFix;
    }

    public void setFix(boolean fix) {
        isFix = fix;
    }
}
