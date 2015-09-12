/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.couchbase.demo.aggregation;

/**
 * The aggregation result
 * 
 * @author David Maier <david.maier at couchbase.com>
 */
public interface IAggregate {
    
    /**
     * The aggregation identifier
     * 
     * @return 
     */
    public String getAggrId();
    
    /**
     * The record identfier for this aggregation
     * @return 
     */
    public String getRecordId();

    /**
     * The result of the aggregation
     * @return 
     */
    public double getResult();
    
    /**
     * To set the result of the aggregation to the curr result
     * 
     * @param currResult 
     */
    public void setResult(double currResult);  
}
