package com.jcpt.jzg.padsystem.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.view.QNumberPicker;

import java.util.Calendar;

/**
 * Created by zealjiang on 2016/12/13 15:40.
 * Email: zealjiang@126.com
 */

public class MonthPickerDialog{

    private Context context;
    private QNumberPicker npYear;
    private QNumberPicker npMonth;
    private String title;
    private int selectedYear;
    private int selectedMonth;
    private boolean laterThanNow = true;
    public MonthPickerDialog(Context context,String title,int selectedYear,int selectedMonth,final boolean laterThanNow) {
        this.context = context;
        this.title = title;
        this.selectedYear = selectedYear;
        this.selectedMonth = selectedMonth;
        this.laterThanNow = laterThanNow;
    }

    public void createDialog(){
        View v = UIUtils.inflate(R.layout.layout_year_month_picker);
        npYear = (QNumberPicker) v.findViewById(R.id.npYear);
        npMonth = (QNumberPicker) v.findViewById(R.id.npMonth);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        Calendar calendar = Calendar.getInstance();
        final int nowYear = calendar.get(Calendar.YEAR);
        final int nowMonth = calendar.get(Calendar.MONTH);
        npYear.setMaxValue(nowYear);
        npYear.setMinValue(nowYear-15);
        npMonth.setMaxValue(12);
        npMonth.setMinValue(1);

        npYear.setValue(selectedYear);
        npMonth.setValue(selectedMonth);
        android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(context);
        builder.setView(v);
        builder.setTitle(title).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int selectYear = npYear.getValue();
                int selectMonth = npMonth.getValue();
                if(!laterThanNow){
                    if(selectYear>nowYear){
                        MyToast.showLong("出厂日期不能晚于当前日期");
                        return;
                    }
                    if(selectYear==nowYear && selectMonth>(nowMonth+1)){
                        MyToast.showLong("出厂日期不能晚于当前日期");
                        return;
                    }
                }
                if(monthPickerOkListenter!=null){
                    monthPickerOkListenter.selectDate(selectYear,selectMonth,selectYear+"-"+selectMonth);
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public interface MonthPickerOkListenter{
        public void selectDate(int year,int month,String yearMonth);
    }

    private MonthPickerOkListenter monthPickerOkListenter;

    public void setMonthPickerOkListenter(MonthPickerOkListenter monthPickerOkListenter){
        this.monthPickerOkListenter = monthPickerOkListenter;
    }
}