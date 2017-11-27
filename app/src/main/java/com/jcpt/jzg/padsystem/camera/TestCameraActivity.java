package com.jcpt.jzg.padsystem.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.PreviewAdapter;
import com.jcpt.jzg.padsystem.adapter.ProvinceCityAdapter;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.view.PieView;
import com.jcpt.jzg.padsystem.view.QNumberPicker;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by zealjiang on 2016/9/21 09:35.
 * Email: zealjiang@126.com
 */
public class TestCameraActivity extends Activity{

    private RecyclerView rvPhoto;
    private final int PHOTO_REQUEST = 10;
    private final int PHOTO_BIG_PHOTO = 11;
    private ArrayList<PictureItem> pictureItems;
    private PreviewAdapter previewAdapter;
    private int curPos = 0;
    private PieView pView;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);
        rvPhoto = (RecyclerView) findViewById(R.id.rvPhoto);
        pView = (PieView) findViewById(R.id.pView);
        tvContent = (TextView) findViewById(R.id.tvContent);
        pView.setmData(new int[]{10,20,30,40,50});
        pView.setOnClickListener(new android.view.View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                showDataPicker();
            }
        });
        initData();
    }

    private QNumberPicker npYear;
    private QNumberPicker npMonth;
    private void showDataPicker(){
        View v = UIUtils.inflate(R.layout.layout_year_month_picker);
        npYear = (QNumberPicker) v.findViewById(R.id.npYear);
        npMonth = (QNumberPicker) v.findViewById(R.id.npMonth);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        npYear.setMaxValue(nowYear);
        npYear.setMinValue(nowYear-15);
        npMonth.setMaxValue(12);
        npMonth.setMinValue(1);
        npYear.setValue(nowYear);
        npMonth.setValue(nowMonth);
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setTitle("这里是标题").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int selectYear = npYear.getValue();
                int selectMonth = npMonth.getValue();
                tvContent.setText(selectYear+"-"+selectMonth);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void initData(){
        pictureItems = new ArrayList<>();
        pictureItems.add(new PictureItem("1","前排座椅","https://pbs.twimg.com/media/CziQ5MuUcAAk8hP.jpg")); //加载中
        pictureItems.add(new PictureItem("2","仪表盘",""));//没有图片直接显示底图
        pictureItems.add(new PictureItem("3","左后车门铰链","https://pbs.twimg.com/media/CziHjPzXEAAoxEh.jpg"));//加载中
        pictureItems.add(new PictureItem("4","左侧底大边",""));//没有图片直接显示底图
        pictureItems.add(new PictureItem("5","左前45°","https://pbs.twimg.com/media/CziE3w_XUAASqY6.jpg"));//加载中
        pictureItems.add(new PictureItem("6","其他1","http://www.pratt-whitney.com/Content/F117_Engine/img/aaa.jpg"));//图片不存在，加载失败
        pictureItems.add(new PictureItem("7","其他2","https://pbs.twimg.com/media/Czh3JJ3UQAAh95U.jpg"));//加载中
        pictureItems.add(new PictureItem("8","其他3",""));//没有图片直接显示底图
        pictureItems.add(new PictureItem("9","其他4","http://qiniu.tangu.la/assets/img/foundersc_ad.png"));
        previewAdapter = new PreviewAdapter(this,pictureItems);
        rvPhoto.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        rvPhoto.setLayoutManager(layoutManager);
        rvPhoto.setAdapter(previewAdapter);


        previewAdapter.setOnItemClickListener(new ProvinceCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                curPos = position;
                String picPath = pictureItems.get(position).getPicPath();
                if(TextUtils.isEmpty(picPath)){
                    userCamera(curPos,Constants.CAPTURE_TYPE_MULTI);
                }else{
                    Intent intent = new Intent(TestCameraActivity.this,PictureZoomActivity.class);
                    intent.putExtra("url",picPath);
                    intent.putExtra("showRecapture",true);
                    startActivityForResult(intent,PHOTO_BIG_PHOTO);
                }
            }
        });
    }


    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position,int capture_type){
        Intent intent = new Intent(this,CameraActivity.class);
        intent.putExtra("showGallery",true);//是否显示从相册选取的图标
        intent.putExtra("taskId","618");
        intent.putExtra("position",position);
        intent.putExtra(Constants.CAPTURE_TYPE,capture_type);//拍摄模式，是单拍还是连拍
        if(capture_type==Constants.CAPTURE_TYPE_MULTI){
            intent.putExtra("picList",pictureItems);
        }else{
            ArrayList<PictureItem> singleList = new ArrayList<>();
            singleList.add(pictureItems.get(position));
            intent.putExtra("picList",singleList);
        }
        startActivityForResult(intent,PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK)
            return;
        if(requestCode==PHOTO_REQUEST){
            if(null != data){
                int captureType = data.getIntExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_MULTI);
                ArrayList<PictureItem> picList = (ArrayList<PictureItem>) data.getSerializableExtra("picList");
                if(picList!=null && picList.size()>0){
                    if(captureType==Constants.CAPTURE_TYPE_SINGLE){
                        pictureItems.get(curPos).setPicPath(picList.get(0).getPicPath());
                        previewAdapter.notifyItemChanged(curPos);
                    }else{
                        pictureItems.clear();
                        pictureItems.addAll(picList);
                    }
                    previewAdapter.notifyDataSetChanged();

                }
            }
        }else if(requestCode==PHOTO_BIG_PHOTO){
            if(data!=null){
                boolean recapture = data.getBooleanExtra("recapture", false);
                if(recapture){
                    FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPos).getPicPath(),true);
                    pictureItems.get(curPos).setPicPath("");
                    userCamera(curPos,Constants.CAPTURE_TYPE_SINGLE);
                }
            }
        }
    }
}
