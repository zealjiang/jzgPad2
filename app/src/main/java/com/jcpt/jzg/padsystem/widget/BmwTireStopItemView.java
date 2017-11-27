package com.jcpt.jzg.padsystem.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;


/**
 * Created by wujj on 2017/10/25.
 * 邮箱：wujj@jingzhengu.com
 * 作用：轮胎轮毂itemView
 */

public class BmwTireStopItemView extends LinearLayout {
    private IMyOnclickLister iMyOnclickLister;
    private int labelColor;
    private String labelTire;
    private String rbtnStr1;
    private String rbtnStr2;
    private RadioGroup rg;
    private RadioButton rbtnYes;
    private RadioButton rbtnNo;

    public BmwTireStopItemView(Context context) {
        this(context, null);
    }

    public BmwTireStopItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BmwTireStopItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initviews(context, attrs);
    }

    private void initviews(Context context, AttributeSet attrs) {
        View parent = View.inflate(context, R.layout.item_bmw_tire_stop_layout, this);
        TextView tv_label = (TextView) parent.findViewById(R.id.tv_label);
        rg = (RadioGroup) parent.findViewById(R.id.rg);
        rbtnYes = (RadioButton) parent.findViewById(R.id.rbtnYes);
        rbtnNo = (RadioButton) parent.findViewById(R.id.rbtnNo);

        rbtnYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (iMyOnclickLister != null) {
                        iMyOnclickLister.OnMyclick1();
                    }
                }
            }
        });
        rbtnNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (iMyOnclickLister != null) {
                        iMyOnclickLister.OnMyclick2();
                    }
                }
            }
        });

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TireStopItemViewWidget);
        if (ta != null) {
            labelTire = ta.getString(R.styleable.TireStopItemViewWidget_labelTire);
            labelColor = ta.getColor(R.styleable.TireStopItemViewWidget_labelColorTire, Color.BLACK);

            rbtnStr1 = ta.getString(R.styleable.TireStopItemViewWidget_rbtnStr1);
            rbtnStr2 = ta.getString(R.styleable.TireStopItemViewWidget_rbtnStr2);
            ta.recycle();//回收内存
        }
        tv_label.setText(TextUtils.isEmpty(labelTire) ? "" : labelTire);
        tv_label.setTextColor(labelColor);

        rbtnYes.setText(TextUtils.isEmpty(rbtnStr1) ? "是" : rbtnStr1);
        rbtnNo.setText(TextUtils.isEmpty(rbtnStr2) ? "否" : rbtnStr2);
    }

    public void setMyOnclickLister(IMyOnclickLister mIMyOnclickLister) {
        iMyOnclickLister = mIMyOnclickLister;
    }

    public interface IMyOnclickLister {
        void OnMyclick1();

        void OnMyclick2();
    }

    //选中的rbtn
    public int getRbtnClickPos(){
        //机舱检查选中的下标
        int checkedRadioButtonId = rg.getCheckedRadioButtonId();
        int rbtnClickPos = rg.indexOfChild(rg.findViewById(checkedRadioButtonId));
        return rbtnClickPos;
    }

    public RadioButton getRbtnYes(){
        return rbtnYes;
    }

    public RadioButton getRbtnNo(){
        return rbtnNo;
    }

    public RadioGroup getRg(){
        return rg;
    }
}
