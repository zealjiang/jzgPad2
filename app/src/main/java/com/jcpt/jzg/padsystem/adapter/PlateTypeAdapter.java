package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 铭牌型号
 * Created by zealjiang on 2016/11/17 15:08.
 * Email: zealjiang@126.com
 */

public class PlateTypeAdapter extends RecyclerView.Adapter<PlateTypeAdapter.ViewHolder> {

    private Context context;
    private List<String> listData;
    private PlateTypeAdapter.OnItemClickListener clickListener;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    public PlateTypeAdapter(Context context, List<String> listData) {
        this.context = context;
        this.listData = listData;
        isClicks = new ArrayList<>();
        for(int i = 0;i<listData.size();i++){
            isClicks.add(false);
        }
    }

    public void onDataChanged(){
        isClicks.clear();
        for(int i = 0;i<listData.size();i++){
            isClicks.add(false);
        }
    }

    public void setCheckPos(int pos){
        isClicks.clear();
        for(int i = 0;i<listData.size();i++){
            if(i==pos){
                isClicks.add(true);
            }else{
                isClicks.add(false);
            }
        }
    }

    @Override
    public PlateTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plate_type, parent, false);
        return new PlateTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlateTypeAdapter.ViewHolder holder, final int position) {

        holder.tvName.setText(listData.get(position));

        if(isClicks.size()>position&&isClicks.get(position)) {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvName.setBackgroundResource(R.drawable.plate_border_sel);
            holder.iv.setVisibility(View.VISIBLE);

            if(clickListener!=null) {
                clickListener.onItemClick(holder.tvName, position);
            }
        } else {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.global_gray_4));
            holder.tvName.setBackgroundResource(R.drawable.plate_border_nor);
            holder.iv.setVisibility(View.GONE);
        }

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isClicks.size()>position) {
                    for (int i = 0; i < isClicks.size(); i++) {
                        isClicks.set(i, false);
                    }
                    isClicks.set(position, true);
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }

    public void setOnItemClickListener(PlateTypeAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos);
    }
}
