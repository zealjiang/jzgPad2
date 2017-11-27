package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;

import java.util.List;

/**
 * Created by wujj on 2017/7/18.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class MyTaskItemAdapter extends RecyclerView.Adapter<MyTaskItemAdapter.ViewHolder>  {

    private Context context;
    private List<String> list;

    public MyTaskItemAdapter(Context context, List<String>list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyTaskItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_claim1,parent,false);
        MyTaskItemAdapter.ViewHolder viewHolder = new MyTaskItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyTaskItemAdapter.ViewHolder holder, final int position) {
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
}
