package com.jcpt.jzg.padsystem.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.FlawAdapter;
import com.jcpt.jzg.padsystem.adapter.MyTagStringAdapter;
import com.jcpt.jzg.padsystem.image.util.FileUtils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.FlawItem;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * zyq
 */
public class FlawItemView extends LinearLayout {
    private TextView tvItemName;
    private TagFlowLayout tagFlowLayout;
    private RecyclerView recyclerView;
    private Activity mContext;
    MyTagStringAdapter displacementAdapter;
    PhotoAdapter photoAdapter;
    private String itemName;

    private List<FlawItem.DefectDetailListBean> lists;
    private List<String> photoLists = new ArrayList<>();

    IFlawPhotoLister IFlawPhotoLister;
    IFlawTagLister mIFlawTagLister;
    IFlawShowPhotoLister mIFlawShowPhotoLister;
    static FlawAdapter.IchooseTagPhotoPathLister mIchooseTagPhotoPathLister;

    public boolean curShowPhoto = false;   //是否显示图片
    public int curtagPositon;  //当前点击tag

    public void setIFlawPhotoLister(FlawItemView.IFlawPhotoLister IFlawPhotoLister) {
        this.IFlawPhotoLister = IFlawPhotoLister;
    }

    public void setmIFlawTagLister(IFlawTagLister mIFlawTagLister) {
        this.mIFlawTagLister = mIFlawTagLister;
    }

    public void setmIFlawShowPhotoLister(IFlawShowPhotoLister mIFlawShowPhotoLister) {
        this.mIFlawShowPhotoLister = mIFlawShowPhotoLister;
    }

    public void setmIchooseTagPhotoPathLister(FlawAdapter.IchooseTagPhotoPathLister mIchooseTagPhotoPathLister) {
        this.mIchooseTagPhotoPathLister = mIchooseTagPhotoPathLister;
    }

    public boolean isCurShowPhoto() {
        return curShowPhoto;
    }

    public void setCurShowPhoto(boolean mcurShowPhoto) {
        this.curShowPhoto = mcurShowPhoto;
    }

    public int getCurtagPositon() {
        return curtagPositon;
    }

    public void setCurtagPositon(int curtagPositon) {
        this.curtagPositon = curtagPositon;
    }

