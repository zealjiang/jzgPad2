package com.jcpt.jzg.padsystem.http;


import com.jcpt.jzg.padsystem.update.UpdateApp;
import com.jcpt.jzg.padsystem.vo.AdmixedData;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;
import com.jcpt.jzg.padsystem.vo.BMWReCheckBean;
import com.jcpt.jzg.padsystem.vo.BrandList;
import com.jcpt.jzg.padsystem.vo.CarConfigModel;
import com.jcpt.jzg.padsystem.vo.CarInfoModel;
import com.jcpt.jzg.padsystem.vo.CarTypeModel;
import com.jcpt.jzg.padsystem.vo.CarTypeSelectModel;
import com.jcpt.jzg.padsystem.vo.CheckPriceBean;
import com.jcpt.jzg.padsystem.vo.DrivingLicenceModel;
import com.jcpt.jzg.padsystem.vo.HistoryPriceModel;
import com.jcpt.jzg.padsystem.vo.InsUseRecordModel;
import com.jcpt.jzg.padsystem.vo.PlateTypeModel;
import com.jcpt.jzg.padsystem.vo.ProvinceCityModel;
import com.jcpt.jzg.padsystem.vo.ProvinceCityUniqueModel;
import com.jcpt.jzg.padsystem.vo.Repairlog;
import com.jcpt.jzg.padsystem.vo.SeriesList;
import com.jcpt.jzg.padsystem.vo.StartCheck;
import com.jcpt.jzg.padsystem.vo.TaskClaim;
import com.jcpt.jzg.padsystem.vo.TaskClaimList;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.TopWarningValueBean;
import com.jcpt.jzg.padsystem.vo.TrafficViolationBean;
import com.jcpt.jzg.padsystem.vo.Upload;
import com.jcpt.jzg.padsystem.vo.User;
import com.jcpt.jzg.padsystem.vo.VinCheckedModel;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/6/7 10:26
 * @desc:ERP本App相关接口
 */
public interface ApiServer {

