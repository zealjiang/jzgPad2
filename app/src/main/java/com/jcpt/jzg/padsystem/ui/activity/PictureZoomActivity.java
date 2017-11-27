package com.jcpt.jzg.padsystem.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ImageUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.AppManager;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.vo.EventProcedurePhoto;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.MyPhotoView;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;

import static com.jcpt.jzg.padsystem.app.PadSysApp.picDirPath;


/**
 * Created by libo on 2016/11/14.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 图片浏览界面 手势可放大缩小
 */
public class PictureZoomActivity extends Activity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vPager)
    MyPhotoView vPager;
    @BindView(R.id.tvRecapture)
    TextView tvRecapture;
    @BindView(R.id.tvDel)
    TextView tvDel;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ivRotate)
    ImageView ivRotate;
    @BindView(R.id.tvRestore)
    Button tvRestore;
    @BindView(R.id.tvSave)
    Button tvSave;

    private String url;//显示的url或者filePath
    private boolean showRecapture;  //是否需要显示重拍按钮
    private boolean isdel = false;  //是否需要显示删除按钮

    private Context mContext;
    private LayoutInflater inflater;
    private View item;
    private MyViewPagerAdapter picAdapter;
    /**
     * adapter里填充的view集合
     */
    private ArrayList<PhotoDraweeView> list = new ArrayList<>();

    /**
     * 图片地址集合完整
     */
    private ArrayList<PictureItem> items = new ArrayList<>();
    /**
     * 图片显示地址集合
     */
    private ArrayList<PictureItem> showItems = new ArrayList<>();

    //当前点击查看大图位置
    private int curPosition = 0;

    //当前显示位置
    private int curShowPosition = 0;

    //上一张图片显示位置
    private int oldShowPosition = 0;
    //记录当前图片的初始旋转角度
    private int initRotateDegree;
    //记录当前图片的当前旋转角度
    private int curRotateDegree;

    private String taskid = "";
    //是否是缩略图进入
    private boolean isThumbnail = false;

    private ArrayList<String> highQualityPicIdArray;
    private final int REQUEST_TAKE_PHOTO = 10;
    private FrescoImageLoader frescoImageLoader;

    private boolean isCarPhotoProdedureFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        //去除title 标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_picture_zoom);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        showRecapture = getIntent().getBooleanExtra("showRecapture", false);
        isdel = getIntent().getBooleanExtra("isDel", false);
        isCarPhotoProdedureFragment = getIntent().getBooleanExtra("isCarPhotoProdedureFragment", false);
        items = (ArrayList<PictureItem>) getIntent().getSerializableExtra("pictureItems");
        curPosition = getIntent().getIntExtra("curPosition", 0);
        taskid = getIntent().getStringExtra("taskid");
        isThumbnail = getIntent().getBooleanExtra("isThumbnail", false);
        highQualityPicIdArray = getIntent().getStringArrayListExtra("highQualityPicIdArray");//高质量图片的ID

        if (showRecapture) {
            tvRecapture.setVisibility(View.VISIBLE);
        } else {
            tvRecapture.setVisibility(View.GONE);
        }
        if (isdel) {
            tvDel.setVisibility(View.VISIBLE);
        } else {
            tvDel.setVisibility(View.GONE);
        }

        frescoImageLoader = FrescoImageLoader.getSingleton();


        if (items != null && items.size() > 0) {

            inflater = LayoutInflater.from(getApplicationContext());

            //查询出有图片的地址，放入显示的集合中
            showItems.addAll(items);
            for (int i = 0; i < items.size(); i++) {
                if (TextUtils.isEmpty(items.get(i).getPicPath())) {
                    showItems.remove(items.get(i));
                }
            }

            for (int i = 0; i < showItems.size(); i++) {
                item = inflater.inflate(R.layout.viewpager_big_pics_item, null);
                PhotoDraweeView image = ((PhotoDraweeView) item.findViewById(R.id.viewpager_photoDraweeView));
                list.add(image);
            }
            //如果图片id是数字则排序
//            if(StringHelper.isNumeric(showItems.get(0).getPicId())){
//                Collections.sort(showItems);
//            }

            //遍历显示集合，查询传入的点击位置对应的显示位置
            for (int i = 0; i < showItems.size(); i++) {
                if (showItems.get(i).getPicId().equals(items.get(curPosition).getPicId())) {
                    curShowPosition = i;
                    break;
                }
            }

            String picId = showItems.get(curShowPosition).getPicId();

            if (isCarPhotoProdedureFragment) {

                if (picId.equals("23") || picId.equals("28")) {
                    tvDel.setVisibility(View.VISIBLE);
                } else {
                    tvDel.setVisibility(View.GONE);
                }
            }

            picAdapter = new MyViewPagerAdapter(list, mContext, showItems);
            vPager.setAdapter(picAdapter);
            vPager.setCurrentItem(curShowPosition);
            vPager.addOnPageChangeListener(this);
            vPager.setOffscreenPageLimit(2);
            if (showItems.size() <= curShowPosition) {
                finish();
                return;
            }
            tv_title.setText(showItems.get(curShowPosition).getPicName());

            tvRestore.setEnabled(false);
            tvSave.setEnabled(false);
        } else {
            finish();
            return;
        }

        picAdapter.refreshView(curShowPosition);
        //获得当前图片的初始角度
        initRotateDegree = ImageUtils.getRotateDegree(showItems.get(curShowPosition).getPicPath());
        curRotateDegree = initRotateDegree;
        oldShowPosition = curShowPosition;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        curShowPosition = position;
        tv_title.setText(showItems.get(curShowPosition).getPicName());
        String picId = showItems.get(curShowPosition).getPicId();

        if (isCarPhotoProdedureFragment) {

            if (picId.equals("23") || picId.equals("28")) {
                tvDel.setVisibility(View.VISIBLE);
            } else {
                tvDel.setVisibility(View.GONE);
            }
        }
        //获得上一张图片的旋转角度并进行旋转
        if (oldShowPosition != curShowPosition) {
            if (curRotateDegree != initRotateDegree) {
                setPicRotate(showItems.get(oldShowPosition).getPicPath(), initRotateDegree);
                picAdapter.refreshView(oldShowPosition);
            }
        }

        //获得当前图片的初始角度
        initRotateDegree = ImageUtils.getRotateDegree(showItems.get(curShowPosition).getPicPath());
        curRotateDegree = initRotateDegree;


        oldShowPosition = curShowPosition;
        tvRestore.setEnabled(false);
        tvSave.setEnabled(false);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyViewPagerAdapter extends PagerAdapter {

        private List<PhotoDraweeView> mList;
        private List<PictureItem> mUrls;

        public MyViewPagerAdapter(List<PhotoDraweeView> mList, Context context, List<PictureItem> mUrls) {
            this.mList = mList;
            this.mUrls = mUrls;
        }

        public void setData(List<PhotoDraweeView> mList, List<PictureItem> mUrls) {
            this.mList = mList;
            this.mUrls = mUrls;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            if (position < getCount()) {
                PhotoDraweeView view = mList.get(position);
                view.setDrawingCacheEnabled(true);
                Bitmap bitmap = view.getDrawingCache();
                if (bitmap != null) {
                    bitmap.recycle();
                }
                view.setDrawingCacheEnabled(false);
                container.removeView(view);

                if (mUrls.get(position).getPicPath().startsWith("http")) {
                    Fresco.getImagePipeline().evictFromMemoryCache(Uri.parse(mUrls.get(position).getPicPath()));

                } else {
                    Fresco.getImagePipeline().evictFromCache(Uri.parse("file://" + mUrls.get(position).getPicPath()));
                }

            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        /**
         * Create the page for the given position.
         */
        @SuppressLint("NewApi")
        @Override
        public Object instantiateItem(final ViewGroup container,
                                      final int position) {
            if (position < getCount()) {
                PhotoDraweeView view = mList.get(position);
                frescoImageLoader.displayImageBig(view, mUrls.get(position).getPicPath());
                container.addView(view);
                return view;
            } else {
                return null;
            }

        }


        public void refreshView(int position) {
            View view = mList.get(position);
            PhotoDraweeView image = ((PhotoDraweeView) view.findViewById(R.id.viewpager_photoDraweeView));
            Fresco.getImagePipeline().evictFromCache(Uri.parse("file://" + mUrls.get(position).getPicPath()));
            frescoImageLoader.displayImageBig(image, mUrls.get(position).getPicPath());
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }


    /**
     * 旋转图片
     */
    private void rotate(Uri uri, SimpleDraweeView simpleDraweeView) {

        ImageRequest build = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(build)
                .build();
        simpleDraweeView.setController(controller);
    }

    @OnClick({R.id.ivBack, R.id.tvRecapture, R.id.tvDel, R.id.ivRotate, R.id.tvRestore, R.id.tvSave})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ivBack:
//                if (isDel){
//                    Intent intent1 = getIntent();
//                    //删除位置 0214 郑有权
//                    intent1.putExtra("recapturePosition", curPosition);
//                    intent1.putExtra("isDel", true);
//                    setResult(RESULT_OK, intent1);
//                }
                finish();
                break;
            case R.id.tvRecapture:
                //点击显示的图片与传入集合对应的图片位置
                for (int i = 0; i < items.size(); i++) {
                    if (showItems.get(curShowPosition).getPicId().equals(items.get(i).getPicId())) {
                        curPosition = i;
                        break;
                    }
                }
                //如果是点击相机下缩列图进入
                if (isThumbnail) {
                    intent = getIntent();
                    intent.putExtra("recapture", true);
                    intent.putExtra("isDel", false);
                    //重拍位置 0214 郑有权
                    intent.putExtra("recapturePosition", curPosition);
                    intent.putExtra("pictureItems", items);//created by wujj on 5.9
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    boolean isClickDefectPhoto = getIntent().getBooleanExtra("isClickDefectPhoto", false);
                    if (isClickDefectPhoto) {
                        Intent intent1 = new Intent();
                        intent1.putExtra("recapture", true);
                        intent1.putExtra("isDel", false);
                        setResult(RESULT_OK, intent1);
                    } else {
                        if (items.size() == 1) {
                            userCamera(curPosition, Constants.CAPTURE_TYPE_SINGLE);
                        } else {
                            userCamera(curPosition, Constants.CAPTURE_TYPE_MULTI);
                        }
                    }
                    finish();
                }
                break;
            case R.id.tvDel:
                for (int i = 0; i < items.size(); i++) {
                    if (showItems.get(curShowPosition).getPicId().equals(items.get(i).getPicId())) {
                        curPosition = i;
                        break;
                    }
                }
                showConfirmTask(showItems.get(curShowPosition).getPicId());
                break;
            case R.id.ivRotate:
                //如果是复检的单子，可能是网络图片,在做旋转之前应该先将网络图片保存在本地再进行旋转
                String path = showItems.get(curShowPosition).getPicPath();
                if (!StringUtils.isEmpty(path) && path.startsWith("http")) {
                    FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainFileCache().getResource(new
                            SimpleCacheKey(path));
                    File file = resource.getFile();
                    if (file != null) {
                        String locPath = PadSysApp.picDirPath + showItems.get(curShowPosition).getPicId() + ".jpg";
                        boolean isSuc = FileUtils.copyFile(file, new File(locPath));
                        if (isSuc) {
                            showItems.get(curShowPosition).setPicPath(locPath);
                        }
                    } else {
                        //TODO  如果从Fresco缓存中获取图片失败(例如：网络不好没下载好)就要从网络下载此图片
                    }
                }

                curRotateDegree = turnLeft90(curRotateDegree);
                setPicRotate(showItems.get(curShowPosition).getPicPath(), curRotateDegree);
                picAdapter.refreshView(curShowPosition);


                if (curRotateDegree != initRotateDegree) {
                    tvRestore.setEnabled(true);
                    tvSave.setEnabled(true);
                } else {
                    tvRestore.setEnabled(false);
                    tvSave.setEnabled(false);
                }

                break;
            case R.id.tvRestore:
                setPicRotate(showItems.get(curShowPosition).getPicPath(), initRotateDegree);
                picAdapter.refreshView(curShowPosition);
                curRotateDegree = initRotateDegree;
                tvRestore.setEnabled(false);
                tvSave.setEnabled(false);
                break;
            case R.id.tvSave:
                initRotateDegree = curRotateDegree;
                tvRestore.setEnabled(false);
                tvSave.setEnabled(false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            //刷新
            picAdapter.refreshView(curShowPosition);
            curPosition = data.getIntExtra("curPosition", curShowPosition);
            curShowPosition = curPosition;

            vPager.setCurrentItem(curShowPosition);
            tv_title.setText(showItems.get(curShowPosition).getPicName());
//            Intent intent1 = new Intent();
//            intent1.putExtra("recapture",true);
//            intent1.putExtra("isDel",false);
//            setResult(RESULT_OK,intent1);
//            finish();

        }
    }

    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position) {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", taskid);
        intent.putExtra(Constants.CAPTURE_TYPE, Constants.CAPTURE_TYPE_SINGLE);//拍摄模式，是单拍还是连拍
        if (highQualityPicIdArray != null) {
            //高质量图片的ID
            intent.putStringArrayListExtra("highQualityPicIdArray", highQualityPicIdArray);
        }

        ArrayList<PictureItem> singleList = new ArrayList<>();
        singleList.add(items.get(position));
        intent.putExtra("picList", singleList);
        intent.putExtra("position", 0);

        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position, int capture_type) {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", taskid);
        intent.putExtra(Constants.CAPTURE_TYPE, capture_type);//拍摄模式，是单拍还是连拍
        //高质量图片的ID
        intent.putStringArrayListExtra("highQualityPicIdArray", highQualityPicIdArray);
        if (capture_type == Constants.CAPTURE_TYPE_MULTI) {
            intent.putExtra("picList", items);
            intent.putExtra("position", position);
        } else {
            ArrayList<PictureItem> singleList = new ArrayList<>();
            singleList.add(items.get(position));
            intent.putExtra("picList", singleList);
            intent.putExtra("position", 0);
        }
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 确认任务信息
     */
    public void showConfirmTask(final String PicId) {
        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(this);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_photo_incomplete);
        TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        tvMsg.setText("请确认是否需要删除此照片？");
        TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
        tvleft.setText("否");
        TextView tvright = (TextView) view.findViewById(R.id.tvright);
        tvright.setText("是");
        myUniversalDialog.setLayoutView(view);
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
                //删除本地图片
                String path = picDirPath + PicId + ".jpg";
                FrescoCacheHelper.clearSingleCacheByUrl(path, true);


                if (isCarPhotoProdedureFragment && (PicId.equals("23") || PicId.equals("28"))) {

                    delHttpUrl(PicId);
                    finish(); //删除更多登记后直接关闭浏览大图界面  -> 李波 on 2017/9/25.
                } else {

                    Intent intent1 = getIntent();
                    //删除位置 0214 郑有权
                    intent1.putExtra("recapturePosition", curPosition);
                    intent1.putExtra("isDel", true);
                    setResult(RESULT_OK, intent1);
                    finish();
                }
            }
        });
    }

    private void setPicRotate(String path, int degree) {

        int exifD = ExifInterface.ORIENTATION_NORMAL;
        int newD = degree % 360;
        switch (newD) {
            case -90:
                exifD = ExifInterface.ORIENTATION_ROTATE_270;
                break;
            case -180:
                exifD = ExifInterface.ORIENTATION_ROTATE_180;
                break;
            case -270:
                exifD = ExifInterface.ORIENTATION_ROTATE_90;
                break;
            case 90:
                exifD = ExifInterface.ORIENTATION_ROTATE_90;
                break;
            case 180:
                exifD = ExifInterface.ORIENTATION_ROTATE_180;
                break;
            case 270:
                exifD = ExifInterface.ORIENTATION_ROTATE_270;
                break;
            case 0:
                exifD = ExifInterface.ORIENTATION_NORMAL;
                break;
        }

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(exifD));
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int toPositiveDegree(int degree) {
        int newD = degree % 360;
        switch (newD) {
            case -90:
                return 270;
            case -180:
                return 180;
            case -270:
                return 90;
            case 0:
                return 0;
            default:
                return newD;
        }
    }

    /**
     * 逆时针旋转90度后的度数，十字坐标系：右为0度，下为90度
     *
     * @param degree
     * @return
     */
    private int turnLeft90(int degree) {
        int pDegree = toPositiveDegree(degree);
        switch (pDegree) {
            case 0:
                return 270;
            case 90:
                return 0;
            case 180:
                return 90;
            case 270:
                return 180;
            default:
                return degree;
        }
    }

    /**
     * 将图片旋转degree度并保存图片
     *
     * @param path
     * @param degree
     * @return
     */
    private boolean savePicRotate2(String path, int degree) {
        Bitmap bitmap = ImageUtils.getBitmapByFile(path);
        if (bitmap == null) {
            return false;
        }
        Bitmap rBitmap = ImageUtils.rotateBitmap(bitmap, degree);
        return ImageUtils.save(rBitmap, path, Bitmap.CompressFormat.JPEG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (curRotateDegree != initRotateDegree) {
            setPicRotate(showItems.get(curShowPosition).getPicPath(), curRotateDegree);
            picAdapter.refreshView(curShowPosition);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (curRotateDegree != initRotateDegree) {
            setPicRotate(showItems.get(curShowPosition).getPicPath(), initRotateDegree);
            picAdapter.refreshView(curShowPosition);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppManager.getAppManager().finishActivity(this);
        LogUtil.e("curRotateDegree: ", curRotateDegree + "");

        Fresco.getImagePipeline().clearMemoryCaches();
        if (list != null && list.size() > 0) {
            list.clear();
            list = null;
            vPager.removeAllViews();
            vPager = null;

            for (int i = 0; i < showItems.size(); i++) {
                if (!showItems.get(i).getPicPath().startsWith("http")) {
                    Fresco.getImagePipeline().evictFromCache(Uri.parse("file://" + showItems.get(i).getPicPath()));
                } else {
                    Fresco.getImagePipeline().evictFromMemoryCache(Uri.parse(showItems.get(i).getPicPath()));
                }
            }
            System.gc();

        }
    }

    /**
     * Created by 李波 on 2017/9/21.
     * 删除数据库对应图片地址
     */
    private void delHttpUrl(String picId) {
        SubmitModel submitModel = DetectMainActivity.detectMainActivity.getSubmitModel();

        for (int i = 0; i < showItems.size(); i++) {
            if (picId.equals(showItems.get(i).getPicId()))
                //删除网络图片的缓存
                FrescoCacheHelper.clearSingleCacheByUrl(showItems.get(i).getPicPath(), true);
        }

        if (picId.equals("23")) {

            //添加删除参数
//            if (submitModel != null && submitModel.getDeletePicId() != null) {
//                if (!submitModel.getDeletePicId().contains("23")) {
//                    submitModel.getDeletePicId().add("23");
//                }
//            }

            //统一由手续信息处理  -> 李波 on 2017/11/16.
            EventProcedurePhoto eventProcedurePhoto = new EventProcedurePhoto();
            eventProcedurePhoto.setDelRegister23(true);
            EventBus.getDefault().post(eventProcedurePhoto);

//            DetectMainActivity.detectMainActivity.setPathRegisterMorePI("");
        }

        if (picId.equals("28")) {

            //添加删除参数
//            if (submitModel != null && submitModel.getDeletePicId() != null) {
//                if (!submitModel.getDeletePicId().contains("28")) {
//                    submitModel.getDeletePicId().add("28");
//                }
//            }
//统一由手续信息处理  -> 李波 on 2017/11/16.
            EventProcedurePhoto eventProcedurePhoto = new EventProcedurePhoto();
            eventProcedurePhoto.setDelRegister28(true);
            EventBus.getDefault().post(eventProcedurePhoto);

//            DetectMainActivity.detectMainActivity.setPathRegisterMorePI2("");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

