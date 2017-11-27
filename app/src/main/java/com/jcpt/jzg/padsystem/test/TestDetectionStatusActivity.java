package com.jcpt.jzg.padsystem.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.widget.DetectionStatusView;
import com.jcpt.jzg.padsystem.widget.GridSpacingItemDecoration;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestDetectionStatusActivity extends AppCompatActivity {

    @BindView(R.id.rvOptions)
    RecyclerView rvOptions;
    private List<DetectItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detection_status);
        ButterKnife.bind(this);
        dataList = new ArrayList<>();
        dataList.add(new DetectItem(0,"左前翼子板",0));
        dataList.add(new DetectItem(1,"右前翼子板",1));
        dataList.add(new DetectItem(2,"左前门",2));
        dataList.add(new DetectItem(3,"右后门",1));
        dataList.add(new DetectItem(4,"引擎盖",2));
        dataList.add(new DetectItem(5,"左前轮毂",1));
        dataList.add(new DetectItem(6,"左前轮胎",2));
        dataList.add(new DetectItem(7,"前横梁",0));
        dataList.add(new DetectItem(8,"前大梁",2));
        dataList.add(new DetectItem(9,"右前大灯",0));
        dataList.add(new DetectItem(10,"左后视镜",1));
        rvOptions.setLayoutManager(new GridLayoutManager(this,3));
        rvOptions.addItemDecoration(new GridSpacingItemDecoration(3,30,false));
        rvOptions.setAdapter(new MyAdapter(this,R.layout.item_detect_option,dataList));
    }


    private class MyAdapter extends CommonAdapter<DetectItem>{

        public MyAdapter(Context context, int layoutId, List<DetectItem> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, DetectItem detectItem) {
            DetectionStatusView nsvList = holder.getView(R.id.nsvList);
            nsvList.setStatus(detectItem.getCheckResult());
            nsvList.setItemName(detectItem.getName());
        }
    }


    private class DetectItem{
        private int id;
        private String name;
        private int checkResult;

        public DetectItem(int id, String name, int checkResult) {
            this.id = id;
            this.name = name;
            this.checkResult = checkResult;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCheckResult() {
            return checkResult;
        }

        public void setCheckResult(int checkResult) {
            this.checkResult = checkResult;
        }
    }
}
