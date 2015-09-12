package com.couchbase.demo.aggregation;

/**
 * A record is just a flexible data structure
 * 
 * @author David Maier <david.maier at couchbase.com>
 */
public interface IRecord {

    /**
     * Get the id of a record
     * @return
     */
    String getId();
    
    /**
     * Set the id of a record
     * @param id
     */
    void setId(String id);

    /**
     * To get the schema of the record
     * @return
     */
    ISchema getSchema();

    
    /**
     * Puts the property with the value to the record if the value has a supported type
     *
     * Otherwise null will be returned.
     *
     * @param prop
     * @param value
     * @return
     */
    Object put(String prop, Object value);
    
    /**
     * Gets a property value
     * @param prop
     * @return 
     */
    public Object get(String prop);

  
    
}
