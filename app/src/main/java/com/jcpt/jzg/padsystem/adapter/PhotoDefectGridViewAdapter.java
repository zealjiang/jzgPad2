package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.List;

/**
 * Created by zealjiang on 2016/11/14 17:42.
 * Email: zealjiang@126.com
 */

public class PhotoDefectGridViewAdapter extends BaseAdapter{

    private Context context;
    private FrescoImageLoader frescoImageLoader;

    public void setItemGridList(List<PictureItem> itemGridList) {
        this.itemGridList = itemGridList;
    }

    private List<PictureItem> itemGridList;

    public PhotoDefectGridViewAdapter(Context context, List<PictureItem> itemGridList) {
        this.context = context;
        this.itemGridList = itemGridList;
        frescoImageLoader = FrescoImageLoader.getSingleton();
    }

    @Override
    public int getCount() {
        return itemGridList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemGridList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        // 判断convertView的状态，来达到复用效果
        if (null == convertView) {
            // 如果convertView为空，则表示第一次显示该条目，需要创建一个view
            view = View.inflate(context, R.layout.item_child_photo_defect, null);
            //新建一个viewholder对象
            holder = new ViewHolder();
            //将findviewbyID的结果赋值给holder对应的成员变量
            holder.tvHolder = (TextView) view.findViewById(R.id.tv_name);
            holder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv);
            // 将holder与view进行绑定
            view.setTag(holder);
        } else {
            // 否则表示可以复用convertView
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        // 直接操作holder中的成员变量即可，不需要每次都findViewById
        holder.tvHolder.setText(itemGridList.get(position).getPicName());

        frescoImageLoader.displayImage(holder.simpleDraweeView,itemGridList.get(position).getPicPath());
//        if(!itemGridList.get(position).getPicPath().equals(holder.simpleDraweeView.getTag())){
            holder.simpleDraweeView.setTag(itemGridList.get(position).getPicPath());
//            if(itemGridList.get(position).getPicPath().contains("http://")){
////                holder.simpleDraweeView.setImageURI(itemGridList.get(position).getPicPath());
//                FrescoImageLoader.displayImage(holder.simpleDraweeView, Uri.fromFile(new File(itemGridList.get(position).getPicPath())));
//            }else{
////                holder.simpleDraweeView.setImageURI("file://"+itemGridList.get(position).getPicPath());
//                FrescoImageLoader.displayImage(holder.simpleDraweeView,Uri.fromFile(new File(itemGridList.get(position).getPicPath())));
//            }
//        }

        return view;
    }


    private static class ViewHolder {
        private TextView tvHolder;
        private SimpleDraweeView simpleDraweeView;
    }


}
