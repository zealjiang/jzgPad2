package com.jcpt.jzg.padsystem.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.widget.ClearableEditText;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jcpt.jzg.padsystem.R.id.cetCarNamePlate;

/**
 * 国标码确认
 * Created by zealjiang on 2016/12/8 20:23.
 * Email: zealjiang@126.com
 */

public class GBCodeConfirmDialog {

    private Activity activity;
    private Dialog dialog;
    private String content;

    public GBCodeConfirmDialog(Activity activity) {
        this.activity = activity;
    }

    public void createDialog(){
        dialog = new Dialog(activity,R.style.MyDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_gb_code_view,null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        init(view);
		/*
         * 将对话框的大小按屏幕大小的百分比设置
         */
        Window dialogWindow = dialog.getWindow();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    private void init(View view){
        final ClearableEditText tvMsg =  (ClearableEditText) view.findViewById(cetCarNamePlate);
        ImageView close = (ImageView) view.findViewById(R.id.close);

        //铭牌车辆
        tvMsg.setTransformationMethod(new InputLowerToUpper());
        if(content!=null){
            tvMsg.setText(content);
            if (!TextUtils.isEmpty(content)) {
                tvMsg.setFocusable(false);
            }
        }

        tvMsg.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            String changedText = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.e("onTextChanged string","start :"+start+"  before: "+before+"  count: "+count);
                //增加字符
                if(before==0&&count>0) {
                    changedText = s.subSequence(start, start + count).toString();
                }else{
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(changedText.equals("")){
                    return;
                }
                //判断新输入的字符是否有汉字或表情符，如果是就删除新输入的，将输入光标定位在最后位置
                //LogUtil.e("afterTextChanged changedText",changedText);
                Pattern p = Pattern.compile(".*[\u4e00-\u9fa5].*|.*[\ud83c\udc00-\ud83c\udfff].*|.*[\ud83d\udc00-\ud83d\udfff].*|.*[\u2600-\u27ff].*");
                //Pattern p = Pattern.compile(".*[\u4e00-\u9fa5].*");
                Matcher m = p.matcher(changedText);
                if(m.matches()){
                    tvMsg.removeTextChangedListener(this);
                    tvMsg.setText(beforeText);
                    tvMsg.setSelection(tvMsg.getText().length());
                    MyToast.showShort("不可输入汉字或表情符");
                    tvMsg.addTextChangedListener(this);
                }
            }
        });

        CustomRippleButton crbCancel =  (CustomRippleButton) view.findViewById(R.id.crb_cancel);
        crbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.showSoftInput(activity,tvMsg);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        final CustomRippleButton crbConfirm =  (CustomRippleButton) view.findViewById(R.id.crb_confirm);
        crbConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(tvMsg.getText().toString())){
                    MyToast.showShort("车辆型号不可为空");
                    return;
                }
                if(okListenter!=null){
                    okListenter.onClick(tvMsg.getText().toString());
                }
                dialog.dismiss();
            }
        });

        tvMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvMsg.setFocusable(true);
                tvMsg.setFocusableInTouchMode(true);
                tvMsg.requestFocus();
                tvMsg.findFocus();
                return false;
            }
        });

    }

    public void setData(String content){
        this.content = content;
    }

    public interface OkListenter{
        public void onClick(String content);
    }

    private OkListenter okListenter;

    public void setOkListenter(OkListenter okListenter){
        this.okListenter = okListenter;
    }
}
