package com.picpaysimplificado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Criação do repositório de usuários
public interface UserRepository extends JpaRepository<User, Long> {
    //Definição do método que vai os usuários pelo documento, o optinal é por conta de não ser certo se vai sempre retornar um usuario (o JPA já monta a query de acordo com o findUserByDocument)
    Optional<User>findUserByDocument(String document);

    Optional<User>findUserById(Long id);
}
