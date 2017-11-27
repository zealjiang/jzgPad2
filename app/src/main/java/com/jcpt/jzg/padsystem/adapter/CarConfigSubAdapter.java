package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.detection.ConfigureList;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 *
 * @author zealjiang
 * @time 2017/3/28 13:55
 */
public class CarConfigSubAdapter extends RecyclerView.Adapter<CarConfigSubAdapter.ViewHolder>  {

    private Context context;
    private List<ConfigureList> listData;//方案数据
    private ArrayList<String> listValueId;//要显示的选中的tagId
    private ArrayList<String> tagList = new ArrayList<>();//一项的tag标签组

    public CarConfigSubAdapter(Context context, List<ConfigureList> listData,ArrayList<String> listValueId) {
        this.context = context;
        this.listData = listData;
        this.listValueId = listValueId;
        if(this.listValueId==null){
            this.listValueId = new ArrayList<>();
        }
        checkSelectedId();
    }

    /**
     * 检查listValueId中单选项是否有错(即 单选出现了多选)
     */
    private void checkSelectedId(){
        if(listValueId.size()<=1){
            return;
        }
        out:
        for (int i = 0; i < listData.size(); i++) {
            ConfigureList configureList = listData.get(i);
            List<ConfigureList.ConfigureValueListBean> tagNameList = configureList.getConfigureValueList();
            if(configureList.getSelectType()==1){
                //单选
                int count = 0;
                for (int j = 0; j < tagNameList.size(); j++) {
                    String tagId = tagNameList.get(j).getConfigureValueId();
                    if(listValueId.contains(tagId)){
                        count++;
                        if(count>1){
                            MyToast.showShort("选中项list中单选项出现多个，有错，进行清理");
                            listValueId.clear();
                            break out;
                        }
                    }
                }
            }else{
                continue;
            }

        }

    }

    @Override
    public CarConfigSubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_config,parent,false);
        CarConfigSubAdapter.ViewHolder viewHolder = new CarConfigSubAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CarConfigSubAdapter.ViewHolder holder, final int position) {
        if(listData==null){
            return;
        }
        //初始化
        final ConfigureList configureList = listData.get(position);
        holder.tvName.setText(configureList.getConfigureName());
        //初始化选项
        tagList.clear();
        final List<ConfigureList.ConfigureValueListBean> tagNameList = configureList.getConfigureValueList();
        for (int i = 0; i < tagNameList.size(); i++) {
            tagList.add(tagNameList.get(i).getConfigureValueName());
        }

        final LayoutInflater mInflater = LayoutInflater.from(context);
        TagAdapter tagAdapter = new TagAdapter<String>(tagList)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_tag, holder.tfl, false);
                tv.setText(s);
                return tv;
            }
        };
        holder.tfl.setAdapter(tagAdapter);


        if(configureList.getSelectType()==1){
            //单选
            holder.tfl.setMaxSelectCount(1);
            holder.tvHint.setText("单选");
        }else{
            //多选
            holder.tfl.setMaxSelectCount(-1);
            holder.tvHint.setText("多选");
        }

        //赋值
        if(listValueId==null){
            return;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < tagNameList.size(); i++) {
            String tagId = tagNameList.get(i).getConfigureValueId();
            if(listValueId.contains(tagId)){
                if(configureList.getSelectType()==1){
                    //单选
                    set.add(i);
                    break;
                }else{
                    //多选
                    set.add(i);
                }
            }
        }
        tagAdapter.setSelectedList(set);

        holder.tfl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if(configureList.getSelectType()==2) {//多选

                    //如果选中的是“无”，则清除“无”之外的其它选中项
                    Set<Integer> selectPosSet = holder.tfl.getSelectedList();
                    if (selectPosSet.size() > 1) {
                        //判断选中项中是否包含“无”
                        Iterator<Integer> iterator = selectPosSet.iterator();
                        while (iterator.hasNext()) {
                            int pos = iterator.next();
                            String tagName = tagNameList.get(pos).getConfigureValueName();
                            if (tagName.equals("无")) {
                                //判断当前点击的是否是“无”
                                List<ConfigureList.ConfigureValueListBean> tagNameList = configureList.getConfigureValueList();
                                if (tagNameList.get(position).getConfigureValueName().equals("无")) {
                                    //选择了“无”成为选中项
                                    //如果选中的是“无”，则清除“无”之外的其它选中项
                                    if (selectPosSet.size() > 1) {
                                        Iterator<Integer> iterator2 = selectPosSet.iterator();
                                        while (iterator2.hasNext()) {
                                            int pos2 = iterator2.next();
                                            String tagName2 = tagNameList.get(pos2).getConfigureValueName();
                                            if (tagName2.equals("无")) {
                                                selectPosSet.clear();
                                                selectPosSet.add(pos2);
                                                holder.tfl.getAdapter().setSelectedList(selectPosSet);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    //移除选中项"无"
                                    selectPosSet.remove(pos);
                                    holder.tfl.getAdapter().setSelectedList(selectPosSet);
                                }
                                break;
                            }
                        }

                    }
                }

                //清除当前某个配置下的所有选中项
                List<ConfigureList.ConfigureValueListBean> tagNameList = configureList.getConfigureValueList();
                for (int i = 0; i < tagNameList.size(); i++) {
                    String id = tagNameList.get(i).getConfigureValueId();
                    listValueId.remove(id);
                }

                //将当前某个配置下的选中项加入到选中项列表中
                Iterator<Integer> iterator = holder.tfl.getSelectedList().iterator();
                while (iterator.hasNext()){
                    int pos = iterator.next();
                    String id = tagNameList.get(pos).getConfigureValueId();
                    if(!listValueId.contains(id)){
                        listValueId.add(id);
                    }
                }

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public ArrayList<String> getSelectedTagId(){
        return listValueId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvHint;
        private TagFlowLayout tfl;

        public ViewHolder(View itemView) {
            super(itemView);
            tfl = (TagFlowLayout) itemView.findViewById(R.id.tfl);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvHint = (TextView) itemView.findViewById(R.id.tvHint);
        }
    }

}
