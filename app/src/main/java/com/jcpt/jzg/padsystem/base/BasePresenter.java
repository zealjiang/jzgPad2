package com.jcpt.jzg.padsystem.base;

/**
 * Created by wangyd on 16/7/25.
 */
public abstract class BasePresenter<T> {
    protected String TAG = getClass().getName();
    protected T baseView;

    public BasePresenter(T from) {
        this.baseView = from;
    }
}
