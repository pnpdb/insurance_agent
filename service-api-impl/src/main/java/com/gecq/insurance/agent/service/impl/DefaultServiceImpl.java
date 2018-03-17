package com.gecq.insurance.agent.service.impl;

import com.gecq.insurance.agent.service.impl.persistence.DefaultDao;
import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.models.PageBean;
import com.gecq.insurance.agent.service.utils.CacheUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gecha on 2016/12/30.
 */
public class DefaultServiceImpl extends ServiceCommon{

    @Autowired
    protected CacheUtils cacheUtils;
    private DefaultDao defaultDao;
    protected DefaultDao getDao(){
        return defaultDao;
    }
    public DefaultServiceImpl(DefaultDao defaultDao){
        this.defaultDao=defaultDao;
    }

    private void setCols(ModelBean conditions){
        if(conditions==null){
            conditions=new ModelBean();
        }
        if(conditions.isBlank("cols")){
            conditions.set("cols","*");
        }
    }
    public List<ModelBean> getDataList(ModelBean conditions) {
        setCols(conditions);
        return this.defaultDao.findList(conditions);
    }

    public PageBean<ModelBean> page(Integer pageNum, int pageSize, ModelBean conditions) {
        if(pageNum==null){
            pageNum=1;
        }
        PageHelper.startPage(pageNum,pageSize,true,true);
        List<ModelBean> dataList = getDataList(conditions);
        return buildPageBean(pageNum,pageSize,(Page<ModelBean>)dataList);
    }

    public ModelBean getDataOne(ModelBean conditions) {
        setCols(conditions);
        return defaultDao.findOne(conditions);
    }

    public int deleteData(ModelBean values) {
        return defaultDao.delete(values);
    }

    public int saveOrUpdateData(ModelBean values) {
        if(values.isBlank("id")){
            values.setId(genUUID());
            return defaultDao.insert(values);
        }else{
            return defaultDao.update(values);
        }
    }
}
