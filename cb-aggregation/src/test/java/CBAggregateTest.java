/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.couchbase.demo.aggregation.conn.BucketFactory;
import com.couchbase.demo.aggregation.impl.CBAggregate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


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
        
        //Clean
        try {
            bucket.remove("count::dmaier");
            bucket.remove("count::dostrovsky");
        } 
        catch (DocumentDoesNotExistException e)
        {
            LOG.warning("Could not remove a nonexistent document");
        }
    
    }

  
    @Test
    public void testGetAggregate() {
    
        CBAggregate aggr  = new CBAggregate("count", "dmaier");
        aggr.setResult(5);
        aggr.create().toBlocking().single();
        
        double result = aggr.get().toBlocking().single().getResult();
        
        LOG.log(Level.INFO, "value = {0}", result);
        
        assertEquals("" + 5.0, "" + result);
    }
    
    @Test
    public void testGetNonExistentAggregate()
    {
        CBAggregate aggr  = new CBAggregate("count", "dostrovsky");
        
        double result = aggr.get().toBlocking().single().getResult();
        
        LOG.log(Level.INFO, "value = {0}", result );
        
        assertEquals("" + 1.0, "" + result);
    }
}
