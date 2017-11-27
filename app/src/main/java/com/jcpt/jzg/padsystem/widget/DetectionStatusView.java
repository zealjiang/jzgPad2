package com.jcpt.jzg.padsystem.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.global.Constants;


/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/9/20 17:00
 * @desc:
 */
public class DetectionStatusView extends LinearLayout {
    private TextView tvItemName;
    private ImageView ivRight;
    private String itemName;
    private RelativeLayout rlDetectItem;
    private int status = Constants.STATUS_NORMAL;


    IDetectionStatusLister iDetectionStatusLister;

    public DetectionStatusView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DetectionStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View parent = View.inflate(context, R.layout.check_item_layout, this);
        rlDetectItem = (RelativeLayout) parent.findViewById(R.id.rlDetectItem);
        tvItemName = (TextView) parent.findViewById(R.id.tvItemName);
        ivRight = (ImageView) parent.findViewById(R.id.ivRight);


        //整个条目的监听  -> 李波 on 2017/8/24.
        rlDetectItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iDetectionStatusLister != null) {
                    iDetectionStatusLister.Onclick(1);
                }
            }
        });

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        changeStatus();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
        tvItemName.setText(itemName);
    }

    private void changeStatus(){
        switch (status){
            case Constants.STATUS_NORMAL:
                ivRight.setImageResource(R.mipmap.ic_check_normal);
                ivRight.setBackgroundColor(Color.WHITE);
                tvItemName.setBackgroundColor(Color.WHITE);
                tvItemName.setTextColor(Color.BLACK);
                break;
            case Constants.STATUS_OK:
                rlDetectItem.setBackgroundResource(R.drawable.drawable_check_blue);
                ivRight.setImageResource(R.drawable.fill_shanc_xzhong);
                break;
            case Constants.STATUS_ABNORMAL:
                rlDetectItem.setBackgroundResource(R.drawable.drawable_check_red);
                ivRight.setImageResource(R.drawable.ic_defect);
                break;
            default:
                break;
        }
    }

    public void setDetectionOnclickLister(IDetectionStatusLister mIDetectionStatusLister){
        iDetectionStatusLister = mIDetectionStatusLister;
    }

    public interface IDetectionStatusLister{
        public void Onclick(int pos);
    }
}
