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

public class DayPickerDialog {

    private Context context;
    private QNumberPicker npYear;
    private QNumberPicker npMonth;
    private QNumberPicker npDay;
    private String title;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private boolean laterThanNow = true;
    private int moreCurYear;
    public DayPickerDialog(Context context, String titlePrefix, int selectedYear, int selectedMonth,int selectedDay, final boolean laterThanNow) {
        this.context = context;
        this.title = titlePrefix;
        this.selectedYear = selectedYear;
        this.selectedMonth = selectedMonth;
        this.selectedDay = selectedDay;
        this.laterThanNow = laterThanNow;
    }

    public void createDialog(){
        View v = UIUtils.inflate(R.layout.layout_day_picker);
        npYear = (QNumberPicker) v.findViewById(R.id.npYear);
        npMonth = (QNumberPicker) v.findViewById(R.id.npMonth);
        npDay = (QNumberPicker) v.findViewById(R.id.npDay);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        Calendar calendar = Calendar.getInstance();
        final int nowYear = calendar.get(Calendar.YEAR);
        final int nowMonth = calendar.get(Calendar.MONTH);
        final int nowDay = calendar.get(Calendar.DATE);
        if(laterThanNow){
            moreCurYear = 6;
        }
        npYear.setMaxValue(nowYear+moreCurYear);
        npYear.setMinValue(nowYear-15);
        npMonth.setMaxValue(12);
        npMonth.setMinValue(1);

        npYear.setValue(selectedYear);
        npMonth.setValue(selectedMonth);
        reSetNpDay();
        npDay.setValue(selectedDay);

        npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal!=newVal) {
                    selectedYear = newVal;
                    reSetNpDay();
                }
            }
        });

        npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal!=newVal){
                    selectedMonth = newVal;
                    reSetNpDay();
                }
            }
        });

        android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(context);
        builder.setView(v);
        builder.setTitle("请选择"+title).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectYear = npYear.getValue();
                int selectMonth = npMonth.getValue();
                int selectDay = npDay.getValue();
                if(!laterThanNow){
                    if(selectYear>nowYear){
                        MyToast.showLong(title+"不能晚于当前日期");
                        return;
                    }
                    if(selectYear==nowYear && selectMonth>(nowMonth+1)){
                        MyToast.showLong(title+"不能晚于当前日期");
                        return;
                    }
                    if(selectYear==nowYear && selectMonth==(nowMonth+1)&& selectDay>nowDay){
                        MyToast.showLong(title+"不能晚于当前日期");
                        return;
                    }
                }
                if(dayPickerOkListenter!=null){
                    dayPickerOkListenter.selectDate(selectYear,selectMonth,selectDay,selectYear+"-"+selectMonth+"-"+selectDay);
                }
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }


    private void reSetNpDay(){
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR,selectedYear);
        ca.set(Calendar.MONTH,selectedMonth-1);
        ca.set(Calendar.DAY_OF_MONTH,1);
        int lastDay = ca.getActualMaximum(Calendar.DATE);

        npDay.setMaxValue(lastDay);
        npDay.setMinValue(1);

    }

    public interface DayPickerOkListenter{
        public void selectDate(int year, int month, int day, String date);
    }

    private DayPickerOkListenter dayPickerOkListenter;

    public void setDayPickerOkListenter(DayPickerOkListenter dayPickerOkListenter){
        this.dayPickerOkListenter = dayPickerOkListenter;
    }
}