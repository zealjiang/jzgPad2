package com.jcpt.jzg.padsystem.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.PlateTypeAdapter;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.mvpview.IPlateType;
import com.jcpt.jzg.padsystem.presenter.PlateTypePrsenter;
import com.jcpt.jzg.padsystem.utils.DateTool;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.InputUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.EventModel;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.FullyLinearLayoutManager;
import com.jcpt.jzg.padsystem.widget.nodoubleclick.NoDoubleOnclickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 铭牌型号选择
 * Created by zealjiang on 2017/4/18 10:32.
 * Email: zealjiang@126.com
 */

public class PlateTypeActivity extends BaseActivity implements IPlateType,TextView.OnEditorActionListener {

    @BindView(R.id.llNormal)
    LinearLayout llNormal;
    @BindView(R.id.rvPlate)
    RecyclerView rvPlate;
    @BindView(R.id.etPlate)
    EditText etPlate;
    @BindView(R.id.llPlate)
    LinearLayout llPlate;
    @BindView(R.id.tvLine)
    TextView tvLine;
    @BindView(R.id.llError)
    LinearLayout llError;
    @BindView(R.id.etPlateError)
    EditText etPlateError;
    @BindView(R.id.crbNext)
    Button crbNext;
    @BindView(R.id.rlRetry)
    RelativeLayout rlRetry;
    @BindView(R.id.crbRetry)
    CustomRippleButton crbRetry;
    @BindView(R.id.tvRetry)
    TextView tvRetry;
    @BindView(R.id.rlProductDate)
    RelativeLayout rlProductDate;
    @BindView(R.id.tvProductDate)
    TextView tvProductDate;

    private String vin;
    private String productDate;
    private PlateTypeAdapter adapter;
    private List<String> listPlateType;
    private PlateTypePrsenter plateTypePrsenter;
    private String sPlateType;//铭牌型号 选中的或手动输入的
    private String brandType;//品牌和型号
    private boolean isInputPlateType = false;//铭牌型号是否是手动输入

