package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.widget.DetectionStatusView;

import java.util.HashMap;
import java.util.List;


/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/17 14:41
 * @desc:车况检测项组合控件adapter
 */
public class DetectionItemAdapter  extends RecyclerView.Adapter<DetectionItemAdapter.ViewHolder> {

    private IDetectionOnclikLister lisenter;
    private Context context;
    private List<CheckItem> data;

    /**
     * Created by 李波 on 2016/11/24.
     * @Des： 定义集合装入对应的item，方便拿取动态更改item的选中状态
     */
    private HashMap<Integer,DetectionStatusView> positionCheckItem=new HashMap<>();

    public DetectionItemAdapter(Context context,List<CheckItem> data) {
        this.context = context;
        this.data = data;
    }


    /**
     * Created by 李波 on 2016/11/24.
     * 获取相应position上的 检测项item
     */
    public DetectionStatusView getPositionCheckItem(int postion){
        return positionCheckItem.get(postion);
    }

    /**
     * Created by 李波 on 2016/11/24.
     *
     * 获取检测项列表
     * 便于 实时更改对应数据到大对象 myAdapter.getDatas().get(curClickPosition).setStatus(status);
     */
    public List<CheckItem> getDatas(){
        return data;
    }

    public  interface IDetectionOnclikLister {
        /**
         * Created by 李波 on 2016/11/24.
         * pos检测项模块位置，positon 左右
         */
        void OnClick(int pos, int position);
    }

    public void setLisenter(IDetectionOnclikLister lisenter) {
        this.lisenter = lisenter;
    }

    public void updateData(List<CheckItem> datas) {
        this.data = datas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detect_option,parent,false);
        DetectionItemAdapter.ViewHolder viewHolder = new DetectionItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.nsvList.setStatus(data.get(position).getStatus());
        holder.nsvList.setItemName(data.get(position).getCheckName());
        positionCheckItem.put(position,holder.nsvList);//存储每个item到集合方便后面拿取   - - 李波 on 2016/11/24.
        holder.nsvList.setDetectionOnclickLister(new DetectionStatusView.IDetectionStatusLister() {
            @Override
            public void Onclick(int pos) {  //设置检测项item左、右点击事件  -> 李波 on 2016/11/24.
                if (lisenter != null) {
                    lisenter.OnClick(position, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private DetectionStatusView nsvList;

        public ViewHolder(View itemView) {
            super(itemView);
            nsvList = (DetectionStatusView) itemView.findViewById(R.id.nsvList);
        }
    }
}
