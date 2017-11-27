package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;

import java.util.List;


public class TaskTypeAdapter  extends RecyclerView.Adapter<TaskTypeAdapter.ViewHolder>{

    private Context context;
    private List<String> data;
    private TaskTypeAdapter.OnItemClickListener clickListener;

    private int currPosition = 0;
    private int itemHeight = 0;

    public TaskTypeAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_type,parent,false);
        TaskTypeAdapter.ViewHolder viewHolder = new TaskTypeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(itemHeight>0){
            holder.container.getLayoutParams().height=itemHeight;
        }
        if(currPosition==position){
            holder.container.setBackgroundResource(R.drawable.menu_selected);
        }else{
            holder.container.setBackgroundResource(R.drawable.menu_unselelcted);
        }
        holder.tvItemName.setText(data.get(position));

        if(clickListener!=null) {
            holder.container.setOnClickListener(new View.OnClickListener() {
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

    public int getCurrPosition() {
        return currPosition;
    }

    public void setCurrPosition(int currPosition) {
        notifyDataSetChanged();
        this.currPosition = currPosition;
    }

    public void setOnItemClickListener(TaskTypeAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout container;
        private TextView tvItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.llItemBg);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
        }
    }
}
