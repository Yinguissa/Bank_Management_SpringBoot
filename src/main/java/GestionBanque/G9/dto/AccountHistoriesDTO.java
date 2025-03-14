package GestionBanque.G9.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoriesDTO {

     private String accountId;
     private double balance;
     private int currentPage;
     private int totalPages;
     private int pageSize;
     private List<AccountOperationDTO> accountOperationDTOList;
}
