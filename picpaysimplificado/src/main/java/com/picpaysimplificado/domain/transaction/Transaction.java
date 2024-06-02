package com.picpaysimplificado.domain.transaction;

import com.picpaysimplificado.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//Transactions pois transaction é uma palavra reservada
@Entity(name="transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Qual foi o valor da transação, quanto foi transferido de um usuário para outro
    private BigDecimal amount;

    //Referência de quem foi o sender e quem foi o receiver (ManyToOne = 1 usuário pode ter várias transações, mas uma transação só pode ter um sender e um receiver
    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name="receiver_id")
    private User receiver;

    //Quando foi realizada a transação
    private LocalDateTime timestamp;
}
