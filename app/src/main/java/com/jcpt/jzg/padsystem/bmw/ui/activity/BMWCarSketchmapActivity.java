package com.jcpt.jzg.padsystem.bmw.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.ICurIndexListener;
import com.jcpt.jzg.padsystem.vo.BMWCoordinateBean;
import com.jcpt.jzg.padsystem.vo.BmwTaskExModel;
import com.jcpt.jzg.padsystem.vo.BmwTaskExModel.DefectLegendListBean;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.widget.CustomMoveTextView;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by libo on 2017/11/6.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 宝马 外观示意图 （标点）
 */
public class BMWCarSketchmapActivity extends BaseActivity {


    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.btHollow)
    Button btHollow;
    @BindView(R.id.btScratch)
    Button btScratch;
    @BindView(R.id.btLose)
    Button btLose;
    @BindView(R.id.btExhausted)
    Button btExhausted;
    @BindView(R.id.btDamage)
    Button btDamage;
    @BindView(R.id.btSave)
    Button btSave;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.llRoot)
    LinearLayout llRoot;

    int ScreenWidth;
    int ScreenHeight;

    int currentIndex; //当前点击标的角标

    /**
     * Created by 李波 on 2017/11/2.
     * 标点大小
     */
    int CustomTextViewSize;

    /**
     * Created by 李波 on 2017/11/2.
     * 标点相应的marginLeft 和 marginTop值，使其处于屏幕中央
     */
    int screanLeftCenter;
    int screanTopCenter;

    MyUniversalDialog myUniversalDialog;

    private  final int SUCCESS = 0;
    private  final int FAIL =1;

    String type;//标点的类型 D - 凹陷....  -> 李波 on 2017/11/2.

    List<DefectLegendListBean> coordinateBeenList = new ArrayList<>();

    int num = 0;

    private String TaskId="12345";

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_car_sketchmap);
        ButterKnife.bind(this);

        String taskId = getIntent().getStringExtra("TaskId");

        if (TextUtils.isEmpty(taskId)) {
            finish();
        }else {
            TaskId = taskId;
        }
        ScreenWidth = ScreenUtils.getScreenWidth(this);
        ScreenHeight = ScreenUtils.getScreenHeight(this);

        CustomTextViewSize = getResources().getDimensionPixelSize(R.dimen.DIP_36_L);

        //注意：这里只是计算的纯屏幕的状态，没有上下左右占位控件，如果有 还得减去他们占位的距离  -> 李波 on 2017/11/7.
        screanLeftCenter = ScreenWidth/2 - (CustomTextViewSize/2);
        screanTopCenter  = ScreenHeight/2 - (CustomTextViewSize/2);

        llRoot.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @Override
    protected void setData() {

    }

    @OnClick({R.id.ivBack,R.id.btHollow,R.id.btScratch,R.id.btLose,R.id.btExhausted,R.id
            .btDamage,R.id.btSave})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:     //返回
                onBackPressed();
                break;
            case R.id.btHollow:   //凹陷
                type="D";
                spotMenu(null);
                break;
            case R.id.btScratch:  //划痕
                type="S";
                spotMenu(null);
                break;
            case R.id.btLose:     //丢失
                type="M";
                spotMenu(null);
                break;
            case R.id.btExhausted://耗尽
                type="W";
                spotMenu(null);
                break;
            case R.id.btDamage:   //损坏
                type="B";
                spotMenu(null);
                break;
            case R.id.btSave:     //保存
                saveData();
                break;
        }
    }

    /**
     * Created by 李波 on 2017/11/2.
     * 添加标点
     * @param value null 表示新增标点，!=null 表示回显标点
     * @param type  标点类型  如：D-凹陷
     * @param ascription 标点归属项  如：车轮，车灯.....
     */
    private void addSpot(String value,String type,String ascription,int marginLeft,int marginTop) {

        final CustomMoveTextView textView = new CustomMoveTextView(BMWCarSketchmapActivity.this);

        textView.setBackgroundResource(R.drawable.drawable_bg_red);
        textView.setTextColor(getResources().getColor(R.color.global_white));
        textView.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CustomTextViewSize,CustomTextViewSize);
        params.leftMargin = marginLeft;

        if (value==null) //新增
            params.topMargin = marginTop - rlTitle.getHeight(); //由于有标题栏占位，减去标题栏的高度  -> 李波 on 2017/11/7.
        else     //回显
            params.topMargin = marginTop;

        textView.setLayoutParams(params);

        int marginTopAndBottom = rlTitle.getHeight() + ll.getHeight();

        textView.setMaxTopAndMaxLeft(CustomTextViewSize,marginTopAndBottom);

        int childCount = rl.getChildCount(); //父控件里子控件的个数 即作为当前要添加控件的角标  -> 李波 on 2017/11/2.

        //新增
        if (value==null) {

            //当删除某些标点的情况下，再次新增标点时，遍历进行缺漏补位，保证标点是 按升序增长  -> 李波 on 2017/11/2.
            for (int i = 0; i < childCount; i++) {
                TextView tv = (TextView) rl.getChildAt(i);
                int context = Integer.valueOf(tv.getText().toString());
                if (i + 1 != context) {  //当前位置如果拿出来的值不符合角标+1，说明此角标对应的这项被删除过，进行补位  -> 李波 on 2017/11/2.
                    childCount = i;
                    break;
                }
            }

            String context = String.valueOf(childCount+1);
            textView.setText(context);
        }else //回显自带值
            textView.setText(value);

        textView.setType(type);
        textView.setAscriptionId(ascription);

        rl.addView(textView,childCount);

        textView.setICurIndexListener(new ICurIndexListener() {
            @Override
            public void curIndex(int curIndex) {
                currentIndex = curIndex;
            }

            @Override
            public void isdelCurIndex() { //拖动到指定区域删除标点，当前不用，直接点击标点执行删除操作  -> 李波 on 2017/11/8.

//                AlertDialog.Builder builder = new AlertDialog.Builder(BMWCarSketchmapActivity.this);
//                builder.setTitle("提示");
//                builder.setMessage("是否删除此标点数据");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (currentIndex < rl.getChildCount())
//                            rl.removeViewAt(currentIndex);
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spotMenu(textView);
            }
        });

    }


    /**
     * Created by 李波 on 2017/11/1.
     * 标点归属选项菜单
     * @param customTextView  null 表示新建时，!=null 表示点击标点时
     */
    private void spotMenu(final CustomMoveTextView customTextView){
        myUniversalDialog = new MyUniversalDialog(this);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_menu_spot);
        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        RadioButton rbCancle = view.findViewById(R.id.rbCancle);
        LinearLayout lldel = view.findViewById(R.id.lldel);
        Button btDel = view.findViewById(R.id.btDel);
        Button btCancle = view.findViewById(R.id.btCancle);

        if (customTextView!=null){ //点击标点时反显标点归属  -> 李波 on 2017/11/2.
            rbCancle.setVisibility(View.GONE);
            lldel.setVisibility(View.VISIBLE);
            btDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final MyUniversalDialog UniversalDialog = new MyUniversalDialog(BMWCarSketchmapActivity.this);
                    View viewdel = UniversalDialog.getLayoutView(R.layout.dialog_ok_or_cancle);
                    Button btOk =  viewdel.findViewById(R.id.btOK);
                    Button btCancle =  viewdel.findViewById(R.id.btnCancel);
                    TextView tvContent = viewdel.findViewById(R.id.tvContent);
                    tvContent.setText("是否确认删除此标点？");

                    btOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentIndex < rl.getChildCount())
                                rl.removeViewAt(currentIndex);
                            UniversalDialog.cancel();
                            myUniversalDialog.cancel();
                        }
                    });

                    btCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UniversalDialog.cancel();
                            myUniversalDialog.cancel();
                        }
                    });

                    UniversalDialog.setLayoutView(viewdel, 320, 230);
                    UniversalDialog.show();
                }
            });

            btCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myUniversalDialog.cancel();
                }
            });


            String part = customTextView.getAscriptionId();
            switch (part){
                case Constants.TYREID:
                    radioGroup.check(R.id.rbTyre);
                    break;
                case Constants.LAMPID:
                    radioGroup.check(R.id.rbLamp);
                    break;
                case Constants.GLASSID:
                    radioGroup.check(R.id.rbGlass);
                    break;
                case Constants.OTHERID:
                    radioGroup.check(R.id.rbOhter);
                    break;
            }
        }

        myUniversalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                myUniversalDialog = null;
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbTyre:   //轮胎轮毂
                        if (customTextView==null)
                            addSpot(null,type,Constants.TYREID,screanLeftCenter,screanTopCenter);
                        else
                            customTextView.setAscriptionId(Constants.TYREID);
                        break;
                    case R.id.rbLamp:  //车灯
                        if (customTextView==null)
                            addSpot(null,type,Constants.LAMPID,screanLeftCenter,screanTopCenter);
                        else
                            customTextView.setAscriptionId(Constants.LAMPID);
                        break;
                    case R.id.rbGlass:  //玻璃
                        if (customTextView==null)
                            addSpot(null,type,Constants.GLASSID,screanLeftCenter,screanTopCenter);
                        else
                            customTextView.setAscriptionId(Constants.GLASSID);
                        break;
                    case R.id.rbOhter:  //其他缺陷
                        if (customTextView==null)
                            addSpot(null,type,Constants.OTHERID,screanLeftCenter,screanTopCenter);
                        else
                            customTextView.setAscriptionId(Constants.OTHERID);
                        break;
                    case R.id.rbCancle:
                        break;
                }
                myUniversalDialog.cancel();
            }
        });

        myUniversalDialog.setLayoutView(view, 320, 0);
        myUniversalDialog.show();
    }



    @Override
    public void onBackPressed() {
        final MyUniversalDialog UniversalDialog = new MyUniversalDialog(this);
        View view = UniversalDialog.getLayoutView(R.layout.dialog_ok_or_cancle);
        Button btOk = (Button) view.findViewById(R.id.btOK);
        Button btCancle = (Button) view.findViewById(R.id.btnCancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UniversalDialog.cancel();
                finish();
            }
        });

        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UniversalDialog.cancel();
            }
        });

        UniversalDialog.setLayoutView(view, 320, 230);
        UniversalDialog.show();
    }


    /**
     * Created by 李波 on 2017/11/7.
     * 保存标点数据 并生成示意图
     */
    private void saveData() {
        num = 0;

        if (Build.VERSION.SDK_INT >= 23) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) {
                        saveImage(BMWCarSketchmapActivity.this,rl);
                    } else {
//                                MyToast.showShort("需要获取SD卡读取权限来保存图片");
                        Toast.makeText(BMWCarSketchmapActivity.this,"需要获取SD卡读取权限来保存图片",Toast
                                .LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            saveImage(BMWCarSketchmapActivity.this,rl);
        }


        //保存标点坐标到数据库  -> 李波 on 2017/11/1.
        coordinateBeenList.clear();

        for (int i = 0; i < rl.getChildCount(); i++) {
            CustomMoveTextView tv = (CustomMoveTextView) rl.getChildAt(i);

            DefectLegendListBean coordinateBean = new DefectLegendListBean();
            coordinateBean.setValue(tv.getText().toString());
            coordinateBean.setType(tv.getType());
            coordinateBean.setAscriptionId(tv.getAscriptionId());
            coordinateBean.setMarginLeft(tv.getLeft());
            coordinateBean.setMarginTop(tv.getTop());
            coordinateBeenList.add(coordinateBean);

            Log.i("TESTTEST", "保存时标点数据: ======="+ coordinateBean.getValue()+" "+coordinateBean.getType()+" "+coordinateBean.getAscriptionId());
        }

        BMWDetectMainActivity.detectMainActivity.getLocalDetectionData().setCoordinateBeenList(coordinateBeenList);


        //保存标点坐标到数据库  -> 李波 on 2017/11/2.
        String data = new Gson().toJson(coordinateBeenList);
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_USE_SPOT,TaskId, PadSysApp.getUser().getUserId(),data);
        finish();
    }

    /**
     * Created by 李波 on 2017/11/2.
     * 回显标点数据 等待容器控件加载完毕后才加载，避免错乱
     */
    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {

            String Json = DBManager.getInstance().queryLocalUseTask(TaskId,Constants.DATA_TYPE_USE_SPOT,PadSysApp.getUser().getUserId());
            if (!TextUtils.isEmpty(Json)) {
                ArrayList<DefectLegendListBean> list = new Gson().fromJson(Json, new TypeToken<ArrayList<DefectLegendListBean>>(){}.getType());

                for (int i = 0; i < list.size(); i++) {
                    DefectLegendListBean coordinateBean = list.get(i);
                    addSpot(coordinateBean.getValue(),
                            coordinateBean.getType(),
                            coordinateBean.getAscriptionId(),
                            coordinateBean.getMarginLeft(),
                            coordinateBean.getMarginTop());

                    Log.i("TESTTEST", "反显标点数据: "+ coordinateBean.getValue()+" "+coordinateBean
                            .getType()+" "+coordinateBean.getAscriptionId());
                }

            }
            llRoot.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        }
    };

    /**
     * 屏幕指定区域截屏生成图片
     * 耗时操作单开线程
     * @param activity 当前Activity
     * @param v 指定区域（如：截取某 RelativeLayout 整个视图）
     */
    public  void saveImage(final Activity activity, final View v) {

        new Thread() {
            public void run() {
                Bitmap bitmap;

                //测试路径
//                String pathSD = Environment.getExternalStorageDirectory()+"/0";
//                if (!FileUtils.isFileExists(pathSD)){
//                    File file = new File(pathSD);
//                    file.mkdirs();
//                }
//                String path =  pathSD + File.separator + "BMWFACADE.png";

                //正式路径
                String path =  PadSysApp.picDirPath + Constants.BWMPICNAME;
                View view = activity.getWindow().getDecorView();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                bitmap = view.getDrawingCache();
                Rect frame = new Rect();
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int[] location = new int[2];
                v.getLocationOnScreen(location);

                try {
                    bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], v.getWidth(), v.getHeight());
                    FileOutputStream fout = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("IMAGE", "生成预览图片失败：" + e);
                    if (num<2) {
                        num++;
                        saveImage(BMWCarSketchmapActivity.this, rl);
                    }else
                        Log.e("IMAGE", "已超过 3 次" + e);

                }finally {
                    // 清理缓存
                    view.destroyDrawingCache();
                }
            };
        }.start();

    }


}
