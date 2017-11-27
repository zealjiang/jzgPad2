package com.jcpt.jzg.padsystem.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 生成缩略图
 * Created by zealjiang on 2016/12/2 11:10.
 * Email: zealjiang@126.com
 */

public class ThumbnailPic {

    public boolean createThumbnailPic(String filePath){
        if(TextUtils.isEmpty(filePath)){
            return false;
        }
        if(!new File(filePath).exists()){
            return false;
        }
        //生成缩略图
        return false;
    }


    /**
     * 产生缩略图。
     *
     * @param thumbnailOptions 产生缩略图的配置。
     *
     * @throws IOException 如果文件不存在，或者没有读取的权限，则会抛出异常。
     */
    public static void thumbnail(Options thumbnailOptions) throws IOException {
        long begin = System.currentTimeMillis();
        BitmapFactory.Options options = calculateImageSize(thumbnailOptions.sourcePath);
        options.inJustDecodeBounds = false;

        options.inSampleSize = calculateInSampleSize(options, thumbnailOptions.width, thumbnailOptions.height);

        boolean needScaleImage = options.outWidth / options.inSampleSize > thumbnailOptions.width &&

                options.outHeight / options.inSampleSize > thumbnailOptions.height;

        Bitmap bitmap = BitmapFactory.decodeFile(thumbnailOptions.sourcePath, options);


        if (needScaleImage) {

            bitmap = thumbnailSamllImage(bitmap, thumbnailOptions.width, thumbnailOptions.height);

        }



        saveBitmapToFile(bitmap, thumbnailOptions.targetPath);



        Log.i("thumbnail", "产生[" + thumbnailOptions.sourcePath + "]缩略图共花费了" + (System.currentTimeMillis() - begin) + "ms");

    }



    private static Bitmap thumbnailSamllImage(Bitmap bitmap, int width, int height) {

        long begin = System.currentTimeMillis();

        int oWidth = bitmap.getWidth();

        int oHeight = bitmap.getHeight();



        float ratio = Math.min(oWidth * 1.0f / width, oHeight * 1.0f / height);

        width = (int)(oWidth / ratio);

        height = (int)(oHeight / ratio);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);

        Log.i("thumbnailSallImage", "花费：" + (System.currentTimeMillis() - begin) + "ms");

        return bitmap;

    }

    /**
     * 计算图片大小。
     *
     * @param sourcePath
     * @return
     * @throws FileNotFoundException
     */

    public static BitmapFactory.Options calculateImageSize(String sourcePath) throws IOException {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(sourcePath));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
            return options;
        } catch (FileNotFoundException e) {
            //throw new SourcePathNotFoundException(e);
        }finally {
            if (is != null) {
                is.close();
            }
            return null;
        }

    }



    /**

     * 计算图片的缩放比例。

     *

     * @param options

     * @param reqWidth

     * @param reqHeight

     * @return

     */

    public static int calculateInSampleSize(BitmapFactory.Options options,

                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;

        final int width = options.outWidth;

        int inSampleSize = 1;



        options.inSampleSize = 8;



        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;

            final int halfWidth = width / 2;



            while ((halfHeight / inSampleSize) > reqHeight &&

                    (halfWidth / inSampleSize) > reqWidth) {

                inSampleSize *= 2;

            }

        }



        return inSampleSize;

    }



    /**

     * 保存Bitmap到文件中。

     *

     * @param bitmap      图片位图数据。

     * @param targetPath 保存的文件位置。

     */

    public static void saveBitmapToFile(Bitmap bitmap, String targetPath) {

        OutputStream os = null;



        try {

            os = new BufferedOutputStream(new FileOutputStream(targetPath));

            bitmap.compress(guessImageType(targetPath), 90, os);

        } catch (FileNotFoundException ex) {
            //throw new TargetPathNotFoundException(ex);
        } finally {

            if (os != null) {

                try {

                    os.close();

                } catch (IOException ex) {
                    //Log.e("Thumbnails.saveBitmapToFile()", "关闭文件流出错。");
                    ex.printStackTrace();

                }

            }

        }

    }



    /**

     * 根据文件名猜测图片类型。

     *

     * @param filePath 图片路径。

     * @return 图片类型。

     */

    public static Bitmap.CompressFormat guessImageType(String filePath) {

        String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();

        if (fileExt.equals("png")) {

            return Bitmap.CompressFormat.PNG;

        } else if (fileExt.equals("webp")) {

            return Bitmap.CompressFormat.WEBP;

        } else {

            return Bitmap.CompressFormat.JPEG;

        }

    }



    /**

     * 生成缩略图的配置。

     */

    public static class Options {

        /**

         * 缩略图存放路径。

         */

        public String targetPath;

        /**

         * 源图片路径。

         */

        public String sourcePath;

        /**

         * 缩略图宽度

         */

        public int width;

        /**

         * 缩略图高度

         */

        public int height;



        public Options() {

        }

    }
}
