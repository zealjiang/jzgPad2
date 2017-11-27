package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.FlawItem;
import com.jcpt.jzg.padsystem.widget.FlawItemView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FlawAdapter extends RecyclerView.Adapter<FlawAdapter.ViewHolder> {

    IchoosePhotoLister mIchoosePhotoLister;
    IchooseTagLister mIchooseTagLister;
    IchooseTagPhotoPathLister mIchooseTagPhotoPathLister;

    List<FlawItem> mDatas;

    private Context context;

    Map<Integer,Boolean> map = new HashMap<>();
    Map<Integer,Integer> mapTag = new HashMap<>();



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flaw_option,parent,false);
        FlawAdapter.ViewHolder viewHolder = new FlawAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        FlawItem flawItem = mDatas.get(position);
        holder.flawItemView.setItemName(flawItem.getDefectTypeName());
        holder.flawItemView.setCurShowPhoto(map.get(position)==null?false:map.get(position));
        holder.flawItemView.setCurtagPositon(mapTag.get(position)==null?0:mapTag.get(position));
        Set<Integer> set = new HashSet<>();
        if(flawItem.getDefectIdList() != null){
            for(int i = 0;i<flawItem.getDefectDetailList().size();i++){
                for(int y=0;y<flawItem.getDefectIdList().size();y++){
                    if(flawItem.getDefectIdList().get(y).equals(flawItem.getDefectDetailList().get(i).getDefectId())){
                        set.add(i);
                    }

                }
            }

        }
        holder.flawItemView.setLists(flawItem.getDefectDetailList(),set);
        holder.flawItemView.setIFlawPhotoLister(new FlawItemView.IFlawPhotoLister() {
            @Override
            public void show(int tagPositon, int sposition) {
                if(mIchoosePhotoLister != null){
                    mIchoosePhotoLister.show(position,tagPositon,sposition);
                }
            }
        });
        holder.flawItemView.setmIFlawTagLister(new FlawItemView.IFlawTagLister() {
            @Override
            public void show(int tagPositon, String tagId, boolean isShow) {
                if(mIchooseTagLister != null){
                    mIchooseTagLister.show(position,tagPositon,tagId,isShow);
                }
            }

        });
        holder.flawItemView.setmIFlawShowPhotoLister(new FlawItemView.IFlawShowPhotoLister() {
            @Override
            public void show(boolean isShow,int tagPosition) {
                map.put(position,isShow);
                mapTag.put(position,tagPosition);
            }
        });
        holder.flawItemView.setmIchooseTagPhotoPathLister(new FlawAdapter.IchooseTagPhotoPathLister() {

            @Override
            public void show(String photoPath) {
                if(mIchooseTagPhotoPathLister != null){
                    mIchooseTagPhotoPathLister.show(photoPath);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public void setmDatas(List<FlawItem> mDatas) {
        this.mDatas = mDatas;
    }

    public void setmIchoosePhotoLister(IchoosePhotoLister mIchoosePhotoLister) {
        this.mIchoosePhotoLister = mIchoosePhotoLister;
    }

    public void setmIchooseTagLister(IchooseTagLister mIchooseTagLister) {
        this.mIchooseTagLister = mIchooseTagLister;
    }

    public void setmIchooseTagPhotoPathLister(IchooseTagPhotoPathLister mIchooseTagPhotoPathLister) {
        this.mIchooseTagPhotoPathLister = mIchooseTagPhotoPathLister;
    }

    public FlawAdapter(Context context,List<FlawItem> datas) {
        this.context = context;
        mDatas = datas;
    }

    public interface IchoosePhotoLister{
        public void show(int itemPositon,int tagPositon,int position);
    }
    public interface IchooseTagLister{
        public void show(int itemPositon,int tagPositon,String tagId,boolean isShow);
    }
    public interface IchooseTagPhotoPathLister{
        public void show(String photoPath);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private FlawItemView flawItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            flawItemView = (FlawItemView) itemView.findViewById(R.id.flawList);
        }
    }
}
