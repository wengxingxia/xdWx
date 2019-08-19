package com.xander.wx.common.exception;


import com.xander.wx.common.utils.StringFormatUtil;

/**
 * Description: 自定义异常——主要用于 记录微信相关网络请求异常
 *
 * @author Xander
 * datetime: 2019/8/19 11:16
 */
public class WxRestException extends RuntimeException {

    private static final long serialVersionUID = 5857825376993092542L;

    final static private String msgFormat = "code:{}, tips:{}";

    private int statusCode;

    private String tips;

    private RuntimeException proxyErrorCause;

    private WxRestException(String msg, int statusCodeP, String tipsP, RuntimeException causeP) {

        super(msg, null);
        this.statusCode = statusCodeP;
        this.tips = tipsP;
        this.proxyErrorCause = causeP;
    }


    public static WxRestException newInstance(int statusCodeP, String tipsP) {
        String msg = StringFormatUtil.format(msgFormat, statusCodeP, tipsP);
        WxRestException instance = new WxRestException(msg, statusCodeP, tipsP, null);
        return instance;
    }

    public static WxRestException newInstance(String tipsP, RuntimeException causeP) {
        int statusCodeP = 500;
        String msg = StringFormatUtil.format(msgFormat, statusCodeP, tipsP);
        WxRestException instance = new WxRestException(msg, statusCodeP, tipsP, causeP);
        return instance;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getTips() {
        return tips;
    }

    public RuntimeException getCause() {
        return proxyErrorCause;
    }

}
