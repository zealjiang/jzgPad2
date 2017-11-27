package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.model.DetectionModel;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.List;


public class BMWOtherCheckAdapter extends RecyclerView.Adapter<BMWOtherCheckAdapter.ViewHolder> {

    List<PictureItem> dataLists;
    OnClickDelLister mOnClickDelLister;
    private Context context;
    private BMWOtherCheckAdapter.OnItemClickListener itemClickListener;


    public BMWOtherCheckAdapter(Context context, List<PictureItem> data) {
        this.context = context;
        this.dataLists = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bmw_other_photo,parent,false);
        BMWOtherCheckAdapter.ViewHolder viewHolder = new BMWOtherCheckAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        FrescoImageLoader.getSingleton().displayImage(holder.ivCar,dataLists.get(position).getPicPath());

        holder.tvName.setText(dataLists.get(position).getPicName());

        if(itemClickListener!=null) {
            holder.ivCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    public void setDataLists(List<PictureItem> dataLists) {
        this.dataLists = dataLists;
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public interface OnClickDelLister{
        void onClickDelLister(int position);
    }

    public void setOnItemClickListener(BMWOtherCheckAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView ivCar;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCar = (SimpleDraweeView) itemView.findViewById(R.id.sdv_subjoin);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
