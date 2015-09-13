package com.couchbase.demo.aggregation;

import rx.Observable;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public interface IReduceFunc {
    
    /**
     * Perfroms an aggregation based on the old value and the current record
     * 
     * @param old
     * @param record
     * @return 
     */
     public IAggregate reduce(IAggregate old, IRecord record);
    
    
    /**
     * Returns the aggregation result as a stream by performing the aggregation based
     * on the aggregation identifier and the current record
     * 
     * @param aggrId
     * @param record
     * @return 
     */
    public Observable<IAggregate> reduce(String aggrId, IRecord record);
    
}
