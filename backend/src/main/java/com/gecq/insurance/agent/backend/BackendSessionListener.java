package com.gecq.insurance.agent.backend;

import com.gecq.insurance.agent.backend.utils.SessionConstance;
import com.gecq.insurance.agent.service.utils.CacheUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

/**
 * Created by gecq on 2016/3/31 0031.
 */
public class BackendSessionListener implements HttpSessionListener {
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    private CacheUtils cacheUtils;

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        Object ul = session.getAttribute(SessionConstance.accountId.name());
        if(cacheUtils==null){
            cacheUtils =  CacheUtils.instance();
        }
        Object id = cacheUtils.get(CacheUtils.Cache.login, CacheUtils.Cache.login.key());
        List<String> ids = (List<String>)id;
        if(ul!=null){
            if(ids!=null)
            {
                ids.remove(ul);
            }
            cacheUtils.set(CacheUtils.Cache.login,CacheUtils.Cache.login.key(),ids);
        }
    }
}
