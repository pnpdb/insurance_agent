package com.gecq.insurance.agent.backend.controllers;

import com.gecq.insurance.agent.backend.DefaultController;
import com.gecq.insurance.agent.backend.utils.SessionConstance;
import com.gecq.insurance.agent.service.models.FormBean;
import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.models.State;
import com.gecq.insurance.agent.service.utils.CacheUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gecha on 2017/1/3.
 */
@Controller
public class CommonController extends DefaultController {
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String signOn(HttpSession session, Model model){
        if(isLogin(session)){
            return "redirect:/admin/home";
        }
        model.addAttribute("sid",md5Digest(session.getId()));
        return "/common/sign-on";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/{sid}/sign/on",method = RequestMethod.POST)
    public String signOn(@PathVariable("sid")String sid, Model model, FormBean form, HttpServletResponse response, HttpSession session){
        ModelBean modelBean = form.getModel();
        if(modelBean.isBlank("account")||modelBean.isBlank("pwd")){
            return "redirect:/";
        }
        if(!md5Digest(session.getId()).equals(sid)){
            writeError(response,"非法操作！");
            return null;
        }
        String loginName = modelBean.getString("account");
        String pwd = modelBean.getString("pwd");
        ModelBean admin = accountService.authorize(loginName,pwd);
        if(admin!=null){
            Object id = cacheUtils.get(CacheUtils.Cache.login, CacheUtils.Cache.login.key());
            List<String> ids = (List<String>)id;
            if(ids!=null){
                if(ids.contains(admin.getId())){
                    model.addAttribute("msg","账号已登录，不可重复登录。").addAttribute("sid",sid);
                    return "/common/sign-on";
                }
            }else{
                ids = new ArrayList<String>();
            }
            if(admin.isBlank("state")||admin.isState(State.disabled)){
                model.addAttribute("msg","账号已被禁用，请联系管理员。").addAttribute("sid",sid);
                return "/common/sign-on";
            }else{
                cookieSet(modelBean.getInt("remember"),response,loginName,pwd);
                session.setAttribute(SessionConstance.accountId.name(),admin.getId());
                ids.add(admin.getId());
                cacheUtils.set(CacheUtils.Cache.login, CacheUtils.Cache.login.key(),ids);
                Object back=session.getAttribute(SessionConstance.returnBack.name());
                if(back!=null){
                    return "redirect:"+back;
                }
                return "redirect:/admin/home";
            }
        }else{
            model.addAttribute("msg","账号或密码错误。").addAttribute("sid",sid);
            return "/common/sign-on";
        }
    }

    @RequestMapping("/error/{code}")
    public String error(@PathVariable("code")int code,Model model){
        model.addAttribute("code",code).addAttribute("msg","");
        return "/common/error";
    }
}
