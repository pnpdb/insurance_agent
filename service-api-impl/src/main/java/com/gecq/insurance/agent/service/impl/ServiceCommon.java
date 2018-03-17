package com.gecq.insurance.agent.service.impl;

import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.models.PageBean;
import com.github.pagehelper.Page;

import java.util.UUID;

/**
 * Created by gecha on 2016/8/5.
 */
public class ServiceCommon {
    protected final String genUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
    protected final PageBean<ModelBean> buildPageBean(int pageNum, int pageSize, Page<ModelBean> page){
        PageBean<ModelBean> result = new PageBean<ModelBean>();
        result.setPageNum(pageNum);
        result.setPages(page.getPages());
        result.setPageSize(pageSize);
        result.setTotal(page.getTotal());
        result.setRows(page.getResult());
        return result;
    }
}
