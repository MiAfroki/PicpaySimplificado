package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

//@Service é uma anotação para indicar para Spring que é uma classe de serviço para ele conseguir fazer as injeções de dependências da forma correta
@Service
public class UserService {
    //Vai precisar utilizar o Autowired para que o Spring consiga fazer a injeção desse Repository
    @Autowired
    //A classe precisa do acesso ao UserRepository para pegar os usuarios e fazer essa manipulação
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        //Se o tipo de usuário for lojista
        if(sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuário do tipo Lojista não está autorizado a realizar transação");
        }
        //Fazendo a comparação para saber se o usuário possui saldo para realizar essa transação
        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Saldo insuficiente");
        }
    }

    //Método que o transaction service só vai utilizar essa classe para fazer a manipulação dos usuários
    public User findUserById(Long id) throws Exception {
        //Se o usuário não for encontrado lançaremos essa exceção
       return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.saveUser((newUser));
        return newUser;
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }

    //Método para criar um novo usuário, salvar ele no banco de dados, fazer a persistência desse usuário (persistir as alterações no usuário, depois que atualizar o balance do usuário na transaction queremos persistir isso no banco de dados e isso é feito através desse método saveUser)
    public void saveUser(User user){ this.repository.save(user); }
}
