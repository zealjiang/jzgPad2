package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.view.CarCompareVerticalItem;
import com.jcpt.jzg.padsystem.vo.AdmixedData;

import java.util.List;

/**
 * Created by liugl on 2016/11/2.
 */

public class CarPicComparedRightAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder vh;
    private AdmixedData datas;
    private CarPicClickInterface carPicClickInterface;


    public CarPicComparedRightAdapter(Context context, AdmixedData datas,CarPicClickInterface carPicClickInterface) {
        this.context = context;
        this.datas = datas;
        this.carPicClickInterface = carPicClickInterface;
    }

    @Override
    public int getCount() {
        return datas.getPhotoData().size();
    }

    @Override
    public Object getItem(int position) {
        return datas.getPhotoData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = new CarCompareVerticalItem(context, datas.getCarData().size());
        vh = new ViewHolder();
        vh.views = ((CarCompareVerticalItem) convertView).getViews();
        convertView.setTag(vh);

        for (int i = 0; i < vh.views.size(); i++) {
            if("-".equals(datas.getPhotoData().get(position).getPropertyValue().get(i))){
                vh.views.get(i).setImageResource(R.drawable.jingzhengu_moren);
            }else {
                String[] pics = datas.getPhotoData().get(position).getPropertyValue().get(i).split("#");
                if ( pics!=null && pics.length>0){
                    vh.views.get(i).setImageURI(pics[0]);
                }
            }
            final int index = i;
            vh.views.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carPicClickInterface.onCarPicItemOnClick(position,(Integer)((vh.views.get(index)).getTag()));
                }
            });
        }
        return convertView;
    }
    class ViewHolder {
        public List<SimpleDraweeView> views;
    }


    public interface CarPicClickInterface{
        public void onCarPicItemOnClick(int row, int colum);
    }

}
