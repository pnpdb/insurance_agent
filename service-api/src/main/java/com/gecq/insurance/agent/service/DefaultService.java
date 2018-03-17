package com.gecq.insurance.agent.service;

import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.models.PageBean;

import java.util.List;

/**
 * Created by gecha on 2016/12/30.
 */
public interface DefaultService {
    List<ModelBean> getDataList(ModelBean conditions);
    PageBean<ModelBean> page(Integer pageNum,int pageSize,ModelBean conditions);
    ModelBean getDataOne(ModelBean conditions);
    int deleteData(ModelBean values);
    int saveOrUpdateData(ModelBean values);
}
