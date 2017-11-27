package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.LocalDetectionData;

/**
 * Created by wujj on 2016/11/23.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public interface ISubjoinInfoView extends IBaseView {
    void requestSubjoinSucceed(DetectionWrapper data);
}
