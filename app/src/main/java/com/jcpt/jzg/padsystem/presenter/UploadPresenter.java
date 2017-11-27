package com.jcpt.jzg.padsystem.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.image.util.FileUtils;
import com.jcpt.jzg.padsystem.interfaces.ProgressListener;
import com.jcpt.jzg.padsystem.mvpview.IUploadListener;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.utils.NetworkExceptionUtils;
import com.jcpt.jzg.padsystem.utils.ShowDialogTool;
import com.jcpt.jzg.padsystem.utils.ZipUtils;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.Upload;
import com.jcpt.jzg.padsystem.vo.UploadFile;
import com.jcpt.jzg.padsystem.widget.CommonProgressDialog;
import com.jcpt.jzg.padsystem.widget.FileAccessI;
import com.jcpt.jzg.padsystem.widget.UploadFileRequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: zyq
 */
public class UploadPresenter extends BasePresenter<IUploadListener> implements ProgressListener{

    private String TaskId = "";
    private String TaskStatus = "";

    Context context;


    UploadFile mUploadFile;
    private String ziPathh;         //压缩文件地址
    private String saveMda5 = "";
    private int uploadSuccessNo = 0;    //上传成功块数
    private String  picURl;      //照片保存位置
    private String ziPathhUrl;      //压缩包保存位置文件夹
    File uploadFile;  //上传的文件
    private String  fileMd5 = "";           //压缩后MD5值
    private long curSection = 1024*1024*200;  //切块大小200M
    private List<File> files;   //切割文件总数

    CommonProgressDialog mDialog;
    //最大重新请求次数
    private int MaxRequest = 3;
    //上传失败错误次数
    private int requestErrorCount = 0;
    private ShowDialogTool showDialogTool;

    public UploadPresenter(Context mcontext,IUploadListener from) {
        super(from);
        context = mcontext;
        showDialogTool = new ShowDialogTool();
    }

    public UploadFile getmUploadFile() {
        return mUploadFile;
    }


    public void initData(String mTaskId,String TaskStatus ){
        this.TaskId = mTaskId;
        this.TaskStatus = TaskStatus;
        List<DBBase> lists =  DBManager.getInstance().query(TaskId,"File",PadSysApp.getUser().getUserId());
        if(lists !=  null){
            if(lists.size()>0){
                DBBase dBBase = lists.get(0);
                String filejson = dBBase.getJson();
                Gson gson  = new Gson();
                mUploadFile = gson.fromJson(filejson,UploadFile.class);
            }else{
                mUploadFile = new UploadFile(TaskId,ziPathh,saveMda5,uploadSuccessNo,TaskStatus);
            }

        }else{
            mUploadFile = new UploadFile(TaskId,ziPathh,saveMda5,uploadSuccessNo,TaskStatus);
        }
    }


    /**
     * 上传文件
     */
    public void uploadFile(File file) {


        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, this);
        requestBodyMap.put("file\"; filename=\"" + file.getName(), fileRequestBody);


