package com.jcpt.jzg.padsystem.presenter;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.IHistoryPrice;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.HistoryPriceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史评估价格
 * Created by zealjiang on 2016/11/17 15:39.
 * Email: zealjiang@126.com
 */

public class HistoryPricePresenter extends BasePresenter<IHistoryPrice> {

    public HistoryPricePresenter(IHistoryPrice from) {
        super(from);
    }

    /**
     * 历史评估价格
     * @author zealjiang
     * @time 2016/11/17 15:49
     */
    public void getHistoryPrice(String vin){
        if(!PadSysApp.networkAvailable){
            MyToast.showShort("没有网络");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params.put("vin",vin+"");
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getHistoryPrice(params)
                .compose(RxThreadUtil.<ResponseJson<List<HistoryPriceModel>>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<List<HistoryPriceModel>>>(){
                    @Override
                    public void onSuccess(ResponseJson<List<HistoryPriceModel>> response) {
                        baseView.dismissDialog();
                        List<HistoryPriceModel> listData = response.getMemberValue();
                        baseView.succeedDetail(listData);
                    }
                },new RequestFailedAction(baseView));
    }

    /**
     * 历史评估价格
     * @author zealjiang
     * @time 2016/11/17 15:49
     */
    public void getHistoryPriceCount(String vin){
        if(!PadSysApp.networkAvailable){
            MyToast.showShort("没有网络");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("UserId",PadSysApp.getUser().getUserId()+"");
        params.put("vin",vin+"");
        params = MD5Utils.encryptParams(params);
        baseView.showDialog();
        PadSysApp.getApiServer().getHistoryPriceCount(params)
                .compose(RxThreadUtil.<ResponseJson<Integer>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<Integer>>(){
                    @Override
                    public void onSuccess(ResponseJson<Integer> response) {
                        baseView.dismissDialog();
                        int count = response.getMemberValue();
                        baseView.succeedCount(count);
                    }
                },new RequestFailedAction(baseView));
    }

}
