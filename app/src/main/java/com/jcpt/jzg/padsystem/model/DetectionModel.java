package com.jcpt.jzg.padsystem.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.DefectSelectRecyclerViewAdapter;
import com.jcpt.jzg.padsystem.adapter.DetectionItemAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.SearchCheckActivity;
import com.jcpt.jzg.padsystem.ui.fragment.DetectionFragment;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel.CheckPositionListBean;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel.CheckPositionListBean.PartsPositionListBean;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel.CheckPositionListBean.PartsPositionListBean.DefectTypeListBean;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel.CheckPositionListBean.PartsPositionListBean.DefectTypeListBean.ImgListBean;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectDetailItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectType;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.CustomButton;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


/**
 * Created by libo on 2016/11/23.
 * <p>
 * 车况检测-- 初始化大对象 （DetectMainActivity --> localDetectionData）
 * 所有的操作将实时反馈写入到初始化大对象里，往后回显和需要取数据均从大对象里取出
 *
 * @Email: libo@jingzhengu.com
 * @Description: 车况检测逻辑 Model
 */
public class DetectionModel {
    public Activity activity;
    public static final int PHOTO_REQUEST = 10;
    public static final int PHOTO_BIG_PHOTO = 11;
    public static final int DEFECT_ITEM = 001; //小缺陷项三张照片的拍照请求标识  -> 李波 on 2016/11/25.
    public static final int DEFECT_ITEM_RECAPTURE = 003; //小缺陷项三张照片的拍照请求标识  -> 李波 on 2016/11/25.
    public static final int POSITION = 002;    //方位照片的拍照请求标识  -> 李波 on 2016/11/25.
    private PopupWindow popupWindow;
    private TextView tvCheckItemkDesc;           //当前检测项描述  -> 郑有权 on 2011/1/8.
    private TextView tvName;           //当前检测项名字  -> 李波 on 2016/11/24.
    private RecyclerView recyclerView; //当前检测项对应的缺陷列表  -> 李波 on 2016/11/24.
    private CustomButton btnComplete;  //完成按钮  -> 李波 on 2016/11/24.
    private static String taskId;
    private FrescoImageLoader frescoImageLoader;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * Created by 李波 on 2016/11/24.
     * <p>
     * 每个检测项对应的缺陷列表
     * 里面包含某一类缺陷和对应此类的小缺陷项集合，而每个小缺陷项里又包含三张缺陷图片
     */
    public List<DefectType> defectTypeList;

    //Submit里面 最终要提交的缺陷ID集合，这里有缺陷被选中就直接对应存进去  -> 李波 on 2016/12/5.
    private ArrayList<String> defectValues;

    public DetectionModel(Activity activity) {
        this.activity = activity;

        if (DetectMainActivity.detectMainActivity != null && DetectMainActivity.detectMainActivity.getSubmitModel() != null)
            defectValues = (ArrayList<String>) DetectMainActivity.detectMainActivity.getSubmitModel().getDefectValue();

        frescoImageLoader = FrescoImageLoader.getSingleton();
    }


