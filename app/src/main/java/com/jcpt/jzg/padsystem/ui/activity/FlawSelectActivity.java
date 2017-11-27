package com.jcpt.jzg.padsystem.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.FlawAdapter;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.FlawItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郑有权 on 2016/11/10.
 */

public class FlawSelectActivity extends BaseActivity {

    @BindView(R.id.rvFlaws)
    RecyclerView rvFlaws;

    FlawAdapter flawAdapter;

    public List<FlawItem> datas;

    int curitemPositon,curtagPositon,curposition;

    private String curLocPhotoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flaw_select);
        ButterKnife.bind(this);


        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        Point size = new Point();
        d.getSize(size);

        int win_width = size.x;
        int win_height = size.y;
        p.height = (int) (win_height * 1); // 高度设置为屏幕的0.6
        p.width = (int) (win_width * 0.6); // 宽度设置为屏幕的0.95
        p.alpha = 1.0f;// 设置透明度
        p.dimAmount = 0.5f; // 设置背景透明度，1完成不透明。
        p.gravity = Gravity.RIGHT;

        this.getWindow().setWindowAnimations(R.style.AnimRight);
        this.getWindow().setAttributes(p);
        this.setFinishOnTouchOutside(false);

        initView();

    }

    public void setda(){
        FlawItem flawItem = new FlawItem();
        flawItem.setDefectTypeId("F01");
        flawItem.setDefectTypeName("表面缺陷");
        List<FlawItem.DefectDetailListBean> lists = new ArrayList<>();
        FlawItem.DefectDetailListBean DefectDetailListBean =  new FlawItem.DefectDetailListBean();
        DefectDetailListBean.setDefectId("D001");
        DefectDetailListBean.setDefectName("轻微变形");
        DefectDetailListBean.setPicDefectId("L05_P08_A003_F01_D001");
        List<FlawItem.DefectDetailListBean.PicDefectIdListBean>  PicDefectIdListBeanLists = new ArrayList<>();
        FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
        PicDefectIdListBean.setPicDefectIdPer("http://img6.cache.netease.com/photo/0008/2014-10-11/A89QVJ9V29540008.jpg");
        FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean1 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
        PicDefectIdListBean1.setPicDefectIdPer("");
        FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean2 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
        PicDefectIdListBean2.setPicDefectIdPer("");
        PicDefectIdListBeanLists.add(PicDefectIdListBean);
        PicDefectIdListBeanLists.add(PicDefectIdListBean1);
        PicDefectIdListBeanLists.add(PicDefectIdListBean2);
        DefectDetailListBean.setPicDefectIdList(PicDefectIdListBeanLists);

        FlawItem.DefectDetailListBean DefectDetailListBean1 =  new FlawItem.DefectDetailListBean();
        DefectDetailListBean1.setDefectId("D002");
        DefectDetailListBean1.setDefectName("锈蚀");
        DefectDetailListBean1.setPicDefectId("L05_P08_A003_F01_D002");
        List<FlawItem.DefectDetailListBean.PicDefectIdListBean>  PicDefectIdListBeanLists1 = new ArrayList<>();
        FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean3 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
        PicDefectIdListBean3.setPicDefectIdPer("");
        PicDefectIdListBeanLists1.add(PicDefectIdListBean3);
        FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean4 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
        PicDefectIdListBean4.setPicDefectIdPer("");
        PicDefectIdListBeanLists1.add(PicDefectIdListBean4);
        FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean5 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
        PicDefectIdListBean5.setPicDefectIdPer("");
        PicDefectIdListBeanLists1.add(PicDefectIdListBean5);
        DefectDetailListBean1.setPicDefectIdList(PicDefectIdListBeanLists1);
        lists.add(DefectDetailListBean);
        lists.add(DefectDetailListBean1);
        List<String> defectIdList = new ArrayList<>();
        defectIdList.add("D001");
        flawItem.setDefectIdList(defectIdList);
        flawItem.setDefectDetailList(lists);
        datas.add(flawItem);
    }

    @Override
    protected void setData() {
        datas = new ArrayList<>();
        setda();

        for(int i=0;i<10;i++){

        FlawItem flawItem = new FlawItem();
        flawItem.setDefectTypeId("F01");
        flawItem.setDefectTypeName("修复缺陷");
            List<FlawItem.DefectDetailListBean> lists = new ArrayList<>();
            FlawItem.DefectDetailListBean DefectDetailListBean =  new FlawItem.DefectDetailListBean();
            DefectDetailListBean.setDefectId("D001");
            DefectDetailListBean.setDefectName("烧焊");
            DefectDetailListBean.setPicDefectId("L05_P08_A003_F01_D001");
            List<FlawItem.DefectDetailListBean.PicDefectIdListBean>  PicDefectIdListBeanLists = new ArrayList<>();
            FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
            PicDefectIdListBean.setPicDefectIdPer("http://img6.cache.netease.com/photo/0008/2014-10-11/A89QVJ9V29540008.jpg");
            FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean1 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
            PicDefectIdListBean1.setPicDefectIdPer("http://img6.cache.netease.com/photo/0008/2014-10-11/A89QVJ9V29540008.jpg");
            FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean2 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
            PicDefectIdListBean2.setPicDefectIdPer("");
            PicDefectIdListBeanLists.add(PicDefectIdListBean);
            PicDefectIdListBeanLists.add(PicDefectIdListBean1);
            PicDefectIdListBeanLists.add(PicDefectIdListBean2);
            DefectDetailListBean.setPicDefectIdList(PicDefectIdListBeanLists);

        FlawItem.DefectDetailListBean DefectDetailListBean1 =  new FlawItem.DefectDetailListBean();
        DefectDetailListBean1.setDefectId("D002");
        DefectDetailListBean1.setDefectName("切焊");
        DefectDetailListBean1.setPicDefectId("L05_P08_A003_F01_D002");
            List<FlawItem.DefectDetailListBean.PicDefectIdListBean>  PicDefectIdListBeanLists1 = new ArrayList<>();
            FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean3 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
            PicDefectIdListBean3.setPicDefectIdPer("");
            PicDefectIdListBeanLists1.add(PicDefectIdListBean3);
            FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean4 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
            PicDefectIdListBean4.setPicDefectIdPer("");
            PicDefectIdListBeanLists1.add(PicDefectIdListBean4);
            FlawItem.DefectDetailListBean.PicDefectIdListBean PicDefectIdListBean5 = new FlawItem.DefectDetailListBean.PicDefectIdListBean();
            PicDefectIdListBean5.setPicDefectIdPer("");
            PicDefectIdListBeanLists1.add(PicDefectIdListBean5);
            DefectDetailListBean1.setPicDefectIdList(PicDefectIdListBeanLists1);
        lists.add(DefectDetailListBean);
        lists.add(DefectDetailListBean1);
            List<String> defectIdList = new ArrayList<>();
            defectIdList.add("D001");
            flawItem.setDefectIdList(defectIdList);
        flawItem.setDefectDetailList(lists);








        datas.add(flawItem);

        }

    }

    public void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFlaws.setLayoutManager(linearLayoutManager);
        flawAdapter = new FlawAdapter(this,datas);
        rvFlaws.setAdapter(flawAdapter);
        flawAdapter.setmIchoosePhotoLister(new FlawAdapter.IchoosePhotoLister() {
            @Override
            public void show(int itemPositon, int tagPositon, int position) {
                System.out.println("setmIchoosePhotoLister"+itemPositon+"----"+tagPositon+"----"+position);
                curitemPositon = itemPositon;
                curtagPositon = tagPositon;
                curposition = position;

            }
        });
        flawAdapter.setmIchooseTagLister(new FlawAdapter.IchooseTagLister() {
            @Override
            public void show(int itemPositon, int tagPositon, String tagId, boolean isShow) {
                MyToast.showLong("item--"+itemPositon+"tagPositon=="+tagPositon+
                "tagId=="+tagId +"isShow==="+isShow);
                if(isShow){
                    datas.get(itemPositon).getDefectIdList().add(tagId);
                }else{
                    datas.get(itemPositon).getDefectIdList().remove(tagId);
                }

            }

        });

        flawAdapter.setmIchooseTagPhotoPathLister(new FlawAdapter.IchooseTagPhotoPathLister() {
            @Override
            public void show(String photoPath) {
                curLocPhotoPath = photoPath;
            }
        });

    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @OnClick({R.id.btnSave})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                finish();
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            System.out.println("onActivityResult");

            datas.get(curitemPositon).getDefectDetailList().get(curtagPositon)
                    .getPicDefectIdList().get(curposition)
                    .setPicDefectIdPer("file://"+curLocPhotoPath);

            flawAdapter.setmDatas(datas);
            flawAdapter.notifyItemChanged(curitemPositon);
            if (data!= null){ //防止没有返回结果
                Uri uri =data.getData();
                if (uri != null) {
                    MyToast.showLong("图片地址"+uri.getPath());
                     Bitmap photo = BitmapFactory.decodeFile(uri.getPath()); //拿到图片

                }
            }

            }
    }






}
