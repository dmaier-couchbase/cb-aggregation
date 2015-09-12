package com.couchbase.demo.aggregation.impl;

import com.couchbase.demo.aggregation.IRecord;
import com.couchbase.demo.aggregation.ISchema;
import java.util.HashMap;


public class Record extends HashMap<String, Object> implements IRecord {
    
   
    private String id; 
    private ISchema schema;
    
    
    /**
     * The default constructor of a record
     * @param schema
     */
    public Record(ISchema schema) {
        super();
        this.schema = schema;
    }

    /**
     * The constructor which takes the id as an argument
     * @param schema
     * @param id 
     */
    public Record(ISchema schema, String id) {
        
        this(schema);
        this.id = id;
    }

  
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public ISchema getSchema() {
        return schema;
    }

    @Override
    public Object put(String key, Object value) {
        
        Object tmpVal = null;
 
        //Convert to the supported type based on the schema
        if (schema.getType(key).equals(Schema.TYPE_STRING))
            tmpVal = value.toString();
        
        if (schema.getType(key).equals(Schema.TYPE_NUM))
            tmpVal = Double.parseDouble(value.toString());
       
        if (tmpVal != null) return super.put(key, tmpVal);
    
        return null;
    }

    @Override
    public Object get(String prop) {
      
        return super.get(prop);
        
    }
    
    
    
}