        PadSysApp.getApiServer().uploadFileInfo(getUploadParams(),requestBodyMap)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<Upload>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (baseView != null) {
                            mDialog.cancel();
                            baseView.uploadFail();
                            if(e != null) {
                                //baseView.showError(NetworkExceptionUtils.getErrorByException(e));
                            }
                        }
                    }
                    @Override
                    public void onNext(Upload response) {
                        //baseView.dismissDialog();
                        if (response.getStatus() == 100) {
                            succeed();
                        } else {
                            fail();
                            baseView.uploadFail();
                            baseView.showError(response.getMsg());
                        }
                    }
                });

    }

    //上传成功，继续上传
    public void succeed(){

        mDialog.cancel();
        requestErrorCount = 0;
        uploadSuccessNo++;
        if(uploadSuccessNo == files.size()){
            MyToast.showLong("上传成功");
            //全部上传成功
            clearUploadFile();
            baseView.uploadSucceed();
        }else if(uploadSuccessNo<files.size()){
            upLoad(files.get(uploadSuccessNo));
        }
    }

    public void fail(){
        MyToast.showLong("上传失败");
        mDialog.cancel();
        requestErrorCount ++;
        //重新请求次数小于最大请求次数。重新上传
        if(requestErrorCount<MaxRequest){
            //上传失败，继续上传。
            upLoad(files.get(uploadSuccessNo));
        }

        saveUpload();
    }



    //上传文件
    public void upLoad(File file){
        if(NetWorkTool.isConnect()){
            upLoadshowDialog(context);
            uploadFile(file);
        }
    }

    //显示正在上传dialog
    public void upLoadshowDialog(Context context){
        mDialog = new CommonProgressDialog(context, R.style.MyUploadDialogStyleTop);
        mDialog.setMessage("正在上传"+"("+ (uploadSuccessNo+1) +"/"+files.size() +")");
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
    }


    public Map<String, String> getUploadParams() {
        Map<String, String> params = new HashMap<String,String>();
        params.put("userid", PadSysApp.getUser().getUserId()+"");
        params.put("fileName",uploadFile.getName());
        params.put("nPos","0");
        params.put("nPosTotal",uploadFile.length()+"");
        params.put("md5",fileMd5);
        params.put("taskId",TaskId);
        params.put("delPicID","");
        params.put("TaskStatus",TaskStatus);
        params = MD5Utils.encryptParams(params);
        return params;
    }

    @Override
    public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
        mDialog.setMax((int)(totalLen/1024));
        mDialog.setProgress((int)(hasWrittenLen/1024));
    }

    /**
     * 压缩切割文件
     */
    public void zipUpload(Context context){
        this.context = context;

        saveMda5 =  mUploadFile.getMD5();
        //获取缓存保存的上传节点
//        uploadSuccessNo = 0;
        uploadSuccessNo = mUploadFile.getUploadSuccessNo();
        TaskStatus = mUploadFile.getTaskStatus();

        picURl = FileUtils.SDCARD_PAHT+ "/JzgPad2/" + TaskId;
        final File file = new File(picURl);

        if (file.exists() && file.list().length > 0) {
            //正在压缩
            showDialogTool.showLoadingDialog(context,"正在压缩……");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ziPathhUrl = FileUtils.SDCARD_PAHT + "/JzgPad2/Upload/"+TaskId;
                    File ziPathh1file = new File(ziPathhUrl);
                    if(!ziPathh1file.exists()){
                        ziPathh1file.mkdirs();
                    }
                    ziPathh = FileUtils.SDCARD_PAHT + "/JzgPad2/Upload/"+TaskId+"/" + TaskId + ".zip";
                    //压缩
                    long startTime1 = System.currentTimeMillis();
                    ZipUtils.zip(picURl, ziPathh);
                    long endTime1 = System.currentTimeMillis();
                    System.out.println("压缩用时" + (endTime1 - startTime1));

                    uploadFile = new File(ziPathh);
                    fileMd5 = MD5Utils.getFileMd5(ziPathh);
                    //判断文件是否改变，改变则删除切割文件,重新切割
                    if (!saveMda5.equals(fileMd5)) {
                        FileAccessI.delTempFile(ziPathh, curSection);
                        //如果文件不一样，则重新从0块开始上传
                        uploadSuccessNo = 0;
                    }
                    long startTime = System.currentTimeMillis();
                    //切割文件
                    files = FileAccessI.returnTempFile(ziPathh, curSection);
                    long endTime = System.currentTimeMillis();
                    System.out.println("切片用时" + (endTime - startTime));


/*                    //统计上传图片文件大小 by zealjiang
                    int totalLength = 0;
                    for (int i = 0; i < files.size(); i++) {
                        totalLength += files.get(i).length();
                    }
                    double sizeM = totalLength/(1024.0*1024.0);
                    MyToast.showShort("上传图片的大小是： "+sizeM +" Mb");*/
                    //上传
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        }
        else{
//            MyToast.showLong("文件不存在");
        }


    }



    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            MyToast.showLong("压缩分割完成");
            showDialogTool.dismissLoadingDialog();

            if(files != null){
                if(uploadSuccessNo == files.size()){
                    uploadSuccessNo= 0;
                }
                if(files.size()>0){
                    upLoad(files.get(uploadSuccessNo));
                }
            }
        }
    };


    /**
     * 保存本地上传进度
     */
    public void saveUpload(){
        mUploadFile.setTaskId(TaskId);
        mUploadFile.setFileName(ziPathh);
        mUploadFile.setMD5(fileMd5);
        mUploadFile.setUploadSuccessNo(uploadSuccessNo);
        mUploadFile.setTaskStatus(TaskStatus);
//        System.out.println("uploadSuccessNo====="+uploadSuccessNo);
        Gson gson = new Gson();
        String mUploadFileJson =gson.toJson(mUploadFile);
//        System.out.println(mUploadFileJson);
        if(DBManager.getInstance().isExist("File",TaskId,PadSysApp.getUser().getUserId())){
            DBManager.getInstance().update(PadSysApp.getUser().getUserId(),TaskId,"File",mUploadFileJson);
        }else{
            DBManager.getInstance().add(mUploadFileJson,"File",TaskId,PadSysApp.getUser().getUserId());
        }


    }


    /**
     * 上传成功后清除图片和缓存
     */
    public void clearUploadFile(){
        if(DBManager.getInstance().isExist("File",TaskId,PadSysApp.getUser().getUserId())){
            DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(),TaskId);
        }
        if(!TextUtils.isEmpty(picURl)){
            File file = new File(picURl);
            deleteFile(file);
        }
        if(!TextUtils.isEmpty(ziPathhUrl)){
            File ziPathhUrlfile = new File(ziPathhUrl);
            deleteFile(ziPathhUrlfile);
        }
    }
    /**
     * 上传成功后清除图片和缓存
     */
    public void clearUploadFile(String fileTaskId){

        if(DBManager.getInstance().isExist("File",TaskId,PadSysApp.getUser().getUserId())){
            DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(),TaskId);
        }

        String picURL = FileUtils.SDCARD_PAHT+ "/JzgPad2/" + fileTaskId;
        if(!TextUtils.isEmpty(picURL)){
            File file = new File(picURL);
            deleteFile(file);
        }
        String ziPathhURL = FileUtils.SDCARD_PAHT + "/JzgPad2/Upload/"+fileTaskId;
        if(!TextUtils.isEmpty(ziPathhURL)){
            File ziPathhUrlfile = new File(ziPathhURL);
            deleteFile(ziPathhUrlfile);
        }


    }

    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
//            MyToast.showLong("文件不存在！");
        }
    }

    /**
     * 是否要提交的文件
     * @param fileName
     * @return
     */
    public boolean isSubmitFile(String fileName) {
        boolean isExist = false;
        if(DetectMainActivity.detectMainActivity != null){
        SubmitModel mSubmitModel = DetectMainActivity.detectMainActivity.getSubmitModel();
        if(mSubmitModel != null){
            List<String> getDefectValue = mSubmitModel.getDefectValue();
            if(getDefectValue != null){
                for(int i = 0;i<getDefectValue.size();i++){
                    if(fileName.contains(getDefectValue.get(i))){
                        isExist = true;
                    }
                }
            }
        }
        }

        return  isExist;
    }



}
