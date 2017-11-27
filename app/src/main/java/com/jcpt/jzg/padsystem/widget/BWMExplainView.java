package com.jcpt.jzg.padsystem.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.dialog.DayPickerDialog;
import com.jcpt.jzg.padsystem.utils.InputUtil;
import com.jcpt.jzg.padsystem.widget.nodoubleclick.NoDoubleOnclickListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wujj on 2017/10/31.
 * 邮箱：wujj@jingzhengu.com
 * 作用：在BMW/MINI授权经销商外的维修itemView
 */

public class BWMExplainView extends LinearLayout {
    private IMyOnclickLister iMyOnclickLister;
    private Context context;
    private TextView tvRecordDate;
    private EditText etRepairContent;
    private EditText etRepairPrice;

    public BWMExplainView(Context context) {
        this(context, null);
        this.context = context;
    }

    public BWMExplainView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BWMExplainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initviews(context);
    }

    private void initviews(final Context context) {
        View view = View.inflate(context, R.layout.fragment_bmw_explain_item, this);
        tvRecordDate = view.findViewById(R.id.tvRecordDate);
        etRepairContent = view.findViewById(R.id.etRepairContent);
        etRepairPrice = view.findViewById(R.id.etRepairPrice);
        TextView tvDelete = view.findViewById(R.id.tvDelete);

        tvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iMyOnclickLister != null) {
                    iMyOnclickLister.OnDeleteclick();
                }
            }
        });

        etRepairContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.showSoftInput(context, etRepairContent);
            }
        });
        etRepairPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.showSoftInput(context, etRepairPrice);
            }
        });

        tvRecordDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                selectDate(tvRecordDate, "维修时间", false);
            }
        });

        //限制输入表情符
        InputUtil.inputRestrict(etRepairContent, true, false, false, false);
        //只允许输入数字
        InputUtil.inputRestrict(etRepairPrice, true, true, false, true);

        /**
         * 维修金额精确到小数点后两位
         */
        Action1<TextViewTextChangeEvent> action = new Action1<TextViewTextChangeEvent>() {
            @Override
            public void call(TextViewTextChangeEvent event) {
                EditText priceView = (EditText) event.view();
                String price = event.text().toString();
                revert(priceView, price);
            }
        };
        RxTextView.textChangeEvents(etRepairPrice)
                .debounce(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);

    }

    private void revert(EditText view, String price) {
        if (!TextUtils.isEmpty(price)) {
            if (price.startsWith(".")) {
                view.setText("");
                return;
            }
            if (price.startsWith("0") && price.length() > 1 && !".".equals(String.valueOf(price.charAt(1)))) {//以0开头，长度大于1，且第二个字符不是'.'，则置0
                view.setText("0");
                view.setSelection(view.getText().toString().length());
                return;
            }
            if (price.contains(".")) {
                String s1 = price.substring(price.indexOf(".") + 1, price.length());
                if (s1.length() > 2) {//"."之后最多只可输入两个字符
                    int selectionEnd = view.getSelectionEnd();
                    String s = price.substring(0, selectionEnd - 1) + price.substring(selectionEnd, price.length());
                    view.setText(s);
                    view.setSelection(selectionEnd - 1);
                }
            }
        }
    }

    private void selectDate(final TextView textView, String titlePrefix, boolean laterThanNow) {
        int year, month, day;
        String date = textView.getText().toString();
        if (TextUtils.isEmpty(date) || date.length() < 8) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DATE);
        } else {
            String[] ymd = date.split("-");
            year = Integer.valueOf(ymd[0]);
            month = Integer.valueOf(ymd[1]);
            day = Integer.valueOf(ymd[2]);
        }
        DayPickerDialog dayPickerDialog = new DayPickerDialog(context, titlePrefix, year, month, day, laterThanNow);
        dayPickerDialog.createDialog();
        dayPickerDialog.setDayPickerOkListenter(new DayPickerDialog.DayPickerOkListenter() {

            @Override
            public void selectDate(int year, int month, int day, String date) {
                textView.setText(date);
            }
        });
    }

    public void setMyOnclickLister(IMyOnclickLister mIMyOnclickLister) {
        iMyOnclickLister = mIMyOnclickLister;
    }

    public interface IMyOnclickLister {
        void OnDeleteclick();
    }

    //获取维修时间
    public String getRecordDate(){
        return tvRecordDate.getText().toString().trim();
    }

    //主要维修项目
    public String getRepairContent(){
        return etRepairContent.getText().toString().trim();
    }

    //金额
    public String getRepairPrice(){
        return etRepairPrice.getText().toString().trim();
    }

    public TextView getTvRecordDate(){
        return tvRecordDate;
    }

    public TextView getEtRepairContent(){
        return etRepairContent;
    }

    public TextView getEtRepairPrice(){
        return etRepairPrice;
    }
}
