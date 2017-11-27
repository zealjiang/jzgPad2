package com.jcpt.jzg.padsystem.image.util;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.test.TestPhotoEditActivity1;


/**
 * Created by panyi on 2016/8/14.
 * <p/>
 * 贴图合成任务 抽象类
 */
public abstract class StickerTask extends AsyncTask<Bitmap, Void, Bitmap> {
//    private Dialog dialog;

    private TestPhotoEditActivity1 mContext;

    public StickerTask(TestPhotoEditActivity1 activity) {
        this.mContext = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mContext.isFinishing())
            return;

//        dialog = mContext.getLoadingDialog(mContext, "正在保存......",
//                false);
//        dialog.show();
        mContext.showDialog();
    }

    @Override
    protected Bitmap doInBackground(Bitmap... params) {
        // System.out.println("保存贴图!");
        Matrix touchMatrix = mContext.ivShow.getImageMatrix();
//        Matrix touchMatrix = mContext.ivShow.getMatrix();
//        Matrix touchMatrix = mContext.ivShow.getImageViewMatrix();

        Bitmap resultBit = Bitmap.createBitmap(params[0]).copy(
                Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(resultBit);

        float[] data = new float[9];
        touchMatrix.getValues(data);// 底部图片变化记录矩阵原始数据
        Matrix3 cal = new Matrix3(data);// 辅助矩阵计算类
        Matrix3 inverseMatrix = cal.inverseMatrix();// 计算逆矩阵
        Matrix m = new Matrix();
        m.setValues(inverseMatrix.getValues());

        handleImage(canvas, m);

        BitmapUtils.saveBitmap(resultBit, mContext.newPath);
        return resultBit;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
//        dialog.dismiss();
        mContext.dismissDialog();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCancelled(Bitmap result) {
        super.onCancelled(result);
        mContext.dismissDialog();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        onPostResult(result);
        mContext.dismissDialog();
    }

    public abstract void handleImage(Canvas canvas, Matrix m);

    public abstract void onPostResult(Bitmap result);
}//end class
