package com.jcpt.jzg.padsystem.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.BrandListAdapter;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.mvpview.ICarTypeSelectInterface;
import com.jcpt.jzg.padsystem.presenter.CarSeriesSelectPresenter;
import com.jcpt.jzg.padsystem.utils.ACache;
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

import static com.jcpt.jzg.padsystem.R.id.brand_list;

/**
 * 品牌列表
 * Created by zealjiang on 2016/11/1 16:18.
 * Email: zealjiang@126.com
 */

public class BrandSelectView extends LinearLayout implements ICarTypeSelectInterface {

    private final String TAG = "BrandSelectView";
    @BindView(brand_list)
    ListView brandList;
    @BindView(R.id.index_list)
    MyLetterListView indexListView;
    LinearLayout makeHeaderLayout;

    @BindView(R.id.viewFragReload)
    RelativeLayout viewFragReload;
    @BindView(R.id.crb_reload)
    CustomRippleButton crbReload;
    @BindView(R.id.tvLoadError)
    TextView tvLoadError;

    private CarSeriesSelectPresenter carSeriesSelectPresenter;
    private String json;
    //品牌列表分组数据
    private ArrayList<Map<String, Object>> brandItems;
    //abcd索引需要的数据
    private Map<String, Integer> indexData;
    //品牌列表数据
    private ArrayList<BrandList.MemberValueBean> makes;
    private String makeName = ""; //品牌名字
    //当前选中的品牌
    private BrandList.MemberValueBean curMake;
    private BrandListAdapter brandListAdapter;
    private OnBrandItemClickListener onBrandItemClickListener;
    private ShowDialogTool showDialogTool;

    public BrandSelectView(Context context) {
        super(context);
        initView();
    }

    public BrandSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BrandSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        showDialogTool = new ShowDialogTool();
        View.inflate(getContext(), R.layout.widget_brand_list, this);
        ButterKnife.bind(this);
/*
        //显示全部
        makeHeaderLayout = (LinearLayout) LayoutInflater.from(this.getContext()).inflate(R.layout.make_header_view, null);
        brandList.addHeaderView(makeHeaderLayout);*/
        indexListView.setOnTouchingLetterChangedListener(new LetterListViewListener());

    }

    /**
     * 清空重选
     * @author zealjiang
     * @time 2017/1/20 17:27
     */
    public void init() {

        //从缓存得到数据
        json = ACache.get(this.getContext().getApplicationContext()).getAsString(Constants.KEY_CARBRANDLIST);

        carSeriesSelectPresenter = new CarSeriesSelectPresenter(this);
        carSeriesSelectPresenter.getBrandList();//请求品牌
        if (!TextUtils.isEmpty(json)) {
            BrandList list = new Gson().fromJson(json, BrandList.class);//从本地缓存中度数据
            carSeriesSelectPresenter.showBrand((ArrayList<BrandList.MemberValueBean>)list.getMemberValue());
            dismissDialog();
        }else{
            //没有网络
            showLoadError();
            crbReload.setVisibility(View.VISIBLE);
            tvLoadError.setVisibility(View.GONE);
        }
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
                carSeriesSelectPresenter.getBrandList();//请求品牌
                break;
        }
    }

    @OnItemClick(R.id.brand_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        //不带全部选项的流程
        curMake = makes.get(position);
        //设置是否带有全部品牌选项标记，用于广播接受的时候判断
        makeName = curMake.getMakeName();

        if(null!=onBrandItemClickListener){
            onBrandItemClickListener.onBrandItemClick(curMake.getMakeId(),makeName,curMake.getMakeLogo());
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
        hideLoadError();
        brandItems = items;
        brandListAdapter = new BrandListAdapter(this.getContext(), brandItems);
        brandList.setAdapter(brandListAdapter);

        //初始化默认加载第一个品牌的车系
        int brandId = makes.get(0).getMakeId();
        String brandName = makes.get(0).getMakeName();
        String brandLogo = makes.get(0).getMakeLogo();

        if(null!=onBrandItemClickListener){
            onBrandItemClickListener.onBrandItemClick(brandId,brandName,brandLogo);
        }

    }

    @Override
    public void showSeries() {

    }

    @Override
    public int getDefaultFontColor() {
        return R.color.common_text_gray_dark;
    }

    @Override
    public void setIndexData(Map<String, Integer> indexData) {
        this.indexData = indexData;
    }

    @Override
    public void setBrands(ArrayList<BrandList.MemberValueBean> makes) {
        this.makes = makes;
    }

    @Override
    public void setSeries(ArrayList<SeriesList.MemberValueBean.ModelGroupListBean> modelGroupListBeen) {

    }

    @Override
    public void setSeriesGroupNames(ArrayList<String> seriesGroupNames) {

    }

    @Override
    public boolean readFromCache(BrandList makeList) {
        Gson gson = new Gson();
        String json = ACache.get(this.getContext().getApplicationContext()).getAsString(Constants.KEY_CARBRANDLIST);//从缓存得到数据
        String json1 = gson.toJson(makeList);//网路数据

        if (!TextUtils.isEmpty(json)) {//如果缓存不为空
            if (json.equals(json1)) {  //如果缓存和网路数据一致
                return true;
            } else {
                ACache.get(this.getContext().getApplicationContext()).put(Constants.KEY_CARBRANDLIST, json1);
                return false;
            }
        } else {
            ACache.get(this.getContext().getApplicationContext()).put(Constants.KEY_CARBRANDLIST, json1);
            return false;
        }
    }

    class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            if (indexData != null && indexData.get(s) != null) {
                int position = indexData.get(s);
                brandList.setSelection(position);
            }
        }
    }

    public interface OnBrandItemClickListener{
        public void onBrandItemClick(int brandId,String brandName,String brandLogo);
    }

    public void setOnBrandItemClickListener(OnBrandItemClickListener onBrandItemClickListener){
        this.onBrandItemClickListener = onBrandItemClickListener;
    }
}
