package com.jcpt.jzg.padsystem.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.MyTagStringAdapter;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by wujj on 2017/10/25.
 * 邮箱：wujj@jingzhengu.com
 * 作用：随车附件itemView
 */

public class BmwCarGoodsItemView extends LinearLayout {
    private IMyOnclickLister iMyOnclickLister;
    private int labelColorCar;
    private String labelCar;
    private String[] numVals;
    private String[] vals = {"达标","不达标"};
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private Context context;
    private String tagVal1;
    private String tagVal2;
    private String tagVal3;
    private String tagVal4;
    private String tagVal5;
    private String tagVal6;
    private String tagVal7;
    private String tagVal8;

    private int tagClickPos = -1;
    private TagFlowLayout tfl;
    private RadioGroup rg_standard;
    private RadioButton rbtn_standardFront;
    private RadioButton rbtn_standardBack;

    public BmwCarGoodsItemView(Context context) {
        this(context, null);
    }

    public BmwCarGoodsItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BmwCarGoodsItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initviews(context, attrs);
    }

    private void initviews(Context context, AttributeSet attrs) {
        View parent = View.inflate(context, R.layout.item_bmw_car_goods_layout, this);
        TextView tv_label = (TextView) parent.findViewById(R.id.tv_label);
        tfl = (TagFlowLayout) parent.findViewById(R.id.tfl);
        rg_standard = parent.findViewById(R.id.rg_standard);
        rbtn_standardFront = parent.findViewById(R.id.rbtn_standardFront);
        rbtn_standardBack = parent.findViewById(R.id.rbtn_standardBack);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CarGoodsItemViewWidget);
        if (ta != null) {
            labelCar = ta.getString(R.styleable.CarGoodsItemViewWidget_labelCar);
            labelColorCar = ta.getColor(R.styleable.CarGoodsItemViewWidget_labelColorCar, Color.BLACK);

            tagVal1 = ta.getString(R.styleable.CarGoodsItemViewWidget_tagVal1);
            tagVal2 = ta.getString(R.styleable.CarGoodsItemViewWidget_tagVal2);
            tagVal3 = ta.getString(R.styleable.CarGoodsItemViewWidget_tagVal3);
            tagVal4 = ta.getString(R.styleable.CarGoodsItemViewWidget_tagVal4);
            tagVal5 = ta.getString(R.styleable.CarGoodsItemViewWidget_tagVal5);
            tagVal6 = ta.getString(R.styleable.CarGoodsItemViewWidget_tagVal6);
            tagVal7 = ta.getString(R.styleable.CarGoodsItemViewWidget_tagVal7);
            tagVal8 = ta.getString(R.styleable.CarGoodsItemViewWidget_tagVal8);
            ta.recycle();//回收内存

        }
        tv_label.setText(TextUtils.isEmpty(labelCar) ? "" : labelCar);
        tv_label.setTextColor(labelColorCar);

        if (!TextUtils.isEmpty(tagVal1)) {
            stringArrayList.add(tagVal1);
        }
        if (!TextUtils.isEmpty(tagVal2)) {
            stringArrayList.add(tagVal2);
        }
        if (!TextUtils.isEmpty(tagVal3)) {
            stringArrayList.add(tagVal3);
        }
        if (!TextUtils.isEmpty(tagVal4)) {
            stringArrayList.add(tagVal4);
        }
        if (!TextUtils.isEmpty(tagVal5)) {
            stringArrayList.add(tagVal5);
        }
        if (!TextUtils.isEmpty(tagVal6)) {
            stringArrayList.add(tagVal6);
        }
        if (!TextUtils.isEmpty(tagVal7)) {
            stringArrayList.add(tagVal7);
        }
        if (!TextUtils.isEmpty(tagVal8)) {
            stringArrayList.add(tagVal8);
        }
        numVals = new String[stringArrayList.size()];
        for (int i = 0; i < stringArrayList.size(); i++) {
            numVals[i] = stringArrayList.get(i);
        }
        setTag(numVals, tfl);
    }

    public void setMyOnclickLister(IMyOnclickLister mIMyOnclickLister) {
        iMyOnclickLister = mIMyOnclickLister;
    }

    public void setTag(final String[] vals, final TagFlowLayout tagFlowLayout) {
        MyTagStringAdapter Adapter = new MyTagStringAdapter(vals, tagFlowLayout, this.context);
        tagFlowLayout.setAdapter(Adapter);
        setTagListener(tagFlowLayout);
    }

    private void setTagListener(final TagFlowLayout tagFlowLayout) {
        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

            }
        });
    }

    //选中的tag
    public int getTagClickPos(){
        HashSet<Integer> selectedList = (HashSet<Integer>) tfl.getSelectedList();
        Iterator<Integer> iterator = selectedList.iterator();
        if (iterator.hasNext()) {
            while (iterator.hasNext()){
                tagClickPos = iterator.next();
            }
        }else {
            tagClickPos = -1;
        }
        return tagClickPos;
    }

    //选中的rbtn
    public int getRbtnClickPos(){
        //机舱检查选中的下标
        int checkedRadioButtonId = rg_standard.getCheckedRadioButtonId();
        int rbtnClickPos = rg_standard.indexOfChild(rg_standard.findViewById(checkedRadioButtonId));
        return rbtnClickPos;
    }

    public TagFlowLayout getTfl(){
        return tfl;
    }

    public RadioButton getRbtnStandardFront(){
        return rbtn_standardFront;
    }

    public RadioButton getRbtnstandardBack(){
        return rbtn_standardBack;
    }

    public interface IMyOnclickLister {
        void OnTagclick(int position);
    }
}
