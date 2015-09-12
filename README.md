Aggregation Example

The knowledged reader may think 'Why not just using Spark here?'. Indeed, you could do exactly the same in Hadoop or with focus on Real-time processing in Spark. But this example should just show you a more lightweighted version which is not using additional frameworks here.

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

We also need to define a Source to retrieve the data from. A source provides records. It's important to allow to allow multiple sources in order to fill a stream for further processing

* Source
 * e.g. 

The stream

Based on this we need to define what the Id of the record is. Therefore a Map functio is used which returns a String. So the record is mapped to an Id which is a string:

* Id (Record)

Based on this Id and other properties we want to perform an aggregation. For this purpose an aggregation function is required which 

* Aggregate
* AggregationFunction

