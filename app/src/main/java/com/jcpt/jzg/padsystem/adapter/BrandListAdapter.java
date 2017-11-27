package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyd on 15/12/23.
 * 品牌列表适配
 */
public class BrandListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Map<String, Object>> list;

    public BrandListAdapter(Context context, List<Map<String, Object>> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.brand_list_item, null);
            holder = new ViewHolder();
            holder.iamge = (SimpleDraweeView) convertView
                    .findViewById(R.id.car_image);
            holder.name = (TextView) convertView
                    .findViewById(R.id.car_name);
            holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).get("name").toString());
        holder.name.setTextColor((Integer) list.get(position).get(
                "fontColor"));

        String imgUrl = (String) list.get(position).get("logo");
        Uri imageUri = Uri.parse(imgUrl);

        holder.iamge.setImageURI(imageUri);
        String currentStr = list.get(position).get("Sort").toString();
        String previewStr = (position - 1) >= 0 ? list.get(position - 1)
                .get("Sort").toString() : " ";
        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
        } else {
            holder.alpha.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        SimpleDraweeView iamge;
        TextView name;
        TextView alpha;
    }


}
