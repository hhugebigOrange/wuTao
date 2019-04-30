package com.xunwei.som.pojo;

public class AssetInfo {
    private Integer assetNumber;

    private String serialNumber;

    private String wareDep;

    private String perserver;

    private String assetClass;

    private String assetName;

    private String type;

    private Integer monad;

    public Integer getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(Integer assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public String getWareDep() {
        return wareDep;
    }

    public void setWareDep(String wareDep) {
        this.wareDep = wareDep == null ? null : wareDep.trim();
    }

    public String getPerserver() {
        return perserver;
    }

    public void setPerserver(String perserver) {
        this.perserver = perserver == null ? null : perserver.trim();
    }

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass == null ? null : assetClass.trim();
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName == null ? null : assetName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getMonad() {
        return monad;
    }

    public void setMonad(Integer monad) {
        this.monad = monad;
    }
}