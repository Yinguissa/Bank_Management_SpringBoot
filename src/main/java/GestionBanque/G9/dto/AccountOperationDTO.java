package GestionBanque.G9.dto;


import GestionBanque.G9.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {


    private Long id;
    private Date dateOperation;
    private double amount;
    private OperationType operationType;
    private String description;
}
