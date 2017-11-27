package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.interfaces.ISaveListener;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.fragment.DetectionFragment;
import com.jcpt.jzg.padsystem.ui.fragment.OtherInformationFragment;
import com.jcpt.jzg.padsystem.ui.fragment.OtherSubjoinInformationFragment;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.vo.SubmitModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * BMW其他信息
 */
public class BMWOtherFragment extends BaseFragment implements INextStepListener {
    @BindView(R.id.rvOtherType)
    RecyclerView rvOtherType;
    OtherTypeAdapter otherTypeAdapter;
    List<String> otherTypeLists = new ArrayList<String>();
    private int currIndex;
    private SparseArray<Fragment> mfragArray;
    private ISaveListener iSaveListener = new ISaveListener() {
        @Override
        public void saveData() {
        }
    };
    //其他检查页面
    private BMWOtherCheckFragment bmwOtherCheckFragment;
    //轮胎轮毂页面
    private BMWTireStopFragment bmwTireStopFragment;
    //随车附件页面
    private BMWCarGoodsFragment bmwCarGoodsFragment;
    //补充说明页面
    private BMWExplainFragment bmwExplainFragment;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            int curPosition = otherTypeAdapter.getCurrPosition();
            if (curPosition == 0 && bmwOtherCheckFragment != null) {
                bmwOtherCheckFragment.setUserVisibleHint(true);
            }
        } else {
            if (bmwOtherCheckFragment != null) {
                bmwOtherCheckFragment.setUserVisibleHint(false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void setView() {
        init();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        //其他检查
        bmwOtherCheckFragment = new BMWOtherCheckFragment();
        //轮胎轮毂
        bmwTireStopFragment = new BMWTireStopFragment();
        //随车附件
        bmwCarGoodsFragment = new BMWCarGoodsFragment();
        //补充说明
        bmwExplainFragment = new BMWExplainFragment();
        mfragArray = new SparseArray<Fragment>();
        mfragArray.put(0, bmwOtherCheckFragment);
        mfragArray.put(1, bmwTireStopFragment);
        mfragArray.put(2, bmwCarGoodsFragment);
        mfragArray.put(3, bmwExplainFragment);
        bmwOtherCheckFragment.setOnNextStepListener(this);
        bmwTireStopFragment.setOnNextStepListener(this);
        bmwCarGoodsFragment.setOnNextStepListener(this);
    }

    public void init() {
        otherTypeLists.add("其他检查");
        otherTypeLists.add("轮胎轮毂");
        otherTypeLists.add("随车附件");
        otherTypeLists.add("补充说明");
        addFragmentToStack(bmwOtherCheckFragment);
        addFragmentToStack(bmwTireStopFragment);
        addFragmentToStack(bmwCarGoodsFragment);
        addFragmentToStack(bmwExplainFragment);
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(bmwOtherCheckFragment);
        fragmentTransaction.hide(bmwTireStopFragment);
        fragmentTransaction.hide(bmwCarGoodsFragment);
        fragmentTransaction.hide(bmwExplainFragment);
        fragmentTransaction.commitAllowingStateLoss();


        rvOtherType.setLayoutManager(new LinearLayoutManager(context));
        otherTypeAdapter = new OtherTypeAdapter(context, otherTypeLists);
        rvOtherType.setAdapter(otherTypeAdapter);
        otherTypeAdapter.setOnItemClickListener(new OtherTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (otherTypeAdapter.getCurrPosition() == position)
                    return;
                changeChildFragment(position);
            }
        });
    }

    /**
     * 切换子fragment
     *
     * @param position
     */
    public void changeChildFragment(int position) {
        otherTypeAdapter.setCurrPosition(position);
        currIndex = position;
        switchFragment(position);
    }

    private void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.other_information_fragments, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    /**
     * 切换fragment
     *
     * @param
     * @param flag 对应索引
     */
    private void switchFragment(int flag) {
        showTab(flag);
    }

    private void showTab(int idx) {
        for (int i = 0; i < mfragArray.size(); i++) {
            Fragment fragment = mfragArray.get(i);
            FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
            if (idx == i) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.hide(fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    /**
     * 检查不可为空项是否都已填写并保存
     *
     * @return
     */
    public boolean checkAndSaveData() {
        return bmwOtherCheckFragment.checkAndSaveData() && bmwTireStopFragment.checkAndSaveData() &&
                bmwCarGoodsFragment.checkAndSaveData() && bmwExplainFragment.checkAndSaveData();
    }

    /**
     * 点击下一步
     */
    @Override
    public void nextStep(int position) {
        changeChildFragment(position);
    }

}
