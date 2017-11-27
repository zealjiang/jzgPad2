package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.List;


public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {
    private int currPos = 0;
    private Context context;
    private List<PictureItem> data;
    private ProvinceCityAdapter.OnItemClickListener clickListener;

    public PreviewAdapter(Context context, List<PictureItem> data) {
        this.context = context;
        this.data = data;
    }

    public void setCurrPos(int currPos) {
        this.currPos = currPos;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_photo_item,parent,false);
        PreviewAdapter.ViewHolder viewHolder = new PreviewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getPicName());
        if(!TextUtils.isEmpty(data.get(position).getPicPath())){

            Fresco.getImagePipeline().evictFromCache(Uri.parse("file://"+data.get(position).getPicPath()));
            FrescoImageLoader.getSingleton().displayImage(holder.ivPhoto,data.get(position).getPicPath());
        }else{
            holder.ivPhoto.setImageURI(Uri.parse(""));
        }
        if(position==currPos){
            holder.rootView.setBackgroundResource(R.drawable.border_corner_selected);
        }else{
            holder.rootView.setBackgroundResource(0);
        }

        if(clickListener!=null) {
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(ProvinceCityAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private SimpleDraweeView ivPhoto;
        private RelativeLayout rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivPhoto = (SimpleDraweeView) itemView.findViewById(R.id.iv_photo);
            rootView = (RelativeLayout) itemView.findViewById(R.id.rl_rootView);
        }
    }

}
