package com.jcpt.jzg.padsystem.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.dialog.DayPickerDialog;
import com.jcpt.jzg.padsystem.view.QNumberPicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {

	private static String mYear;
	private static String mMonth;
	private static String mDay;
	private static String mWay;
	private static String mHour;
	private static String mMinute;
	/***
	 * yyyy-MM-dd HH:mm:ss
	 *-
	private static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";


	/**
	 * 获取时间2014-01-12
	 * 
	 * @return
	 */
	public static String StringDateTime() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd kk:mm:ss", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间2014/01/12
	 * 
	 * @return
	 */
	public static String StringDate() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间14:30
	 * 
	 * @return
	 */
	public static String StringTime() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("kk:mm", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间年
	 * 
	 * @return
	 */
	public static String StringTimeYear() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("yyyy", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间月
	 * 
	 * @return
	 */
	public static String StringTimeMonth() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("MM", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间日
	 * 
	 * @return
	 */
	public static String StringTimeDay() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("dd", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间小时14
	 * 
	 * @return
	 */
	public static String StringTimeHour() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("kk", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间分钟30
	 * 
	 * @return
	 */
	public static String StringTimeMin() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("mm", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间1月30日
	 * 
	 * @return
	 */
	public static String StringData() {
		final Calendar c = Calendar.getInstance();
		// c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		mMinute = String.valueOf(c.get(Calendar.MINUTE));

		return mMonth + "月" + mDay + "日";
	}

	/**
	 * 获取时间-->星期日
	 * 
	 * @return
	 */
	public static String StringWeek() {
		final Calendar c = Calendar.getInstance();
		// c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		mMinute = String.valueOf(c.get(Calendar.MINUTE));

		if ("1".equals(mWay)) {
			mWay = "天";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		// return mYear + "年" + mMonth + "月" + mDay+"日"+"/星期"+mWay;
		return "星期" + mWay;
	}

	/***
	 * 两个日期相差多少秒
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getTimeDelta(Date date1, Date date2) {
		long timeDelta = (date1.getTime() - date2.getTime()) / 1000;// 单位是秒
		int secondsDelta = timeDelta > 0 ? (int) timeDelta : (int) Math
				.abs(timeDelta);
		return secondsDelta;
	}

	/***
	 * 两个日期相差多少秒
	 * 
	 * @param dateStr1
	 *            :yyyy-MM-dd HH:mm:ss
	 * @param dateStr2
	 *            :yyyy-MM-dd HH:mm:ss
	 */
	public static int getTimeDelta(String dateStr1, String dateStr2) {
		Date date1 = parseDateByPattern(dateStr1, "yyyy-MM-dd HH:mm:ss");
		Date date2 = parseDateByPattern(dateStr2, "yyyy-MM-dd HH:mm:ss");
		return getTimeDelta(date1, date2);
	}

	public static Date parseDateByPattern(String dateStr, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}
	
	/**
	 * 
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String DateFormat(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		String day = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return day;
	}
	/**
	 * 
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String YearandMonthFormat(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		String day = new SimpleDateFormat("yyyy-MM").format(c.getTime());
		return day;
	}

	/***
	 *
	 * @param title dialog标题
	 * @param tvContent 回显TextView
	 * @param maxYear 最大年
	 * @param minYear 最小年
	 * @param laterThanNow 是否能晚于当前时间
	 */
	public void selectYearMonth(Context context, final String title, final TextView tvContent, int maxYear, int minYear, final boolean laterThanNow){
		View v = UIUtils.inflate(R.layout.layout_year_month_picker);
		final QNumberPicker npYear = (QNumberPicker) v.findViewById(R.id.npYear);
		final QNumberPicker npMonth = (QNumberPicker) v.findViewById(R.id.npMonth);
		npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		Calendar calendar = Calendar.getInstance();
		final int nowYear = calendar.get(Calendar.YEAR);
		final int nowMonth = calendar.get(Calendar.MONTH);
		npYear.setMaxValue(maxYear);
		npYear.setMinValue(minYear);
		npMonth.setMaxValue(12);
		npMonth.setMinValue(1);
		String ym = tvContent.getText().toString().trim();
		String[] arr = ym.split("-");
		int selectedYear = nowYear;
		int selectedMonth = nowMonth+1;
		if(arr.length==2){
			selectedYear = Integer.valueOf(arr[0]);
			selectedMonth = Integer.valueOf(arr[1]);
		}
		npYear.setValue(selectedYear);
		npMonth.setValue(selectedMonth);
		android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(context);
		builder.setView(v);
		builder.setTitle("请选择"+title).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				int selectYear = npYear.getValue();
				int selectMonth = npMonth.getValue();
				if(!laterThanNow){
					if(selectYear>nowYear){
						MyToast.showLong(title+"不能晚于当前日期");
						return;
					}
					if(selectYear==nowYear && selectMonth>(nowMonth+1)){
						MyToast.showLong(title+"不能晚于当前日期");
						return;
					}
				}
				tvContent.setText(selectYear+"-"+selectMonth);

			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create().show();
	}


	/**
	 * 滚轮日历
	 * @param textView 控件
	 * @param titlePrefix 如"开票日期"
	 * @param laterThanNow 能否选择今天之后的日期 true表示可以，false表示不可以
	 * @author zealjiang
	 * @time 2017/1/16 13:52
	 */
	public void selectDate(Context context,final TextView textView,final String titlePrefix,final boolean laterThanNow){
		int year,month,day;
		String date = textView.getText().toString();
		if(TextUtils.isEmpty(date)||date.length()<8){
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH)+1;
			day = calendar.get(Calendar.DATE);
		}else {
			String[] ymd = date.split("-");
			year = Integer.valueOf(ymd[0]);
			month = Integer.valueOf(ymd[1]);
			day = Integer.valueOf(ymd[2]);
		}
		DayPickerDialog dayPickerDialog = new DayPickerDialog(context, titlePrefix,year,month,day,laterThanNow);
		dayPickerDialog.createDialog();
		dayPickerDialog.setDayPickerOkListenter(new DayPickerDialog.DayPickerOkListenter(){

			@Override
			public void selectDate(int year, int month,int day, String date) {
				textView.setText(date);
			}
		});
	}

	/**
	 * 如果日期长度超过10位，截取前10位
	 *
	 * @return
	 */
	public String dateSplit(String date) {
		if (TextUtils.isEmpty(date)) {
			return date;
		} else if (date.length() > 10) {
			return date.substring(0, 10);
		} else {
			return date;
		}
	}

	/**
	 * 如果日期长度超过length位，截取前length位
	 *
	 * @return
	 */
	public String dateSplit(String date,int length){
		if(TextUtils.isEmpty(date)){
			return date;
		}else if(date.length()>length){
			return date.substring(0,length);
		}else{
			return date;
		}
	}
}
