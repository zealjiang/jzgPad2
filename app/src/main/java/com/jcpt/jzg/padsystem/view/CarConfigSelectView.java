package com.jcpt.jzg.padsystem.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.dialog.MonthPickerDialog;
import com.jcpt.jzg.padsystem.mvpview.ICarConfigSelectInterface;
import com.jcpt.jzg.padsystem.presenter.CarConfigSelectPresenter;
import com.jcpt.jzg.padsystem.utils.DateTimeUtils;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.ShowDialogTool;
import com.jcpt.jzg.padsystem.vo.CarConfigModel;
import com.jcpt.jzg.padsystem.vo.LocalCarConfigModel;
import com.jcpt.jzg.padsystem.widget.ClearableEditText;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.nodoubleclick.NoDoubleOnclickListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 车型选择--参数选择
 * Created by zealjiang on 2016/11/2 17:58.
 * Email: zealjiang@126.com
 */

public class CarConfigSelectView extends LinearLayout implements ICarConfigSelectInterface {

    private CarConfigSelectView self;
    private final String TAG = "CarConfigSelectView";

    @BindView(R.id.tagflow_gear)
    TagFlowLayout tagFlowGear;
    @BindView(R.id.tagflow_driving_mode)
    TagFlowLayout tagFlowDrivingMode;
    @BindView(R.id.tagflow_displacement)
    TagFlowLayout tagFlowDisplacement;
    @BindView(R.id.crb_date)
    CustomRippleButton crbDateSelect;
    @BindView(R.id.viewFragReload)
    RelativeLayout viewFragReload;
    @BindView(R.id.cetCarNamePlate)
    ClearableEditText cetCarNamePlate;


    private CarConfigSelectPresenter carConfigSelectPresenter;

    private ArrayList<String> gearList = new ArrayList<>();
    private ArrayList<String> drivingModeList = new ArrayList<>();
    private ArrayList<String> displacementList = new ArrayList<>();

    private TagAdapter tagAdapterGear;
    private TagAdapter tagAdapterDrivingMode;
    private TagAdapter tagAdapterDisplacement;

    private DatePickerDialog dialog;
    private Calendar calendar;
    private int year;
    private int month = -1;
    private int day;
    private CarConfigModel carConfigModel;
    private String gearSelected;//变速箱当前选中项
    private String drivingModeSelected;//驱动方式当前选中项
    private String displacementSelected;//排气量当前选中项
    private LocalCarConfigModel localCarConfigModel;
    private ArrayList<String> selectedCondition = new ArrayList();//查询条件，有先后顺序之分
    private ShowDialogTool showDialogTool;

    public CarConfigSelectView(Context context) {
        super(context);
        initView();
    }

