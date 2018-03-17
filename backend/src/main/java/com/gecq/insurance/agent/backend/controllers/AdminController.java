package com.gecq.insurance.agent.backend.controllers;

import com.gecq.insurance.agent.backend.DefaultController;
import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.utils.CacheUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by gecha on 2016/12/30.
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends DefaultController {
    @RequestMapping("/home")
    public String home(Model model, HttpSession session){
        ModelBean admin = getCurrentAdmin(session);
        model.addAttribute("treeMenu",admin.get("treeMenu")).addAttribute("admin",getCurrentAdmin(session));
        return "/admin/index";
    }
    @RequestMapping(value = "/login/out",method = RequestMethod.POST)
    public String loginOut(HttpSession session){
        cacheUtils.remove(CacheUtils.Cache.admin,getCurrentUserId(session));
        session.invalidate();
        return "redirect:/";
    }
}