    /**
     * Created by 李波 on 2016/11/24.
     * <p>
     * 显示缺陷项的侧滑菜单（popwindow）
     *
     * @param checkItemList    检测项集合
     * @param curClickPosition 当前点击的检测项角标
     */
    public void showDefectPopWindow(List<CheckItem> checkItemList, int curClickPosition) {
        {
            View contentView = View.inflate(activity, R.layout.popupwindow_defect, null);
            tvName = (TextView) contentView.findViewById(R.id.tvName);
            tvCheckItemkDesc = (TextView) contentView.findViewById(R.id.tvCheckItemkDesc);
            recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
            btnComplete = (CustomButton) contentView.findViewById(R.id.btnComplete);

            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            popupWindow = new PopupWindow(contentView);

            //获取每个检测项的缺陷列表  -> 李波 on 2016/11/24.
            defectTypeList = checkItemList.get(curClickPosition).getDefectTypeList();

            DefectSelectRecyclerViewAdapter adapter = new DefectSelectRecyclerViewAdapter(activity, defectTypeList, taskId, checkItemList.get(curClickPosition).getCheckName());
            recyclerView.setAdapter(adapter);


//            MyToast.showShort("左边");
            popupWindow.setWidth(UIUtils.dip2px(activity, 600));
            popupWindow.setHeight(GridLayoutManager.LayoutParams.MATCH_PARENT);
            popupWindow.setFocusable(true);
            View rootView = View.inflate(activity, R.layout.fragment_detection_important, null);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.AnimRight);
            popupWindow.showAtLocation(rootView, Gravity.RIGHT, 0, 0);
            backgroundAlpha((Activity) activity, 0.5f);

            //设置侧滑菜单对应的当前检测项名称  -> 李波 on 2016/11/24.
            String checkName = checkItemList.get(curClickPosition).getCheckName();
            tvName.setText(checkName);

            //设置侧滑菜单对应的当前检测项描述  -> 郑有权 on 2017/1/8
            String checkItemDesc = checkItemList.get(curClickPosition).getCheckItemDesc();
            tvCheckItemkDesc.setText(checkItemDesc == null ? "说明：" : "说明：" + checkItemDesc);

        }
    }

    /**
     * Created by 李波 on 2016/11/24.
     * <p>
     * 缺陷列表侧滑 popwindow 关闭时的监听
     *
     * @param curClickPosition 当前点击的检测项
     * @param myAdapter        车况检测--检测项的adapter
     */
    public void setPopupWindowListener(final int curClickPosition, final DetectionItemAdapter myAdapter) {
        {
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha((Activity) activity, 1f);
                    int status = 0; //0-小缺陷项未选中，1-小缺陷项选中  -> 李波 on 2016/11/24.
                    boolean isFix = false;

                    //遍历检测项的缺陷列表里面 所有缺陷分类对应的小缺陷的选中状态，根据小缺陷项选中状态设置检测项的状态  -> 李波 on 2016/11/24.
                    //getDefectDetailList为某一类缺陷里面 的小缺陷项集合  -> 李波 on 2016/11/24.
                    for (int i = 0; i < defectTypeList.size(); i++) {
                        for (int j = 0; j < defectTypeList.get(i).getDefectDetailList().size(); j++) {
                            status = defectTypeList.get(i).getDefectDetailList().get(j).getStatus();
                            if (status == 1)
                                break;
                        }
                        if (status == 1)
                            break;
                    }

                    for (int i = 0; i < defectTypeList.size(); i++) {
                        for (int j = 0; j < defectTypeList.get(i).getDefectDetailList().size(); j++) {
                            isFix = defectTypeList.get(i).getDefectDetailList().get(j).isFix();
                            if (isFix)
                                break;
                        }
                        if (isFix)
                            break;
                    }

                    //修改过缺陷详情
                    // 检测项绝对不允许回到空状态  -> 李波 on 2016/12/9.
                    if (isFix) {
                        DetectionFragment.detectionIsFix = true;
                        if (status == 0) { //如果缺陷详情全都没选中，直接将检测项置为正常状态
                            changeStatusAndData(myAdapter, curClickPosition, 1);
                        } else if (status == 1) { //一旦有小缺陷项被选中代表有缺陷，直接将该检测项状态置为 2-缺陷  -> 李波 on 2016/11/24.
                            changeStatusAndData(myAdapter, curClickPosition, 2);
                        }
                    }
// 以下是允许回到空状态，预留，必备需求变更
                    //当没有小缺陷项选中时，先获取检测项状态，只有当检测项为 2-缺陷状态时，才将检测项状态更改为 0-未选择状态  -> 李波 on 2016/11/24.
//                    if (status==0) {
//                        int s = myAdapter.getPositionCheckItem(curClickPosition).getStatus();
//                        if(s==2) {
//                            DetectionFragment.setFix(true);
//                            changeStatusAndData(myAdapter, curClickPosition,0);
//                        }
//                    }else if(status==1) { //一旦有小缺陷项被选中代表有缺陷，直接将该检测项状态置为 2-缺陷  -> 李波 on 2016/11/24.
//                        DetectionFragment.setFix(true);
//                        changeStatusAndData(myAdapter, curClickPosition,2);
//                    }
                }
            });
            btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
