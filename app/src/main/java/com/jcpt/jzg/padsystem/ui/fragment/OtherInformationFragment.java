package com.jcpt.jzg.padsystem.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.model.OtherInformationModel;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.EventProcedureModel;
import com.jcpt.jzg.padsystem.vo.OtherCheckInfo;
import com.jcpt.jzg.padsystem.vo.ProcedureModel;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagAdapter;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *  * 作者：zyq
 *  * 邮箱：zhengyq@jingzhengu.com
 *  信息核对
 */

/**
 * Created by libo on 2016/11/11.（自此创建时间开始由李波接管此界面）
 *
 * @Email: libo@jingzhengu.com
 * @Description: 其他信息--信息核对界面
 */
public class OtherInformationFragment extends BaseFragment {

    public static final String TAG = OtherInformationFragment.class.getSimpleName();

    private final String SHOWMILEAGE_PICID = "2";   //表显里程 对应基本照片仪表盘id  -> 李波 on 2016/12/4.
    private final String CARCOLOR_PICID = "4";      //实车颜色 对应基本照片左前45度id  -> 李波 on 2016/12/4.
    private final String CARNAMEPLATE_PICID = "16";  //车身铭牌 对应基本照片铭牌id  -> 李波 on 2016/12/4.
    private final String CARVIN_PICID = "17";        //车架VIN  对应基本照片车架VIN号id  -> 李波 on 2016/12/4.
    private final String CARLICENSE_PICID = "19";    //行驶证车辆照片 对应手续信息行驶证正本背面照片id 固定等于 19  -> 李波 on 2016/12/4.
    private final String CARTYRE_PICID = "26"; //轮胎尺寸照片

    private String mileageHttpUrl;   //表显里程 对应基本照片仪表盘HTTP地址  -> 李波 on 2016/12/4.
    private String carColorHttpUrl;      //实车颜色 对应基本照片左前45度HTTP地址   -> 李波 on 2016/12/4.
    private String carNameplateHttpUrl;  //车身铭牌 对应基本照片铭牌HTTP地址   -> 李波 on 2016/12/4.
    private String carVinHttpUrl;        //车架VIN  对应基本照片车架VIN号HTTP地址   -> 李波 on 2016/12/4.
    private String carLicenseHttpUrl;    //行驶证车辆照片 对应手续信息行驶证正本背面照片HTTP地址   -> 李波 on 2016/12/4.
    private String carTyreHttpUrl;//轮胎规格照片

    @BindView(R.id.tflShowMileage)
    TagFlowLayout tflShowMileage;
    @BindView(R.id.tflShowMileageLight)
    TagFlowLayout tflShowMileageLight;
    @BindView(R.id.tflCarColor)
    TagFlowLayout tflCarColor;
    @BindView(R.id.tflCarNameplate)
    TagFlowLayout tflCarNameplate;
    @BindView(R.id.tflCarVin)
    TagFlowLayout tflCarVin;
    @BindView(R.id.tflCarTyre)
    TagFlowLayout tflCarTyre;
    @BindView(R.id.tflCarLicense)
    TagFlowLayout tflCarLicense;
    @BindView(R.id.tabrCarColor)
    TableRow tabrCarColor;
    @BindView(R.id.tabrCarTyre)
    TableRow tabrCarTyre;
    @BindView(R.id.tabrCarLicense)
    TableRow tabrCarLicense;
    @BindView(R.id.etShowMileage)
    EditText etShowMileage;
    @BindView(R.id.crbtnNextStep)
    CustomRippleButton crbtnNextStep;

    @BindView(R.id.sdvShowMileage)
    SimpleDraweeView sdvShowMileage;
    @BindView(R.id.sdvCarColor)
    SimpleDraweeView sdvCarColor;
    @BindView(R.id.sdvCarNameplate)
    SimpleDraweeView sdvCarNameplate;
    @BindView(R.id.sdvCarVin)
    SimpleDraweeView sdvCarVin;
    @BindView(R.id.sdvCarLicense)
    SimpleDraweeView sdvCarLicense;

    @BindView(R.id.llCarTyre)
    LinearLayout llCarTyre;

