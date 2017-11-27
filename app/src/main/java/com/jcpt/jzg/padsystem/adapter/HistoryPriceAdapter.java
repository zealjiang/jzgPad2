package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.HistoryPriceModel;

import java.util.List;

/**
 * 历史评估价格
 * Created by zealjiang on 2016/11/17 15:08.
 * Email: zealjiang@126.com
 */

public class HistoryPriceAdapter extends RecyclerView.Adapter<HistoryPriceAdapter.ViewHolder>  {

    private Context context;
    private List<HistoryPriceModel> listData;
    private HistoryPriceAdapter.OnItemClickListener clickListener;

    public HistoryPriceAdapter(Context context, List<HistoryPriceModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public HistoryPriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_price,parent,false);
        HistoryPriceAdapter.ViewHolder viewHolder = new HistoryPriceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryPriceAdapter.ViewHolder holder, final int position) {
        holder.tvTime.setText(listData.get(position).getUpdateTime());
        holder.tvBuyPrice.setText(listData.get(position).getBuyPrice());
        holder.tvSellPrice.setText(listData.get(position).getSalePrice());
        holder.tvArea.setText(listData.get(position).getTaskSourceName());



/*        holder.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener!=null){
                    clickListener.onItemClick(v,position);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTime;
        private TextView tvBuyPrice;
        private TextView tvSellPrice;
        private TextView tvArea;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvBuyPrice = (TextView) itemView.findViewById(R.id.tvBuyPrice);
            tvSellPrice = (TextView) itemView.findViewById(R.id.tvSellPrice);
            tvArea = (TextView) itemView.findViewById(R.id.tvArea);
        }
    }

    public void setOnItemClickListener(HistoryPriceAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}
