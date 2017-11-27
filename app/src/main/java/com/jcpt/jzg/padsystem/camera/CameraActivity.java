package com.jcpt.jzg.padsystem.camera;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.PreviewAdapter;
import com.jcpt.jzg.padsystem.adapter.ProvinceCityAdapter;
import com.jcpt.jzg.padsystem.app.AppManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.Luban;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.ScreenSwitchUtils;
import com.jcpt.jzg.padsystem.utils.StringHelper;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;
import rx.functions.Action1;

import static android.hardware.Camera.Parameters.SCENE_MODE_AUTO;
import static com.jcpt.jzg.padsystem.R.id.rv;

public class CameraActivity extends AppCompatActivity {

    private final String TAG = "CameraActivity";

    private final int FLAG_CHOOCE_PICTURE = 1001;
    private final int MEDIA_TYPE_IMAGE = 1;
    private Camera mCamera;
    private CameraPreview mPreview;
    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    private int statusBarHeight;

    @BindView(R.id.btnAlbum)
    Button btnAlbum;
    @BindView(R.id.button_flash)
    ImageView flashButton;
    @BindView(R.id.ll_control)
    LinearLayout llControl;
    @BindView(rv)
    RecyclerView recyclerView;
    @BindView(R.id.tv_photo_name)
    TextView tvPhotoName;
    @BindView(R.id.focus_index)
    View focusIndex;//手动对焦框
    @BindView(R.id.seekbar)
    SeekBar seekbar;//进度条

    @BindView(R.id.btnHDR)
    Button btnHDR;
    @BindView(R.id.btnWhiteBalance)
    Button btnWhiteBalance;

    @BindView(R.id.rlBigPreview)
    RelativeLayout rlBigPreview;
    @BindView(R.id.photoDraweeView)
    PhotoDraweeView photoDraweeView;



    private FrameLayout fLpreview;
    private int preViewHeight;
    private int preViewWidth;
    //默认前置或者后置相机 这里暂时设置为后置
    private int mCameraId = 0;
    //记录当前正在拍的是第几张图片
    private int curPhoto = 0;
    private int captureType =2;
    private ArrayList<PictureItem> pictureItems;
    private String taskId;
    //是否拍到最后一张了
    private Handler handler = new Handler();
    private boolean showGallery = false;
    private PreviewAdapter previewAdapter;
    private ProgressDialog dialog;
    private static final int REQ_BIG_PHOTO = 10086;
    private boolean isTakingPhoto = false;
    private Toast toast;


    //private ScreenSwitchUtils instance;
    public  int screenOrientation;
    private ArrayList<String>  highQualityPicIdArray;
    private int tabPosition;//连续拍照当前点击的第几张图
    private final int REQUEST_CODE_CAMERA_STORAGE = 10;//请求拍照和存储
    private LinearLayoutManager layoutManager;
    private int surfaceViewW,surfaceViewH;

    @Override
    protected void onStart() {
        super.onStart();
        requestCamera();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            requestCamera();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //instance.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    private void requestCamera(){
        MobclickAgent.onResume(this);
        requestPermissionCamera();
        LogUtil.e("CameraActivity","onResume");

        //如果手动从文件夹中删除了图片,将删除的图片的path置空
        if (pictureItems != null && pictureItems.size() > 0) {
            for (int i = 0; i < pictureItems.size(); i++) {
                if (!StringUtils.isEmpty(pictureItems.get(i).getPicPath())&&!pictureItems.get(i).getPicPath().startsWith("http")&&!FileUtils.isFileExists(pictureItems.get(i).getPicPath())) {
                    pictureItems.get(i).setPicPath("");
                }
            }
        }

        //刷新adapter
        previewAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //instance = ScreenSwitchUtils.init(this.getApplicationContext());
        //去除title 标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏ationBar
        //getSupportActionBar().hide();
        //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        flashButton.setImageResource(R.mipmap.btn_camera_flash_auto);
        pictureItems = (ArrayList<PictureItem>) getIntent().getSerializableExtra("picList");
        showGallery = getIntent().getBooleanExtra("showGallery", false);//是否显示从相册选取的图标
        taskId = getIntent().getStringExtra("taskId");
        curPhoto = getIntent().getIntExtra("position",0);
        captureType = getIntent().getIntExtra(Constants.CAPTURE_TYPE,0);//显示拍摄模式，是单拍还是连拍

        if(captureType==Constants.CAPTURE_TYPE_SINGLE && pictureItems.size()>1){
            //MyToast.showLong("调用者注意:单拍模式下，照片集合size必须==1");
            finish();
            return;
        }
        highQualityPicIdArray = getIntent().getStringArrayListExtra("highQualityPicIdArray");//高质量图片的ID

        String taskDir = Constants.ROOT_DIR + File.separator + taskId;
        FileUtils.createOrExistsDir(new File(taskDir));

        if(pictureItems!=null && pictureItems.size()>0){
            tvPhotoName.setText(pictureItems.get(curPhoto).getPicName());
        }else{
            finish();
            return;
        }
        showTips();

        //检查设备是否有摄像头
        boolean hasCamera = checkCameraHardware(this);
        if (!hasCamera) {
            MyToast.showLong("此设备不支持拍照");
            this.finish();
            return;
        }

        if (showGallery) {
            btnAlbum.setVisibility(View.VISIBLE);
            btnAlbum.setEnabled(true);
        } else {
            btnAlbum.setVisibility(View.INVISIBLE);
            btnAlbum.setEnabled(false);
        }

        if(captureType==Constants.CAPTURE_TYPE_MULTI){//连拍则显示预览
            recyclerView.setVisibility(View.VISIBLE);
        }else{// 单拍隐藏
            recyclerView.setVisibility(View.GONE);
        }

        reSizeView();
        setPreviewAdapter();


    }


