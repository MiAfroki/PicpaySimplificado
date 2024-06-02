package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    //Declarando as dependências dessa classe
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    //A classe RestTemplate é oferecida pelo Spring para fazer comunicações HTTP entre serviços (simplifica bastante as chamadas HTTP) e através dela podemos fazer chamadas HTTP tipo get, post, put)
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    //Método público chamado para criar uma transação que vai receber o parâmetro Transaction (TransactionDT0 é os dados que eu vou receber lá no payload quando o usuário fizer a requisição de POST (quando fizeram a requisição de POST /transaction para o meu servidor enviando os dados da transação vou receber o payload) e ai a classe transaction vai receber o payload para fazer a criação da transação e fazer as validações necessárias)
    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if(!isAuthorized){
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount((transaction.value()));
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        //Parte onde o dinheiro sai da conta de quem mandou e vai pra quem precisa receber
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender,"Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver,"Transação recebida com sucesso");

        return new Transaction();
    }

    public boolean authorizeTransaction(User sender, BigDecimal value){
       ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

       if(authorizationResponse.getStatusCode() == HttpStatus.OK){
           String message = (String) authorizationResponse.getBody().get("message");
           //Aqui será onde vai ser comparado a String Autorizado com o que vier na mensagem
           return "Autorizado".equalsIgnoreCase(message);
       } else return false;
    }
}
