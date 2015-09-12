package com.couchbase.demo.aggregation;

import rx.Observable;

/**
 * A source retrieves/provides a stream of records
 * 
 * @author David Maier <david.maier at couchbase.com>
 */
public interface ISource {
   
    public Observable<IRecord> retrieve() throws Exception;
    
}
