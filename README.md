# Aggregation Example

## Requirements

The purpose here is to:

* Retrieve data from a source stream
* Stream process by aggregating based on an identifier

* e.g.:
 * A user is identified by his 'uid'
 * Identifier is the uid of the user or the session id of a user
 * Count how often the user performed a request

## Implementation Idea

So we need to define how the retrieved record is looking like in the first step (Let's call this a Schema):

* Record
 * Prop 1 : Type_1
 * ...
 * Prop n : Type_n

We also need to define a Source to retrieve the data from. A source provides records.

* Source
 * e.g. FileSource

It's just important that the records those are provided by the Source are following the schema. In our example we will just read from a file and fill our stream from there.

Based on this we need to define what the Id of the record is. Therefore a Map function is used which enriches the Record with an Id. For our example an Id is a String.

* Record MapFunc(Record r)

Based on this Id and other properties we want to perform an aggregation. For this purpose a Reduce function is required. The Reduce Function returns in our example everytime a numeric result. The aggregation result is stored in Couchbase based on the  previously determined record identifier and an aggregation identifier.

* Aggregate
 * String aggrId
 * String recordId
 * Double result

* Aggregate ReduceFunc(Aggregate old, Record record)

## Example

The following example shows how records of the format

```
dmaier;123-456-789;0.5
dmaier;123-456-789;0.3
dmaier;123-456-789;0.4
dmaier;123-456-789;0.3
dostrovsky;123-456-789;0.2
dostrovsky;123-456-789;0.3
dostrovsky;123-456-789;0.3
dostrovsky;123-456-789;0.5
```

are counted.

```java
        ISchema schema = new Schema();
        schema.add("uid", Schema.TYPE_STRING);
        schema.add("token", Schema.TYPE_STRING);
        schema.add("elapsed", Schema.TYPE_NUM);
        
        FileSource fs = new FileSource(TestConstants.FILE, schema);
                
        //Perform the aggregation
        fs.retrieve().map(r -> new IdentifyByUidMapFunc().map(r))
                     .flatMap(r -> new CBCountReduceFunc().reduce(r))
                     .toBlocking()
                     .last();
```

The counting happens in parallel. The synchronization happens implicitely by using Couchbases optimistic concurrency handling (CAS). The final aggregation result is stored in Couchbase. Here an example of retrieving the number of entries for the user 'dmaier' and 'dostrovsky':

```
        //Read the aggregation result for 2 record id-s
        IAggregate count_dmaier = new CBAggregate(CBCountReduceFunc.AGGR_ID, "dmaier")
                .get()
                .toBlocking()
                .single();
        
        IAggregate count_dostrovsky= new CBAggregate(CBCountReduceFunc.AGGR_ID, "dostrovsky")
                .get()
                .toBlocking()
                .single();
        
        LOG.log(Level.INFO, "count_maier = {0}", count_dmaier.getResult());
        LOG.log(Level.INFO, "count_dostrovsky = {0}", count_dostrovsky.getResult());
```
