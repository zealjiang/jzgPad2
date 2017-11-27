package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.DetectionItemAdapter;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.model.BMWDetectionModel;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWSearchCheckActivity;
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.model.DetectionModel;
import com.jcpt.jzg.padsystem.ui.activity.SearchCheckActivity;
import com.jcpt.jzg.padsystem.ui.fragment.DetectionFragment;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.GridSpacingItemDecoration;
import com.jcpt.jzg.padsystem.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *  * 作者：zyq
 *  * 邮箱：zhengyq@jingzhengu.com
 *  车况检测 ----重点检测项
 */
public class BMWImportantDetectionFragment extends BaseFragment {

    private final int CHECKSEARCH = 100;

    /**
     * Created by 李波 on 2016/11/24.
     * 重点检测项
     */
    @BindView(R.id.rvOptions)
    RecyclerView rvOptions;

    @BindView(R.id.UnimportantRvOptions)
    RecyclerView UnimportantRvOptions;

    @BindView(R.id.crbtnNextStep)
    CustomRippleButton crbtnNextStep;
    @BindView(R.id.tv_important)
    TextView tv_important;
    @BindView(R.id.tv_un_important)
    TextView tv_un_important;

    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.pulltorefreshview)
    PullToRefreshView pulltorefreshview;

    /**
     * Created by 李波 on 2016/11/24.
     * 重点检测项的 adapter
     */
    private DetectionItemAdapter myAdapter;
    private DetectionItemAdapter UnmyAdapter;

    /**
     * Created by 李波 on 2016/11/24.
     * CheckPositionList 方位集合下面 重点检查项分支
     */
    private ImportantItem item;
    private ImportantItem unImportantItem;

    /**
     * Created by 李波 on 2016/11/24.
     * ImportantItem 重点 里各个方位对应大照片集合
     * 对应检测项上面横向的一排 照片
     */
