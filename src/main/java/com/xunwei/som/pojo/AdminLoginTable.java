package com.xunwei.som.pojo;

public class AdminLoginTable {
    private Integer priNumber;

    private String priGroup;

    private String selPri;

    private String delPri;

    private String updPri;

    private String addPri;

    public Integer getPriNumber() {
        return priNumber;
    }

    public void setPriNumber(Integer priNumber) {
        this.priNumber = priNumber;
    }

    public String getPriGroup() {
        return priGroup;
    }

    public void setPriGroup(String priGroup) {
        this.priGroup = priGroup == null ? null : priGroup.trim();
    }

    public String getSelPri() {
        return selPri;
    }

    public void setSelPri(String selPri) {
        this.selPri = selPri == null ? null : selPri.trim();
    }

    public String getDelPri() {
        return delPri;
    }

    public void setDelPri(String delPri) {
        this.delPri = delPri == null ? null : delPri.trim();
    }

    public String getUpdPri() {
        return updPri;
    }

    public void setUpdPri(String updPri) {
        this.updPri = updPri == null ? null : updPri.trim();
    }

    public String getAddPri() {
        return addPri;
    }

    public void setAddPri(String addPri) {
        this.addPri = addPri == null ? null : addPri.trim();
    }
}