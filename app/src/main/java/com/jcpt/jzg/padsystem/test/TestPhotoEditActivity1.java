package com.jcpt.jzg.padsystem.test;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.image.util.BitmapUtils;
import com.jcpt.jzg.padsystem.image.util.StickerTask;
import com.jcpt.jzg.padsystem.image.view.crop.CropImageType;
import com.jcpt.jzg.padsystem.image.view.crop.CropImageView;
import com.jcpt.jzg.padsystem.image.view.mosaic.DrawMosaicView;
import com.jcpt.jzg.padsystem.image.view.mosaic.MosaicUtil;
import com.jcpt.jzg.padsystem.image.view.operate.ImageObject;
import com.jcpt.jzg.padsystem.image.view.operate.OperateUtils;
import com.jcpt.jzg.padsystem.image.view.operate.OperateView;
import com.jcpt.jzg.padsystem.image.view.operate.StickerItem;
import com.jcpt.jzg.padsystem.image.view.operate.StickerView;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;

import java.io.File;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestPhotoEditActivity1 extends AppCompatActivity {
    private static final String TAG = "TestPhotoEditActivity";
    @BindView(R.id.ivShow)
public SimpleDraweeView ivShow;
    @BindView(R.id.ivCrop)
    CropImageView ivCrop;
    @BindView(R.id.ivMarsic)
    DrawMosaicView ivMarsic;
    @BindView(R.id.llStickerContainer)
    LinearLayout llStickerContainer;
    @BindView(R.id.sticker_panel)
    StickerView stickerPanel;
    private OperateView operateView;

    /**
     * 图片原始路径，由上一个页面传入
     */
    private String srcImagePath;
    /**
     * 由前一个页面传入，暂时写死  TODO
     */
    private String fileName = "temp.jpg";
    /**
     * 处理后的图片路径
     */
    public String newPath = Environment.getExternalStorageDirectory() + "/padsys2.0/images/" + fileName;
    private Bitmap bmImage;
    private static final int STICKERS[] = {R.drawable.ic_arrow, R.drawable.ic_circle};
    /**
     * 0:默认显示<br/> 1:剪裁<br/> 2:加马赛克<br/> 3:加箭头<br/> 4:加圆圈<br/>
     */
    private int type = 0;
    private OperateUtils operateUtils;
    private SaveStickersTask mSaveTask;
    //    private StickerView stickerView;
    private MaterialDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_photo_edit);
        ButterKnife.bind(this);
        srcImagePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/4-6.jpg";
//        bmImage = BitmapFactory.decodeFile(srcImagePath);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
       int imageWidth = metrics.widthPixels / 2;
        int imageHeight = metrics.heightPixels / 2;
        bmImage =   BitmapUtils.getSampledBitmap(srcImagePath, imageWidth,
                imageHeight);
        ivShow.setImageURI(Uri.parse("file://" + srcImagePath));
        operateUtils = new OperateUtils(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("照片编辑");
        getSupportActionBar().setHomeButtonEnabled(true);
        FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() + "/padsys2.0/images/");
        File sys = getExternalFilesDir("sys");
        if (!sys.exists()) {
            sys.mkdirs();

        }
        //Environment.getDataDirectory()/Andorid/data/+"/"+getPackageName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FileUtils.deleteFile(newPath);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 马赛克被点击
     */
    private void prepareMarsic() {
        ivShow.setVisibility(View.GONE);
        ivCrop.setVisibility(View.GONE);
        ivMarsic.setVisibility(View.VISIBLE);
        llStickerContainer.setVisibility(View.GONE);
        if (!FileUtils.isFileExists(newPath))
            ivMarsic.setMosaicBackgroundResource(srcImagePath);
        else
            ivMarsic.setMosaicBackgroundResource(newPath);
        Bitmap bit = MosaicUtil.getMosaic(bmImage);
        ivMarsic.setMosaicResource(bit);
        ivMarsic.setMosaicBrushWidth(50);
    }

    /**
     * 剪裁被点击
     */
    private void prepareCrop() {
        ivShow.setVisibility(View.GONE);
        ivCrop.setVisibility(View.VISIBLE);
        ivMarsic.setVisibility(View.GONE);
        llStickerContainer.setVisibility(View.GONE);
        ivCrop.setImageBitmap(bmImage);
        Bitmap hh = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_crop_btn);
        ivCrop.setCropOverlayCornerBitmap(hh);
        ivCrop.setGuidelines(CropImageType.CROPIMAGE_GRID_ON_TOUCH);
        ivCrop.setFixedAspectRatio(false);

    }

    private void addPic(int resId) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId);
        ImageObject imgObject = operateUtils.getImageObject(bmp, operateView, 5, 250, 150);
        operateView.addItem(imgObject);
    }

    private void addPic1(int resId) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId);
//        ImageObject imgObject = operateUtils.getImageObject(bmp, operateView, 5, 250, 150);
        stickerPanel.addBitImage(bmp);
