README
Programming Assignment 1
Alessandra Poblador, acp2164
========================================================================

a. Brief description of code
------------------------------------

Contents:
Server.java
serverThread.java
Client.java
clientWriter.java
Account.java
BlockedIP.java
make.sh
user_pass.txt

b. Details on development environment
------------------------------------

Written in Java 1.6
Built in Eclipse IDE

c. Instructions on how to run
------------------------------------

First run makefile:

> ./make.sh

To run Server:

> java Server [portNumber]

ex:
> java Server 8888

To run Client in a new terminal window:

> java Client [IP] [portNumber]

ex:
> java Client localhost 8888

When prompted with "Enter a command:", you may run the commands:
- whoelse
- wholasthr
- broadcast
- message
- logout
... as specified in the HW document.

d. Sample commands
------------------------------------

Server:
> java Server 8888

Client 1:
> java Client localhost 8888
> Username: columbia
> Password: 116bway
> whoelse

Client 2:
> java Client localhost 8888
> Username: foobar
> Password: passpass
> message columbia hi