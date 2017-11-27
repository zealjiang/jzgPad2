package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.ProvinceCityModel;

import java.util.List;

/**
 * 省市选择
 * Created by zealjiang on 2016/11/17 15:08.
 * Email: zealjiang@126.com
 */

public class ProvinceCityAdapter extends RecyclerView.Adapter<ProvinceCityAdapter.ViewHolder>  {

    private Context context;
    private List<ProvinceCityModel> listData;
    private int selectedPosition = -1;// 选中的位置
    private ProvinceCityAdapter.OnItemClickListener clickListener;

    public ProvinceCityAdapter(Context context, List<ProvinceCityModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public ProvinceCityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_province,parent,false);
        ProvinceCityAdapter.ViewHolder viewHolder = new ProvinceCityAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProvinceCityAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(listData.get(position).getName());

        if(selectedPosition == position) {
            holder.tvName.setBackgroundColor(context.getResources().getColor(R.color.common_gray_line));
        } else {
            holder.tvName.setBackgroundColor(Color.WHITE);
        }

        if(clickListener!=null) {
            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
    public int getSelectedPosition(){
        if(selectedPosition == -1){
            return 0;
        }
        return selectedPosition;
    }



    public void setOnItemClickListener(ProvinceCityAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