    private void showTips(){

        if(pictureItems != null && "行驶证正本正面".equals(pictureItems.get(curPhoto).getPicName())){
            if(toast==null){
                toast = Toast.makeText(this, "请对准行驶证四角拍摄，且避免行驶证表面出现反光\n" +
                        "                 (请横持设备拍摄行驶证正本正面)",Toast.LENGTH_LONG);
            }else {
                toast.setText("请对准行驶证四角拍摄，且避免行驶证表面出现反光\n" +
                        "                 (请横持设备拍摄行驶证正本正面)");
            }
            toast.setGravity(Gravity.CENTER, -200, 0);
            toast.show();
        }
    }

    private void showDialog(){
        if(dialog==null)
            dialog = ProgressDialog.show(this,"照片处理中","");
        else
            dialog.show();

    }

    private void closeDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @OnClick({R.id.iv_back, R.id.btnAlbum,R.id.ll_camera,R.id.button_capture,R.id.ll_flash,R.id.ivRecap,R.id.ivOK,R.id.btnCancel,R.id.btnHDR,R.id.btnWhiteBalance})
    public void onClick(View view) {
        if(mCamera == null){
            return;
        }
        Camera.Parameters p = mCamera.getParameters();
        switch (view.getId()) {
            case R.id.ivRecap:
                rlBigPreview.setVisibility(View.GONE);
                photoDraweeView.setImageURI(Uri.parse(""));
                photoDraweeView.setTag("");
                break;
            case R.id.ivOK:
                rlBigPreview.setVisibility(View.GONE);
                String filePath = (String) photoDraweeView.getTag();
                if(!TextUtils.isEmpty(filePath)){
                    PictureItem pictureItem = pictureItems.get(curPhoto);
                    if(highQualityPicIdArray!=null&&highQualityPicIdArray.size()>0){
                        if(highQualityPicIdArray.contains(pictureItem.getPicId())){
                            compress(filePath,Luban.HIGH_QUALITY_COMPRESS);//ImageCompressor.SELF_GEAR);
                        }else{
                            compress(filePath,Luban.DEFALU_COMPRESS);//ImageCompressor.THIRD_GEAR);
                        }
                    }else{
                        compress(filePath,Luban.DEFALU_COMPRESS);//ImageCompressor.THIRD_GEAR);
                    }
                }
                photoDraweeView.setTag("");
                break;
            case R.id.iv_back:
                back();
                break;
            case R.id.btnAlbum:
                getPicFromGallery();
                break;
            case R.id.ll_camera:
                switchCamera();
                break;
            case R.id.button_capture:
                //点击了拍照，相机就不再处在预览状态了，所以就不能再点击屏幕进行自动对焦了
                mPreview.setIsPreviewing(false);
                takePictureFormCamera();
                break;
            case R.id.ll_flash:
                toggleFlash(p, p.getFlashMode());
                break;
            case R.id.btnHDR:
                //toggleHDR(mCamera,p.getSceneMode());
                break;
            case R.id.btnWhiteBalance:
                toggleWhiteBalance(mCamera,p.getWhiteBalance());
                break;
            case R.id.btnCancel:
                back();
                break;
        }
    }
    private void takePictureFormCamera() {
        LogUtil.e(TAG,"点击拍照");
        if(isTakingPhoto)//防止快速点击拍照按钮
            return;

        //点击拍照的瞬间拿到当前设备的旋转角度，以便后续矫正照片方位  -> 李波 on 2017/1/10.
        screenOrientation = ScreenSwitchUtils.screenOrientation;

        isTakingPhoto = true;

        try {
            if (Build.VERSION.SDK_INT >= 23) {
                RxPermissions rxPermissions = new RxPermissions(this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if (granted) {
                                    focusIndex.setVisibility(View.GONE);
                                    mCamera.takePicture(null, null, captrueCallback);
                                    //autoFocusTakePic();
                                } else {
                                    MyToast.showLong("请在设置-权限管理中开启拍照和SD卡读写授权后重试!");
                                    isTakingPhoto = false;
                                }
                            }
                        });
            }else{

                focusIndex.setVisibility(View.GONE);
                mCamera.takePicture(null, null, captrueCallback);
                //autoFocusTakePic();
            }
        }catch (Exception e){
            LogUtil.e("拍照","拍照出错"+e.getMessage());
        }

    }

    /**
     * 自动对焦后再拍照
     */
    private void autoFocusTakePic(){
        mCamera.autoFocus(new Camera.AutoFocusCallback() {

            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                try{
                    mCamera.takePicture(null, null, captrueCallback);
                }catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "对焦失败");
                }
            }
        });
    }

    private void getPicFromGallery(){

        //从相册选择
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, FLAG_CHOOCE_PICTURE);
    }


    /**
     * 开关闪光灯
     * <p/>
     * 闪一下FLASH_MODE_ON
     * 关闭模式FLASH_MODE_OFF
     * 自动感应是否要用闪光灯FLASH_MODE_AUTO
     */
    public void toggleFlash(Camera.Parameters p, String cameraFlashMode) {
        if (mCamera == null) {
            return;
        }
        if (Camera.Parameters.FLASH_MODE_OFF.equals(cameraFlashMode)) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            flashButton.setImageResource(R.mipmap.btn_camera_flash_on);
            saveFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(cameraFlashMode)) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            flashButton.setImageResource(R.mipmap.btn_camera_flash_auto);
            saveFlashMode(Camera.Parameters.FLASH_MODE_ON);
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(cameraFlashMode)) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            flashButton.setImageResource(R.mipmap.btn_camera_flash_torch);
            saveFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        } else if (Camera.Parameters.FLASH_MODE_TORCH.equals(cameraFlashMode)) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            flashButton.setImageResource(R.mipmap.btn_camera_flash_off);
            saveFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        } else {
            Toast.makeText(this, "Flash mode setting is not supported.", Toast.LENGTH_SHORT).show();
        }
        if(mCameraId== Camera.CameraInfo.CAMERA_FACING_FRONT){
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            flashButton.setImageResource(R.mipmap.btn_camera_flash_off);
            saveFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        }
        mCamera.setParameters(p);
    }

    public void toggleHDR(Camera mCamera,String sceneModel){
        if (mCamera == null) {
            return ;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> sceneModes = parameters.getSupportedSceneModes();
        // Check if camera scene exists
        if (sceneModes == null) {
            return;
        }
        if (!Camera.Parameters.SCENE_MODE_HDR.equals(sceneModel)) {
            // Turn on the hdr
            if (sceneModes.contains(Camera.Parameters.SCENE_MODE_HDR)) {
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_HDR);
                btnHDR.setText("HDR开");
            }
        }else{
            if (sceneModes.contains(Camera.Parameters.SCENE_MODE_AUTO)) {
                parameters.setSceneMode(SCENE_MODE_AUTO);
                btnHDR.setText("HDR关");
            }
        }
        mCamera.setParameters(parameters);
    }

    public void toggleWhiteBalance(Camera mCamera,String whiteBalanceModel){
        if (mCamera == null) {
            return ;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> whiteBalanceModes = parameters.getSupportedWhiteBalance();
        // Check if camera whiteBalanceModes exists
        if (whiteBalanceModes == null) {
            return;
        }

        if (!Camera.Parameters.WHITE_BALANCE_AUTO.equals(whiteBalanceModel)) {
            // Turn on the whiteBalanceModel
            if (whiteBalanceModes.contains(Camera.Parameters.WHITE_BALANCE_AUTO)) {
                parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
                btnWhiteBalance.setText("白平衡自动");
            }
        }else{
            if (whiteBalanceModes.contains(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT)) {
                parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
                btnWhiteBalance.setText("白平衡阴天");
            }
        }
        mCamera.setParameters(parameters);
    }


    //处理6.0动态权限问题
    private void requestPermissionCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .subscribe(new Action1<Boolean>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void call(Boolean granted) {
                            if (granted) {
                                getCamera();
                            } else {

                                MyToast.showLong("请在设置-权限管理中开启拍照和SD卡读写授权后重试!");
                                finish();

                            }
                        }
                    });

