package com.xunwei.som.pojo;

import java.util.Date;

public class ConstractExec {
    private String contractCode;

    private String perforOfContract;

    private String terminOfContract;

    private Date earTerminDate;

    private String earTerminInstr;

    private Date transFilDate;

    private String makePople;

    private String remark;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode == null ? null : contractCode.trim();
    }

    public String getPerforOfContract() {
        return perforOfContract;
    }

    public void setPerforOfContract(String perforOfContract) {
        this.perforOfContract = perforOfContract == null ? null : perforOfContract.trim();
    }

    public String getTerminOfContract() {
        return terminOfContract;
    }

    public void setTerminOfContract(String terminOfContract) {
        this.terminOfContract = terminOfContract == null ? null : terminOfContract.trim();
    }

    public Date getEarTerminDate() {
        return earTerminDate;
    }

    public void setEarTerminDate(Date earTerminDate) {
        this.earTerminDate = earTerminDate;
    }

    public String getEarTerminInstr() {
        return earTerminInstr;
    }

    public void setEarTerminInstr(String earTerminInstr) {
        this.earTerminInstr = earTerminInstr == null ? null : earTerminInstr.trim();
    }

    public Date getTransFilDate() {
        return transFilDate;
    }

    public void setTransFilDate(Date transFilDate) {
        this.transFilDate = transFilDate;
    }

    public String getMakePople() {
        return makePople;
    }

    public void setMakePople(String makePople) {
        this.makePople = makePople == null ? null : makePople.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}