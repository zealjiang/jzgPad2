package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/18 17:49
 * @desc:
 */
public class TaskItem implements Serializable{

    /**
     * TaskId : 4654
     * OrderNo : JZG0201201606154654
     * OrgName : 车速贷
     * CityName : 北京北京
     * LinkManName : 啊11441
     * LinkMobile : 13436702510
     * Address : 北京市门头沟区
     * CreateTime : 2016/06/15 15:57:58
     * BeginCheckTime :
     * PicPath : http://imageup.jingzhengu.com/padt/2016/12/12/4654/0d8f9eb5-3350-411b-842b-7cc6d95c24d7_10000.jpg
     * CarName : 奥迪2002.6L 手动
     * VinCode : LFV3A24F973036632
     * BackReason :
     * ReportLink : http://192.168.0.140:8081/Report/pad2.aspx?userId=10&taskId=4654
     * Status : 6
     * PlanId : 5
     * UpdateTime : 2016/12/12 23:03:08
     * ReCheckUserName : 张三
     * CreateUserName  下单人  只有淘车有
     */

    private int TaskId;
    private String OrderNo;
    private String OrgName;
    private String CityName;
    private String LinkManName;
    private String LinkMobile;
    private String Address;
    private String CreateTime;
    private String BeginCheckTime;
    private String PicPath;
    private String CarName;
    private String VinCode;
    private String BackReason;
    private String ReportLink;
    private int Status;
    private String PlanId;
    private String UpdateTime;
    private String ReCheckUserName;
    private int OrderStatus;
    private int ReViewType;
    private String ProductType;
    private int IsPriority;//是否优先    int（默认0,加急1）
    private int OldTaskId;// 初检任务Id int  (0表示没有详情数据，1表示有详情数据)
    private int IsRecheck;// 初检或者复检  int（默认0，初检1，复检2）
    private String YXOrderNo;//淘车编号
    private String CreateUserName;//下单人，只有淘车有
    private String Des;
    private int SpSourceId; //值为1表示宝马，非1表示其他
    private int TaskSourceId; //机构id


    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int TaskId) {
        this.TaskId = TaskId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String OrgName) {
        this.OrgName = OrgName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getLinkManName() {
        return LinkManName;
    }

    public void setLinkManName(String LinkManName) {
        this.LinkManName = LinkManName;
    }

    public String getLinkMobile() {
        return LinkMobile;
    }

    public void setLinkMobile(String LinkMobile) {
        this.LinkMobile = LinkMobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getBeginCheckTime() {
        return BeginCheckTime;
    }

    public void setBeginCheckTime(String BeginCheckTime) {
        this.BeginCheckTime = BeginCheckTime;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String PicPath) {
        this.PicPath = PicPath;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String CarName) {
        this.CarName = CarName;
    }

    public String getVinCode() {
        return VinCode;
    }

    public void setVinCode(String VinCode) {
        this.VinCode = VinCode;
    }

    public String getBackReason() {
        return BackReason;
    }

    public void setBackReason(String BackReason) {
        this.BackReason = BackReason;
    }

    public String getReportLink() {
        return ReportLink;
    }

    public void setReportLink(String ReportLink) {
        this.ReportLink = ReportLink;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getPlanId() {
        return PlanId;
    }

    public void setPlanId(String PlanId) {
        this.PlanId = PlanId;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public String getReCheckUserName() {
        return ReCheckUserName;
    }

    public void setReCheckUserName(String reCheckUserName) {
        ReCheckUserName = reCheckUserName;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public int getReViewType() {
        return ReViewType;
    }

    public void setReViewType(int reViewType) {
        ReViewType = reViewType;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public int getIsPriority() {
        return IsPriority;
    }

    public void setIsPriority(int isPriority) {
        IsPriority = isPriority;
    }

    public int getOldTaskId() {
        return OldTaskId;
    }

    public void setOldTaskId(int oldTaskId) {
        OldTaskId = oldTaskId;
    }

    public int getIsRecheck() {
        return IsRecheck;
    }

    public void setIsRecheck(int isRecheck) {
        IsRecheck = isRecheck;
    }

    public String getYXOrderNo() {
        return YXOrderNo;
    }

    public void setYXOrderNo(String YXOrderNo) {
        this.YXOrderNo = YXOrderNo;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String createUserName) {
        CreateUserName = createUserName;
    }

    public int getSpSourceId() {
        return SpSourceId;
    }

    public void setSpSourceId(int spSourceId) {
        SpSourceId = spSourceId;
    }

    public int getTaskSourceId() {
        return TaskSourceId;
    }

    public void setTaskSourceId(int taskSourceId) {
        TaskSourceId = taskSourceId;
    }
}
