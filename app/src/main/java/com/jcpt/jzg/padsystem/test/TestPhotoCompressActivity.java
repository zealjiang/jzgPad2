package com.jcpt.jzg.padsystem.test;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.SDCardUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.interfaces.OnCompressListener;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.PhotoCompressor;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;


/***
 * 测试图片压缩，完美方案
 */
public class TestPhotoCompressActivity extends AppCompatActivity {

    private static final String TAG = "TestPhotoCompressActivity";


    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    @BindView(R.id.ivBlur)
    SimpleDraweeView ivBlur;
    @BindView(R.id.ivBlur2)
    SimpleDraweeView ivBlur2;
    private FunctionConfig functionConfig;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_util);
        initGalleryFinal();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tvContent, R.id.tvContent2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvContent:
                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                break;
            case R.id.tvContent2:
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                break;
        }
    }


    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && resultList.size() > 0) {
                imgPath = resultList.get(0).getPhotoPath();
                ivBlur.setImageURI(Uri.parse("file://" + imgPath));
                double fileSize = FileUtils.getFileSize(imgPath, ConstUtils.MemoryUnit.KB);
                LogUtil.e(TAG, "old Photo=" + imgPath + ",fileSize=" + fileSize + "KB");

                PhotoCompressor.get(TestPhotoCompressActivity.this)
                        .load(new File(imgPath))   //传人要压缩的图片
                        .setPhotoCacheDir(SDCardUtils.getSDCardPath() + "/voiceofnet/image_temp/")
                        .putGear(PhotoCompressor.THIRD_GEAR)      //设定压缩档次，默认三挡
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                String newPath = file.getAbsolutePath();
                                ivBlur2.setImageURI(Uri.parse("file://" + newPath));
                                double newSize = FileUtils.getFileSize(newPath, ConstUtils.MemoryUnit.KB);
                                LogUtil.e(TAG, "compressed Photo=" + newPath + ",newSize=" + newSize);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();    //启动压缩
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            MyToast.showShort(errorMsg);
        }
    };

    private void initGalleryFinal() {
        FunctionConfig.Builder builder = new FunctionConfig.Builder().setCropHeight(768).setCropWidth(1024).setEnableCamera(true).setForceCrop(true).setEnableCrop(true);
        ImageLoader imageLoader = FrescoImageLoader.getSingleton();
        builder.setEnableCamera(true);
        builder.setEnablePreview(false);
        functionConfig = builder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, ThemeConfig.DEFAULT)
                .setTakePhotoFolder(new File(SDCardUtils.getSDCardPath() + "voiceofnet/photo_temp/"))
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);

    }


}