//                    MyToast.showShort("完成");
                }
            });
        }
    }

    /**
     * 更改车况检测- -检测项的显示状态 和 对应数据实时更新到大对象
     *
     * @param myAdapter        检测项adpater 通过它拿取对应位置的 item ，然后更改item的 状态 和 实时数据更新到大对象
     * @param curClickPosition 当前点击位置
     * @param status           检测项状态 0 - 未选择，1 - 正常，2 - 缺陷
     */
    public void changeStatusAndData(DetectionItemAdapter myAdapter, int curClickPosition, int status) {
        myAdapter.getPositionCheckItem(curClickPosition).setStatus(status); //更改检测项状态 0-未选择，1-正常，2-缺陷  -> 李波 on 2016/11/24.
        myAdapter.getDatas().get(curClickPosition).setStatus(status); //根据状态实时更改对应数据到大对象  -> 李波 on 2016/11/24.
//        myAdapter.notifyItemChanged(curClickPosition);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }


    /**
     * Created by 李波 on 2016/11/25.
     * <p>
     * 显示拍照后的图片
     *
     * @param sdv_defect      图片
     * @param position        图片在 图片id 集合里对应的位置
     * @param photoPrefixList 图片的id 集合,用作拼接照相后图片名字的前缀
     */
    public void showCameraPhoto(SimpleDraweeView sdv_defect, int position, List<String> photoPrefixList) {

        String photoPrefix = "";
        if (photoPrefixList != null) {
            photoPrefix = photoPrefixList.get(position);
        }

        querySDphoto(sdv_defect, photoPrefix);

    }

    /**
     * Created by 李波 on 2016/11/28.
     * <p>
     * 查询SD卡下拍照的照片根据图片的id前缀取出图片来显示
     * 并且删掉同id的历史照片
     *
     * @param sdv_defect  要显示的 SimpleDraweeView
     * @param photoPrefix 图片的 id
     */
    private void querySDphoto(SimpleDraweeView sdv_defect, String photoPrefix) {
        File filePad2 = new File(PadSysApp.picDirPath);
        File[] files = filePad2.listFiles();

        String CurPhotoPath = "";

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String photoName = files[i].getName();
                if (photoName.endsWith(".jpg")) {
                    String photoNamePrefixId = photoName.substring(0, photoName.indexOf("."));
                    if (photoNamePrefixId.equals(photoPrefix)) {
                        CurPhotoPath = files[i].getAbsolutePath();
                        break;
                    }
                }
            }
        }

        frescoImageLoader.displayImage(sdv_defect, CurPhotoPath);
    }


    /**
     * 取出显示带有时间戳的图片 --- 目前不用，作为以后不时之需备用
     * Created by 李波 on 2016/11/28.
     * <p>
     * 查询SD卡下拍照的照片根据图片的id前缀取出图片来显示
     * 并且删掉同id的历史照片
     *
     * @param sdv_defect  要显示的 SimpleDraweeView
     * @param photoPrefix 图片的 id
     */
    private void querySDphotoTime(SimpleDraweeView sdv_defect, String photoPrefix) {
        File filePad2 = new File(PadSysApp.picDirPath);
        File[] files = filePad2.listFiles();

        //装入同位置同Id多次拍照的所有照片以便取出时间最新的一张显示，其他的历史删除  -> 李波 on 2016/11/25.
        ArrayList<String> photoList = new ArrayList<String>();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String photoName = files[i].getName();
                if (photoName.endsWith(".jpg") && photoName.contains("-")) {
                    String photoNamePrefixId = photoName.substring(0, photoName.indexOf("-"));
                    if (photoNamePrefixId.equals(photoPrefix)) {
                        photoList.add(files[i].getName());
                    }
                }
            }
        }

        //（选择排序）排序--时间倒序，即把时间最新的放最前面  -> 李波 on 2016/11/25.
        for (int x = 0; x < photoList.size() - 1; x++) {
            for (int y = x + 1; y < photoList.size(); y++) {
                String photoName_x = photoList.get(x);
                String photoTime_x = photoName_x.substring(photoName_x.indexOf("-") + 1, photoName_x.lastIndexOf("."));
                long photoTime_xL = Long.valueOf(photoTime_x);

                String photoName_y = photoList.get(y);
                String photoTime_y = photoName_y.substring(photoName_y.indexOf("-") + 1, photoName_y.lastIndexOf("."));
                long photoTime_yL = Long.valueOf(photoTime_y);

                if (photoTime_xL < photoTime_yL) {
                    String tempX = new String(photoList.get(x));
                    String tempY = new String(photoList.get(y));

                    photoList.set(x, tempY);
                    photoList.set(y, tempX);

                }
            }
        }

        //排序后取出最新的一张照片来显示  -> 李波 on 2016/11/25.
        String CurPhotoPath = "";
        if (photoList.size() > 0)
            CurPhotoPath = PadSysApp.picDirPath + photoList.get(0);

        Uri uri = Uri.parse("file://" + CurPhotoPath);
        sdv_defect.setImageURI(uri);

        //取出最新拍照照片后其他同id的前缀的历史照片全部删除  -> 李波 on 2016/11/25.
        for (int i = 1; i < photoList.size(); i++) {
            String historyPhoto = PadSysApp.picDirPath + photoList.get(i);
            FileUtils.deleteFile(historyPhoto);
        }
    }

    /**
     * Created by 李波 on 2016/11/28.
     * <p>
     * 显示拍照后的图片
     *
     * @param sdv_defect    图片
     * @param photoPrefixId 图片 id
     */
    public void showCameraPhoto(SimpleDraweeView sdv_defect, String photoPrefixId) {
        querySDphoto(sdv_defect, photoPrefixId);
    }

    /**
     * Created by 李波 on 2016/11/24.
     * <p>
     * 调用相机拍照
     *
     * @param photoPrefix 照片的唯一 Id 标示，这里用来作为保存照片的前缀名字
     */
    public void camera(String photoPrefix, Activity activity, int requestCode, String taskId, String photoName) {
//        //调用系统相机
//        Intent intent = new Intent(
//                MediaStore.ACTION_IMAGE_CAPTURE);
//        //照片 id 作为图片名称  -> 李波 on 2016/11/29.
//        String photoName = photoPrefix + ".jpg";
//        //请求权限
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) {
                    FileUtils.createOrExistsDir(PadSysApp.picDirPath);
                } else {
                    MyToast.showShort("需要获取SD卡读写权限来保存图片");
                }
            }
        });
