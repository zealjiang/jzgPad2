package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.List;


public class CarPhotoBaseAdapter extends RecyclerView.Adapter<CarPhotoBaseAdapter.ViewHolder> {

    private List<PictureItem> dataLists;
    private FrescoImageLoader frescoImageLoader;
    private Context context;
    private CarPhotoBaseAdapter.OnItemClickListener clickListener;

    public void setDataLists(List<PictureItem> dataLists) {
        this.dataLists = dataLists;
    }

    public CarPhotoBaseAdapter(Context context, List<PictureItem> datas) {
        this.context = context;
        dataLists = datas;
        frescoImageLoader = FrescoImageLoader.getSingleton();


    }

    @Override
    public CarPhotoBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_child_photo_defect,parent,false);
        CarPhotoBaseAdapter.ViewHolder viewHolder = new CarPhotoBaseAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        frescoImageLoader.displayImage(holder.simpleDraweeView,dataLists.get(position).getPicPath());
        holder.tvName.setText(dataLists.get(position).getPicName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(v,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView simpleDraweeView;
        private TextView tvName;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.sdv);
        }
    }


    public void setOnItemClickListener(CarPhotoBaseAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}
