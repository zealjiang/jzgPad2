package com.jcpt.jzg.padsystem.mvpview;/**
 * Created by voiceofnet on 2016/12/6.
 */

import com.jcpt.jzg.padsystem.base.IBaseView;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/12/6 11:24
 * @desc:
 */
public interface IDetectionConfiguration extends IBaseView {
    void requestSucceed(DetectionWrapper data);

    void startCheckSucceed();
}
