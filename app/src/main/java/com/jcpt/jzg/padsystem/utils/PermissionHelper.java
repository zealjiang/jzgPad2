package com.jcpt.jzg.padsystem.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by voiceofnet on 2017/7/10.
 */

/***
 * 权限相关包装类，可以查询需要申请的权限是否存在，如果不存在则申请
 */
public class PermissionHelper {
    private static final PermissionHelper ourInstance = new PermissionHelper();

    public static PermissionHelper getInstance() {
        return ourInstance;
    }
    private PackageManager pm;
    private String packName;

    private PermissionHelper() {
        packName = PadSysApp.getAppContext().getPackageName();
        pm = PadSysApp.getAppContext().getPackageManager();
    }
    /***
     * @param listener 成功回调
     * @param permissions 需要申请的权限
     */
    public void requestPermission(Activity activity,final RequestResultListener listener, final String ... permissions){
        String[] newPermissions = checkPermissions(permissions);
        if(newPermissions.length==0){
            listener.onResult(true);
        }else{
            RxPermissions rxPermissions = new RxPermissions(activity);
            rxPermissions.request(newPermissions)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            listener.onResult(granted);
                            if (!granted) {
                                MyToast.showLong(getTips(permissions));
                            }
                        }
                    });
        }

    }

    /***
     * 计算权限被拒提醒类型
     * @param permissions
     * @return
     */
    private String getTips(String ... permissions){
        List<String> pers = Arrays.asList(permissions);
        if(pers.contains(Manifest.permission.RECORD_AUDIO)){
            return "此功能需要开启录音授权，请在设置-权限管理中开启精真估专业版的权限!";
        }else if(pers.contains(Manifest.permission.CAMERA)){
            return "此功能需要开启相机授权，请在设置-权限管理中开启精真估专业版的权限!";
        }else if(pers.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) || pers.contains(Manifest.permission.READ_EXTERNAL_STORAGE)){
            return "此功能需要开启SD卡读写授权，请在设置-权限管理中开启!";
        }else{
            return "此功能需要开启SD卡读写授权，请在设置-权限管理中开启!";
        }

    }

    /***
     * 过滤一下，请求的权限是否已经拥有，有则不需要重复申请
     * @param permissions
     * @return
     */
    public String [] checkPermissions(String ... permissions){
        ArrayList<String> pers = new ArrayList<>();
        for(String permission:permissions){
            if(PackageManager.PERMISSION_GRANTED != pm.checkPermission(permission,packName)){
                pers.add(permission);
            }
        }
        return pers.toArray(new String[]{});
    }


    public interface RequestResultListener{
        void onResult(boolean result);
    }
}
