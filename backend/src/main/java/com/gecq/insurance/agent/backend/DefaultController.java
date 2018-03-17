package com.gecq.insurance.agent.backend;

import com.alibaba.dubbo.remoting.TimeoutException;
import com.gecq.insurance.agent.backend.utils.SessionConstance;
import com.gecq.insurance.agent.service.admin.AccountService;
import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.models.PageBean;
import com.gecq.insurance.agent.service.utils.CacheUtils;
import com.gecq.insurance.agent.service.utils.CookieUtils;
import com.gecq.insurance.agent.service.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by gecha on 2016/12/30.
 */

public class DefaultController {
    @Value("#{webConfig['page.size.backend']}")
    protected int pageSize;
    @Autowired
    protected CacheUtils cacheUtils;

    protected String randomId(){
        return UUID.randomUUID().toString();
    }

    @Autowired
    protected AccountService accountService;
    @ExceptionHandler(Exception.class)
    public void handleJsonException(Exception ex,HttpServletResponse response) {
        if (ex instanceof MaxUploadSizeExceededException){
            writeError(response,"上传文件过大，不能超过5MB！");
        } else if(ex instanceof SQLException){
            writeError(response,"数据库操作异常！");
        } else if(ex instanceof ParseException){
            writeError(response,"解析字符异常："+ex.getLocalizedMessage());
        } else if(ex instanceof TimeoutException){
            writeError(response,"远程接口连接超时，请重试！");
        }
        else{
            writeError(response,"通信异常");
        }
        ex.printStackTrace();
    }

    protected boolean isLogin(HttpSession session){
        return StringUtils.isNotBlank(getCurrentUserId(session));
    }

    protected String getCurrentUserId(HttpSession session){
        Object ul = session.getAttribute(SessionConstance.accountId.name());
        if(ul!=null){
            return String.valueOf(ul);
        }
        return null;
    }

    protected ModelBean getCurrentAdmin(HttpSession session){
        return accountService.getAccountDetailByUserId(getCurrentUserId(session));
    }

    @Autowired
    protected CookieUtils cookieUtils;

    protected void cookieSet(Integer remember,HttpServletResponse response,String account,String pwd){
        if(remember!=null&&remember==1){
            cookieUtils.addCookie(response,SessionConstance.accountName.name(),account,0);
            cookieUtils.addCookie(response,SessionConstance.accountPwd.name(),pwd,0);
        }else{
            cookieUtils.clearCookie(response,SessionConstance.accountName.name());
            cookieUtils.clearCookie(response,SessionConstance.accountPwd.name());
        }
    }

    private static final String UTF8 = "utf-8";

    /**
     * MD5数字签名
     *
     * @param src
     * @return
     */
    protected String md5Digest(String src) {
        // 定义数字签名方法, 可用：MD5, SHA-1
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(src.getBytes(UTF8));
            return this.byte2HexStr(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return src;
    }

    /**
     * 字节数组转化为大写16进制字符串
     *
     * @param b
     * @return
     */
    private String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte by:b) {
            String s = Integer.toHexString(by & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }

    protected boolean isNotThisSession(String sid,HttpSession session){
        return !md5Digest(session.getId()).equals(sid);
    }

    private void writeResponse(String msg,int status,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        try {
            response.getWriter().write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    protected Model checkPermissions(Model model, HttpSession session, String permission) {
        ModelBean admin = getCurrentAdmin(session);
        List<ModelBean> permissions = (List<ModelBean>) admin.get("permissions");
        if (permissions != null) {
            for (ModelBean p : permissions) {
                String sc = p.getString("code");
                if (sc != null && permission.contains(sc)) {
                    model.addAttribute(sc, p);
                }
            }
        }
        return model;
    }

    protected void writeSuccess(HttpServletResponse response,String msg){
        writeResponse(msg,200,response);
    }

    protected void writeSuccess(HttpServletResponse response){
        writeResponse("SUCCESS",200,response);
    }

    protected void writeSuccessJson(HttpServletResponse response,Object obj){
        response.setStatus(200);
        renderJson(response,obj);
    }

    protected void writeError(HttpServletResponse response,String msg){
        writeResponse(msg,500,response);
    }
    @Autowired
    private JsonUtils jsonUtils;
    private void renderJson(HttpServletResponse response,Object obj){
        jsonUtils.toJson(response, obj);
    }

}
