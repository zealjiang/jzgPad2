package com.jcpt.jzg.padsystem.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.vo.CarTypeSelectModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 车型选择--差异配置
 * Created by zealjiang on 2016/11/10 10:20.
 * Email: zealjiang@126.com
 */

public class BMWCarDiffSelectView extends LinearLayout {


    @BindView(R.id.tagflow_diff_config)
    TagFlowLayout tagFlowDiff;
    private BMWCarDiffSelectView self;
    private ArrayList<String> diffList = new ArrayList<>();
    private TagAdapter tagAdapterDiff;
    private List<CarTypeSelectModel.MemberValueBean.ConfigListBean> listData;
    private ArrayList<String> selectedItemName;
    private ArrayList<String> selectedItemPos;
    private BMWCarDiffSelectView.OnDiffSelectedItemListener onDiffSelectedItemListener;
    private BMWDetectMainActivity detectMainActivity;

    public BMWCarDiffSelectView(Context context) {
        super(context);
        initView();
    }

    public BMWCarDiffSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BMWCarDiffSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setDetectMainActivity(BMWDetectMainActivity detectMainActivity){
        this.detectMainActivity = detectMainActivity;
    }

    private void initView() {
        self = this;
        View.inflate(getContext(), R.layout.widget_diff_select, this);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        selectedItemName = new ArrayList<>();
        selectedItemPos = new ArrayList<>();
        final LayoutInflater mInflater = LayoutInflater.from(this.getContext());

        tagAdapterDiff= new TagAdapter<String>(diffList)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_car_config, tagFlowDiff, false);
                tv.setText(s);
                return tv;
            }
        };
        tagFlowDiff.setAdapter(tagAdapterDiff);
        tagFlowDiff.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                //Toast.makeText(CarDiffSelectView.this.getContext(), diffList.get(position), Toast.LENGTH_SHORT).show();
                selectedItemPos.clear();
                String displacementSelected = diffList.get(position);
                //添加或删除点击的项
                if(selectedItemName.contains(displacementSelected)){
                    selectedItemName.remove(displacementSelected);
                }else{
                    selectedItemName.add(displacementSelected);
                }
                if(null!=onDiffSelectedItemListener){
                    //由Name得到对应的ID
                    for (int i = 0; i < selectedItemName.size(); i++) {
                        String pos = getConfigPosByName(listData,selectedItemName.get(i));
                        selectedItemPos.add(pos);
                    }
                    onDiffSelectedItemListener.onDiffSelectedItem(selectedItemPos);
                }

                return true;
            }
        });
    }

    /**
     * 初始化tag标签
     * @author zealjiang
     * @time 2016/11/10 11:17
     */
    public void initTagFlowData(List<CarTypeSelectModel.MemberValueBean.ConfigListBean> listData){
        this.listData = listData;
        selectedItemName.clear();
        selectedItemPos.clear();
        diffList.clear();
        if(listData==null){
            //刷新参数选择
            tagAdapterDiff.notifyDataChanged();
            return;
        }
        for (int i = 0; i < listData.size(); i++) {
            if(TextUtils.isEmpty(listData.get(i).getName())){
                return;
            }
            diffList.add(listData.get(i).getName());
        }

        HashSet<Integer> set = null;
        tagAdapterDiff.setSelectedList(set);
        //刷新参数选择
        tagAdapterDiff.notifyDataChanged();
    }

    public void clearData(){
        selectedItemName.clear();
        selectedItemPos.clear();
        diffList.clear();
        //刷新参数选择
        tagAdapterDiff.notifyDataChanged();
    }

    /**
     * 根据配置项的名称得到对应的ID
     * @author zealjiang
     * @time 2016/11/10 17:01
     */
    private String getConfigPosByName(List<CarTypeSelectModel.MemberValueBean.ConfigListBean> configListBeen,String name){
        for (int i = 0; i < configListBeen.size(); i++) {
            if(configListBeen.get(i).getName().equals(name)){
                return configListBeen.get(i).getPos();
            }
        }
        return "";
    }

    /**
     * 根据配置项的位置得到对应的名称
     * @author zealjiang
     * @time 2016/11/10 17:01
     */
    private String getConfigNameByPos(List<CarTypeSelectModel.MemberValueBean.ConfigListBean> configListBeen,String pos){
        for (int i = 0; i < configListBeen.size(); i++) {
            if(configListBeen.get(i).getPos().equals(pos)){
                return configListBeen.get(i).getName();
            }
        }
        return "";
    }

    public void resetTagFlow(List<CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean> filterCars){
        if(listData==null||listData.size()==0){
            return;
        }
        //思路 去除各车型中都有的配置项，剩下的配置项就是差异项
        //列出选中的项和筛选出的车辆的差异项
        diffList.clear();
        //列出N个数组
        ArrayList<ArrayList<String>> carListConfigPos = new ArrayList<>();
        for (int i = 0; i < filterCars.size(); i++) {
            String spos =filterCars.get(i).getPosString();
            if(spos.length()==0){
                carListConfigPos.add(new ArrayList<String>());
                continue;
            }
            String[] posArray = spos.split(",");
            ArrayList<String> list = new ArrayList<>();
            for (int j = 0; j < posArray.length; j++) {
                list.add(posArray[j]);
            }
            carListConfigPos.add(list);
        }

        //去除N个数组中相同的项
        for (int i = 0; i < listData.size(); i++) {
            String pos = listData.get(i).getPos();
            //判断N个数组中是否都有此项，若有则去除
            int k=0;
            for (int j = 0; j < carListConfigPos.size(); j++) {
                if(carListConfigPos.get(j).contains(pos)){
                    k++;
                }
            }
            if(k==carListConfigPos.size()){
                //从N个数组中去除此项
                for (int j = 0; j < carListConfigPos.size(); j++) {
                    carListConfigPos.get(j).remove(pos);
                }
            }
        }
        //将N个数组中不同的项添加到差异项中
        for (int i = 0; i < carListConfigPos.size(); i++) {
            ArrayList<String> listPos = carListConfigPos.get(i);
            for (int j = 0; j < listPos.size(); j++) {
                String configName = getConfigNameByPos(listData,listPos.get(j));
                if(!diffList.contains(configName)&&configName.length()>0){
                    diffList.add(configName);
                }
            }
        }

        //添加选中的项
        for (int i = 0; i < selectedItemName.size(); i++) {
            if(!diffList.contains(selectedItemName.get(i))){
                diffList.add(selectedItemName.get(i));
            }
        }
        //保留选中的项的状态
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < selectedItemName.size(); i++) {
            for (int j = 0; j < diffList.size(); j++) {
                if(diffList.get(j).equals(selectedItemName.get(i))){
                    set.add(j);
                }
            }
        }
        tagAdapterDiff.setSelectedList(set);

        //刷新参数选择
        tagAdapterDiff.notifyDataChanged();

    }

    public interface OnDiffSelectedItemListener{
         void onDiffSelectedItem(ArrayList<String> selectedItemPos);
    }

    public void setOnDiffSelectedItemListener(BMWCarDiffSelectView.OnDiffSelectedItemListener onDiffSelectedItemListener){
        this.onDiffSelectedItemListener = onDiffSelectedItemListener;
    }
}
