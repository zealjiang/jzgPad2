package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.PreventDoubleClickUtil;
import com.jcpt.jzg.padsystem.vo.BMWExplainBean;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.widget.BWMExplainView;
import com.jcpt.jzg.padsystem.widget.BmwOtherCheckItemView;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.nodoubleclick.NoDoubleOnclickListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by wujj on 2017/10/25.
 * 邮箱：wujj@jingzhengu.com
 * 作用：补充说明
 */

public class BMWExplainFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.etBuy)
    EditText etBuy;
    @BindView(R.id.etSale)
    EditText etSale;
    @BindView(R.id.itemView_yes_or_no)
    BmwOtherCheckItemView itemViewYesOrNo;
    @BindView(R.id.ll_repair_record)
    LinearLayout llRepairRecord;
    @BindView(R.id.ll_item)
    LinearLayout llItem;
    @BindView(R.id.btnSubmit)
    CustomRippleButton btnSubmit;
    @BindView(R.id.viewLine)
    View viewLine;
    private BMWExplainBean bmwExplainBean;
    private SubmitModel submitModel;

    @Override
    protected void initData() {
        //查询此条缓存是否存在
        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        boolean isExist = DBManager.getInstance().isExist(Constants.DATA_TYPE_BMW_EXPLAIN, taskId, PadSysApp.getUser().getUserId());
        DBManager.getInstance().closeDB();
        if (isExist) {
            List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_BMW_EXPLAIN, PadSysApp.getUser().getUserId());
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (list.get(i).getDataType().equals(Constants.DATA_TYPE_BMW_EXPLAIN)) {
                        String json = list.get(i).getJson();
                        if (!TextUtils.isEmpty(json)) {
                            bmwExplainBean = new Gson().fromJson(json, BMWExplainBean.class);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.fragment_explain_bmw, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        llRepairRecord.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    protected void setView() {
        showData();
        initListener();
    }

    //回显数据
    private void showData() {
        if (bmwExplainBean != null) {

            etBuy.setText(bmwExplainBean.getPriceFront());
            etSale.setText(bmwExplainBean.getPriceAfter());
            int ifHasRepair = bmwExplainBean.getIfHasRepair();
            switch (ifHasRepair) {
                case 0:
                    itemViewYesOrNo.getRbtnYes().setChecked(true);
                    llRepairRecord.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    itemViewYesOrNo.getRbtnNo().setChecked(true);
                    llRepairRecord.setVisibility(View.GONE);
                    break;
            }

            //维修记录
            if (ifHasRepair == 0) {//有维修记录
                llRepairRecord.setVisibility(View.VISIBLE);
                llItem.setVisibility(View.VISIBLE);
                List<LinkedHashMap<String, String>> spOutRepairRecordList = bmwExplainBean.getSpOutRepairRecordList();
                if (spOutRepairRecordList != null && spOutRepairRecordList.size() > 0) {
                    viewLine.setVisibility(View.GONE);
                    for (int i = 0; i < spOutRepairRecordList.size(); i++) {
                        final BWMExplainView bwmExplainView = new BWMExplainView(getActivity());
                        LinkedHashMap<String, String> map = spOutRepairRecordList.get(i);
                        String recordDate = map.get("RecordDate");
                        String repairContent = map.get("RepairContent");
                        String repairPrice = map.get("RepairPrice");
                        bwmExplainView.getTvRecordDate().setText(recordDate);
                        bwmExplainView.getEtRepairContent().setText(repairContent);
                        bwmExplainView.getEtRepairPrice().setText(repairPrice);
                        llItem.addView(bwmExplainView);

                        //删除监听
                        bwmExplainView.setMyOnclickLister(new BWMExplainView.IMyOnclickLister() {
                            @Override
                            public void OnDeleteclick() {
                                llItem.removeView(bwmExplainView);
                                if (llItem.getChildCount() == 0) {
                                    llItem.setVisibility(View.GONE);
                                    viewLine.setVisibility(View.VISIBLE);
                                } else {
                                    llItem.setVisibility(View.VISIBLE);
                                    viewLine.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                } else {
                    llItem.setVisibility(View.GONE);
                    viewLine.setVisibility(View.VISIBLE);
                }
            } else {
                llRepairRecord.setVisibility(View.GONE);
                llItem.removeAllViews();
                llItem.setVisibility(View.GONE);
            }

        }
    }

    @OnClick({R.id.etBuy, R.id.etSale, R.id.tvAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etBuy:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etBuy);
                break;
            case R.id.etSale:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etSale);
                break;
            case R.id.tvAdd:
                final BWMExplainView bwmExplainView = new BWMExplainView(getActivity());
                llItem.setVisibility(View.VISIBLE);
                llItem.addView(bwmExplainView);
                viewLine.setVisibility(View.GONE);
                bwmExplainView.setMyOnclickLister(new BWMExplainView.IMyOnclickLister() {
                    @Override
                    public void OnDeleteclick() {
                        llItem.removeView(bwmExplainView);
                        if (llItem.getChildCount() == 0) {
                            llItem.setVisibility(View.GONE);
                            viewLine.setVisibility(View.VISIBLE);
                        } else {
                            llItem.setVisibility(View.VISIBLE);
                            viewLine.setVisibility(View.GONE);
                        }
                    }
                });
                break;
        }
    }

    private void initListener() {
        itemViewYesOrNo.setMyOnclickLister(new BmwOtherCheckItemView.IMyOnclickLister() {
            @Override
            public void OnOkclick() {
                llRepairRecord.setVisibility(View.VISIBLE);
                llItem.setVisibility(View.VISIBLE);
                if (llItem.getChildCount() == 0) {
                    final BWMExplainView bwmExplainView = new BWMExplainView(getActivity());
                    llItem.addView(bwmExplainView, 0);
                    viewLine.setVisibility(View.GONE);
                    bwmExplainView.setMyOnclickLister(new BWMExplainView.IMyOnclickLister() {
                        @Override
                        public void OnDeleteclick() {
                            llItem.removeView(bwmExplainView);
                            if (llItem.getChildCount() == 0) {
                                llItem.setVisibility(View.GONE);
                                viewLine.setVisibility(View.VISIBLE);
                            } else {
                                llItem.setVisibility(View.VISIBLE);
                                viewLine.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }

            @Override
            public void OnCancleclick() {
                if (llRepairRecord.getVisibility() == View.VISIBLE && llItem.getVisibility() == View.VISIBLE) {
                    showDeleteDialog();
                } else {
                    llRepairRecord.setVisibility(View.GONE);
                }
            }
        });
        /**
         * 精确到小数点后两位
         */
        Action1<TextViewTextChangeEvent> action = new Action1<TextViewTextChangeEvent>() {
            @Override
            public void call(TextViewTextChangeEvent event) {
                EditText priceView = (EditText) event.view();
                String price = event.text().toString();
                revert(priceView, price);
            }
        };
        RxTextView.textChangeEvents(etBuy)
                .debounce(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
        RxTextView.textChangeEvents(etSale)
                .debounce(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);

        //防止重复点击提交
        btnSubmit.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //检查数据完整性
                boolean checkDataCompleteAndSave = ((BMWDetectMainActivity) getActivity()).checkDataCompleteAndSave();
                if (checkDataCompleteAndSave) {
                    if (PadSysApp.networkAvailable) {
                        if (PreventDoubleClickUtil.noDoubleClick()) {
                            //收车价
                            String BToBPrice = submitModel.getBmwTaskExModel().getBToBPrice();
                            //售车价
                            String OCUBToBPrice = submitModel.getBmwTaskExModel().getOCUBToBPrice();
                            if (BToBPrice.equals("0.01") || OCUBToBPrice.equals("0.02")) {//大事故车
                                ((BMWDetectMainActivity) getActivity()).checkAndSubmit(2, "0");
                            } else {
                                ((BMWDetectMainActivity) getActivity()).checkAndSubmit(0, "0");
                            }
                        }
                    } else {
                        MyToast.showShort("没有网络");
                    }
                }
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("变更选项后，已填写的维修记录将被清空，是否继续？");
        builder.setCancelable(false);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除所有维修数据记录
                dialog.dismiss();
                llItem.removeAllViews();
                llRepairRecord.setVisibility(View.GONE);

            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //不删除维修数据记录
                itemViewYesOrNo.getRbtnYes().setChecked(true);
                dialog.dismiss();
            }
        });

        builder.create().show();
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
                String s2 = price.substring(0, price.indexOf("."));
                if (s2.length() > 3) {//"."前面最多只可输入3个字符
                    int selectionEnd = view.getSelectionEnd();
                    String s = price.substring(0, selectionEnd - 1) + price.substring(selectionEnd, price.length());
                    view.setText(s);
                    view.setSelection(selectionEnd - 1);
                }
                if (s1.length() > 2) {//"."之后最多只可输入两个字符
                    int selectionEnd = view.getSelectionEnd();
                    String s = price.substring(0, selectionEnd - 1) + price.substring(selectionEnd, price.length());
                    view.setText(s);
                    view.setSelection(selectionEnd - 1);
                }
            } else {
                if (price.length() > 3) {
                    String result = price.substring(0, 3);
                    view.setText(result);
                    view.setSelection(result.length());
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        savedata();
    }

    //保存数据
    private void savedata() {
        if (bmwExplainBean == null) {
            bmwExplainBean = new BMWExplainBean();
        }
        if (submitModel == null) {
            submitModel = ((BMWDetectMainActivity) getActivity()).getSubmitModel();
        }
        //整备前价格
        bmwExplainBean.setPriceFront(etBuy.getText().toString().trim());
        submitModel.getBmwTaskExModel().setBToBPrice(etBuy.getText().toString().trim());

        //整备后价格
        bmwExplainBean.setPriceAfter(etSale.getText().toString().trim());
        submitModel.getBmwTaskExModel().setOCUBToBPrice(etSale.getText().toString().trim());

        //是否有维修
        int rbtnClickPos = itemViewYesOrNo.getRbtnClickPos();
        bmwExplainBean.setIfHasRepair(rbtnClickPos);
        switch (rbtnClickPos) {
            case 0:
                submitModel.getBmwTaskExModel().setIsOutRepair("1");
                break;
            case 1:
                submitModel.getBmwTaskExModel().setIsOutRepair("0");
                break;
            default:
                submitModel.getBmwTaskExModel().setIsOutRepair("-1");
        }

        //维修记录
        List<LinkedHashMap<String, String>> spOutRepairRecordList = new ArrayList<>();
        //防止提交时重复add数据进spOutRepairRecordList中
        if (spOutRepairRecordList != null && spOutRepairRecordList.size() > 0) {
            spOutRepairRecordList.clear();
        }
        LinkedHashMap<String, String> map = null;

        for (int i = 0; i < llItem.getChildCount(); i++) {
            BWMExplainView childAt = (BWMExplainView) llItem.getChildAt(i);
            String recordDate = childAt.getRecordDate();
            String repairContent = childAt.getRepairContent();
            String repairPrice = childAt.getRepairPrice();

            map = new LinkedHashMap<>();
            map.put("TaskId", ((BMWDetectMainActivity) getActivity()).getTaskid());
            map.put("RecordDate", recordDate);
            map.put("RepairContent", repairContent);
            map.put("RepairPrice", repairPrice);
            spOutRepairRecordList.add(map);
        }
        bmwExplainBean.setSpOutRepairRecordList(spOutRepairRecordList);
        submitModel.setSpOutRepairRecordList(spOutRepairRecordList);
        ((BMWDetectMainActivity) getActivity()).setSubmitModel(submitModel);
        //保存到数据库
        String json = new Gson().toJson(bmwExplainBean);
        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_BMW_EXPLAIN, taskId, PadSysApp.getUser().getUserId(), json);
    }

    //检查不可为空项是否都已填写并保存
    public boolean checkAndSaveData() {
        if (TextUtils.isEmpty(etBuy.getText())) {
            MyToast.showShort("请输入整备前价格");
            return false;
        }
        String strBuy = etBuy.getText().toString();
        float buyPrice = Float.parseFloat(strBuy);
        if (buyPrice == 0) {
            MyToast.showShort("整备前价格不能为0");
            return false;
        }
        if (buyPrice > 999.98) {
            MyToast.showShort("整备前价格不可超过999.98万元");
            return false;
        }
        if (TextUtils.isEmpty(etSale.getText())) {
            MyToast.showShort("请输入按OCU标准整备后价格");
            return false;
        }
        String strSale = etSale.getText().toString();
        float salePrice = Float.parseFloat(strSale);
        if (salePrice == 0) {
            MyToast.showShort("按OCU标准整备后价格不能为0");
            return false;
        }
        if (salePrice > 999.99) {
            MyToast.showShort("按OCU标准整备后价格不可超过999.99万元");
            return false;
        }
        if (buyPrice >= salePrice) {
            MyToast.showShort("按OCU标准整备后价格必须大于整备前价格");
            return false;
        }
        if (itemViewYesOrNo.getRbtnClickPos() == -1) {
            MyToast.showLong("补充说明填写不完整");
            return false;
        }
        if (llRepairRecord.getVisibility() == View.VISIBLE) {//维修记录可见
            for (int i = 0; i < llItem.getChildCount(); i++) {
                BWMExplainView childAt = (BWMExplainView) llItem.getChildAt(i);
                String recordDate = childAt.getRecordDate();
                String repairContent = childAt.getRepairContent();
                String repairPrice = childAt.getRepairPrice();
                if (TextUtils.isEmpty(recordDate) || TextUtils.isEmpty(repairContent) || TextUtils.isEmpty(repairPrice)) {
                    MyToast.showLong("补充说明填写不完整");
                    return false;
                }
            }
        }
        savedata();
        return true;
    }
}
