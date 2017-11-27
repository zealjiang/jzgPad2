package com.jcpt.jzg.padsystem.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.DetectionItemAdapter;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.model.DetectionModel;
import com.jcpt.jzg.padsystem.ui.fragment.DetectionFragment;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.InputUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.widget.ClearableEditText;
import com.jcpt.jzg.padsystem.widget.GridSpacingItemDecoration;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagAdapter;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by libo on 2017/8/21.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 检测项搜索界面
 */
public class SearchCheckActivity extends BaseActivity implements View.OnLayoutChangeListener {


    @BindView(R.id.tv_important)
    TextView tvImportant;
    @BindView(R.id.rvOptions)
    RecyclerView rvOptions;
    @BindView(R.id.tv_un_important)
    TextView tvUnImportant;
    @BindView(R.id.UnimportantRvOptions)
    RecyclerView UnimportantRvOptions;
    @BindView(R.id.scrollViewDetection)
    NestedScrollView scrollViewDetection;
    @BindView(R.id.etCheck)
    ClearableEditText etCheck;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.hotSearchTfl)
    TagFlowLayout hotSearchTfl;
    @BindView(R.id.tvIm)
    TextView tvIm;
    @BindView(R.id.tvUnIm)
    TextView tvUnIm;
    @BindView(R.id.ll)
    LinearLayout ll;

    boolean isFirstEnten;

    /**
     * Created by 李波 on 2016/11/24.
     * 重点检测项的 adapter
     */
    private DetectionItemAdapter myAdapter;
    private DetectionItemAdapter UnmyAdapter;

    private TagAdapter mTagAdapter;

    /**
     * Created by 李波 on 2016/11/24.
     * CheckPositionList 方位集合下面 重点检查项分支
     */
    private ImportantItem importantItem;
    private ImportantItem unImportantItem;

    /**
     * Created by 李波 on 2016/11/24.
     * ImportantItem 重点 里各个方位对应大照片集合
     * 对应检测项上面横向的一排 照片
     */
