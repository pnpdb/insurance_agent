package com.gecq.insurance.agent.service.impl.admin;

import com.gecq.insurance.agent.service.admin.RoleService;
import com.gecq.insurance.agent.service.impl.DefaultServiceImpl;
import com.gecq.insurance.agent.service.impl.persistence.admin.RoleDao;
import com.gecq.insurance.agent.service.models.ModelBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gecha on 2016/12/30.
 */
@Component("roleService")
public class RoleServiceImpl extends DefaultServiceImpl implements RoleService {
    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        super(roleDao);
    }

    public ModelBean getRoleForEdit(String roleId) {
        ModelBean result = new ModelBean();
        if(StringUtils.isNotBlank(roleId)){
            ModelBean role = getDataOne(new ModelBean("id",roleId));
            result.set("role",role);
        }

        return result;
    }
}
