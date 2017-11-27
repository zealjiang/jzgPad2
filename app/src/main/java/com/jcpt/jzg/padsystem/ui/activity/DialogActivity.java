package com.jcpt.jzg.padsystem.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2017/9/18.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 全局Dialog
 */
public class DialogActivity extends AppCompatActivity {


    @BindView(R.id.context)
    TextView context;
    @BindView(R.id.bt)
    Button bt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_layout);
        ButterKnife.bind(this);

        String content = getIntent().getStringExtra("content");
        context.setText(content);
    }

    @OnClick({R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt:
                finish();
                break;
        }
    }

}
