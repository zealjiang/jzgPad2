package com.jcpt.jzg.padsystem.utils;

import android.content.SyncStatusObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.image.util.FileUtils;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectDetailItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectType;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: voiceofnet
 * email: 郑有权
 * @time: 2017/3/2 10:27
 * @desc:
 */
public class StringHelper {

    public static String getUrl(Map<String,String> params){
        int index = 0;
        StringBuffer sb = new StringBuffer();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            String value = params.get(key);
            if(index==0){
                sb.append("?").append(key).append("=").append(value);
            }else{
                sb.append("&").append(key).append("=").append(value);
            }
            index++;
        }
        return sb.toString();
    }

    public static SpannableString getSpannableText(String text, int index) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new ForegroundColorSpan(Color.RED), index, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }


    /**
     * L04_P08_A004_F01_D002_1.jpg ---->D002
     * @param st
     * @return
     */
    public static String cut1(String st,int pos){
        String str ="";
        if(st.contains("_")){
            String [] strs = st.split("_");
            if(strs.length > 5 && pos <strs.length){
                str = strs[pos];
            }
        }
        return str;

    }
    /**
     * L04_P08_A004_F01_D002_1.jpg ---->D002
     * @param st
     * @return
     */
    public static String cut(String st){
        String str ="";
        if(st.contains("_")){
            int ls = st.lastIndexOf("_");
            int ls1  = st.lastIndexOf("_", ls-1);
            str = st.substring(ls1+1, ls);

            System.out.println("ls--"+ls+"--ls1--"+ls1);
            System.out.println("str--"+str);
        }


        return str;

    }


    public static String subFile(String fileName){
        String str = "";
        if(fileName.contains(".jpg")){
            str  =  fileName.substring(0,fileName.indexOf(".jpg"));
        }
        return  str;
    }


    public static int returnInt(String id){
        if(TextUtils.isEmpty(id)){
            return 0;
        }else{
            if(TextUtils.isEmpty(id.substring(2))){
                return 0;
            }
            return Integer.parseInt(id.substring(2));
        }

    }
    //获取字符串中的所有数字
    public static String returnNumbers(String str) {
        String str2 = "";
        if (!TextUtils.isEmpty(str.trim())) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }
        }
        return str2;
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }


    public static boolean isImage(String pathName){
        Bitmap drawable = BitmapFactory.decodeFile(pathName);
        if(drawable == null){
            return false;
        }
        return  true;
    }

    /**
     * 保存
     *
     * @param data
     */
    public static void savePlanPhotoId(DetectionWrapper data) {
        if (data != null) {
            List<CheckPositionItem> getCheckPositionList = data.getCheckPositionList();
            if (getCheckPositionList != null && getCheckPositionList.size() > 0) {
                for (int i = 0; i < getCheckPositionList.size(); i++) {
                    CheckPositionItem mCheckPositionItem = getCheckPositionList.get(i);
                    if (mCheckPositionItem != null) {
                        PadSysApp.planPhotoIds.put(mCheckPositionItem.getCheckPositionId(), mCheckPositionItem.getCheckPositionName());
                        List<ImportantItem> getImportantList = mCheckPositionItem.getImportantList();
                        if (getImportantList != null && getImportantList.size() > 0) {
                            for (int j = 0; j < getImportantList.size(); j++) {
                                ImportantItem mImportantItem = getImportantList.get(j);
                                if (mImportantItem != null) {
                                    List<CheckItem> getCheckItemList = mImportantItem.getCheckItemList();
                                    if (getCheckItemList != null && getCheckItemList.size() > 0) {
                                        for (int k = 0; k < getCheckItemList.size(); k++) {
                                            CheckItem mCheckItem = getCheckItemList.get(k);
                                            if (mCheckItem != null) {
                                                PadSysApp.planPhotoIds.put(mCheckItem.getCheckId(), mCheckItem.getCheckName());
                                                List<DefectType> getDefectTypeList = mCheckItem.getDefectTypeList();
                                                if (getDefectTypeList != null && getDefectTypeList.size() > 0) {
                                                    for (int l = 0; l < getDefectTypeList.size(); l++) {
                                                        DefectType mDefectType = getDefectTypeList.get(l);
                                                        if (mDefectType != null) {
                                                            PadSysApp.planPhotoIds.put(mDefectType.getDefectTypeId(), mDefectType.getDefectTypeName());
                                                            List<DefectDetailItem> getDefectDetailList = mDefectType.getDefectDetailList();
                                                            if (getDefectDetailList != null && getDefectDetailList.size() > 0) {
                                                                for (int m = 0; m < getDefectDetailList.size(); m++) {
                                                                    DefectDetailItem mDefectDetailItem = getDefectDetailList.get(m);
                                                                    if (mDefectDetailItem != null) {
                                                                        PadSysApp.planPhotoIds.put(mDefectDetailItem.getDefectId(), mDefectDetailItem.getDefectName());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }



    public static String cutPhotoName(String photoName){
        StringBuilder str = new StringBuilder();
        if(!TextUtils.isEmpty(photoName)){
//            str.append(PadSysApp.planPhotoIds.get(cut1(photoName,0)));
            str.append(PadSysApp.planPhotoIds.get(cut1(photoName,1)+"_"+cut1(photoName,2)));
//            str.append(PadSysApp.planPhotoIds.get(cut1(photoName,3)));
            str.append(PadSysApp.planPhotoIds.get(cut(photoName)));
        }

        return str.toString();
    }



    /**
     * 传入文件名以及字符串, 将字符串信息保存到文件中
     *
     * @param strFilename       写入文件名称
     * @param strBuffer         写入数据
     */
    public static void TextToFile(final String strFilename, final String strBuffer)
    {
        try
        {
            // 创建文件对象
            File fileText = new File(strFilename);
            // 向文件写入对象写入信息
            FileWriter fileWriter = new FileWriter(fileText);

            // 写文件
            fileWriter.write(strBuffer);
            // 关闭
            fileWriter.close();
        }
        catch (IOException e)
        {
            //
            e.printStackTrace();
        }
    }



    //方案写入本地文件
    public static void SDPlan(){
        try {
        new Thread(){
            @Override
            public void run() {
                List<DBBase> lists =  DBManager.getInstance().queryLocalAllPlan();
                if(lists != null && lists.size()>0){
                    for (int i = 0;i<lists.size();i++){
                        TextToFile(FileUtils.SDCARD_PAHT + "/JzgPad2/"+"plan_"+lists.get(i).getTaskId()+".txt",
                                "planId ========" + lists.get(i).getTaskId()+"======planJson ========" + lists.get(i).getJson());

                    }
                }
            }
        }.start();

        }catch (Exception e){
            System.out.println("保存方案为本地文件失败");
        }

    }

}
