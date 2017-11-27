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
import com.jcpt.jzg.padsystem.vo.BMWReCheckBean;

import java.util.List;

/**
 * 宝马复检待维修项
 */
public class BMWMaintainAdapter extends RecyclerView.Adapter<BMWMaintainAdapter.ViewHolder>  {

    private Context context;
    private List<BMWReCheckBean.BMWRepairBean> listData;

    public BMWMaintainAdapter(Context context, List<BMWReCheckBean.BMWRepairBean> listData) {
        this.context = context;
        this.listData = listData;
    }


    @Override
    public BMWMaintainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bmw_maintain,parent,false);
        BMWMaintainAdapter.ViewHolder viewHolder = new BMWMaintainAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BMWMaintainAdapter.ViewHolder holder, final int position) {
        if(listData==null || listData.size()==0){
            return;
        }
        final BMWReCheckBean.BMWRepairBean outfitBean = listData.get(position);

        holder.tvNotOutfit.setText(outfitBean.getRepairDes());
        holder.tvStandard.setText(outfitBean.getStandard());
        switch (outfitBean.getRepairResult()){
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
                outfitBean.setRepairResult(1);
            }
        });

        holder.rbNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outfitBean.setRepairResult(0);
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
        private TextView tvNotOutfit;
        private TextView tvStandard;
        private RadioGroup rg;
        private RadioButton rbYes;
        private RadioButton rbNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNotOutfit = (TextView) itemView.findViewById(R.id.tvNotOutfit);
            tvStandard = (TextView) itemView.findViewById(R.id.tvStandard);
            rg = (RadioGroup) itemView.findViewById(R.id.rg);
            rbYes = (RadioButton) itemView.findViewById(R.id.rbYes);
            rbNo = (RadioButton) itemView.findViewById(R.id.rbNo);
        }
    }

}
