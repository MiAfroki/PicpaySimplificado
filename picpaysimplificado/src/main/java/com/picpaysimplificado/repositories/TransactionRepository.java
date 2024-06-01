package com.picpaysimplificado.repositories;

import com.picpaysimplificado.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

//Repositório que vai fazer a manipulação da tabela de transações
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
