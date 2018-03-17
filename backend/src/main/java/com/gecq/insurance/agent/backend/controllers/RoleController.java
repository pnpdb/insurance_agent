package com.gecq.insurance.agent.backend.controllers;

import com.gecq.insurance.agent.backend.DefaultController;
import com.gecq.insurance.agent.service.admin.PermissionService;
import com.gecq.insurance.agent.service.admin.RoleService;
import com.gecq.insurance.agent.service.models.FormBean;
import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.models.PageBean;
import com.gecq.insurance.agent.service.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by gecha on 2017/1/9.
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController extends DefaultController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @RequestMapping("/data")
    public String query(@RequestParam(required = false) Integer pageNum, Model model, HttpSession session, FormBean form){
        ModelBean conditions = form.getModel();
        PageBean<ModelBean> page = roleService.page(pageNum,pageSize,conditions);
        checkPermissions(model,session,"roleAdd,roleDel,roleEdit,rolePermissionAssign");
        model.addAttribute("page",page).addAttribute("conditions",conditions).addAttribute("randomId", randomId());
        return "admin/role/index";
    }

    @RequestMapping("/data/opt/add")
    public String add(Model model){
        model.addAttribute("type","add");
        return "/admin/role/edit";
    }
    @RequestMapping("/data/opt/edit")
    public String edit(@RequestParam("rd") String id,Model model){
        ModelBean obj = roleService.getDataOne(new ModelBean("id",id));
        model.addAttribute("model",obj).addAttribute("type","edit");
        return "/admin/role/edit";
    }

    @Autowired
    private JsonUtils jsonUtils;

    @RequestMapping("/data/opt/permission-assign")
    public String permissionAssign(Model model,@RequestParam("rd") String roleId){
        model.addAttribute("type","checkbox").addAttribute("permissions",jsonUtils.toJson(permissionService.getPermissionToAssign(roleId))).addAttribute("roleId",roleId);
        return "/admin/role/permission-assign";
    }

    @RequestMapping("/data/opt/{type}/save.json")
    public void save(@PathVariable("type")String type, HttpServletResponse response, FormBean form){
        if(!"add".equals(type)&"edit".equals(type)){
            writeError(response,"未知操作！");
            return;
        }
        ModelBean modelBean = form.getModel();
        if(modelBean==null){
            writeError(response,"无可操作数据！");
            return;
        }
        int result = roleService.saveOrUpdateData(modelBean);
        if(result==1){
            writeSuccess(response);
        }else{
            writeError(response,"角色数据保存失败！");
        }
    }
}
