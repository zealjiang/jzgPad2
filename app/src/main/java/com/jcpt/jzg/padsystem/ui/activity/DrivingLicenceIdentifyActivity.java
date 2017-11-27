package com.jcpt.jzg.padsystem.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.FileUtils;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.DrivingLicenceInfoAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.mvpview.IDrivingLicenceInf;
import com.jcpt.jzg.padsystem.mvpview.IVInChecked;
import com.jcpt.jzg.padsystem.presenter.DrivingLicenceIdentifyPresenter;
import com.jcpt.jzg.padsystem.presenter.VinCheckedPresenter;
import com.jcpt.jzg.padsystem.utils.ACache;
import com.jcpt.jzg.padsystem.utils.CarVINCheck;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.DrivingLicenceModel;
import com.jcpt.jzg.padsystem.vo.NameValueModel;
import com.jcpt.jzg.padsystem.vo.VinCheckedModel;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.zhy.base.adapter.recyclerview.DividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jcpt.jzg.padsystem.app.PadSysApp.getUser;
import static com.jcpt.jzg.padsystem.global.Constants.ROOT_DIR;

/**
 * 行驶证图片识别
 * Created by zealjiang on 2016/11/16 13:49.
 * Email: zealjiang@126.com
 */

public class DrivingLicenceIdentifyActivity extends BaseActivity implements IDrivingLicenceInf,IVInChecked {

    @BindView(R.id.sdv_driving_license)
    SimpleDraweeView sdvDrivingLicence;
    @BindView(R.id.rv_info)
    RecyclerView recyclerView;
    @BindView(R.id.ll_info)
    LinearLayout linearLayoutInfo;

    private LinearLayoutManager linearLayoutManager;
    private List<NameValueModel> listData;
    private DrivingLicenceIdentifyPresenter drivingLicenceIdentifyPresenter;
    private String pathDrivingDicence;
    private boolean isShowVIN;//是否显示VIN
    private DrivingLicenceModel drivingLicenceModel;
    private DrivingLicenceInfoAdapter adapter;
    private String urlFilePath = ROOT_DIR +"/temp.jpg";
    private VinCheckedPresenter vinCheckedPresenter;
    private String taskId;

