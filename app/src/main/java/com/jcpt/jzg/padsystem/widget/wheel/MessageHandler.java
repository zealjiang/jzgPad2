package com.jcpt.jzg.padsystem.widget.wheel;

import android.os.Handler;
import android.os.Message;

/**
 * created by wujj on 2017/11/10
 */
public class MessageHandler extends Handler {
  public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
  public static final int WHAT_SMOOTH_SCROLL = 2000;
  public static final int WHAT_ITEM_SELECTED = 3000;

  final WheelView loopview;

  MessageHandler(WheelView loopview) {
      this.loopview = loopview;
  }

  @Override
  public final void handleMessage(Message msg) {
      switch (msg.what) {
          case WHAT_INVALIDATE_LOOP_VIEW:
              loopview.invalidate();
              break;

          case WHAT_SMOOTH_SCROLL:
              loopview.smoothScroll(WheelView.ACTION.FLING);
              break;

          case WHAT_ITEM_SELECTED:
              loopview.onItemSelected();
              break;
      }
  }

}
