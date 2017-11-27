package com.jcpt.jzg.padsystem.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.blankj.utilcode.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Engine {

    private ExifInterface srcExif;
    private File srcImg;
    private File tagImg;
    private int srcWidth;
    private int srcHeight;

    Engine(File srcImg, File tagImg) throws IOException {
        if (isJpeg(srcImg)) {
            this.srcExif = new ExifInterface(srcImg.getAbsolutePath());
        }
        this.tagImg = tagImg;
        this.srcImg = srcImg;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(srcImg.getAbsolutePath(), options);
        this.srcWidth = options.outWidth;
        this.srcHeight = options.outHeight;
    }

    private boolean isJpeg(File photo) {
        return photo.getAbsolutePath().contains("jpeg") || photo.getAbsolutePath().contains("jpg");
    }

    private int computeSize() {
        int mSampleSize;

        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);

        //针对本应用特殊处理
        if (longSide == 2048) {
            mSampleSize = 1;
            return mSampleSize;
        }

        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                mSampleSize = 1;
            } else if (longSide >= 1664 && longSide < 4990) {
                mSampleSize = 2;
            } else if (longSide >= 4990 && longSide < 10240) {
                mSampleSize = 4;
            } else {
                mSampleSize = longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            mSampleSize = longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            mSampleSize = (int) Math.ceil(longSide / (1280.0 / scale));
        }

        //by zj  小图片小压缩
        if (longSide <= 2048 && mSampleSize > 2) {
            mSampleSize = 2;
        }

        return mSampleSize;
    }


    File compress() throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = computeSize();

        Bitmap tagBitmap = BitmapFactory.decodeFile(srcImg.getAbsolutePath(), options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        tagBitmap = rotatingImage(tagBitmap);
        if(tagBitmap==null)return null;
        tagBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        tagBitmap.recycle();

        FileOutputStream fos = new FileOutputStream(tagImg);
        fos.write(stream.toByteArray());
        fos.flush();
        fos.close();
        stream.close();


        //by zealjiang  如果压缩后的图片比原图还大，则直接将原图复制并返回
        if(tagImg.length()>=srcImg.length()){//如果压缩后的图片比原图还大，则直接将原图复制并返回
            String tagImgPath = tagImg.getAbsolutePath();
            tagImg.delete();
            boolean isSuc = FileUtils.copyFile(srcImg.getPath(),tagImgPath);
            if(isSuc){
                return new File(tagImgPath);
            }else {
                return null;
            }
        }

        return tagImg;
    }

    private Bitmap rotatingImage(Bitmap bitmap) {
        if (srcExif == null) return bitmap;
        if (bitmap == null) return null;

        Matrix matrix = new Matrix();
        int angle = 0;
        int orientation = srcExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = 270;
                break;
        }

        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    File highQualityCompress() {
        if(null==srcImg||!srcImg.exists()){
            return null;
        }
        int MaxSize = 500;//压缩后图片的最大大小 单位kB
        String thumb =  tagImg.getAbsolutePath();
        File result = new File(thumb.substring(0, thumb.lastIndexOf("/")));
        if (!result.exists() && !result.mkdirs()) return null;

        if(srcImg.length()/1024<=MaxSize){
            new File(thumb).delete();//如果文件存在，复制会失败
            boolean isSuc = FileUtils.copyFile(srcImg.getPath(),thumb);
            if(isSuc){
                return new File(thumb);
            }else {
                return null;
            }
        }

        String imagePath = srcImg.getAbsolutePath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        while(baos.toByteArray().length/1024>MaxSize){//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            quality -=3;//每次都减少3
            bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        bitmap.recycle();
        try {
            FileOutputStream fos = new FileOutputStream(thumb);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(new File(thumb).length()>=srcImg.length()){//如果压缩后的图片比原图还大，则直接将原图复制并返回
            new File(thumb).delete();
            boolean isSuc = FileUtils.copyFile(srcImg.getPath(),thumb);
            if(isSuc){
                return new File(thumb);
            }else {
                return null;
            }
        }
        return new File(thumb);
    }
}
