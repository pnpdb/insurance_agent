package com.gecq.insurance.agent.service.admin;

import com.gecq.insurance.agent.service.DefaultService;
import com.gecq.insurance.agent.service.models.ModelBean;

/**
 * Created by gecha on 2016/12/30.
 */
public interface AccountService extends DefaultService {
    ModelBean authorize(String account,String pwd);
    int updatePwd(String id,String oPwd,String nPwd);
    ModelBean getAccountDetailByUserId(String userId);
}
