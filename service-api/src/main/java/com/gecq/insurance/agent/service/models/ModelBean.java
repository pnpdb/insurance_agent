package com.gecq.insurance.agent.service.models;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by gecha on 2016/8/5.
 */
public class ModelBean extends TreeMap<String,Object> implements Serializable {
    public ModelBean (){}
    public ModelBean(String key,Object v){
        set(key,v);
    }
    @Override
    public Object put(String key, Object value) {
        if(key!=null){
            String[] strings = key.split("_");
            if(strings.length>1)
            {
                key = strings[0];
                for(int n=1;n<strings.length;n++){
                    String it = strings[n];
                    key+=it.substring(0,1).toUpperCase()+it.substring(1);
                }
            }
        }
        return super.put(key, value);
    }

    public ModelBean remove(String key){
        super.remove(key);
        return this;
    }

    public ModelBean setId(String id){
        return set("id",id);
    }
    public String getId(){
        return (String)get("id");
    }

    public ModelBean set(String k, Object v){
        put(k,v);
        return this;
    }

    public ModelBean setArray(String k,Object...values){
        put(k,values);
        return this;
    }

    public boolean isBlank(String key){
        return StringUtils.isBlank(getString(key));
    }

    public boolean isNum(String key){return StringUtils.isNumeric(getString(key));}

    public boolean isNotBlank(String key){
        return !isBlank(key);
    }

    public java.sql.Timestamp getTimestamp(String key) {
        return (java.sql.Timestamp) get(key);
    }
    public boolean lengthRange(String key,int min,int max){
        String val = getString(key);
        if(val==null){
            return false;
        }
        int length = val.trim().length();
        return length>min&&length<max;
    }

    public boolean isEquals(String key,Object anotherV){
        Object val = get(key);
        return val!=null&&val.equals(anotherV);
    }

    public String getString(String key){
        Object v = get(key);
        if(v==null){
            return null;
        }
        return String.valueOf(v);
    }
    public Integer getInt(String key){
        String val = getString(key);
        if(StringUtils.isBlank(val)){
            return 0;
        }
        return Integer.parseInt(val);
    }
    public boolean getBoolean(String key){
        return Boolean.parseBoolean(getString(key));
    }

    public BigDecimal getMoney(String key){
        String val = getString(key);
        if(isBlank(key)||!StringUtils.isNumeric(val.replace(".",""))){
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(Double.parseDouble(val));
    }

    public ModelBean setState(State state){
        return set("state",state);
    }
    public boolean isState(State state){
        return state.name().equals(getString("state"));
    }
    @Override
    public String toString() {
        Set<String> keys = keySet();
        String content="";
        for(String string : keys ){
            content +="," +string+" = \'"+get(string)+"\'";
        }
        content = isEmpty()?"":content.substring(1);
        return "ModelBean{"+content+"}";
    }

    public ModelBean copyTo(ModelBean target){
        Set<String> keys = keySet();
        for(String key:keys){
            target.set(key,get(key));
        }
        return target;
    }

    public ModelBean clone() {
        return copyTo(new ModelBean());
    }

    @Override
    public boolean equals(Object o) {
        if(getId()!=null){
            if(o instanceof ModelBean){
                ModelBean modelBean = (ModelBean)o;
                return getId().equals(modelBean.getId());
            }
        }
        return super.equals(o);
    }
}
