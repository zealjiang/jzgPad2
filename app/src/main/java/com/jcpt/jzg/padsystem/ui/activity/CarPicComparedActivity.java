package com.jcpt.jzg.padsystem.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.CarPicComparedLeftAdapter;
import com.jcpt.jzg.padsystem.adapter.CarPicComparedRightAdapter;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.dialog.CarStyleConfirmDialog;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.view.MyHorizontalItem;
import com.jcpt.jzg.padsystem.view.MyListView;
import com.jcpt.jzg.padsystem.view.MySyncHorizontalScrollView;
import com.jcpt.jzg.padsystem.vo.AdmixedData;
import com.jcpt.jzg.padsystem.vo.NewStyle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * Created by liugl on 2016/11/2.
 * modified by zealjiang 2016/12/11
 */

public class CarPicComparedActivity extends BaseActivity implements MyHorizontalItem.MyCloseOnClickListener, MyHorizontalItem.MyOkOnClickListener,CarPicComparedRightAdapter.CarPicClickInterface{

    @BindView(R.id.contentListViewLeft)
    MyListView contentListViewLeft;
    @BindView(R.id.rightContentHorscrollView)
    MySyncHorizontalScrollView rightContentHorscrollView;
    @BindView(R.id.mytable_scrollview)
    MySyncHorizontalScrollView mytableScrollview;
    @BindView(R.id.ll_hrz)
    LinearLayout llHrz;
    @BindView(R.id.contentListViewRight)
    MyListView contentListViewRight;

    private AdmixedData admixedData;
    ArrayList<String> styleids = new ArrayList<>();
    private CarPicComparedLeftAdapter admixedLeftAdapter;
    private CarPicComparedRightAdapter admixedRightAdapter;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_carpiccompared);
        ButterKnife.bind(this);
        admixedData = (AdmixedData) getIntent().getSerializableExtra(Constants.ADMIXEDCONTRASTACTIVITY);
        if (admixedData == null) {
            Toast.makeText(this, "请选择车型", Toast.LENGTH_SHORT).show();
            return;
        }
        initData();
        initViews();

    }

    @Override
    protected void setData() {

    }

    private void initData() {
        llHrz.removeAllViews();
        for (int i = 0; i < admixedData.getCarData().size(); i++) {
            MyHorizontalItem myHorizontalItem = new MyHorizontalItem(this);
            myHorizontalItem.setPosition(i);
            myHorizontalItem.setContent(admixedData.getCarData().get(i).getStyleName());
            myHorizontalItem.setGuidePrice(admixedData.getCarData().get(i).getNowMsrp()+"万元");
            llHrz.addView(myHorizontalItem);
            myHorizontalItem.setMyCloseOnClickListener(this);
            myHorizontalItem.setMyOkOnClickListener(this);
        }


        admixedLeftAdapter = new CarPicComparedLeftAdapter(this, admixedData);
        admixedRightAdapter = new CarPicComparedRightAdapter(this, admixedData,this);
        contentListViewLeft.setAdapter(admixedLeftAdapter);
        contentListViewRight.setAdapter(admixedRightAdapter);
    }

    private void initViews(){

        OverScrollDecoratorHelper.setUpOverScroll(mytableScrollview);
        OverScrollDecoratorHelper.setUpOverScroll(rightContentHorscrollView);
        mytableScrollview.setmSyncView(rightContentHorscrollView);
        rightContentHorscrollView.setmSyncView(mytableScrollview);

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
        admixedRightAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOkClickListener(View view, int position) {
        if(position<admixedData.getCarData().size()){
            NewStyle newStyle = new NewStyle();
            newStyle.setName(admixedData.getCarData().get(position).getStyleName());
            newStyle.setNowMsrp(admixedData.getCarData().get(position).getNowMsrp());
            newStyle.setStyleId(admixedData.getCarData().get(position).getStyleID());

            String content = "您已选择" + newStyle.getName() + "款车型\n指导价" + newStyle.getNowMsrp() + "万,请确认这是否为您所需要的车型";
            CarStyleConfirmDialog carStyleConfirmDialog = new CarStyleConfirmDialog(CarPicComparedActivity.this);
            carStyleConfirmDialog.setData(content,newStyle);
            carStyleConfirmDialog.createDialog();
        }
    }

    @Override
    public void onCarPicItemOnClick(int row, int colum) {
        if(row<admixedData.getPhotoData().size()
                &&colum<admixedData.getPhotoData().get(row).getPropertyValue().size()){
            ArrayList<String> paths = new ArrayList<>();
            Intent intent = new Intent(this, ViewPagerPhotoAty.class);
            Bundle b = new Bundle();
            if (admixedData.getPhotoData().get(row).getPropertyValue().get(colum).split("#")!=null && admixedData.getPhotoData().get(row).getPropertyValue().get(colum).split("#").length>1){
                paths.add(admixedData.getPhotoData().get(row).getPropertyValue().get(colum).split("#")[1]);
            }else{
                return;
            }
            b.putStringArrayList("imgLists", paths);
            b.putInt("itemPosition", 0);
            intent.putExtra("imgListsBundle", b);
            startActivity(intent);
        }

    }


    private void destroy(){
        if(admixedData==null||admixedData.getCarData()==null){
            finish();
            return;
        }
        styleids.clear();
        for(int i=0;i<admixedData.getCarData().size();i++){
            styleids.add(admixedData.getCarData().get(i).getStyleID()+"");
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra("styleids",styleids);
        intent.putExtra("AdmixedData",admixedData);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void back(View view) {
        destroy();
    }

    @Override
    public void onBackPressed() {
        destroy();
    }

}
