package com.jcpt.jzg.padsystem.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.vo.CarTypeSelectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 车型---备选款型
 * Created by zealjiang on 2016/11/4 20:10.
 * Email: zealjiang@126.com
 */

public class CarTypeListAdapter extends RecyclerView.Adapter<CarTypeListAdapter.SimpleItemViewHolder>{

    protected List<CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean> datas;
    private OnItemClickListener clickListener;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private DetectMainActivity detectMainActivity;

    public CarTypeListAdapter(List<CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean> datas){
        this.datas = datas;
        isClicks = new ArrayList<>();
        for(int i = 0;i<datas.size();i++){
            isClicks.add(false);
        }
    }

    public void setDetectMainActivity(DetectMainActivity detectMainActivity){
        this.detectMainActivity = detectMainActivity;
    }

    /**
     * 重置所有项为非选中
     * @author zealjiang
     * @time 2016/11/10 10:12
     */
    public void resetClicks(){
        isClicks.clear();
        for(int i = 0;i<datas.size();i++){
            isClicks.add(false);
        }
    }

    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_type_list,parent,false);
        SimpleItemViewHolder simpleItemViewHolder = new SimpleItemViewHolder(itemView);
        return simpleItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final SimpleItemViewHolder holder, final int position) {

        CarTypeSelectModel.MemberValueBean.ListBeanX.ListBean dataBean = datas.get(position);
        boolean isTitle = dataBean.isTitle();
        if(isTitle){
            holder.tvYear.setVisibility(View.VISIBLE);
            holder.tvYear.setText(dataBean.getYear());
        }else{
            holder.tvYear.setVisibility(View.GONE);
        }
        holder.tvCarType.setText(dataBean.getName());
        holder.tvPrice.setText(dataBean.getNowMsrp()+"万元");
        if(isClicks.get(position)){
            holder.tvCarType.setTextColor(holder.tvCarType.getContext().getResources().getColor(R.color.common_orange_light));
        }else{
            holder.tvCarType.setTextColor(holder.tvCarType.getContext().getResources().getColor(R.color.global_black));
        }

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*              //此为点击item就将item的颜色置为选中的红色
                for(int i = 0; i <isClicks.size();i++){
                    isClicks.set(i,false);
                }
                isClicks.set(position,true);
                notifyDataSetChanged();*/
                if(clickListener!=null){
                    clickListener.onItemClick(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (this.datas != null) ? this.datas.size() : 0 ;
    }

    protected final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvYear;
        protected TextView tvCarType;
        protected TextView tvPrice;
        protected LinearLayout llItem;

        public SimpleItemViewHolder (View itemView) {
            super(itemView);
            this.tvYear = (TextView) itemView.findViewById (R.id.tv_year);
            this.tvCarType = (TextView) itemView.findViewById (R.id.tv_car_type);
            this.tvPrice = (TextView) itemView.findViewById (R.id.tv_price);
            this.llItem = (LinearLayout) itemView.findViewById (R.id.ll_item);
        }
    }

    /**
     * 获取选中的车型位置
     * @author zealjiang
     * @time 2016/11/11 20:42
     */
    public int getSelectedPosition(){
        for(int i = 0; i <isClicks.size();i++){
            if(isClicks.get(i)){
                return i;
            }
        }
        return -1;
    }

    public List<Boolean> getIsClicks(){
        return isClicks;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }

}