    private int isAlertAccdient;
    private String accdientAlertMsg;
    private String vinCheckedFailMsg;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_driving_licence_identify);
        ButterKnife.bind(this);
        pathDrivingDicence = getIntent().getStringExtra("path");
        taskId = getIntent().getStringExtra("taskId");
        //isShowVIN = getIntent().getBooleanExtra("isShowVIN",true);
        isShowVIN = true;
    }

    @Override
    protected void setData() {
        setTitle("行驶证识别");
        listData = new ArrayList<>();
        if(isShowVIN){
            listData.add(new NameValueModel("VIN码",""));
        }
        listData.add(new NameValueModel("车牌号码",""));
/*        listData.add(new NameValueModel("车辆型号",""));
        listData.add(new NameValueModel("发动机号",""));*/

        init();
    }


    private void init(){
        drivingLicenceIdentifyPresenter = new DrivingLicenceIdentifyPresenter(this);
        if(TextUtils.isEmpty(pathDrivingDicence)) {
            pathDrivingDicence = "";
        }else {
            Uri uri = null;
            if (pathDrivingDicence.startsWith("http")){
                uri = Uri.parse(pathDrivingDicence);
                //下载到本地，为识别提供服务
                savePic(pathDrivingDicence);
            }else if(pathDrivingDicence.startsWith(Environment.getExternalStorageDirectory()+"")){
                uri = Uri.parse("file://"+pathDrivingDicence);
            }

            sdvDrivingLicence.setImageURI(uri);
            //sdvDrivingLicence.setImageURI(Uri.fromFile(new File(pathDrivingDicence)));
            //sdvDrivingLicence.setImageURI(Uri.parse(pathDrivingDicence));
        }

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new DrivingLicenceInfoAdapter(this,listData);
        recyclerView.setAdapter(adapter);

        requestDrivingLicence();
    }

    private void requestDrivingLicence(){
        if(!PadSysApp.networkAvailable){
            MyToast.showShort(getResources().getString(R.string.check_net));
            return;
        }
        if (pathDrivingDicence.startsWith("http")){
            if(!new File(urlFilePath).exists()){
                //下载到本地，为识别提供服务
                savePic(pathDrivingDicence);
            }
            pathDrivingDicence = urlFilePath;
        }
        drivingLicenceIdentifyPresenter.identifyDrivingLicence(pathDrivingDicence);
    }

    @OnClick({R.id.ivLeft,R.id.crb_identify,R.id.crb_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft://返回
                finish();
                break;
            case R.id.crb_identify://重新识别
                if(TextUtils.isEmpty(pathDrivingDicence)){
                    MyToast.showShort("请重新上传行驶证照片");
                    return;
                }
                requestDrivingLicence();
                break;
            case R.id.crb_save://保存识别结果并返回

                /**
                 * 先校验VIN，正确，则跳转到手续页
                 */
                String vin = listData.get(0).getValue();
                boolean isValid = CarVINCheck.getInstance().isVINValid(vin);
                if(isValid){
                    if (vinCheckedPresenter == null){
                        vinCheckedPresenter = new VinCheckedPresenter(DrivingLicenceIdentifyActivity.this);
                    }
                    vinCheckedPresenter.getVinCheckedResult(getUser().getUserId()+"", vin,taskId,null);
                }else {
                    MyToast.showLong("您输入的VIN有误，请重新输入");
                }
                break;
        }
    }

    private boolean check(){
        for (int i = 0; i < listData.size(); i++) {
            String value = listData.get(i).getValue();
            if(TextUtils.isEmpty(value)){
                MyToast.showShort("请重新识别或修改"+listData.get(i).getName()+"信息");
                return false;
            }
        }
        return true;
    }

    @Override
    public void succeed(DrivingLicenceModel drivingLicenceModel) {
        if(drivingLicenceModel==null){
            MyToast.showShort("行驶证识别失败，请重新拍照识别");
            return;
        }
        if(TextUtils.isEmpty(drivingLicenceModel.getPlateNo())||(isShowVIN&&TextUtils.isEmpty(drivingLicenceModel.getVIN()))){
            MyToast.showShort("行驶证识别失败，请重新识别");
            return;
        }
        adapter.notifyDataSetChanged();
        this.drivingLicenceModel = drivingLicenceModel;
        listData.clear();
        if(isShowVIN){
            listData.add(new NameValueModel("VIN码",drivingLicenceModel.getVIN()));
        }
        listData.add(new NameValueModel("车牌号码",drivingLicenceModel.getPlateNo()));


    }


    private void savePic(String url){
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().
                getResource(new SimpleCacheKey(url));
        File file = resource.getFile();
        FileUtils.copyFile(file.getAbsoluteFile(),new File(urlFilePath).getAbsoluteFile());
    }

    @Override
    public void requestVinCheckedSucceed(ResponseJson<VinCheckedModel> response, MyUniversalDialog myUniversalDialog,String vin) {
        //判断识别信息是否为空
        boolean boo = check();
        if (!boo) {
            return;
        }
        ACache.get(PadSysApp.getAppContext()).put("vin", vin);
        VinCheckedModel memberValue = response.getMemberValue();
        isAlertAccdient = memberValue.getIsAlertAccdient();
        accdientAlertMsg = memberValue.getAccdientAlertMsg();

        drivingLicenceModel = new DrivingLicenceModel();
        drivingLicenceModel.setVIN(listData.get(0).getValue());
        drivingLicenceModel.setPlateNo(listData.get(1).getValue());

        Intent intent = new Intent();
        intent.putExtra("DrivingLicenceModel", drivingLicenceModel);
        intent.putExtra("isAlertAccdient",isAlertAccdient);
        intent.putExtra("accdientAlertMsg",accdientAlertMsg);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void requestVinCheckedFailed(String message, MyUniversalDialog myUniversalDialog) {
        MyToast.showLong(message);
    }
}
