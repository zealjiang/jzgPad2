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
import com.jcpt.jzg.padsystem.bmw.model.BMWDetectionModel;
import com.jcpt.jzg.padsystem.model.DetectionModel;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.List;


public class BMWCarPhotoSubjoinAdapter extends RecyclerView.Adapter<BMWCarPhotoSubjoinAdapter.ViewHolder> {

    List<PictureItem> dataLists;
    OnClickDelLister mOnClickDelLister;
    private Context context;
    private BMWCarPhotoSubjoinAdapter.OnItemClickListener itemClickListener;
    private boolean isOpenCamera;

    public BMWCarPhotoSubjoinAdapter(Context context, List<PictureItem> data, boolean isOpenCamera) {
        this.context = context;
        this.dataLists = data;
        this.isOpenCamera = isOpenCamera;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_child_photo_subjoin,parent,false);
        BMWCarPhotoSubjoinAdapter.ViewHolder viewHolder = new BMWCarPhotoSubjoinAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (isOpenCamera) {
            if (position == 0) {
                holder.img.setVisibility(View.GONE);
                BMWDetectionModel.loadResPic(holder.ivCar, R.drawable.tianjiapic);
            } else {
                holder.img.setVisibility(View.GONE);
                FrescoImageLoader.getSingleton().displayImage(holder.ivCar, dataLists.get(position).getPicPath());
            }
        }else {
            holder.img.setVisibility(View.GONE);
            FrescoImageLoader.getSingleton().displayImage(holder.ivCar, dataLists.get(position).getPicPath());
        }

        holder.tvName.setText(dataLists.get(position).getPicName());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickDelLister != null){
                    mOnClickDelLister.onClickDelLister(position);
                }
            }
        });

        if(itemClickListener!=null) {
            holder.ivCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public void setDataLists(List<PictureItem> dataLists) {
        this.dataLists = dataLists;
    }

    public void setmOnClickDelLister(OnClickDelLister mOnClickDelLister) {
        this.mOnClickDelLister = mOnClickDelLister;
    }


    public interface OnClickDelLister{
        void onClickDelLister(int position);
    }

    public void setOnItemClickListener(BMWCarPhotoSubjoinAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView ivCar;
        private ImageView img;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCar = (SimpleDraweeView) itemView.findViewById(R.id.sdv_subjoin);
            img = (ImageView) itemView.findViewById(R.id.img_del);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
