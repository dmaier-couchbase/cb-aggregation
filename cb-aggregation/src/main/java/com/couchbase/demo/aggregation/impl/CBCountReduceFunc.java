package com.couchbase.demo.aggregation.impl;

import com.couchbase.demo.aggregation.IAggregate;
import com.couchbase.demo.aggregation.IRecord;
import com.couchbase.demo.aggregation.IReduceFunc;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class CBCountReduceFunc implements IReduceFunc {
    
    private static final Logger LOG = Logger.getLogger(CBCountReduceFunc.class.getName()); 
    public static String AGGR_ID = "count";
    
    @Override
    public IAggregate reduce(IAggregate old, IRecord record)  {
    
        LOG.log(Level.INFO, "old = {0}", old.getResult());
        
        IAggregate agg = new CBAggregate(old.getAggrId(), record.getId());
        agg.setResult(old.getResult() + 1);
        
        LOG.log(Level.INFO, "new = {0}", agg.getResult());
        
        return agg;
    }

    @Override
    public Observable<IAggregate> reduce(String aggrId, IRecord record) {
        
        return new CBAggregate(aggrId, record.getId()).get()                                
                .map(old -> { 
                    
                    IAggregate a = new CBAggregate(aggrId, record.getId()); 
                    IAggregate reduced = reduce(old, record);
                    return reduced;
                })
                .flatMap( a -> ((CBAggregate) a).persist() );
    }
    
    /**
     * Using the constant aggregation identifier
     * 
     * @param record
     * @return 
     */
    public Observable<IAggregate> reduce(IRecord record) {
        
        return reduce(AGGR_ID, record);
    }
    
}
