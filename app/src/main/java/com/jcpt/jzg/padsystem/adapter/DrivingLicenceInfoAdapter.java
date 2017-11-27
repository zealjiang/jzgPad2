package com.jcpt.jzg.padsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.NameValueModel;
import com.jcpt.jzg.padsystem.widget.ClearableEditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 行驶证图片识别
 * Created by zealjiang on 2016/11/16 14:35.
 * Email: zealjiang@126.com
 */

public class DrivingLicenceInfoAdapter extends RecyclerView.Adapter<DrivingLicenceInfoAdapter.ViewHolder> {

    private Context context;
    private List<NameValueModel> data;

    public DrivingLicenceInfoAdapter(Context context, List<NameValueModel> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public DrivingLicenceInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_driving_licence,parent,false);
        DrivingLicenceInfoAdapter.ViewHolder viewHolder = new DrivingLicenceInfoAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DrivingLicenceInfoAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getName());
        holder.cetValue.setText(data.get(position).getValue());


        holder.cetValue.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            String changedText = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //增加字符
                if(before==0&&count>0) {
                    changedText = s.subSequence(start, start + count).toString();
                }else{
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String sFace = ".*[\ud83c\udc00-\ud83c\udfff].*|.*[\ud83d\udc00-\ud83d\udfff].*|.*[\u2600-\u27ff].*";
                StringBuilder sRestric = new StringBuilder();
                StringBuilder sHint = new StringBuilder();

                sRestric.append(sFace);
                sHint.append("不可输入表情符");

                //车辆牌照不可输入I O
                if("车牌号码".equals(data.get(position).getName())){
                    if(s.toString().trim().length()>7){
                        holder.cetValue.removeTextChangedListener(this);
                        data.get(position).setValue(beforeText);
                        holder.cetValue.removeTextChangedListener(this);
                        holder.cetValue.setText(beforeText);
                        holder.cetValue.setSelection(holder.cetValue.getText().length());
                        MyToast.showShort("车牌号码长度不能超过7位");
                        holder.cetValue.addTextChangedListener(this);
                        return;
                    }

                    String limitLetters = "iIoO";

                    char[] chars = limitLetters.toCharArray();

                    StringBuilder sbRegularLimit = new StringBuilder();
                    StringBuilder sbHint = new StringBuilder();
                    for (int i = 0; i < chars.length; i++) {
                        sbRegularLimit.append(".*"+chars[i]+".*|");

                        if(!sbHint.toString().toUpperCase().contains(String.valueOf(chars[i]).toUpperCase())) {
                            sbHint.append(String.valueOf(chars[i]).toUpperCase() + "  ");
                        }
                    }
                    sbRegularLimit.deleteCharAt(sbRegularLimit.length()-1);
                    sbHint.deleteCharAt(sbHint.length()-1);

                    String sLetter = sbHint.toString();
                    String regularLimit = sbRegularLimit.toString();
                    if(!StringUtils.isEmpty(regularLimit)){
                        sRestric = sRestric.length()>0 ? sRestric.append("|").append(regularLimit) : sRestric.append(regularLimit);
                        sHint = sHint.length()>0 ? sHint.append("、").append(sLetter) : sHint.append("不可输入"+sLetter);
                    }

                }else if("VIN码".equals(data.get(position).getName())){
                    if(s.toString().trim().length()>17){
                        holder.cetValue.removeTextChangedListener(this);
                        data.get(position).setValue(beforeText);
                        holder.cetValue.removeTextChangedListener(this);
                        holder.cetValue.setText(beforeText);
                        holder.cetValue.setSelection(holder.cetValue.getText().length());
                        MyToast.showShort("VIN长度不能超过17位");
                        holder.cetValue.addTextChangedListener(this);
                        return;
                    }

                    //最长17位
                    String vinRestric = "0123456789abcdefghjklmnprstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ";
                    for (int i = 0; i < s.toString().trim().length(); i++) {
                        if(!vinRestric.contains(s.toString().substring(i,i+1))){
                            holder.cetValue.removeTextChangedListener(this);
                            MyToast.showShort("VIN不能包含"+s.toString().substring(i,i+1));
                            data.get(position).setValue(beforeText);
                            holder.cetValue.removeTextChangedListener(this);
                            holder.cetValue.setText(beforeText);
                            holder.cetValue.setSelection(holder.cetValue.getText().length());
                            holder.cetValue.addTextChangedListener(this);
                            return;
                        }
                    }
                }

                final String restric = sRestric.toString();
                final String hint = sHint.toString();


                if(changedText.equals("")){
                    data.get(position).setValue(s.toString());
                    return;
                }
                Pattern p = Pattern.compile(restric);
                Matcher m = p.matcher(s.toString());
                if(m.matches()){
                    holder.cetValue.removeTextChangedListener(this);
                    data.get(position).setValue(beforeText);
                    holder.cetValue.removeTextChangedListener(this);
                    holder.cetValue.setText(beforeText);
                    MyToast.showShort(hint);
                    holder.cetValue.setSelection(holder.cetValue.getText().length());
                    holder.cetValue.addTextChangedListener(this);
                    return;
                }else {
                    holder.cetValue.removeTextChangedListener(this);
                    //手动修改后设置新值
                    holder.cetValue.removeTextChangedListener(this);
                    data.get(position).setValue(s.toString());
                    holder.cetValue.setText(s.toString());
                    if("VIN码".equals(holder.tvName.getText())){
                        holder.cetValue.setText(s.toString().toUpperCase());
                    }
                    holder.cetValue.addTextChangedListener(this);
                }

                holder.cetValue.setSelection(holder.cetValue.getText().length());

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView  tvName;
        private com.jcpt.jzg.padsystem.widget.ClearableEditText cetValue;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            cetValue = (ClearableEditText) itemView.findViewById(R.id.tv_value);
        }
    }

}
