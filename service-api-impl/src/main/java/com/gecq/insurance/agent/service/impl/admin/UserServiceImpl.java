package com.gecq.insurance.agent.service.impl.admin;

import com.gecq.insurance.agent.service.impl.DefaultServiceImpl;
import com.gecq.insurance.agent.service.impl.persistence.admin.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gecha on 2016/12/30.
 */
@Component("userService")
public class UserServiceImpl extends DefaultServiceImpl {
    @Autowired
    public UserServiceImpl(UserDao userDao) {
        super(userDao);
    }
}
