package com.xunwei.som.base.model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 分页输出参数
 */
@XmlRootElement(name="PagedList")
@XmlAccessorType(XmlAccessType.FIELD)
public class PagedList<T> extends PagedIn {
    private int recordCount;  //总记录数
    @XmlElementWrapper(name="list")
    @XmlElement(name="model")
    private List<T> list;     //列表

    public PagedList(){
        this(25,0,0);
    }

    public PagedList(int pageSize, int pageIndex,int recordCount){
        super(pageSize, pageIndex);
        this.recordCount = recordCount;

    }

    //region getter and setter
    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getPageCount() {
        return recordCount > 0 ? (int) Math.ceil(recordCount/(double) pageSize) : 0;
    }

    public boolean hasPreviousPage() {
        return pageIndex > 0;
    }

    public boolean hasNextPage()
    {
        return pageIndex < (getPageCount() - 1);
    }

    public boolean isFirstPage()
    {
        return pageIndex <= 0;
    }

    public boolean isLastPage()
    {
        return pageIndex >= (getPageCount() - 1);
    }


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
    //endregion

}
