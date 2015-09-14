
package com.couchbase.demo.aggregation.impl;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.demo.aggregation.IAggregate;
import com.couchbase.demo.aggregation.conn.BucketFactory;
import java.util.logging.Logger;
import rx.Observable;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class CBAggregate implements IAggregate {

    private static Logger LOG = Logger.getLogger(CBAggregate.class.getName());
    
    private final String aggrId;
    private final String recordId;
    private double result = 0;

    public CBAggregate(String aggrId, String recordId) {
        this.aggrId = aggrId;
        this.recordId = recordId;
    }
    
    
    @Override
    public String getAggrId() {
        
        return this.aggrId;
    }

    @Override
    public String getRecordId() {
        
        return this.recordId;
    }

    @Override
    public double getResult() {
        
        return this.result;
    }

    @Override
    public void setResult(double currResult) {
        
        this.result = currResult;
    }
    
    /**
     * Persist the aggregate to Couchbase and return an aggregate
     * 
     * @return 
     */
    public Observable<CBAggregate> persist() {
        
        return BucketFactory.getAsyncBucket()
                .upsert(toJson())
                .map(d -> {return this;});
    }
    
    
    /**
     * Get the aggregate from Couchbase, if it is not yet existent then
     * create it.
     * 
     * @return 
     */
    public Observable<CBAggregate> get() {
         
        String key = this.aggrId + "::" + this.recordId;

        //Create the document if not existent
        return BucketFactory.getAsyncBucket().get(key)
                .defaultIfEmpty(null)
                .map(d -> d.content().getDouble("value"))
                .onErrorResumeNext(e -> this.persist().map(a -> a.getResult()))
                .map(val -> {
                    this.setResult(val);
                    return this;
                });  
    }
    
    /**
     * Create the JSON for this aggregate
     * 
     * @return 
     */
    private JsonDocument toJson()
    {
        String key = this.aggrId + "::" + this.recordId;
       
        JsonObject obj = JsonObject.empty();
        obj.put("value", this.result);
        
        return JsonDocument.create(key, obj);
    }
  
    
    
    
}
