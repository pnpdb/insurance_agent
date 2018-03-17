package com.gecq.insurance.agent.service.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gecha on 2016/7/16.
 */
public class PageBean<E> implements Serializable {
    private int pageNum;
    private int pageSize;
    private long total;
    private int pages;
    private List<E> rows;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<E> getRows() {
        return rows;
    }

    public void setRows(List<E> rows) {
        this.rows = rows;
    }
}
