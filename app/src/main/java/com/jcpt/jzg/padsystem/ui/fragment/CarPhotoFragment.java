package com.jcpt.jzg.padsystem.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.OtherTypeAdapter;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.interfaces.ICustomCameraListener;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  车辆照片
 */
public class CarPhotoFragment extends BaseFragment implements ICustomCameraListener{

    @BindView(R.id.rvOtherType)
    RecyclerView rvOtherType;

    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;

    OtherTypeAdapter otherTypeAdapter;

    CarPhotoProdedureFragment carPhotoProdedureFragment;
    CarPhotoBaseFragment carPhotoBaseFragment;
    CarPhotoDefectFragment carPhotoDefectFragment;
    CarPhotoSubjoinFragment carPhotoSubjoinFragment;
    private DetectionWrapper detectionWrapper;
    List<String> otherTypeLists = new ArrayList<String>();

    private boolean configDriving; //行驶证照片 81
    private boolean configRegister;//登记证照片 82
    @Override
    protected void setView() {
        init();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_photo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            carPhotoBaseFragment.setUserVisibleHint(carPhotoBaseFragment.isVisible());
            carPhotoDefectFragment.setUserVisibleHint(carPhotoDefectFragment.isVisible());

            if ((configDriving || configRegister) && carPhotoProdedureFragment!=null) {
                carPhotoProdedureFragment.setShowRegistration(((DetectMainActivity)getActivity()).isShowRegistration());
                carPhotoProdedureFragment.setShowDriverLicense(((DetectMainActivity)getActivity()).isShowDriverLicense());
                carPhotoProdedureFragment.setUserVisibleHint(carPhotoProdedureFragment.isVisible());
            }

        }
    }

    public void init(){
        ((DetectMainActivity) context).setICustomCameraListener(CarPhotoFragment.this);
        carPhotoProdedureFragment = new CarPhotoProdedureFragment();
        carPhotoBaseFragment = new CarPhotoBaseFragment();
        carPhotoDefectFragment = new CarPhotoDefectFragment();
        carPhotoSubjoinFragment = new CarPhotoSubjoinFragment();

        detectionWrapper = ((DetectMainActivity)this.getActivity()).getWrapper();

        List<String> configList =  detectionWrapper.getProcedureList();
        for (int i = 0; i < configList.size(); i++) {
            switch (configList.get(i)) {
                case "81":
                    configDriving = true;
                    break;
                case "82":
                    configRegister = true;
                    break;
            }
        }

        if (configDriving||configRegister) {
            addFragmentToStack(carPhotoProdedureFragment);
            otherTypeLists.add("手续照片");
        }else {
            addFragmentToStack(carPhotoBaseFragment);
        }

        otherTypeLists.add("基本照片");
        otherTypeLists.add("缺陷照片");
        otherTypeLists.add("附加照片");
        rvOtherType.setLayoutManager(new LinearLayoutManager(context));
        otherTypeAdapter = new OtherTypeAdapter(context,otherTypeLists);
        rvOtherType.setAdapter(otherTypeAdapter);

        otherTypeAdapter.setOnItemClickListener(new OtherTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if(otherTypeAdapter.getCurrPosition()==position)
                    return;
                otherTypeAdapter.setCurrPosition(position);

                if (configDriving||configRegister) {

                    if (position == 0) {
                        addFragmentToStack(carPhotoProdedureFragment);
                    } else if (position == 1) {
                        addFragmentToStack(carPhotoBaseFragment);
                    } else if (position == 2) {
                        addFragmentToStack(carPhotoDefectFragment);
                    } else if (position == 3) {
                        addFragmentToStack(carPhotoSubjoinFragment);

                    }

                }else {
                    if (position == 0) {
                        addFragmentToStack(carPhotoBaseFragment);
                    } else if (position == 1) {
                        addFragmentToStack(carPhotoDefectFragment);
                    } else if (position == 2) {
                        addFragmentToStack(carPhotoSubjoinFragment);
                    }
                }
            }
        });
    }


    private void addFragmentToStack(Fragment fragment) {
            manager = context.getSupportFragmentManager();
            fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.flCarPhoto, fragment);
            fragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.txPhotoNext})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.txPhotoNext:
                if(isComplete() && ((DetectMainActivity)getActivity()).isDetectionComplete()){
                    //统计车况检测结束时间
                    ((DetectMainActivity)getActivity()).recordDetectionET();

                    ((DetectMainActivity)getActivity()).skipToFragment(4);
                }
                break;
        }


    }

    public boolean isComplete(){
        boolean isComplete = false;

        if (configDriving||configRegister) {
            if(!carPhotoProdedureFragment.isFinish()){
                showConfirmTask("手续照片",0);
                isComplete = false;
            }else if(!carPhotoBaseFragment.isFinish()){
                showConfirmTask("基本照片",1);
                isComplete = false;
            }else{
                isComplete = true;
            }
        }else {
            if(!carPhotoBaseFragment.isFinish()){
                showConfirmTask("基本照片",0);
                isComplete = false;
            }else{
                isComplete = true;
            }
        }

        return  isComplete;
    }


    /**
     * 确认任务信息
     */
    public void showConfirmTask(String photoMsg, final int position){
        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(getActivity());
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_photo_incomplete);
        TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        tvMsg.setText(photoMsg + "拍摄不全，需补全");
        TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
        tvleft.setText("取消");
        TextView tvright = (TextView) view.findViewById(R.id.tvright);
        tvright.setText("现在补拍"+photoMsg);
        myUniversalDialog.setLayoutView(view);
        myUniversalDialog.setLayoutHeightWidth(720, 1280);
        myUniversalDialog.show();
        tvleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
            }
        });
        tvright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
                otherTypeAdapter.setCurrPosition(position);
                if (configDriving||configRegister) {
                    if (position == 0) {
                        addFragmentToStack(carPhotoProdedureFragment);
                    } else {
                        addFragmentToStack(carPhotoBaseFragment);
                    }
                }else {
                    addFragmentToStack(carPhotoBaseFragment);
                }


            }
        });

    }

    @Override
    public void setPhoto(Intent data) {

    }

    @Override
    public void recapturePhoto(Intent data) {

    }
}
