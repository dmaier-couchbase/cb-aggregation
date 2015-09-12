package com.couchbase.demo.aggregation.util;

/**
 * This is a tuple
 * 
 * @author David Maier <david.maier at couchbase.com>
 */
public class Tuple<K,V> {
    
    private final K k;
    private final V v;

    public Tuple(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public V getV() {
        return v;
    }
    
}
