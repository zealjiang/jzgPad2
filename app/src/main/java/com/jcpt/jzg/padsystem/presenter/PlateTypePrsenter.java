package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IPlateType;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.PlateTypeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author zealjiang
 * @time 2017/4/18 15:31
 */
public class PlateTypePrsenter extends BasePresenter<IPlateType> {


    public PlateTypePrsenter(IPlateType from) {
        super(from);
    }

    public void getPlateTypes(String vin){

        if(!PadSysApp.networkAvailable){
            MyToast.showShort("没有网络");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params.put("vin",vin+"");
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();

        PadSysApp.getApiServer().getPlateTypes(params)
                .compose(RxThreadUtil.<ResponseJson<PlateTypeModel>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<PlateTypeModel>>(){
                    @Override
                    public void onSuccess(ResponseJson<PlateTypeModel> response) {
                        baseView.dismissDialog();
                        if(response==null||response.getMemberValue()==null){
                            baseView.fail("");
                            return;
                        }
                        List<String> listData = response.getMemberValue().getMappedNoticeList();
                        baseView.success(listData);
                    }
                },new RequestFailedAction(baseView));

    }

}
