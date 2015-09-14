package com.couchbase.demo.aggregation.impl;

import com.couchbase.demo.aggregation.IRecord;
import com.couchbase.demo.aggregation.ISchema;
import com.couchbase.demo.aggregation.ISource;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;
import rx.observables.StringObservable;

/**
 *
 * @author David Maier <david.maier at couchbase.com>
 */
public class FileSource implements ISource {

    private static Logger LOG = Logger.getLogger(FileSource.class.getName());
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
        
        return StringObservable.from(new FileReader(file))
                .flatMap(s -> Observable.from(s.split(NL)))
                .doOnNext(s -> LOG.log(Level.INFO, "Retrieving line: {0}", s))
                .map(l -> {    
                    
                    //Line to record
                    String[] arr = l.split(DELIM);
                    
                    IRecord r = new Record(schema);
                    
                    for (int i = 0; i < arr.length; i++) {
                          
                          r.put(schema.getProp(i), arr[i]);
                    }
                    
                    return r;
                });
    }  

    @Override
    public ISchema getSchema() {
        
        return this.schema;
    }
    
}
