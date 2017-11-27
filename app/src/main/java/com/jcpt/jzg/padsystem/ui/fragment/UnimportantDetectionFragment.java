package com.jcpt.jzg.padsystem.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.DetectionItemAdapter;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.model.DetectionModel;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  * 作者：zyq
 *  * 邮箱：zhengyq@jingzhengu.com
 *  车况检测 ----非重点检测项
 */
public class UnimportantDetectionFragment extends BaseFragment{



    @BindView(R.id.rvNoKeyPointOptions)
    RecyclerView rvNoKeyPointOptions;

    @BindView(R.id.crbtnNextStep)
    CustomRippleButton crbtnNextStep;

    private DetectionItemAdapter myAdapter;

    /**
     * Created by 李波 on 2016/11/28.
     * CheckPositionList 方位集合下面 非重点检测项分支
     */
    private ImportantItem item;

    /**
     * Created by 李波 on 2016/11/29.
     * 重点项分支 用于点击下一步时判断所有重点项是否已选择
     */
    private ImportantItem importantItem;
    /**
     * Created by 李波 on 2016/11/28.
     * ImportantItem 非重点 里 各个检测项
     */
    private List<CheckItem> checkItemList;

    /**
     * Created by 李波 on 2016/11/28.
     * 检测项 Model
     */
    private DetectionModel detectionModel;
    private String taskId;
    public static boolean isLastItem;




    /**
     * Created by 李波 on 2016/11/28.
     * 点击下一步的回调监听
     */
    private INextStepListener iNextStepListener;

    public void setiNextStepListener(INextStepListener iNextStepListener){
        this.iNextStepListener = iNextStepListener;
    }

    @Override
    protected void initData() {
        taskId = ((DetectMainActivity) getActivity()).getTaskid();
        detectionModel = new DetectionModel(context);
        detectionModel.setTaskId(taskId);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detection_unimportant, container, false);
        ButterKnife.bind(this, view);
        LogUtil.e(TAG,getActivity().toString());
        return view;
    }



    @Override
    protected void setView() {
        rvNoKeyPointOptions.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rvNoKeyPointOptions.addItemDecoration(new GridSpacingItemDecoration(2,30,false));
    }

    public ImportantItem getItem() {
        return item;
    }

    public void setItem(ImportantItem item) {
        this.item = item;
        if(isActivityCreated)
            updateData();
    }

    public void setImportantItem(ImportantItem importantItem) {
        this.importantItem = importantItem;
    }

    private boolean isActivityCreated = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateData();
        isActivityCreated = true;
    }

    private void updateData(){
        if(item!=null){
            checkItemList = item.getCheckItemList();
            if(checkItemList==null)
                checkItemList = new ArrayList<>();
            if(myAdapter==null){

                myAdapter = new DetectionItemAdapter(getActivity(),checkItemList);
                rvNoKeyPointOptions.setAdapter(myAdapter);

                myAdapter.setLisenter(new DetectionItemAdapter.IDetectionOnclikLister() {
                    @Override //pos 是当前点击的item位置角标，position是item条目点击位置：1-左边，2-右边 - - - 李波
                    public void OnClick(int pos, int position) {
                        //获取当前点击的检测项状态：0-未选中，1-正常，2-缺陷  -> 李波 on 2016/11/24.
                        int status  = myAdapter.getPositionCheckItem(pos).getStatus();
                        switch (position) {
                            case 1:   //点击检测项 - - 左边放大镜弹出右侧侧滑缺陷项popwindow - - 李波
                                detectionModel.showDefectPopWindow(checkItemList,pos);
                                detectionModel.setPopupWindowListener(pos,myAdapter);
                                break;
                            case 2:   //点击检测项 - - 右边对勾 - -李波
//                                MyToast.showShort("右边");
                                if (status==2){
                                    MyToast.showShort("此检测项处于缺陷状态");
                                }else if (status==0){  //如果是未选中状态，置为 1-- OK 蓝色状态
                                    detectionModel.changeStatusAndData(myAdapter, pos,1);
                                    setFixStatus();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });

            }else{
                myAdapter.updateData(checkItemList);
            }

        }
    }

    @OnClick({R.id.crbtnNextStep})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crbtnNextStep:

//                int status = DetectionModel.getImportantSelStatus(importantItem.getCheckItemList());
//
//                if (status == 0){
//                    MyToast.showLong("您还有未检测项的重点项");
//                }else {
                    detectionModel.saveUseTaskToDB();
                    iNextStepListener.nextStep(0);//此position无实际用途
//                }
                break;
        }
    }

    public RecyclerView getRvNoKeyPointOptions() {
        return rvNoKeyPointOptions;
    }

    private void setFixStatus(){
        DetectionFragment.detectionIsFix = true;
    }

    /**
     * created by wujj on 2017/2/14
     * 是否显示下一步按钮
     */
    public void showNextStep(){
        if (isLastItem){
            crbtnNextStep.setVisibility(View.VISIBLE);
        }else {
            crbtnNextStep.setVisibility(View.GONE);
        }
    }
}
