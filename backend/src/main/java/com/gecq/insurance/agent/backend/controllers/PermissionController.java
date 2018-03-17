package com.gecq.insurance.agent.backend.controllers;

import com.gecq.insurance.agent.backend.DefaultController;
import com.gecq.insurance.agent.service.admin.PermissionService;
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
 * Created by gecha on 2017/1/4.
 */
@Controller
@RequestMapping("/admin/permission")
public class PermissionController extends DefaultController {

    @Autowired
    private PermissionService permissionService;
    @RequestMapping("/data")
    public String query(@RequestParam(required = false) Integer pageNum, Model model, HttpSession session, FormBean form){
        ModelBean conditions = form.getModel();
        PageBean<ModelBean> page = permissionService.page(pageNum,pageSize,conditions);
        checkPermissions(model,session,"permissionAdd,permissionDel,permissionEdit");
        model.addAttribute("page",page).addAttribute("conditions",conditions).addAttribute("randomId", randomId());
        return "admin/permission/index";
    }

    @Autowired
    private JsonUtils jsonUtils;
    @RequestMapping("/data/opt/add")
    public String add(Model model){
        ModelBean obj = permissionService.getPermissionForEdit(null,null);
        model.addAttribute("permissions",jsonUtils.toJson(obj.get("children"))).addAttribute("type","add");
        return "/admin/permission/edit";
    }
    @RequestMapping("/data/opt/edit")
    public String edit(String id,String parent,Model model){
        ModelBean obj = permissionService.getPermissionForEdit(id,parent);
        model.addAttribute("permissions",jsonUtils.toJson(obj.get("children"))).addAttribute("model",obj.get("permission")).addAttribute("type","edit");
        return "/admin/permission/edit";
    }

    @RequestMapping("/data/opt/{type}/save.json")
    public void save(@PathVariable("type")String type, HttpServletResponse response,FormBean form){
        if(!"add".equals(type)&"edit".equals(type)){
            writeError(response,"未知操作！");
            return;
        }
        ModelBean modelBean = form.getModel();
        if(modelBean==null){
            writeError(response,"无可操作数据！");
            return;
        }
        int result = permissionService.saveOrUpdateData(modelBean);
        if(result==1){
            writeSuccess(response);
        }else{
            writeError(response,"权限数据保存失败！");
        }
    }

}
