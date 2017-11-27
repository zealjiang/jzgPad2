package com.jcpt.jzg.padsystem.ui.fragment;

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
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.interfaces.ISaveListener;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.vo.SubmitModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  * 作者：zyq
 *  * 邮箱：zhengyq@jingzhengu.com
 *  
 */

/**
 * Created by libo on 2016/11/11.（自此创建时间开始由李波接管此界面）
 *
 * @Email: libo@jingzhengu.com
 * @Description: 其他信息
 */
public class OtherFragment extends BaseFragment implements INextStepListener {


    @BindView(R.id.rvOtherType)
    RecyclerView rvOtherType;

    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;

    OtherTypeAdapter otherTypeAdapter;

    //信息核对界面
    OtherInformationFragment otherInformationFragment;
    //评估结果
    OtherSubjoinInformationFragment otherSubjoinInformationFragment;

    List<String> otherTypeLists = new ArrayList<String>();

    private int currIndex;
    private SparseArray<Fragment> mfragArray;

    private ISaveListener iSaveListener = new ISaveListener() {
        @Override
        public void saveData() {
            savaData();
//            MyToast.showShort("保存成功");//Created by wujj on 2016/12/8.
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            if (manager == null){
//                manager = context.getSupportFragmentManager();
//            }
//            otherInformationFragment = (OtherInformationFragment) manager.findFragmentByTag("otherInformationFragment");
//            otherSubjoinInformationFragment = (OtherSubjoinInformationFragment) manager.findFragmentByTag("otherSubjoinInformationFragment");
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            context.setOnListenerSaveData(iSaveListener);

            if (currIndex == 0 && otherInformationFragment != null) {
                DetectMainActivity.isShowOtherInformationFragment = true;//created by wujj on 2017/2/14
                otherInformationFragment.ifNoRegistLicenseProperty();//created by wujj on 2017/2/7
                otherInformationFragment.showRealTimeData();
            }
            if (currIndex == 1 && otherSubjoinInformationFragment != null) {//--->Created by wujj 2016/12/5
                DetectMainActivity.isShowOtherInformationFragment = false;//created by wujj on 2017/2/14
                //只有车况检测修改过的情况下才再去遍历每个检测项，进而去判断是否显示火烧、泡水红色提示信息
                if (DetectionFragment.detectionIsFix){//created by wujj on 2016/12/27
                    otherSubjoinInformationFragment.ifShowFireAndWaterInfo();//------>Created by wujj 2016/12/5
                }
            }

            if(otherSubjoinInformationFragment != null){
                //请求网络--历史评估价格
                SubmitModel submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();
                String vin = submitModel.getVin();
                otherSubjoinInformationFragment.setVin(vin);
            }

        } else {
            DetectMainActivity.isShowOtherInformationFragment = false;//created by wujj on 2017/2/14
            LogUtil.i(TAG, "-----------------OtherFragment--不可见---------------------");
//            OtherSubjoinInformationFragment.setIfClick(false);
            DetectionFragment.detectionIsFix = false;//created by wujj on 2016/12/27
            //只有不是点击修改进来的，不可见的时候才保存数据---------------------------Created by wujj on 2016/12/8.
            if (!DetectMainActivity.detectMainActivity.isModify())
                savaData();

        }

    }

    /**
     * Created by 李波 on 2016/12/7.
     * 保存其他信息数据 包括 核对 和 附加
     */
    public void savaData() {
        if (currIndex == 0 && otherInformationFragment != null) {
            otherInformationFragment.saveChange();
        }
        /**
         * Created by wujj on 2016/12/6
         * 只有修改内容后，切换不同tab的时候才保存附加信息的数据
         * isFix--是否有修改过内容的标记
         */
        if (currIndex == 1 && otherSubjoinInformationFragment != null) {//-------->Created by wujj 2016/12/5
            if (otherSubjoinInformationFragment.isFix()) {
                otherSubjoinInformationFragment.setFix(false);
                otherSubjoinInformationFragment.saveData();//--------------------->Created by wujj 2016/12/5
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        savaData();
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

        otherInformationFragment = new OtherInformationFragment();
        otherSubjoinInformationFragment = new OtherSubjoinInformationFragment();
        mfragArray = new SparseArray<Fragment>();
        mfragArray.put(0, otherInformationFragment);
        mfragArray.put(1, otherSubjoinInformationFragment);
        otherInformationFragment.setOnNextStepListener(this);
    }

    public void init() {

        otherTypeLists.add("信息核对");
        otherTypeLists.add("评估结果");

        addFragmentToStack(otherSubjoinInformationFragment);
        addFragmentToStack(otherInformationFragment);

        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(otherSubjoinInformationFragment);
        fragmentTransaction.show(otherInformationFragment);
        fragmentTransaction.commitAllowingStateLoss();


        rvOtherType.setLayoutManager(new LinearLayoutManager(context));
        otherTypeAdapter = new OtherTypeAdapter(context,otherTypeLists);
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
        if (!DetectMainActivity.detectMainActivity.isModify()){//----------Created by wujj on 2016/12/8.
            //新建情况下，信息核对--必填项未填的情况下点击附加信息不允许切换-----------Created by wujj on 2016/12/14.
//            if (position == 1){
//                boolean b = otherInformationFragment.DataIsNotAmpty();
//                if (!b){
//                    return;
//                }
//            }
            savaData();
        }
        otherTypeAdapter.setCurrPosition(position);
        currIndex = position;
        switchFragment(position);
    }


    private void addFragmentToStack(Fragment fragment,String flag) {
        if (manager == null){
            manager = context.getSupportFragmentManager();
        }
        fragmentTransaction = manager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.other_information_fragments, fragment,flag);
        }
        if (fragment == otherInformationFragment){
            fragmentTransaction.show(fragment);
        }else {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =   manager.beginTransaction();
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
//        Fragment fragment = mfragArray.get(flag);
//        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
//        fragment.onPause(); // 暂停当前tab
//        if (fragment.isAdded()) {
//            fragment.onResume(); // 启动目标tab的onResume()
//        } else {
//            if (flag == 0){
//                fragmentTransaction.add(R.id.other_information_fragments, fragment,"otherInformationFragment");
//            }else if (flag == 1){
//                fragmentTransaction.add(R.id.other_information_fragments, fragment,"otherSubjoinInformationFragment");
//            }
//        }
        showTab(flag);
//        fragmentTransaction.commit();
    }

    private void showTab(int idx) {
        if (idx == 1){
            DetectMainActivity.isShowOtherInformationFragment = false;
            otherSubjoinInformationFragment.ifShowFireAndWaterInfo();
        }else {
            DetectMainActivity.isShowOtherInformationFragment = true;
        }
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
        return otherInformationFragment.checkDataComplete() && otherSubjoinInformationFragment.checkAndSaveData();
    }


    /**
     * 点击下一步跳转到附加信息界面 角标置为 1
     */
    @Override
    public void nextStep(int position) {
        changeChildFragment(position);
    }


}
