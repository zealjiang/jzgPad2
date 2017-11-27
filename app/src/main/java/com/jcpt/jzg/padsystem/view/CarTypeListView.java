package com.jcpt.jzg.padsystem.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.CarTypeListAdapter;
import com.jcpt.jzg.padsystem.dialog.CarStyleConfirmDialog;
import com.jcpt.jzg.padsystem.mvpview.ICarDiffInterface;
import com.jcpt.jzg.padsystem.presenter.CarTypeSelectPresenter;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.ShowDialogTool;
import com.jcpt.jzg.padsystem.vo.CarTypeSelectModel;
import com.jcpt.jzg.padsystem.vo.EventModel;
import com.jcpt.jzg.padsystem.vo.LocalCarConfigModel;
import com.jcpt.jzg.padsystem.vo.NewStyle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 车型选择--备选款型
 * Created by zealjiang on 2016/11/4 19:30.
 * Email: zealjiang@126.com
 */

public class CarTypeListView extends LinearLayout implements ICarDiffInterface {

    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tvHint)
    TextView tvHint;

    private com.jcpt.jzg.padsystem.presenter.CarTypeSelectPresenter carTypeSelectPresenter;
    private CarTypeListAdapter carTypeListAdapter;
    private List<CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean> datas;//包含所有数据
    private List<CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean> showDatas;//根据配置项变化的显示数据
    private CarTypeListView.OnListDataLoadOverListener onListDataLoadOverListener;
    private DetectMainActivity detectMainActivity;
    private Context context;
    private int selectedPosition = -1;
    private ShowDialogTool showDialogTool;
    private String ActivityId;//服务器回传用

    public CarTypeListView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CarTypeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CarTypeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void setDetectMainActivity(DetectMainActivity detectMainActivity){
        this.detectMainActivity = detectMainActivity;
        carTypeListAdapter.setDetectMainActivity(detectMainActivity);
    }

    private void initView() {
        showDialogTool = new ShowDialogTool();
        View.inflate(getContext(), R.layout.widget_car_type_list, this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        datas = new ArrayList<>();
        showDatas = new ArrayList<>();
        rvType.setLayoutManager(new LinearLayoutManager(this.getContext()));
        carTypeListAdapter = new CarTypeListAdapter(showDatas);
        rvType.setAdapter(carTypeListAdapter);
        carTypeListAdapter.setOnItemClickListener(new CarTypeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {

                NewStyle newStyle = new NewStyle();
                newStyle.setMakeID(showDatas.get(pos).getMakeID());
                newStyle.setMakeName(showDatas.get(pos).getMakeName());
                newStyle.setModelID(showDatas.get(pos).getModelID());
                newStyle.setModelName(showDatas.get(pos).getModelName());
                newStyle.setStyleId(showDatas.get(pos).getStyleId());
                newStyle.setName(showDatas.get(pos).getName());
                newStyle.setNowMsrp(showDatas.get(pos).getNowMsrp());
                newStyle.setYear(showDatas.get(pos).getYear()+"");
                newStyle.setActivityId(ActivityId);

                String content = "您已选择" + newStyle.getName() + "款车型\n指导价" + newStyle.getNowMsrp() + "万,请确认这是否为您所需要的车型";
                CarStyleConfirmDialog carStyleConfirmDialog = new CarStyleConfirmDialog(detectMainActivity);
                carStyleConfirmDialog.setData(content,newStyle);
                carStyleConfirmDialog.createDialog();
            }
        });

        carTypeSelectPresenter = new CarTypeSelectPresenter(this);
    }

    /**
     * 从车型确认处传过来的车型信息
     * @param newStyle
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NewStyle newStyle) {
        if (newStyle == null) {
            return;
        } else {
            if (newStyle.isCancel()) {
                return;
            }else{
                //记下选中的位置
                for (int i = 0; i < showDatas.size(); i++) {
                    if(showDatas.get(i).getStyleId()==newStyle.getStyleId()){
                        selectedPosition = i;
                        break;
                    }
                }
                if(selectedPosition<0){
                    return;
                }
                carTypeListAdapter.resetClicks();
                if(carTypeListAdapter.getIsClicks()!=null){
                    carTypeListAdapter.getIsClicks().set(selectedPosition,true);
                }
                carTypeListAdapter.notifyDataSetChanged();

                //发送到CarSelectSubFragment即车型选择
                EventModel eventModel = new EventModel();
                eventModel.setName("selectedPos");
                eventModel.setValue(selectedPosition+"");
                EventBus.getDefault().post(eventModel);

                rvType.scrollToPosition(selectedPosition);
            }
        }
    }

    /**
     * 请求差异配置
     * @author zealjiang
     * @time 2016/11/9 12:01
     */
    @Deprecated
    public void requestCarDiff(String modelid,String vin,String pl,String bsx,String qdfs,String productYear,String nameplate) {
        carTypeSelectPresenter.requestCarDiff(modelid,vin,pl,bsx,qdfs,productYear,nameplate);
    }

    /**
     * 请求差异配置
     * @author zealjiang
     * @time 2017/4/20 14:07
     */
    public void requestCarDiff(String vin,String productYear,String nameplate) {
        carTypeSelectPresenter.requestCarDiff0417(vin,productYear,nameplate);
    }

    /**
     * 隐藏查询为空的提示
     */
    public void hideHint(){
        tvHint.setVisibility(View.GONE);
    }

    @Override
    public void succeed(CarTypeSelectModel carTypeSelectModel) {
        datas.clear();
        showDatas.clear();
        if(carTypeSelectModel.getMemberValue()==null||carTypeSelectModel.getMemberValue().getList()==null){
            carTypeListAdapter.resetClicks();
            tvTitle.setText("备选款型（剩余"+showDatas.size()+"）");
            //刷新数据
            carTypeListAdapter.notifyDataSetChanged();

            if(onListDataLoadOverListener!=null){
                onListDataLoadOverListener.onListDataLoadOver(null,"");
            }

            tvHint.setVisibility(View.VISIBLE);
            return;
        }

        ActivityId = carTypeSelectModel.getMemberValue().getActivityId();

        tvHint.setVisibility(View.GONE);

        //重新组装数据
        List<CarTypeSelectModel.MemberValueBean.ListBeanX> listBeanX = carTypeSelectModel.getMemberValue().getList();
        for (int i = 0; i < listBeanX.size(); i++) {
            CarTypeSelectModel.MemberValueBean.ListBeanX beanX = listBeanX.get(i);
            List<CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean> listDataBeen =  beanX.getList();
            if(listDataBeen.size()==0){
                continue;
            }
            for (int j = 0; j < listDataBeen.size(); j++) {
                CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean dataBean = listDataBeen.get(j);
                if(j==0){
                    dataBean.setIsTitle(true);
                    dataBean.setYear(beanX.getYear()+"");
                }else{
                    dataBean.setIsTitle(false);
                    dataBean.setYear(beanX.getYear()+"");
                }
                datas.add(dataBean);
            }
        }

        showDatas.addAll(datas);
        carTypeListAdapter.resetClicks();
        tvTitle.setText("备选款型（剩余"+showDatas.size()+"）");
        //刷新数据
        carTypeListAdapter.notifyDataSetChanged();

        if(onListDataLoadOverListener!=null){
            onListDataLoadOverListener.onListDataLoadOver(carTypeSelectModel.getMemberValue().getConfigList(),carTypeSelectModel.getMemberValue().getFullName());
        }
    }

    public void clearData(){
        datas.clear();
        showDatas.clear();
        tvTitle.setText("备选款型（剩余"+showDatas.size()+"）");
        //刷新数据
        carTypeListAdapter.notifyDataSetChanged();

    }


    /**
     * 根据选中的差异配置来刷新车型列表
     * @author zealjiang
     * @time 2016/11/10 16:54
     */
    public void getCarTypeListByDiff(ArrayList<String> selectedItemPos){
        showDatas.clear();
        //查询出包含差异项的车型
        for (int i = 0; i < datas.size(); i++) {
            String pos = datas.get(i).getPosString();

            String[] poss = pos.split(",");
            //判断此车型是否包含选中的差异配置
            String[] selectedPoss = selectedItemPos.toArray(new String[selectedItemPos.size()]);
            if(containArray(selectedPoss,poss)){
                showDatas.add(datas.get(i));
            }
        }

        tvTitle.setText("备选款型（剩余"+showDatas.size()+"）");
        carTypeListAdapter.resetClicks();
        //刷新数据
        carTypeListAdapter.notifyDataSetChanged();

        //根据筛选出的车型，重新列出差异配置
        if(onListDataLoadOverListener!=null){
            onListDataLoadOverListener.onFileterOutCar(showDatas);
        }
    }

    /**
     * 判断数组b是否包含数组a中的所有元素
     * @author zealjiang
     * @time 2016/11/10 18:19
     */
    public boolean containArray(String[] a, String[] b) {
        boolean flag = false;
        int k = 0;
        /**
         * 统计b中包含a中的元素是否与a的元素个数相同
         */
        if (a.length <= b.length) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < b.length; j++) {
                    if (a[i].equals(b[j])) {
                        k++;
                        continue;
                    }
                }
            }
        }
        if (k == a.length) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取选中的车型
     * @author zealjiang
     * @time 2016/11/11 20:38
     */
    public LocalCarConfigModel getSelectedStyle(){
        int position = carTypeListAdapter.getSelectedPosition();
        if(position==-1||showDatas.size()==0){
            return null;
        }
        CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean listDataBean = showDatas.get(position);
        LocalCarConfigModel localCarConfigModel = new LocalCarConfigModel();

        localCarConfigModel.setBrandId(listDataBean.getMakeID());
        localCarConfigModel.setBrandName(listDataBean.getMakeName());
        localCarConfigModel.setSerieId(listDataBean.getModelID());
        localCarConfigModel.setSerieName(listDataBean.getModelName());
        localCarConfigModel.setStyleId(listDataBean.getStyleId());
        localCarConfigModel.setStyleName(listDataBean.getName());
        localCarConfigModel.setYear(listDataBean.getYear());
        localCarConfigModel.setNowMsrp(listDataBean.getNowMsrp()+"");
        localCarConfigModel.setStyleFullName(listDataBean.getName());
        localCarConfigModel.setActivityId(ActivityId);

        return localCarConfigModel;
    }

    /**
     * 获取当前选出的所有款型车型ID
     * @author zealjiang
     * @time 2016/12/7 10:22
     * @return 如果当前选出的备选款型为Null,就返回null
     */
    public ArrayList<String> getShowStyleIdArray(){
        if(showDatas!=null&&showDatas.size()>0){
            ArrayList<String> styleIdArray = new ArrayList<>();
            for (int i = 0; i < showDatas.size(); i++) {
                styleIdArray.add(showDatas.get(i).getStyleId()+"");
            }
            return styleIdArray;
        }else{
            return null;
        }

    }


    @Override
    public void showError(String error) {
        if(!TextUtils.isEmpty(error))
            MyToast.showLong(error);

        datas.clear();
        showDatas.clear();
        //刷新数据
        carTypeListAdapter.notifyDataSetChanged();
        tvTitle.setText("备选款型（剩余"+showDatas.size()+"）");

        //更新差异配置
        //根据筛选出的车型，重新列出差异配置
        if(onListDataLoadOverListener!=null){
            onListDataLoadOverListener.onFileterOutCar(showDatas);
        }

        MyToast.showShort("查询出错,请点击右上方修改按钮选择车型");
    }

    @Override
    public void showDialog() {
        showDialogTool.showLoadingDialog(this.getContext());
    }

    @Override
    public void dismissDialog() {
        showDialogTool.dismissLoadingDialog();
    }

    public interface OnListDataLoadOverListener{
        public void onListDataLoadOver(List<CarTypeSelectModel.MemberValueBean.ConfigListBean> listData,String brandSeriesName);
        public void onFileterOutCar(List<CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean> filterCars);
    }

    public void setOnListDataLoadOverListener(CarTypeListView.OnListDataLoadOverListener onListDataLoadOverListener){
        this.onListDataLoadOverListener = onListDataLoadOverListener;
    }



}
