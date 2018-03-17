package com.gecq.insurance.agent.service.utils;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gecq on 2015/8/11 0011.
 */
@Component("cookieUtils")
public class CookieUtils{

    /**
     * 默认cookie生命周期 7天
     */
    public  final int DEFAULT_COOKIE_AGE=7*24*60*60;
    /**
     * 设置cookie（接口方法）
     *
     * @param response
     * @param name     cookie名字
     * @param value    cookie值
     * @param maxAge   cookie生命周期  以秒为单位
     */
    public  void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 根据名字获取cookie（接口方法）
     *
     * @param request
     * @param name    cookie名字
     * @return
     */
    public  Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name);
        } else {
            return null;
        }
    }

    public  void clearCookie(HttpServletResponse response, String name) {
        addCookie(response, name, null, 0);
    }

    /**
     * 将cookie封装到Map里面（非接口方法）
     *
     * @param request
     * @return
     */
    private  Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
