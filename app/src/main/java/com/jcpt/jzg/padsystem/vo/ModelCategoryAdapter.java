package com.jcpt.jzg.padsystem.vo;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.global.Constants;

import java.util.List;

/**
 * 车系分类列表适配器 ClassName: ModelCategoryAdapter <br/>
 * date: 2014-6-18 下午3:15:31 <br/>
 *
 * @author wang
 * @since JDK 1.6
 */
public class ModelCategoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Model> list;
    private List<String> groupNames;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    public ModelCategoryAdapter(Context context, List<Model> list,
                                List<String> groupNames) {
        super();
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.groupNames = groupNames;
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
        if (groupNames.contains(((Model) getItem(position)).getName()) &&
                Constants.IS_TITLE.equals(((Model) getItem(position)).getManufacturerName())) {
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
        return groupNames.contains(((Model) getItem(position)).getName()) &&
                Constants.IS_TITLE.equals(((Model) getItem(position)).getManufacturerName()) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Model model = (Model) getItem(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            int type = getItemViewType(position);
            switch (type) {
                case TYPE_ITEM:
                    view = inflater.inflate(R.layout.addexam_list_item, null);
                    view.setBackgroundColor(list.get(position).getItemColor());
                    viewHolder.img = (SimpleDraweeView) view
                            .findViewById(R.id.modle_img);
                    break;
                case TYPE_SEPARATOR:
                    view = inflater.inflate(R.layout.addexam_list_item_tag, null);
                    break;
            }
            viewHolder.text = (TextView) view
                    .findViewById(R.id.addexam_list_item_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (viewHolder.img != null) {
            Uri imageUri = Uri.parse(model.getModelimgpath());
            viewHolder.img.setImageURI(imageUri);

        }

        viewHolder.text.setText(list.get(position).getName());
        viewHolder.text.setTextColor(list.get(position).getFontColor());
        return view;
    }

    class ViewHolder {
        TextView text;
        SimpleDraweeView img;

    }

}
