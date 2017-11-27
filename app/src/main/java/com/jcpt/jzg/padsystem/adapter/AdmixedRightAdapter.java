package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.view.MyVerticalItem;
import com.jcpt.jzg.padsystem.vo.AdmixedData;

import java.util.List;

/**
 *
 * @author zealjiang
 * @time 2016/12/29 15:38
 */
public class AdmixedRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    private Context context;
    private List<AdmixedData.ShowDataBean> showDataList;
    private int carDataSize;

    public AdmixedRightAdapter(Context context, List<AdmixedData.ShowDataBean> showDataList,int carDataSize) {
        this.context = context;
        this.showDataList = showDataList;
        this.carDataSize = carDataSize;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if(showDataList.get(position).getDataType()==2){
            return TYPE_2;//类型名称  如基本参数
        }else{
            return TYPE_1;
        }
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType) {

        View listView = new MyVerticalItem(context, carDataSize);
        View titleView = View.inflate(context, R.layout.admixed_item2, null);

        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_1:
                holder = new MyLVViewHolder(listView);
                break;
            case TYPE_2:
                holder = new MyTitleViewHolder(titleView);

               float dip_50 = context.getResources().getDimension(R.dimen.DIP_50)/ Constants.density;
               float dip_170 = context.getResources().getDimension(R.dimen.DIP_170_L)/ Constants.density;

                int height = SizeUtils.dp2px(context,50);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(SizeUtils.dp2px(context, dip_170 * carDataSize+carDataSize),height);
                ((MyTitleViewHolder)holder).linearLayout.setLayoutParams(layoutParams);
                break;
            }
        return holder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return showDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_1:
                for (int i = 0; i < ((MyLVViewHolder)holder).views.size(); i++) {
                    ((TextView) ((MyLVViewHolder)holder).views.get(i)).setText(showDataList.get(position).getPropertyValue().get(i));
                }
                break;
            case TYPE_2:

                break;
            default:

                break;
        }
    }


    class MyLVViewHolder extends RecyclerView.ViewHolder{
        public List<View> views;

        public MyLVViewHolder(View itemView) {
            super(itemView);
            views = ((MyVerticalItem) itemView).getViews();
        }
    }

    class MyTitleViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;

        public MyTitleViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll);
        }
    }
}
