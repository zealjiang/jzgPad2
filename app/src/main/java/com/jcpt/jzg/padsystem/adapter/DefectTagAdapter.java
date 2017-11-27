package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.detection.DefectDetailItem;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagAdapter;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * author: guochen
 * date: 2016/9/22 10:59
 * email:
 * 小缺陷项集合对应的 tagFlowLayout adapter
 */
public class DefectTagAdapter extends TagAdapter<DefectDetailItem> {
    private TagFlowLayout layout;
    private Context context;

//    public DefectTagAdapter(String[] params, Context context) {
//        super(params);
//        this.context = context;
//    }
//
//    public DefectTagAdapter(String[]  params,TagFlowLayout layout, Context context) {
//        super(params);
//        this.layout = layout;
//        this.context = context;
//    }

    public DefectTagAdapter(List<DefectDetailItem> params,TagFlowLayout layout, Context context) {
        super(params);
        this.layout = layout;
        this.context = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, DefectDetailItem s) {
        TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv_tag,layout,false);
        tv.setText(s.getDefectName()); //设置小缺陷项的名称  -> 李波 on 2016/11/24.
        return tv;
    }
}
