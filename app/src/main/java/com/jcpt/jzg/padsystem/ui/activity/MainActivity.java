package com.jcpt.jzg.padsystem.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.AppManager;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.dialog.ShowMsgDialog;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.fragment.MyTaskFragment;
import com.jzg.lib.crash.LogsActivity;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

import static com.jcpt.jzg.padsystem.R.id.tvExport;

public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private Toolbar toolbar;
    /**
     * Created by wujj on 2017/1/13
     * 是否点击的回退按钮标识
     */
    private boolean isClickBackPressed;

    //导出数据库使用
    private boolean isStarted;//判断是否开启计时
    public static final int MIN_CLICK_DELAY_TIME = 3000;
    private long lastClickTime = 0;
    private int count = 0;
    private TimerTask timerTask;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        manager = getSupportFragmentManager();
        fragmentTransaction =   manager.beginTransaction();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTaskClaim = (TextView) toolbar.findViewById(R.id.tvTaskClaim);
        tvTaskClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TaskClaimActivity.class));
            }
        });

        if(PadSysApp.getUser().getIsSuperAppraiser()==1){
            tvTaskClaim.setVisibility(View.GONE);
        }

        setSupportActionBar(toolbar);
        setTitle("");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ColorStateList csl=getResources().getColorStateList(R.color.navigation_menu_item_color);
        navigationView.setItemTextColor(csl);
        navigationView.getHeaderView(0).findViewById(R.id.tvMyTask).setOnClickListener(listener);
        navigationView.getHeaderView(0).findViewById(R.id.tvModifyPwd).setOnClickListener(listener);
        navigationView.getHeaderView(0).findViewById(R.id.tvExit).setOnClickListener(listener);
        navigationView.getHeaderView(0).findViewById(tvExport).setOnClickListener(listener);
        navigationView.getHeaderView(0).findViewById(R.id.tvTest).setOnClickListener(listener);
        fragmentTransaction.replace(R.id.fragments, new MyTaskFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            switch (v.getId()){
                case R.id.tvMyTask:
                    toolbar.setTitle("我的任务");
                    break;
                case R.id.tvModifyPwd:
//                    Intent intent = new Intent(MainActivity.this, UploadActivity.class);
//                      startActivity(intent);
                    break;
                case R.id.tvExit:
                    isClickBackPressed = false;
                    //退出
                    exit(isClickBackPressed);
                    break;
                case tvExport:
                    startActivity(new Intent(MainActivity.this,LogsActivity.class));
                    break;
                case R.id.tvTest:
                    count++;
                    if(!isStarted){
                        if(timer==null){
                            timer = new Timer();
                        }else{
                            timer.purge();
                        }

                        lastClickTime = System.currentTimeMillis();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        long currentTime = System.currentTimeMillis();
                                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                                            lastClickTime = currentTime;
                                            timerTask.cancel();
                                            isStarted = false;
                                            if(count>=4){
                                                navigationView.getHeaderView(0).findViewById(tvExport).setVisibility(View.VISIBLE);
                                            }
                                            count = 0;
                                        }else{
                                            navigationView.getHeaderView(0).findViewById(tvExport).setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                        };
                        isStarted = true;
                        timer.schedule(timerTask,0,1000);
                    }

                    break;
            }

        }
    };


    //做程序调试用，比如要测试第一次进入没缓存的情况，这样省去每次都去卸载了重新安装  -> 李波 on 2017/11/16.
    private void dubugClearDB() {
        //删除本地数据库此taskId的记录
        DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(),PadSysApp.taskId);
    }

    @Override
    public void onBackPressed() {
        isClickBackPressed = true;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //删除存放压缩图片的临时文件夹  鲁班压缩用到的 ImageCompressor.java  by zealjiang
            File tmpPath = new File(Environment.getExternalStorageDirectory(), "compress_cache");
            FileUtils.deleteDir(tmpPath);

            //删除JzgPad2文件下所有的空文件夹
            deleteEmptyFolder();

            //退出
            exit(isClickBackPressed);
        }
    }

    /**
     * 删除JzgPad2文件下所有的空文件夹
     */
    private void deleteEmptyFolder() {
        File rootDir = new File(Constants.ROOT_DIR);
        if(rootDir.exists()){
            File[] fileDirs = rootDir.listFiles();
            if(fileDirs == null){
                return;
            }
            if(fileDirs.length>0){
                for (int i = 0; i < fileDirs.length; i++) {
                    if(fileDirs[i].isDirectory()){
                        File[] files = fileDirs[i].listFiles();
                        if(files.length == 0){
                            fileDirs[i].delete();
                        }
                    }
                }
            }
        }
    }

    /**
     * 退出前提示用户 by zealjiang
     */
    private void exit(final boolean isClickBackPressed){
        ShowMsgDialog.showMaterialDialog2Btn(this, "提示", "是否退出精真估检测系统", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**
                 * Created by wujj on 2017/1/13
                 * 如果点击的侧滑菜单中的退出系统则跳转到登录页面且密码置空，如果点击返回键则退出到桌面
                 */
                AppManager.getAppManager().finishAllActivity();
                System.exit(1);
            }
        }, "", "退出");
    }

}
