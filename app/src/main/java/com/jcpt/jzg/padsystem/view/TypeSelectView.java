package com.jcpt.jzg.padsystem.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.CarTypeAdapter;
import com.jcpt.jzg.padsystem.mvpview.ICarTypeInterface;
import com.jcpt.jzg.padsystem.presenter.CarTypePresenter;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.ShowDialogTool;
import com.jcpt.jzg.padsystem.vo.CarTypeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 车型选择
 * @author zealjiang
 * @time 2017/4/19 16:43
 */
public class TypeSelectView extends LinearLayout implements ICarTypeInterface {


    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.vLine)
    View vLine;

    private com.jcpt.jzg.padsystem.presenter.CarTypePresenter carTypePresenter;
    private CarTypeAdapter carTypeAdapter;
    private List<CarTypeModel.MemberValueBean.ListBean> datas;//包含所有数据
    private CarTypeListView.OnListDataLoadOverListener onListDataLoadOverListener;
    private DetectMainActivity detectMainActivity;
    private Context context;
    private int selectedPosition = -1;
    private TypeSelectView.OnTypeItemClickListener onTypeItemClickListener;
    private ShowDialogTool showDialogTool;

    public TypeSelectView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public TypeSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public TypeSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void setDetectMainActivity(DetectMainActivity detectMainActivity){
        this.detectMainActivity = detectMainActivity;
        carTypeAdapter.setDetectMainActivity(detectMainActivity);
    }

    private void initView() {
        showDialogTool = new ShowDialogTool();
        View.inflate(getContext(), R.layout.widget_car_type_list, this);
        ButterKnife.bind(this);
        tvTitle.setVisibility(GONE);
        vLine.setVisibility(GONE);

        datas = new ArrayList<>();
        rvType.setLayoutManager(new LinearLayoutManager(this.getContext()));
        carTypeAdapter = new CarTypeAdapter(datas);
        rvType.setAdapter(carTypeAdapter);
        carTypeAdapter.setOnItemClickListener(new CarTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {

                int styleId = Integer.valueOf(datas.get(pos).getStyleId());
                String styleName = datas.get(pos).getStyleName();
                String styleYear = datas.get(pos).getStyleYear()+"";
                String styleNowMsrp = datas.get(pos).getStyleNowMsrp();
                String styleFullName = datas.get(pos).getStyleFullName();

                if(onTypeItemClickListener!=null){
                    onTypeItemClickListener.onTypeItemClick(styleId,styleName,styleYear,styleNowMsrp,styleFullName);
                }

            }
        });

        carTypePresenter = new CarTypePresenter(this);
    }


    /**
     * 请求车型
     * @author zealjiang
     * @time 2017/4/19 18:11
     */
    public void requestCarTypeList(String modelid) {
        carTypePresenter.requestCarType(modelid);
    }

    @Override
    public void succeed(CarTypeModel carTypeModel) {
        datas.clear();

        //重新组装数据
        List<CarTypeModel.MemberValueBean> listBean = carTypeModel.getMemberValue();
        for (int i = 0; i < listBean.size(); i++) {
            CarTypeModel.MemberValueBean bean = listBean.get(i);
            List<CarTypeModel.MemberValueBean.ListBean> listDataBeen =  bean.getList();
            if(listDataBeen.size()==0){
                continue;
            }
            for (int j = 0; j < listDataBeen.size(); j++) {
                CarTypeModel.MemberValueBean.ListBean dataBean = listDataBeen.get(j);
                if(j==0){
                    dataBean.setIsTitle(true);
                    dataBean.setStyleYear(bean.getYear()+"");
                }else{
                    dataBean.setIsTitle(false);
                    dataBean.setStyleYear(bean.getYear()+"");
                }
                datas.add(dataBean);
            }
        }

        carTypeAdapter.resetClicks();
        //刷新数据
        carTypeAdapter.notifyDataSetChanged();

    }



    @Override
    public void showError(String error) {
        if(!TextUtils.isEmpty(error))
            MyToast.showLong(error);

        datas.clear();
        //刷新数据
        carTypeAdapter.notifyDataSetChanged();

    }

    @Override
    public void showDialog() {
        showDialogTool.showLoadingDialog(this.getContext());
    }

    @Override
    public void dismissDialog() {
        showDialogTool.dismissLoadingDialog();
    }

    public interface OnTypeItemClickListener{
        public void onTypeItemClick(int styleId,String styleName,String styleYear,String styleNowMsrp,String styleFullName);
    }

    public void setOnTypeItemClickListener(TypeSelectView.OnTypeItemClickListener onTypeItemClickListener){
        this.onTypeItemClickListener = onTypeItemClickListener;
    }

}
