package com.jcpt.jzg.padsystem.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.ICameraListener;
import com.jcpt.jzg.padsystem.model.DetectionModel;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.ui.activity.SearchCheckActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel.CheckPositionListBean.PartsPositionListBean.DefectTypeListBean.ImgListBean;
import com.jcpt.jzg.padsystem.vo.detection.DefectDetailItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectType;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import top.zibin.luban.Luban;

import static com.jcpt.jzg.padsystem.app.PadSysApp.picDirPath;

/**
 * Created by wujj on 2016/11/21.
 * 每个检测项对应的缺陷列表adapter
 * 内部又包含了某一项缺陷分类对应的小缺陷集合 tagFlowLayout 的适配
 */

public class DefectSelectRecyclerViewAdapter extends RecyclerView.Adapter<DefectSelectRecyclerViewAdapter.ViewHolder> {
    private Activity activity;
    private boolean isHasPhoto = false;
    private List<DefectType> defectTypeList;
    private SimpleDraweeView sdv_defect; //当前选中的小缺陷项的图片  -> 李波 on 2016/11/25.
    private int position;//当前选中的小缺陷项的图片位置0-2，对应一到三张   -> 李波 on 2016/11/25.
    private String defectName;//当前缺陷详情的名称  -> 李波 on 2017/8/25.
    private List<String> picDefectIdList;  //每个小缺陷项里对应的三张图片id  -> 李波 on 2016/11/24.
    private List<ImgListBean> picDefectHttpUrlList; //服务器匹配数据后的 网络图片地址
    private String taskId;
    private ArrayList<String> defectValues; //最后要提交的缺陷详情 id 列表  -> 李波 on 2016/12/11.
    private ArrayList<String> DeletePicId;  //修改的情况下 删除缺陷项 对应图片的 id 集合，用于修改后的提交  -> 李波 on 2016/12/11.
    private ProgressDialog dialog;
    private final String TAG = "DefectSelectRecyclerViewAdapter";
    private String tvName = "";     //检测项名称
    private DetectionModel detectionModel;
    private FrescoImageLoader frescoImageLoader;

    /**
     * Created by 李波 on 2016/12/13.
     * RecyclerView 列表的 position 对应 的当前 Flt 的 position 角标，为了点击三张图片时切换到对应数据
     * 前一个是 列表 position，后一个是 flt position。
     */
    private HashMap<Integer, Integer> rvPosToFltPos = new HashMap<>();

