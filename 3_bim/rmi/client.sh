echo "-------------------------------------------"
echo "Cliente rodando"
echo "-------------------------------------------"
java -Djava.security.policy=java.policy  TicketClient rmi://localhost:1099/ticket
