package com.jcpt.jzg.padsystem.interfaces;

import android.content.Intent;

/**
 * Created by libo on 2016/11/24.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 系统相机拍照完成后的监听处理
 */
public interface ICameraListener {
    /**
     * Created by 李波 on 2016/11/25.
     */
    void setPhoto();
    /**
     * Created by 李波 on 2016/11/30.
     * 预览大图点击重拍的处理
     */
    void recapturePhoto(Intent data);

}
