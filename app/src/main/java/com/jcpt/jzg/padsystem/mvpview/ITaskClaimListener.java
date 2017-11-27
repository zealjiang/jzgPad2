package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.TaskBack;
import com.jcpt.jzg.padsystem.vo.TaskClaim;
import com.jcpt.jzg.padsystem.vo.TaskClaimList;

import java.util.Map;

/**
 * Created by 郑有权 on 2016/11/15.
 */

public interface ITaskClaimListener extends IBaseView{

    void taskClaimListsucceed(TaskClaimList taskClaimList, boolean isClickSearch);
    void taskClaimsucceed(TaskClaim TaskClaim);
    void taskBackucceed(String memberValueUrl);
    void taskClaimFail(TaskClaim TaskClaim);

    /**
     * 获取任务认领列表参数
     * @return
     */
    Map<String, String> getTaskClaimListParams();
    /**
     * 获取任务认领参数
     * @return
     */
    Map<String, String> getTaskClaimParams();
    /**
     * 获取任务退回
     * @return
     */
    Map<String, String> getTaskBackParams();


}