    //测试
//    public  static final String BASE_URL="http://192.168.0.140:8066";
    //正式
//    public  static final String BASE_URL="http://jiancepadtwo.guchewang.com";
    //沙河
    public static final String BASE_URL = "http://jiancepadtwo.sandbox.guchewang.com";
    //---------选择车型相关的接口-------------- 李志江
    @FormUrlEncoded
    @POST("/api/CarTypeInfo/GetMakeList")
    public Observable<BrandList> getBrandList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/CarTypeInfo/GetModelList")
    public Observable<SeriesList> getSeriesList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/CarTypeInfo/GetParams")
    public Observable<CarConfigModel> getCarConfig(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/cartypeinfo/GetDiff")
    public Observable<CarTypeSelectModel> getCarDiff(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/CarTypeinfo/GetDiff0417")
    public Observable<CarTypeSelectModel> getCarDiff0417(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/CarTypeInfo/StyleCompare") //车辆参配
    public Observable<ResponseJson<AdmixedData>> getAdmixedData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/FeedBack/Add") //纠错
    public Observable<ResponseJson<Object>> reqCorrector(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/CarTypeInfo/GetNameplate") //获取铭牌型号
    public Observable<ResponseJson<PlateTypeModel>> getPlateTypes(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/CarTypeInfo/GetStyleList") //获取车型
    public Observable<CarTypeModel> getStyleList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/CarTypeInfo/GetDataByVin") //通过VIN获取（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
    public Observable<ResponseJson<CarInfoModel>> getDataByVin(@FieldMap Map<String, String> params);
    //-----------------------

    //---------任务初始化接口------------李志江
    @FormUrlEncoded
    @POST("/api/Configure/GetConfigure0327") //初始化接口
    public Observable<ResponseJson<DetectionWrapper>> getConfigure(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/task/GetBackInfo") //检测数据详情接口
    public Observable<ResponseJson<TaskDetailModel>> getDetailData(@FieldMap Map<String, String> params);
    //-----------------------------

    //开始检测接口      郑有权
    @FormUrlEncoded
    @POST("/api/task/StartCheck")
    public Observable<ResponseJson<StartCheck>> getStartCheck(@FieldMap Map<String, String> params);

    //任务列表和任务认领接口
    @FormUrlEncoded
    @POST("/api/task/GetTaskViewList")
    public Observable<ResponseJson<TaskClaimList>> getTaskClaimList(@FieldMap Map<String, String> params);
    //任务认领接口      郑有权
    @FormUrlEncoded
    @POST("/api/task/AcceptTask")
    public Observable<ResponseJson<TaskClaim>> getTaskClaim(@FieldMap Map<String, String> params);
    //任务退回接口      郑有权
    @FormUrlEncoded
    @POST("/Api/Task/RejectTask")
    public Observable<ResponseJson<String>> backTask(@FieldMap Map<String, String> params);
    //维保记录查看接口      郑有权
    @FormUrlEncoded
    @POST("/api/CarInfo/GetActiveRepairLog")
    public Observable<ResponseJson<Repairlog>> getActiveRepairLog(@FieldMap Map<String, String> params);
    //维保查询接口
    @FormUrlEncoded
    @POST("api/CarInfo/SelActiveRepairLog")
    public Observable<ResponseJson<Repairlog>> getCarInfoController(@FieldMap Map<String, String> params);

    //上传文件
//    @FormUrlEncoded
    @POST("/api/PictrueIdent/UploadFileStep")
    @Multipart
//    Observable<ResponseJson<String>> uploadFileInfo(@FieldMap Map<String, String> options) ;
    Observable<Upload> uploadFileInfo(@QueryMap Map<String, String> options,
                                                    @PartMap Map<String, RequestBody> externalFileParameters) ;
    //出险记录        李志江
    @FormUrlEncoded
    @POST("/api/carinfo/GetInsuranceList")
    public Observable<ResponseJson<InsUseRecordModel>> getInsUseRecord(@FieldMap Map<String, String> params);

    //-----------登录------------------------------ 李志工
    @FormUrlEncoded
    @POST("/api/user/login")
    public Observable<ResponseJson<User>> login(@FieldMap Map<String, String> params);
    //-----------升级------------------------------ 李志江
    @FormUrlEncoded
    @POST("/api/app/ver")
    public Observable<UpdateApp> update(@FieldMap Map<String, String> params);

    //-----------手续信息------------------------------ 李志江
    @Multipart
    @POST("/api/PictrueIdent/CarDriveCard") //行驶证识别
    public Observable<ResponseJson<DrivingLicenceModel>> drivingLicenceIdentify(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("/api/area/GetProvinceList") //省
    public Observable<ResponseJson<List<ProvinceCityModel>>> getProvince(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/area/GetCityList") //市
    public Observable<ResponseJson<List<ProvinceCityModel>>> getCity(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/area/GetCityInfoByPlateNumber") //根据车牌号码获取上牌地区
    public Observable<ResponseJson<ProvinceCityUniqueModel>> getProvinceCityUnique(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/Task/GetHistoryByVin") //历史评估价格
    public Observable<ResponseJson<List<HistoryPriceModel>>> getHistoryPrice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/Task/GetHistoryCountByVin") //历史评估价格条数
    public Observable<ResponseJson<Integer>> getHistoryPriceCount(@FieldMap Map<String, String> params);
    //------------------

    //-----------提交------------------------------ 李志江
    @FormUrlEncoded
    @POST("/api/TaskSave/Save0421") //任务保存 - 修改提交
    public Observable<ResponseJson<CheckPriceBean>> submit(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("/api/TaskSave/EstimateRange") //获取峰值预警值接口
    public Observable<ResponseJson<TopWarningValueBean>> getTopWarningValue(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/CarInfo/CheckVin") //VIN校验
    public Observable<ResponseJson<VinCheckedModel>> getVinCheckedResult(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/Task/GetTaskStatus") //点继续检测前先去校验任务状态-当MemberValue为1的时候，可以继续检测，其他状态提示msg
    public Observable<ResponseJson<Integer>> GetTaskStatus(@FieldMap Map<String, String> params);

    //----------------------宝马---------------------------------
    //宝马获取订单信息        李志江
    @FormUrlEncoded
    @POST("/api/task/GetBMWBackInfo")
    public Observable<ResponseJson<BMWOrderInfBean>> getBMWBackInfo(@FieldMap Map<String, String> params);

    //宝马复检待维修项        李志江
    @FormUrlEncoded
    @POST("/api/task/GetRepairList")
    public Observable<ResponseJson<BMWReCheckBean>> getRepairList(@FieldMap Map<String, String> params);

    //宝马复检提交        李志江
    @FormUrlEncoded
    @POST("/api/tasksave/SaveCosmetology")
    public Observable<ResponseJson<String>> saveCosmetology(@FieldMap Map<String, String> params);

    //宝马违章查询        李志江
    @FormUrlEncoded
    @POST("/api/CarInfo/TrafficViolation")
    public Observable<ResponseJson<TrafficViolationBean>> getTrafficViolation(@FieldMap Map<String, String> params);

    //-----------宝马提交文字---------
    @FormUrlEncoded
    @POST("/api/TaskSave/SaveBMW") //任务保存 - 修改提交
    public Observable<ResponseJson<CheckPriceBean>> submitBMW(@FieldMap Map<String, String> params);
    //-------------------------------------------------------------
}
