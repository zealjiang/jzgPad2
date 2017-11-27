package com.jcpt.jzg.padsystem.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改密码
 *zyq
 */
public class SetPasswrodActivity extends BaseActivity {

    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
    }

    @Override
    protected void setData() {
        init();
    }


    public void init(){

    }


    @OnClick({R.id.btnSetPassword})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnSetPassword:
                MyToast.showLong("修改密码");
            break;
        }
    }


}
