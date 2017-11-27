package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.List;



public class CarPhotoProdedureAdapter extends RecyclerView.Adapter<CarPhotoProdedureAdapter.ViewHolder> {

    private List<PictureItem> dataLists;
    private FrescoImageLoader frescoImageLoader;
    private Context context;
    private CarPhotoProdedureAdapter.OnItemClickListener clickListener;

    private boolean configDriving; //行驶证照片 81
    private boolean configRegister;//登记证照片 82

    public void setDataLists(List<PictureItem> dataLists) {
        this.dataLists = dataLists;
    }

    public CarPhotoProdedureAdapter(Context context, List<PictureItem> datas,
                                    boolean configDriving,boolean configRegister) {
        this.context = context;
        dataLists = datas;
        frescoImageLoader = FrescoImageLoader.getSingleton();
        this.configDriving = configDriving;
        this.configRegister = configRegister;

    }

    @Override
    public CarPhotoProdedureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_child_photo_defect,parent,false);
        CarPhotoProdedureAdapter.ViewHolder viewHolder = new CarPhotoProdedureAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        frescoImageLoader.displayImage(holder.simpleDraweeView,dataLists.get(position).getPicPath());

        if (configDriving && configRegister) {
            if (position == 6 || position == 7) {
                if (TextUtils.isEmpty(dataLists.get(position).getPicPath())) {
                    holder.simpleDraweeView.setImageResource(R.drawable.jiahaopic);
                }
            }
        }else if(configDriving==false && configRegister ==true){
            if (position == 2 || position == 3) {
                if (TextUtils.isEmpty(dataLists.get(position).getPicPath())) {
                    holder.simpleDraweeView.setImageResource(R.drawable.jiahaopic);
                }
            }
        }

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


    public void setOnItemClickListener(CarPhotoProdedureAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}