//    private ArrayList<PictureItem> pictureItems;
    /**
     * Created by 李波 on 2016/11/24.
     * ImportantItem 重点 里 各个检测项
     */
    private List<CheckItem> checkItemList;
    private List<CheckItem> UncheckItemList;

    /**
     * Created by 李波 on 2016/11/24.
     * 检查项 Model
     */
    private BMWDetectionModel detectionModel;

    private CheckPositionItem checkPositionItem;

    private String taskId;

    public boolean isLastItem;

    //初始化检测项
    @Override
    protected void initData() {
        taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        detectionModel = new BMWDetectionModel(context);
        detectionModel.setTaskId(taskId);

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detection_important, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    //初始化数据
    @Override
    protected void setView() {
        //重点检测项
        rvOptions.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvOptions.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        rvOptions.setNestedScrollingEnabled(false);
        UnimportantRvOptions.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        UnimportantRvOptions.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        UnimportantRvOptions.setNestedScrollingEnabled(false);

        init();

    }

    private void init() {

        pulltorefreshview.setOnSearchListener(new PullToRefreshView.OnSearchListener() {
            @Override
            public void onSearch() {
                Intent i = new Intent(getActivity(), BMWSearchCheckActivity.class);
                startActivityForResult(i, CHECKSEARCH);
            }
        });

    }

    public ImportantItem getItem() {
        return item;
    }

    public void setItem(ImportantItem item, CheckPositionItem checkPositionItem, ImportantItem
            unImportantItem) {
        this.item = item;
        this.checkPositionItem = checkPositionItem;
        this.unImportantItem = unImportantItem;

        if (isActivityCreated) updateData();

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateData();
        isActivityCreated = true;
    }

    private boolean isActivityCreated = false;

    private void updateData() {

        if (checkPositionItem != null) {

            //重点  -> 李波 on 2017/8/25.
            checkItemList = item.getCheckItemList();

            if (checkItemList == null) checkItemList = new ArrayList<>();
            if (checkItemList.size() > 0) {
                tv_important.setText("重点(" + checkItemList.size() + ")");
                tv_important.setVisibility(View.VISIBLE);
            } else {
                tv_important.setVisibility(View.GONE);
            }

            if (myAdapter == null) {
                myAdapter = new DetectionItemAdapter(getActivity(), checkItemList);
                rvOptions.setAdapter(myAdapter);

                myAdapter.setLisenter(new DetectionItemAdapter.IDetectionOnclikLister() {
                    @Override //pos 是当前点击的item位置角标，position是item条目点击位置：1-左边，2-右边 - - - 李波
                    public void OnClick(int pos, int position) {
                        //获取当前点击的检测项状态：0-未选中，1-正常，2-缺陷  -> 李波 on 2016/11/24.
                        int status = myAdapter.getPositionCheckItem(pos).getStatus();
                        switch (position) {
                            case 1:   //点击检测项 - - 弹出右侧侧滑缺陷项popwindow - - 李波
                                detectionModel.showDefectPopWindow(checkItemList, pos);
                                detectionModel.setPopupWindowListener(pos, myAdapter);
                                DetectionFragment.detectionIsFix = true;
                                break;
                            case 2:   //点击检测项 - - 右边对勾 - -李波
//                                MyToast.showShort("右边");
                                if (status == 2) {
                                    MyToast.showShort("此检测项处于缺陷状态");
                                } else if (status == 0) {  //如果是未选中状态，置为 1-- OK 蓝色状态
                                    detectionModel.changeStatusAndData(myAdapter, pos, 1);
                                    setFixStatus();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });

            } else {
                myAdapter.updateData(checkItemList);
            }


            //非重点  -> 李波 on 2017/8/25.
            UncheckItemList = unImportantItem.getCheckItemList();

            if (UncheckItemList == null) UncheckItemList = new ArrayList<>();
            if (UncheckItemList.size() > 0) {
                tv_un_important.setText("非重点(" + UncheckItemList.size() + ")");
                tv_un_important.setVisibility(View.VISIBLE);
            } else {
                tv_un_important.setVisibility(View.GONE);
            }

            if (UnmyAdapter == null) {


                UnmyAdapter = new DetectionItemAdapter(getActivity(),UncheckItemList);
                UnimportantRvOptions.setAdapter(UnmyAdapter);

                UnmyAdapter.setLisenter(new DetectionItemAdapter.IDetectionOnclikLister() {
                    @Override //pos 是当前点击的item位置角标，position是item条目点击位置：1-左边，2-右边 - - - 李波
                    public void OnClick(int pos, int position) {
                        //获取当前点击的检测项状态：0-未选中，1-正常，2-缺陷  -> 李波 on 2016/11/24.
                        int status = UnmyAdapter.getPositionCheckItem(pos).getStatus();
                        switch (position) {
                            case 1:   //点击检测项 - - 左边放大镜弹出右侧侧滑缺陷项popwindow - - 李波
                                detectionModel.showDefectPopWindow(UncheckItemList, pos);
                                detectionModel.setPopupWindowListener(pos, UnmyAdapter);
                                break;
                            case 2:   //点击检测项 - - 右边对勾 - -李波
//                                MyToast.showShort("右边");
                                if (status == 2) {
                                    MyToast.showShort("此检测项处于缺陷状态");
                                } else if (status == 0) {  //如果是未选中状态，置为 1-- OK 蓝色状态
                                    detectionModel.changeStatusAndData(UnmyAdapter, pos, 1);
                                    setFixStatus();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });


            } else {
                UnmyAdapter.updateData(UncheckItemList);
            }
        }
    }

    private INextStepListener iNextStepListener;

    public void setiNextStepListener(INextStepListener iNextStepListener) {
        this.iNextStepListener = iNextStepListener;
    }

    @OnClick({R.id.crbtnNextStep})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crbtnNextStep:
                detectionModel.saveUseTaskToDB();
                iNextStepListener.nextStep(0);//此position无实际用途
                break;
        }
    }




    public ScrollView getScrollView(){
        return scrollView;
    }

    public PullToRefreshView getPulltorefreshview(){
        return pulltorefreshview;
    }

    private void setFixStatus() {
        DetectionFragment.detectionIsFix = true;
    }


    /**
     * created by wujj on 2017/2/14
     * 是否显示下一步按钮
     */
    public void showNextStep() {
        if (crbtnNextStep != null) {
            if (isLastItem) {
                crbtnNextStep.setVisibility(View.VISIBLE);
            } else {
                crbtnNextStep.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECKSEARCH) {
            //搜索界面操作完回到此页面后刷新最新数据
            myAdapter.notifyDataSetChanged();
            UnmyAdapter.notifyDataSetChanged();
        }
    }

}
