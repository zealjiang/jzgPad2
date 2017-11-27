package com.jcpt.jzg.padsystem.presenter;

import android.widget.Toast;

import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BasePresenter;
import com.jcpt.jzg.padsystem.http.RequestFailedAction;
import com.jcpt.jzg.padsystem.http.RequestSuccessAction;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.http.RxThreadUtil;
import com.jcpt.jzg.padsystem.mvpview.ITaskClaimListener;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.vo.TaskClaim;
import com.jcpt.jzg.padsystem.vo.TaskClaimList;

import rx.Subscription;
import rx.functions.Action1;

import static com.jcpt.jzg.padsystem.app.PadSysApp.getApiServer;
import static com.jcpt.jzg.padsystem.app.PadSysApp.getAppContext;

/**
 * author: zyq
 */
public class TaskClaimPresenter extends BasePresenter<ITaskClaimListener> {


    public TaskClaimPresenter(ITaskClaimListener from) {
        super(from);
    }

    /**
     * 加载任务认领列表
     */
    public Subscription loadTaskClaimList(final boolean isClickSearch) {
        baseView.showDialog();
        if (NetWorkTool.isConnect()) {
            baseView.showDialog();
            Subscription subscription = PadSysApp.getApiServer().getTaskClaimList(baseView.getTaskClaimListParams())
                    .compose(RxThreadUtil.<ResponseJson<TaskClaimList>>networkSchedulers())
                    .subscribe(new RequestSuccessAction<ResponseJson<TaskClaimList>>() {
                        @Override
                        public void onSuccess(ResponseJson<TaskClaimList> response) {
                            baseView.dismissDialog();
                            if (response.getStatus() == 100) {
                                baseView.taskClaimListsucceed(response.getMemberValue(),isClickSearch);
                            } else {
                                baseView.showError(response.getMsg());
                            }
                        }
                    }, new RequestFailedAction(baseView));

            return subscription;
        }else{
            baseView.showError("");
        }
        return null;
    }

    /**
     * 任务认领
     */
    public void loadTaskClaim() {
        if (NetWorkTool.isConnect()) {
            baseView.showDialog();
            getApiServer().getTaskClaim(baseView.getTaskClaimParams())
                    .compose(RxThreadUtil.<ResponseJson<TaskClaim>>networkSchedulers())
                    .subscribe(new RequestSuccessAction<ResponseJson<TaskClaim>>() {
                        @Override
                        public void onSuccess(ResponseJson<TaskClaim> response) {
                            baseView.dismissDialog();
                            if (response.getStatus() == 100) {
                                baseView.taskClaimsucceed(response.getMemberValue());
                            } else if (response.getStatus() == 304) {
                                baseView.taskClaimFail(response.getMemberValue());
                            } else {
                                baseView.showError(response.getMsg());
                            }
                        }
                    }, new Action1<Throwable>(){

                        @Override
                        public void call(Throwable throwable) {
                            baseView.dismissDialog();
                            Toast.makeText(getAppContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                            baseView.showError("");
                        }
                    });
        }else{
            baseView.showError("没有网络");
        }
    }

    /**
     * 退回
     */
    public void backTask() {
        if (NetWorkTool.isConnect()) {

            baseView.showDialog();
            getApiServer().backTask(baseView.getTaskBackParams())
                    .compose(RxThreadUtil.<ResponseJson<String>>networkSchedulers())
                    .subscribe(new RequestSuccessAction<ResponseJson<String>>() {
                        @Override
                        public void onSuccess(ResponseJson<String> response) {
                            baseView.dismissDialog();
                            if (response.getStatus() == 100) {
                                baseView.taskBackucceed(response.getMemberValue());
                            } else {
                            baseView.showError(response.getMsg());
                        }
                    }
                    }, new RequestFailedAction(baseView));
        }else{
            baseView.showError("没有网络");
        }
    }


}
