package com.jcpt.jzg.padsystem.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * 自定义弹出框显示
 * @author jzg
 * @Date 2015-05-12
 */
public class ShowMsgDialog extends Dialog {

	private ShowMsgDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	/**
	 * 定义的Dialog.   theme 调用R.style.dialog
	 * @param context
	 * @param theme
	 */
	public ShowMsgDialog(Context context, int theme) {
		super(context, theme);
	}

	private ShowMsgDialog(Context context) {
		super(context);
	}

	/**
	 * 这是兼容的 AlertDialog 带取消和确定按钮
	 * @author zealjiang
	 * @time 2016/8/11 11:31
	 */
	public static AlertDialog.Builder showMaterialDialog2Btn(Context context, String title, String content, OnClickListener cancelclickListener
			, OnClickListener okclickListener) {
		  /*
		  这里使用了 android.support.v7.app.AlertDialog.Builder
		  可以直接在头部写 import android.support.v7.app.AlertDialog
		  那么下面就可以写成 AlertDialog.Builder
		  */
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setNegativeButton("取消", cancelclickListener);
		builder.setPositiveButton("确定", okclickListener);
		builder.setCancelable(false);
		builder.show();
		return builder;
	}

	/**
	 * 这是兼容的 AlertDialog 带取消和确定按钮
	 * @author zealjiang
	 * @time 2016/8/11 11:31
	 */
	public static AlertDialog.Builder showMaterialDialog2Btn(Context context, String title, String content, OnClickListener cancelclickListener
			, OnClickListener okclickListener,String cancel,String sure) {
		  /*
		  这里使用了 android.support.v7.app.AlertDialog.Builder
		  可以直接在头部写 import android.support.v7.app.AlertDialog
		  那么下面就可以写成 AlertDialog.Builder
		  */
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setNegativeButton((TextUtils.isEmpty(cancel))?"取消":cancel, cancelclickListener);
		builder.setPositiveButton((TextUtils.isEmpty(sure))?"确定":sure, okclickListener);
		builder.setCancelable(false);
		builder.show();
		return builder;
	}

	/**
	 * 这是兼容的 AlertDialog
	 */

	/**
	 * 这是兼容的 AlertDialog 不带取消和确定按钮
	 * @author zealjiang
	 * @time 2016/8/11 11:31
	 */
	public static AlertDialog.Builder showMaterialDialogNoBtn(Activity activity, String title, String content) {
		  /*
		  这里使用了 android.support.v7.app.AlertDialog.Builder
		  可以直接在头部写 import android.support.v7.app.AlertDialog
		  那么下面就可以写成 AlertDialog.Builder
		  */
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setCancelable(true);
		builder.show();
		return builder;
	}

}
