package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.TaskClaimList;

import java.util.List;

import static com.jcpt.jzg.padsystem.R.drawable.btn_bg_checked;
import static com.jcpt.jzg.padsystem.R.drawable.btn_bg_normal;
import static com.jcpt.jzg.padsystem.utils.UIUtils.getResources;

/**
 * 任务认领标签
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>  {

    private Context context;
    private List<TaskClaimList.SourceListBean> listData;
    private int selectedPosition = 0;// 选中的位置
    private TagAdapter.OnItemClickListener clickListener;

    public TagAdapter(Context context, List<TaskClaimList.SourceListBean> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tag_task,parent,false);
        TagAdapter.ViewHolder viewHolder = new TagAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TagAdapter.ViewHolder holder, final int position) {
        if(listData.get(position).getTaskCount()>99999){
            holder.btnTag.setText(listData.get(position).getName()+"\n99999+");
        }else{
            holder.btnTag.setText(listData.get(position).getName()+"\n"+listData.get(position).getTaskCount());
        }

        if(selectedPosition == position) {
            holder.btnTag.setBackgroundResource(btn_bg_checked);
            holder.btnTag.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.btnTag.setBackgroundResource(btn_bg_normal);
            holder.btnTag.setTextColor(Color.parseColor("#000000"));
        }
        int padding = getResources().getDimensionPixelSize(R.dimen.DIP_4);
        holder.btnTag.setPadding(padding,padding,padding,padding);

        holder.btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener!=null){
                    clickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView btnTag;

        public ViewHolder(View itemView) {
            super(itemView);
            btnTag = (TextView) itemView.findViewById(R.id.btnTag);
        }
    }

    public void setOnItemClickListener(TagAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}
