/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.couchbase.demo.aggregation.main;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.demo.aggregation.ISchema;
import com.couchbase.demo.aggregation.conn.BucketFactory;
import com.couchbase.demo.aggregation.impl.CountByUserReduceFunc;
import com.couchbase.demo.aggregation.impl.FileSource;
import com.couchbase.demo.aggregation.impl.IdentifyByUidMapFunc;
import com.couchbase.demo.aggregation.impl.Schema;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class Main {

    /**
     * Entry point of the example
     * 
     * @param args 
     * @throws java.lang.Exception 
     */
    public static void main(String[] args) throws Exception {
       
        AsyncBucket bucket = BucketFactory.getAsyncBucket();
        
        
        ISchema schema = new Schema();
        schema.add("uid", Schema.TYPE_STRING);
        schema.add("token", Schema.TYPE_STRING);
        schema.add("elapsed", Schema.TYPE_NUM);
        
        FileSource fs = new FileSource("test.csv", schema);
                
        fs.retrieve().map(r -> new IdentifyByUidMapFunc().map(r))
                     .flatMap(r -> new CountByUserReduceFunc().reduce("count", r))
                     .toBlocking()
                     .single();
        
    }
 
}
