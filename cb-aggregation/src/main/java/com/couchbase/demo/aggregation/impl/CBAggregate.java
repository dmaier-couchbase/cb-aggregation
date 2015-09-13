
package com.couchbase.demo.aggregation.impl;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.demo.aggregation.IAggregate;
import com.couchbase.demo.aggregation.conn.BucketFactory;
import rx.Observable;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class CBAggregate implements IAggregate {

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
     * Persist the aggregate to Couchbase
     * 
     * @return 
     */
    public Observable<CBAggregate> persist() {
        
        String key = this.aggrId + "::" + this.recordId;
        
        JsonDocument doc = JsonDocument.create(key, toJson());
        
        return BucketFactory.getAsyncBucket()
                .upsert(doc)
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
         
         //TODO: Check for error type
         return BucketFactory.getAsyncBucket().get(key)
                 .map(d -> d.content().getDouble("value"))
                 .map(val -> { this.setResult(val); return this;})
                 .onErrorResumeNext(e -> this.persist());
        
        
    }
    
    /**
     * Create the JSON for this aggregate
     * 
     * @return 
     */
    private JsonObject toJson()
    {
        JsonObject obj = JsonObject.empty();
        obj.put("value", this.result);
        
        return obj;
    }
  
    
    
    
}
