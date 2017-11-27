package com.jcpt.jzg.padsystem.widget.tag;

import android.view.View;

import com.jcpt.jzg.padsystem.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TagAdapter<T> {
    private List<T> mTagDatas;
    private OnDataChangedListener mOnDataChangedListener;
    private HashSet<Integer> mCheckedPosList = new HashSet<Integer>();

    public TagAdapter(List<T> datas) {
        mTagDatas = datas;
    }

    public TagAdapter(T[] datas) {
        mTagDatas = new ArrayList<T>(Arrays.asList(datas));
    }

    interface OnDataChangedListener {
        void onChanged();
    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public void setSelectedList(int... poses) {
        Set<Integer> set = new HashSet<>();
        for (int pos : poses) {
            set.add(pos);
        }
        setSelectedList(set);
    }

    public void setSelectedList(Set<Integer> set) {
        mCheckedPosList.clear();
        if (set != null)
            mCheckedPosList.addAll(set);
        notifyDataChanged();
    }

    HashSet<Integer> getPreCheckedList() {

        return mCheckedPosList;
    }


    public int getCount() {
        return mTagDatas == null ? 0 : mTagDatas.size();
    }

    public void notifyDataChanged() {
        /**
         * 确保当前的选中的标签的位置ID在mTagDatas中
         * zealjiang 2017-2-23
         */
        //removeErrorCheckedPos();
        LogUtil.e("",mCheckedPosList+"");
        mOnDataChangedListener.onChanged();
    }

    /**
     * 去掉错误的选中项
     * @author zealjiang
     * @time 2017/2/24 13:48
     */
    private void removeErrorCheckedPos(){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (int value : mCheckedPosList) {
            if(value>mTagDatas.size()-1){
                if(!temp.contains(value)) {
                    temp.add(value);
                }
            }
        }
        for (int i = 0; i < temp.size(); i++) {
            mCheckedPosList.remove(temp.get(i));
        }
    }

    public T getItem(int position) {
        return mTagDatas.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);

    public boolean setSelected(int position, T t) {
        return false;
    }


}