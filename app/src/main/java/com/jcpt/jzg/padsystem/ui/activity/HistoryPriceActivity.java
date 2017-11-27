package com.jcpt.jzg.padsystem.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.HistoryPriceAdapter;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.mvpview.IHistoryPrice;
import com.jcpt.jzg.padsystem.presenter.HistoryPricePresenter;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.HistoryPriceModel;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryPriceActivity extends BaseActivity implements IHistoryPrice{

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.crbClose)
    CustomRippleButton crbClose;

    private HistoryPriceActivity self;
    private HistoryPriceAdapter adapterHistoryPrice;
    private HistoryPricePresenter historyPricePresenter;
    private List<HistoryPriceModel> listHistoryPrice;
    private String vin;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history_price);
        ButterKnife.bind(this);
        self = this;
        vin = getIntent().getStringExtra("vin");

        listHistoryPrice = new ArrayList<>();
        historyPricePresenter = new HistoryPricePresenter(this);
    }

    @Override
    protected void setData() {
        setTitle("历史评估价格");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        //添加分割线
        //rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapterHistoryPrice = new HistoryPriceAdapter(this,listHistoryPrice);
        rv.setAdapter(adapterHistoryPrice);

        if(TextUtils.isEmpty(vin)){
            MyToast.showShort("VIN有误");
            return;
        }
        //网络请求
        historyPricePresenter.getHistoryPrice(vin);
    }

    @OnClick({R.id.ivLeft,R.id.crbClose})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ivLeft://返回
                finish();
                break;
            case R.id.crbClose://关闭
                finish();
                break;
        }
    }

    @Override
    public void succeedDetail(List<HistoryPriceModel> listData) {
        listHistoryPrice.clear();
        listHistoryPrice.addAll(listData);
        adapterHistoryPrice.notifyDataSetChanged();
    }

    @Override
    public void succeedCount(int count) {

    }
}
