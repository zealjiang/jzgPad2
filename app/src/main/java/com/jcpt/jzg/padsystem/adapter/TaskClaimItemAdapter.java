package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.TaskItem;

import java.util.List;


public class TaskClaimItemAdapter extends RecyclerView.Adapter<TaskClaimItemAdapter.ViewHolder>  {

    private Context context;
    private TaskClaimItemAdapter.IMyViewHolderClicks clickListener;
    private List<String>list;

    public TaskClaimItemAdapter(Context context, List<String>list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TaskClaimItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_claim1,parent,false);
        TaskClaimItemAdapter.ViewHolder viewHolder = new TaskClaimItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskClaimItemAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public void setmListener(IMyViewHolderClicks listener) {
        this.clickListener = listener;
    }

    public interface IMyViewHolderClicks{
        public void onFollowStatusChange(TaskItem taskClaim);
    }
}
