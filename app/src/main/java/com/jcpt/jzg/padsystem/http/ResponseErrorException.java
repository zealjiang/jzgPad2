package com.jcpt.jzg.padsystem.http;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 14:41
 * @desc:
 */
public class ResponseErrorException extends RuntimeException {
    public ResponseErrorException(String msg) {
        super(msg);
    }
}
