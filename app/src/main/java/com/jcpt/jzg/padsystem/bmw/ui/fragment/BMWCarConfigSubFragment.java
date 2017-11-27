package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.CarConfigSubAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.CarConfigSubModel;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.detection.ConfigureList;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BMWCarConfigSubFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    private DetectionWrapper detectionWrapper;
    private LinearLayoutManager linearLayoutManager;
    private CarConfigSubAdapter adapter;
    private String taskId;
    private CarConfigSubModel carConfigSubModel;
    private SubmitModel submitModel;
    private List<ConfigureList> listConfigure;
    private LinkedHashMap<String,String> configureSelectedMap;



    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //2
        View view = inflater.inflate(R.layout.fragment_car_config_sub, container, false);
        ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        if(listConfigure==null){
            return view;
        }
        adapter = new CarConfigSubAdapter(this.getContext(),listConfigure,carConfigSubModel.getSelectedConfigList());
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    protected void setView() {
        //3
    }
    @Override
    protected void initData() {
        //1
        configureSelectedMap = new LinkedHashMap <>();

        getPlanAndData();
    }

    private void getPlanAndData() {
        taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        submitModel = ((BMWDetectMainActivity)getActivity()).getSubmitModel();
        if(taskId==null||submitModel==null){
            return;
        }
        //选中项数据
        carConfigSubModel = new CarConfigSubModel();

        //获取方案
        detectionWrapper = ((BMWDetectMainActivity)this.getActivity()).getWrapper();
        if(detectionWrapper==null){
            return;
        }
        listConfigure = detectionWrapper.getConfigureList();

        //Test
/*        List<ConfigureList.ConfigureValueListBean> tagList = listConfigure.get(0).getConfigureValueList();
        for (int j = 0; j < 10; j++) {
            tagList.add(tagList.get(0));
        }*/

        //从本地数据库中获取此用户下的taskId
        List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_CAR_TYPE_CONFIG, PadSysApp.getUser().getUserId());
        if(list!=null&&list.size()>0){
            String json = list.get(0).getJson();
            if(!TextUtils.isEmpty(json)){
                carConfigSubModel = new Gson().fromJson(json,CarConfigSubModel.class);
            }
        }
        //判断这个任务是否存在检测详情数据
        TaskDetailModel taskDetailModel = ((BMWDetectMainActivity)this.getActivity()).getTaskDetailModel();
        if(taskDetailModel!=null){
            List<String> carConfigList =  taskDetailModel.getConfigureList();
            if(carConfigList!=null) {

                //重新为submitModel赋上检测详情数据的值
                submitModel.setConfigureValueList(carConfigList);
                carConfigSubModel.setSelectedConfigList((ArrayList<String>) carConfigList);

            }
        }
    }

    /**
     * 检查并保存数据
     * @param isCheck 提交时不可为空的项是否都已经填写是否检查
     * @param isNextCheck 必填项是否检查
     */
    public boolean checkAndSaveData(boolean isCheck,boolean isNextCheck,String hintHead){
        //查看方案中是否包含“查看配置”，如果有，判断数据是否都填写
        if(detectionWrapper==null){
            //获取方案
            detectionWrapper = PadSysApp.wrapper;
            if(detectionWrapper==null){
                //MyToast.showShort("获取方案出错");
                return false;
            }
        }
        if(adapter==null){//此Fragment还没初始化
            return true;
        }


        //获取选中的项
        ArrayList<String> selectedTagIdList = adapter.getSelectedTagId();
        if(selectedTagIdList==null){
            return false;
        }
        //得到所有组的配置项
        if(!isCheck&&!isNextCheck){
        }else {
            for (int i = 0; i < listConfigure.size(); i++) {
                List<ConfigureList.ConfigureValueListBean> tagList = listConfigure.get(i).getConfigureValueList();

                boolean hasTagId = false;
                for (int j = 0; j < tagList.size(); j++) {
                    String tagId = tagList.get(j).getConfigureValueId();
                    if (selectedTagIdList.contains(tagId)) {
                        hasTagId = true;
                        break;
                    } else {
                        continue;
                    }
                }
                if (!hasTagId) {//本组中一个也没选
                    if (isCheck) {
                        MyToast.showShort("您还有未查看的配置，请完成");
                        return false;
                    }
                    if (isNextCheck) {
                        MyToast.showShort("您还有未查看的配置，请完成");
                        return false;
                    }
                }
            }

            //生成展示用的选中的配置项
            configureSelectedMap.clear();
            for (int i = 0; i < listConfigure.size(); i++) {

                StringBuilder sb = new StringBuilder();
                List<ConfigureList.ConfigureValueListBean> tagList = listConfigure.get(i).getConfigureValueList();
                for (int j = 0; j < tagList.size(); j++) {
                    String tagId = tagList.get(j).getConfigureValueId();
                    if (selectedTagIdList.contains(tagId)) {
                        sb.append(tagList.get(j).getConfigureValueName() + ",");
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
                configureSelectedMap.put(listConfigure.get(i).getConfigureName(), sb.toString());
            }
        }

        submitModel.setConfigureSelectedMap(configureSelectedMap);
        submitModel.setConfigureValueList(selectedTagIdList);
        carConfigSubModel.setSelectedConfigList(selectedTagIdList);

        String json = new Gson().toJson(carConfigSubModel);
        //保存到数据库
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_CAR_TYPE_CONFIG, taskId,PadSysApp.getUser().getUserId(),json);
        return true;
    }

    /**
     * 检查
     * @param isShowHint 没填写完是否显示提示信息，true显示，false不显示
     * @return 要填的项都填完返回true,反之返回false
     */
    public boolean check(boolean isShowHint){
        //查看方案中是否包含“查看配置”，如果有，判断数据是否都填写
        if(detectionWrapper==null){
            //获取方案
            detectionWrapper = PadSysApp.wrapper;
            if(detectionWrapper==null){
                //MyToast.showShort("获取方案出错");
                return false;
            }
            listConfigure = detectionWrapper.getConfigureList();
            if(listConfigure==null||listConfigure.size()==0){
                //如果方案里不包含“查看配置”
                return true;
            }
        }
        if(adapter==null){//此Fragment还没初始化
            return true;
        }

        //获取选中的项
        ArrayList<String> selectedTagIdList = adapter.getSelectedTagId();
        if(selectedTagIdList==null){
            return false;
        }
        //得到所有组的配置项
        for (int i = 0; i < listConfigure.size(); i++) {
            List<ConfigureList.ConfigureValueListBean> tagList = listConfigure.get(i).getConfigureValueList();

            boolean hasTagId = false;
            for (int j = 0; j < tagList.size(); j++) {
                String tagId = tagList.get(j).getConfigureValueId();
                if (selectedTagIdList.contains(tagId)) {
                    hasTagId = true;
                    break;
                } else {
                    continue;
                }
            }
            if (!hasTagId) {//本组中一个也没选
                if (isShowHint) {
                    MyToast.showShort("您还有未查看的配置，请完成");
                }
                return false;
            }
        }

        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        checkAndSaveData(false,false,"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkAndSaveData(false,false,"");
    }
}