//    private ArrayList<PictureItem> pictureItems;
    /**
     * Created by 李波 on 2016/11/24.
     * ImportantItem 重点 里 各个检测项
     */
    private List<CheckItem> checkItemList;
    private List<CheckItem> UncheckItemList;

    /**
     * Created by 李波 on 2016/11/24.
     * 检查项 Model
     */
    private DetectionModel detectionModel;

    //                       翼子板、车门、机舱盖、后备箱盖、保险杠、发动机、变速箱、水箱框架  默认检测项热搜词  -> 李波 on  2017/8/21.
    private String[] tagHotItemSearch = {"翼子板", "车门", "机舱盖", "后备箱盖", "保险杠", "发动机", "变速箱", "水箱框架"};

    private String searchItem;

    private TagView tagV; //当前选中的热搜词

    private Action1 action1;

    private Subscription subscribe;

    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_check);
        activityRootView = findViewById(R.id.ll);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        ButterKnife.bind(this);
        checkItemList = new ArrayList<>();
        UncheckItemList = new ArrayList<>();
        detectionModel = new DetectionModel(this);
        isFirstEnten = true;
    }

    @Override
   protected void onResume() {
            super.onResume();
              //添加layout大小发生改变监听器
              activityRootView.addOnLayoutChangeListener(this);
     }


    @Override
    protected void setData() {
        //重点检测项
        rvOptions.setLayoutManager(new GridLayoutManager(this, 2));
        rvOptions.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        rvOptions.setNestedScrollingEnabled(false);
        UnimportantRvOptions.setLayoutManager(new GridLayoutManager(this, 2));
        UnimportantRvOptions.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        UnimportantRvOptions.setNestedScrollingEnabled(false);

        setTag(hotSearchTfl, tagHotItemSearch);

        action1 = new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                if (tagV != null && tagV.isChecked()) //热搜选中后定时清除选中状态   -> 李波 on 2017/8/25.
                    tagV.setChecked(false);
            }
        };
        setListener();

    }


    @OnClick({R.id.tvCancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCancle:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }


    private void setListener() {


        etCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){ //当搜索框为空时清除热搜选中状态  -> 李波 on 2017/9/13.
                    if (tagV != null && tagV.isChecked()) {
                        tagV.setChecked(false);
                    }
                }

                if (s.length() > 0) startSearch();
            }
        });

        //设置如果是小写字母自动转换成大写
        etCheck.setTransformationMethod(new InputLowerToUpper());
        InputUtil.editTextfilter(etCheck);

        etCheck.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //隐藏软键盘
                    UIUtils.hideInputMethodManager(SearchCheckActivity.this);

                    searchItem = etCheck.getText().toString().trim().toUpperCase();

                    if (searchItem != null && searchItem.length() > 0) {
                        startSearch();
                    } else {
                        MyToast.showShort("请输入检测项关键字");
                    }

                }
                return false;
            }
        });
    }


    private void startSearch() {
        if (scrollViewDetection.getVisibility() == View.GONE) {
            scrollViewDetection.setVisibility(View.VISIBLE);
        }
        searchItem = etCheck.getText().toString().trim().toUpperCase();
        searchCheckItemList(searchItem);
        updateData();
    }

    private void updateData() {
//        cancleTavCheckState();
        //重点检测项  -> 李波 on 2017/8/21.
        if (checkItemList.size() > 0) {
            tvImportant.setText("重点(" + checkItemList.size() + ")");
            tvIm.setVisibility(View.GONE);
        } else {
            tvImportant.setText("重点");
            tvIm.setVisibility(View.VISIBLE);
        }

        if (myAdapter == null) {
            myAdapter = new DetectionItemAdapter(this, checkItemList);
            rvOptions.setAdapter(myAdapter);

            myAdapter.setLisenter(new DetectionItemAdapter.IDetectionOnclikLister() {
                @Override //pos 是当前点击的item位置角标，position是item条目点击位置：1-左边，2-右边 - - - 李波
                public void OnClick(int pos, int position) {
                    //获取当前点击的检测项状态：0-未选中，1-正常，2-缺陷  -> 李波 on 2016/11/24.
                    int status = myAdapter.getPositionCheckItem(pos).getStatus();
                    switch (position) {
                        case 1:   //点击检测项 - - 左边放大镜弹出右侧侧滑缺陷项popwindow - - 李波
                            detectionModel.showDefectPopWindow(checkItemList, pos);
                            detectionModel.setPopupWindowListener(pos, myAdapter);
                            DetectionFragment.detectionIsFix = true;
                            break;
                        case 2:   //点击检测项 - - 右边对勾 - -李波
                            //                                MyToast.showShort("右边");
                            if (status == 2) {
                                MyToast.showShort("此检测项处于缺陷状态");
                            } else if (status == 0) {  //如果是未选中状态，置为 1-- OK 蓝色状态
                                detectionModel.changeStatusAndData(myAdapter, pos, 1);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

        } else {
            myAdapter.updateData(checkItemList);
        }


        //非重点检测项  -> 李波 on 2017/8/21.
        if (UncheckItemList.size() > 0) {
            tvUnImportant.setText("非重点(" + UncheckItemList.size() + ")");
            tvUnIm.setVisibility(View.GONE);
        } else {
            tvUnImportant.setText("非重点");
            tvUnIm.setVisibility(View.VISIBLE);
        }

        UnimportantRvOptions.setVisibility(View.VISIBLE);
        if (UnmyAdapter == null) {

            UnmyAdapter = new DetectionItemAdapter(this, UncheckItemList);

            UnimportantRvOptions.setAdapter(UnmyAdapter);

            UnmyAdapter.setLisenter(new DetectionItemAdapter.IDetectionOnclikLister() {
                @Override //pos 是当前点击的item位置角标，position是item条目点击位置：1-左边，2-右边 - - - 李波
                public void OnClick(int pos, int position) {
                    //获取当前点击的检测项状态：0-未选中，1-正常，2-缺陷  -> 李波 on 2016/11/24.
                    int status = UnmyAdapter.getPositionCheckItem(pos).getStatus();
                    switch (position) {
                        case 1:   //点击检测项 - - 左边放大镜弹出右侧侧滑缺陷项popwindow - - 李波
                            detectionModel.showDefectPopWindow(UncheckItemList, pos);
                            detectionModel.setPopupWindowListener(pos, UnmyAdapter);
                            break;
                        case 2:   //点击检测项 - - 右边对勾 - -李波
//                                MyToast.showShort("右边");
                            if (status == 2) {
                                MyToast.showShort("此检测项处于缺陷状态");
                            } else if (status == 0) {  //如果是未选中状态，置为 1-- OK 蓝色状态
                                detectionModel.changeStatusAndData(UnmyAdapter, pos, 1);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

        } else {
            UnmyAdapter.updateData(UncheckItemList);
        }

        scrollViewDetection.smoothScrollTo(0, 0);
    }

    public void setTag(final TagFlowLayout tagFlowLayout, final String[] vals) {
        final LayoutInflater mInflater = LayoutInflater.from(this);
        mTagAdapter = new TagAdapter<String>(vals) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_tag_search, hotSearchTfl,
                        false);

                tv.setText(s);
                return tv;
            }
        };

        tagFlowLayout.setAdapter(mTagAdapter);

        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(TagView view, int position, FlowLayout parent) {

                tagV = (TagView) tagFlowLayout.getChildAt(position);
                TextView tv = (TextView) tagV.getTagView();

                searchItem = tv.getText().toString().toUpperCase();
                etCheck.setText(searchItem);
                etCheck.setSelection(searchItem.length());//将光标移至文字末尾
                UIUtils.hideInputMethodManager(SearchCheckActivity.this);

                if (scrollViewDetection.getVisibility() == View.GONE) {
                    scrollViewDetection.setVisibility(View.VISIBLE);
                }

                searchCheckItemList(searchItem);
                updateData();

                return true;
            }
        });


    }

    /**
     * Created by 李波 on 2017/8/21.
     * 根据搜索词匹配本地所有重点和非重点检测项 拿出合集
     */
    private void searchCheckItemList(String searchItem) {
        checkItemList.clear();
        UncheckItemList.clear();

        //匹配搜索重点检测项合集  -> 李波 on 2017/8/21.
        List<CheckItem> imcheckItemList = DetectMainActivity.detectMainActivity.getCheckItemList();
        for (int i = 0; i < imcheckItemList.size(); i++) {
            if (imcheckItemList.get(i).getCheckName().contains(searchItem)) {
                checkItemList.add(imcheckItemList.get(i));
            }
        }

        //匹配搜索非重点检测项合集  -> 李波 on 2017/8/21.
        List<CheckItem> unimcheckItemList = DetectMainActivity.detectMainActivity
                .getUncheckItemList();
        for (int i = 0; i < unimcheckItemList.size(); i++) {
            if (unimcheckItemList.get(i).getCheckName().contains(searchItem)) {
                UncheckItemList.add(unimcheckItemList.get(i));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DetectionModel.DEFECT_ITEM:
                    iCameraListener.setPhoto();
                    break;
                case DetectionModel.DEFECT_ITEM_RECAPTURE:
                    iCameraListener.recapturePhoto(data);
                    break;
            }
        }
    }

    /**
     * Created by 李波 on 2017/8/25.
     * 延迟两秒自动取消热搜的选中状态
     */
    private void cancleTavCheckState() {
        subscribe = Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread
                ()).subscribe(action1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null) subscribe.unsubscribe();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int
            oldTop, int oldRight, int oldBottom) {

        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
//        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {   //软键盘弹起  -> 李波 on 2017/9/12.
//            if (tagV != null && tagV.isChecked()) { //热搜清除选中状态
//                tagV.setChecked(false);
//            }
//        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) { //软键盘关闭  -> 李波 on 2017/9/12.
//
//            if (tagV != null && !tagV.isChecked()&&isFirstEnten) { //热搜清除选中状态
//                tagV.setChecked(true);
//                isFirstEnten=false;
//            }
//        }

    }

    }
