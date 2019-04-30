package com.xunwei.som.base.model;

/**
 * 分页输入参数
 */
public class PagedIn {

    protected int pageSize;   //每页记录数，默认25条
    protected int pageIndex;  //页索引，从0开始
    protected int recordIndex;//页记录第一条的序号，如0，25,50...

    public PagedIn(){
        this(25,0);
    }

    public PagedIn(int pageSize, int pageIndex){
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.recordIndex =  pageIndex * pageSize;
    }

    //region getter and setter

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        recordIndex =  pageIndex * pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        recordIndex =  pageIndex * pageSize;
    }

    public int getRecordIndex() {
        return recordIndex;
    }


}
