package com.jcpt.jzg.padsystem.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zealjiang on 2016/12/29 14:24.
 * Email: zealjiang@126.com
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0;
        }
    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}
