package com.couchbase.demo.aggregation;

import rx.Observable;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public interface IReduceFunc {
    
    public IAggregate reduce(IAggregate old, IRecord record);
    
    public Observable<IAggregate> reduce(String aggrId, IRecord record);
    
}
