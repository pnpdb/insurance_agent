package com.gecq.insurance.agent.service.impl.persistence.admin;

import com.gecq.insurance.agent.service.impl.persistence.DefaultDao;
import com.gecq.insurance.agent.service.models.ModelBean;

import java.util.List;

/**
 * Created by gecha on 2016/12/30.
 */
public interface PermissionDao extends DefaultDao {
    List<ModelBean> findByUserId(ModelBean conditions);
    List<ModelBean> findPermissionIdsByRoleId(String roleId);
}