    @BindView(R.id.tvTyre)
    TextView tvTyre;
    @BindView(R.id.etOtherColor)
    EditText etOtherColor;
    @BindView(R.id.sdvCarTyre)
    SimpleDraweeView sdvCarTyre;
    Unbinder unbinder;

    private String[] tagShowMileageVals = {"公里数与实车不符"};
    private String[] tflShowMileageLightVals = {"仪表故障灯常亮"};
    private String[] tagCarColors = {"白", "灰", "红", "蓝", "绿", "紫", "粉", "黄", "黑", "棕", "橙", "银", "金", "青", "双色", "其他"};
    //    private String[] tagCarNameplates = {"破损","实车铭牌未见","铭牌出厂日期与登记证2014年12月12日不一致"};
    private String[] tagCarVins = {"实车车架号未见", "可以看清无法拓印", "锈蚀", "与登记证不一致", "打磨修复痕迹", "打刻较浅", "异物遮挡无法拍摄"};
    private String[] tagCarTyre = {"实车与登记证不一致", "同轴花纹不一致"};
    private String[] tagCarLicense = {"颜色不一致(贴纸)", "天窗不一致", "轮毂不一致", "车身覆盖件不一致", "前照灯总成不一致", "中网不一致"};

    TagAdapter mTagAdapter;

    //汽车铭牌 和 VIN 控制是否可多选/单选的角标项
    public static final int CARNAMEP_CONTROL = 1;
    public static final int CARVINS_CONTROL = 0;

    private INextStepListener iNextStepListener;
    private String taskId;
    private SubmitModel submitModel;
    //行驶证是否未见
    private boolean isShowDriverLicense = false;
    //登记证是否未见
    private boolean isHideRegistration = false;


    private OtherCheckInfo otherCheckInfo;

    private OtherInformationModel otherInformationModel;

    private int curPicPos = 0;
    private ArrayList<PictureItem>pictureItems = new ArrayList<>();
    private final int PHOTO_BIG_PHOTO = 2;
    private FrescoImageLoader frescoImageLoader;
    private boolean carTyreFlag;//显示轮胎规格的标记

    public void setOnNextStepListener(INextStepListener iNextStepListener) {
        this.iNextStepListener = iNextStepListener;
    }


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_information, container, false);

        frescoImageLoader = FrescoImageLoader.getSingleton();
        /**
         * Created by wujj on 2016/12/12.
         * 如果是EditText点击则重新获取焦点
         */
        UIUtils uiUtils = new UIUtils();
        uiUtils.setupUI(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initListener();//Created by wujj on 2016/12/12.
//        Log.i("other", "onCreateView");
        sdvCarTyre.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected void setView() {
//        Log.i("other", "onActivityCreated");
        init();
        otherInformationModel.setEtshowMileageTouch(etShowMileage);
        otherInformationModel.getSaveDataFromLocal(etShowMileage, tflShowMileage, tflShowMileageLight, tflCarColor, tflCarNameplate, tflCarVin, tflCarTyre, tflCarLicense, etOtherColor);

        //不能输入表情符 英文 数字
        etOtherColor.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            String changedText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.e("onTextChanged string","start :"+start+"  before: "+before+"  count: "+count);
                //增加字符
                if (before == 0 && count > 0) {
                    changedText = s.subSequence(start, start + count).toString();
                } else {
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (changedText.equals("")) {
                    return;
                }
                //判断新输入的字符是否有汉字或表情符，如果是就删除新输入的，将输入光标定位在最后位置
                //LogUtil.e("afterTextChanged changedText",changedText);
                Pattern p = Pattern.compile(".*[\ud83c\udc00-\ud83c\udfff].*|.*[\ud83d\udc00-\ud83d\udfff].*|.*[\u2600-\u27ff].*|.*[a-zA-Z].*|.*[0-9].*");
                Matcher m = p.matcher(changedText);
                if (m.matches()) {
                    etOtherColor.removeTextChangedListener(this);
                    etOtherColor.setText(beforeText);
                    etOtherColor.setSelection(etOtherColor.getText().length());
                    MyToast.showShort("不可输入表情符、英文或数字");
                    etOtherColor.addTextChangedListener(this);
                }
            }
        });

    }


    /**
     * Created by wujj on 2016/12/12.
     * 点击软键盘上的完成，EditText失去焦点
     */
    private void initListener() {
        etShowMileage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //隐藏软键盘--Created by wujj on 2016/12/12.
                    UIUtils.hideInputMethodManager(context);
                    //失去焦点--Created by wujj on 2016/12/12.
                    etShowMileage.clearFocus();
                    checkEtShowMileage(Integer.valueOf(etShowMileage.getText().toString()));//created by wujj on 2017/2/14
                }
                return false;
            }
        });

        etShowMileage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String sMileage = etShowMileage.getText().toString();
                    if(!TextUtils.isEmpty(sMileage)&&Integer.valueOf(sMileage)<100){
                        MyToast.showShort("表显里程填写不能小于100公里");
                    }
                }
            }
        });

        etShowMileage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0) {
                    if(submitModel==null){
                        return;
                    }
                    submitModel.setMileage(Integer.valueOf(s.toString()));
                }
            }
        });
