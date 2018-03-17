package com.gecq.insurance.agent.service.models;

/**
 * Created by gecha on 2016/8/5.
 */
public class FormBean {
    private ModelBean model;

    public ModelBean getModel() {
        if(model==null){
            model=new ModelBean();
        }
        return model;
    }

    public void setModel(ModelBean model) {
        this.model = model;
    }
}
