Partial state transfer example
==============================

This example demonstrates fine-grained replication for objects large in size when
only certain attributes are changes during an ongoing transaction. Only these
changes will be replicated to other nodes in the cluster, not the whole object.

To compile, type:

    `mvn clean compile dependency:copy-dependencies -DstripVersion`
     
and then, to run (in two different terminals):

    `java -cp target/classes:target/dependency/* org.infinispan.examples.partialreplication.Node0` 

and

    `java -cp target/classes:target/dependency/* org.infinispan.examples.partialreplication.Node1`


Node0 application will listen for changes to a bicycle, Node1 will make these changes.


A typical usage scenario is as follows:
---------------------------------------

1. start node0
2. start node1
3. type "p" in node0 to see current bicycle components
4. type "u" in node1 to update some of the bicycle components (only the frame and fork components 
   will be changed) 
5. look at the console of node0 -> you should see "Updated components: fork, frame"
6. type "p" in node0 to see changed components  

