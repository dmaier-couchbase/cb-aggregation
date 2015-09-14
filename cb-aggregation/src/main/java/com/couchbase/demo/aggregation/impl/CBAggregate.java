
package com.couchbase.demo.aggregation.impl;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.demo.aggregation.IAggregate;
import com.couchbase.demo.aggregation.conn.BucketFactory;
import java.util.logging.Level;
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
    
    //Compare and Swap for concurrency handling
    private long cas = 0;
    
    public CBAggregate(String aggrId, String recordId) {
        this.aggrId = aggrId;
        this.recordId = recordId;
    }
    
    public CBAggregate(String aggrId, String recordId, long cas) {
        this.aggrId = aggrId;
        this.recordId = recordId;
        this.cas = cas;
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

    public long getCas() {
        return cas;
    }
    
    /**
     * Persist the aggregate to Couchbase by creating it
     * and then return aggregate
     * 
     * @return 
     */
    public Observable<IAggregate> create() {
        
        return BucketFactory.getAsyncBucket()
                .insert(toJson())
                .map(d -> {
                
                    this.cas = d.cas();                   
                    return (IAggregate) this;
                })
                .onErrorResumeNext( e -> {
                    
                    //If already existent then return the existent
                    //one instead
                    if (e instanceof DocumentAlreadyExistsException) {
                        
                        return this.get();
                    }
                        
                    return Observable.error(e);
                });
    }
    
    
    /**
     * Persist the aggregate to Couchbase by replacing it
     * and return an aggregate
     * 
     * @return 
     */
    public Observable<IAggregate> replace() {
        
        return BucketFactory.getAsyncBucket()
                .replace(toJson())
                .map(d -> {
                
                    this.cas = d.cas();                   
                    return this;
                });
    }
    
 
    /**
     * Get the aggregate from Couchbase, if it is not yet existent then
     * create it.
     * 
     * @return 
     */
    public Observable<IAggregate> get() {
         
        String key = this.aggrId + "::" + this.recordId;

        //Create the document if not existent
        return BucketFactory.getAsyncBucket().get(key)
                .defaultIfEmpty(null)
                .map(d -> {
                    
                    //Expose the CAS value for concurrency handling
                    this.cas = d.cas();
                    
                    LOG.log(Level.INFO, "cas = {0}", this.cas);
                    
                    return d.content().getDouble("value");
                })
                .onErrorResumeNext(e -> {
                    
                    //If an empty stream was returned then the document was not 
                    //existent and so null was emitted
                    if (e instanceof NullPointerException)
                    {
                        return this.create().map(a -> a.getResult());
                    }
                    
                    return Observable.error(e);
                })
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
                
        return JsonDocument.create(key, obj, this.cas);
    }
  
     
}
