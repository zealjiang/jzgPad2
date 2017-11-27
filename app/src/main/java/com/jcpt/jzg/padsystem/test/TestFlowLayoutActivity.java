package com.jcpt.jzg.padsystem.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagAdapter;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: guochen
 * date: 2016/9/20 10:48
 * email:
 */
public class TestFlowLayoutActivity extends AppCompatActivity {
    @BindView(R.id.id_listview)
    ListView idListview;
    private List<List<String>> mDatas = new ArrayList<List<String>>();
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);
        ButterKnife.bind(this);
        initData();
        initView();

    }

    private void initView() {
        idListview.setAdapter(new CommonAdapter<List<String>>(this, R.layout.item_for_listview, mDatas)
        {
            Map<Integer, Set<Integer>> selectedMap = new HashMap<Integer, Set<Integer>>();


            @Override
            public void convert(final ViewHolder viewHolder, List<String> strings)
            {
                TagFlowLayout tagFlowLayout = viewHolder.getView(R.id.id_tagflowlayout);

                TagAdapter<String> tagAdapter = new TagAdapter<String>(strings)
                {
                    @Override
                    public View getView(FlowLayout parent, int position, String o)
                    {
                        //can use viewholder
                        TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                parent, false);
                        tv.setText(o);
                        return tv;
                    }
                };
                tagFlowLayout.setAdapter(tagAdapter);
                //重置状态
                tagAdapter.setSelectedList(selectedMap.get(viewHolder.getItemPosition()));

                tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
                {
                    @Override
                    public void onSelected(Set<Integer> selectPosSet)
                    {
                        selectedMap.put(viewHolder.getItemPosition(), selectPosSet);
                    }
                });
            }
        });

    }

    private void initData() {
        for (int i = 'A'; i < 'z'; i++)
        {
            List<String> itemData = new ArrayList<String>(10);
            for (int j = 0; j < 10; j++)
            {
                itemData.add((char) i + "");
            }
            mDatas.add(itemData);
        }
    }
}
