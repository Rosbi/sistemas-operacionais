echo "-------------------------------------------"
echo "Servidor rodando agora abra um novo terminal e rode ./client.sh"
echo "-------------------------------------------"
java -Djava.security.policy=java.policy  TicketServerImpl rmi://:1099/ticket
