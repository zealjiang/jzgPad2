package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.view.MyGridView;
import com.jcpt.jzg.padsystem.vo.PhotoModel;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.List;


/**
 * 缺陷照片适配器
 * Created by zealjiang on 2016/11/14 16:40.
 * Email: zealjiang@126.com
 */
public class PhotoDefectExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groupList;
    private List<List<PictureItem>> itemList;
    private IchooseFlawPhotoLister mIchooseFlawPhotoLister;

    public IchooseFlawPhotoLister getmIchooseFlawPhotoLister() {
        return mIchooseFlawPhotoLister;
    }

    public void setmIchooseFlawPhotoLister(IchooseFlawPhotoLister mIchooseFlawPhotoLister) {
        this.mIchooseFlawPhotoLister = mIchooseFlawPhotoLister;
    }


    public List<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }

    public List<List<PictureItem>> getItemList() {
        return itemList;
    }

    public void setItemList(List<List<PictureItem>> itemList) {
        this.itemList = itemList;
    }

    public PhotoDefectExpandableAdapter(Context context, List<String> groupList, List<List<PictureItem>> itemList) {
        this.context = context;
        this.groupList = groupList;
        this.itemList = itemList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(null==convertView){
            convertView = View.inflate(context, R.layout.item_group_photo_defect,null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_group_title);

        // 设置分组组名
        tvName.setText(groupList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        View view;
        ViewHolder holder;
//        System.out.println("groupPosition  --->" +groupPosition);

        // 判断convertView的状态，来达到复用效果
        if (null == convertView) {
            // 如果convertView为空，则表示第一次显示该条目，需要创建一个view
            view = View.inflate(context, R.layout.item_photo_defect_expandablelist,null);
            //新建一个viewholder对象
            holder = new ViewHolder();
            //将findviewbyID的结果赋值给holder对应的成员变量
            holder.gridView = (MyGridView) view.findViewById(R.id.gv_photo_defect);

            view.setTag(holder);

        } else {
            // 否则表示可以复用convertView
            view = convertView;
            holder = (ViewHolder) view.getTag();
//            System.out.println("groupPosition + Nonull --->" +groupPosition);
        }
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MyToast.showShort("组： "+groupPosition+" 子项： "+position);
                    if(itemList != null && itemList.size()>0) {
                        mIchooseFlawPhotoLister.show(groupPosition, position, itemList.get(groupPosition).get(position));
                    }
            }
        });

        // 将holder与view进行绑定
        holder.gridViewAdapter = new PhotoDefectGridViewAdapter(context, itemList.get(groupPosition));
        holder.gridView.setAdapter(holder.gridViewAdapter);


        return view;
    }


    private static class ViewHolder {
        private com.jcpt.jzg.padsystem.view.MyGridView gridView;
        PhotoDefectGridViewAdapter gridViewAdapter = null;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public interface IchooseFlawPhotoLister{
        public void show(int itemPositon,int tagPositon,PictureItem mPictureItem);
    }


}
