package com.jcpt.jzg.padsystem.ui.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.AppManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.utils.DateTool;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


@SuppressLint("NewApi")
public class YearandMonthDateActivity extends Activity {

	private YearandMonthDateActivity context;
	
	@BindView(R.id.date_datepicker) DatePicker date_datepicker;
	@BindView(R.id.date_show_text) TextView date_show_text;
	@BindView(R.id.date_title) TextView date_title;

	
    int win_width ;
    int win_height;
    
    List<String> data;
    
    int curYear;
    int curMonthOfYear;
    int curDayOfMonth;
    
    private String checkDate = "";
    private String requestDate = "";
    @BindView(R.id.all_date_text)
    TextView all_date;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		context = this;
		//隐藏标题 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.date_activity);
		AppManager.getAppManager().addActivity(this);
		ButterKnife.bind(this);
		Intent intent = getIntent();
		boolean isShowAll = intent.getBooleanExtra("isShowAll", true);
		if(!isShowAll){
			all_date.setVisibility(View.GONE);
		}
		date_title.setText(intent.getStringExtra("title"));
		WindowManager m = getWindowManager(); 
		Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
		WindowManager.LayoutParams p = getWindow().getAttributes(); //获取对话框当前的参数值 
		
		Point size = new Point(); 
		d.getSize(size); 
		
		win_width = size.x;
		
		win_height = size.y;
		
		p.height = (int) (win_height * 0.7); //高度设置为屏幕的0.6
		p.width = (int) (win_width * 0.5); //宽度设置为屏幕的0.95
		p.alpha = 1.0f;//设置透明度
		this.getWindow().setAttributes( p);
		
		//设置为true点击区域外消失
		setFinishOnTouchOutside(true);
		init();
	}
	
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    // If we've received a touch notification that the user has touched
	    // outside the app, finish the activity.
	    if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
	      finish();
	      return true;
	    }

	    // Delegate everything else to Activity.
	    return super.onTouchEvent(event);
	  }
	
	
	public void init(){
		//date_datepicker
		//隐藏时间控件的日
		hitDay();
		Calendar calendar=Calendar.getInstance();
        curYear=calendar.get(Calendar.YEAR);
        curMonthOfYear=calendar.get(Calendar.MONTH);
        curDayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        
        checkDate = curYear+"-"+(curMonthOfYear+1);
        
        date_show_text.setText(curYear+"年"+(curMonthOfYear+1)+"月");
        
        date_datepicker.init(curYear, curMonthOfYear, curDayOfMonth, new OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
            	checkDate = year+"-"+(monthOfYear+1);
            	date_show_text.setText(year+"年"+(monthOfYear+1)+"月");
            }
           
        });
	}
	
	private void hitDay() {
		
		 try {  
	            /* 处理android5.0以上的特殊情况 */  
	            if (Build.VERSION.SDK_INT >= 21) {  
	                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");  
	                if (daySpinnerId != 0) {  
	                    View daySpinner = date_datepicker.findViewById(daySpinnerId);  
	                    if (daySpinner != null) {  
	                        daySpinner.setVisibility(View.GONE);  
	                    }  
	                }  
	            } else {  
	                Field[] datePickerfFields = date_datepicker.getClass().getDeclaredFields();  
	                for (Field datePickerField : datePickerfFields) {  
	                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {  
	                        datePickerField.setAccessible(true);  
	                        Object dayPicker = new Object();  
	                        try {  
	                            dayPicker = datePickerField.get(date_datepicker);  
	                        } catch (IllegalAccessException e) {  
	                            e.printStackTrace();  
	                        } catch (IllegalArgumentException e) {  
	                            e.printStackTrace();  
	                        }  
	                        ((View) dayPicker).setVisibility(View.GONE);  
	                    }  
	                }  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		
	}

	@OnClick({R.id.all_date_text,R.id.today_text,R.id.tomorrow_text,R.id.date_cancel_text
		,R.id.date_affirm_text})
	public void onclick(View v){
		switch (v.getId()) {
		case R.id.all_date_text:
			requestDate = "1990-01-01";
			destoryAty(requestDate);
			break;
		case R.id.today_text:
			requestDate = curYear+"-"+(curMonthOfYear+1)+"-"+curDayOfMonth;
			destoryAty(requestDate);
			break;
		case R.id.tomorrow_text:
			requestDate = DateTool.getSpecifiedDayBefore(curYear+"-"+(curMonthOfYear+1)+"-"+curDayOfMonth);
			destoryAty(requestDate);
			break;
		case R.id.date_cancel_text:
			requestDate = "";
			finish();
			break;
		case R.id.date_affirm_text:
			requestDate = checkDate;
			destoryAty(requestDate);
			break;

		default:
			break;
		}
		
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
		{ setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); }
		
        
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}
	

	
	public void destoryAty(String car){
		Intent intent = new Intent();
		intent.putExtra(Constants.DateActivityTowaitActivity, car);
		setResult(Constants.waitActivityToDateActivity, intent);
		finish();
	}


	
}