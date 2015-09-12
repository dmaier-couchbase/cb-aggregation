package com.couchbase.demo.aggregation.impl;

import com.couchbase.demo.aggregation.ISchema;
import com.couchbase.demo.aggregation.util.Tuple;
import java.util.ArrayList;


public class Schema extends ArrayList<Tuple<String, String>> implements ISchema {
    
    //For now just numbers and Strings are supported as record values
    public final static String TYPE_NUM = "NUMBER";
    public final static String TYPE_STRING = "STRING";
    
    
    @Override
    public void add(String prop, String type)
    {
        this.add(new Tuple<>(prop, type ));
    }
    
    @Override
    public String getType(String prop)
    {
        for (Tuple<String, String> t : this) {
        
                if (t.getK().equals(prop)) return t.getV();
        }
        
        return null;
    }

    @Override
    public String getProp(int idx) {
     
        return super.get(idx).getK();
    }
  
    
            
}
