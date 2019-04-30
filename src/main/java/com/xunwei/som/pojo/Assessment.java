package com.xunwei.som.pojo;

public class Assessment {
    private String assessmentName;

    private Integer assessmentValue;

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName == null ? null : assessmentName.trim();
    }

    public Integer getAssessmentValue() {
        return assessmentValue;
    }

    public void setAssessmentValue(Integer assessmentValue) {
        this.assessmentValue = assessmentValue;
    }
}