//        //指定照片存储路径  -> 李波 on 2016/11/25.
//        File file = new File(PadSysApp.picDirPath, photoName);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
////        ((DetectMainActivity) context).startActivityForResult(intent, requestCode);
//        try {
//            ((DetectMainActivity) activity).startActivityForResult(intent, requestCode);
//        }catch (Exception e){
//            ((SearchCheckActivity) activity).startActivityForResult(intent, requestCode);
//        }

        //调用自定义相机
        Intent intent = new Intent(activity,CameraActivity.class);
        intent.putExtra("showGallery",true);//是否显示从相册选取的图标
        intent.putExtra("taskId",taskId);
        intent.putExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_SINGLE);//拍摄模式，是单拍还是连拍

            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem pictureItem = new PictureItem();
            pictureItem.setPicId(photoPrefix);
            pictureItem.setPicName(photoName);
            singleList.add(pictureItem);
            intent.putExtra("picList",singleList);
            intent.putExtra("position",0);

        try {
            ((DetectMainActivity) activity).startActivityForResult(intent, requestCode);
        }catch (Exception e){
            ((SearchCheckActivity) activity).startActivityForResult(intent, requestCode);
        }

    }

    /**
     * Created by 李波 on 2016/11/25.
     * <p>
     * 6.0以上动态设置相机权限
     * 打开相机之前动态获取权限
     *
     * @param photoPrefix 图片的id 用于拍照后的前缀名称
     * @param context
     */
    public void applyPermission(final String photoPrefix,final Activity context, final int requestCode, final String taskId, final String photoName) {
        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions.request(Manifest.permission.CAMERA) //请求相机权限
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
//                            Toast.makeText(context, "成功授权", Toast.LENGTH_SHORT).show();
                            camera(photoPrefix, context, requestCode, taskId, photoName);

                        } else {
                            MyToast.showLong("此功能需要开启相机权限!");
                        }
                    }
                });
    }

    /**
     * 获取本方位重点项是否全部检测
     *
     * @param checkItemList 重点检测项集合
     * @return status 0：有未检测项，-1：已全部检测
     */
    public int getImportantSelStatus(List<CheckItem> checkItemList) {
        int status = -1;
        for (int i = 0; i < checkItemList.size(); i++) {
            status = checkItemList.get(i).getStatus();
            if (status == 0) {
                break;
            }
        }
        return status;
    }


    /**
     * 跳转自定义相机
     *
     * @param position     当前点击的图片位置
     * @param capture_type 拍摄模式，是单拍还是连拍
     * @param taskId       当前任务 Id
     * @param pictureItems 当前点击图片的图片集合
     */
    public void userCamera(int position, int capture_type, String taskId, ArrayList<PictureItem> pictureItems) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", taskId);
