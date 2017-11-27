package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.TaskBack;
import com.jcpt.jzg.padsystem.vo.TaskClaim;
import com.jcpt.jzg.padsystem.vo.TaskItem;

import java.util.List;
import java.util.Map;

/**
 * Created by 郑有权 on 2016/11/15.
 */

public interface IUploadListener extends IBaseView{

    void uploadSucceed();
    void uploadFail();

    /**
     * 获取上传参数
     * @return
     */
   // Map<String, String> getUploadParams();



}
