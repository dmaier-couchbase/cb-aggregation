# Aggregation Example

The knowledged reader may think 'Why not just using Spark with Couchbase here?'. Indeed, you could do exactly the same in Hadoop (batch oriented) or with focus on Real-time processing in Spark. But this example should just show you a more lightweighted version which is not using additional frameworks here by working directly with Couchbase's Client Library.

The purpose here is to retrieve data from a source, stream process by aggregating based on an identifier.

* e.g.:
 * User logs in
 * Identifier is the uid of the user or the session id of a user
 * Count how often the user performed a request

So we need to define how the retrieved record is looking like in the first step (Let's call this an InputSchema):

* Record
 * Prop 1 : Type_1
 * ...
 * Prop n : Type_n

We also need to define a Source to retrieve the data from. A source provides records. It's important to allow to combine multiple sources in order to fill a stream for further processing.

* Source
 * e.g. FileSource

The stream (to be processed) is then filled with records from multiple sources. It's just important that they follow the same schema. In our example we will just read from multiple files in parallel and fill an Observable from there.

Based on this we need to define what the Id of the record is. Therefore a Map function is used which enriches the Record with an Id which is a String. So the record is mapped to an Id which is a string:

* IdentifiedRecord MapFunc(Record r)

Based on this Id and other properties we want to perform an aggregation. For this purpose a Reduce function is required. The Reduce Function returns in our example everytime a numeric result (as part of the Aggregate). The aggregation result is stored in Couchbase based on the  previously determined record identifier and an aggregate identifier.

* Aggregate
 * String aggrId
 * String recordId
 * Double result

* Aggregate ReduceFunc(aggrId, recordId)

