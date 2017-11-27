package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding.view.RxView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.mvpview.ITaskStatus;
import com.jcpt.jzg.padsystem.presenter.TaskStatusPresenter;
import com.jcpt.jzg.padsystem.ui.fragment.MyTaskFragment;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.vo.TaskItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.jcpt.jzg.padsystem.app.PadSysApp.getUser;


public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> implements ITaskStatus{


    Context context;
    List<TaskItem> mdatas;
    MyTaskFragment myTaskFragment;
    List<String> list = new ArrayList<>();
    private boolean isSuperModify;//如果是超级用户，又是修改列表查询出的数据则为true,反之为false
    private TaskStatusPresenter taskStatusPresenter;
    private TaskItem curTaskItem;
    private int curPos;

    public void setDatas(List<TaskItem> datas) {
        this.mdatas = datas;
    }

    public void setIsSuperModify(boolean isSuperModify) {
        this.isSuperModify = isSuperModify;
    }

    @Override
    public MyTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_task, parent, false);
        MyTaskAdapter.ViewHolder viewHolder = new MyTaskAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyTaskAdapter.ViewHolder holder, final int position) {

        final TaskItem task = mdatas.get(position);
        holder.ivCar.setImageURI(Uri.parse(TextUtils.isEmpty(task.getPicPath()) ? "" : task.getPicPath()));
        holder.ivCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (4 == task.getStatus() || 6 == task.getStatus() || 2 == task.getStatus() || 8 == task.getStatus()) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onTtemPhoto(position);
                    }
                }
            }
        });
        holder.carName.setText(TextUtils.isEmpty(task.getCarName()) ? "暂无车型" : task.getCarName());
        if (TextUtils.isEmpty(task.getUpdateTime())) {
            holder.tvDetectTime.setText("更新时间: 暂无");
        } else {
            holder.tvDetectTime.setText("更新时间: " + task.getUpdateTime());
        }
        int isRecheck = task.getIsRecheck();
        switch (isRecheck) {
            //默认
            case 0:
                holder.tvFirstCheck.setVisibility(View.GONE);
                holder.tvSecondCheck.setVisibility(View.GONE);
                break;
            //初检
            case 1:
                holder.tvFirstCheck.setVisibility(View.VISIBLE);
                holder.tvSecondCheck.setVisibility(View.GONE);
                break;
            //复检
            case 2:
                holder.tvFirstCheck.setVisibility(View.GONE);
                holder.tvSecondCheck.setVisibility(View.VISIBLE);
                break;
        }
        int isPriority = task.getIsPriority();
        switch (isPriority) {
            //默认
            case 0:
                holder.tvPriority.setVisibility(View.GONE);
                break;
            //加急
            case 1:
                holder.tvPriority.setVisibility(View.VISIBLE);
                break;
        }
        if (TextUtils.isEmpty(task.getYXOrderNo())) {//无机构单号
            if (TextUtils.isEmpty(task.getCreateUserName())) {//无下单账号
                holder.tv1.setText("评估编号：" + task.getOrderNo());
                holder.tv2.setText("下单时间：" + task.getCreateTime());
                holder.tv3.setText("产品类型：" + task.getProductType());
                holder.tv4.setVisibility(View.GONE);
                holder.tv8.setVisibility(View.GONE);
                holder.tv5.setText(task.getOrgName() + "：" + task.getCityName());
                holder.tv6.setText(TextUtils.isEmpty(task.getVinCode()) ? "VIN：暂无" : "VIN：" + task.getVinCode());
                if (TextUtils.isEmpty(task.getLinkManName()) || TextUtils.isEmpty(task.getLinkMobile())) {
                    holder.tv7.setText("");
                } else if (!TextUtils.isEmpty(task.getLinkManName()) && !TextUtils.isEmpty(task.getLinkMobile())) {
                    holder.tv7.setText(task.getLinkManName() + "：" + task.getLinkMobile());
                }
            } else {//有下单账号
                holder.tv1.setText("评估编号：" + task.getOrderNo());
                holder.tv2.setText("下单时间：" + task.getCreateTime());
                holder.tv3.setText(TextUtils.isEmpty(task.getVinCode()) ? "VIN：暂无" : "VIN：" + task.getVinCode());
                if (TextUtils.isEmpty(task.getLinkManName()) || TextUtils.isEmpty(task.getLinkMobile())) {
                    holder.tv4.setVisibility(View.GONE);
                    holder.tv4.setText("");
                } else if (!TextUtils.isEmpty(task.getLinkManName()) && !TextUtils.isEmpty(task.getLinkMobile())) {
                    holder.tv4.setVisibility(View.VISIBLE);
                    holder.tv4.setText(task.getLinkManName() + "：" + task.getLinkMobile());
                }
                holder.tv5.setText(task.getOrgName() + "：" + task.getCityName());
                holder.tv6.setText("下单账号：" + task.getCreateUserName());
                holder.tv7.setText("产品类型：" + task.getProductType());
                holder.tv8.setVisibility(View.GONE);
            }
        } else {//有机构单号
            if (TextUtils.isEmpty(task.getCreateUserName())) {//无下单账号
                holder.tv1.setText("评估编号：" + task.getOrderNo());
                holder.tv2.setText(task.getOrgName() + "：" + task.getCityName());
                holder.tv3.setText(TextUtils.isEmpty(task.getVinCode()) ? "VIN：暂无" : "VIN：" + task.getVinCode());
                if (TextUtils.isEmpty(task.getLinkManName()) || TextUtils.isEmpty(task.getLinkMobile())) {
                    holder.tv4.setVisibility(View.GONE);
                    holder.tv4.setText("");
                } else if (!TextUtils.isEmpty(task.getLinkManName()) && !TextUtils.isEmpty(task.getLinkMobile())) {
                    holder.tv4.setVisibility(View.VISIBLE);
                    holder.tv4.setText(task.getLinkManName() + "：" + task.getLinkMobile());
                }
                holder.tv5.setText("机构单号：" + task.getYXOrderNo());
                holder.tv6.setText("下单时间：" + task.getCreateTime());
                holder.tv7.setText("产品类型：" + task.getProductType());
                holder.tv8.setVisibility(View.GONE);
            } else {//有下单账号
                holder.tv4.setVisibility(View.VISIBLE);
                holder.tv1.setText("评估编号：" + task.getOrderNo());
                holder.tv2.setText(task.getOrgName() + "：" + task.getCityName());
                holder.tv3.setText("下单时间：" + task.getCreateTime());
                holder.tv4.setText("产品类型：" + task.getProductType());
                holder.tv5.setText("机构单号：" + task.getYXOrderNo());
                holder.tv6.setText("下单账号：" + task.getCreateUserName());
                holder.tv7.setText(TextUtils.isEmpty(task.getVinCode()) ? "VIN：暂无" : "VIN：" + task.getVinCode());
                holder.tv8.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(task.getLinkManName()) || TextUtils.isEmpty(task.getLinkMobile())) {
                    holder.tv8.setText("");
                } else if (!TextUtils.isEmpty(task.getLinkManName()) && !TextUtils.isEmpty(task.getLinkMobile())) {
                    holder.tv8.setText(task.getLinkManName() + "：" + task.getLinkMobile());
                }
            }
        }
        holder.tvAddress.setText("地址：" + task.getAddress());
        if (TextUtils.isEmpty(task.getDes())){
            holder.tvDes.setVisibility(View.GONE);
        }else {
            holder.tvDes.setVisibility(View.VISIBLE);
            holder.tvDes.setText("备注：" + task.getDes());
        }

        holder.btn1.setVisibility(View.GONE);
        holder.btn2.setVisibility(View.GONE);
        holder.btn3.setVisibility(View.GONE);
        holder.tvLookCause.setVisibility(View.GONE);

        if (1 == task.getStatus()) {
            holder.llayout_task.setVisibility(View.VISIBLE);
            holder.tvPhingguStatus.setVisibility(View.VISIBLE);
            holder.tvAlreadySign.setVisibility(View.GONE);
            holder.tvPhingguStatus.setText("未提交");
            holder.btn1.setText("退回");
            holder.btn1.setVisibility(View.VISIBLE);
            holder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(position, Constants.BACK);
                    }
                }
            });
            if (DBManager.getInstance().isExistTaskUpload(task.getTaskId() + "", PadSysApp.getUser().getUserId(), "File")) {
                holder.btn2.setText("继续上传");
            } else if (DBManager.getInstance().isExistTask(task.getTaskId() + "")) {
                holder.btn2.setText("继续检测");
            } else {
                holder.btn2.setText("开始检测");
            }

            holder.btn2.setVisibility(View.VISIBLE);

            RxView.clicks(holder.btn2).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (mOnClickListener != null) {
                        curPos = position;
                        curTaskItem = mdatas.get(position);
                        if (taskStatusPresenter == null) {
                            taskStatusPresenter = new TaskStatusPresenter(MyTaskAdapter.this);
                        }
                        taskStatusPresenter.GetTaskStatus(String.valueOf(task.getTaskId()));
                    }
                }
            });

            //超级用户修改列表进入可修改
            if (isSuperModify) {
                holder.btn3.setText("修改");
                holder.btn3.setVisibility(View.VISIBLE);
                holder.btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnClickListener != null) {
                            mOnClickListener.onItemClick(position, Constants.MODIFICATION);
                        }
                    }
                });
            }

        } else if (4 == task.getStatus()) {//被驳回

            holder.llayout_task.setVisibility(View.GONE);
            holder.tvPhingguStatus.setVisibility(View.VISIBLE);
            holder.tvAlreadySign.setVisibility(View.GONE);
            holder.tvPhingguStatus.setText("被驳回");
            holder.btn1.setText("退回");
            holder.btn1.setVisibility(View.GONE);
            holder.btn2.setText("查看报告");
            holder.btn2.setVisibility(View.VISIBLE);
            holder.btn3.setText("修改");
            holder.btn3.setVisibility(View.VISIBLE);
            holder.tvLookCause.setVisibility(View.VISIBLE);
            holder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(position, Constants.BACK);
                    }
                }
            });
            holder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(position, Constants.CHECKREPORT);
                    }
                }
            });
            holder.btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(position, Constants.MODIFICATION);
                    }
                }
            });
            holder.tvLookCause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(position, Constants.CHECKREASON);
                    }
                }
            });
        } else if (6 == task.getStatus()) {
            holder.llayout_task.setVisibility(View.VISIBLE);
            holder.tvPhingguStatus.setVisibility(View.GONE);
            holder.tvAlreadySign.setVisibility(View.VISIBLE);
            holder.tvPhingguStatus.setText("已签收");
            holder.btn2.setText("查看报告");
            holder.btn2.setVisibility(View.VISIBLE);
            holder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(position, Constants.CHECKREPORT);
                    }
                }
            });

            //超级用户修改列表进入可修改
            if (isSuperModify) {
                holder.btn3.setText("修改");
                holder.btn3.setVisibility(View.VISIBLE);
                holder.btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnClickListener != null) {
                            mOnClickListener.onItemClick(position, Constants.MODIFICATION);
                        }
                    }
                });
            }
        } else if (2 == task.getStatus()) {
            holder.llayout_task.setVisibility(View.VISIBLE);
            holder.tvPhingguStatus.setVisibility(View.VISIBLE);
            holder.tvAlreadySign.setVisibility(View.GONE);
            holder.tvPhingguStatus.setText("待特批");
            holder.btn2.setText("查看报告");
            holder.btn2.setVisibility(View.VISIBLE);
            holder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(position, Constants.CHECKREPORT);
                    }
                }
            });

            //超级用户修改列表进入可修改
            if (isSuperModify) {
                holder.btn3.setText("修改");
                holder.btn3.setVisibility(View.VISIBLE);
                holder.btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnClickListener != null) {
                            mOnClickListener.onItemClick(position, Constants.MODIFICATION);
                        }
                    }
                });
            }
        }else if (8 == task.getStatus()){
            holder.llayout_task.setVisibility(View.VISIBLE);
            holder.tvPhingguStatus.setVisibility(View.VISIBLE);
            holder.tvAlreadySign.setVisibility(View.GONE);
            holder.tvPhingguStatus.setText("已提交");
            holder.btn2.setText("查看报告");
            holder.btn2.setVisibility(View.VISIBLE);
            holder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(position, Constants.CHECKREPORT);
                    }
                }
            });

            //超级用户修改列表进入可修改
            if (isSuperModify) {
                holder.btn3.setText("修改");
                holder.btn3.setVisibility(View.VISIBLE);
                holder.btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnClickListener != null) {
                            mOnClickListener.onItemClick(position, Constants.MODIFICATION);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

    @Override
    public void requestTaskStatusSucceed(ResponseJson<Integer> response) {
        if (response.getMemberValue() == 1) {
            if (DBManager.getInstance().isExistTaskUpload(curTaskItem.getTaskId() + "", PadSysApp.getUser().getUserId(), "File")) {
                mOnClickListener.onItemClick(curPos, Constants.UPLOAD);
            } else if (DBManager.getInstance().isExistTask(curTaskItem.getTaskId() + "")) {
                mOnClickListener.onItemClick(curPos, Constants.CONTINUEDETECTION);
            } else {
                mOnClickListener.onItemClick(curPos, Constants.START);
            }
        }else {

            MyToast.showShort(response.getMsg());
            //刷新列表
            myTaskFragment.setRefreshParams();
            showDialog();
            myTaskFragment.clearRequest();
        }
    }

    @Override
    public void requestTaskStatusFailed(String Msg) {
        MyToast.showShort(Msg);
        //刷新列表
        myTaskFragment.setRefreshParams();
        showDialog();
        myTaskFragment.clearRequest();
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    public interface OnClickListener<T> {
        void onItemClick(int position, int pos);

        void onTtemPhoto(int position);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public MyTaskAdapter(Context context, List<TaskItem> datas,MyTaskFragment myTaskFragment) {
        this.myTaskFragment = myTaskFragment;
        this.context = context;
        this.mdatas = datas;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llayout_task;
        private SimpleDraweeView ivCar;
        private TextView carName;
        private TextView tvDetectTime;
        private TextView tvFirstCheck;
        private TextView tvSecondCheck;
        private TextView tvPriority;
        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private TextView tv5;
        private TextView tv6;
        private TextView tv7;
        private TextView tv8;
        private TextView btn1;
        private TextView btn2;
        private TextView btn3;
        private TextView tvLookCause;
        private TextView tvPhingguStatus;
        private TextView tvAlreadySign;
        private TextView tvDes;//备注
        private TextView tvAddress;//地址

        public ViewHolder(View itemView) {
            super(itemView);
            llayout_task = (LinearLayout) itemView.findViewById(R.id.llayout_task);
            ivCar = (SimpleDraweeView) itemView.findViewById(R.id.ivCar);
            carName = (TextView) itemView.findViewById(R.id.carName);
            tvDetectTime = (TextView) itemView.findViewById(R.id.tvDetectTime);
            tvFirstCheck = (TextView) itemView.findViewById(R.id.tvFirstCheck);
            tvSecondCheck = (TextView) itemView.findViewById(R.id.tvSecondCheck);
            tvPriority = (TextView) itemView.findViewById(R.id.tvPriority);

            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
            tv6 = (TextView) itemView.findViewById(R.id.tv6);
            tv7 = (TextView) itemView.findViewById(R.id.tv7);
            tv8 = (TextView) itemView.findViewById(R.id.tv8);

            btn1 = (TextView) itemView.findViewById(R.id.btn1);
            btn2 = (TextView) itemView.findViewById(R.id.btn2);
            btn3 = (TextView) itemView.findViewById(R.id.btn3);
            tvLookCause = (TextView) itemView.findViewById(R.id.tvLookCause);
            tvPhingguStatus = (TextView) itemView.findViewById(R.id.tvPhingguStatus);
            tvAlreadySign = (TextView) itemView.findViewById(R.id.tvAlreadySign);

            //备注
            tvDes = (TextView) itemView.findViewById(R.id.tvDes);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
        }
    }

}
