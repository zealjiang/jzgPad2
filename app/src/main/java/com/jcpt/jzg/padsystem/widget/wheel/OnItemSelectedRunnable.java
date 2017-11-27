package com.jcpt.jzg.padsystem.widget.wheel;
/**
 * created by wujj on 2017/11/10
 */
final class OnItemSelectedRunnable implements Runnable {
    final WheelView loopView;

    OnItemSelectedRunnable(WheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
    }
}
