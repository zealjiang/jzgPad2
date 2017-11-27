package com.jcpt.jzg.padsystem.vo.detection;

import java.io.Serializable;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 15:02
 * @desc:
 */
public class PictureItem implements Serializable,Comparable<PictureItem> {


    /**
     * PicId : 9
     * PicName : 右后车门铰链
     */

    private String PicId;
    private String PicName;
    private String PicFullName;
    private String PicPath;//大图路径，提交和查看大图用
    private String thumbnailPath;//缩略图路径，显示用
    private boolean isSmallCompress;//是否为少量压缩，默认为false
    private int PicOrder;
    private String CreateTime;
    private String PicStatus;

    public int getPicOrder() {
        return PicOrder;
    }

    public void setPicOrder(int picOrder) {
        PicOrder = picOrder;
    }

    public String getPicId() {
        return PicId;
    }

    public void setPicId(String PicId) {
        this.PicId = PicId;
    }

    public String getPicName() {
        return PicName;
    }

    public void setPicName(String PicName) {
        this.PicName = PicName;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }


    public boolean isSmallCompress() {
        return isSmallCompress;
    }

    public void setSmallCompress(boolean smallCompress) {
        isSmallCompress = smallCompress;
    }

    public String getPicFullName() {
        return PicFullName;
    }

    public void setPicFullName(String picFullName) {
        PicFullName = picFullName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getPicStatus() {
        return PicStatus;
    }

    public void setPicStatus(String picStatus) {
        PicStatus = picStatus;
    }

    public PictureItem() {
    }

    public PictureItem(String picId, String picName, String picPath) {
        this.PicId = picId;
        this.PicName = picName;
        this.PicPath = picPath;
    }
    public PictureItem(String picId, String picName, String picPath,int picOrder ) {
        this.PicId = picId;
        this.PicName = picName;
        this.PicPath = picPath;
        this.PicOrder = picOrder;
    }
    public PictureItem(String picId, String picName, String picPath,String thumbnailPath) {
        this(picId,picName,picPath);
        this.thumbnailPath = thumbnailPath;
    }

    @Override
  /* public int compareTo(PictureItem another) {
     return  Integer.valueOf(this.getPicOrder()).compareTo(Integer.valueOf(another.getPicOrder()));
    }*/
   public int compareTo(PictureItem another) {
        //附件照片的排序
        if(this.getPicId().indexOf("_") != -1){
            return  new Double((String) this.getPicId().substring(this.getPicId().lastIndexOf("_")+1))
                    .compareTo(new Double((String) another.getPicId().substring(another.getPicId().lastIndexOf("_")+1)));
        //基本照片的排序
        }else if(another.getPicOrder() != 0){
            return  Integer.valueOf(this.getPicOrder()).compareTo(Integer.valueOf(another.getPicOrder()));
        }else{
            return  new Double((String) this.getPicId()).compareTo(new Double((String) another.getPicId()));
        }

    }
}
