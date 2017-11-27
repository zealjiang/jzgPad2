package com.jcpt.jzg.padsystem.ui.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.view.HackyViewPager;
import com.jcpt.jzg.padsystem.view.TouchImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ViewPager界面
 *
 * @author jzg
 * @Date 2015-05-08
 */

public class ViewPagerPhotoAty extends BaseActivity implements
        OnPageChangeListener, OnClickListener {


    @BindView(R.id.viewpager_photo)
    ViewPager viewpager;
    @BindView(R.id.viewGroup)
    ViewGroup viewGroup;
    @BindView(R.id.mycar_details_bottom_textview)
    TextView mycar_details_bottom_textview;

    /**
     * adapter里填充的view集合
     */
    private List<View> list = new ArrayList<View>();

    /**
     * 图片地址集合
     */
    private List<String> items = new ArrayList<String>();

    /**
     * 点的数组
     */
    private ImageView[] tips;

    private LayoutInflater inflater;
    private TouchImageView image;
    private View item;

    private PicAdapter picAdapter;

    private int currentPosition = 0;

    private static final String ISLOCKED_ARG = "isLocked";

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.viewpager_photo_layout);
        ButterKnife.bind(this);
        init();

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) viewpager).setLocked(isLocked);
        }
    }

    @Override
    protected void setData() {

    }

    @SuppressLint("NewApi")
    public void init() {

        items = getIntent().getBundleExtra("imgListsBundle").getStringArrayList("imgLists");
        currentPosition = getIntent().getBundleExtra("imgListsBundle").getInt("itemPosition");


        // 将点加入ViewGroup中
        if (items.size() > 1) {

            tips = new ImageView[items.size()];

            for (int i = 0; i < tips.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LayoutParams(10, 10));
                tips[i] = imageView;
                if (i == currentPosition) {
                    tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
                } else {
                    tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        new LayoutParams(LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT));
                layoutParams.leftMargin = 5;
                layoutParams.rightMargin = 5;

                viewGroup.addView(imageView, layoutParams);
            }


        }

        if (items.size() > 0) {
            mycar_details_bottom_textview.setText((currentPosition + 1) + "/" + items.size());
        }
        inflater = LayoutInflater.from(getApplicationContext());

        for (int i = 0; i < items.size(); i++) {
            item = inflater.inflate(R.layout.viewpager_pics_item, null);
            list.add(item);
        }

        picAdapter = new PicAdapter(list, items);
        viewpager.setAdapter(picAdapter);
        viewpager.setOnPageChangeListener(this);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setCurrentItem(currentPosition);


    }

    public void mycar_details_back(View v) {
        this.finish();
    }




    /**
     * 轮播图片适配器
     *
     * @author 作者 E-mail:
     * @version 创建时间：2015-1-7 下午8:15:34 类说明
     */
    private class PicAdapter extends PagerAdapter {

        private List<View> mList;
        private List<String> mUrls;

        public List<View> getmList() {
            return mList;
        }

        public void setmList(List<View> mList) {
            this.mList = mList;
        }

        public List<String> getmUrls() {
            return mUrls;
        }

        public void setmUrls(List<String> mUrls) {
            this.mUrls = mUrls;
        }

        public PicAdapter(List<View> list, List<String> urls) {
            mList = list;
            mUrls = urls;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return mList.size();
        }

        /**
         * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
         * instantiateItem(View container, int position) This method was
         * deprecated in API level . Use instantiateItem(ViewGroup, int)
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        /**
         * Create the page for the given position.
         */
        @SuppressLint("NewApi")
        @Override
        public Object instantiateItem(final ViewGroup container,
                                      final int position) {

            View view = mList.get(position);
            image = ((TouchImageView) view.findViewById(R.id.viewpager_view));

            if (!TextUtils.isEmpty(mUrls.get(position))) {
                if (mUrls.get(position).indexOf("http") != -1) {
                    image.setImageURI(Uri.parse(mUrls.get(position)));
                } else if (mUrls.get(position).indexOf("drawable://") != -1) {
                } else if("-".equals(mUrls.get(position))){
                    image.setImageResource(R.drawable.jingzhengu_moren);
                } else {
                }


            }


            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ViewPagerPhotoAty.this.finish();
                }
            });

            container.removeView(mList.get(position));
            container.addView(mList.get(position));
            return mList.get(position);

        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        currentPosition = arg0;
        setImageBackground(arg0 % tips.length);
        mycar_details_bottom_textview.setText((arg0 + 1) + "/" + tips.length);
    }

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

}
