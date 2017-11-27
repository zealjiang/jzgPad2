package com.jcpt.jzg.padsystem.update;


import com.jcpt.jzg.padsystem.base.BaseObject;

/**
 * 软件升级
 * @author zealjiang
 * @time 2016/11/15 17:04
 */
public class UpdateApp extends BaseObject {

    /**
     * VersionCode : 1
     * VersionName : 1.0.0
     * DownloadUrl : http://www.jingzhengu.com/app/yiqidazhong/JzgDzhg.apk
     * UpdateLog : 更新日志：android 修复动弹点赞问题修复图片保存图库未刷新BUG安装包大小：5.50MB
     * UpdateForce : 0
     */

    private MemberValueBean MemberValue;

    public MemberValueBean getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(MemberValueBean MemberValue) {
        this.MemberValue = MemberValue;
    }

    public static class MemberValueBean {
        private String VersionCode;
        private String VersionName;
        private String DownloadUrl;
        private String UpdateLog;
        private String UpdateForce;

        public String getVersionCode() {
            return VersionCode;
        }

        public void setVersionCode(String VersionCode) {
            this.VersionCode = VersionCode;
        }

        public String getVersionName() {
            return VersionName;
        }

        public void setVersionName(String VersionName) {
            this.VersionName = VersionName;
        }

        public String getDownloadUrl() {
            return DownloadUrl;
        }

        public void setDownloadUrl(String DownloadUrl) {
            this.DownloadUrl = DownloadUrl;
        }

        public String getUpdateLog() {
            return UpdateLog;
        }

        public void setUpdateLog(String UpdateLog) {
            this.UpdateLog = UpdateLog;
        }

        public String getUpdateForce() {
            return UpdateForce;
        }

        public void setUpdateForce(String UpdateForce) {
            this.UpdateForce = UpdateForce;
        }
    }
}