    public DefectSelectRecyclerViewAdapter(Activity activity, List<DefectType> defectTypeList, String taskId, String tvName) {
        this.activity = activity;
        this.defectTypeList = defectTypeList;
        this.taskId = taskId;
        this.tvName = tvName;

        //SubmitModel submitModel = ((DetectMainActivity) context).getSubmitModel();
        SubmitModel submitModel = DetectMainActivity.detectMainActivity.getSubmitModel();
        defectValues = (ArrayList<String>) submitModel.getDefectValue();
        DeletePicId = (ArrayList<String>) submitModel.getDeletePicId();

        detectionModel = new DetectionModel(activity);
        frescoImageLoader = FrescoImageLoader.getSingleton();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                                View contentView = View.inflate(context, R.layout.popupwindow_defect, null);
        View itemView = LayoutInflater.from(activity).inflate(R.layout.popupwindow_defect_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //给缺陷列表里某一类缺陷赋值如：表面缺陷  -> 李波 on 2016/11/24.
        holder.defectTypeName.setText(defectTypeList.get(position).getDefectTypeName());

        //获取缺陷列表中某一类缺陷里对应的小缺陷项集合  -> 李波 on 2016/11/24.
        final List<DefectDetailItem> defectDetailList = defectTypeList.get(position).getDefectDetailList();

        //适配某一类缺陷 对应的小缺陷集合  -> 李波 on 2016/11/24.
        setTag(defectDetailList, holder, position);

        deFectPhotoClick(holder, position, defectDetailList);
    }

    /**
     * Created by 李波 on 2016/12/13.
     * 缺陷详情照片的点击事件
     */
    private void deFectPhotoClick(final ViewHolder holder, final int position, final List<DefectDetailItem> defectDetailList) {
        holder.sdv_defect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = rvPosToFltPos.get(position);
                //点击某一小缺陷项的时候 拿取对应的三张图片的 id 标识集合供后面拍照命名路径使用  -> 李波 on 2016/11/25.
                picDefectIdList = defectDetailList.get(pos).getPicDefectIdList();
                //拿取小缺陷项对应的三张照片的网络地址集合  -> 李波 on 2016/12/11.
                picDefectHttpUrlList = defectDetailList.get(pos).getPicDefectHttpUrlList();

                sdv_defect = holder.sdv_defect1;
                setPosition(0);
                String photoPrefix = picDefectIdList.get(0);
                defectName = tvName + defectDetailList.get(pos).getDefectName();
                cameraOrBrowsePhoto(photoPrefix, defectDetailList.get(pos).getDefectName());
            }
        });

        holder.sdv_defect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = rvPosToFltPos.get(position);

                //点击某一小缺陷项的时候 拿取对应的三张图片的 id 标识集合供后面拍照命名路径使用  -> 李波 on 2016/11/25.
                picDefectIdList = defectDetailList.get(pos).getPicDefectIdList();

                //拿取小缺陷项对应的三张照片的网络地址集合  -> 李波 on 2016/12/11.
                picDefectHttpUrlList = defectDetailList.get(pos).getPicDefectHttpUrlList();

                sdv_defect = holder.sdv_defect2;
                setPosition(1);
                String photoPrefix = picDefectIdList.get(1);
                defectName = tvName + defectDetailList.get(pos).getDefectName();
                cameraOrBrowsePhoto(photoPrefix, defectDetailList.get(pos).getDefectName());

            }
        });

        holder.sdv_defect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = rvPosToFltPos.get(position);

                //点击某一小缺陷项的时候 拿取对应的三张图片的 id 标识集合供后面拍照命名路径使用  -> 李波 on 2016/11/25.
                picDefectIdList = defectDetailList.get(pos).getPicDefectIdList();

                //拿取小缺陷项对应的三张照片的网络地址集合  -> 李波 on 2016/12/11.
                picDefectHttpUrlList = defectDetailList.get(pos).getPicDefectHttpUrlList();

                sdv_defect = holder.sdv_defect3;
                setPosition(2);
                String photoPrefix = picDefectIdList.get(2);
                defectName = tvName + defectDetailList.get(pos).getDefectName();
                cameraOrBrowsePhoto(photoPrefix, defectDetailList.get(pos).getDefectName());
            }
        });
    }

    private void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return defectTypeList == null ? 0 : defectTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView defectTypeName;
        private TextView tvTitle;
        private TagFlowLayout tagFlowLayout;
        private RelativeLayout llPhotos;
        private SimpleDraweeView sdv_defect1;
        private SimpleDraweeView sdv_defect2;
        private SimpleDraweeView sdv_defect3;

        public ViewHolder(View itemView) {
            super(itemView);
            defectTypeName = (TextView) itemView.findViewById(R.id.defectTypeName);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tagFlowLayout = (TagFlowLayout) itemView.findViewById(R.id.tagFlowLayout);
            llPhotos = (RelativeLayout) itemView.findViewById(R.id.llPhotos);
            sdv_defect1 = (SimpleDraweeView) itemView.findViewById(R.id.sdv_defect1);
            sdv_defect2 = (SimpleDraweeView) itemView.findViewById(R.id.sdv_defect2);
            sdv_defect3 = (SimpleDraweeView) itemView.findViewById(R.id.sdv_defect3);
//            initListener(tagFlowLayout,llPhotos,sdv_defect1,sdv_defect2,sdv_defect3);
        }
    }

    //设置tag的点击事件
    private void initListener(final int rvPosition, final List<DefectDetailItem> defectDetailList, final TagFlowLayout tagFlowLayout, final RelativeLayout llPhotos, final SimpleDraweeView sdv_defect1,
                              final SimpleDraweeView sdv_defect2, final SimpleDraweeView sdv_defect3, final TextView tvTitle) {
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(TagView view, int position, FlowLayout parent) {

                rvPosToFltPos.put(rvPosition, position);

                TagView tagView = (TagView) tagFlowLayout.getChildAt(position);
                TextView tv = (TextView) tagView.getTagView();

                //点击某一小缺陷项的时候 拿取对应的三张图片的 id 标识集合供后面拍照命名路径使用  -> 李波 on 2016/11/25.
                picDefectIdList = defectDetailList.get(position).getPicDefectIdList();

                //拿取小缺陷项对应的三张照片的网络地址集合  -> 李波 on 2016/12/11.
                picDefectHttpUrlList = defectDetailList.get(position).getPicDefectHttpUrlList();

                //只要点击了状态就永远置为修改状态，以此来控制检测项不能再为空状态  -> 李波 on 2016/12/9.
                defectDetailList.get(position).setFix(true);

                String defectName = defectDetailList.get(position).getDefectName();

                tvTitle.setText(defectName);

                if (tagView.isChecked()) {

                    //根据小缺陷项的选中状态，实时更改对应的数据到大对象里（1-选中，0-未选中）  -> 李波 on 2016/11/24.
                    defectDetailList.get(position).setStatus(1);

                    String picDefectId = defectDetailList.get(position).getPicDefectId();

                    //如果是选中就实时把选中的缺陷详情id 注入到 submit 缺陷列表里，以备提交数据  -> 李波 on 2016/12/9.
                    if (!defectValues.contains(picDefectId))
                        defectValues.add(picDefectId);

                    llPhotos.setVisibility(View.VISIBLE);
                    showPic(sdv_defect1, 0, picDefectIdList);
                    showPic(sdv_defect2, 1, picDefectIdList);
                    showPic(sdv_defect3, 2, picDefectIdList);
                } else {
                    //选中的小缺陷项再次点击弹出dialog  -> 李波 on 2016/11/24.
                    showDialog(defectDetailList, position, tagView, llPhotos, sdv_defect1, sdv_defect2, sdv_defect3);
                    tagView.setChecked(true);
                }
                return true;
            }
        });


        /**
         * Created by 李波 on 2016/11/24.
         * 小缺陷项拍照完成后的显示
         */
        ((BaseActivity) activity).setOnListenerCamera(new ICameraListener() {
            @Override
            public void setPhoto() {
                if (picDefectIdList!=null){
                final String photoId = picDefectIdList.get(position); //缺陷详情 图片的 id 示例 L05_P08_A003_F01_D001_1  -> 李波 on 2016/12/11.


                final File file = new File(PadSysApp.picDirPath, photoId + ".jpg");

                if (file.exists()) {  //存在才压缩避免NULL  -> 李波 on 2017/8/25.
                    //new
                    Luban.with(activity).load(file)                     //传人要压缩的图片
                            .setCompressListener(new top.zibin.luban.OnCompressListener() { //设置回调
                                @Override
                                public void onStart() {
                                    // 压缩开始前调用，可以在方法内启动 loading UI
                                    showDialog();
                                }

                                @Override
                                public void onSuccess(File file) {
                                    // 压缩成功后调用，返回压缩后的图片文件
                                    compressSuc(file, photoId);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // 当压缩过程出现问题时调用
                                    closeDialog();
                                    e.printStackTrace();
                                }
                            }).launch();    //启动压缩
                }
            }else {
                    LogUtil.e(TAG, "缺陷详情 对应的图片id 集合为null？疑是方案配置问题！");
                }
            }
            private void compressSuc(File file, String photoId) {
                LogUtil.e(TAG, "src pic:" + file.length() / 1024 + "kb");
                if(null==file){
                    closeDialog();
                    MyToast.showShort("失败");
                }
                LogUtil.e(TAG,"src pic:"+file.length()/1024+"kb");
                String bigPic = Constants.ROOT_DIR + File.separator +taskId+File.separator;
                String fileName = photoId +".jpg";
                String fullPath = bigPic + fileName;
                LogUtil.e(TAG,"final Path="+bigPic+fileName);
                if(FileUtils.createOrExistsDir(bigPic)) {
                    if (FileUtils.isFileExists(fullPath)) {
                        FrescoCacheHelper.clearSingleCacheByUrl(fullPath, true);
                        FileUtils.copyFile(file, new File(bigPic, fileName));
                        closeDialog();
                    }
                }
                //显示之前先删除本张照片Fresco缓存，避免同名显示缓存图片  -> 李波 on 2016/11/30.
                FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                detectionModel.showCameraPhoto(sdv_defect, position, picDefectIdList);

                try {
                    //如果是修改的情况下 拍照显示后，把对应的删除图片id集合里的 id 移除掉  -> 李波 on 2016/12/11.
                    //计算图片对应的是第几张  -> 李波 on 2016/12/11.
                    String photoIndex = photoId.substring(photoId.length() - 1);
                    int photoIn = Integer.valueOf(photoIndex) - 1;

                    String picId = "";

                    if (picDefectHttpUrlList != null && picDefectHttpUrlList.size() > 0) {
                        for (int i = 0; i < picDefectHttpUrlList.size(); i++) {
                            String picDefectPosition = picDefectHttpUrlList.get(i).getUriID();
                            if (StringUtils.isEmpty(picDefectPosition)) {
                                continue;
                            }
                            picDefectPosition = picDefectPosition.substring(picDefectPosition.length() - 1);
                            int picDefPosition = Integer.valueOf(picDefectPosition) - 1;

                            if (photoIn == picDefPosition) {
                                picId = picDefectHttpUrlList.get(photoIn).getPicID();
                            }
                        }
                    }

                    if (DeletePicId.contains(picId))
                        DeletePicId.remove(picId);
                } catch (Exception e) {  //不论是null还是角标越界，都不需要对DeletePciId做任何处理  -> 李波 on 2016/12/11.

                }
            }

            private void showDialog() {
                if (dialog == null)
                    dialog = ProgressDialog.show(activity, "照片处理中", "");
                else
                    dialog.show();

            }

            private void closeDialog() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }


            @Override
            public void recapturePhoto(Intent data) {
                if (null != data) {

                    if (picDefectIdList!=null) {
                        boolean recapture = data.getBooleanExtra("recapture", false);
                        boolean isDel = data.getBooleanExtra("isDel", false);
                        if (recapture) {
                            String photoId = picDefectIdList.get(position);
                            String picPath = PadSysApp.picDirPath + photoId + ".jpg";
                            if (Build.VERSION.SDK_INT >= 23) {
                                detectionModel.applyPermission(photoId, activity, detectionModel.DEFECT_ITEM, taskId, defectName);

                            } else {
                                detectionModel.camera(photoId, activity, detectionModel.DEFECT_ITEM, taskId, defectName);
                            }
                        } else if (isDel) {

                            SubmitModel submitModel = ((DetectMainActivity) activity).getSubmitModel();
                            String deleteId = picDefectIdList.get(position);

                            //添加删除图片的id参数
                            if (submitModel != null && submitModel.getDeletePicId() != null) {
                                if (!submitModel.getDeletePicId().contains(deleteId)) {
                                    submitModel.getDeletePicId().add(deleteId);
                                }
                            }

                            detectionModel.showCameraPhoto(sdv_defect, position, picDefectIdList);
                            //删除对应的图片，如果是网络图片清空网络地址，如果是本地图片，删除本地图片
                            if (picDefectHttpUrlList != null && picDefectHttpUrlList.size() > 0) {

                                for (int i = 0; i < picDefectHttpUrlList.size(); i++) {
                                    if (picDefectHttpUrlList.get(i).getUriID().equals(deleteId))
                                        picDefectHttpUrlList.get(i).setPicPath("");
                                }

                            }
                        } else {
                            //显示对应缺陷项下的照片
                            showPic(sdv_defect, position, picDefectIdList);
                        }

                    }else {
                        LogUtil.e(TAG, "缺陷详情 对应的图片id 集合为null？疑是方案配置问题！");
                    }
                }
            }
        });
    }

    /**
     * 显示小缺陷项的三张图片
     *
     * @param sdv_defect      当前图片
     * @param position        图片id集合里面的位置
     * @param picDefectIdList 图片的id集合
     */
    private void showPic(SimpleDraweeView sdv_defect, int position, List<String> picDefectIdList) {

        frescoImageLoader.displayImage(sdv_defect, "");

        String picId = picDefectIdList.get(position);
        String photoSDPath = PadSysApp.picDirPath + picId + ".jpg";
        File filePhoto = new File(photoSDPath);

        if (filePhoto.exists()) {
            detectionModel.showCameraPhoto(sdv_defect, position, picDefectIdList);
        } else if (picDefectHttpUrlList != null && picDefectHttpUrlList.size() > 0) {
            for (int i = 0; i < picDefectHttpUrlList.size(); i++) {
                String picDefectPosition = picDefectHttpUrlList.get(i).getUriID();
                if (StringUtils.isEmpty(picDefectPosition)) {
                    continue;
                }
                picDefectPosition = picDefectPosition.substring(picDefectPosition.length() - 1);
                int picDefPosition = Integer.valueOf(picDefectPosition) - 1;

                if (position == picDefPosition) {
                    frescoImageLoader.displayImage(sdv_defect, picDefectHttpUrlList.get(i).getPicPath());
                }

            }
        }
    }

    /**
     * 启动拍照或者浏览图片
     *
     * @param photoPrefix
     */
    private void cameraOrBrowsePhoto(String photoPrefix, String photoName) {
        String picPath = PadSysApp.picDirPath + photoPrefix + ".jpg";
        String picPathHTTP = null;
        File Picfile = new File(picPath);


        if (picDefectHttpUrlList != null && picDefectHttpUrlList.size() > 0) {

            for (int i = 0; i < picDefectHttpUrlList.size(); i++) {
                String picDefectPosition = picDefectHttpUrlList.get(i).getUriID();
                if (StringUtils.isEmpty(picDefectPosition)) {
                    continue;
                }
                picDefectPosition = picDefectPosition.substring(picDefectPosition.length() - 1);
                int picDefPosition = Integer.valueOf(picDefectPosition) - 1;

                if (position == picDefPosition) {
                    picPathHTTP = picDefectHttpUrlList.get(i).getPicPath();
                }

            }

        }

        if (StringUtils.isEmpty(picPathHTTP)) {
            if (!Picfile.exists()) {
                if (Build.VERSION.SDK_INT >= 23) {
                    detectionModel.applyPermission(photoPrefix, activity, detectionModel.DEFECT_ITEM, taskId, defectName);
                } else {
                    detectionModel.camera(photoPrefix, activity, detectionModel.DEFECT_ITEM,taskId, defectName);
                }
            } else {
                skipToPictureZoomActivity(picPath, photoName, photoPrefix);
            }
        } else {

            if (!Picfile.exists()) {
                skipToPictureZoomActivity(picPathHTTP, photoName, photoPrefix);
            } else {
                skipToPictureZoomActivity(picPath, photoName, photoPrefix);
            }

        }

    }

    //跳转到大图浏览界面  -> 李波 on 2016/12/13.
    private void skipToPictureZoomActivity(String picPath, String photoName, String photoId) {
        //连续拍照图片列表
        ArrayList<PictureItem> pictureItems = new ArrayList<>();
        PictureItem p = new PictureItem();
        p.setPicPath(picPath);
        p.setPicId(photoId);
        p.setPicName(tvName + photoName);
        pictureItems.add(p);
        Intent intent = new Intent(activity, PictureZoomActivity.class);

        intent.putExtra("url", picPath);
        intent.putExtra("showRecapture", true);
        intent.putExtra("isDel", true);
        intent.putExtra("pictureItems", pictureItems);
        intent.putExtra("isClickDefectPhoto", true);
        intent.putExtra("taskid", taskId);
        try {
            ((DetectMainActivity) activity ).startActivityForResult(intent, detectionModel.DEFECT_ITEM_RECAPTURE);
        }catch (Exception e){
            ((SearchCheckActivity) activity).startActivityForResult(intent, detectionModel.DEFECT_ITEM_RECAPTURE);
        }

    }


    /**
     * 当小缺陷项选中时再次点击将弹出 dialog
     *
     * @param defectDetailList
     * @param position
     * @param tagView
     * @param llPhotos
     * @param sdv_defect1
     * @param sdv_defect2
     * @param sdv_defect3
     */
    private void showDialog(final List<DefectDetailItem> defectDetailList, final int position, final TagView tagView,
                            final RelativeLayout llPhotos, final SimpleDraweeView sdv_defect1,
                            final SimpleDraweeView sdv_defect2, final SimpleDraweeView sdv_defect3) {
        final MyUniversalDialog dialog = new MyUniversalDialog(activity);
        View layoutView = dialog.getLayoutView(R.layout.dialog_defect);
        TextView tvDelete = (TextView) layoutView.findViewById(R.id.tvDelete);
        TextView tvProcess = (TextView) layoutView.findViewById(R.id.tvProcess);
        TextView tvNoticeInfo = (TextView) layoutView.findViewById(R.id.tvNoticeInfo);
        tvNoticeInfo.setText("请选择您需要的操作");
        dialog.setLayoutView(layoutView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setDialogGravity(MyUniversalDialog.DialogGravity.CENTER);
        dialog.show();
        detectionModel.backgroundAlpha((Activity) activity, 1f);
        //删除此小缺陷项  -> 李波 on 2016/11/24.
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏dialog
                detectionModel.backgroundAlpha((Activity) activity, 0.5f);
                dialog.dismiss();
                //取消选中状态
                tagView.setChecked(false);

                //删除此小缺陷项，实时更改对应的状态数据到大对象里  -> 李波 on 2016/11/24.
                defectDetailList.get(position).setStatus(0);

                //删除缺陷项时对应删除三张图片  -> 李波 on 2016/12/11.
                for (int i = 0; i < picDefectIdList.size(); i++) {
                    String photoPath = PadSysApp.picDirPath + picDefectIdList.get(i) + ".jpg";
                    FileUtils.deleteFile(photoPath);
                }

                //删除缺陷项时，如果是修改状态还要把删除的图片 PicID 注入到DeletePicId集合里  -> 李波 on 2016/12/11.
                if (picDefectHttpUrlList != null) {
                    for (ImgListBean imgBean : picDefectHttpUrlList) {
                        imgBean.setPicPath("");
                        imgBean.setUriID(null);
                        String picId = imgBean.getPicID();
                        if (!DeletePicId.contains(picId))
                            DeletePicId.add(picId);


                    }
                }
                String picDefectId = defectDetailList.get(position).getPicDefectId();

                //删除此缺陷项时，实时从submit缺陷列表里移除掉对应 id  -> 李波 on 2016/12/9.
                if (defectValues.contains(picDefectId))
                    defectValues.remove(picDefectId);

                TextView tv = (TextView) tagView.getTagView();
                //隐藏照片预览图
                llPhotos.setVisibility(View.GONE);

            }
        });

        //处理此小缺陷项  -> 李波 on 2016/11/24.
        tvProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏dialog
                detectionModel.backgroundAlpha((Activity) activity, 0.5f);
                dialog.dismiss();
                tagView.setChecked(true);

                //处理此小缺陷项，仍然为选中状态，实时更改对应的状态数据到大对象里  -> 李波 on 2016/11/24.
                defectDetailList.get(position).setStatus(1);
                String picDefectId = defectDetailList.get(position).getPicDefectId();

                if (!defectValues.contains(picDefectId))
                    defectValues.add(picDefectId);

                llPhotos.setVisibility(View.VISIBLE);
                //显示对应缺陷项下的照片
                showPic(sdv_defect1, 0, picDefectIdList);
                showPic(sdv_defect2, 1, picDefectIdList);
                showPic(sdv_defect3, 2, picDefectIdList);

            }
        });
    }


    /**
     * TagFlowLayout 适配每一个缺陷分类对应的小缺陷项集合
     *
     * @param defectDetailList 每个缺陷分类对应的小缺陷项集合，此集合会在小缺陷项选中或未选中时实时更改里面的status数据。记录和回显
     * @param holder
     */
    public void setTag(List<DefectDetailItem> defectDetailList, ViewHolder holder, int rvPosition) {
        DefectTagAdapter adapter = new DefectTagAdapter(defectDetailList, holder.tagFlowLayout, activity);
        holder.tagFlowLayout.setAdapter(adapter);

        //遍历defectDetailList根据item的status值设置item的选中状态（初始化或者回显）  -> 李波 on 2016/11/24.
        HashSet<Integer> vinEcho = new HashSet<Integer>();
        for (int i = 0; i < defectDetailList.size(); i++) {
            if (defectDetailList.get(i).getStatus() == 1) {
                vinEcho.add(i);
                String picDefectId = defectDetailList.get(i).getPicDefectId();
                if (!defectValues.contains(picDefectId))
                    defectValues.add(picDefectId);
            }
        }
        adapter.setSelectedList(vinEcho); //设置TagFlowLayout的哪些item是选中的  -> 李波 on 2016/11/24.


        initListener(rvPosition, defectDetailList, holder.tagFlowLayout, holder.llPhotos, holder.sdv_defect1, holder.sdv_defect2, holder.sdv_defect3, holder.tvTitle);

        for (int i = 0; i < holder.tagFlowLayout.getChildCount(); i++) {
            TagView tag = (TagView) holder.tagFlowLayout.getChildAt(i);
            TextView tv = (TextView) tag.getTagView();
            Drawable drawable = activity.getResources().getDrawable(R.drawable.tag_bg_orange);
            tv.setBackgroundDrawable(drawable);
        }
    }
}
