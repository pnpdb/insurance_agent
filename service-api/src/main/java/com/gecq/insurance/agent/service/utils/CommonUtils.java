package com.gecq.insurance.agent.service.utils;

import com.gecq.insurance.agent.service.models.ModelBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gecha on 2017/1/3.
 */
@Component("commonUtils")
public class CommonUtils {
    public List<ModelBean> treeMenu(List<ModelBean> all){
        List<ModelBean> parents = new ArrayList<>();
        List<ModelBean> children = buildTreeParents(all,parents);
        for(ModelBean modelBean:parents){
            buildTreeLeaf(modelBean,children);
        }
        return parents;
    }

    private void buildTreeLeaf(ModelBean parent,List<ModelBean> children){
        List<ModelBean> useAble=new ArrayList<>();
        List<ModelBean> _children = new ArrayList<>();
        for (ModelBean modelBean:children){
            if(modelBean.isEquals("parentId",parent.getId())){
                if(!modelBean.isEquals("type","modal")){
                    _children.add(modelBean);
                }
            }else {
                useAble.add(modelBean);
            }
        }
        parent.set("children",_children);
        for(ModelBean modelBean:_children){
            buildTreeLeaf(modelBean,useAble);
        }
    }

    private List<ModelBean> buildTreeParents(List<ModelBean> all,List<ModelBean> parents){
        List<ModelBean> useAble=new ArrayList<>();
        for(ModelBean modelBean:all){
            if(modelBean.isBlank("parentId")){
                parents.add(modelBean);
            }else{
                useAble.add(modelBean);
            }
        }
        return useAble;
    }
}