    public FlawItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlawItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = (Activity) context;
        clearPhotoList();
        initView(context);
    }

    private void initView(Context context) {
        View parent = View.inflate(context,R.layout.view_flaw_option,this);
        tvItemName = (TextView) parent.findViewById(R.id.tvFlawItemName);
        tagFlowLayout= (TagFlowLayout)parent.findViewById(R.id.tflFlawType);
        recyclerView =(RecyclerView)parent.findViewById(R.id.rlFlawPhoto);


    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
        tvItemName.setText(itemName);
    }

    public void setLists(List<FlawItem.DefectDetailListBean> lists,Set<Integer> set) {
        this.lists = lists;
        if(lists != null){
            String [] strings = new String[lists.size()];
            for(int i = 0;i<lists.size();i++){
                strings[i] = lists.get(i).getDefectName();
            }
            setTag(strings,tagFlowLayout,set);
            if(curShowPhoto){
                recyclerView.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.GONE);
            }
            showPhoto(lists.get(curtagPositon),curShowPhoto,curtagPositon);

        }
    }

    public void clearPhotoList(){
        photoLists.clear();
        for(int i = 0;i<3;i++){
            photoLists.add("");
        }
    }

    /**
     * 显示照片
     * @param mphotoNameList    照片地址
     * @param isRl      是否显示照片
     * @param tagPositon    当前tag位置
     */
    public void showPhoto(final FlawItem.DefectDetailListBean mphotoNameList,boolean isRl,final int tagPositon) {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(UIUtils.dip2px(mContext,5), UIUtils.dip2px(mContext,5), 0, 0);
        recyclerView.setLayoutParams(lp);
        recyclerView.setLayoutManager(linearLayoutManager);
        clearPhotoList();
        if(mphotoNameList != null){
            for(int i= 0;i<mphotoNameList.getPicDefectIdList().size();i++){
                photoLists.set(i,mphotoNameList.getPicDefectIdList().get(i).getPicDefectIdPer());
            }
        }

        if(isRl){
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.GONE);
        }

        photoAdapter = new FlawItemView.PhotoAdapter(mContext, photoLists);
        recyclerView.setAdapter(photoAdapter);

        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if(IFlawPhotoLister != null){
                    IFlawPhotoLister.show(tagPositon,position);
                }
                if(TextUtils.isEmpty(photoLists.get(position))){
                    //拍照
                    takePhoto(mContext,100,mphotoNameList.getPicDefectId()+"_"+position);

                }else{
                    //查看大图


                }
            }
        });


    }


    public static void takePhoto(Context context, int requestCode,String filename) {

        if (ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA},100);
        } else {
            if(ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }else{
                String filePath = FileUtils.SDCARD_PAHT+"/PAD/Album/";
                File f=new File(filePath);
                if(!f.exists()){
                    f.mkdirs();
                }
                String fileName = filePath+filename+".jpg";
                File f1=new File(fileName);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f1));
                intent.putExtra("Photo_Path",fileName);
                if(mIchooseTagPhotoPathLister != null){
                    mIchooseTagPhotoPathLister.show(fileName);
                }
                ((Activity) context).startActivityForResult(intent, requestCode);
            }

        }
    }









    /**
     * 设置当前 TagFlowLayout数据
     * @param vals  显示的数据
     * @param tagFlowLayout 控件
     * @param set 默认选中项
     */
    public void setTag(final  String[] vals, final TagFlowLayout tagFlowLayout,Set<Integer> set){
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        displacementAdapter = new MyTagStringAdapter(vals,tagFlowLayout,mContext);
        tagFlowLayout.setAdapter(displacementAdapter);
        displacementAdapter.setSelectedList(set);
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(TagView view, int position, FlowLayout parent)
            {
                //如果已选择选项，则弹出是否修改还是取消的确认框
                if(!view.isChecked()){
                    //弹出提示
                    showConfirmTask(position,view);
                }else{
                    curShowPhoto = true;
                    curtagPositon = position;
                    if(mIFlawShowPhotoLister != null){
                        mIFlawShowPhotoLister.show(curShowPhoto,curtagPositon);
                    }

                    if(mIFlawTagLister != null){
                        mIFlawTagLister.show(curtagPositon,lists.get(curtagPositon).getDefectId(),true);
                    }
                    showPhoto(lists.get(curtagPositon),curShowPhoto,curtagPositon);

                }

                return true;
            }
        });
        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                MyToast.showLong(selectPosSet.toString());
                if(selectPosSet.size()>0){
                    for(int s:selectPosSet){
                        MyToast.showLong(vals[s]+""+s);
                    }
                }
            }
        });


    }


    private static class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

        private Context context;
        private List<String> data;
        private PhotoAdapter.OnItemClickListener clickListener;

        public PhotoAdapter(Context context,List<String> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_photo_flaw,parent,false);
            PhotoAdapter.ViewHolder viewHolder = new PhotoAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.ivCar.setTag(data.get(position));
            if(data.get(position).equals(holder.ivCar.getTag())){
                holder.ivCar.setImageURI(data.get(position));
            }

            if(clickListener!=null) {
                holder.ivCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onItemClick(v, position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private SimpleDraweeView ivCar;

            public ViewHolder(View itemView) {
                super(itemView);
                ivCar = (SimpleDraweeView) itemView.findViewById(R.id.imgFlaw);
            }
        }

        public void setOnItemClickListener(PhotoAdapter.OnItemClickListener listener) {
            clickListener = listener;
        }

        interface OnItemClickListener {
            void onItemClick(View itemView, int pos);
        }

    }


    /**
     * 缺陷项编辑，取消或者编辑图片
     * @param position  当前TagFlowLayout点击的位置
     * @param tagView   选中的View
     */
    public void showConfirmTask(final int position,final TagView tagView){
        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(mContext);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_layout);
        TextView tvmessage = (TextView) view.findViewById(R.id.tvmessage);
        tvmessage.setText("请选择您需要的操作");
        TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
        tvleft.setText("删除此缺陷");
        TextView tvright = (TextView) view.findViewById(R.id.tvright);
        tvright.setText("处理照片");
        myUniversalDialog.setLayoutView(view);
        myUniversalDialog.show();
        tvleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
                curShowPhoto = false;
                curtagPositon = position;
                if(mIFlawShowPhotoLister != null){
                    mIFlawShowPhotoLister.show(curShowPhoto,curtagPositon);
                }

                if(mIFlawTagLister != null){
                    mIFlawTagLister.show(curtagPositon,lists.get(curtagPositon).getDefectId(),false);
                }
                showPhoto(null,curShowPhoto,curtagPositon);
            }
        });
        tvright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
                tagView.setChecked(true);
                curShowPhoto = true;
                curtagPositon = position;
                if(mIFlawShowPhotoLister != null){
                    mIFlawShowPhotoLister.show(curShowPhoto,curtagPositon);
                }

                if(mIFlawTagLister != null){
                    mIFlawTagLister.show(curtagPositon,lists.get(curtagPositon).getDefectId(),true);
                }
                showPhoto(lists.get(curtagPositon),curShowPhoto,curtagPositon);
            }
        });



    }

    public interface IFlawPhotoLister{
        /**
         * 缺陷项
         * @param tagPositon TagFlowLayout点击的位置
         * @param position Tag点击图片位置
         */
        public void show(int tagPositon,int position);
    }
    public interface IFlawTagLister{
        /**
         *
         * @param tagPositon
         * @param tagId
         * @param isShow
         */
        public void show(int tagPositon,String tagId,boolean isShow);
    }
    public interface IFlawShowPhotoLister{
        /**
         * 缺陷项图片显示
         * @param isShow    是否有显示图片
         * @param tagPostion    显示图片对应的tag的位置
         */
        public void show(boolean isShow,int tagPostion);
    }

}
