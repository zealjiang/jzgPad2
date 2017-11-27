package com.jcpt.jzg.padsystem.mvpview;

import com.jcpt.jzg.padsystem.base.IBaseView;

import java.util.List;

/**
 * Created by liugl on 2017/2/21.
 */

public interface IPlateType extends IBaseView {
   void success(List<String> suc);
   void fail(String string);
   void error(String string);
}
