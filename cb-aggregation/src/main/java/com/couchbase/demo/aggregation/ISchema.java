package com.couchbase.demo.aggregation;

/**
 * Defines how a Record is looking like.
 * 
 * The order of the properties in a schema is relevant.
 * 
 * @author David Maier <david.maier at couchbase.com>
 */
public interface ISchema {

    /**
     * Add the information which property has which type
     *
     * @param prop
     * @param type
     */
    void add(String prop, String type);

    /**
     * Returns the type of a specific property.
     *
     * If the property is not existent then null will be returned.
     *
     * @param prop
     * @return
     */
    String getType(String prop);
    
    /**
     * Returns the property name at a specific position
     * 
     * @param idx
     * @return 
     */
    String getProp(int idx);
    
}
