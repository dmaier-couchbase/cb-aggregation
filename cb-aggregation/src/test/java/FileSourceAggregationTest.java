import com.couchbase.client.java.Bucket;
import com.couchbase.demo.aggregation.ISchema;
import com.couchbase.demo.aggregation.conn.BucketFactory;
import com.couchbase.demo.aggregation.impl.CBAggregate;
import com.couchbase.demo.aggregation.impl.CBCountReduceFunc;
import com.couchbase.demo.aggregation.impl.FileSource;
import com.couchbase.demo.aggregation.impl.IdentifyByUidMapFunc;
import com.couchbase.demo.aggregation.impl.Schema;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class FileSourceAggregationTest {
    
    private static Logger LOG = Logger.getLogger(FileSourceAggregationTest.class.getName());
    
    private static Bucket bucket;
    
    
    @BeforeClass
    public static void setUpClass() {
   
        //Connect
        bucket = BucketFactory.getBucket();
    
    }

  
    @Test
    public void testAggregate() throws Exception {
    
      
        ISchema schema = new Schema();
        schema.add("uid", Schema.TYPE_STRING);
        schema.add("token", Schema.TYPE_STRING);
        schema.add("elapsed", Schema.TYPE_NUM);
        
        FileSource fs = new FileSource("test.csv", schema);
                
        fs.retrieve().map(r -> new IdentifyByUidMapFunc().map(r))
                     .flatMap(r -> new CBCountReduceFunc().reduce(r))
                     .toBlocking()
                     .last();
 
    }
    
    
}
