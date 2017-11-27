package com.jcpt.jzg.padsystem.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.SeriesListAdapter;
import com.jcpt.jzg.padsystem.mvpview.ICarTypeSelectInterface;
import com.jcpt.jzg.padsystem.presenter.CarSeriesSelectPresenter;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.ShowDialogTool;
import com.jcpt.jzg.padsystem.vo.BrandList;
import com.jcpt.jzg.padsystem.vo.SeriesList;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by zealjiang on 2016/11/1 16:18.
 * Email: zealjiang@126.com
 */

public class SeriesSelectView extends LinearLayout implements ICarTypeSelectInterface {

    @BindView(R.id.series_list)
    ListView seriesList;
    @BindView(R.id.viewFragReload)
    RelativeLayout viewFragReload;
    @BindView(R.id.crb_reload)
    CustomRippleButton crbReload;

    private LinearLayout modelHearderLayout;
    private SeriesListAdapter seriesListAdapter;
    //车系列表数据
    private ArrayList<SeriesList.MemberValueBean.ModelGroupListBean> modelGroupListBeen;
    //车系列表分组数据
    private ArrayList<String> modelsGroupNames;

    private CarSeriesSelectPresenter carSeriesSelectPresenter;
    private String brandName = ""; //品牌名字
    private int brandId;
    private String brandLogo = "";
    /**当前选中的品牌下的某款车系 by zj*/
    private SeriesList.MemberValueBean.ModelGroupListBean curSerie;
    private SeriesSelectView.OnSeriesItemClickListener onSeriesItemClickListener;
    private ShowDialogTool showDialogTool;

    public SeriesSelectView(Context context) {
        super(context);
        initView();
    }

    public SeriesSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SeriesSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        showDialogTool = new ShowDialogTool();
        View.inflate(getContext(), R.layout.widget_series_list, this);
        ButterKnife.bind(this);

        modelHearderLayout = (LinearLayout) LayoutInflater.from(this.getContext().getApplicationContext()).inflate(R.layout.model_header_view, null);
        carSeriesSelectPresenter = new CarSeriesSelectPresenter(this);
    }

    /**
     * 根据品牌数据请求车系
     * @author zealjiang
     * @time 2016/11/1 20:52
     */
    public void requestSeries(int brandId,String brandName,String brandLogo) {

        this.brandId = brandId;
        this.brandName = brandName;
        this.brandLogo = brandLogo;

        carSeriesSelectPresenter.getSeriesList(brandId);//请求车系
    }

    /**
     * 设置ListView头部信息
     */
    private void setHeader() {
        SimpleDraweeView img = (SimpleDraweeView) modelHearderLayout.findViewById(R.id.header_img);
        TextView text = (TextView) modelHearderLayout.findViewById(R.id.header_text);
        Uri uri = Uri.parse(brandLogo);
        img.setImageURI(uri);
        text.setText(brandName);
    }

    public void showLoadError(){
        viewFragReload.setVisibility(View.VISIBLE);
    }

    public void hideLoadError(){
        viewFragReload.setVisibility(View.GONE);
    }

    @OnClick(R.id.crb_reload)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.crb_reload:

                break;
        }
    }

    @OnItemClick(R.id.series_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        //如果点击位置不是车系的头部位置则进行跳转，并把当前的品牌数据和车系数据发送到车型界面去
        if (position != 0) {
            curSerie = modelGroupListBeen.get(position - 1);
            seriesListAdapter.setSelectedPosition(position-1);
            if(null!=onSeriesItemClickListener){
                onSeriesItemClickListener.onSeriesItemClick(curSerie.getId(),curSerie.getName(),curSerie.getModelimgpath());
            }
            seriesListAdapter.notifyDataSetChanged();
        }
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

    @Override
    public void showBrand(ArrayList<Map<String, Object>> items) {
    }

    @Override
    public void showSeries() {
        if(seriesList.getHeaderViewsCount()>0){
            seriesList.removeHeaderView(modelHearderLayout);
        }

        seriesListAdapter = new SeriesListAdapter(this.getContext(), modelGroupListBeen, modelsGroupNames);
        setHeader();
        seriesList.addHeaderView(modelHearderLayout);
        seriesList.setAdapter(seriesListAdapter);

        //初始化默认选中第一个车系
        int seriesId = modelGroupListBeen.get(1).getId();
        String seriesName = modelGroupListBeen.get(1).getName();
        String seriesLogo = modelGroupListBeen.get(1).getModelimgpath();

        if(null!=onSeriesItemClickListener){
            onSeriesItemClickListener.onSeriesItemClick(seriesId,seriesName,seriesLogo);
        }

        seriesListAdapter.setSelectedPosition(1);
        seriesListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getDefaultFontColor() {
        return R.color.common_text_gray_dark;
    }

    @Override
    public void setIndexData(Map<String, Integer> indexData) {
    }

    @Override
    public void setBrands(ArrayList<BrandList.MemberValueBean> makes) {
    }

    @Override
    public void setSeries(ArrayList<SeriesList.MemberValueBean.ModelGroupListBean> modelGroupListBeen) {
        this.modelGroupListBeen = modelGroupListBeen;
    }

    @Override
    public void setSeriesGroupNames(ArrayList<String> seriesGroupNames) {
        this.modelsGroupNames = seriesGroupNames;
    }


    @Override
    public boolean readFromCache(BrandList makeList) {
        return false;
    }

    public interface OnSeriesItemClickListener{
        public void onSeriesItemClick(int seriesId,String seriesName,String seriesLogo);
    }

    public void setOnSeriesItemClickListener(SeriesSelectView.OnSeriesItemClickListener onSeriesItemClickListener){
        this.onSeriesItemClickListener = onSeriesItemClickListener;
    }
}
