package com.jcpt.jzg.padsystem.vo.detection;

import java.io.Serializable;

/**
 * Created by wujj on 2017/3/31.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class StarScoreItem implements Serializable {
    private int FinalScoreIntervalId;
    private String ReportProjectId;
    private String ResultDes;
    private int ProgrammeId;

    public int getFinalScoreIntervalId() {
        return FinalScoreIntervalId;
    }

    public void setFinalScoreIntervalId(int finalScoreIntervalId) {
        FinalScoreIntervalId = finalScoreIntervalId;
    }

    public String getReportProjectId() {
        return ReportProjectId;
    }

    public void setReportProjectId(String reportProjectId) {
        ReportProjectId = reportProjectId;
    }

    public String getResultDes() {
        return ResultDes;
    }

    public void setResultDes(String resultDes) {
        ResultDes = resultDes;
    }

    public int getProgrammeId() {
        return ProgrammeId;
    }

    public void setProgrammeId(int programmeId) {
        ProgrammeId = programmeId;
    }
}
