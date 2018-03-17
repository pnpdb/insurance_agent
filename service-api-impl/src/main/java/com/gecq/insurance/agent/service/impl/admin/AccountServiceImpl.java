package com.gecq.insurance.agent.service.impl.admin;

import com.gecq.insurance.agent.service.admin.AccountService;
import com.gecq.insurance.agent.service.admin.PermissionService;
import com.gecq.insurance.agent.service.impl.DefaultServiceImpl;
import com.gecq.insurance.agent.service.impl.persistence.admin.AccountDao;
import com.gecq.insurance.agent.service.impl.persistence.admin.UserDao;
import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.utils.CacheUtils;
import com.gecq.insurance.agent.service.utils.CommonUtils;
import com.gecq.insurance.agent.service.utils.EncryptionUtils;
import com.gecq.insurance.agent.service.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by gecha on 2016/12/30.
 */
@Component("accountService")
public class AccountServiceImpl extends DefaultServiceImpl implements AccountService {
    @Autowired
    protected EncryptionUtils encryptionUtils;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao) {
        super(accountDao);
    }

    public ModelBean authorize(String account, String pwd) {
        if(StringUtils.isBlank(account)|| StringUtils.isBlank(pwd)){
            return null;
        }
        AccountDao dao = (AccountDao)getDao();
        ModelBean modelBean = dao.findByAccount(account);
        if(modelBean!=null){
            if(encryptionUtils.validEncryptedChar(pwd,modelBean.getString("pwd"), modelBean.getString("pwdSalt"))){
                setAccountPermissions(modelBean);
                cacheUtils.set(CacheUtils.Cache.admin,modelBean.getId(),modelBean);
                return modelBean;
            }
        }
        return null;
    }

    public int updatePwd(String id, String oPwd, String nPwd) {
        AccountDao accountDao = (AccountDao)getDao();
        ModelBean admin= accountDao.findOne(new ModelBean("id",id).set("cols","id,pwd,pwdSalt"));
        boolean valid= encryptionUtils.validEncryptedChar(oPwd,admin.getString("pwd"),admin.getString("pwdSalt"));
        if(!valid){
            return -1;
        }
        byte[] bytes=encryptionUtils.nextSalt();
        String np = encryptionUtils.getEncryptedChar(nPwd,bytes);
        String salts = encryptionUtils.byteToHexString(bytes);
        admin.set("pwd",np).set("pwdSalt",salts);
        return  accountDao.updatePwd(admin);
    }

    public ModelBean getAccountDetailByUserId(String userId) {
        ModelBean accountBean = (ModelBean) cacheUtils.get(CacheUtils.Cache.admin,userId);
        if(accountBean==null){
            AccountDao accountDao = (AccountDao)getDao();
            accountBean = accountDao.findAccountDetailByUserId(userId);
            if(accountBean!=null){
                setAccountPermissions(accountBean);
                cacheUtils.set(CacheUtils.Cache.admin,userId,accountBean);
            }
        }
        return accountBean;
    }

    public int saveOrUpdateData(ModelBean modelBean){
        if(modelBean.isBlank("id")){
            return newUserAccount(modelBean);
        }else{
            return updateUserAccount(modelBean);
        }
    }

    private int newUserAccount(ModelBean modelBean){
        modelBean.setId(genUUID());
        ModelBean account = new ModelBean();
        byte[] bytes=encryptionUtils.nextSalt();
        String np = encryptionUtils.getEncryptedChar(modelBean.getString("pwd"),bytes);
        String salts = encryptionUtils.byteToHexString(bytes);
        account.set("state",modelBean.getString("state"))
                .set("account",modelBean.getString("account"))
                .set("userId",modelBean.getId())
                .set("pwd",np)
                .set("pwdSalt",salts);
        userDao.insert(modelBean);
        return getDao().insert(modelBean);
    }

    private int updateUserAccount(ModelBean modelBean){
        ModelBean account = new ModelBean();
        account.set("id",modelBean.getId()).set("account",modelBean.getString("account"));
        userDao.update(modelBean);
        return getDao().update(modelBean);
    }

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private JsonUtils jsonUtils;
    private void setAccountPermissions(ModelBean modelBean){
        List<ModelBean> permissions = permissionService.getUserPermissions(modelBean.getId());
        modelBean.set("permissions",permissions);
        modelBean.set("treeMenu",jsonUtils.toJson(commonUtils.treeMenu(permissions)));
    }
}
