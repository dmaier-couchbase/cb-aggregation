package com.couchbase.demo.aggregation.impl;

import com.couchbase.demo.aggregation.IMapFunc;
import com.couchbase.demo.aggregation.IRecord;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class IdentifyByUidMapFunc implements IMapFunc {

    @Override
    public IRecord map(IRecord record) {
        
       record.setId(record.get("uid").toString());
       return record;
        
    }
}
