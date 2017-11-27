package com.jcpt.jzg.padsystem.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.base.BaseFragment;

import java.util.ArrayList;

public class NewProcedureFragment extends BaseFragment {
    private String [] tflCardTypeLists = {"身份证","军官证","护照","组织机构代码证","车主证件未见"};
    private String [] tflDjzZhezhuLists = {"一致","不一致"};
    private String [] tflChepaihaomaLists = {"无车牌","未悬挂"};
    private String [] tflFuelTypeLists = {"汽油","柴油","混动","天然气","纯电动"};
    private String [] tflBusloadLists = {"2","4","5","6","7","8","9","9以上"};
    private String [] tflHuodeFangshiLists = {"购买","仲裁裁判","继承","赠与","协议抵偿债务","中奖",
            "资产重组","资产整体买卖","调拨","境外自带","法院调解、裁定、判决"};
    private String [] tflCarColorLists = {"白","灰","红","粉","黄","蓝", "绿","紫","棕","黑","其他"};
    private String [] tflJinkouGuochanLists = {"国产","进口"};
    private String [] tflPailiangLists = {"T"};
    private String [] tflShiyongxingzhiLists = {"非营运","营转非","出租营转非","营运","租赁","警用","消防",
            "救护","工程抢险","货运","公路客运","公交客运","出租客运","旅游客运"};
    private String [] tflXianshiyongfangLists = {"仅个人记录","有单位记录","有出租车记录","有汽车租赁公司记录","有汽车销售（服务）公司记录"};
    private String [] tflJiaoqiangxianbaodanLists = {"正常","未见","被保险人与车主不一致"};
    private String [] tflYuanchefapiaoLists = {"无工商章","未见"};
    private String [] tflQitapiaozhengLists = {"过户票","备用钥匙","进口关单","购置税完税证明（征税）","购置税完税证明（免税）"};
    ArrayList<String> tflQitapiaozhengList = new ArrayList<>();
    private String [] tflDJZFujiaxinxiLists = {"正在抵押","发动机号变更","重打车架号","登记证补领","颜色变更"};
    private String [] tflNianjianyouxiaoqiLists = {"无法判断"};

    @Override
    protected void initData() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_procedure, container, false);
    }

    @Override
    protected void setView() {

    }





}