/*            int checkSelfPermission;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA);
            } catch (RuntimeException e) {
                Toast.makeText(CameraActivity.this, "没有拍照和SD卡读写权限，请在设置-权限管理中开启!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "RuntimeException:" + e.getMessage());
                return;
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED");
                if (!ActivityCompat.shouldShowRequestPermissionRationale(CameraActivity.this, Manifest.permission.CAMERA)) {
                    AskForPermission();
                } else {
                    Log.e(TAG, "requestCameraPermission else");
                    //ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_STORAGE);
                    RxPermissions.getInstance(this)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .subscribe(new Action1<Boolean>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void call(Boolean granted) {
                            if (granted) {
                                getCamera();
                            } else {
                                AskForPermission();
                            }
                        }
                    });
                }

            } else {
                Log.e(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
                getCamera();
            }*/

        } else {
            getCamera();
        }
    }


    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("没有拍照和SD卡读写权限，请在设置-权限管理中开启!");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MyToast.showLong("没有拍照和SD卡读写权限，请在设置-权限管理中开启!");
                finish();
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }


    private void compress(String imgPath, int compressGear){
        //new
        Luban.with(this)
                .load(new File(imgPath))                     //传人要压缩的图片
                .setCompressLevel(compressGear)
                .setCompressListener(new top.zibin.luban.OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                        showDialog();
                    }
                    @Override
                    public void onSuccess(File file) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        compressSuc(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过程出现问题时调用
                        closeDialog();
                        e.printStackTrace();
                    }
                }).launch();    //启动压缩

    }

    private void compressSuc(File compressedFile){
        if(null==compressedFile){
            closeDialog();
            MyToast.showShort("失败");
        }
        LogUtil.e(TAG,"src pic:"+compressedFile.length()/1024+"kb");
        String bigPic = Constants.ROOT_DIR + File.separator +taskId+File.separator;
        String fileName = String.valueOf(pictureItems.get(curPhoto).getPicId())+".jpg";
        String fullPath = bigPic + fileName;
        LogUtil.e(TAG,"final Path="+bigPic+fileName);
        if(FileUtils.createOrExistsDir(bigPic)){
            if(FileUtils.isFileExists(fullPath))
                FrescoCacheHelper.clearSingleCacheByUrl(fullPath,true);
            FileUtils.copyFile(compressedFile,new File(bigPic,fileName));
            closeDialog();

            if(captureType==Constants.CAPTURE_TYPE_MULTI){//连拍，则继续
                showPhoto(fullPath);
                take2Next();
            }else{//单拍走人
                pictureItems.get(curPhoto).setPicPath(fullPath);
                back();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK || data==null)
            return;
        if (requestCode == FLAG_CHOOCE_PICTURE) {
            Uri uri = data.getData();
            String imgPath = getPath(CameraActivity.this, uri);
            LogUtil.e(TAG,"src path:"+imgPath);
            //判断是否是图片 0217 郑有权
            if(StringHelper.isImage(imgPath)){
                PictureItem pictureItem = pictureItems.get(curPhoto);
                if(highQualityPicIdArray!=null&&highQualityPicIdArray.size()>0){
                    if(highQualityPicIdArray.contains(pictureItem.getPicId())){
                        compress(imgPath,Luban.HIGH_QUALITY_COMPRESS);//ImageCompressor.SELF_GEAR);
                    }else{
                        compress(imgPath,Luban.DEFALU_COMPRESS);//.THIRD_GEAR);
                    }
                }else{
                    compress(imgPath,Luban.DEFALU_COMPRESS);//ImageCompressor.THIRD_GEAR);
                }
            }else{
                MyToast.showLong("请选择正确图片");
            }


        }else if(requestCode==REQ_BIG_PHOTO){
            //清空当前小缩略图缓存
            //FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPhoto).getPicPath(),true);
            boolean showRecapture = data.getBooleanExtra("recapture", false);

            curPhoto = data.getIntExtra("recapturePosition",curPhoto);
            tvPhotoName.setText(pictureItems.get(curPhoto).getPicName());
            if(showRecapture){
                //修改图片名称
                tvPhotoName.setText(pictureItems.get(curPhoto).getPicName());
                //pictureItems.get(curPhoto).setPicPath("");
                previewAdapter.setCurrPos(curPhoto);
                previewAdapter.notifyItemChanged(curPhoto);
            }
        }
    }

    public static String getPath(final Context context, final Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private int pictureWidth = 0;
    private int pictureHeight = 0;
    private void initPictureSize(Camera.Parameters parameters){
        //必需重置下面这两个值 by zealjiang 2017-1-9
        pictureWidth = 0;
        pictureHeight = 0;
        List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();
        sort(sizeList);
        if (sizeList.size()>0) {
            if(sizeList.get(0).width<sizeList.get(sizeList.size()-1).width){//如果是从小到大，则反序
                Collections.reverse(sizeList);
            }

            Camera.Size tempSize = null;
            int i = 0;
            for (Camera.Size size : sizeList) {
                Log.e("TAG","cameraId :" +mCameraId+ "  SupportedPictureSizes : " + size.width + "x" + size.height);
                if (size.width/1000==1) {//取宽度为1xxx的值
                    if(i==0){//取宽度为1xxx第一个
                        tempSize = size;
                    }
                    i++;
                    if(size.width!=size.height){
                        pictureWidth = size.width;
                        pictureHeight = size.height;
                        break;
                    }
                }
            }
            if(pictureWidth==0||pictureHeight==0){
                if(tempSize!=null){
                    pictureWidth = tempSize.width;
                    pictureHeight = tempSize.height;
                }else{
                    pictureWidth = sizeList.get(0).width;
                    pictureHeight = sizeList.get(0).height;
                }
            }
        }else{
            pictureWidth = ScreenUtils.getScreenWidth(this);
            pictureHeight = ScreenUtils.getScreenHeight(this);
            LogUtil.e(TAG,"输出图片大小取屏幕分辨率");
        }
        parameters.setPictureSize(pictureWidth, pictureHeight);
        LogUtil.e(TAG, "cameraId :" +mCameraId+ "  selected PictureSize width :"+pictureWidth+" height: "+pictureHeight);
    }

    /**
     * Get     the     value     of     the     data     column     for     this     Uri.     This     is     useful     for
     * MediaStore     Uris,     and     other     file-based     ContentProviders.
     *
     * @param context       The     context.
     * @param uri           The     Uri     to     query.
     * @param selection     (Optional)     Filter     used     in     the     query.
     * @param selectionArgs (Optional)     Selection     arguments     used     in     the     query.
     * @return The     value     of     the     _data     column,     which     is     typically     a     file     path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The     Uri     to     check.
     * @return Whether     the     Uri     authority     is     ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The     Uri     to     check.
     * @return Whether     the     Uri     authority     is     DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The     Uri     to     check.
     * @return Whether     the     Uri     authority     is     MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The     Uri     to     check.
     * @return Whether     the     Uri     authority     is     Google     Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private void setPreviewAdapter() {
        previewAdapter = new PreviewAdapter(this,  pictureItems);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(previewAdapter);
        MoveToPosition(layoutManager,recyclerView,curPhoto);
        previewAdapter.setCurrPos(curPhoto);

        previewAdapter.setOnItemClickListener(new ProvinceCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if(!TextUtils.isEmpty(pictureItems.get(position).getPicPath())){
                    tabPosition = position;
                    Intent intent = new Intent(CameraActivity.this, PictureZoomActivity.class);
                    intent.putExtra("pictureItems",pictureItems);
                    intent.putExtra("curPosition",position);
                    intent.putExtra("url",pictureItems.get(position).getPicPath());
                    intent.putExtra("showRecapture",true); //连拍情况下 预留大图时，是否显示重拍按钮
                    intent.putExtra("isThumbnail",true); //是否是缩略图进入大图
                    startActivityForResult(intent,REQ_BIG_PHOTO);
                }else{
                    if (curPhoto == position) {
                        return;
                    }
                    previewAdapter.setCurrPos(position);
                    tvPhotoName.setText(pictureItems.get(position).getPicName());
                    curPhoto = position;
                    showTips();
                }
            }
        });
    }

    /**
     * RecyclerView 移动到当前位置，
     *  郑有权
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }





    /**
     * 重新计算预览界面的宽高
     * @author zealjiang
     * @time 2017/4/11 15:34
     */
    private void reSizeView() {
        screenWidth = ScreenUtils.getScreenWidth(this);
        screenHeight = ScreenUtils.getScreenHeight(this);

        //当前是横屏显示
        //预览区的宽高比4:3
        //高度 screenWidth - statusBarHeight 求宽度 高/宽 = 3/4  --->  宽=高*（4/3）

        //int top = (int) (50 * getResources().getDisplayMetrics().density + 0.5f);
        //preViewHeight = screenHeight - top - statusBarHeight;
        //preViewWidth = preViewHeight * 16 / 9;
        int rightWidth = screenWidth - preViewWidth;

        //仿照系统相机，宽度比为4:3，高是屏幕的高度，根据高度和宽高比计算出宽度
        surfaceViewW = screenHeight/3*4;
        surfaceViewH = screenHeight;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llControl.getLayoutParams();
        params.width = screenWidth - surfaceViewW;
        llControl.setLayoutParams(params);

    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private Camera getCameraInstance(int id) {
        Camera c = null;
        try {
            c = Camera.open(id);
        } catch (Exception e) {
            LogUtil.e(TAG,"打开相机失败");
            e.printStackTrace();
        }
        return c;
    }

    private Camera.PictureCallback captrueCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LogUtil.e(TAG,"src pic:"+data.length/1024+"kb");
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                LogUtil.e(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                LogUtil.e(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                LogUtil.e(TAG, "Error accessing file: " + e.getMessage());
            }

            try {
                camera.cancelAutoFocus();
                camera.startPreview();
            } catch (Exception e) {
                LogUtil.e(TAG, "相机取消自动聚焦出错" + e.getMessage());
            }

            mPreview.setIsPreviewing(true);
            isTakingPhoto = false;
            rlBigPreview.setVisibility(View.VISIBLE);
            photoDraweeView.setPhotoUri(Uri.parse("file://"+pictureFile.getAbsolutePath()));
            LogUtil.e(TAG, "file: " + pictureFile.getAbsolutePath());
            photoDraweeView.setTag(pictureFile.getAbsolutePath());
        }
    };


    private void take2Next(){
        if(curPhoto==pictureItems.size()-1){
            int emptyIndex = getNext();
            if(emptyIndex<0){
                back();
            }else{
                curPhoto = emptyIndex;
                previewAdapter.setCurrPos(curPhoto);
                tvPhotoName.setText(pictureItems.get(curPhoto).getPicName());
                MoveToPosition(layoutManager,recyclerView,curPhoto);
            }
        }else{
            curPhoto++;
            if(TextUtils.isEmpty(pictureItems.get(curPhoto).getPicPath())){//如果当前照片路径为空，则拍当前这一张
                previewAdapter.setCurrPos(curPhoto);
                tvPhotoName.setText(pictureItems.get(curPhoto).getPicName());
            }else{//否则继续递归调用
                take2Next();
            }
        }


    }

    private int getNext(){
        int index = -1;
        for(int i=0;i<pictureItems.size();i++){
            PictureItem item = pictureItems.get(i);
            if(TextUtils.isEmpty(item.getPicPath())){
                index = i;
                break;
            }
        }
        return index;
    }

    private void showPhoto(String path) {
        if (curPhoto < pictureItems.size() - 1) {
            recyclerView.smoothScrollToPosition(curPhoto + 1);
        } else {
            recyclerView.smoothScrollToPosition(curPhoto);
        }
        pictureItems.get(curPhoto).setPicPath(path);
        previewAdapter.setCurrPos(curPhoto);

    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mPreview.setCamera(null);
        }
    }

    /**
     * Create a File for saving an image
     */
    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.TEMP_TAKE_PHOTO_DIR);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                LogUtil.e("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public  void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth  需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList   需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight,List<Camera.Size> preSizeList) {
        boolean mIsPortrait = false;
        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (mIsPortrait) {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        } else {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList) {
            if ((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio, deltaWidth;
        float deltaRatioMin = Float.MAX_VALUE;
        float deltaWidthMin = 500;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            deltaWidth = Math.abs(size.width - ReqTmpWidth);

            if (deltaRatio < deltaRatioMin && deltaWidth < deltaWidthMin) {
                deltaRatioMin = deltaRatio;
                deltaWidthMin = deltaWidth;
                retSize = size;
            }
        }

        return retSize;
    }

    /***
     * 获取最佳的相机预览和输出分辨率
     * @param srcList
     * @param bestWidth
     * @param bestHeight
     * @return
     */
    private Camera.Size getBestCameraSize(List<Camera.Size> srcList,int bestWidth,int bestHeight){
        Camera.Size resultSize=getWantedCameraSize(srcList,bestWidth,bestHeight);
        if(resultSize==null){
            for (Camera.Size size:srcList) {
                if(Math.abs(size.width*1.0f/size.height - bestWidth*1.0f/bestHeight)<0.11 && size.width*size.height>1000000){//如果指定的分辨率方案不存在，则找出跟指定分辨率方案高宽比一致且大于100W像素的方案
                    resultSize = size;
                    break;
                }
            }
        }
        if(resultSize==null){
            resultSize = srcList.get(srcList.size()-1);
        }
        return resultSize;
    }

    /***
     * 查找指定分辨率是否存在
     * @param srcList
     * @param bestWidth
     * @param bestHeight
     * @return
     */
    private Camera.Size getWantedCameraSize(List<Camera.Size> srcList,int bestWidth,int bestHeight){
        Camera.Size resultSize= null;
        for (Camera.Size size:srcList) {
            if(size.width==bestWidth &&size.height==bestHeight){
                resultSize = size;
                break;
            }
        }
        return resultSize;
    }

    private void sort(List<Camera.Size> srcList){
        Collections.sort(srcList, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                return lhs.width -rhs.width;
            }
        });
    }


    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = getCameraInstance(mCameraId);

        if (mCamera == null) return;
        setupCamera(mCamera);
        mPreview.setCamera(mCamera);

    }


    /**
     * 设置相机
     */
    private void setupCamera(Camera camera) {
        if (camera == null) return;
        isTakingPhoto = false;
        Camera.Parameters params = camera.getParameters();
        //setCameraDisplayOrientation(this, mCameraId, camera);
        //------------------------------------设置最佳预览分辨率---------------------------------
        List<Camera.Size> listPreSize = params.getSupportedPreviewSizes();
        sort(listPreSize);
        preViewWidth=0;
        preViewHeight=0;
        Camera.Size previewSize = getBestCameraSize(listPreSize,surfaceViewW,surfaceViewH);
        preViewWidth = previewSize.width;
        preViewHeight = previewSize.height;
        params.setPreviewSize(preViewWidth, preViewHeight);
        LogUtil.e(TAG,"cameraId :" +mCameraId+ "  selected PreviewSize : width: "+previewSize.width+"   height: "+previewSize.height);
        //------------------------------------设置最佳输出分辨率---------------------------------
        pictureWidth = 0;
        pictureHeight = 0;
        List<Camera.Size> sizeList = params.getSupportedPictureSizes();
        sort(sizeList);
        Camera.Size pictureSize = getBestCameraSize(sizeList, 2048, 1536);
        pictureWidth = pictureSize.width;
        pictureHeight = pictureSize.height;
/*        pictureWidth = sizeList.get(0).width;
        pictureHeight = sizeList.get(0).height;*/
        params.setPictureSize(pictureWidth, pictureHeight);

        LogUtil.e(TAG, "cameraId :" +mCameraId+ "  selected PictureSize width :"+pictureWidth+" height: "+pictureHeight);
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            // Autofocus mode is supported set the focus mode
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }


        //修改闪光灯选项
        toggleFlash(params, getFlashMode());

        //开启HDR

        //开启白平衡
        //CameraUtil.getInstance().turnOnWhiteBalance(camera);
        //params = CameraUtil.getInstance().turnOnWhiteBalance(params);
