package com.jcpt.jzg.padsystem.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.blankj.utilcode.utils.FileUtils;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;
import java.util.List;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/29 13:56
 * @desc: Fresco 图片框架缓存处理辅助工具类
 */
public class FrescoCacheHelper {
    /***
     * 根据url清除单个缓存
     * @param url 图片url
     * @param needDeleteFile 是否需要同步删除文件
     */
    public static void clearSingleCacheByUrl(String url,boolean needDeleteFile){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Fresco.getImagePipeline().evictFromCache(Uri.parse(url));
        if(needDeleteFile && !url.startsWith("http")){
            FileUtils.deleteFile(url);
        }
    }

    //删除一组图片
    public static void clearAllCaches(List<String> urls,boolean needDeleteFile){
        if(urls==null){
            return;
        }
        for(String url:urls){
            if(TextUtils.isEmpty(url)){
                continue;
            }
            Fresco.getImagePipeline().evictFromCache(Uri.parse(url));
            if(needDeleteFile && !url.startsWith("http")){
                FileUtils.deleteFile(url);
            }
        }
    }

    public  static  void getCacheUrl( String url){
        Uri uri = Uri.parse(url);
        ImageRequest imageRequest=ImageRequest.fromUri(uri);
        CacheKey cacheKey= DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest,null);
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainDiskStorageCache().getResource(cacheKey);
        if(resource != null){
            File file=((FileBinaryResource)resource).getFile();
            System.out.println("url =======" + file.toURI().toString());
        }

    }

    /***
     * 删除一组缓存
     * @param urls 图片url
     * @param needDeleteFile 是否需要同步删除文件
     */
    public static void clearMultiCaches(List<String> urls,boolean needDeleteFile){
        if(urls==null || urls.size()==1){
            return;
        }
        for(String url:urls){
            if(TextUtils.isEmpty(url)){
                continue;
            }
            Fresco.getImagePipeline().evictFromCache(Uri.parse(url));
            if(needDeleteFile && !url.startsWith("http")){
                FileUtils.deleteFile(url);
            }
        }
    }

    /***
     * 清除Fresco全部缓存
     */
    public static void clearAllCache(){
        Fresco.getImagePipeline().clearCaches();
    }

    /***
     * 获取Fresco缓存大小
     * @return
     */
    public static String getFrescoCacheSize(){
        String size = "0KB";
        long cacheSize = Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
        long kb = Math.round(cacheSize / 1024);
        long mb = Math.round((cacheSize/1024)/1024);
        if(mb>=1){
            size = mb+"MB";
        }else{
           if(kb>=1){
               size = kb+"KB";
           }else{
               size = cacheSize+"Byte";
           }
        }
        return size;
    }
}
