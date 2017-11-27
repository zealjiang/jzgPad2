package com.jcpt.jzg.padsystem.interfaces;

import android.content.Intent;

import java.util.zip.ZipInputStream;

/**
 * Created by libo on 2016/11/29.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 自定义相机拍照完成后的监听处理
 */
public interface ICustomCameraListener {
    /**
     * Created by 李波 on 2016/11/29.
     * 自定义相机完成拍照后显示照片
     */
    void setPhoto(Intent data);
    /**
     * Created by 李波 on 2016/11/29.
     * 浏览大图后的重拍
     */
    void recapturePhoto(Intent data);
}