    private boolean isSelectInput = false;//是否选择了手动输入
    private String sProductDate;//出厂日期
    private DateTool dateTool;
    private long lastExecuteTime;//上一次执行操作的时间

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_plate_type);
        ButterKnife.bind(this);
        vin = getIntent().getStringExtra("vin");
        productDate = getIntent().getStringExtra("productDate");
        sPlateType = getIntent().getStringExtra("nameplate");
        brandType = getIntent().getStringExtra("brandType");

        tvProductDate.setText(productDate);

        dateTool = new DateTool();
        //铭牌列表
        listPlateType = new ArrayList<>();
        plateTypePrsenter = new PlateTypePrsenter(this);

        LinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        rvPlate.setLayoutManager(linearLayoutManager);

        adapter = new PlateTypeAdapter(this,listPlateType);
        rvPlate.setAdapter(adapter);
        adapter.setOnItemClickListener(new PlateTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {

                if(pos==listPlateType.size()-1){
                    llPlate.setVisibility(View.VISIBLE);
                    isSelectInput = true;
                    KeyboardUtils.showSoftInput(PlateTypeActivity.this.getApplication(), etPlate);

                    //如果选择了手动输入，判断手动输入内容是否为空，如果为空，清空铭牌，下一步不可点
                    if(TextUtils.isEmpty(etPlate.getText().toString().trim())){
                        sPlateType = "";
                        check();
                    }
                }else {
                    isSelectInput = false;
                    sPlateType = listPlateType.get(pos);
                    if(etPlate.getVisibility()!=View.GONE){
                        llPlate.setVisibility(View.GONE);
                        etPlate.setText("");
                    }

                    check();
                    isInputPlateType = false;
                }
            }
        });

        //铭牌车辆
        etPlate.setTransformationMethod(new InputLowerToUpper());
        //不能输入表情符 汉字
        InputUtil.getInstance().inputRestrict(etPlate,true,true,false,false);
        //铭牌车辆
        etPlateError.setTransformationMethod(new InputLowerToUpper());
        //不能输入表情符 汉字
        InputUtil.getInstance().inputRestrict(etPlateError,true,true,false,false);



        etPlate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                check();

                if(!TextUtils.isEmpty(s.toString())) {
                    isInputPlateType = true;
                }
            }
        });

        etPlateError.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check();
                if(!TextUtils.isEmpty(s.toString())) {
                    isInputPlateType = true;
                }
            }
        });

        tvProductDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check();
                if(!StringUtils.isEmpty(s.toString())){
                    EventModel eventModel = new EventModel();
                    eventModel.setName("productDate");
                    eventModel.setValue(sProductDate);

                    EventBus.getDefault().post(eventModel);//将出厂日期传给手续信息
                }


            }
        });
        //网络请求
        //plateTypePrsenter.getPlateTypes(vin);

        List<String> typeList = new ArrayList<>();
        String type;
        if(!StringUtils.isEmpty(brandType)){
            if(brandType.contains(",")){
                String[] brandAndType = brandType.split(",");
                if(!StringUtils.isEmpty(brandAndType[1])){
                    type = brandAndType[1];
                    typeList.add(type);
                }
            }
        }

        putTypeList(typeList);
    }

    @Override
    protected void setData() {
        setTitle("铭牌型号选择");

        rlProductDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //出厂日期
                dateTool.selectYearMonth(PlateTypeActivity.this, "出厂日期", tvProductDate, Calendar.getInstance().get(Calendar.YEAR), 1990, false);
            }
        });
    }

    @OnClick({R.id.ivLeft,R.id.crbNext,R.id.crbRetry})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivLeft://返回
                finish();
                break;
            case R.id.crbNext:

                Intent intent = new Intent();
                intent.putExtra("vin",vin);
                intent.putExtra("plateType",sPlateType);
                intent.putExtra("productDate",sProductDate);
                intent.putExtra("isInputPlateType",isInputPlateType);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.crbRetry:
                //网络请求
                plateTypePrsenter.getPlateTypes(vin);
                break;
        }
    }

    private boolean check(){
        if(llNormal.getVisibility()!=View.GONE&&isSelectInput){//当前选中的是手动输入
            sPlateType = etPlate.getText().toString();
        }else if(llError.getVisibility()!=View.GONE){
            sPlateType = etPlateError.getText().toString();
        }

        sProductDate = tvProductDate.getText().toString();

        if(StringUtils.isEmpty(sPlateType)||TextUtils.isEmpty(sProductDate)){
            //不可点击
            crbNext.setEnabled(false);
            crbNext.setBackgroundColor(Color.parseColor("#e9e9e9"));
            crbNext.setTextColor(Color.parseColor("#d0d0d0"));
            return false;
        }else {
            crbNext.setEnabled(true);
            crbNext.setBackgroundColor(getResources().getColor(R.color.common_btn_blue));
            crbNext.setTextColor(Color.parseColor("#ffffff"));
            return true;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()){
            case R.id.etPlate:
                if(check()) {
                    crbNext.performClick();
                }
                break;
        }
        return false;
    }

    /**
     * 请求结果为0
     */
    private void isZero(int size){
        if(size==0){
            llNormal.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
            //MyToast.showShort("没有找到合适的铭牌型号，请对照铭牌在下方填写");
            if(!TextUtils.isEmpty(sPlateType)){
                etPlateError.setText(sPlateType);
            }
        }else{
            llNormal.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
        }
    }

    @Override
    public void success(List<String> suc) {
        putTypeList(suc);
    }

    /**
     *  添加到列表里
     */
    private void putTypeList(List<String> suc){
        if(suc==null){
            isZero(0);
            return;
        }

        isZero(suc.size());
        listPlateType.clear();
        listPlateType.addAll(suc);

        if(suc.size()>0){
            listPlateType.add("手动输入");
            adapter.setCheckPos(listPlateType.size()-2);
        }else{
            adapter.setCheckPos(listPlateType.size()-1);
        }

        //adapter.onDataChanged();

        adapter.notifyDataSetChanged();


        rlRetry.setVisibility(View.GONE);
        etPlateError.setText("");


        if(!TextUtils.isEmpty(sPlateType)){
            etPlate.setText(sPlateType);
        }
    }

    @Override
    public void fail(String string) {
        rlRetry.setVisibility(View.GONE);
        llNormal.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        //MyToast.showShort("没有找到合适的铭牌型号，请对照铭牌在下方填写");
        etPlateError.setFocusable(true);
        etPlateError.requestFocus();
        KeyboardUtils.showSoftInput(PlateTypeActivity.this.getApplication(), etPlateError);


        if(!TextUtils.isEmpty(sPlateType)){
            etPlateError.setText(sPlateType);
        }
    }

    @Override
    public void error(String string) {
        MyToast.showShort(string);
        llNormal.setVisibility(View.GONE);
        llError.setVisibility(View.GONE);
        rlRetry.setVisibility(View.VISIBLE);
    }


}
