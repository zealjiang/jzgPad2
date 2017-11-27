package com.jcpt.jzg.padsystem.vo;

import android.text.TextUtils;

import java.util.List;

/**
 * 任务认领列表
 */
public class TaskClaimList {


    private List<TaskItem> TaskList;
    private List<SourceListBean> SourceList;
    private int SourceID;

    public List<TaskItem> getTaskList() {
        return TaskList;
    }

    public void setTaskList(List<TaskItem> TaskList) {
        this.TaskList = TaskList;
    }

    public List<SourceListBean> getSourceList() {
        return SourceList;
    }

    public void setSourceList(List<SourceListBean> SourceList) {
        this.SourceList = SourceList;
    }

    public int getSourceID() {
        return SourceID;
    }

    public void setSourceID(int sourceID) {
        SourceID = sourceID;
    }

    public static class SourceListBean {
        /**
         * Id : 0
         * Name : 全部
         * TaskCount : 616
         */

        private int Id;
        private String Name;
        private int TaskCount;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getTaskCount() {
            return TaskCount;
        }

        public void setTaskCount(int TaskCount) {
            this.TaskCount = TaskCount;
        }

        @Override
        public boolean equals(Object obj) {
            return TextUtils.equals(((SourceListBean)obj).getName(),Name);
        }
    }
}
