package com.jcpt.jzg.padsystem.base;


/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface IBaseView {
    /**
     * 显示错误信息
     *
     * @param error 错误信息
     */
    void showError(String error);

    /**
     * 显示加载
     */
    void showDialog();

    /**
     * 关闭加载
     */
    void dismissDialog();


}
