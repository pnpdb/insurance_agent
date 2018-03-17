package com.gecq.insurance.agent.service.impl.persistence;

import com.gecq.insurance.agent.service.models.ModelBean;

import java.util.List;

/**
 * Created by gecha on 2016/12/30.
 */
public interface DefaultDao {
    int delete(ModelBean conditions);
    int insert(ModelBean values);
    int update(ModelBean values);
    int updateState(ModelBean values);
    int sizeCount(ModelBean conditions);
    List<ModelBean> findList(ModelBean conditions);
    ModelBean findOne(ModelBean conditions);
}
