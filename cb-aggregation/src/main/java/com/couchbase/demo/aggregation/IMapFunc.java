package com.couchbase.demo.aggregation;

/**
 * Maps a record to another one
 *  
 * 
 * @author David Maier <david.maier at couchbase.com>
 */
public interface IMapFunc {
    
    public IRecord map(IRecord record);
    
}