//        Action1<TextViewTextChangeEvent> action = new Action1<TextViewTextChangeEvent>() {
//            @Override
//            public void call(TextViewTextChangeEvent event) {
//                EditText mileageView = (EditText) event.view();
//                String mileage = event.text().toString();
//                revert(mileageView, mileage);
//            }
//        };
//        RxTextView.textChangeEvents(etShowMileage)
//                .debounce(0, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(action);
    }

//    private void revert(EditText mileageView, String mileage) {
//        if (!TextUtils.isEmpty(mileage)){
//            if (mileage.startsWith("0")){
//                mileageView.setText("");
//                mileageView.setSelection(mileageView.getText().toString().length());
//                MyToast.showShort("首位不能输入0");
//            }
//        }
//    }

    /**
     * created by wujj on 2017/2/14
     * 离开焦点时校验表显里程不能小于100
     */
    public void checkEtShowMileage(int showmileage) {
/*        if(submitModel==null){
            submitModel = ((DetectMainActivity) this.getActivity()).getSubmitModel();
        }
        int showmileage = submitModel.getMileage();*/
        if (showmileage < 100) {
            MyToast.showShort("表显里程填写不能小于100公里");
        }
    }

    @Override
    protected void initData() {
        submitModel = ((DetectMainActivity) this.getActivity()).getSubmitModel();
        taskId = ((DetectMainActivity) getActivity()).getTaskid();

        //查询此条缓存是否存在
        boolean isExist = DBManager.getInstance().isExist(Constants.OTHER_INFROMATION, taskId, PadSysApp.getUser().getUserId());
        if (isExist) {
            List<DBBase> list = DBManager.getInstance().query(taskId, Constants.OTHER_INFROMATION, PadSysApp.getUser().getUserId());
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getDataType() == Constants.OTHER_INFROMATION) {
                        String json = list.get(i).getJson();
                        if (!TextUtils.isEmpty(json)) {
                            otherCheckInfo = new Gson().fromJson(json, OtherCheckInfo.class);
                        }
                    }
                }
            }
        }

        if (otherCheckInfo == null)
            otherCheckInfo = new OtherCheckInfo();

        otherInformationModel = new OtherInformationModel(this,submitModel, otherCheckInfo, taskId, context);

        otherInformationModel.calculationFlowLayoutItemWidth();
    }

    public void init() {

        otherInformationModel.initData(tflShowMileage, tflShowMileageLight, tflCarColor, tflCarNameplate, tflCarVin, tflCarTyre, tflCarLicense);
        if (submitModel == null) {
            return;
        }
        String productionTime = submitModel.getProductionTime();//登记日期
        String[] tagCarNameplates = {"破损", "实车铭牌未见", "铭牌出厂日期与登记证" + productionTime + "不一致"};

        setTag(tflShowMileage, tagShowMileageVals);
        setTag(tflShowMileageLight, tflShowMileageLightVals);
        setTag(tflCarColor, tagCarColors);
        setTag(tflCarNameplate, tagCarNameplates);
        setTag(tflCarVin, tagCarVins);

        setTag(tflCarTyre, tagCarTyre);
        setTag(tflCarLicense, tagCarLicense);

        /**
         * created by wujj on 2017/2/7
         * 如果登记证未见，“铭牌出厂日期与登记证不一致”，“与登记证不一致”，置灰不可选
         */
        ifNoRegistLicenseProperty();

        //处理照片的显示
        showPic();
    }

    /**
     * 处理照片的显示
     */
    private void showPic(){
        //处理照片的显示
        //方案里包含基本照片
        List<PictureItem> pictureList = DetectMainActivity.detectMainActivity.getLocalDetectionData().getPictureList();
        if (pictureList != null) {

            /**
             * zealjiang
             * 思路：取出本地保存的基本照片，对每一张基本照片进行处理（处理方法：第一：判断是图片地址是否为空，
             * 不为空判断是网络地址还是本地地址 如果是本地地址：判断图片是否存在，存在显示本地图片，不存在，将图片地址设置为空
             * 如果是网络地址：判断本地是否存在对应的图片，存在将本地地址设置到属性中，并显示，不存在显示网络图片
             * ）
             */
            for (int i = 0; i < pictureList.size(); i++) {
                PictureItem pictureItem = pictureList.get(i);
                String picId = pictureItem.getPicId();
                switch (picId) {
                    case "26": //轮胎尺寸
                        if (FileUtils.isFileExists(PadSysApp.picDirPath + "26.jpg")) {
                            Fresco.getImagePipeline().evictFromCache(Uri.parse(PadSysApp.picDirPath + "26.jpg"));
                            frescoImageLoader.displayImage(sdvCarTyre, PadSysApp.picDirPath + "26.jpg");
                        } else {
                            if (TextUtils.isEmpty(pictureItem.getPicPath())){
                                sdvCarTyre.setVisibility(View.GONE);
                            }
                            frescoImageLoader.displayImage(sdvCarTyre, pictureItem.getPicPath());
                            carTyreHttpUrl = pictureItem.getPicPath();
                        }
                        break;
                    case "2":  //仪表盘  -> 李波 on 2016/12/12.
                        if (FileUtils.isFileExists(PadSysApp.picDirPath + "2.jpg")) {
                            Fresco.getImagePipeline().evictFromCache(Uri.parse(PadSysApp.picDirPath + "2.jpg"));
                            frescoImageLoader.displayImage(sdvShowMileage, PadSysApp.picDirPath + "2.jpg");
                        } else {
                            frescoImageLoader.displayImage(sdvShowMileage, pictureItem.getPicPath());
                            mileageHttpUrl = pictureItem.getPicPath();
                        }
                        break;
                    case "4":  //左前45度  -> 李波 on 2016/12/12.
                        if (FileUtils.isFileExists(PadSysApp.picDirPath +"4.jpg")) {
                            Fresco.getImagePipeline().evictFromCache(Uri.parse(PadSysApp.picDirPath + "4.jpg"));
                            frescoImageLoader.displayImage(sdvCarColor, PadSysApp.picDirPath + "4.jpg");
                        } else {
                            frescoImageLoader.displayImage(sdvCarColor, pictureItem.getPicPath());
                            carColorHttpUrl = pictureItem.getPicPath();
                        }
                        break;
                    case "16": //铭牌  -> 李波 on 2016/12/12.
                        if (FileUtils.isFileExists(PadSysApp.picDirPath +  "16.jpg")) {
                            Fresco.getImagePipeline().evictFromCache(Uri.parse(PadSysApp.picDirPath + "16.jpg"));
                            frescoImageLoader.displayImage(sdvCarNameplate, PadSysApp.picDirPath + "16.jpg");
                        } else {
                            frescoImageLoader.displayImage(sdvCarNameplate, pictureItem.getPicPath());
                            carNameplateHttpUrl = pictureItem.getPicPath();
                        }
                        break;
                    case "17": //车架VIN  -> 李波 on 2016/12/12.
                        if (FileUtils.isFileExists(PadSysApp.picDirPath + "17.jpg")) {
                            Fresco.getImagePipeline().evictFromCache(Uri.parse(PadSysApp.picDirPath + "17.jpg"));
                            frescoImageLoader.displayImage(sdvCarVin, PadSysApp.picDirPath + "17.jpg");
                        } else {
                            frescoImageLoader.displayImage(sdvCarVin, pictureItem.getPicPath());
                            carVinHttpUrl = pictureItem.getPicPath();
                        }
                        break;
                }
            }
        }

        //行驶证正本背面照片
        //行驶证正本背面
        if (FileUtils.isFileExists(PadSysApp.picDirPath + "19.jpg")) {
            Fresco.getImagePipeline().evictFromCache(Uri.parse(PadSysApp.picDirPath + "19.jpg"));
            frescoImageLoader.displayImage(sdvCarLicense, PadSysApp.picDirPath + "19.jpg");
        } else {
            //从本地数据库中获取此用户下的taskId
            ProcedureModel procedureModel = null;
            List<DBBase> list = DBManager.getInstance().query(taskId,Constants.DATA_TYPE_PROCEDURE,PadSysApp.getUser().getUserId());
            LogUtil.e(TAG,"本地数据库： "+list+" list.size(): "+list.size());
            if(list!=null&&list.size()>0){
                String json = list.get(0).getJson();
                if(!TextUtils.isEmpty(json)){
                    procedureModel = new Gson().fromJson(json,ProcedureModel.class);
                }
            }
            if(procedureModel!=null){
                List<TaskDetailModel.ProcedurePicListBean> procedurePicList =  procedureModel.getProcedurePicList();//获取网络图片
                if(procedurePicList!=null){
                    for (int i = 0; i < procedurePicList.size(); i++) {
                        if(procedurePicList.get(i).getPicID().equals("19")){
                            //行驶证正本背面
                            frescoImageLoader.displayImage(sdvCarLicense, procedurePicList.get(i).getPicPath());
                            carLicenseHttpUrl = procedurePicList.get(i).getPicPath();
                        }
                    }
                }
            }
        }

    }

    //created by wujj on 2017/2/7
    public void ifNoRegistLicenseProperty() {
        int registLicenseProperty = submitModel.getRegistLicenseProperty();
        if (registLicenseProperty == 2) {
            TagView tagCarNameplate = (TagView) tflCarNameplate.getChildAt(2);
            tagCarNameplate.isEnabled = false;
            tagCarNameplate.setChecked(false);
            TextView tvCarNameplate = (TextView) tagCarNameplate.getTagView();
            tvCarNameplate.setBackgroundResource(R.drawable.not_optional_bg1);

            if (tflCarNameplate.getSelectedList().contains(2))
                tflCarNameplate.getSelectedList().remove(2);

            TagView tagCarVin = (TagView) tflCarVin.getChildAt(3);
            tagCarVin.isEnabled = false;
            tagCarVin.setChecked(false);
            TextView tvCarVin = (TextView) tagCarVin.getTagView();
            tvCarVin.setBackgroundResource(R.drawable.not_optional_bg1);

            if (tflCarVin.getSelectedList().contains(3))
                tflCarVin.getSelectedList().remove(3);
        } else {
            TagView tagCarNameplate = (TagView) tflCarNameplate.getChildAt(2);
            tagCarNameplate.isEnabled = true;
            TextView tvCarNameplate = (TextView) tagCarNameplate.getTagView();
            tvCarNameplate.setBackgroundResource(R.drawable.tag_bg1);

            TagView tagCarVin = (TagView) tflCarVin.getChildAt(3);
            tagCarVin.isEnabled = true;
            TextView tvCarVin = (TextView) tagCarVin.getTagView();
            tvCarVin.setBackgroundResource(R.drawable.tag_bg1);

            TagView Tag1 = (TagView) tflCarNameplate.getChildAt(1);
            if (Tag1.isChecked()) {
                otherInformationModel.changeTagViewEnabled(1, CARNAMEP_CONTROL, Tag1, tflCarNameplate);
            }
            TagView Tag2 = (TagView) tflCarVin.getChildAt(0);
            if (Tag2.isChecked()) {
                otherInformationModel.changeTagViewEnabled(0, CARVINS_CONTROL, Tag2, tflCarVin);
            }
        }
    }

    public void setTag(final TagFlowLayout tagFlowLayout, final String[] vals) {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mTagAdapter = new TagAdapter<String>(vals) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, tflShowMileage, false);

                otherInformationModel.adapterItemWidth(position, tv, tagFlowLayout, tflShowMileage, tflShowMileageLight, tflCarNameplate, tflCarColor);

                tv.setText(s);
                return tv;
            }
        };
        tagFlowLayout.setAdapter(mTagAdapter);

        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(TagView view, int position, FlowLayout parent) {

                TagView tagV = (TagView) tagFlowLayout.getChildAt(position);
                TextView tv = (TextView) tagV.getTagView();
//                MyToast.showLong(tv.getText() + "==== " + tagV.isChecked());

                if (tagFlowLayout == tflCarNameplate) {
                    otherInformationModel.changeTagViewEnabled(position, CARNAMEP_CONTROL, tagV, tagFlowLayout);
                    int registLicenseProperty = submitModel.getRegistLicenseProperty();
                    if (registLicenseProperty == 2) {
                        TagView tagCarNameplate = (TagView) tagFlowLayout.getChildAt(2);
                        tagCarNameplate.isEnabled = false;
                        tagCarNameplate.setChecked(false);
                        TextView tvCarNameplate = (TextView) tagCarNameplate.getTagView();
                        tvCarNameplate.setBackgroundResource(R.drawable.not_optional_bg1);

                        if (tflCarNameplate.getSelectedList().contains(2))
                            tflCarNameplate.getSelectedList().remove(2);
                    }
                } else if (tagFlowLayout == tflCarVin) {
                    otherInformationModel.changeTagViewEnabled(position, CARVINS_CONTROL, tagV, tagFlowLayout);
                    int registLicenseProperty = submitModel.getRegistLicenseProperty();
                    if (registLicenseProperty == 2) {
                        TagView tagCarVin = (TagView) tflCarVin.getChildAt(3);
                        tagCarVin.isEnabled = false;
                        tagCarVin.setChecked(false);
                        TextView tvCarVin = (TextView) tagCarVin.getTagView();
                        tvCarVin.setBackgroundResource(R.drawable.not_optional_bg1);

                        if (tflCarVin.getSelectedList().contains(3))
                            tflCarVin.getSelectedList().remove(3);
                    }
                } else if (tagFlowLayout == tflCarColor) {
                    if (position == 15 && tagV.isChecked()) {    //如果是其他颜色时显示颜色描述  -> 李波 on 2016/12/16.
                        etOtherColor.setVisibility(View.VISIBLE);
                    } else {
                        etOtherColor.setVisibility(View.GONE);
                    }
                }

                return true;
            }
        });

    }

    @OnClick({R.id.crbtnNextStep, R.id.sdvShowMileage, R.id.sdvCarColor, R.id.sdvCarNameplate, R.id.sdvCarVin, R.id.sdvCarTyre,R.id.sdvCarLicense})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.crbtnNextStep:
                otherInformationModel.saveDataToLocal(etShowMileage.getText().toString().trim(), etOtherColor.getText().toString().trim());
                iNextStepListener.nextStep(1);
                break;
            case R.id.sdvShowMileage:
                if (FileUtils.isFileExists(PadSysApp.picDirPath + SHOWMILEAGE_PICID + ".jpg"))
                    otherInformationModel.toPictureActivity(PadSysApp.picDirPath + SHOWMILEAGE_PICID + ".jpg", etShowMileage,"仪表盘","2");
                else
                    otherInformationModel.toPictureActivity(mileageHttpUrl, etShowMileage,"仪表盘","2");
                break;
            case R.id.sdvCarColor:
                if (FileUtils.isFileExists(PadSysApp.picDirPath + CARCOLOR_PICID + ".jpg"))
                    otherInformationModel.toPictureActivity(PadSysApp.picDirPath + CARCOLOR_PICID + ".jpg", etShowMileage,"左前45°","4");
                else
                    otherInformationModel.toPictureActivity(carColorHttpUrl, etShowMileage,"左前45°","4");
                break;
            case R.id.sdvCarNameplate:
                if (FileUtils.isFileExists(PadSysApp.picDirPath + CARNAMEPLATE_PICID + ".jpg"))
                    otherInformationModel.toPictureActivity(PadSysApp.picDirPath + CARNAMEPLATE_PICID + ".jpg", etShowMileage,"铭牌","16");
                else
                    otherInformationModel.toPictureActivity(carNameplateHttpUrl, etShowMileage,"铭牌","16");
                break;
            case R.id.sdvCarVin:

                if (FileUtils.isFileExists(PadSysApp.picDirPath + CARVIN_PICID + ".jpg"))
                    otherInformationModel.toPictureActivity(PadSysApp.picDirPath + CARVIN_PICID + ".jpg", etShowMileage,"车架VIN","17");
                else
                    otherInformationModel.toPictureActivity(carVinHttpUrl, etShowMileage,"车架VIN","17");
                break;
            case R.id.sdvCarTyre:
                if (FileUtils.isFileExists(PadSysApp.picDirPath + CARTYRE_PICID + ".jpg"))
                    otherInformationModel.toPictureActivity(PadSysApp.picDirPath + CARTYRE_PICID + ".jpg", etShowMileage,"轮胎尺寸","26");
                else
                    otherInformationModel.toPictureActivity(carTyreHttpUrl, etShowMileage,"轮胎尺寸","26");
                break;
            case R.id.sdvCarLicense:
                if (FileUtils.isFileExists(PadSysApp.picDirPath + CARLICENSE_PICID + ".jpg"))
                    otherInformationModel.toPictureActivity(PadSysApp.picDirPath + CARLICENSE_PICID + ".jpg", etShowMileage,"行驶证正本背面","19");
                else if (!TextUtils.isEmpty(carLicenseHttpUrl)) {
                    otherInformationModel.toPictureActivity(carLicenseHttpUrl, etShowMileage,"行驶证正本背面","19");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_BIG_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                boolean recapture = data.getBooleanExtra("recapture", false);
                curPicPos = data.getIntExtra("recapturePosition",curPicPos);
                pictureItems = (ArrayList<PictureItem>) data.getSerializableExtra("pictureItems");
                if (recapture) {
                    FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), false);
                    pictureItems.get(curPicPos).setPicPath("");
                    userCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE);
                }
            }
        }
    }
    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position, int capture_type) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", ((DetectMainActivity) getActivity()).getTaskid());
        intent.putExtra(Constants.CAPTURE_TYPE, capture_type);//拍摄模式，是单拍还是连拍
        if (capture_type == Constants.CAPTURE_TYPE_MULTI) {
            intent.putExtra("picList", pictureItems);
            intent.putExtra("position", position);
        } else {
            ArrayList<PictureItem> singleList = new ArrayList<>();
            singleList.add(pictureItems.get(position));
            intent.putExtra("picList", singleList);
            intent.putExtra("position", 0);
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        showRealTimeData();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveChange();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        saveChange();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventProcedureModel eventProcedureModel) {

        isShowDriverLicense = eventProcedureModel.isDrivingNoneIsSelect();
        isHideRegistration = eventProcedureModel.isRegisterNoneIsSelected();

        //根据手续信息登记证是否可见来显示隐藏轮胎规格  -> 李波 on 2017/1/5.
        if (isHideRegistration) {
            tabrCarTyre.setVisibility(View.GONE);
            //一旦隐藏恢复初始值  -> 李波 on 2017/1/5.
            otherInformationModel.reInitData(tflCarTyre);
            tvTyre.setText("");
        } else {
            tabrCarTyre.setVisibility(View.VISIBLE);
        }

        //根据手续信息行驶证是否可见来显示行驶证车辆照片  -> 李波 on 2017/1/5.
        if (isShowDriverLicense) {
            tabrCarLicense.setVisibility(View.GONE);
            //隐藏时恢复初始值  -> 李波 on 2017/1/5.
            otherInformationModel.reInitData(tflCarLicense);
            carLicenseHttpUrl = "";
        } else {
            tabrCarLicense.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Created by 李波 on 2016/12/5.
     * 预留保存按钮的保存数据
     */
/*    public void saveData() {
        otherInformationModel.saveData(etShowMileage.getText().toString().trim(), etOtherColor.getText().toString().trim());
    }*/

    /**
     * Created by 李波 on 2016/12/5.
     * 实时显示手续信息里铭牌日期与轮胎规格
     */
    public void showRealTimeData() {
        otherInformationModel.showRealTimeData(tflCarNameplate, tvTyre);
//        otherInformationModel.showPic(sdvCarLicense,sdvCarTyre,carTyreHttpUrl,llCarTyre);
        //是否显示轮胎规格照片
        ifShowCarTyrePic();
        //处理照片的显示
        showPic();
    }

    /**
     * 显示轮胎规格照片
     */
    private void ifShowCarTyrePic() {
        String planId = DetectMainActivity.detectMainActivity.getTaskItem().getPlanId();
        if (PadSysApp.wrapper != null) {//内存中如果存在对应planid的检测方案，则直接用
            showCarTyrePic(PadSysApp.wrapper);
        } else {
            boolean isExist = DBManager.getInstance().isPlanExist(planId, Constants.DATA_TYPE_PLAN);//内存中不存在，查数据库中是否有检测方案
            if (isExist) {//数据库中存在,直接用
                String json = DBManager.getInstance().queryLocalPlan(planId, Constants.DATA_TYPE_PLAN);
                DetectionWrapper wrapper = new Gson().fromJson(json, DetectionWrapper.class);
                if (wrapper != null) {
                    PadSysApp.wrapper = wrapper;
                    showCarTyrePic(wrapper);
                }
            }
        }
    }
    private void showCarTyrePic(DetectionWrapper wrapper) {
        carTyreFlag = false;
        List<PictureItem> pictureList = wrapper.getPictureList();
        if (pictureList != null && pictureList.size() > 0) {
            for (int i = 0; i < pictureList.size(); i++) {
                String picId = pictureList.get(i).getPicId();
                if (picId.equals("26")) {
                    carTyreFlag = true;
                }
            }
            if (carTyreFlag == true) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llCarTyre.getLayoutParams();
                layoutParams.height = UIUtils.dip2px(context, 200);
                llCarTyre.setLayoutParams(layoutParams);
                sdvCarTyre.setVisibility(View.VISIBLE);
                if (FileUtils.isFileExists(PadSysApp.picDirPath + "26.jpg")) {
                    Fresco.getImagePipeline().evictFromCache(Uri.parse(PadSysApp.picDirPath +  "26.jpg"));
                    frescoImageLoader.displayImage(sdvCarTyre, PadSysApp.picDirPath +  "26.jpg");
                } else {
                    if (TextUtils.isEmpty(carTyreHttpUrl)){
                        sdvCarTyre.setVisibility(View.GONE);
                    }
                    frescoImageLoader.displayImage(sdvCarTyre, carTyreHttpUrl);
                }
            } else {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llCarTyre.getLayoutParams();
                layoutParams.height = UIUtils.dip2px(context, 50);
                llCarTyre.setLayoutParams(layoutParams);
                sdvCarTyre.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Created by 李波 on 2016/12/5.
     * OtherFragmet 切换到其他fragment时，保存核对信息里的数据
     */
    public void saveChange() {
        String mileage ;
        String otherColor ;
        if(etShowMileage==null||etOtherColor==null) {
           return;
        }

        mileage = etShowMileage.getText().toString().trim();
        otherColor = etOtherColor.getText().toString().trim();

        otherInformationModel.saveChange(mileage,otherColor);
    }


    public boolean checkDataComplete() {
        return otherInformationModel.checkDataComplete(etShowMileage.getText().toString().trim(), etOtherColor.getText().toString().trim());
    }

}
