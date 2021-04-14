#!/bin/bash

javac src/TicketClient.java
mkdir public_html
cp ../TicketServer.class ../TicketServerImpl_Stub.class ./public_html/
javac src/TicketServer.java src/TicketServerImpl.java -Xlint
rmic ../TicketServerImpl
javac src/TicketClient.java
cp TicketServer.class TicketServerImpl_Stub.class ./public_html/
echo "-------------------------------------------"
echo "RMI Pronto, agora abra um novo terminal e rode ./server.sh"
echo "-------------------------------------------"
rmiregistry