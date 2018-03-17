package com.gecq.insurance.agent.service.impl.persistence.admin;

import com.gecq.insurance.agent.service.impl.persistence.DefaultDao;
import com.gecq.insurance.agent.service.models.ModelBean;

/**
 * Created by gecha on 2016/12/30.
 */
public interface AccountDao extends DefaultDao {
    ModelBean findByAccount(String account);
    int updatePwd(ModelBean conditions);
    ModelBean findAccountDetailByUserId(String userId);
}
