package com.gecq.insurance.agent.service.admin;

import com.gecq.insurance.agent.service.DefaultService;
import com.gecq.insurance.agent.service.models.ModelBean;

import java.util.List;

/**
 * Created by gecha on 2016/12/30.
 */
public interface PermissionService extends DefaultService{
    List<ModelBean> getUserPermissions(String userId);
    ModelBean getPermissionForEdit(String permissionId,String parentId);
    List<ModelBean> getPermissionToAssign(String roleId);
}
