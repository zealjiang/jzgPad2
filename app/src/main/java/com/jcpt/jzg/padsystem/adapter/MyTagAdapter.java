package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagAdapter;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;

import java.util.List;

/**
 * author: guochen
 * date: 2016/9/22 10:59
 * email: 
 */
public class MyTagAdapter extends TagAdapter<String> {
    private TagFlowLayout layout;
    private Context context;

    public MyTagAdapter(List<String> params,Context context) {
        super(params);
        this.context = context;
    }
    public MyTagAdapter(List<String> params,TagFlowLayout layout,Context context) {
        this(params,context);
        this.layout = layout;
        this.context = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv_tag,layout,false);
        tv.setText(s);
        return tv;
    }
}
