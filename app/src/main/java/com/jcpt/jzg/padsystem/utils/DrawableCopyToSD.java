package com.jcpt.jzg.padsystem.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;


import com.jcpt.jzg.padsystem.app.PadSysApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by libo on 2017/11/27.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class DrawableCopyToSD {
    /**
     * 存储资源为ID的图片
     * @param id
     * @param name
     */
    public static void saveDrawableToSD(Context context, int drawableId, String name, Bitmap
            .CompressFormat format,int compress) {
        Drawable drawable = idToDrawable(context,drawableId);
        Bitmap bitmap = drawableToBitmap(drawable);
        saveBitmap(bitmap, name, format,compress);
    }

    /**
     * 将资源ID转化为Drawable
     * @param id
     * @return
     */
    public static Drawable idToDrawable(Context context,int id) {
        return context.getResources().getDrawable(id);
    }

    /**
     * 将Drawable转化为Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable == null)
            return null;
        return ((BitmapDrawable)drawable).getBitmap();
    }

    /**
     * 将Bitmap以指定格式保存到指定路径
     * @param bitmap
     * @param path
     */
    public static void saveBitmap(Bitmap bitmap, String name, Bitmap.CompressFormat format,int compress) {
        // 创建一个位于SD卡上的文件
        File file = new File(PadSysApp.picDirPath,name);
        FileOutputStream out = null;
        try{
            // 打开指定文件输出流
            out = new FileOutputStream(file);
            // 将位图输出到指定文件
            bitmap.compress(format, compress,out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
