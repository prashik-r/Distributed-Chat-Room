# Distributed-Chat-Room

The project mainly focuses at implementing a distributed chat facility which would be able to handle multiple clients in the chatroom. Alongside numerous other features are implemented in the project to enrich user experience in the chat room .

## Technology used 

```
Language : Java
Database : MySQL
Protocol : Java RMI for RPC
```

## Chat Room Features 
- Create a group as admin.
- Join/Leave a group.
- One to One chat facility.
- Group chat facility.
- Admin privileges to accept/reject group joining request by a user.

## MySQL Database Schema


| Table : Info | id (varchar)  | password (varchar)  |
| ------- | --- | --- |


| Table : Persongroup | group name (varchar)  | person (varchar)|admin(int) |
| ------- | --- | --- | --- |

| Table : Requests | id (varchar)  | group name (varchar)  |
| ------- | --- | --- |
 
**Table Info** :
This table consists of the usernames and passwords of the people
to login to the system developed.

**Table Persongroup** :
It consists of the group details, it has name of the group , the
persons present in the group and also the information of admin of the
group.

**Table Requests** :
The requests keeps tracks of the requests made by a user to enter
a particular group , it has user id and the group name requested for.

## Steps to Run

- Open `JavaLibrary1/src/lib1/Javaconnect.java` and edit the following parameters to connect to the MySQL database.
    - Connection String
    - Username
    - Password

- Import all the project into the NetBeans application. ( Namely : `ChatGUI`, `JavaLibrary1`, `RMIMainServer`, `RMIServer1` ).

- Run the following projects in the given below manner to execute the program :

    * **RMIServer1:** Run File `RMIServer1.java`	
(\RMIServer1\src\rmiserver1\RMIServer1.java)
    * **RMIServer2:** Run File `RMIServer2.java`	
(\RMIServer1\src\rmiserver1\RMIServer2.java)
    * **RMIServer3:** Run File `RMIServer3.java`	
(\RMIServer1\src\rmiserver1\RMIServer3.java)
    * **RMIMainServer:** Run File `RMIMainServer.java`.	
(\RMIMainServer\src\rmimainserver\RMIMainServer.java)
    * **ChatGUI:** Run File `ChatGUI.java`.		
(\ChatGUI\src\chatgui\ChatGUI.java)
    

## Working 

### Architecture 
The architecture consists of the following components :
- Main server
- Side Server
- Client

#### Main Server
The main server contains the meta data . It has the
information of which side server handles which client.The
Main Server is responsible to implement the overall
execution of the distributed chat facility , it is the job of
main server to connect the clients(users) to the side
servers which eventually connects to the main server to
exchange the messages.

#### Side Server
The side servers acts as a intermediates between the client
and the main server . Each side server is one to one
connected to the clients and helps the main server to
identify each client by coordinating the messages given by
the clients in group chat system.Also order of messages is
maintained by main server using the side servers to update
their clients message box each time a user sends any
message.

#### Client
The client takes care of the message sending and receiving
utilities by the user . One client per user maintains chat
record of the user and coordinates with the side server to
connect to the group chat implemented by the main server.

**The client first requests the main server which assigns a side server to the client , thus the main server contains the information of each client and its group and also the one to one mapping of the side server to client . The overall message order and the sending and receiving of the messages is handled by the main server. Each client is further handled by the respective server and thus all are coordinated to successfully implement the group chat feature by the main server.**

### One to one chat feature
One to one chat is implemented as a special case of group
chat with 2 users , the client sends messages to the assigned
side server which takes the message to the main server , the
main server sends the messages to the side server of the
client to which message has to be sent. Finally the side server
sends the message to the assigned client.

### Group chat and Message ordering
To implement the group chat multiple servers are one to one
connected to the main server by the process discussed earlier . At
each point a user sends a message in a group chat and the messages is
delivered to the main server by the respective side server , the main
server sends the messages to each client in the group through the
respective side servers.Also , to maintain the order of messages has
been timestamped with the first server contact(using server's time) to
ensure the same order to all clients.