    public CarConfigSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CarConfigSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        self = this;
        showDialogTool = new ShowDialogTool();
        View.inflate(getContext(), R.layout.widget_config_select, this);
        ButterKnife.bind(this);
        carConfigSelectPresenter = new CarConfigSelectPresenter(this);
        initData();
    }

    /**
     * 请求参数选择
     * @author zealjiang
     * @time 2016/11/2 20:48
     */
    public void requestConfig(int modelId){
        carConfigSelectPresenter.requestCarConfig(modelId);
    }

    /**
     * 设置车辆铭牌
     * @author zealjiang
     * @time 2017/1/17 10:19
     */
    public void setNameplate(String nameplate){
        cetCarNamePlate.setText(nameplate);
    }

    /**
     * 设置出厂日期
     * @author zealjiang
     * @time 2016/11/4 9:30
     */
    public void setDate(int year,int month,int day){
        if(year==0){
            String date = DateTimeUtils.getNow("yyyy月MM");
            this.year = calendar.get(Calendar.YEAR);
            this.month = calendar.get(Calendar.MONTH)+1;
            this.day = calendar.get(Calendar.DAY_OF_MONTH);
            crbDateSelect.setText(date);
        }else{
            this.year = year;
            this.month = month;
            this.day = day;
            crbDateSelect.setText(year+"年"+month+"月");
        }

    }

    private void initData() {

        //设置默认出厂日期
        String date = DateTimeUtils.getNow("yyyy月MM");
        crbDateSelect.setText(date);
        this.year = Integer.valueOf(date.substring(0,4));
        this.month = Integer.valueOf(date.substring(5,7));


        final LayoutInflater mInflater = LayoutInflater.from(this.getContext().getApplicationContext());

        //变速箱
        tagAdapterGear = new TagAdapter<String>(gearList)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_car_config, tagFlowGear, false);
                tv.setText(s);
                return tv;
            }
        };
        tagFlowGear.setAdapter(tagAdapterGear);
        tagFlowGear.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                //Toast.makeText(CarConfigSelectView.this.getContext(), gearList.get(position), Toast.LENGTH_SHORT).show();
                String gearSelected = gearList.get(position);
                if(gearSelected.equals(self.gearSelected)){
                    //如果包含此条件，则去除此条件
                    for (int i = 0; i < selectedCondition.size(); i++) {
                        if(selectedCondition.get(i).startsWith("变速箱:")){
                            selectedCondition.remove(i);
                            break;
                        }
                    }

                    //将选中状态置为未选中状态并重置所有选项(所有选项中保持之前的选中状态)
                    self.gearSelected = "";

                    setConfigListBySelection(selectedCondition);
                    setSelectedList();
                    refresh();
                }else{
                    //如果不包含此条件，则添加此条件
                    boolean has = false;
                    for (int i = 0; i < selectedCondition.size(); i++) {
                        if(selectedCondition.get(i).startsWith("变速箱:")){
                            has = true;
                            break;
                        }
                    }
                    if(!has){
                        selectedCondition.add("变速箱:"+gearSelected);
                    }

                    self.gearSelected = gearSelected;

                    setConfigListBySelection(selectedCondition);
                    setSelectedList();
                    refresh();
                }
                return true;
            }
        });

        //驱动方式
        tagAdapterDrivingMode = new TagAdapter<String>(drivingModeList)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_car_config, tagFlowDrivingMode, false);
                tv.setText(s);
                return tv;
            }
        };
        tagFlowDrivingMode.setAdapter(tagAdapterDrivingMode);
        tagFlowDrivingMode.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                String drivingModeSelected = drivingModeList.get(position);
                if(drivingModeSelected.equals(self.drivingModeSelected)){
                    //如果包含此条件，则去除此条件
                    for (int i = 0; i < selectedCondition.size(); i++) {
                        if(selectedCondition.get(i).startsWith("驱动方式:")){
                            selectedCondition.remove(i);
                            break;
                        }
                    }

                    //将选中状态置为未选中状态并重置所有选项(所有选项中保持之前的选中状态)
                    self.drivingModeSelected = "";
                    //resetConfig(self.drivingModeSelected);

                    setConfigListBySelection(selectedCondition);
                    setSelectedList();
                    refresh();
                }else{
                    //如果不包含此条件，则添加此条件
                    boolean has = false;
                    for (int i = 0; i < selectedCondition.size(); i++) {
                        if(selectedCondition.get(i).startsWith("驱动方式:")){
                            has = true;
                            break;
                        }
                    }
                    if(!has){
                        selectedCondition.add("驱动方式:"+drivingModeSelected);
                    }

                    self.drivingModeSelected = drivingModeSelected;

                    setConfigListBySelection(selectedCondition);
                    setSelectedList();
                    refresh();
                }
                return true;

            }
        });

        //排气量
        tagAdapterDisplacement= new TagAdapter<String>(displacementList)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_car_config, tagFlowDisplacement, false);
                tv.setText(s);
                return tv;
            }
        };
        tagFlowDisplacement.setAdapter(tagAdapterDisplacement);
        tagFlowDisplacement.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                String displacementSelected = displacementList.get(position);
                if(displacementSelected.equals(self.displacementSelected)){
                    //如果包含此条件，则去除此条件
                    for (int i = 0; i < selectedCondition.size(); i++) {
                        if(selectedCondition.get(i).startsWith("排气量:")){
                            selectedCondition.remove(i);
                            break;
                        }
                    }
                    //将选中状态置为未选中状态并重置所有选项(所有选项中保持之前的选中状态)
                    self.displacementSelected = "";

                    setConfigListBySelection(selectedCondition);
                    setSelectedList();
                    refresh();
                }else{
                    //如果不包含此条件，则添加此条件
                    boolean has = false;
                    for (int i = 0; i < selectedCondition.size(); i++) {
                        if(selectedCondition.get(i).startsWith("排气量:")){
                            has = true;
                            break;
                        }
                    }
                    if(!has){
                        selectedCondition.add("排气量:"+displacementSelected);
                    }

                    self.displacementSelected = displacementSelected;

                    setConfigListBySelection(selectedCondition);
                    setSelectedList();
                    refresh();
                }
                return true;

            }
        });

        //出厂日期
        crbDateSelect.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {

                MonthPickerDialog monthPickerDialog = new MonthPickerDialog(self.getContext(),"请选择出厂日期",year,month,false);
                monthPickerDialog.setMonthPickerOkListenter(new MonthPickerDialog.MonthPickerOkListenter() {
                    @Override
                    public void selectDate(int year, int month, String yearMonth) {
                        String date = year + "年" + month + "月";
                        crbDateSelect.setText(date);
                        self.year = year;
                        self.month = month;
                    }
                });
                monthPickerDialog.createDialog();
            }
        });

        //铭牌车辆
        cetCarNamePlate.setTransformationMethod(new InputLowerToUpper());

        cetCarNamePlate.addTextChangedListener(new TextWatcher() {
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
                if(before==0&&count>0) {
                    changedText = s.subSequence(start, start + count).toString();
                }else{
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(changedText.equals("")){
                    return;
                }
                //判断新输入的字符是否有汉字或表情符，如果是就删除新输入的，将输入光标定位在最后位置
                //LogUtil.e("afterTextChanged changedText",changedText);
                Pattern p = Pattern.compile(".*[\u4e00-\u9fa5].*|.*[\ud83c\udc00-\ud83c\udfff].*|.*[\ud83d\udc00-\ud83d\udfff].*|.*[\u2600-\u27ff].*");
                //Pattern p = Pattern.compile(".*[\u4e00-\u9fa5].*");
                Matcher m = p.matcher(changedText);
                if(m.matches()){
                    cetCarNamePlate.removeTextChangedListener(this);
                    cetCarNamePlate.setText(beforeText);
                    cetCarNamePlate.setSelection(cetCarNamePlate.getText().length());
                    MyToast.showShort("不可输入汉字或表情符");
                    cetCarNamePlate.addTextChangedListener(this);
                }
            }
        });
    }

    private void addGear(String gear){
        if(!gearList.contains(gear)){
            if(!TextUtils.isEmpty(gear)) {
                gearList.add(gear);
            }
        }
    }
    private void addDrivingMode(String drivingMode){
        if(!drivingModeList.contains(drivingMode)){
            if(!TextUtils.isEmpty(drivingMode)) {
                drivingModeList.add(drivingMode);
            }
        }
    }
    private void addDisplacement(String displacement){
        if(!displacementList.contains(displacement)){
            if(!TextUtils.isEmpty(displacement)){
                displacementList.add(displacement);
            }
        }
    }

    @Override
    public void succeed(CarConfigModel carConfigModel) {

        //初始化参数
        if(null==carConfigModel){
            return;
        }
        if(carConfigModel.getMemberValue().size()==0){
            return;
        }
        hideLoadError();

        //清空所有选项数据
        clearConfigData();

        //清空所有选中条件
        selectedCondition.clear();

        this.carConfigModel = carConfigModel;
        List<CarConfigModel.MemberValueBean> memberValueBeanList = carConfigModel.getMemberValue();
        for (int i = 0; i < memberValueBeanList.size(); i++) {
            CarConfigModel.MemberValueBean memberValueBean = memberValueBeanList.get(i);
            //变速箱
            String gear = memberValueBean.getGear();
            addGear(gear);
            //驱动方式
            String drivingMode = memberValueBean.getDrivingMode();
            addDrivingMode(drivingMode);
            //排气量
            String displacement = memberValueBean.getDisplacement();
            addDisplacement(displacement);
        }

        //清空选中的配置选项
        clearSelectedConfig();

        //设置默认选中项
        setSelectedList();

        //刷新参数选择
        refresh();

    }

    /**
     * 如果该选项下只有一项，则设置该项为选中项;
     * 或如果该选项下有多个选项，则选中上次选中的项;
     * 或如果都有一项被选中，则只显示选中的项;
     * @author zealjiang
     * @time 2017/03/20 11:18
     */
    private void setSelectedList(){
        //清空所有默认选中项
        HashSet<Integer> set = new HashSet<>();
        tagAdapterGear.setSelectedList(set);
        tagAdapterDrivingMode.setSelectedList(set);
        tagAdapterDisplacement.setSelectedList(set);

        //如果该选项下只有一项，则设置该项为选中项;如果同一类选项下有一项被选中，则不显示此类下的其它项
        //变速箱
        if(gearList.size()==1){
            tagAdapterGear.setSelectedList(0);
            gearSelected = gearList.get(0);
        }

        //驱动方式
        if(drivingModeList.size()==1){
            tagAdapterDrivingMode.setSelectedList(0);
            drivingModeSelected = drivingModeList.get(0);
        }

        //排气方式
        if(displacementList.size()==1){
            tagAdapterDisplacement.setSelectedList(0);
            displacementSelected = displacementList.get(0);
        }
    }

    /**
     * 根据选中的变速箱，找出符合的驱动方式和排气量
     */
    private void setConfigListBySelection(ArrayList selectedCondition){

        //清空所有选项数据
        clearConfigData();

        int size = selectedCondition.size();
        if(size==0){
            //重置
            List<CarConfigModel.MemberValueBean> memberValueBeanList = carConfigModel.getMemberValue();
            for (int i = 0; i < memberValueBeanList.size(); i++) {
                CarConfigModel.MemberValueBean memberValueBean = memberValueBeanList.get(i);
                //变速箱
                String gear = memberValueBean.getGear();
                addGear(gear);
                //驱动方式
                String drivingMode = memberValueBean.getDrivingMode();
                addDrivingMode(drivingMode);
                //排气量
                String displacement = memberValueBean.getDisplacement();
                addDisplacement(displacement);
            }
        }else if(size==1){
            String config = selectedCondition.get(0).toString().split(":")[0];
            String configValue = selectedCondition.get(0).toString().split(":")[1];
            if(config.equals("变速箱")){
                gearList.add(configValue);
                //查找此变速箱条件下的驱动方式和排气量
                List<CarConfigModel.MemberValueBean> memberValueBeanList = carConfigModel.getMemberValue();
                for (int j = 0; j < memberValueBeanList.size(); j++) {
                    CarConfigModel.MemberValueBean memberValueBean = memberValueBeanList.get(j);
                    //变速箱
                    String gear = memberValueBean.getGear();
                    if(gear.equals(configValue)){
                        //驱动方式
                        String drivingMode = memberValueBean.getDrivingMode();
                        addDrivingMode(drivingMode);
                        //排气量
                        String displacement = memberValueBean.getDisplacement();
                        addDisplacement(displacement);
                    }
                }
            }else if(config.equals("驱动方式")){
                drivingModeList.add(configValue);
                //获取此驱动方式下的所有变速箱和排气量
                List<CarConfigModel.MemberValueBean> memberValueBeanList = carConfigModel.getMemberValue();
                for (int i = 0; i < memberValueBeanList.size(); i++) {
                    CarConfigModel.MemberValueBean memberValueBean = memberValueBeanList.get(i);
                    //驱动方式
                    String drivingMode = memberValueBean.getDrivingMode();
                    if(drivingMode.equals(configValue)){
                        //变速箱
                        String gear = memberValueBean.getGear();
                        addGear(gear);
                        //排气量
                        String displacement = memberValueBean.getDisplacement();
                        addDisplacement(displacement);
                    }
                }
            }else if(config.equals("排气量")){
                displacementList.add(configValue);
                //获取此排气量下的所有变速箱和驱动方式
                List<CarConfigModel.MemberValueBean> memberValueBeanList = carConfigModel.getMemberValue();
                for (int i = 0; i < memberValueBeanList.size(); i++) {
                    CarConfigModel.MemberValueBean memberValueBean = memberValueBeanList.get(i);
                    //排气量
                    String dispalcement = memberValueBean.getDisplacement();
                    if(dispalcement.equals(configValue)){
                        //变速箱
                        String gear = memberValueBean.getGear();
                        addGear(gear);
                        //驱动方式
                        String drivingMode = memberValueBean.getDrivingMode();
                        addDrivingMode(drivingMode);
                    }
                }
            }
        }else if(size==2){
            //得到选中的项
            HashMap<String,String> configMap = new HashMap<>();
            for (int i = 0; i < selectedCondition.size(); i++) {
                String config = selectedCondition.get(i).toString().split(":")[0];
                String configValue = selectedCondition.get(i).toString().split(":")[1];
                configMap.put(config, configValue);
            }
            //根据选中项，得到未选中项
            List<CarConfigModel.MemberValueBean> memberValueBeanList = carConfigModel.getMemberValue();
            for (int i = 0; i < memberValueBeanList.size(); i++) {
                CarConfigModel.MemberValueBean memberValueBean = memberValueBeanList.get(i);
                if(configMap.containsKey("变速箱")){
                    //添加变速箱
                    addGear(configMap.get("变速箱"));
                    //变速箱
                    String gear = memberValueBean.getGear();
                    if(configMap.get("变速箱").equals(gear)){
                        //驱动方式
                        if(configMap.containsKey("驱动方式")){
                            //添加驱动方式
                            addDrivingMode(configMap.get("驱动方式"));
                            String drivingMode = memberValueBean.getDrivingMode();
                            if(configMap.get("驱动方式").equals(drivingMode)){
                                //满足条件，将对应的排气量加入
                                //变速箱
                                String displacement = memberValueBean.getDisplacement();
                                addDisplacement(displacement);
                                //判断下一个
                                continue;
                            }else{
                                //判断下一个
                                continue;
                            }
                        }else{
                            //排气量
                            if(configMap.containsKey("排气量")){
                                //添加排气量
                                addDisplacement(configMap.get("排气量"));
                                String displacement = memberValueBean.getDisplacement();
                                if(configMap.get("排气量").equals(displacement)){
                                    //满足条件，将对应的驱动方式加入
                                    //驱动方式
                                    String drivingMode = memberValueBean.getDrivingMode();
                                    addDrivingMode(drivingMode);
                                    //判断下一个
                                    continue;
                                }
                            }else{
                                MyToast.showShort("选中两项条件出错 只有变速箱");
                                break;
                            }
                        }
                    }else {
                        continue;
                    }
                }else {
                    if (configMap.containsKey("驱动方式")) {
                        //添加驱动方式
                        addDrivingMode(configMap.get("驱动方式"));

                        //驱动方式
                        String drivingMode = memberValueBean.getDrivingMode();
                        if (configMap.get("驱动方式").equals(drivingMode)) {
                            //排气量
                            String displacement = memberValueBean.getDisplacement();
                            if (configMap.containsKey("排气量")) {
                                //添加排气量
                                addDisplacement(configMap.get("排气量"));

                                if (configMap.get("排气量").equals(displacement)) {
                                    //满足条件，将对应的变速箱加入
                                    //变速箱
                                    String gear = memberValueBean.getGear();
                                    addGear(gear);
                                    //判断下一个
                                    continue;
                                }
                            }else {
                                MyToast.showShort("选中两项条件出错 只有驱动方式");
                                break;
                            }

                        }else {
                            continue;
                        }
                    } else {
                        MyToast.showShort("选中两项条件出错 最多只包含排气量一项");
                        break;
                    }
                }


            }
        }else if(size==3){
            for (int i = 0; i < selectedCondition.size(); i++) {
                String config = selectedCondition.get(i).toString().split(":")[0];
                String configValue = selectedCondition.get(i).toString().split(":")[1];
                if(config.equals("变速箱")) {
                    gearList.add(configValue);
                }else if(config.equals("驱动方式")){
                    drivingModeList.add(configValue);
                }else if(config.equals("排气量")){
                    displacementList.add(configValue);
                }
            }
        }
    }

    /**
     * 重置 车辆配置（变速箱、排气量、驱动方式）
     */
    public void resetConfig(){

        //清空所有选中条件
        selectedCondition.clear();

        //清空所有选项数据
        clearConfigData();

        List<CarConfigModel.MemberValueBean> memberValueBeanList = carConfigModel.getMemberValue();
        for (int i = 0; i < memberValueBeanList.size(); i++) {
            CarConfigModel.MemberValueBean memberValueBean = memberValueBeanList.get(i);
            //变速箱
            String gear = memberValueBean.getGear();
            addGear(gear);
            //驱动方式
            String drivingMode = memberValueBean.getDrivingMode();
            addDrivingMode(drivingMode);
            //排气量
            String displacement = memberValueBean.getDisplacement();
            addDisplacement(displacement);
        }

        //清空选中的配置选项
        clearSelectedConfig();

        //设置默认选中项
        setSelectedList();

        //刷新参数选择
        refresh();

    }

    /**
     * 清空选中的配置选项
     * @author zealjiang
     * @time 2016/11/4 14:33
     */
    private void clearSelectedConfig(){
        gearSelected = "";//变速箱当前选中项
        drivingModeSelected = "";//驱动方式当前选中项
        displacementSelected = "";//排气量当前选中项
    }


    /**
     * 清空铭牌车辆型号
     * @author zealjiang
     * @time 2017/1/20 17:49
     */
    public void clearCarNamePlate(){
        //清空铭牌车辆型号
        cetCarNamePlate.setText("");
    }

    /**
     * 清空所有选项数据
     * @author zealjiang
     * @time 2016/11/4 11:19
     */
    private void clearConfigData(){
        gearList.clear();
        drivingModeList.clear();
        displacementList.clear();
    }


    /**
     * 刷新参数选择
     * @author zealjiang
     * @time 2016/11/4 11:08
     */
    private void refresh(){
        //刷新参数选择
        tagAdapterGear.notifyDataChanged();
        tagAdapterDrivingMode.notifyDataChanged();
        tagAdapterDisplacement.notifyDataChanged();
    }

    /**
     * 检查必填项是否都已经填写完成
     * @author zealjiang
     * @time 2016/11/10 13:49
     */
    public boolean checkInput(){
        //铭牌车辆型号
        if(TextUtils.isEmpty(cetCarNamePlate.getText().toString())){
            MyToast.showShort("请输入铭牌车辆型号");
            return false;
        }
        //变速箱
        if(tagFlowGear.getSelectedList().size()==0){
            MyToast.showShort("请选择变速箱");
            return false;
        }
        //驱动方式
        if(tagFlowDrivingMode.getSelectedList().size()==0){
            MyToast.showShort("请选择驱动方式");
            return false;
        }
        //排气量
        if(tagFlowDisplacement.getSelectedList().size()==0){
            MyToast.showShort("请选择排气量");
            return false;
        }
        //出厂日期
        if(year==0||month==-1){
            MyToast.showShort("请选择出厂日期");
            return false;
        }
        return true;
    }

    /**
     * 获取车系选择选中的数据
     * @author zealjiang
     * @time 2016/11/10 14:15
     */
    public LocalCarConfigModel getSelectedSeriesData(){

        //变速箱
        localCarConfigModel.setGear(gearSelected);
        //驱动方式
        localCarConfigModel.setDrivingMode(drivingModeSelected);
        //排气量
        localCarConfigModel.setDisplacement(displacementSelected);
        //出厂日期
        String smonth = month+"";
        if(month<10){
            smonth = "0"+month;
        }
        localCarConfigModel.setProductDate(year+"-"+smonth);
        //车辆铭牌
        localCarConfigModel.setNameplate(cetCarNamePlate.getText().toString());
        return  localCarConfigModel;
    }

    public void setLocalCarConfigModel(LocalCarConfigModel localCarConfigModel){
        this.localCarConfigModel = localCarConfigModel;
    }

    public void showLoadError(){
        viewFragReload.setVisibility(View.VISIBLE);
    }

    public void hideLoadError(){
        viewFragReload.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {
        if(!TextUtils.isEmpty(error))
            MyToast.showLong(error);
    }

    @Override
    public void showDialog() {
        showDialogTool.showLoadingDialog(this.getContext());
    }

    @Override
    public void dismissDialog() {
        showDialogTool.dismissLoadingDialog();
    }
}