//        operateView.addItem(imgObject);
    }

    /***
     * 贴纸按钮被点击
     */
    private void prepareStick() {
//        ivShow.setVisibility(View.GONE);
        ivCrop.setVisibility(View.GONE);
        ivMarsic.setVisibility(View.GONE);
        llStickerContainer.setVisibility(View.VISIBLE);
        if (type == 3) {
            addPic1(STICKERS[0]);
        } else if (type == 4) {
            addPic1(STICKERS[1]);
        }


//        operateView = new OperateView(TestPhotoEditActivity1.this, bmImage);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(bmImage.getWidth(),bmImage.getHeight());
//        operateView.setLayoutParams(layoutParams);
//        llStickerContainer.addView(operateView);
//        operateView.setMultiAdd(true);
//        if(type==3){
//            addPic(STICKERS[0]);
//        }else if(type==4){
//            addPic(STICKERS[1]);
//        }
    }

    private void saveStick() {
        operateView.save();
        bmImage = Bitmap.createBitmap(operateView.getWidth(), operateView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmImage);
        operateView.draw(canvas);
        ImageUtils.save(bmImage, newPath, Bitmap.CompressFormat.JPEG);
    }


    /**
     * 被点击项
     *
     * @param newType
     */
    private void process(int newType) {
        if (newType == type) {
            if (type == 3) {
                addPic1(STICKERS[0]);
            } else if (type == 4) {
                addPic1(STICKERS[1]);
            }
            return;
        }
        if (type == 1) {
            bmImage = ivCrop.getCroppedImage();
            ImageUtils.save(bmImage, newPath, Bitmap.CompressFormat.JPEG);
        } else if (type == 2) {
            bmImage = ivMarsic.getMosaicBitmap();
            ImageUtils.save(bmImage, newPath, Bitmap.CompressFormat.JPEG);
        } else if (type == 3) {
            if (newType == 4) {
                addPic1(STICKERS[1]);
                return;
            } else {
                saveStick();
            }
        } else if (type == 4) {
            if (newType == 3) {
                addPic1(STICKERS[0]);
                return;
            } else {
                saveStick();
            }
        }
        type = newType;
        switch (type) {
            case 1:
                prepareCrop();
                break;
            case 2:
                prepareMarsic();
                break;
            case 3:
                prepareStick();
                break;
            case 4:
                prepareStick();
                break;
        }
    }

    @OnClick({R.id.tvCrop, R.id.tvMarsic, R.id.tvDel, R.id.tvArrow, R.id.tvCircle, R.id.tvCamera, R.id.tvOK})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCrop:
                process(1);
                break;
            case R.id.tvMarsic:
                process(2);
                break;
            case R.id.tvArrow:
                process(3);
                break;
            case R.id.tvCircle:
                process(4);
                break;
            case R.id.tvDel:
                showCustomView();
                break;
            case R.id.tvCamera:
                break;
            case R.id.tvOK://保存
                saveStickers();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FileUtils.deleteFile(newPath);
    }

    /**
     * 切换底图Bitmap
     *
     * @param newBit
     */
    public void changeMainBitmap(Bitmap newBit) {
        if (bmImage != null) {
            if (!bmImage.isRecycled()) {// 回收
                bmImage.recycle();
            }
            bmImage = newBit;
        } else {
            bmImage = newBit;
        }// end if
        ivShow.setImageBitmap(bmImage);
    }

    /**
     * 保存贴图层 合成一张图片
     */
    public void saveStickers() {
        // System.out.println("保存 合成图片");
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
        mSaveTask = new SaveStickersTask(TestPhotoEditActivity1.this);
        mSaveTask.execute(bmImage);
    }

    /**
     * 保存贴图任务
     *
     * @author panyi
     */
    private final class SaveStickersTask extends StickerTask {

        public SaveStickersTask(TestPhotoEditActivity1 activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            LinkedHashMap<Integer, StickerItem> addItems = stickerPanel.getBank();
            for (Integer id : addItems.keySet()) {
                StickerItem item = addItems.get(id);
                item.matrix.postConcat(m);// 乘以底部图片变化矩阵
                canvas.drawBitmap(item.bitmap, item.matrix, null);
            }// end for
        }

        @Override
        public void onPostResult(Bitmap result) {
            stickerPanel.clear();
           changeMainBitmap(result);
        }
    }// end inner class


    public static Dialog getLoadingDialog(Context context, int titleId,
                                          boolean canCancel) {
        return getLoadingDialog(context,context.getString(titleId),canCancel);
    }


    public static Dialog getLoadingDialog(Context context, String title,
                                          boolean canCancel) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(canCancel);
        dialog.setMessage(title);
        return dialog;
    }

    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new MaterialDialog.Builder(this)
//                .title(R.string.progress_dialog)
                .content("正在加载,请稍后")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
    }

    /**
     * 隐藏dialog
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showCustomView(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("请先确认如下信息")
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.test_dialog, true)
                .titleColor(UIUtils.getColor(R.color.cardview_light_background))//
                .backgroundColor(UIUtils.getColor(R.color.common_btn_blue))
//                .positiveText("开始检测")
//                .negativeText("取消")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        MyToast.showShort("确定");
//                    }
//                })
               .build();
        TextView cancel = (TextView) dialog.getCustomView().findViewById(R.id.tv_cancel);
        TextView tvOK = (TextView) dialog.getCustomView().findViewById(R.id.tv_ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showShort("取消");
            }
        });
        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showShort("确定");
            }
        });
        dialog.show();
    }

}
