package GestionBanque.G9.model;


import GestionBanque.G9.enums.AccountStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)


@DiscriminatorColumn(name = "TYPE",length = 4, discriminatorType = DiscriminatorType.STRING)
//on ajoute abstract pour ne pas que jpa crée la table bankAccount dans la base
@Entity
public  abstract class  BankAccount {
    @Id
  //  @Column(name = "count-number")
    private String id;

    private double balance;

    private Date createdAt;
    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "bankAccount",fetch = FetchType.LAZY, cascade={CascadeType.ALL})
    //@JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<AccountOperation> accountOperations;

}
