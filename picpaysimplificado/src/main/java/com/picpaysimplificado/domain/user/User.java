package com.picpaysimplificado.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.usertype.UserType;

import java.math.BigDecimal;

//Não pode colocar user pois é uma palavra reservada normalmente dos bancos de dados cicle
@Entity(name="users")

//Para poder dizer qual o nome da tabela que esta entidade está representando
@Table(name="users")

@Getter
@Setter

//Cria um construtor que recebe todos os parametros da classe:
@AllArgsConstructor

//Chave primaria da entidade(tabela):
@EqualsAndHashCode(of="id")

public class User {
    //Declarando os campos que vão ter dentro do usuário
    @Id

    //Estratégia para geração desse valor, é gerado pelo nosso sistema e podemos colocar qual a estratégia dessa geração (GenerationType.IDENTITY: Vai fazer com que gere de forma incremental (ID: 1, 2, 3, 4, 5...) Não é a forma mais segura porém vamos começar desta forma, pois depois podemos mudar e melhorar a modelagem de dados
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //Meu id é um atributo privado dessa classe e é do tipo: Long
    private Long id;

    //Parte que vai formar o nome e o sobrenome do usuário:
    private String firstName;
    private String lastName;

    //Parte do documento (CPF) que precisará ser único
    @Column(unique=true)
    private String document;

    //Parte do email que precisará ser único também
    @Column(unique = true)
    private String email;

    //Parte da senha que não iremos utilizar pois não haverá autenticação
    private String password;

    //Decimal grande que será o balance que representará o saldo do usuário
    private BigDecimal balance;
    //Anotação para dizer que esse valor representa um desses valores do Enum (ou MERCHANT ou o COMMON)
    @Enumerated(EnumType.STRING)

    //Campo para criar o tipo de usuário
    private UserType userType;

}