//        params.setExposureCompensation(2);
//        System.out.println("getExposureCompensation======="+params.getExposureCompensation());
//        System.out.println("getMaxExposureCompensation======="+params.getMaxExposureCompensation());
//        System.out.println("getMinExposureCompensation======="+params.getMinExposureCompensation());
//        System.out.println("getExposureCompensationStep======="+params.getExposureCompensationStep());
        camera.setParameters(params);

        //设置相机预览成功后的回调
        camera.startPreview();
    }

    /**
     * 获取相机
     *
     * @author zealjiang
     * @time 2016/9/21 15:38
     */
    private void getCamera() {
        if (mCamera == null) {
            //异步线程获取相机
            //new OpenCameraTask().execute();
            //主线程获取相机
            onMainThreadGetCamera();
        }
    }

    /**
     * 启动预览
     *
     * @author zealjiang
     * @time 2016/9/21 15:39
     */
    private void startPreview() {
        setupCamera(mCamera);

        //这段代码放到这是因为
        if (mPreview == null) {
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, this);
            fLpreview = (FrameLayout) findViewById(R.id.cameraPreview);
            fLpreview.addView(mPreview);
            mPreview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(mCameraId== Camera.CameraInfo.CAMERA_FACING_FRONT){//前置摄像头不支持手动对焦
                        return true;
                    }
                    //如果预览还没好，不可进对焦 、曝光度的设置
                    if(!mPreview.getIsPreviewing()){
                        return false;
                    }
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            float y = event.getY();
                            int height = mPreview.getHeight();
                            if(y<height-200){
                                int pwidth = fLpreview.getWidth();
                                int pheight = fLpreview.getHeight();
                                /**某些情况会出现mCamera为空的异常，这里加了判断*/
                                if(mCamera==null){
                                    releaseCamera();
                                    mCamera = getCameraInstance(mCameraId);
                                    setupCamera(mCamera);
                                    mPreview.setCamera(mCamera);
                                }
                                if(mPreview.getmCamera()==null){
                                    mPreview.setCamera(mCamera);
                                }
                                mPreview.handleFocus(event,pwidth,pheight);
                            }
                            int focusIndexW = UIUtils.dip2px(CameraActivity.this,120);//注：120dp是对焦框的边长
                            RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(focusIndex.getLayoutParams());
                            float leftM = event.getX() - focusIndexW/2,topM = event.getY() - focusIndexW/2;
                            if(event.getX()<focusIndexW/2){
                                leftM = 0;
                            }
                            if(event.getY()<focusIndexW/2){
                                topM = 0;
                            }
                            if(event.getX()>surfaceViewW - focusIndexW){
                                leftM = surfaceViewW - focusIndexW;
                            }
                            if(event.getY()>surfaceViewH - focusIndexW){
                                topM = surfaceViewH - focusIndexW;
                            }
                            layout.setMargins((int) leftM, (int) topM, 0, 0);


                            focusIndex.setLayoutParams(layout);
                            focusIndex.setVisibility(View.VISIBLE);
                            ScaleAnimation sa = new ScaleAnimation(0.8f, 0.5f,0.8f, 0.5f,ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                            sa.setDuration(2000);
                            focusIndex.startAnimation(sa);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    focusIndex.setVisibility(View.INVISIBLE);
                                }
                            }, 2000);


                            if(seekbar.getVisibility() != View.VISIBLE){
                            seekbar.setVisibility(View.VISIBLE);
                            seekbar.setMax(mCamera.getParameters().getMaxExposureCompensation()*2);
                            seekbar.setProgress(mCamera.getParameters().getMaxExposureCompensation());
                            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    //如果在拖动滑杆的同时点击了相册，CameraActivity进入onPause状态，Camera被回收
                                    if(mCamera==null){
                                        return;
                                    }
                                    Camera.Parameters parameters = mCamera.getParameters();
                                    parameters.setExposureCompensation(progress - parameters.getMaxExposureCompensation());
                                    mCamera.setParameters(parameters);

                                    i = 0;
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                    if(seekbar.getVisibility() == View.VISIBLE){
                                        i = 0;
                                    }else{
                                        i = 0;
                                        new MyThread1().start();
                                    }
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {}
                            });
                                i = 0;
                                new MyThread1().start();
                            }else{
                                i = 0;
                            }

                            break;
                    }
                    return false;
                }
            });

        }
        // Create our Preview view and set it as the content of our activity.
        mPreview.setCamera(mCamera);
        Log.e(TAG, "mPreview.setCamera mCamera: " + mCamera);
    }

    //添加聚焦曝光率进度条 2017.02.13   郑有权
    int i = 0;
    class MyThread1 extends Thread{

        public void run() {
            while(i <= 2){
//                System.out.println("MyThread1=======" +i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }

            if(i == 3){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        seekbar.setVisibility(View.INVISIBLE);
                    }
                }, 0);
            }
        }
    }


    /**
     * 保存闪光灯选项
     *
     * @param flashMode
     */
    public void saveFlashMode(String flashMode) {
        SharedPreferences preferences = getSharedPreferences("Camera_FLASH_MODE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FLASH_MODE", flashMode);
        editor.commit();
    }

    /**
     * 闪光灯选项
     *
     * @return
     */
    public String getFlashMode() {
        SharedPreferences preferences = getSharedPreferences("Camera_FLASH_MODE", Context.MODE_PRIVATE);
        return preferences.getString("FLASH_MODE", Camera.Parameters.FLASH_MODE_AUTO);
    }

    /**
     * 在主线程获取相机，可以造成主线程阻塞
     *
     * @author zealjiang
     * @time 2016/9/22 9:23
     */
    private void onMainThreadGetCamera() {
        mCamera = getCameraInstance(mCameraId);
        if (mCamera == null) {
            finish();
            return;
        } else {
            startPreview();
        }
    }

    /**
     * 关闭此拍照Activity
     *
     * @author zealjiang
     * @time 2016/9/21 11:06
     */
    private void back() {
        if(rlBigPreview.getVisibility()==View.VISIBLE){
            rlBigPreview.setVisibility(View.GONE);
            photoDraweeView.setImageURI(Uri.parse(""));
            photoDraweeView.setTag("");
        }else{
            Intent intent = getIntent();
            intent.putExtra("picList", pictureItems);
            intent.putExtra("curPosition", curPhoto);
            intent.putExtra(Constants.CAPTURE_TYPE, captureType);
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("CameraActivity","onPause");
        MobclickAgent.onPause(this);
        releaseCamera();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtil.e(TAG,"onConfigurationChanged");
    }

    /**
     * 使用音量键拍照  郑有权
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if(mPreview.isPreviewing()){
                    mPreview.setIsPreviewing(false);
                    takePictureFormCamera();
                }

                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                if(mPreview.isPreviewing()){
                    mPreview.setIsPreviewing(false);
                    takePictureFormCamera();
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                if(rlBigPreview.getVisibility() == View.VISIBLE){
                    rlBigPreview.setVisibility(View.GONE);
                }else{
                    finish();
                }
                return false;
        }
        return super.onKeyDown(keyCode, event);

    }
}
