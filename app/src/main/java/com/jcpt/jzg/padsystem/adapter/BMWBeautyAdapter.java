package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.BMWBeautyModel;
import com.jcpt.jzg.padsystem.vo.BMWMaintainModel;

import java.util.List;

/**
 * 宝马复检美容项
 */
public class BMWBeautyAdapter extends RecyclerView.Adapter<BMWBeautyAdapter.ViewHolder>  {

    private Context context;
    private List<BMWBeautyModel.PartBean> listData;

    public BMWBeautyAdapter(Context context, List<BMWBeautyModel.PartBean> listData) {
        this.context = context;
        this.listData = listData;
    }


    @Override
    public BMWBeautyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bmw_beauty,parent,false);
        BMWBeautyAdapter.ViewHolder viewHolder = new BMWBeautyAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BMWBeautyAdapter.ViewHolder holder, final int position) {
        if(listData==null || listData.size()==0){
            return;
        }
        final BMWBeautyModel.PartBean partBean = listData.get(position);

        holder.tvName.setText(partBean.getNameCh());
        switch (partBean.getValueStr()){
            case 0:
                holder.rbNo.setChecked(true);
                break;
            case 1:
                holder.rbYes.setChecked(true);
                break;
            default:
                holder.rg.clearCheck();
        }

        holder.rbYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.get(position).setValueStr(1);
            }
        });

        holder.rbNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.get(position).setValueStr(0);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listData==null){
            return 0;
        }
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private RadioGroup rg;
        private RadioButton rbYes;
        private RadioButton rbNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            rg = (RadioGroup) itemView.findViewById(R.id.rg);
            rbYes = (RadioButton) itemView.findViewById(R.id.rbYes);
            rbNo = (RadioButton) itemView.findViewById(R.id.rbNo);
        }
    }

}
