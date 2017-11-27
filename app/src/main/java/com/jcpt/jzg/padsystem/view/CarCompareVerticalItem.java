package com.jcpt.jzg.padsystem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.SizeUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.global.Constants;

import java.util.ArrayList;

/**
 * Created by liugl on 2016/11/2.
 */

public class CarCompareVerticalItem extends  LinearLayout{
    private LinearLayout ll_hrz;
    private Context context;
    private ArrayList<SimpleDraweeView> views;
    private int viewCount;


    public CarCompareVerticalItem(Context context) {
        super(context);
        initViews();
    }

    public CarCompareVerticalItem(Context context, int viewCount) {
        super(context, null);
        this.context = context;
        this.viewCount = viewCount;

        initViews();
        addItemView(viewCount);
    }

    public CarCompareVerticalItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CarCompareVerticalItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews(){
        View view = View.inflate(context, R.layout.carpiccompared_vertical_list_item, this);
        ll_hrz = (LinearLayout) view.findViewById(R.id.ll_hrz);
    }

    public void addItemView(int count){
        views = new ArrayList<>();
        views.clear();
        float dip_200 = context.getResources().getDimension(R.dimen.DIP_200_L)/ Constants.density;
        float dip_170 = context.getResources().getDimension(R.dimen.DIP_170_L)/ Constants.density;
        float dip_1 = context.getResources().getDimension(R.dimen.DIP_1)/ Constants.density;
        for(int i=0;i<count;i++){
            SimpleDraweeView imgView = (SimpleDraweeView)View.inflate(context, R.layout.vertical_item_carpic, null);
            LayoutParams layoutParams = new LayoutParams(SizeUtils.dp2px(context,dip_170),
                    SizeUtils.dp2px(context,dip_200));
            layoutParams.leftMargin = SizeUtils.dp2px(context,dip_1);
            imgView.setLayoutParams(layoutParams);
            views.add(imgView);
            imgView.setTag(i);
            ll_hrz.addView(imgView);
        }
    }

    public LinearLayout getLl_hrz() {
        return ll_hrz;
    }

    public void setLl_hrz(LinearLayout ll_hrz) {
        this.ll_hrz = ll_hrz;
    }

    public ArrayList<SimpleDraweeView> getViews() {
        return views;
    }

    public void setViews(ArrayList<SimpleDraweeView> views) {
        this.views = views;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
