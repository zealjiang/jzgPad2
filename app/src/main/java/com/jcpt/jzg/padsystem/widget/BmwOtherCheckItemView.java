package com.jcpt.jzg.padsystem.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;


/**
 * Created by wujj on 2017/10/25.
 * 邮箱：wujj@jingzhengu.com
 * 作用：其他检查itemView
 */

public class BmwOtherCheckItemView extends LinearLayout {
    private IMyOnclickLister iMyOnclickLister;
    private int labelColor;
    private String label;
    private String rbtnStrYes;
    private String rbtnStrNo;
    private RadioGroup rg;
    private RadioButton rbtnYes;
    private RadioButton rbtnNo;

    public BmwOtherCheckItemView(Context context) {
        this(context, null);
    }

    public BmwOtherCheckItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BmwOtherCheckItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initviews(context, attrs);
    }

    private void initviews(Context context, AttributeSet attrs) {
        View parent = View.inflate(context, R.layout.item_bmw_other_check_layout, this);
        TextView tv_label = (TextView) parent.findViewById(R.id.tv_label);
        rg = (RadioGroup) parent.findViewById(R.id.rg);
        rbtnYes = (RadioButton) parent.findViewById(R.id.rbtnYes);
        rbtnNo = (RadioButton) parent.findViewById(R.id.rbtnNo);

        rbtnYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (iMyOnclickLister != null) {
                        iMyOnclickLister.OnOkclick();
                    }
                }
            }
        });
        rbtnNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (iMyOnclickLister != null) {
                        iMyOnclickLister.OnCancleclick();
                    }
                }
            }
        });

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OtherCheckItemViewWidget);
        if (ta != null) {
            label = ta.getString(R.styleable.OtherCheckItemViewWidget_label);
            labelColor = ta.getColor(R.styleable.OtherCheckItemViewWidget_labelColor, Color.BLACK);

            rbtnStrYes = ta.getString(R.styleable.OtherCheckItemViewWidget_rbtnStrYes);
            rbtnStrNo = ta.getString(R.styleable.OtherCheckItemViewWidget_rbtnStrNo);
            ta.recycle();//回收内存
        }
        tv_label.setText(TextUtils.isEmpty(label) ? "" : label);
        tv_label.setTextColor(labelColor);

        rbtnYes.setText(TextUtils.isEmpty(rbtnStrYes) ? "是" : rbtnStrYes);
        rbtnNo.setText(TextUtils.isEmpty(rbtnStrNo) ? "否" : rbtnStrNo);
    }

    public void setMyOnclickLister(IMyOnclickLister mIMyOnclickLister) {
        iMyOnclickLister = mIMyOnclickLister;
    }

    public interface IMyOnclickLister {
        void OnOkclick();

        void OnCancleclick();
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
