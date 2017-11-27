package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.AdmixedData;

/**
 * Created by liugl on 2016/11/2.
 */

public class CarPicComparedLeftAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder vh;
    private AdmixedData datas;


    public CarPicComparedLeftAdapter(Context context,AdmixedData datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.getPhotoData().size();
    }

    @Override
    public Object getItem(int position) {
        return datas.getPhotoData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.carpiccompared_left_item, null);
            vh.title = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.title.setText(datas.getPhotoData().get(position).getPropertyName());
        return convertView;
    }
    class ViewHolder {
        public TextView title;
    }
}
