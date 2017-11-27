package com.jcpt.jzg.padsystem.view;
/**
 * author: gcc
 * date: 2016/11/1 17:20
 * email:
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.SizeUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.global.Constants;

import java.util.ArrayList;


public class MyVerticalItem extends LinearLayout {

    private LinearLayout ll_hrz;
    private Context context;
    private ArrayList<View> views;


    public ArrayList<View> getViews() {
        return views;
    }

    public MyVerticalItem(Context context) {
        this(context, null);
    }
    public MyVerticalItem(Context context, int viewCount) {
        this(context, null);
        addItemView(viewCount);
    }

    public MyVerticalItem(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public MyVerticalItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = View.inflate(context, R.layout.vertical_list_item, this);
        ll_hrz = (LinearLayout) view.findViewById(R.id.ll_hrz);
    }

    public LinearLayout getLl_hrz() {
        return ll_hrz;
    }

    public void setLl_hrz(LinearLayout ll_hrz) {
        this.ll_hrz = ll_hrz;
    }
    public void addItemView(int count){
        views = new ArrayList<>();
        views.clear();
        float dip_170 = context.getResources().getDimension(R.dimen.DIP_170_L)/ Constants.density;
        float dip_1 = context.getResources().getDimension(R.dimen.DIP_1)/ Constants.density;
        for(int i=0;i<count;i++){
            View textView = View.inflate(context, R.layout.vertical_item, null);

            LayoutParams layoutParams = new LayoutParams(SizeUtils.dp2px(context,dip_170), LayoutParams.MATCH_PARENT);


            layoutParams.leftMargin = SizeUtils.dp2px(context, 1);
            layoutParams.topMargin = SizeUtils.dp2px(context,1);
            textView.setLayoutParams(layoutParams);
            views.add(textView);
            ll_hrz.addView(textView);
        }

    }

}
