package com.jcpt.jzg.padsystem.interfaces;

import android.text.TextWatcher;

/**
 * Created by voiceofnet on 2017/11/13.
 * 如果只关心afterTextChanged可以用这个简化类来代替
 */

public abstract class SimpleTextWatcher implements TextWatcher {
    protected  String beforeText = "";
    protected String changedText = "";
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeText = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before == 0 && count > 0) {
            changedText = s.subSequence(start, start + count).toString();
        } else {
            changedText = "";
        }
    }
}
