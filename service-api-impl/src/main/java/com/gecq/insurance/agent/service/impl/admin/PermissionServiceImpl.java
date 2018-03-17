package com.gecq.insurance.agent.service.impl.admin;

import com.gecq.insurance.agent.service.admin.PermissionService;
import com.gecq.insurance.agent.service.impl.DefaultServiceImpl;
import com.gecq.insurance.agent.service.impl.persistence.admin.PermissionDao;
import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.models.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by gecha on 2016/12/30.
 */
@Component("permissionService")
public class PermissionServiceImpl extends DefaultServiceImpl implements PermissionService {
    @Autowired
    PermissionServiceImpl(PermissionDao permissionDao) {
        super(permissionDao);
    }

    public List<ModelBean> getUserPermissions(String userId) {
        ModelBean conditions = new ModelBean("userId",userId);
        conditions.set("cols","rp.name,rp.url,rp.code,rp.id,rp.parent_id,rp.url_pattern,rp.level,rp.type");
        return ((PermissionDao)getDao()).findByUserId(conditions);
    }

    @SuppressWarnings("unchecked")
    public ModelBean getPermissionForEdit(String permissionId,String parentId) {
        ModelBean parent = new ModelBean("id","top");
        ModelBean conditions = new ModelBean("cols","id,parent_id,name");
        getPermissions(parent,conditions,parentId);
        if(StringUtils.isNotBlank(permissionId)){
            parent.set("permission",getDataOne(new ModelBean("id",permissionId)));
        }
        return parent;
    }

    private void getPermissions(ModelBean parent,ModelBean conditions,String selected){
        List<ModelBean> permissions = getDao().findList(conditions.set("parentId",parent.getId()));
        parent.set("children",permissions);
        for(ModelBean permission:permissions){
            if(selected!=null&&selected.contains(permission.getId())){
                permission.set("selected",true);
            }
            getPermissions(permission,conditions,selected);
        }
    }

    @Override
    public PageBean<ModelBean> page(Integer pageNum, int pageSize, ModelBean conditions) {
        conditions.set("cols","id,parent_id,name,code,url,state");
        return super.page(pageNum, pageSize, conditions);
    }

    @SuppressWarnings("unchecked")
    public List<ModelBean> getPermissionToAssign(String roleId) {
        ModelBean parent = new ModelBean("id","top");
        ModelBean conditions = new ModelBean("cols","id,parent_id,name");
        List<ModelBean> permissionIds = ((PermissionDao)getDao()).findPermissionIdsByRoleId(roleId);
        StringBuilder sb = new StringBuilder();
        for(ModelBean permissionId:permissionIds){
            sb.append(permissionId.getString("permissionId")).append(",");
        }
        getPermissions(parent,conditions,sb.toString());
        return (List<ModelBean>) parent.get("children");
    }
}
