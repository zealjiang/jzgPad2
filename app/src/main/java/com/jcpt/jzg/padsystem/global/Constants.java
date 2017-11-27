package com.jcpt.jzg.padsystem.global;

import android.os.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/9/14 11:20
 * @desc:
 */
public  class Constants {

    public static final String BWMPICNAME = "BMWFACADE.png";

    public static final int STICKER_BTN_HALF_SIZE = 30;
    //车系头部信息位置
    public static final String DateActivityTowaitActivity = "DateActivityTowaitActivity";
    public static final int waitActivityToDateActivity = 0x00000020;
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory()+"/JzgPad2";

    /**
     * Created by 李波 on 2016/12/2.
     * frescol 缩略图压缩系数 数值越小 缩略图的大小就越小
     */
    public static final int FRESCOL_COMPRESSION_COEFFICIENT = 100;

    //-----------车型选择-------------
    public static final String KEY_CARBRANDLIST = "brandList";
    /**
     * 确定列表标题和内容是否有重复标记
     */
    public static final String IS_TITLE = "istitle";


    //我的任务列表按钮
    public static final int BACK = 0;
    public static final int CONTINUEDETECTION = 1;
    public static final int CHECKREPORT = 2;
    public static final int MODIFICATION = 3;
    public static final int CHECKREASON = 4;
    public static final int UPLOAD = 5;
    public static final int START = 6;
    public static final int LONG = 7;

    /**
     * 屏幕宽高以备适配使用
     */
    public static int ScreenWidth;
    public static int ScreenHeight;
    public static float density;//屏幕密度因子
    //请求成功状态码 100
    public static final int SUCCESS_STATUS_CODE=100;
    //被其他人认领 304
    public static final int CLAIM_STATUS_CODE=304;

    //-----------Acache-------------
    public static final String KEY_ACACHE_USER = "acache_user";

    //-----附加信息editText保留的小数位数
    public static final int MAXLENGTH = 1;


    public static final int STATUS_NORMAL=0;
    public static final int STATUS_OK=1;
    public static final int STATUS_ABNORMAL=2;

    public static final int CAPTURE_TYPE_SINGLE = 10081;//单拍
    public static final int CAPTURE_TYPE_MULTI = 10082;//连拍
    public static final String CAPTURE_TYPE = "capture_type";//拍摄模式



    public static final String DATA_TYPE_PLAN="DETECTION_PLAN";//方案数据
    public static final String DATA_TYPE_USE_TASK="DETECTION_TASK";//用户任务数据
    public static final String DATA_TYPE_SUBMIT_PART="DATA_TYPE_SUBMIT_PART";//提交的部分非页面显示数据
    public static final String DATA_TYPE_PROCEDURE="DATA_TYPE_PROCEDURE";//手续信息标识
    public static final String DATA_TYPE_CAR_TYPE="DATA_TYPE_CAR_TYPE";//车型选择标识
    public static final String DATA_TYPE_CAR_TYPE_CONFIG="DATA_TYPE_CAR_TYPE_CONFIG";//车型选择-查看配置标识
    public static final String DATA_TYPE_SUBJOININFO="DATA_TYPE_SUBJOININFO";//附加信息需要提交标识
    public static final String OTHER_INFROMATION="OTHER_INFROMATION";//附加信息初始化数据
    public static final String ADDITIONAL_PHOTO="ADDITIONAL_PHOTO";//附加照片初始化数据
    public static final String DATA_TYPE_BMW_MAINTAIN="DATA_TYPE_BMW_MAINTAIN";//宝马复检待维修项
    public static final String DATA_TYPE_BMW_BEAUTY="DATA_TYPE_BMW_BEAUTY";//宝马复检美容

    public static final String TEMP_TAKE_PHOTO_DIR = "PAD2";//手续信息照片更新

    public static final String DATA_TYPE_BMW_CARGOODS="DATA_TYPE_BMW_CARGOODS";//BMW随车附件数据
    public static final String DATA_TYPE_BMW_TIRESTOP="DATA_TYPE_BMW_TIRESTOP";//BMW轮胎轮毂数据
    public static final String DATA_TYPE_BMW_OTHER="DATA_TYPE_BMW_OTHER";//BMW其他检查数据
    public static final String DATA_TYPE_BMW_EXPLAIN="DATA_TYPE_BMW_EXPLAIN";//BMW补充说明数据

    public static final String DATA_TYPE_USE_SPOT="DETECTION_SOPT";//BMW 外观示意图标点数据
    //归属项对应 id
    public static final String TYREID  ="o125";     //轮胎轮毂
    public static final String LAMPID  ="o129";     //车灯
    public static final String GLASSID ="o131";     //玻璃
    public static final String OTHERID ="";         //其他(置空)

    public static Map<String ,String> photoMaps = new HashMap<String ,String>(){{
        put("1","前排座椅");
        put("2","仪表盘");
        put("3","左侧底大边");
        put("4","左前45°");
        put("5","发动机舱");
        put("6","发动机舱左侧");
        put("7","发动机舱右侧");
        put("8","右侧底大边");
        put("9","后排座椅");
        put("10","中控台");
        put("11","右后45°");
        put("12","左后翼子板封胶");
        put("13","右后翼子板封胶");
        put("14","行李箱底板");
        put("15","行李箱底板底部");
        put("16","铭牌");
        put("17","车架VIN");
        put("D001","轻微变形");
        put("D002","变形");
        put("D003","剐蹭");
        put("D004","色差");
        put("D005","划痕");
        put("D006","锈蚀");
        put("D007","破损");
        put("D008","老化");
        put("D009","磨损");
        put("D010","脏污");
        put("D011","异物");
        put("D012","进水");
        put("D013","缝隙不匀");
        put("D014","使用极限");
        put("D015","异常磨损");
        put("D016","爆漆");
        put("D017","流漆");
        put("D018","掉漆");
        put("D019","改装");
        put("D020","缺失");
        put("D021","同轴花纹不符");
        put("D022","烧焊");
        put("D023","切焊");
        put("D024","封边不整");
        put("D025","拆卸痕迹");
        put("D026","胶体异常");
        put("D027","更换");
        put("D028","喷漆");
        put("D029","钣金修复");
        put("D030","拆卸更换");
        put("D031","故障");
        put("D032","卡滞");
        put("D033","异响");
        put("D034","渗漏");
        put("D035","怠速抖动");
        put("D036","负荷抖动");
        put("D037","怠速熄火");
        put("D038","负荷熄火");
        put("D039","冒蓝烟");
        put("D040","冒黑烟");
        put("D041","冒白烟");
        put("D042","液位异常");
        put("D043","液体变质");
        put("D062","故障灯常亮");
        put("D044","发霉异味");
        put("D045","水印");
        put("D046","泥沙");
        put("D053","多处锈蚀");
        put("D054","霉斑");
        put("D047","焦糊异味");
        put("D048","烟熏");
        put("D049","碳化");
        put("D050","受热变形");
        }
    };

    public static final String ADMIXEDCONTRASTACTIVITY = "admixedcontrastactivity";
}
