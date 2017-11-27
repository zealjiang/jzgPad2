package com.jcpt.jzg.padsystem.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.OtherTypeAdapter;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 方案 ConfigureStatus 0表示没有查看配置，1 表示有
 */
public class CarSelectFragment extends BaseFragment{

    @BindView(R.id.rv)
    RecyclerView recyclerView;


    OtherTypeAdapter otherTypeAdapter;
    CarSelectSubFragment carSelectSubFragment;
    CarConfigSubFragment carConfigSubFragment;

    List<String> tagLists = new ArrayList<String>();
    private SparseArray<Fragment> fragmentSparseArray;
    private final String StrConfig = "配置查看";

    @Override
    protected void setView() {
        init();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_select, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    protected void initData() {
        fragmentSparseArray = new SparseArray<>();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            carSelectSubFragment.setUserVisibleHint(carSelectSubFragment.isVisible());
            carConfigSubFragment.setUserVisibleHint(carConfigSubFragment.isVisible());
        }
    }

    public void init(){
        carSelectSubFragment = new CarSelectSubFragment();
        carConfigSubFragment = new CarConfigSubFragment();

        //添加到list中
        fragmentSparseArray.put(0,carSelectSubFragment);
        tagLists.add("车型选择");
        addFragmentToStack(carConfigSubFragment);
        addFragmentToStack(carSelectSubFragment);
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(carConfigSubFragment);
        fragmentTransaction.show(carSelectSubFragment);
        fragmentTransaction.commitAllowingStateLoss();


        //获取方案 判断是否有配置项
        DetectionWrapper detectionWrapper = ((DetectMainActivity)this.getActivity()).getWrapper();
        if(detectionWrapper!=null){
            int status = detectionWrapper.getConfigureStatus();
            if(status == 1){
                //有
                fragmentSparseArray.put(1,carConfigSubFragment);
                tagLists.add(StrConfig);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        otherTypeAdapter = new OtherTypeAdapter(context,tagLists);
        recyclerView.setAdapter(otherTypeAdapter);

        otherTypeAdapter.setOnItemClickListener(new OtherTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if(otherTypeAdapter.getCurrPosition()==position)
                    return;
                otherTypeAdapter.setCurrPosition(position);
                if(position == 0){
                    switchFragment(0);
                }else if(position == 1){
                    switchFragment(1);
                }
            }
        });

    }


    /**
     * 此种显示和隐藏fragment的方式要比replace 重新初始化fragment生命周期要好，但内存占用上会大
     * @param fragment
     */
    private void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =   manager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.flContent, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void switchFragment(int fragmentId) {
        Fragment curFragment = fragmentSparseArray.get(fragmentId);
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        if (curFragment.isAdded()) {
        } else {
            fragmentTransaction.add(R.id.flContent, curFragment);
        }

        fragmentTransaction.show(curFragment);
        for (int i = 0; i < fragmentSparseArray.size(); i++) {
            if(fragmentId==i){
                continue;
            }
            fragmentTransaction.hide(fragmentSparseArray.get(i));
        }

        fragmentTransaction.commitAllowingStateLoss();
    }


    @OnClick({R.id.crbNext})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.crbNext:
                //判断是否有查看配置
                if(tagLists.contains(StrConfig)){
                    //如果是在车型选择界面点击的“下一步”直接跳转到查看配置
                    if(otherTypeAdapter.getCurrPosition()==0){
                        otherTypeAdapter.setCurrPosition(1);
                        switchFragment(1);
                        break;
                    }
                    nextToDetect(2);
                }else{
                    nextToDetect(2);
                }

                break;
        }
    }

    public boolean checkAndSaveData(boolean isCheck,boolean isNextCheck){
        //判断车型选择和查看配置在进入下一步前必填项是否都已经填写上
        //车型选择
        boolean booCar = carSelectSubFragment.checkAndSaveData(isCheck);
        if(!booCar){
            return false;
        }
        //查看配置
        if(tagLists.contains(StrConfig)) {
            boolean booConfig = carConfigSubFragment.checkAndSaveData(isCheck, isNextCheck, "");
            if (!booConfig) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param isShowHint 没填写完是否显示提示信息，true显示，false不显示
     * @return 要填的项都填完返回true,反之返回false
     */
    public boolean check(boolean isShowHint){
        //判断车型选择和查看配置在进入下一步前必填项是否都已经填写上
        //车型选择
        boolean booCar = carSelectSubFragment.check(isShowHint);
        if(!booCar){
            return false;
        }
        //查看配置
        if(tagLists.contains(StrConfig)) {
            boolean booConfig = carConfigSubFragment.check(isShowHint);
            if (!booConfig) {
                return false;
            }
        }
        return true;
    }


    /**
     * 检查必填项并跳转到车况检测
     * @param index 当前点击的哪个tab 手续信息为0 以此后推
     * @return 返回false表示车型选择已填完，但点击的是其他信息
     * @author zealjiang
     * @time 2016/12/9 13:46
     */
    public boolean nextToDetect(int index){
        boolean isSaveSuc = checkAndSaveData(true,true);
        if(isSaveSuc){
            //统计车型选择结束时间
            ((DetectMainActivity) this.getActivity()).recordCarSelectET();

            //设置车况检测和车辆照片tabView可点击
            ((DetectMainActivity)CarSelectFragment.this.getActivity()).remove(2);
            ((DetectMainActivity)CarSelectFragment.this.getActivity()).remove(3);

            if(index==2||index==3) {
                //统计车况检测开始时间
                ((DetectMainActivity) this.getActivity()).recordDetectionST();

                //跳转到车况检测
                ((DetectMainActivity) this.getActivity()).skipToFragment(index);
            }
            return true;
        }else{
            //设置车况检测和车辆照片tabView不可点击
            ((DetectMainActivity)CarSelectFragment.this.getActivity()).add(2);
            ((DetectMainActivity)CarSelectFragment.this.getActivity()).add(3);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        checkAndSaveData(false,false);
    }
}
