package com.gecq.insurance.agent.backend;

import com.gecq.insurance.agent.backend.utils.SessionConstance;
import com.gecq.insurance.agent.service.admin.AccountService;
import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.utils.CacheUtils;
import com.gecq.insurance.agent.service.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by gecq on 2016/8/22.
 */
public class AuthorizedInterceptor  extends HandlerInterceptorAdapter {
    @Autowired
    private CookieUtils cookieUtils;

    @Autowired
    private CacheUtils cacheUtils;

    private AccountService accountService;

    public AuthorizedInterceptor(AccountService accountService) {
        this.accountService=accountService;
    }

    public final boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        httpServletResponse.setCharacterEncoding("UTF-8");
        String method = httpServletRequest.getMethod();
        String path = httpServletRequest.getRequestURI();
        if(!"post".equalsIgnoreCase(method)&&!"/admin/home".equals(path)){
            httpServletResponse.getWriter().write("<div style='padding:10px;font-size:12px;color:#cccccc;text-align:center'>不支持的请求类型！</div>");
            return false;
        }

        if (!isLogin(session)) {
            String type = httpServletRequest.getHeader("X-Requested-With");
            if(type == null){
                type = httpServletRequest.getHeader("x-requested-with");
            }
            if ( "XMLHttpRequest".equalsIgnoreCase(type)) {
                String contentType = httpServletRequest.getContentType();
                if(StringUtils.isNotBlank(contentType)){
                    if(contentType.toLowerCase().contains("json")){
                        onJsonTimeout(httpServletResponse);
                        return false;
                    }
                }
                onTimeout(httpServletResponse);
                return false;
            }
            //看看cookie里面是否有保存登录信息
            Cookie cookieName = cookieUtils.getCookieByName(httpServletRequest, SessionConstance.accountName.name());
            Cookie cookiePwd = cookieUtils.getCookieByName(httpServletRequest, SessionConstance.accountPwd.name());
            if (cookieName != null && cookiePwd != null
                    && StringUtils.isNotBlank(cookieName.getValue()) && StringUtils.isNotBlank(cookiePwd.getValue())) {
                //cookie里面找到用户名和密码
                ModelBean admin = accountService.authorize(cookieName.getValue(), cookiePwd.getValue());
                if (admin != null) {
                    session.setAttribute(SessionConstance.accountId.name(), admin.getString("roleId"));
                    session.setAttribute(SessionConstance.accountName.name(), cookieName.getValue());
                    return permissionCheck(httpServletResponse,session,path);//权限控制
                } else {
                    //清掉cookie里面的错误信息
                    cookieUtils.clearCookie(httpServletResponse,SessionConstance.accountName.name());
                    cookieUtils.clearCookie(httpServletResponse,SessionConstance.accountPwd.name());
                }
            }
            if("get".equalsIgnoreCase(httpServletRequest.getMethod())){
                session.setAttribute(SessionConstance.returnBack.name(), path);
            }
            session.setAttribute("msg","请先登录");
            onNotLogin(httpServletRequest,httpServletResponse);
            return false;
        }
        return permissionCheck(httpServletResponse,session,path);//权限控制
    }

    private  boolean isLogin(HttpSession session) {
        return StringUtils.isNotBlank(userId(session));
    }

    private  String userId(HttpSession session) {
        Object ul = session.getAttribute(SessionConstance.accountId.name());
        if(ul==null){
            return null;
        }
        return String.valueOf(ul);
    }

    private void onTimeout(HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.getWriter().write("<div style='padding:10px;font-size:12px'>登录超时，请刷新后重新登录操作</div>");
    }
    private ObjectNode buildJsonNode(){
        ObjectMapper objectMapper = new ObjectMapper();
        return  objectMapper.createObjectNode();
    }

    private void onJsonTimeout(HttpServletResponse httpServletResponse) throws Exception {
        ObjectNode jsonNode = buildJsonNode();
        jsonNode.put("code", -1);
        jsonNode.put("msg", "登录超时，请刷新后重新登录操作");
        httpServletResponse.getWriter().write(jsonNode.toString());
    }

    private void onNotLogin(HttpServletRequest request,HttpServletResponse httpServletResponse) throws Exception {
        String iframe = request.getParameter("iframe");
        if(StringUtils.isNotBlank(iframe)){
            onTimeout(httpServletResponse);
            return;
        }
        httpServletResponse.sendRedirect("/");
    }
    private void onRefuse(HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("UTF-8");
        response.setStatus(403);
        response.getWriter().write("抱歉，你的访问被拒绝！");
    }
    private boolean ignorePermissionCheck(String path) {
        return "/admin/dashboard,/admin/home,/admin/login/out,/admin/data/profile/edit,/admin/profile/save.json,/admin/pwd/update.json,/admin/data/update/pwd".contains(path);
    }

    private boolean permissionCheck(HttpServletResponse response,HttpSession session,String path) throws Exception{
        if(ignorePermissionCheck(path)){
            return true;
        }
        boolean permissionCheck = permissionCheck(session,path);
        if(!permissionCheck){
            onRefuse(response);
        }
        return permissionCheck;
    }

    @SuppressWarnings("unchecked")
    private boolean permissionCheck(HttpSession session,String path){
        System.out.println("path ==== "+path);
        ModelBean adminBean = (ModelBean) cacheUtils.get(CacheUtils.Cache.admin, userId(session));
        if(adminBean==null){
            return  false;
        }
        String level = adminBean.getString("level");
        List<ModelBean> permissions = (List<ModelBean>) adminBean.get("permissions");
        return permissionCheck(permissions,path,level);
    }

    @SuppressWarnings("unchecked")
    private boolean permissionCheck(List<ModelBean> permissions, String path, String level){
        if(permissions!=null){
            for(ModelBean p:permissions){
                String code = p.getString("urlPattern");
                code = code.replace("\r","");
                String[] codes = code.split("\n");
                String pl = p.getString("level");
                for(String c : codes){
                    if(path.matches(c)){
                        return level.contains(pl);
                    }
                }
            }
        }
        return false;
    }
}
