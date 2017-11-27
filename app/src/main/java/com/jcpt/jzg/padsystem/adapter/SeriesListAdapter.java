package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.vo.SeriesList;

import java.util.List;

/**
 * 车系适配器
 * Created by zealjiang on 2016/11/2 10:05.
 * Email: zealjiang@126.com
 */

public class SeriesListAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<SeriesList.MemberValueBean.ModelGroupListBean> list;
    private List<String> groupNames;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private int selectedPosition = -1;// 选中的位置   
    private Context context;

    public SeriesListAdapter(Context context, List<SeriesList.MemberValueBean.ModelGroupListBean> list, List<String> groupNames) {
        super();
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.groupNames = groupNames;
        this.context = context;
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
    public boolean isEnabled(int position) {

        if (groupNames.contains(((SeriesList.MemberValueBean.ModelGroupListBean) getItem(position)).getName()) &&
                Constants.IS_TITLE.equals(((SeriesList.MemberValueBean.ModelGroupListBean) getItem(position)).getManufacturerName())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return groupNames.contains(((SeriesList.MemberValueBean.ModelGroupListBean) getItem(position)).getName()) &&
                Constants.IS_TITLE.equals(((SeriesList.MemberValueBean.ModelGroupListBean) getItem(position)).getManufacturerName()) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        SeriesList.MemberValueBean.ModelGroupListBean serie = (SeriesList.MemberValueBean.ModelGroupListBean) getItem(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            int type = getItemViewType(position);
            switch (type) {
                case TYPE_ITEM:
                    view = inflater.inflate(R.layout.addexam_list_item, null);
                    view.setBackgroundColor(list.get(position).getItemColor());
                    viewHolder.img = (SimpleDraweeView) view.findViewById(R.id.modle_img);
                    break;
                case TYPE_SEPARATOR:
                    view = inflater.inflate(R.layout.addexam_list_item_tag, null);
                    break;
            }
            viewHolder.text = (TextView) view.findViewById(R.id.addexam_list_item_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (viewHolder.img != null) {
                viewHolder.img.setVisibility(View.VISIBLE);

                Uri imageUri = Uri.parse(StringUtils.null2Length0(serie.getModelimgpath()));
                viewHolder.img.setImageURI(imageUri);
        }

        viewHolder.text.setText(list.get(position).getName());
        viewHolder.text.setTextColor(list.get(position).getFontColor());

        if(selectedPosition == position) {
            view.setBackgroundColor(context.getResources().getColor(R.color.common_gray_line));
        } else {
            view.setBackgroundColor(Color.WHITE);
        }
        return view;
    }

    class ViewHolder {
        TextView text;
        SimpleDraweeView img;
    }
}
