package com.jcpt.jzg.padsystem.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.AdmixedLeftAdapter;
import com.jcpt.jzg.padsystem.adapter.AdmixedRightAdapter;
import com.jcpt.jzg.padsystem.app.AppManager;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.dialog.CarStyleConfirmDialog;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.mvpview.IAdmixedInf;
import com.jcpt.jzg.padsystem.presenter.CarParamConfigPresenter;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.view.MyHorizontalItem;
import com.jcpt.jzg.padsystem.view.MySyncHorizontalScrollView;
import com.jcpt.jzg.padsystem.vo.AdmixedData;
import com.jcpt.jzg.padsystem.vo.NewStyle;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.ScrollBottomScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * author: guochen
 * date: 2016/11/1 15:13
 * modified by zealjiang 2016/12/11
 * 参配列表
 */
public class AdmixedContrastActivity extends BaseActivity implements IAdmixedInf,MyHorizontalItem.MyCloseOnClickListener, MyHorizontalItem.MyOkOnClickListener, View.OnClickListener {


    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.title_return)
    ImageView titleReturn;
    @BindView(R.id.tv_zhaopianbidui)
    CustomRippleButton crbPicCompare;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_hrz)
    LinearLayout llHrz;
    @BindView(R.id.mytable_scrollview)
    MySyncHorizontalScrollView mytableScrollview;
    @BindView(R.id.contentListViewLeft)
    XRecyclerView contentListViewLeft;
    @BindView(R.id.contentListViewRight)
    XRecyclerView contentListViewRight;
    @BindView(R.id.rightContentHorscrollView)
    MySyncHorizontalScrollView rightContentHorscrollView;
    @BindView(R.id.tv_show_all)
    TextView tvShowAll;
    @BindView(R.id.scrollBottomScrollView)
    ScrollBottomScrollView scrollBottomScrollView;

    private ArrayList<String> styleids;
    private AdmixedData admixedData;
    private String IsDiff = "0";//1差异项 0全部
    private String styleIdsParam;//请求的styleid用|分割
    private CarParamConfigPresenter carParamConfigPresenter;
    private AdmixedLeftAdapter admixedLeftAdapter;
    private AdmixedRightAdapter admixedRightAdapter;
    private List<AdmixedData.ShowDataBean> showDataList;
    private final int loadNum = 15;


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_admixed);
        ButterKnife.bind(this);
        carParamConfigPresenter = new CarParamConfigPresenter(this);
        Intent intent = getIntent();
        //请求的styleid用|分割
        styleids = intent.getStringArrayListExtra("styleIds");
        if (styleids == null || styleids.size() == 0) {
            Toast.makeText(this, "请选择车型", Toast.LENGTH_SHORT).show();
            return;
        }

        showDataList = new ArrayList<>();
        initView();
        startAlreadyListThread();

    }

    @Override
    protected void setData() {

    }


    private String FilterData() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < styleids.size(); i++) {
            sb.append(styleids.get(i) + "|");
        }
        sb.deleteCharAt(sb.length()-1);
        styleIdsParam = sb.toString();
        return styleIdsParam;
    }


    private void initView() {
        tvShowAll.setOnClickListener(this);
        crbPicCompare.setOnClickListener(this);
        OverScrollDecoratorHelper.setUpOverScroll(mytableScrollview);
        OverScrollDecoratorHelper.setUpOverScroll(rightContentHorscrollView);
        mytableScrollview.setmSyncView(rightContentHorscrollView);
        rightContentHorscrollView.setmSyncView(mytableScrollview);
        //syncScroll(contentListViewLeft,contentListViewRight);

    }

    private void initLeftRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contentListViewLeft.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contentListViewLeft.setLayoutManager(layoutManager);
        contentListViewLeft.setPullRefreshEnabled(true);
        contentListViewLeft.setLoadingMoreEnabled(true);

    }

    private void initRightRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contentListViewRight.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contentListViewRight.setLayoutManager(layoutManager);
        contentListViewRight.setPullRefreshEnabled(false);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        llHrz.removeAllViews();
                        for (int i = 0; i < admixedData.getCarData().size(); i++) {
                            MyHorizontalItem myHorizontalItem = new MyHorizontalItem(AdmixedContrastActivity.this);
                            myHorizontalItem.setPosition(i);
                            myHorizontalItem.setContent(admixedData.getCarData().get(i).getStyleName());
                            myHorizontalItem.setGuidePrice(admixedData.getCarData().get(i).getNowMsrp()+"万元");
                            llHrz.addView(myHorizontalItem);
                            myHorizontalItem.setMyCloseOnClickListener(AdmixedContrastActivity.this);
                            myHorizontalItem.setMyOkOnClickListener(AdmixedContrastActivity.this);
                        }

                        initRecyclerView();

                    }
                });
            }
        }).start();


    }

    private void initRecyclerView(){

        showDataList = admixedData.getShowData();
        initLeftRecyclerView();
        admixedLeftAdapter = new AdmixedLeftAdapter(AdmixedContrastActivity.this, showDataList);
        contentListViewLeft.setAdapter(admixedLeftAdapter);
        admixedLeftAdapter.notifyDataSetChanged();

        initRightRecyclerView();
        admixedRightAdapter = new AdmixedRightAdapter(AdmixedContrastActivity.this, showDataList,admixedData.getCarData().size());
        contentListViewRight.setAdapter(admixedRightAdapter);
        admixedRightAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCloseClickListener(View view, int position) {

        if(position<admixedData.getCarData().size()){
            if(admixedData.getCarData().size()==1){//最后一个不能关闭
                return;
            }
            //删除对应的车辆
            admixedData.getCarData().remove(position);
            //删除对应的view
            llHrz.removeViewAt(position);
            //重新设置车辆的position
            for (int i = 0; i < admixedData.getCarData().size(); i++) {
                ((MyHorizontalItem)llHrz.getChildAt(i)).setPosition(i);
            }

            //删除对应的图片
            for (int i = 0; i < admixedData.getPhotoData().size(); i++) {
                admixedData.getPhotoData().get(i).getPropertyValue().remove(position);
            }
            //删除对应的参数
            for (int i = 0; i < admixedData.getShowData().size(); i++) {
                if(admixedData.getShowData().get(i).getDataType()==1){
                    admixedData.getShowData().get(i).getPropertyValue().remove(position);
                }
            }
        }


        admixedLeftAdapter.notifyDataSetChanged();
        contentListViewLeft.scrollToPosition(0);
        admixedRightAdapter = new AdmixedRightAdapter(AdmixedContrastActivity.this, showDataList,admixedData.getCarData().size());
        contentListViewRight.setAdapter(admixedRightAdapter);

    }

    @Override
    public void onOkClickListener(View view, int position) {
        if (position < admixedData.getCarData().size()) {
            NewStyle newStyle = new NewStyle();
            newStyle.setName(admixedData.getCarData().get(position).getStyleName());
            newStyle.setNowMsrp(admixedData.getCarData().get(position).getNowMsrp());
            newStyle.setStyleId(admixedData.getCarData().get(position).getStyleID());

            String content = "您已选择" + newStyle.getName() + "款车型\n指导价" + newStyle.getNowMsrp() + "万,请确认这是否为您所需要的车型";
            CarStyleConfirmDialog carStyleConfirmDialog = new CarStyleConfirmDialog(AdmixedContrastActivity.this);
            carStyleConfirmDialog.setData(content,newStyle);
            carStyleConfirmDialog.createDialog();

        }
    }

    public void startAlreadyListThread() {
        if (TextUtils.isEmpty(FilterData())) {
            Toast.makeText(this, "请选择车型", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PadSysApp.networkAvailable){
            MyToast.showShort(getResources().getString(R.string.check_net));
            return;
        }
        carParamConfigPresenter.requestAdmixedData(styleIdsParam,IsDiff);
    }


    public void goBack(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_all:
                if ("显示全部参配".equals(tvShowAll.getText().toString().trim())) {
                    tvShowAll.setText("隐藏相同配置");
                    tvShowAll.setBackgroundColor(getResources().getColor(R.color.red));
                    //要显示全部配置
                    IsDiff = "0";
                    startAlreadyListThread();
                } else {
                    tvShowAll.setText("显示全部参配");
                    tvShowAll.setBackgroundColor(getResources().getColor(R.color.common_btn_blue));
                    IsDiff = "1";
                    startAlreadyListThread();
                }
                break;
            case R.id.tv_zhaopianbidui:
                if(!PadSysApp.networkAvailable){
                    MyToast.showShort(getResources().getString(R.string.check_net));
                    return;
                }
                if(admixedData==null){
                    MyToast.showShort("车辆数据不可为空");
                    return;
                }
                Intent intent = new Intent(this, CarPicComparedActivity.class);
                intent.putExtra(Constants.ADMIXEDCONTRASTACTIVITY, admixedData);
                startActivityForResult(intent, 1);
                break;

            default:

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (data != null) {
                    this.admixedData = (AdmixedData)data.getSerializableExtra("AdmixedData");
                    refresh();
                }
                break;
            default:

                break;
        }
    }

    private void refresh(){
        llHrz.removeAllViews();
        for (int i = 0; i < admixedData.getCarData().size(); i++) {
            MyHorizontalItem myHorizontalItem = new MyHorizontalItem(AdmixedContrastActivity.this);
            myHorizontalItem.setPosition(i);
            myHorizontalItem.setContent(admixedData.getCarData().get(i).getStyleName());
            myHorizontalItem.setGuidePrice(admixedData.getCarData().get(i).getNowMsrp()+"");
            llHrz.addView(myHorizontalItem);
            myHorizontalItem.setMyCloseOnClickListener(AdmixedContrastActivity.this);
            myHorizontalItem.setMyOkOnClickListener(AdmixedContrastActivity.this);
        }

        initRecyclerView();
    }

    @Override
    public void succeed(AdmixedData admixedData) {
        if(admixedData!=null){
            this.admixedData = admixedData;
            initData();
        }
    }
}
