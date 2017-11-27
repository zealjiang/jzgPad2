package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.BMWOtherCheckAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWCarSketchmapActivity;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.interfaces.OnCompressListener;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.ImageCompressor;
import com.jcpt.jzg.padsystem.utils.InputUtil;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.StringHelper;
import com.jcpt.jzg.padsystem.vo.BMWOtherBean;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.BmwOtherCheckItemView;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by wujj on 2017/10/25.
 * 邮箱：wujj@jingzhengu.com
 * 作用：其他检查
 */

public class BMWOtherCheckFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.tvNext)
    CustomRippleButton tvNext;
    @BindView(R.id.etLeftFront_in)
    EditText etLeftFrontIn;
    @BindView(R.id.etRightFront_in)
    EditText etRightFrontIn;
    @BindView(R.id.etLeftBack_in)
    EditText etLeftBackIn;
    @BindView(R.id.etRightBack_in)
    EditText etRightBackIn;
    @BindView(R.id.etLeftFront_out)
    EditText etLeftFrontOut;
    @BindView(R.id.etRightFront_out)
    EditText etRightFrontOut;
    @BindView(R.id.etLeftBack_out)
    EditText etLeftBackOut;
    @BindView(R.id.etRightBack_out)
    EditText etRightBackOut;
    @BindView(R.id.etLeftFront_stop)
    EditText etLeftFrontStop;
    @BindView(R.id.etRightFront_stop)
    EditText etRightFrontStop;
    @BindView(R.id.etLeftBack_stop)
    EditText etLeftBackStop;
    @BindView(R.id.etRightBack_stop)
    EditText etRightBackStop;
    @BindView(R.id.rgWaterCar)
    RadioGroup rgWaterCar;
    @BindView(R.id.rgWindowLamp)
    RadioGroup rgWindowLamp;
    @BindView(R.id.rgBodyPaint)
    RadioGroup rgBodyPaint;
    @BindView(R.id.rgBodyAppearancePaste)
    RadioGroup rgBodyAppearancePaste;
    @BindView(R.id.rgBodyAppearancePaint)
    RadioGroup rgBodyAppearancePaint;
    @BindView(R.id.rbtnYes)
    RadioButton rbtnYes;
    @BindView(R.id.rbtnNo)
    RadioButton rbtnNo;
    @BindView(R.id.rgBodyAppearanceChrome)
    RadioGroup rgBodyAppearanceChrome;
    @BindView(R.id.refitLicence)
    BmwOtherCheckItemView refitLicence;
    @BindView(R.id.resetKeyMemory)
    BmwOtherCheckItemView resetKeyMemory;
    @BindView(R.id.envCerPerIns)
    BmwOtherCheckItemView envCerPerIns;
    @BindView(R.id.navigationMap)
    BmwOtherCheckItemView navigationMap;
    @BindView(R.id.violationRecord)
    BmwOtherCheckItemView violationRecord;
    @BindView(R.id.changeProcedure)
    BmwOtherCheckItemView changeProcedure;
    @BindView(R.id.rbtnWaterYes)
    RadioButton rbtnWaterYes;
    @BindView(R.id.rbtnWaterNo)
    RadioButton rbtnWaterNo;
    @BindView(R.id.rbtnWindowLampYes)
    RadioButton rbtnWindowLampYes;
    @BindView(R.id.rbtnWindowLampNo)
    RadioButton rbtnWindowLampNo;
    @BindView(R.id.rbtnBodyPaintYes)
    RadioButton rbtnBodyPaintYes;
    @BindView(R.id.rbtnBodyPaintNo)
    RadioButton rbtnBodyPaintNo;
    @BindView(R.id.rbtnBodyAppearancePasteYes)
    RadioButton rbtnBodyAppearancePasteYes;
    @BindView(R.id.rbtnBodyAppearancePasteNo)
    RadioButton rbtnBodyAppearancePasteNo;
    @BindView(R.id.rbtnBodyAppearancePaintYes)
    RadioButton rbtnBodyAppearancePaintYes;
    @BindView(R.id.rbtnBodyAppearancePaintNo)
    RadioButton rbtnBodyAppearancePaintNo;
    @BindView(R.id.rbtnBodyAppearanceChromeYes)
    RadioButton rbtnBodyAppearanceChromeYes;
    @BindView(R.id.rbtnBodyAppearanceChromeNo)
    RadioButton rbtnBodyAppearanceChromeNo;
    @BindView(R.id.sdv_add_water)
    SimpleDraweeView sdvAddWater;
    @BindView(R.id.recyclerView_water)
    RecyclerView recyclerViewWater;
    @BindView(R.id.sdv_add_windowLamp)
    SimpleDraweeView sdvAddWindowLamp;
    @BindView(R.id.recyclerView_windowLamp)
    RecyclerView recyclerViewWindowLamp;
    @BindView(R.id.sdv_add_bodyPaint)
    SimpleDraweeView sdvAddBodyPaint;
    @BindView(R.id.recyclerView_bodyPaint)
    RecyclerView recyclerViewBodyPaint;
    @BindView(R.id.sdv_add_bodyAppearancePaste)
    SimpleDraweeView sdvAddBodyAppearancePaste;
    @BindView(R.id.recyclerView_bodyAppearancePaste)
    RecyclerView recyclerViewBodyAppearancePaste;
    @BindView(R.id.sdv_add_bodyAppearancePaint)
    SimpleDraweeView sdvAddBodyAppearancePaint;
    @BindView(R.id.recyclerView_bodyAppearancePaint)
    RecyclerView recyclerViewBodyAppearancePaint;
    @BindView(R.id.sdv_add_bodyAppearanceChrome)
    SimpleDraweeView sdvAddBodyAppearanceChrome;
    @BindView(R.id.recyclerView_bodyAppearanceChrome)
    RecyclerView recyclerViewBodyAppearanceChrome;
    @BindView(R.id.ll_water)
    LinearLayout llWater;
    @BindView(R.id.ll_windowLamp)
    LinearLayout llWindowLamp;
    @BindView(R.id.ll_bodyPaint)
    LinearLayout llBodyPaint;
    @BindView(R.id.ll_bodyAppearancePaste)
    LinearLayout llBodyAppearancePaste;
    @BindView(R.id.ll_bodyAppearancePaint)
    LinearLayout llBodyAppearancePaint;
    @BindView(R.id.ll_bodyAppearanceChrome)
    LinearLayout llBodyAppearanceChrome;
    @BindView(R.id.underpin)
    BmwOtherCheckItemView underpin;
    @BindView(R.id.etRemark)
    EditText etRemark;
    private INextStepListener iNextStepListener;
    private BMWOtherBean bmwOtherBean;
    private String curphotoPrefix = "";  //当前拍照编号
    private final int PHOTO_REQUEST = 10;
    private final int PHOTO_BIG_PHOTO_WATER = 11;
    private final int PHOTO_BIG_PHOTO_WINDOWLAMP = 13;
    private final int PHOTO_BIG_PHOTO_BODYPAINT = 14;
    private final int PHOTO_BIG_PHOTO_BODYAPPEARANCEPASTE = 15;
    private final int PHOTO_BIG_PHOTO_BODYAPPEARANCEPAINT = 16;
    private final int PHOTO_BIG_PHOTO_BODYAPPEARANCECHROME = 17;
    private ProgressDialog dialog;
    private BMWOtherCheckAdapter adapterWater;
    private BMWOtherCheckAdapter adapterWindowLamp;
    private BMWOtherCheckAdapter adapterBodyPaint;
    private BMWOtherCheckAdapter adapterBodyAppearancePaste;
    private BMWOtherCheckAdapter adapterBodyAppearancePaint;
    private BMWOtherCheckAdapter adapterBodyAppearanceChrome;

    private ArrayList<PictureItem> pictureItemsWater;//综合判断为泡水车
    private ArrayList<PictureItem> pictureItemsWindowLamp;//车窗玻璃及大灯状态良好（不影响安全行驶）
    private ArrayList<PictureItem> pictureItemsBodyPaint;//车身及漆面处于与车龄相适应的良好状态
    private ArrayList<PictureItem> pictureItemsBodyAppearancePaste;//车身外观无粘贴物
    private ArrayList<PictureItem> pictureItemsBodyAppearancePaint;//车身漆面缺陷光洁、密闭
    private ArrayList<PictureItem> pictureItemsBodyAppearanceChrome;//车身外观镀铬部件状态良好

    private int curPicPosWater = 0;
    private int curPicPosWindowLamp = 0;
    private int curPicPosBodyPaint = 0;
    private int curPicPosBodyAppearancePaste = 0;
    private int curPicPosBodyAppearancePaint = 0;
    private int curPicPosBodyAppearanceChrome = 0;

    private Map<String, String> mapsPhotoWater = new HashMap<>();
    private Map<String, String> mapsPhotoWindowLamp = new HashMap<>();
    private Map<String, String> mapsPhotoBodyPaint = new HashMap<>();
    private Map<String, String> mapsPhotoBodyAppearancePaste = new HashMap<>();
    private Map<String, String> mapsPhotoBodyAppearancePaint = new HashMap<>();
    private Map<String, String> mapsPhotoBodyAppearanceChrome = new HashMap<>();

    private int curEndPicWaterPos = 0;  //当前最后一张图片的id编号
    private int curEndPicWindowLampPos = 0;
    private int curEndPicBodyPaintPos = 0;
    private int curEndPicBodyAppearancePastePos = 0;
    private int curEndPicBodyAppearancePaintPos = 0;
    private int curEndPicBodyAppearanceChromePos = 0;

    private String prefixCharacters;//图片前缀
    private SubmitModel submitModel;
    private List<SubmitModel.SpCheckItemGroupListBean> spCheckItemGroupList;

    public BMWOtherCheckFragment() {
        pictureItemsWater = new ArrayList<>();
        pictureItemsWindowLamp = new ArrayList<>();
        pictureItemsBodyPaint = new ArrayList<>();
        pictureItemsBodyAppearancePaste = new ArrayList<>();
        pictureItemsBodyAppearancePaint = new ArrayList<>();
        pictureItemsBodyAppearanceChrome = new ArrayList<>();
    }

    @Override
    protected void initData() {

        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        //查询此条缓存是否存在
        boolean isExist = DBManager.getInstance().isExist(Constants.DATA_TYPE_BMW_OTHER, taskId, PadSysApp.getUser().getUserId());
        DBManager.getInstance().closeDB();
        if (isExist) {
            List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_BMW_OTHER, PadSysApp.getUser().getUserId());
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (list.get(i).getDataType().equals(Constants.DATA_TYPE_BMW_OTHER)) {
                        String json = list.get(i).getJson();
                        if (!TextUtils.isEmpty(json)) {
                            bmwOtherBean = new Gson().fromJson(json, BMWOtherBean.class);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_bmw, container, false);
        unbinder = ButterKnife.bind(this, view);
        initListener();
        return view;
    }

    //是否可拍摄照片的监听
    private void initListener() {
        rgWaterCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == rbtnWaterYes.getId()) {
                    if (pictureItemsWater != null && pictureItemsWater.size() > 0) {
                        showDeleteDialog(llWater, mapsPhotoWater, rbtnWaterNo, "火烧泡水");
                    } else {
                        llWater.setVisibility(View.GONE);
                    }
                } else if (checkedId == rbtnWaterNo.getId()) {
                    llWater.setVisibility(View.VISIBLE);
                }
            }
        });
        rgWindowLamp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == rbtnWindowLampYes.getId()) {
                    if (pictureItemsWindowLamp != null && pictureItemsWindowLamp.size() > 0) {
                        showDeleteDialog(llWindowLamp, mapsPhotoWindowLamp, rbtnWindowLampNo, "玻璃及大灯");
                    } else {
                        llWindowLamp.setVisibility(View.GONE);
                    }
                } else if (checkedId == rbtnWindowLampNo.getId()) {
                    llWindowLamp.setVisibility(View.VISIBLE);
                }
            }
        });
        rgBodyPaint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == rbtnBodyPaintYes.getId()) {
                    if (pictureItemsBodyPaint != null && pictureItemsBodyPaint.size() > 0) {
                        showDeleteDialog(llBodyPaint, mapsPhotoBodyPaint, rbtnBodyPaintNo, "车身外观缺陷");
                    } else {
                        llBodyPaint.setVisibility(View.GONE);
                    }
                } else if (checkedId == rbtnBodyPaintNo.getId()) {
                    llBodyPaint.setVisibility(View.VISIBLE);
                }
            }
        });
        rgBodyAppearancePaste.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == rbtnBodyAppearancePasteYes.getId()) {
                    if (pictureItemsBodyAppearancePaste != null && pictureItemsBodyAppearancePaste.size() > 0) {
                        showDeleteDialog(llBodyAppearancePaste, mapsPhotoBodyAppearancePaste, rbtnBodyAppearancePasteNo, "粘贴物");
                    } else {
                        llBodyAppearancePaste.setVisibility(View.GONE);
                    }
                } else if (checkedId == rbtnBodyAppearancePasteNo.getId()) {
                    llBodyAppearancePaste.setVisibility(View.VISIBLE);
                }
            }
        });
        rgBodyAppearancePaint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == rbtnBodyAppearancePaintYes.getId()) {
                    if (pictureItemsBodyAppearancePaint != null && pictureItemsBodyAppearancePaint.size() > 0) {
                        showDeleteDialog(llBodyAppearancePaint, mapsPhotoBodyAppearancePaint, rbtnBodyAppearancePaintNo, "漆面缺陷");
                    } else {
                        llBodyAppearancePaint.setVisibility(View.GONE);
                    }
                } else if (checkedId == rbtnBodyAppearancePaintNo.getId()) {
                    llBodyAppearancePaint.setVisibility(View.VISIBLE);
                }
            }
        });
        rgBodyAppearanceChrome.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == rbtnBodyAppearanceChromeYes.getId()) {
                    if (pictureItemsBodyAppearanceChrome != null && pictureItemsBodyAppearanceChrome.size() > 0) {
                        showDeleteDialog(llBodyAppearanceChrome, mapsPhotoBodyAppearanceChrome, rbtnBodyAppearanceChromeNo, "镀铬部件缺陷");
                    } else {
                        llBodyAppearanceChrome.setVisibility(View.GONE);
                    }
                } else if (checkedId == rbtnBodyAppearanceChromeNo.getId()) {
                    llBodyAppearanceChrome.setVisibility(View.VISIBLE);
                }
            }
        });

        //限制输入表情符
        InputUtil.inputRestrict(etRemark, true, false, false, false);
    }

    private void showDeleteDialog(final LinearLayout llItem, final Map<String, String> mapsPhoto, final RadioButton rbtnNo, String photoName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("是否删除全部" + photoName + "照片？");
        builder.setCancelable(false);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                llItem.setVisibility(View.GONE);
                dialog.dismiss();
                if (llItem == llWater) {
                    prefixCharacters = "QTJC_WaterCar_";
                } else if (llItem == llWindowLamp) {
                    prefixCharacters = "QTJC_WindowLamp_";
                } else if (llItem == llBodyPaint) {
                    prefixCharacters = "QTJC_BodyPaint_";
                } else if (llItem == llBodyAppearancePaste) {
                    prefixCharacters = "QTJC_BodyAppearancePaste_";
                } else if (llItem == llBodyAppearancePaint) {
                    prefixCharacters = "QTJC_BodyAppearancePaint_";
                } else if (llItem == llBodyAppearanceChrome) {
                    prefixCharacters = "QTJC_BodyAppearanceChrome_";
                }
                //删除本地图片
                File dir = new File(PadSysApp.picDirPath);
                File[] files = dir.listFiles();
                ArrayList<String> deleteArray = new ArrayList<>();
                if (files != null) {
                    if (files.length > 0) {
                        for (File file : files) {
                            if (file.getName().contains(prefixCharacters)) {
                                deleteArray.add(PadSysApp.picDirPath + file.getName());
                            }
                        }
                    }
                }
                FrescoCacheHelper.clearAllCaches(deleteArray, true);
                mapsPhoto.clear();
                //刷新照片
                loadPhoto();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                rbtnNo.setChecked(true);
            }
        });

        builder.create().show();
    }

    @Override
    protected void setView() {
        //泡水车照片
        adapterWater = new BMWOtherCheckAdapter(getActivity(), pictureItemsWater);
        recyclerViewWater.setAdapter(adapterWater);
        recyclerViewWater.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterWater.setOnItemClickListener(new BMWOtherCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                curPicPosWater = pos;
                clickPic(pictureItemsWater, pos);
            }
        });

        //车窗玻璃及大灯状态良好（不影响安全行驶）
        adapterWindowLamp = new BMWOtherCheckAdapter(getActivity(), pictureItemsWindowLamp);
        recyclerViewWindowLamp.setAdapter(adapterWindowLamp);
        recyclerViewWindowLamp.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterWindowLamp.setOnItemClickListener(new BMWOtherCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                curPicPosWindowLamp = pos;
                clickPic(pictureItemsWindowLamp, pos);
            }
        });

        //车身及漆面处于与车龄相适应的良好状态
        adapterBodyPaint = new BMWOtherCheckAdapter(getActivity(), pictureItemsBodyPaint);
        recyclerViewBodyPaint.setAdapter(adapterBodyPaint);
        recyclerViewBodyPaint.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterBodyPaint.setOnItemClickListener(new BMWOtherCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                curPicPosBodyPaint = pos;
                clickPic(pictureItemsBodyPaint, pos);
            }
        });

        //车身外观无粘贴物
        adapterBodyAppearancePaste = new BMWOtherCheckAdapter(getActivity(), pictureItemsBodyAppearancePaste);
        recyclerViewBodyAppearancePaste.setAdapter(adapterBodyAppearancePaste);
        recyclerViewBodyAppearancePaste.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterBodyAppearancePaste.setOnItemClickListener(new BMWOtherCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                curPicPosBodyAppearancePaste = pos;
                clickPic(pictureItemsBodyAppearancePaste, pos);
            }
        });

        //车身漆面缺陷光洁、密闭
        adapterBodyAppearancePaint = new BMWOtherCheckAdapter(getActivity(), pictureItemsBodyAppearancePaint);
        recyclerViewBodyAppearancePaint.setAdapter(adapterBodyAppearancePaint);
        recyclerViewBodyAppearancePaint.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterBodyAppearancePaint.setOnItemClickListener(new BMWOtherCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                curPicPosBodyAppearancePaint = pos;
                clickPic(pictureItemsBodyAppearancePaint, pos);
            }
        });

        //车身外观镀铬部件状态良好
        adapterBodyAppearanceChrome = new BMWOtherCheckAdapter(getActivity(), pictureItemsBodyAppearanceChrome);
        recyclerViewBodyAppearanceChrome.setAdapter(adapterBodyAppearanceChrome);
        recyclerViewBodyAppearanceChrome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterBodyAppearanceChrome.setOnItemClickListener(new BMWOtherCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                curPicPosBodyAppearanceChrome = pos;
                clickPic(pictureItemsBodyAppearanceChrome, pos);
            }
        });

        showdata();
    }

    private void clickPic(ArrayList<PictureItem> pictureItems, int pos) {
        String picPath = pictureItems.get(pos).getPicPath();
        if (!TextUtils.isEmpty(picPath)) {
            //查看大图
            Intent intent = new Intent(context, PictureZoomActivity.class);
            intent.putExtra("url", picPath);
            intent.putExtra("showRecapture", true);
            intent.putExtra("isDel", true);
            intent.putExtra("pictureItems", pictureItems);
            intent.putExtra("curPosition", pos);
            intent.putExtra("taskid", ((BMWDetectMainActivity) getActivity()).getTaskid());
            if (pictureItems == pictureItemsWater) {
                startActivityForResult(intent, PHOTO_BIG_PHOTO_WATER);
            } else if (pictureItems == pictureItemsWindowLamp) {
                startActivityForResult(intent, PHOTO_BIG_PHOTO_WINDOWLAMP);
            } else if (pictureItems == pictureItemsBodyPaint) {
                startActivityForResult(intent, PHOTO_BIG_PHOTO_BODYPAINT);
            } else if (pictureItems == pictureItemsBodyAppearancePaste) {
                startActivityForResult(intent, PHOTO_BIG_PHOTO_BODYAPPEARANCEPASTE);
            } else if (pictureItems == pictureItemsBodyAppearancePaint) {
                startActivityForResult(intent, PHOTO_BIG_PHOTO_BODYAPPEARANCEPAINT);
            } else if (pictureItems == pictureItemsBodyAppearanceChrome) {
                startActivityForResult(intent, PHOTO_BIG_PHOTO_BODYAPPEARANCECHROME);
            }
        }
    }

    //拍照
    private void useCamera(String photoPrefix, String strPrefix) {
        curphotoPrefix = photoPrefix;
        //调用自定义相机
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", ((BMWDetectMainActivity) getActivity()).getTaskid());
        intent.putExtra(Constants.CAPTURE_TYPE, Constants.CAPTURE_TYPE_SINGLE);//拍摄模式，是单拍还是连拍

        ArrayList<PictureItem> singleList = new ArrayList<>();
        PictureItem pictureItem = new PictureItem();
        pictureItem.setPicId(photoPrefix);
        pictureItem.setPicName(strPrefix + StringHelper.returnNumbers(photoPrefix));
        singleList.add(pictureItem);
        intent.putExtra("picList", singleList);
        intent.putExtra("position", 0);

        ((BMWDetectMainActivity) context).startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_BIG_PHOTO_WATER && resultCode == Activity.RESULT_OK) { //综合判断为泡水车
            if (data != null) {
                //重拍
                boolean recapture = data.getBooleanExtra("recapture", false);
                curPicPosWater = data.getIntExtra("recapturePosition", curPicPosWater);
                if (recapture) {
                    FrescoCacheHelper.clearSingleCacheByUrl(pictureItemsWater.get(curPicPosWater).getPicPath(), false);
                    pictureItemsWater.get(curPicPosWater).setPicPath("");
                    useCamera(pictureItemsWater.get(curPicPosWater).getPicId(), "火烧泡水");
                }
                //删除
                boolean isDel = data.getBooleanExtra("isDel", false);
                if (isDel) {
                    //删除本地图片
                    String path = PadSysApp.picDirPath + pictureItemsWater.get(curPicPosWater).getPicId() + ".jpg";
                    if (FileUtils.isFileExists(path)) {
                        FileUtils.deleteFile(path);
                    }
                    mapsPhotoWater.remove(pictureItemsWater.get(curPicPosWater).getPicId());
                }
            }
        } else if (requestCode == PHOTO_BIG_PHOTO_WINDOWLAMP && resultCode == Activity.RESULT_OK) {//车窗玻璃及大灯状态良好（不影响安全行驶）
            //重拍
            boolean recapture = data.getBooleanExtra("recapture", false);
            curPicPosWindowLamp = data.getIntExtra("recapturePosition", curPicPosWindowLamp);
            if (recapture) {
                FrescoCacheHelper.clearSingleCacheByUrl(pictureItemsWindowLamp.get(curPicPosWindowLamp).getPicPath(), false);
                pictureItemsWindowLamp.get(curPicPosWindowLamp).setPicPath("");
                useCamera(pictureItemsWindowLamp.get(curPicPosWindowLamp).getPicId(), "玻璃及大灯");
            }
            //删除
            boolean isDel = data.getBooleanExtra("isDel", false);
            if (isDel) {
                //删除本地图片
                String path = PadSysApp.picDirPath + pictureItemsWindowLamp.get(curPicPosWindowLamp).getPicId() + ".jpg";
                if (FileUtils.isFileExists(path)) {
                    FileUtils.deleteFile(path);
                }
                mapsPhotoWindowLamp.remove(pictureItemsWindowLamp.get(curPicPosWindowLamp).getPicId());
            }
        } else if (requestCode == PHOTO_BIG_PHOTO_BODYPAINT && resultCode == Activity.RESULT_OK) {//车身及漆面处于与车龄相适应的良好状态
            //重拍
            boolean recapture = data.getBooleanExtra("recapture", false);
            curPicPosBodyPaint = data.getIntExtra("recapturePosition", curPicPosBodyPaint);
            if (recapture) {
                FrescoCacheHelper.clearSingleCacheByUrl(pictureItemsBodyPaint.get(curPicPosBodyPaint).getPicPath(), false);
                pictureItemsBodyPaint.get(curPicPosBodyPaint).setPicPath("");
                useCamera(pictureItemsBodyPaint.get(curPicPosBodyPaint).getPicId(), "车身外观缺陷");
            }
            //删除
            boolean isDel = data.getBooleanExtra("isDel", false);
            if (isDel) {
                //删除本地图片
                String path = PadSysApp.picDirPath + pictureItemsBodyPaint.get(curPicPosBodyPaint).getPicId() + ".jpg";
                if (FileUtils.isFileExists(path)) {
                    FileUtils.deleteFile(path);
                }
                mapsPhotoBodyPaint.remove(pictureItemsBodyPaint.get(curPicPosBodyPaint).getPicId());
            }
        } else if (requestCode == PHOTO_BIG_PHOTO_BODYAPPEARANCEPASTE && resultCode == Activity.RESULT_OK) {//车身外观无粘贴物
            //重拍
            boolean recapture = data.getBooleanExtra("recapture", false);
            curPicPosBodyAppearancePaste = data.getIntExtra("recapturePosition", curPicPosBodyAppearancePaste);
            if (recapture) {
                FrescoCacheHelper.clearSingleCacheByUrl(pictureItemsBodyAppearancePaste.get(curPicPosBodyAppearancePaste).getPicPath(), false);
                pictureItemsBodyAppearancePaste.get(curPicPosBodyAppearancePaste).setPicPath("");
                useCamera(pictureItemsBodyAppearancePaste.get(curPicPosBodyAppearancePaste).getPicId(), "粘贴物");
            }
            //删除
            boolean isDel = data.getBooleanExtra("isDel", false);
            if (isDel) {
                //删除本地图片
                String path = PadSysApp.picDirPath + pictureItemsBodyAppearancePaste.get(curPicPosBodyAppearancePaste).getPicId() + ".jpg";
                if (FileUtils.isFileExists(path)) {
                    FileUtils.deleteFile(path);
                }
                mapsPhotoBodyAppearancePaste.remove(pictureItemsBodyAppearancePaste.get(curPicPosBodyAppearancePaste).getPicId());
            }
        } else if (requestCode == PHOTO_BIG_PHOTO_BODYAPPEARANCEPAINT && resultCode == Activity.RESULT_OK) {//车身漆面缺陷光洁、密闭
            //重拍
            boolean recapture = data.getBooleanExtra("recapture", false);
            curPicPosBodyAppearancePaint = data.getIntExtra("recapturePosition", curPicPosBodyAppearancePaint);
            if (recapture) {
                FrescoCacheHelper.clearSingleCacheByUrl(pictureItemsBodyAppearancePaint.get(curPicPosBodyAppearancePaint).getPicPath(), false);
                pictureItemsBodyAppearancePaint.get(curPicPosBodyAppearancePaint).setPicPath("");
                useCamera(pictureItemsBodyAppearancePaint.get(curPicPosBodyAppearancePaint).getPicId(), "漆面缺陷");
            }
            //删除
            boolean isDel = data.getBooleanExtra("isDel", false);
            if (isDel) {
                //删除本地图片
                String path = PadSysApp.picDirPath + pictureItemsBodyAppearancePaint.get(curPicPosBodyAppearancePaint).getPicId() + ".jpg";
                if (FileUtils.isFileExists(path)) {
                    FileUtils.deleteFile(path);
                }
                mapsPhotoBodyAppearancePaint.remove(pictureItemsBodyAppearancePaint.get(curPicPosBodyAppearancePaint).getPicId());
            }
        } else if (requestCode == PHOTO_BIG_PHOTO_BODYAPPEARANCECHROME && resultCode == Activity.RESULT_OK) {//车身外观镀铬部件状态良好
            if (data != null) {
                //重拍
                boolean recapture = data.getBooleanExtra("recapture", false);
                curPicPosBodyAppearanceChrome = data.getIntExtra("recapturePosition", curPicPosBodyAppearanceChrome);
                if (recapture) {
                    FrescoCacheHelper.clearSingleCacheByUrl(pictureItemsBodyAppearanceChrome.get(curPicPosBodyAppearanceChrome).getPicPath(), false);
                    pictureItemsBodyAppearanceChrome.get(curPicPosBodyAppearanceChrome).setPicPath("");
                    useCamera(pictureItemsBodyAppearanceChrome.get(curPicPosBodyAppearanceChrome).getPicId(), "镀铬部件缺陷");
                }
                //删除
                boolean isDel = data.getBooleanExtra("isDel", false);
                if (isDel) {
                    //删除本地图片
                    String path = PadSysApp.picDirPath + pictureItemsBodyAppearanceChrome.get(curPicPosBodyAppearanceChrome).getPicId() + ".jpg";
                    if (FileUtils.isFileExists(path)) {
                        FileUtils.deleteFile(path);
                    }
                    mapsPhotoBodyAppearanceChrome.remove(pictureItemsBodyAppearanceChrome.get(curPicPosBodyAppearanceChrome).getPicId());
                }

            }
        } else if (requestCode == PHOTO_REQUEST) {
            File file = new File(PadSysApp.picDirPath, curphotoPrefix + ".jpg");
            compress(context, file.getAbsolutePath(), curphotoPrefix, ((BMWDetectMainActivity) getActivity()).getTaskid());
        }
    }

    //压缩照片
    private void compress(Context context, String imgPath, final String defectPicId, final String taskId) {
        final String TAG = "PhotoCompressor";

        ImageCompressor.get(context)
                .setFilename("temp")
                .load(new File(imgPath))                     //传人要压缩的图片
                .putGear(ImageCompressor.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtil.e(TAG, "src pic:" + file.length() / 1024 + "kb");
                        String realPath = Constants.ROOT_DIR + File.separator + taskId + File.separator;
                        String fileName = defectPicId + ".jpg";
                        String fullPath = realPath + fileName;

                        LogUtil.e(TAG, "final Path=" + realPath + fileName);
                        if (FileUtils.createOrExistsDir(realPath)) {
                            if (FileUtils.isFileExists(fullPath))
                                FrescoCacheHelper.clearSingleCacheByUrl(fullPath, true);
                            FileUtils.copyFile(file, new File(realPath, fileName));
                            closeDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        closeDialog();
                    }
                }).launch();
    }

    public void showDialog() {
        if (dialog == null)
            dialog = ProgressDialog.show(this.getContext(), "照片处理中", "");
        else
            dialog.show();
    }

    private void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadPhoto();
        } else {
//            savedata();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPhoto();
    }

    //显示照片
    private void loadPhoto() {
        //当在车辆照片那儿 删除的时候，这边的maps还保留着，所以全清  -> 李波 on 2017/11/8.
        mapsPhotoWater.clear();
        mapsPhotoWindowLamp.clear();
        mapsPhotoBodyPaint.clear();
        mapsPhotoBodyAppearancePaste.clear();
        mapsPhotoBodyAppearancePaint.clear();
        mapsPhotoBodyAppearanceChrome.clear();

        pictureItemsWater.clear();
        pictureItemsWindowLamp.clear();
        pictureItemsBodyPaint.clear();
        pictureItemsBodyAppearancePaste.clear();
        pictureItemsBodyAppearancePaint.clear();
        pictureItemsBodyAppearanceChrome.clear();
        //读取本地拍照图片
        File dir = new File(PadSysApp.picDirPath);
        File[] files = dir.listFiles();
        if (files != null) {
            if (files.length > 0) {
                for (File file : files) {
                    if (file.getName().contains("QTJC_WaterCar_")) {
                        mapsPhotoWater.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
                        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                    } else if (file.getName().contains("QTJC_WindowLamp_")) {
                        mapsPhotoWindowLamp.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
                        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                    } else if (file.getName().contains("QTJC_BodyPaint_")) {
                        mapsPhotoBodyPaint.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
                        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                    } else if (file.getName().contains("QTJC_BodyAppearancePaste_")) {
                        mapsPhotoBodyAppearancePaste.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
                        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                    } else if (file.getName().contains("QTJC_BodyAppearancePaint_")) {
                        mapsPhotoBodyAppearancePaint.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
                        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                    } else if (file.getName().contains("QTJC_BodyAppearanceChrome_")) {
                        mapsPhotoBodyAppearanceChrome.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
                        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                    }
                }
            }
        }

        for (Map.Entry<String, String> entry : mapsPhotoWater.entrySet()) {
            pictureItemsWater.add(new PictureItem(entry.getKey(), "火烧泡水" + StringHelper.returnNumbers(entry.getKey()), entry.getValue()));
        }
        for (Map.Entry<String, String> entry : mapsPhotoWindowLamp.entrySet()) {
            pictureItemsWindowLamp.add(new PictureItem(entry.getKey(), "玻璃及大灯" + StringHelper.returnNumbers(entry.getKey()), entry.getValue()));
        }
        for (Map.Entry<String, String> entry : mapsPhotoBodyPaint.entrySet()) {
            pictureItemsBodyPaint.add(new PictureItem(entry.getKey(), "车身外观缺陷" + StringHelper.returnNumbers(entry.getKey()), entry.getValue()));
        }
        for (Map.Entry<String, String> entry : mapsPhotoBodyAppearancePaste.entrySet()) {
            pictureItemsBodyAppearancePaste.add(new PictureItem(entry.getKey(), "粘贴物" + StringHelper.returnNumbers(entry.getKey()), entry.getValue()));
        }
        for (Map.Entry<String, String> entry : mapsPhotoBodyAppearancePaint.entrySet()) {
            pictureItemsBodyAppearancePaint.add(new PictureItem(entry.getKey(), "漆面缺陷" + StringHelper.returnNumbers(entry.getKey()), entry.getValue()));
        }
        for (Map.Entry<String, String> entry : mapsPhotoBodyAppearanceChrome.entrySet()) {
            pictureItemsBodyAppearanceChrome.add(new PictureItem(entry.getKey(), "镀铬部件缺陷" + StringHelper.returnNumbers(entry.getKey()), entry.getValue()));
        }
        Collections.sort(pictureItemsWater);
        Collections.sort(pictureItemsWindowLamp);
        Collections.sort(pictureItemsBodyPaint);
        Collections.sort(pictureItemsBodyAppearancePaste);
        Collections.sort(pictureItemsBodyAppearancePaint);
        Collections.sort(pictureItemsBodyAppearanceChrome);
        updatePhoto();
    }

    public void updatePhoto() {

        adapterWater.setDataLists(pictureItemsWater);
        adapterWater.notifyDataSetChanged();
        if (pictureItemsWater != null && pictureItemsWater.size() > 0) {
            int curEndPos = Integer.parseInt(StringHelper.returnNumbers(pictureItemsWater.get(pictureItemsWater.size() - 1).getPicId()));
            if (curEndPos >= curEndPicWaterPos) {
                curEndPicWaterPos = curEndPos;
            }
        } else {
            curEndPicWaterPos = 0;
        }

        adapterWindowLamp.setDataLists(pictureItemsWindowLamp);
        adapterWindowLamp.notifyDataSetChanged();
        if (pictureItemsWindowLamp != null && pictureItemsWindowLamp.size() > 0) {
            int curEndPos = Integer.parseInt(StringHelper.returnNumbers(pictureItemsWindowLamp.get(pictureItemsWindowLamp.size() - 1).getPicId()));
            if (curEndPos >= curEndPicWindowLampPos) {
                curEndPicWindowLampPos = curEndPos;
            }
        } else {
            curEndPicWindowLampPos = 0;
        }

        adapterBodyPaint.setDataLists(pictureItemsBodyPaint);
        adapterBodyPaint.notifyDataSetChanged();

        if (pictureItemsBodyPaint != null && pictureItemsBodyPaint.size() > 0) {
            int curEndPos = Integer.parseInt(StringHelper.returnNumbers(pictureItemsBodyPaint.get(pictureItemsBodyPaint.size() - 1).getPicId()));
            if (curEndPos >= curEndPicBodyPaintPos) {
                curEndPicBodyPaintPos = curEndPos;
            }
        } else {
            curEndPicBodyPaintPos = 0;
        }

        adapterBodyAppearancePaste.setDataLists(pictureItemsBodyAppearancePaste);
        adapterBodyAppearancePaste.notifyDataSetChanged();
        if (pictureItemsBodyAppearancePaste != null && pictureItemsBodyAppearancePaste.size() > 0) {
            int curEndPos = Integer.parseInt(StringHelper.returnNumbers(pictureItemsBodyAppearancePaste.get(pictureItemsBodyAppearancePaste.size() - 1).getPicId()));
            if (curEndPos >= curEndPicBodyAppearancePastePos) {
                curEndPicBodyAppearancePastePos = curEndPos;
            }
        } else {
            curEndPicBodyAppearancePastePos = 0;
        }


        adapterBodyAppearancePaint.setDataLists(pictureItemsBodyAppearancePaint);
        adapterBodyAppearancePaint.notifyDataSetChanged();
        if (pictureItemsBodyAppearancePaint != null && pictureItemsBodyAppearancePaint.size() > 0) {
            int curEndPos = Integer.parseInt(StringHelper.returnNumbers(pictureItemsBodyAppearancePaint.get(pictureItemsBodyAppearancePaint.size() - 1).getPicId()));
            if (curEndPos >= curEndPicBodyAppearancePaintPos) {
                curEndPicBodyAppearancePaintPos = curEndPos;
            }
        } else {
            curEndPicBodyAppearancePaintPos = 0;
        }

        adapterBodyAppearanceChrome.setDataLists(pictureItemsBodyAppearanceChrome);
        adapterBodyAppearanceChrome.notifyDataSetChanged();
        if (pictureItemsBodyAppearanceChrome != null && pictureItemsBodyAppearanceChrome.size() > 0) {
            int curEndPos = Integer.parseInt(StringHelper.returnNumbers(pictureItemsBodyAppearanceChrome.get(pictureItemsBodyAppearanceChrome.size() - 1).getPicId()));
            if (curEndPos >= curEndPicBodyAppearanceChromePos) {
                curEndPicBodyAppearanceChromePos = curEndPos;
            }
        } else {
            curEndPicBodyAppearanceChromePos = 0;
        }
    }

    //回显数据
    private void showdata() {
        if (bmwOtherBean != null) {
            //内侧刹车片厚度 单位：mm
            etLeftFrontIn.setText(bmwOtherBean.getInsideBrakeLeftFront());
            etRightFrontIn.setText(bmwOtherBean.getInsideBrakeRightFront());
            etLeftBackIn.setText(bmwOtherBean.getInsideBrakeLeftAfter());
            etRightBackIn.setText(bmwOtherBean.getInsideBrakeRightAfter());

            //外侧刹车片厚度 单位：mm
            etLeftFrontOut.setText(bmwOtherBean.getOutsideBrakeLeftFront());
            etRightFrontOut.setText(bmwOtherBean.getOutsideBrakeRightFront());
            etLeftBackOut.setText(bmwOtherBean.getOutsideBrakeLeftAfter());
            etRightBackOut.setText(bmwOtherBean.getOutsideBrakeRightAfter());

            //刹车盘厚度 单位：mm
            etLeftFrontStop.setText(bmwOtherBean.getBrakeLeftFront());
            etRightFrontStop.setText(bmwOtherBean.getBrakeRightFront());
            etLeftBackStop.setText(bmwOtherBean.getBrakeLeftAfter());
            etRightBackStop.setText(bmwOtherBean.getBrakeRightAfter());

            //底盘有托底/损伤痕迹
            int qtjc_underpin = bmwOtherBean.getQTJC_Underpin();
            switch (qtjc_underpin) {
                case 0:
                    underpin.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    underpin.getRbtnNo().setChecked(true);
                    break;
            }

            //综合判断为泡水车
            int qtjc_waterCar = bmwOtherBean.getQTJC_WaterCar();
            switch (qtjc_waterCar) {
                case 0:
                    rbtnWaterYes.setChecked(true);
                    break;
                case 1:
                    rbtnWaterNo.setChecked(true);
                    break;
            }

            //车窗玻璃及大灯状态良好（不影响安全行驶）
            int qtjc_windowLamp = bmwOtherBean.getQTJC_WindowLamp();
            switch (qtjc_windowLamp) {
                case 0:
                    rbtnWindowLampYes.setChecked(true);
                    break;
                case 1:
                    rbtnWindowLampNo.setChecked(true);
                    break;
            }

            //车身及漆面处于与车龄相适应的良好状态
            int qtjc_bodyPaint = bmwOtherBean.getQTJC_BodyPaint();
            switch (qtjc_bodyPaint) {
                case 0:
                    rbtnBodyPaintYes.setChecked(true);
                    break;
                case 1:
                    rbtnBodyPaintNo.setChecked(true);
                    break;
            }

            //车身外观无粘贴物
            int qtjc_bodyAppearancePaste = bmwOtherBean.getQTJC_BodyAppearancePaste();
            switch (qtjc_bodyAppearancePaste) {
                case 0:
                    rbtnBodyAppearancePasteYes.setChecked(true);
                    break;
                case 1:
                    rbtnBodyAppearancePasteNo.setChecked(true);
                    break;
            }

            //车身漆面缺陷光洁、密闭
            int qtjc_bodyAppearancePaint = bmwOtherBean.getQTJC_BodyAppearancePaint();
            switch (qtjc_bodyAppearancePaint) {
                case 0:
                    rbtnBodyAppearancePaintYes.setChecked(true);
                    break;
                case 1:
                    rbtnBodyAppearancePaintNo.setChecked(true);
                    break;
            }

            //车身外观镀铬部件状态良好
            int qtjc_bodyAppearanceChrome = bmwOtherBean.getQTJC_BodyAppearanceChrome();
            switch (qtjc_bodyAppearanceChrome) {
                case 0:
                    rbtnBodyAppearanceChromeYes.setChecked(true);
                    break;
                case 1:
                    rbtnBodyAppearanceChromeNo.setChecked(true);
                    break;
            }

            //加装部件及其它改装项目等复合当地法律规定及具备相关许可证（对比行驶证）
            int qtjc_refitLicence = bmwOtherBean.getQTJC_RefitLicence();
            switch (qtjc_refitLicence) {
                case 0:
                    refitLicence.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    refitLicence.getRbtnNo().setChecked(true);
                    break;
            }

            //重新设定车辆及钥匙记忆
            int qtjc_resetKeyMemory = bmwOtherBean.getQTJC_ResetKeyMemory();
            switch (qtjc_resetKeyMemory) {
                case 0:
                    resetKeyMemory.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    resetKeyMemory.getRbtnNo().setChecked(true);
                    break;
            }

            //环保凭证，验车周期
            int qtjc_envCerPerIns = bmwOtherBean.getQTJC_EnvCerPerIns();
            switch (qtjc_envCerPerIns) {
                case 0:
                    envCerPerIns.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    envCerPerIns.getRbtnNo().setChecked(true);
                    break;
            }

            //导航地图已经免费升级到最新版本
            int qtjc_navigationMap = bmwOtherBean.getQTJC_NavigationMap();
            switch (qtjc_navigationMap) {
                case 0:
                    navigationMap.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    navigationMap.getRbtnNo().setChecked(true);
                    break;
            }

            //是否有违章记录
            int qtjc_violationRecord = bmwOtherBean.getQTJC_ViolationRecord();
            switch (qtjc_violationRecord) {
                case 0:
                    violationRecord.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    violationRecord.getRbtnNo().setChecked(true);
                    break;
            }

            //发动机、减震包车架号（变更手续完备）
            int qtjc_changeProcedure = bmwOtherBean.getQTJC_ChangeProcedure();
            switch (qtjc_changeProcedure) {
                case 0:
                    changeProcedure.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    changeProcedure.getRbtnNo().setChecked(true);
                    break;
            }

            //备注
            String remark = bmwOtherBean.getRemark();
            if (!TextUtils.isEmpty(remark)){
                etRemark.setText(remark);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setOnNextStepListener(INextStepListener iNextStepListener) {
        this.iNextStepListener = iNextStepListener;
    }

    @OnClick({R.id.tvNext, R.id.etLeftFront_in, R.id.etRightFront_in, R.id.etLeftBack_in, R.id.etRightBack_in,
            R.id.etLeftFront_out, R.id.etRightFront_out, R.id.etLeftBack_out, R.id.etRightBack_out, R.id.etLeftFront_stop,
            R.id.etRightFront_stop, R.id.etLeftBack_stop, R.id.etRightBack_stop, R.id.sdv_add_water, R.id.sdv_add_windowLamp,
            R.id.sdv_add_bodyPaint, R.id.sdv_add_bodyAppearancePaste, R.id.sdv_add_bodyAppearancePaint, R.id.sdv_add_bodyAppearanceChrome, R.id.btnfacade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                if (iNextStepListener != null) {
                    iNextStepListener.nextStep(1);
                    //解决有的小米Max2手机不隐藏软键盘的问题
                    KeyboardUtils.hideSoftInput(getActivity());
                }
                break;
            case R.id.etLeftFront_in:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etLeftFrontIn);
                break;
            case R.id.etRightFront_in:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etRightFrontIn);
                break;
            case R.id.etLeftBack_in:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etLeftBackIn);
                break;
            case R.id.etRightBack_in:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etRightBackIn);
                break;
            case R.id.etLeftFront_out:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etLeftFrontOut);
                break;
            case R.id.etRightFront_out:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etRightFrontOut);
                break;
            case R.id.etLeftBack_out:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etLeftBackOut);
                break;
            case R.id.etRightBack_out:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etRightBackOut);
                break;
            case R.id.etLeftFront_stop:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etLeftFrontStop);
                break;
            case R.id.etRightFront_stop:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etRightFrontStop);
                break;
            case R.id.etLeftBack_stop:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etLeftBackStop);
                break;
            case R.id.etRightBack_stop:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etRightBackStop);
                break;
            case R.id.sdv_add_water:
                useCamera("QTJC_WaterCar_" + (curEndPicWaterPos + 1), "火烧泡水");
                break;
            case R.id.sdv_add_windowLamp:
                useCamera("QTJC_WindowLamp_" + (curEndPicWindowLampPos + 1), "玻璃及大灯");
                break;
            case R.id.sdv_add_bodyPaint:
                useCamera("QTJC_BodyPaint_" + (curEndPicBodyPaintPos + 1), "车身外观缺陷");
                break;
            case R.id.sdv_add_bodyAppearancePaste:
                useCamera("QTJC_BodyAppearancePaste_" + (curEndPicBodyAppearancePastePos + 1), "粘贴物");
                break;
            case R.id.sdv_add_bodyAppearancePaint:
                useCamera("QTJC_BodyAppearancePaint_" + (curEndPicBodyAppearancePaintPos + 1), "漆面缺陷");
                break;
            case R.id.sdv_add_bodyAppearanceChrome:
                useCamera("QTJC_BodyAppearanceChrome_" + (curEndPicBodyAppearanceChromePos + 1), "镀铬部件缺陷");
                break;
            case R.id.btnfacade:
                Intent intent = new Intent(getActivity(), BMWCarSketchmapActivity.class);
                intent.putExtra("TaskId", ((BMWDetectMainActivity) getActivity()).getTaskid());
                jump(intent);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        savedata();
    }


    //保存数据
    private void savedata() {
        if (bmwOtherBean == null) {
            bmwOtherBean = new BMWOtherBean();
        }
        if (submitModel == null) {
            submitModel = ((BMWDetectMainActivity) getActivity()).getSubmitModel();
        }
        spCheckItemGroupList = ((BMWDetectMainActivity) getActivity()).getSpCheckItemGroupList();
        //防止提交时重复add数据进spCheckItemGroupList中
        if (spCheckItemGroupList != null && spCheckItemGroupList.size() > 0) {
            for (int i = 0; i < spCheckItemGroupList.size(); i++) {
                if (spCheckItemGroupList.get(i).getGroupId() == 1) {
                    spCheckItemGroupList.remove(i);
                }
            }
        }
        SubmitModel.SpCheckItemGroupListBean spCheckItemGroupListBean = new SubmitModel.SpCheckItemGroupListBean();
        spCheckItemGroupListBean.setGroupId(1);
        List<SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean> SpCheckItemList = new ArrayList<>();

        //内侧刹车片厚度
        bmwOtherBean.setInsideBrakeLeftFront(etLeftFrontIn.getText().toString().trim());
        bmwOtherBean.setInsideBrakeRightFront(etRightFrontIn.getText().toString().trim());
        bmwOtherBean.setInsideBrakeLeftAfter(etLeftBackIn.getText().toString().trim());
        bmwOtherBean.setInsideBrakeRightAfter(etRightBackIn.getText().toString().trim());

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean1 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean1.setNameEn("QTJC_InsideBrakePadsSize");//字段
        spCheckItemListBean1.setValueStr(etLeftFrontIn.getText().toString().trim() + "," + etRightFrontIn.getText().toString().trim() + "," +
                etLeftBackIn.getText().toString().trim() + "," + etRightBackIn.getText().toString().trim());//值
        spCheckItemListBean1.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean1);

        //外侧刹车片厚度
        bmwOtherBean.setOutsideBrakeLeftFront(etLeftFrontOut.getText().toString().trim());
        bmwOtherBean.setOutsideBrakeRightFront(etRightFrontOut.getText().toString().trim());
        bmwOtherBean.setOutsideBrakeLeftAfter(etLeftBackOut.getText().toString().trim());
        bmwOtherBean.setOutsideBrakeRightAfter(etRightBackOut.getText().toString().trim());

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean2 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean2.setNameEn("QTJC_OutsideBrakePadsSize");//字段
        spCheckItemListBean2.setValueStr(etLeftFrontOut.getText().toString().trim() + "," + etRightFrontOut.getText().toString().trim() + "," +
                etLeftBackOut.getText().toString().trim() + "," + etRightBackOut.getText().toString().trim());//值
        spCheckItemListBean2.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean2);

        //刹车盘厚度
        bmwOtherBean.setBrakeLeftFront(etLeftFrontStop.getText().toString().trim());
        bmwOtherBean.setBrakeRightFront(etRightFrontStop.getText().toString().trim());
        bmwOtherBean.setBrakeLeftAfter(etLeftBackStop.getText().toString().trim());
        bmwOtherBean.setBrakeRightAfter(etRightBackStop.getText().toString().trim());

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean3 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean3.setNameEn("QTJC_BrakeDiscSize");//字段
        spCheckItemListBean3.setValueStr(etLeftFrontStop.getText().toString().trim() + "," + etRightFrontStop.getText().toString().trim() + "," +
                etLeftBackStop.getText().toString().trim() + "," + etRightBackStop.getText().toString().trim());//值
        spCheckItemListBean3.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean3);

        //底盘有托底/损伤痕迹
        int rbtnUnderpinPos = underpin.getRbtnClickPos();
        bmwOtherBean.setQTJC_Underpin(rbtnUnderpinPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean4 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean4.setNameEn("QTJC_Underpin");//字段
        switch (rbtnUnderpinPos) {
            case 0:
                spCheckItemListBean4.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean4.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean4.setValueStr("-1");
        }
        spCheckItemListBean4.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean4);

        //综合判断为泡水车
        int rbtnWaterPos = rgWaterCar.indexOfChild(rgWaterCar.findViewById(rgWaterCar.getCheckedRadioButtonId()));
        bmwOtherBean.setQTJC_WaterCar(rbtnWaterPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean5 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean5.setNameEn("QTJC_WaterCar");//字段
        switch (rbtnWaterPos) {
            case 0:
                spCheckItemListBean5.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean5.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean5.setValueStr("-1");
        }
        spCheckItemListBean5.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean5);

        //车窗玻璃及大灯状态良好（不影响安全行驶）
        int rbtnWindowLampPos = rgWindowLamp.indexOfChild(rgWindowLamp.findViewById(rgWindowLamp.getCheckedRadioButtonId()));
        bmwOtherBean.setQTJC_WindowLamp(rbtnWindowLampPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean7 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean7.setNameEn("QTJC_WindowLamp");//字段
        switch (rbtnWindowLampPos) {
            case 0:
                spCheckItemListBean7.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean7.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean7.setValueStr("-1");
        }
        spCheckItemListBean7.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean7);

        //车身及漆面处于与车龄相适应的良好状态
        int rbtnBodyPaintPos = rgBodyPaint.indexOfChild(rgBodyPaint.findViewById(rgBodyPaint.getCheckedRadioButtonId()));
        bmwOtherBean.setQTJC_BodyPaint(rbtnBodyPaintPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean8 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean8.setNameEn("QTJC_BodyPaint");//字段
        switch (rbtnBodyPaintPos) {
            case 0:
                spCheckItemListBean8.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean8.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean8.setValueStr("-1");
        }
        spCheckItemListBean8.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean8);

        //车身外观无粘贴物
        int rbtnBodyAppearancePastePos = rgBodyAppearancePaste.indexOfChild(rgBodyAppearancePaste.findViewById(rgBodyAppearancePaste.getCheckedRadioButtonId()));
        bmwOtherBean.setQTJC_BodyAppearancePaste(rbtnBodyAppearancePastePos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean9 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean9.setNameEn("QTJC_BodyAppearancePaste");//字段
        switch (rbtnBodyAppearancePastePos) {
            case 0:
                spCheckItemListBean9.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean9.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean9.setValueStr("-1");
        }
        spCheckItemListBean9.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean9);

        //车身漆面缺陷光洁、密闭
        int rbtnBodyAppearancePaintPos = rgBodyAppearancePaint.indexOfChild(rgBodyAppearancePaint.findViewById(rgBodyAppearancePaint.getCheckedRadioButtonId()));
        bmwOtherBean.setQTJC_BodyAppearancePaint(rbtnBodyAppearancePaintPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean10 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean10.setNameEn("QTJC_BodyAppearancePaint");//字段
        switch (rbtnBodyAppearancePaintPos) {
            case 0:
                spCheckItemListBean10.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean10.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean10.setValueStr("-1");
        }
        spCheckItemListBean10.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean10);

        //车身外观镀铬部件状态良好
        int rbtnBodyAppearanceChromePos = rgBodyAppearanceChrome.indexOfChild(rgBodyAppearanceChrome.findViewById(rgBodyAppearanceChrome.getCheckedRadioButtonId()));
        bmwOtherBean.setQTJC_BodyAppearanceChrome(rbtnBodyAppearanceChromePos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean11 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean11.setNameEn("QTJC_BodyAppearanceChrome");//字段
        switch (rbtnBodyAppearanceChromePos) {
            case 0:
                spCheckItemListBean11.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean11.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean11.setValueStr("-1");
        }
        spCheckItemListBean11.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean11);

        //加装部件及其它改装项目等复合当地法律规定及具备相关许可证（对比行驶证）
        int rbtnRefitLicencePos = refitLicence.getRbtnClickPos();
        bmwOtherBean.setQTJC_RefitLicence(rbtnRefitLicencePos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean12 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean12.setNameEn("QTJC_RefitLicence");//字段
        switch (rbtnRefitLicencePos) {
            case 0:
                spCheckItemListBean12.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean12.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean12.setValueStr("-1");
        }
        spCheckItemListBean12.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean12);

        //重新设定车辆及钥匙记忆
        int rbtnResetKeyMemoryPos = resetKeyMemory.getRbtnClickPos();
        bmwOtherBean.setQTJC_ResetKeyMemory(rbtnResetKeyMemoryPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean13 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean13.setNameEn("QTJC_ResetKeyMemory");//字段
        switch (rbtnResetKeyMemoryPos) {
            case 0:
                spCheckItemListBean13.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean13.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean13.setValueStr("-1");
        }
        spCheckItemListBean13.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean13);

        //环保凭证，验车周期
        int rbtnEnvCerPerInsPos = envCerPerIns.getRbtnClickPos();
        bmwOtherBean.setQTJC_EnvCerPerIns(rbtnEnvCerPerInsPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean14 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean14.setNameEn("QTJC_EnvCerPerIns");//字段
        switch (rbtnEnvCerPerInsPos) {
            case 0:
                spCheckItemListBean14.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean14.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean14.setValueStr("-1");
        }
        spCheckItemListBean14.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean14);

        //导航地图已经免费升级到最新版本
        int rbtnNavigationMapPos = navigationMap.getRbtnClickPos();
        bmwOtherBean.setQTJC_NavigationMap(rbtnNavigationMapPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean15 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean15.setNameEn("QTJC_NavigationMap");//字段
        switch (rbtnNavigationMapPos) {
            case 0:
                spCheckItemListBean15.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean15.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean15.setValueStr("-1");
        }
        spCheckItemListBean15.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean15);

        //是否有违章记录
        int rbtnViolationRecordPos = violationRecord.getRbtnClickPos();
        bmwOtherBean.setQTJC_ViolationRecord(rbtnViolationRecordPos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean16 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean16.setNameEn("QTJC_ViolationRecord");//字段
        switch (rbtnViolationRecordPos) {
            case 0:
                spCheckItemListBean16.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean16.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean16.setValueStr("-1");
        }
        spCheckItemListBean16.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean16);

        //发动机、减震包车架号（变更手续完备）
        int rbtnChangeProcedurePos = changeProcedure.getRbtnClickPos();
        bmwOtherBean.setQTJC_ChangeProcedure(rbtnChangeProcedurePos);
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean17 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean17.setNameEn("QTJC_ChangeProcedure");//字段
        switch (rbtnChangeProcedurePos) {
            case 0:
                spCheckItemListBean17.setValueStr("1");//是
                break;
            case 1:
                spCheckItemListBean17.setValueStr("0");//否
                break;
            default:
                spCheckItemListBean17.setValueStr("-1");
        }
        spCheckItemListBean17.setIsQualified("-1");//值
        SpCheckItemList.add(spCheckItemListBean17);

        //备注
        String remark = etRemark.getText().toString();
        bmwOtherBean.setRemark(remark);
        submitModel.setAssessmentDes(remark);

        spCheckItemGroupListBean.setSpCheckItemList(SpCheckItemList);
        spCheckItemGroupList.add(spCheckItemGroupListBean);
        submitModel.setSpCheckItemGroupList(spCheckItemGroupList);
        ((BMWDetectMainActivity) getActivity()).setSubmitModel(submitModel);
        //保存到数据库
        String json = new Gson().toJson(bmwOtherBean);
        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_BMW_OTHER, taskId, PadSysApp.getUser().getUserId(), json);
    }

    //检查不可为空项是否都已填写并保存
    public boolean checkAndSaveData() {
        if (TextUtils.isEmpty(etLeftFrontIn.getText()) || TextUtils.isEmpty(etRightFrontIn.getText()) || TextUtils.isEmpty(etLeftBackIn.getText()) || TextUtils.isEmpty(etRightBackIn.getText())
                || TextUtils.isEmpty(etLeftFrontOut.getText()) || TextUtils.isEmpty(etRightFrontOut.getText()) || TextUtils.isEmpty(etLeftBackOut.getText())
                || TextUtils.isEmpty(etRightBackOut.getText()) || TextUtils.isEmpty(etLeftFrontStop.getText()) || TextUtils.isEmpty(etRightFrontStop.getText())
                || TextUtils.isEmpty(etLeftBackStop.getText()) || TextUtils.isEmpty(etRightBackStop.getText())) {
            MyToast.showLong("其他检查未填写完整");
            return false;
        }
        if (rgWaterCar.getCheckedRadioButtonId() == -1 || rgWindowLamp.getCheckedRadioButtonId() == -1 ||
                rgBodyPaint.getCheckedRadioButtonId() == -1 || rgBodyAppearancePaste.getCheckedRadioButtonId() == -1 ||
                rgBodyAppearancePaint.getCheckedRadioButtonId() == -1 || rgBodyAppearanceChrome.getCheckedRadioButtonId() == -1) {
            MyToast.showLong("其他检查未填写完整");
            return false;
        }
        if (underpin.getRg().getCheckedRadioButtonId() == -1 || refitLicence.getRg().getCheckedRadioButtonId() == -1 || resetKeyMemory.getRg().getCheckedRadioButtonId() == -1 ||
                envCerPerIns.getRg().getCheckedRadioButtonId() == -1 || navigationMap.getRg().getCheckedRadioButtonId() == -1 ||
                violationRecord.getRg().getCheckedRadioButtonId() == -1 || changeProcedure.getRg().getCheckedRadioButtonId() == -1) {
            MyToast.showLong("其他检查未填写完整");
            return false;
        }
        savedata();
        return true;
    }
}
