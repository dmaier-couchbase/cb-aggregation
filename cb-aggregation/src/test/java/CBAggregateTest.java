/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.couchbase.client.java.Bucket;
import com.couchbase.demo.aggregation.conn.BucketFactory;
import com.couchbase.demo.aggregation.impl.CBAggregate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class CBAggregateTest {
    
    private static Logger LOG = Logger.getLogger(CBAggregateTest.class.getName());
    
    private static Bucket bucket;
    
    
    @BeforeClass
    public static void setUpClass() {
   
        //Connect
        bucket = BucketFactory.getBucket();
    
    }

  
    @Test
    public void testGetAggregate() {
    
        CBAggregate aggr  = new CBAggregate("count", "dmaier");
        aggr.setResult(5);
        aggr.persist().toBlocking().single();
        
        LOG.log(Level.INFO, "value = {0}", aggr.get().toBlocking().single().getResult());
    
    }
    
    @Test
    public void testGetNonExistentAggregate()
    {
        CBAggregate aggr  = new CBAggregate("count", "dostrovsky");
        
        LOG.log(Level.INFO, "value = {0}", aggr.get().toBlocking().single().getResult());

    }
}
