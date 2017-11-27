package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;

import java.util.ArrayList;

/**
 * Created by wujj on 2017/2/20.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class ResultCheckedAdapter extends RecyclerView.Adapter<ResultCheckedAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> list;

    public ResultCheckedAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(context).inflate(R.layout.activity_result_checked_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int pos = list.get(position).indexOf(":");
        if(pos!=-1&&list.get(position).length()>pos+1){
            holder.tv.setText(list.get(position).substring(0,pos+1));
            holder.tvValue.setText(list.get(position).substring(pos+1));
        }else{
            holder.tv.setText(list.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private TextView tvValue;
        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
