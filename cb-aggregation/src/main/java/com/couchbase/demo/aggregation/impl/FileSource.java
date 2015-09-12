package com.couchbase.demo.aggregation.impl;

import com.couchbase.demo.aggregation.IRecord;
import com.couchbase.demo.aggregation.ISchema;
import com.couchbase.demo.aggregation.ISource;
import java.io.File;
import java.io.FileReader;
import rx.Observable;
import rx.observables.StringObservable;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class FileSource implements ISource {

    public static final String NL = "\n";
    public static final String DELIM = ";";
    
    /**
     * The associated file of this file source
     */
    private final File file;
    
    private final ISchema schema;
    
    
    /**
     * The constructor which takes a file as the argument
     * 
     * @param path
     */
    public FileSource(String path, ISchema schema) {
        
        this.file = new File(path);
        this.schema = schema;
    }

    @Override
    public Observable<IRecord> retrieve() throws Exception {
        
        Observable<String> lineStream = StringObservable.split(StringObservable.from(new FileReader(this.file)), NL);
        
        return lineStream.map(l -> l.split(DELIM))
                  .map(a -> { 
                  
                      IRecord r = new Record(schema);
                      
                      for (int i = 0; i < a.length; i++) {
                          
                          r.put(schema.getProp(i), a[i]);
                      }
                      
                      return r;}
                  );
        
        
    }  
    
}
