package GestionBanque.G9.model;

import GestionBanque.G9.enums.OperationType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountOperation {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateOperation;

    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

     @ManyToOne
    private BankAccount bankAccount;

    private String description;

}
