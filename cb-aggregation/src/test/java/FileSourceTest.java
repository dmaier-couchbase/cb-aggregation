/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.couchbase.demo.aggregation.IRecord;
import com.couchbase.demo.aggregation.ISchema;
import com.couchbase.demo.aggregation.ISource;
import com.couchbase.demo.aggregation.impl.FileSource;
import com.couchbase.demo.aggregation.impl.Schema;
import java.io.File;
import java.io.FileReader;
import org.junit.Test;
import static org.junit.Assert.*;
import rx.Observable;
import rx.observables.StringObservable;


/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class FileSourceTest {
    
    
    @Test
    public void testStringObservable() throws Exception {
    
        File f = new File(TestConstants.FILE);
        
        StringObservable.from(new FileReader(f))
                .flatMap(s -> Observable.from(s.split("\n")))
                .doOnNext(s -> System.out.println(s))
                .toBlocking().last();
       
    
    }
 
    
    @Test
    public void testRetrieveFile() throws Exception {
        
        
        ISchema schema = new Schema();
        schema.add("uid", Schema.TYPE_STRING);
        schema.add("token", Schema.TYPE_STRING);
        schema.add("elapsed", Schema.TYPE_NUM);
        
        ISource source = new FileSource(TestConstants.FILE, schema);
        
        IRecord record = source.retrieve().toBlocking().last();
        
        System.out.println("uid = " + record.get("uid"));
        
        assertEquals("dmaier", record.get("uid").toString());
    }
}
