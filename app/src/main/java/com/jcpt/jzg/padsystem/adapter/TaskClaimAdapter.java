package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.TaskItem;

import java.util.ArrayList;
import java.util.List;


public class TaskClaimAdapter extends RecyclerView.Adapter<TaskClaimAdapter.ViewHolder> {

    private Context context;
    private List<TaskItem> listData;
    private TaskClaimAdapter.IMyViewHolderClicks clickListener;
    private List<String> list = new ArrayList<>();

    public TaskClaimAdapter(Context context, List<TaskItem> listData) {
        this.context = context;
        this.listData = listData;
    }

    public void setDatas(List<TaskItem> listData){
        this.listData = listData;
    }

    @Override
    public TaskClaimAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_claim, parent, false);
        TaskClaimAdapter.ViewHolder viewHolder = new TaskClaimAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskClaimAdapter.ViewHolder holder, final int position) {

        final TaskItem item = listData.get(position);
        if (TextUtils.isEmpty(item.getCarName())){
            holder.carName.setVisibility(View.GONE);
        }else {
            holder.carName.setVisibility(View.VISIBLE);
            holder.carName.setText(item.getCarName());
        }
        int isRecheck = item.getIsRecheck();
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
        int isPriority = item.getIsPriority();
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
        if (isRecheck == 0 && isPriority == 0){
            holder.llCarName.setVisibility(View.GONE);
        }else {
            holder.llCarName.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(item.getYXOrderNo())) {//无机构单号
            if (TextUtils.isEmpty(item.getCreateUserName())) {//无下单账号
                holder.tv1.setText("评估编号：" + item.getOrderNo());
                holder.tv2.setText("下单时间：" + item.getCreateTime());
                holder.tv3.setText("产品类型：" + item.getProductType());
                holder.tv4.setVisibility(View.GONE);
                holder.tv8.setVisibility(View.GONE);
                holder.tv5.setText(item.getOrgName() + "：" + item.getCityName());
                holder.tv6.setText(TextUtils.isEmpty(item.getVinCode()) ? "VIN：暂无" : "VIN：" + item.getVinCode());
                if (TextUtils.isEmpty(item.getLinkManName()) || TextUtils.isEmpty(item.getLinkMobile())) {
                    holder.tv7.setText("");
                } else if (!TextUtils.isEmpty(item.getLinkManName()) && !TextUtils.isEmpty(item.getLinkMobile())) {
                    holder.tv7.setText(item.getLinkManName() + "：" + item.getLinkMobile());
                }
            } else {//有下单账号
                holder.tv1.setText("评估编号：" + item.getOrderNo());
                holder.tv2.setText("下单时间：" + item.getCreateTime());
                holder.tv3.setText(TextUtils.isEmpty(item.getVinCode()) ? "VIN：暂无" : "VIN：" + item.getVinCode());
                if (TextUtils.isEmpty(item.getLinkManName()) || TextUtils.isEmpty(item.getLinkMobile())) {
                    holder.tv4.setVisibility(View.GONE);
                    holder.tv4.setText("");
                } else if (!TextUtils.isEmpty(item.getLinkManName()) && !TextUtils.isEmpty(item.getLinkMobile())) {
                    holder.tv4.setVisibility(View.VISIBLE);
                    holder.tv4.setText(item.getLinkManName() + "：" + item.getLinkMobile());
                }
                holder.tv5.setText(item.getOrgName() + "：" + item.getCityName());
                holder.tv6.setText("下单账号：" + item.getCreateUserName());
                holder.tv7.setText("产品类型：" + item.getProductType());
                holder.tv8.setVisibility(View.GONE);
            }
        } else {//有机构单号
            if (TextUtils.isEmpty(item.getCreateUserName())) {//无下单账号
                holder.tv1.setText("评估编号：" + item.getOrderNo());
                holder.tv2.setText(item.getOrgName() + "：" + item.getCityName());
                holder.tv3.setText(TextUtils.isEmpty(item.getVinCode()) ? "VIN：暂无" : "VIN：" + item.getVinCode());
                if (TextUtils.isEmpty(item.getLinkManName()) || TextUtils.isEmpty(item.getLinkMobile())) {
                    holder.tv4.setVisibility(View.GONE);
                    holder.tv4.setText("");
                } else if (!TextUtils.isEmpty(item.getLinkManName()) && !TextUtils.isEmpty(item.getLinkMobile())) {
                    holder.tv4.setVisibility(View.VISIBLE);
                    holder.tv4.setText(item.getLinkManName() + "：" + item.getLinkMobile());
                }
                holder.tv5.setText("机构单号：" + item.getYXOrderNo());
                holder.tv6.setText("下单时间：" + item.getCreateTime());
                holder.tv7.setText("产品类型：" + item.getProductType());
                holder.tv8.setVisibility(View.GONE);
            } else {//有下单账号
                holder.tv4.setVisibility(View.VISIBLE);
                holder.tv1.setText("评估编号：" + item.getOrderNo());
                holder.tv2.setText(item.getOrgName() + "：" + item.getCityName());
                holder.tv3.setText("下单时间：" + item.getCreateTime());
                holder.tv4.setText("产品类型：" + item.getProductType());
                holder.tv5.setText("机构单号：" + item.getYXOrderNo());
                holder.tv6.setText("下单账号：" + item.getCreateUserName());
                holder.tv7.setText(TextUtils.isEmpty(item.getVinCode()) ? "VIN：暂无" : "VIN：" + item.getVinCode());
                holder.tv8.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(item.getLinkManName()) || TextUtils.isEmpty(item.getLinkMobile())) {
                    holder.tv8.setText("");
                } else if (!TextUtils.isEmpty(item.getLinkManName()) && !TextUtils.isEmpty(item.getLinkMobile())) {
                    holder.tv8.setText(item.getLinkManName() + "：" + item.getLinkMobile());
                }
            }
        }
        holder.tvAddress.setText("地址：" + item.getAddress());
        if (TextUtils.isEmpty(item.getDes())){
            holder.tvDes.setVisibility(View.GONE);
        }else {
            holder.tvDes.setVisibility(View.VISIBLE);
            holder.tvDes.setText("备注：" + item.getDes());
        }


        holder.btnClaimTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onFollowStatusChange(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llCarName;
        private TextView carName;
        private TextView tvFirstCheck;
        private TextView tvSecondCheck;
        private TextView tvPriority;
        private RecyclerView recyclerView;
        private TextView btnClaimTask;
        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private TextView tv5;
        private TextView tv6;
        private TextView tv7;
        private TextView tv8;
        private TextView tvDes;//备注
        private TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            llCarName = (LinearLayout) itemView.findViewById(R.id.llCarName);
            carName = (TextView) itemView.findViewById(R.id.carName);
            tvFirstCheck = (TextView) itemView.findViewById(R.id.tvFirstCheck);
            tvSecondCheck = (TextView) itemView.findViewById(R.id.tvSecondCheck);
            tvPriority = (TextView) itemView.findViewById(R.id.tvPriority);
//            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            btnClaimTask = (TextView) itemView.findViewById(R.id.btnClaimTask);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
            tv6 = (TextView) itemView.findViewById(R.id.tv6);
            tv7 = (TextView) itemView.findViewById(R.id.tv7);
            tv8 = (TextView) itemView.findViewById(R.id.tv8);
            tvDes = (TextView) itemView.findViewById(R.id.tvDes);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
//            RecyclerView.LayoutManager manager = new GridLayoutManager(itemView.getContext(), 2);
//            manager.setAutoMeasureEnabled(true);
//            recyclerView.setLayoutManager(manager);
        }
    }

    public void setmListener(IMyViewHolderClicks listener) {
        this.clickListener = listener;
    }

    public interface IMyViewHolderClicks {
        public void onFollowStatusChange(TaskItem taskClaim);
    }
}
