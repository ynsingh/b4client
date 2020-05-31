# Components in the client software.

### 2020/04/26

While, going through ClientMain.java, I found that it can be rewritten in more
structured fashion. I found after the date\_time check, and certificate
verification, following components should be instantiated as objects and their
object reference is kept in GlobalObject.

The GlobalObject instance is
instantiated in main entry of the program (authenticate/ClientMain.java) and its
object reference is maintained in ClientMain object. There is also a small loop
with sleep period of 60 seconds in the end of ClientMain.java. The loop exits
when the running status is set
to false by GUI.

These components have threads running which can pass messages to each other for
implementing all possible use case scenarios. Some objects will simply
represent objects as abstraction of configuration and do not require independent
thread running. **Each thread will also check that if it is running without doing
any task, then it should sleep for some *T* seconds. If it is running for
consecutively for *i* loops without doing any task, then it will sleep for *2^i
T* period. In case, a task is found, then *i* will be reset to *0*.**

One important point is the use of *synchronized blocks to avoid race condition*.
This should be used specially when a variable or datastructure in an object is
modified.
As multiple threads can try accessing same data structure through same function.
Most of the API calls to service objects running threads, should be enclosed in
synchronized, to avoid deadlocks. Whatever group of instruction has to be
executed as an atomic step, should be put in synchronized block.

Unfortunately, this has not been taken care of in the current code. It may show
sometime inconsistency due to race condition and will have tremendous bearing on
the performance. This will be typically problem with input task Queues of
service object.

Each service component should be considered as thread which takes up tasks from
the input queue and updates its on data structures and generates task which are
dispatched to input queues of the other objects running their own
service thread.

For service object, interface should be defined. The objects should implement
the interface. Global object should define the placeholder for object reference
to service object by way of interfaces. Similarly, all service objects should
use interface of Global object as mechanism to keep placeholder for Global
Object.

1. RTManager (threaded loop) - It will do periodic exchange with neighbours,
trigger merge operations. Input queue will contain routing table updates
received from neighbours.

   - Message _RTUpdate_ from _CommMgr_,  Action: merge into DHTable, Timer for
each updated entry timer reset
   - Event _Update Timer_ expires, Action: Dispatch The DHT Routing Table to all
neighbour in staggerred fashion, _update timer_ reset. 


2. DHTRouter (threaded loop) - It will use DHTable of appropriate layer and
decide the next node to whom the message is to be forwarded. Input queue will
contain messages to be routed. Message type received/send

   - _Query_ from **CommMgr** or any other component, Action: Send to next hop
     through **CommMgr**, If current node is root node, send to **IndexingMgr** 
   - _QResponse_ from **IndexingMgr**, Action: Send to next hop through
     **CommMgr**
   - _QResponse_ from **CommMgr**, Action: Send to next hop through **CommMgr**,
if the current node is root node, depending on type of response, send to
appropriate component.
   - _Publish_. from **CommMgr**, Action: Send to next hop through **CommMgr**,
     if the current node is root node, send to **IndexingMgr** for adding to
index.

3. DHTable (threaded loop) - Maintain the data structure of DHTable for all
layers. The data structure will contain endpoint address of all the nodes in the
table alongwith the transport, and direct/proxy mode to be used. Input queue
will be timer based events to purge the table entry, transmission of pings to
the neigbours for heart beat message. Message type send
   - _Heartbeat Timer_ expires, Action: If the routing table did not changed since last
     heartbeat,  send _HeartBeat_ to neighbours through **CommMgr**, if there is
change in routing table, send the _RoutingTable_ to all neighbours through
**CommMgr**
   - _Entry Purge Timer_ expires, purge the corresponding entry.

   The above thread should handle multiple DHT Layers. So, number of DHTables should
be equal to number of layers.

4. SpilloverTable (threaded loop) - Spillover table manages the range of
hashIDs to next node for forwarding the query. Thread takes care of timeouts
based removal. 

5. UI (threaded loop) - to display outputs and take inputs from user. It will
also play media outputs, captures media inputs and dispatches it to mediabridge.
It will also take user inputs and send messages to various componenets for
making system work as per user's wish.

6. CommMgr (threaded loop) - All communications with all possible transport will
be received, and queued up for various components to retrieve the messages from
buffer. Message type received
- _HeartBeat_ from other Nodes, response _Acknowledgement_ to other nodes.

7. MulticastManager (threaded loop) - Publishing/removing the forwarder entry in
DHT. Managing the MediaBridge forwarding and mixing table. Communicating with
the parent, grandparent and children nodes in overlaid multicast topology. It
will also set the routing information and media translation configuration in
media bridge.

7. Media Bridge (threaded loop) - receives the feed from multiple sources, fuses
them to correct differntial delay, forwards them to children nodes, forwards a
stream to UI for playback.

8.  ProxyRouter (threaded loop) - Will maintain the table of nodes (behind NAT/
firewall/ http\_proxies) on whose behalf it will receive the messages from
others. These will be sent to CommMgr, from where the messages will be pushed to
nodes on thier setup connections or pulled by the nodes via http.

9. IndexingMgr (threaded loop) - Stores (key, value) pairs for which the current
node is root node or [proxyroot][EE698C Lecture-2]. It receives query and publish
messages and responds after searching database/filesystem. When sending the
response, it also send the key,value pair to the previous node from which the
key query was received for caching. It republishes when republish timer expires
and removes the entry after sometime. In case of the entry is stored as
proxyroot, the root node is informed of the entry timer expiry. If the timer
reset is not received within a specified time, then the entry is republished.
The entry is removed when removal timer expires. In case of shortage of space as
root node, the thread searches for proxyroot nodes, and creates the spillover
table. The thread also Searches for backup nodes and transfer the stored entries
to it. It also monitor root nodes for which it is backup. When a monitored root
node is dead, it republishes all the backedup entries. It can work with multiple
layers simultaneously. In storage layer, it will store the file fragments.

In case, the query is for endpoint address, and the hash ID matches the current
node, the endpoint address for receiving the messages is sent via DHT message.

In case, the query is for multicast grop forwarders, the list of endpoint
addresses, transport is sent via DHT message to source node.

10. Key cache \(threaded loop\) - Store the key,value sent from the other nodes.
Each entry will have timer associated. The entry should be purged after the
timer expires. In case response is sent back to query source, a copy is sent to
previous node from which query was received for cacheing.

11. Search Engine (threaded loop) - stores local content which is put for public
consumption. The crawler is also run by this thread which creates the index for
all the keys stored in key list. All keys will have timer (may of few months).
Whenever a key is used, the timer is reset. Any content added to storage is
indexed via crawler for the store keys. Any new keys when received, all the
content is search for this key and index updated.

12. Content Cache (threaded loop) - The response to the content search is always
passed to previous node from which query arrived. All such content received is
cached. If the content is served from local cache, the content is again pushed
to previous node from which content query received. This way popular content
gets spread in the whole network and become readily available for large number
of users.

13. Configuration (singleton object) - abstracts the configuration file of the
client as a object. It can be used by any component. Any changes in
configuration is also pushed to local file system.

14. Broadcast/RandomWalk router (threaded loop) - used for search engine
functionality using unstructured search. Also implements broadcast routing,
random walk routing with TTL (time to live field). When a broadcast packet is
droped due to TTL=0, it sends its endpoint address to source. It will work as
edge node which can be used by source to initiate the second round of expanded
search.

15. Global Object (singleton object) - Keeps all status variable. Used to
control various threads. 

[EE698C Lecture-2]: https://youtu.be/Pf_1JFmKOCg