//        intent.putExtra("position",position);
        intent.putExtra(Constants.CAPTURE_TYPE, capture_type);//拍摄模式，是单拍还是连拍
        if (capture_type == Constants.CAPTURE_TYPE_MULTI) {
            intent.putExtra("picList", pictureItems);
            intent.putExtra("position", position);
        } else {

            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem pictureItem = new PictureItem();
            pictureItem.setPicId(pictureItems.get(position).getPicId());
            singleList.add(pictureItem);
            intent.putExtra("picList", singleList);
            intent.putExtra("position", 0);
        }
//        ((DetectMainActivity) context).startActivityForResult(intent, PHOTO_REQUEST);

        try {
            ((DetectMainActivity) activity).startActivityForResult(intent, PHOTO_REQUEST);
        }catch (Exception e){
            ((SearchCheckActivity) activity).startActivityForResult(intent, PHOTO_REQUEST);
        }
    }

    //保存数据到数据库
    public void saveUseTaskToDB() {
        ArrayList<String> submitDefectValues = (ArrayList<String>) DetectMainActivity.detectMainActivity.getSubmitModel().getDefectValue();
        DetectMainActivity.detectMainActivity.getLocalDetectionData().setDefectValueList(submitDefectValues);
        String json = new Gson().toJson(DetectMainActivity.detectMainActivity.getLocalDetectionData());
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_USE_TASK, taskId, PadSysApp.getUser().getUserId(), json);
    }

    /***************车况检测与服务器数据匹配************************************************************************************************/

    /**
     * 车况检测--服务器获取的数据详情与本地初始化数据的匹配映射 回显数据
     * 具体做法：以详情数据为参照，遍历数据详情与大对象（DetectMainActivity.LocalDetectionData）对比，有匹配的就把详情里的值设置到 大对象里
     *
     * @param taskDetailModel 服务器获取的数据对象
     */
    public void equalsDailDataToPadSysAppWrapper(TaskDetailModel taskDetailModel) {

        long startTime = System.currentTimeMillis();

        //获取检测项数据  -> 李波 on 2016/11/30.
        List<CheckPositionListBean> checkPositionListTask = taskDetailModel.getCheckPositionList();
        List<CheckPositionItem> checkPositionList = DetectMainActivity.detectMainActivity.getLocalDetectionData().getCheckPositionList();

/*        //默认将重点检测项都置为正常状态
        for (int i = 0; i < checkPositionList.size(); i++) {
            CheckPositionItem checkPositionItem = checkPositionList.get(i);
            List<ImportantItem> importantList = checkPositionItem.getImportantList();
            //获取重点检测项  -> 李波 on 2016/12/20.
            ImportantItem importantItem = null;
            if (importantList != null) {
                if (importantList.size() == 1) {
                    String importantId = importantList.get(0).getImportantId();
                    if (importantId.equals("1")) {
                        importantItem = importantList.get(0);
                    }
                } else if (importantList.size() == 2) {//有重点和非重点 ImportantId : "1" -- 重点，0 -- 非重点
                    String importantId = importantList.get(0).getImportantId();
                    if (importantId.equals("1")) {
                        importantItem = importantList.get(0);
                    } else {
                        importantItem = importantList.get(1);
                    }
                }
                //默认将重点检测项都置为正常状态
                if (importantItem != null) {
                    List<CheckItem> impCheckItemList = importantItem.getCheckItemList();
                    for (int k = 0; k < impCheckItemList.size(); k++) {
                        impCheckItemList.get(k).setStatus(1);
                    }
                }
            }
        }*/

        if (checkPositionListTask != null) {
            //遍历检测数据详情根据方位 id 来匹配更改 检测大对象对应的数据  -> 李波 on 2016/11/30.
            for (int i = 0; i < checkPositionListTask.size(); i++) {
                String checkPositionTaskId = checkPositionListTask.get(i).getCheckPositionID();
                for (int j = 0; j < checkPositionList.size(); j++) {
                    String checkPositionId = checkPositionList.get(j).getCheckPositionId();
                    if (checkPositionTaskId.equals(checkPositionId)) {
                        //如果有匹配的id 就取出对应方位检测项对象 把数据详情数据对应设置到检测大对象里  -> 李波 on 2016/11/30.
                        CheckPositionItem checkPositionItem = checkPositionList.get(j);
                        List<ImportantItem> importantList = checkPositionItem.getImportantList();

                        //取出方案里，重点与非重点检测项  -> 李波 on 2016/11/30.
                        ImportantItem importantItem = new ImportantItem();//重点先拿出，后面非重点根据情况来拿取  -> 李波 on 2016/12/1.
                        //取出方案里的重点检测项
                        if (importantList != null) {
                            if (importantList.size() == 1) {
                                String importantId = importantList.get(0).getImportantId();
                                if (importantId.equals("1")) {
                                    importantItem = importantList.get(0);
                                }
                            } else if (importantList.size() == 2) {//有重点和非重点 ImportantId : "1" -- 重点，0 -- 非重点
                                String importantId = importantList.get(0).getImportantId();
                                if (importantId.equals("1")) {
                                    importantItem = importantList.get(0);
                                } else {
                                    importantItem = importantList.get(1);
                                }
                            }
                        }

                        //-----------进行方位下面检测项的数据映射  -> 李波 on 2016/11/30.

                        //取出数据详情检测项集合  -> 李波 on 2016/11/30.
                        List<PartsPositionListBean> partsPositionList = checkPositionListTask.get(i).getPartsPositionList();

                        //取出大对象检测项集合  -> 李波 on 2016/11/30.
                        List<CheckItem> impCheckItemList = importantItem.getCheckItemList();
//                      List<CheckItem> UmImpCheckItemList = UmImportantItem.getCheckItemList();

                        if (partsPositionList != null && partsPositionList.size() > 0) {
                            //遍历数据详情检测项集合，根据 检测项 ID 匹配更改检测大对象里的数据  -> 李波 on 2016/11/30.
                            for (int k = 0; k < partsPositionList.size(); k++) {
                                PartsPositionListBean partsPositionListBean = partsPositionList.get(k);//详情里方位下的检测项

                                //区分重点还是非重点 isimportant,1--重点，0-非重点 -> 李波 on 2016/11/30.
                                int isImportant = partsPositionListBean.getIsImportant();
                                //获取检测项的 id，下面根据重点非重点和此id 就能确定某方位下的检测项  -> 李波 on 2016/12/1.
                                String checkId = partsPositionListBean.getCheckID();///详情里方位下的某检测项checkId

                                if (isImportant == 1) {
                                    if (impCheckItemList != null)//方位下的重点检测项
                                        DataMate(impCheckItemList, partsPositionListBean, checkId);
                                } else {
                                    if (importantList != null) {
                                        ImportantItem UmImportantItem = null;
                                        if (importantList.size() == 1) {
                                            String importantId = importantList.get(0).getImportantId();
                                            if (importantId.equals("0")) {
                                                UmImportantItem = importantList.get(0);
                                            }
                                        } else if (importantList.size() == 2) {//有重点和非重点 ImportantId : "1" -- 重点，0 -- 非重点
                                            String importantId = importantList.get(0).getImportantId();
                                            if (importantId.equals("0")) {
                                                UmImportantItem = importantList.get(0);
                                            } else {
                                                UmImportantItem = importantList.get(1);
                                            }
                                        }

                                        if (UmImportantItem != null) {
                                            List<CheckItem> UmImpCheckItemList = UmImportantItem.getCheckItemList();//方案里方位下的非重点检测项
                                            if (UmImpCheckItemList != null)
                                                DataMate(UmImpCheckItemList, partsPositionListBean, checkId);
                                        }
                                    }
                                }
                            }
                        } else {  //详情数据无缺陷检测项，说明检测项全是正常状态 --1 重点非重点检测项全置为正常--1 -> 李波 on 2016/12/12.
                            //非重点检测项  -> 李波 on 2016/12/12.
/*                            //将非重点项置为正常
                            if (importantList != null) {
                                ImportantItem UmImportantItem = null;
                                if (importantList.size() == 1) {
                                    String importantId = importantList.get(0).getImportantId();
                                    if (importantId.equals("0")) {
                                        UmImportantItem = importantList.get(0);
                                    }
                                } else if (importantList.size() == 2) {//有重点和非重点 ImportantId : "1" -- 重点，0 -- 非重点
                                    String importantId = importantList.get(0).getImportantId();
                                    if (importantId.equals("0")) {
                                        UmImportantItem = importantList.get(0);
                                    } else {
                                        UmImportantItem = importantList.get(1);
                                    }
                                }

                                //将非重点项置为正常
                                if (UmImportantItem != null) {
                                    List<CheckItem> UmImpCheckItemList = UmImportantItem.getCheckItemList();
                                    for (int k = 0; k < UmImpCheckItemList.size(); k++) {
                                        UmImpCheckItemList.get(k).setStatus(1);
                                    }
                                }
                            }*/

                        }
                    }
                }
            }
            long endTime = System.currentTimeMillis();

            System.out.println("匹配数据StartTime == " + startTime);
            System.out.println("匹配数据EndTime   == " + endTime);
            System.out.println("匹配数据所花时间 == " + (endTime - startTime));

        }
    }

    /**
     * 重点或非重点的数据映射
     * 根据数据详情的检测项 id 来匹配数据
     *
     * @param checkItemList         检测大对象里重点或非重点的检测项集合
     * @param partsPositionListBean 数据详情里某一检测项
     * @param checkId               数据详情里某一检测项的 id
     */
    private void DataMate(List<CheckItem> checkItemList, PartsPositionListBean partsPositionListBean, String checkId) {
        for (int l = 0; l < checkItemList.size(); l++) {   //遍历检测大对象里检测项的集合取出 检测项 id 进行匹配  -> 李波 on 2016/12/1.
            String impCheckId = checkItemList.get(l).getCheckId();
            if (checkId.equals(impCheckId)) {

                int cStatus = partsPositionListBean.getCStatus();


                checkItemList.get(l).setStatus(cStatus);
                switch (cStatus){
                    case 0://未知 默认状态
                        checkItemList.get(l).setStatus(Constants.STATUS_OK);  //如果状态是未选状态 那就置为正常状态，当前只有正常和缺陷两种状态   -> 李波 on 2017/8/24.
                        break;
                    case 1://正常

                        break;
                    case 2://缺陷
                        //有匹配检测项时，拿出数据详情和大对象的检测项下的缺陷详情列表  -> 李波 on 2016/11/30.
                        List<DefectTypeListBean> defectTypeListTask = partsPositionListBean.getDefectTypeList();

                        List<DefectType> defectTypeList = checkItemList.get(l).getDefectTypeList();

                        //------进行缺陷详情匹配，第一通过defectTypeId进行分类匹配，第二通过缺陷详情 id 进行分类下的最终匹配  -> 李波 on 2016/11/30.
                        for (int m = 0; m < defectTypeListTask.size(); m++) {
                            DefectTypeListBean defectTypeListBean = defectTypeListTask.get(m);

                            String defectTypeTaskId = defectTypeListBean.getDefectTypeID();   //数据详情--缺陷分类id  -> 李波 on 2016/11/30.
                            String defectValueTaskId = defectTypeListBean.getDefectValueID(); //数据详情--缺陷详情id  -> 李波 on 2016/11/30.

                            //第一步，缺陷分类id匹配 遍历大对象下匹配检测项的缺陷列表取出缺陷分类id进行匹配 -> 李波 on 2016/11/30.
                            for (int n = 0; n < defectTypeList.size(); n++) {
                                String defectTypeId = defectTypeList.get(n).getDefectTypeId();

                                if (defectTypeId.equals(defectTypeTaskId)) {
                                    //当有缺陷分类匹配上时取出检测项大对象 里缺陷详情列表进行数据映射  -> 李波 on 2016/11/30.
                                    List<DefectDetailItem> defectDetailList = defectTypeList.get(n).getDefectDetailList();

                                    //第二步，遍历匹配检测大对象里的缺陷详情列表，进行最终缺陷详情 id 匹配  -> 李波 on 2016/11/30.
                                    for (int o = 0; o < defectDetailList.size(); o++) {

                                        //取出检测大对象匹配的缺陷详情列表里每一个缺陷详情的id进行匹配  -> 李波 on 2016/12/1.
                                        DefectDetailItem defectDetailItem = defectDetailList.get(o);
                                        String defectId = defectDetailItem.getDefectId();

                                        if (defectId.equals(defectValueTaskId)) {
                                            //当缺陷详情id匹配上后，修改检测大对象里对应缺陷详情选择状态  -> 李波 on 2016/11/30.
                                            defectDetailItem.setStatus(1); //缺陷项状态有两种：有缺陷置为1-选中状态 0-未选中-> 李波 on 2016/11/30.

                                            //把服务器有缺陷项的 id 加入到最终 提交的 Submit 大对象里，供后续提交   -> 李波 on 2016/12/5.
                                            String picDefectId = defectDetailItem.getPicDefectId();
                                            if (defectValues == null)
                                                defectValues = (ArrayList<String>) DetectMainActivity.detectMainActivity.getSubmitModel().getDefectValue();
                                            if (!defectValues.contains(picDefectId))
                                                defectValues.add(picDefectId);

                                            //取出数据详情缺陷详情的三张照片集合映射到检测大对象里  -> 李波 on 2016/11/30.
                                            List<ImgListBean> imgList = defectTypeListBean.getImgList();
                                            defectDetailItem.setPicDefectHttpUrlList(imgList);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                }


            }
        }

    }
    /***********************************************************************************************************************************/

    /**
     * Created by 李波 on 2016/12/5.
     * 判断车况检测必填数据的完整性，确保提交
     */
    public boolean checkDetectionDataComplete(Context context, boolean isShowToast) {

//        List<CheckPositionItem> checkPositionItems = ((DetectMainActivity) context).getLocalDetectionData().getCheckPositionList();
        List<CheckPositionItem> checkPositionItems = DetectMainActivity.detectMainActivity.getLocalDetectionData().getCheckPositionList();

        //遍历所有检测方位 基本 照片
//        List<PictureItem> pictureItems = ((DetectMainActivity) context).getLocalDetectionData().getPictureList();
        List<PictureItem> pictureItems = DetectMainActivity.detectMainActivity.getLocalDetectionData().getPictureList();
        //郑有权 方位没有照片报空指针 2016/12/29
        if (pictureItems != null && pictureItems.size() > 0) {

            for (int j = 0; j < pictureItems.size(); j++) {
                String picPath = pictureItems.get(j).getPicPath();
                if (TextUtils.isEmpty(picPath)) {
                    if (isShowToast)
                        MyToast.showLong("您有未拍完的基本照片");
                    return false;
                }
                if (!picPath.startsWith("http")) {
                    File file = new File(picPath);
                    if (!file.exists()) {
                        if (isShowToast)
                            MyToast.showLong("您有未拍完的基本照片");
                        return false;
                    }
                }
            }

        }


        //重点检测项是否全部选择的判断  -> 李波 on 2016/12/5.
        for (int i = 0; i < checkPositionItems.size(); i++) {
            //遍历所有检测方位 基本 照片

//            ImportantItem importantItem = checkPositionItems.get(i).getImportantList().get(0);
//            List<CheckItem>  checkItemList =importantItem.getCheckItemList();
            List<ImportantItem> importantList = checkPositionItems.get(i).getImportantList();
            ImportantItem importantItem = null;

            if (importantList != null) {
                if (importantList.size() == 1) {
                    String importantId = importantList.get(0).getImportantId();
                    if (importantId.equals("1")) {
                        importantItem = importantList.get(0);
                    }
                } else if (importantList.size() == 2) {//有重点和非重点 ImportantId : "1" -- 重点，0 -- 非重点
                    String importantId = importantList.get(0).getImportantId();
                    if (importantId.equals("1")) {
                        importantItem = importantList.get(0);
                    } else {
                        importantItem = importantList.get(1);
                    }
                }
            }
            if (importantItem != null) {
                List<CheckItem> checkItemList = importantItem.getCheckItemList();
                for (int j = 0; j < checkItemList.size(); j++) {
                    if (checkItemList.get(j).getStatus() == 0) {
                        if (isShowToast)
                            MyToast.showLong("您有未选择的方位重点检测项");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Created by 李波 on 2016/12/11.
     * 当前为车况检测 点击 其他信息时 弹出
     */
    public void showDialog(Context context) {
        final MyUniversalDialog dialog = new MyUniversalDialog(context);
        View layoutView = dialog.getLayoutView(R.layout.dialog_defect);
        TextView tvCancle = (TextView) layoutView.findViewById(R.id.tvDelete);
        TextView tvContinue = (TextView) layoutView.findViewById(R.id.tvProcess);
        TextView tvNoticeInfo = (TextView) layoutView.findViewById(R.id.tvNoticeInfo);
        tvCancle.setText("取消");
        tvContinue.setText("继续拍摄基本照片");
        tvNoticeInfo.setText("您有未拍摄的基本照片，请先完成。");
        dialog.setLayoutView(layoutView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setDialogGravity(MyUniversalDialog.DialogGravity.CENTER);
        dialog.setLayoutHeightWidth(720, 1280);
        dialog.show();


        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 加载本地图片（drawable图片）
     *
     * @param simpleDraweeView
     * @param id
     */
    public static void loadResPic(SimpleDraweeView simpleDraweeView, int id) {
        Uri uri = Uri.parse("res://" +
                PadSysApp.getAppContext().getPackageName() +
                "/" + id);
        simpleDraweeView.setImageURI(uri);
    }

    public void init() {
        defectValues = null;
        defectTypeList = null;
    }

}
